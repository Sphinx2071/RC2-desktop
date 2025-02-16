/*
 * Copyright (c) 2016-2022 University of Washington
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *  Neither the name of the University of Washington nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE UNIVERSITY OF WASHINGTON AND CONTRIBUTORS “AS IS” AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE UNIVERSITY OF WASHINGTON OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package edu.uw.cse.ifrcdemo.setup.gointosharedlib.util;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import edu.uw.cse.ifrcdemo.setup.gointosharedlib.logic.FileInputStreamSupplier;
import edu.uw.cse.ifrcdemo.setup.gointosharedlib.model.csv.CsvRepository;
import edu.uw.cse.ifrcdemo.sharedlib.model.datattype.Module;
import edu.uw.cse.ifrcdemo.sharedlib.model.db.HealthMobileDbModel;
import edu.uw.cse.ifrcdemo.sharedlib.model.db.ReliefMobileDbModel;
import edu.uw.cse.ifrcdemo.sharedlib.model.row.BaseSyncRow;
import edu.uw.cse.ifrcdemo.sharedlib.model.stub.HasCustomTable;
import edu.uw.cse.ifrcdemo.sharedlib.util.FileUtil;
import edu.uw.cse.ifrcdemo.sharedlib.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// TODO: change this name

/**
 * CsvFileUtil is a utility class for handling CSV file operations related to sync rows and custom tables.
 * It provides methods for checking required files, reading base tables, and processing range CSV files.
 *
 * @author [Your Name]
 * @version 1.0
 * @since [The release or version this class was introduced]
 */
public class CsvFileUtil {
  /**
   * Checks a directory for required files based on the module type.
   *
   * @param dir The directory path to check
   * @param moduleType The type of module (RELIEF or HEALTH)
   * @return A List of String containing names of missing files
   * @throws IllegalArgumentException if an invalid module type is provided
   */
  public static List<String> checkDirForRequiredFiles(Path dir, Module moduleType) {
    Stream<Class<? extends BaseSyncRow>> stream = null;

    if(moduleType == Module.RELIEF) {
      stream = ReliefMobileDbModel.tableDefinition.stream();
    } else if (moduleType == Module.HEALTH) {
      stream = HealthMobileDbModel.tableDefinition.stream();;
    } else {
      throw new IllegalArgumentException("RC2 module at csv");
    }

    return stream.map(FileUtil::getFileName)
        .filter(x -> Files.notExists(dir.resolve(x)))
        .collect(Collectors.toList());
  }

  // TODO: move to CsvRepository?
  /**
   * Reads a base table with a custom table for entities that implement BaseSyncRow and HasCustomTable.
   *
   * @param <T> The type of the entity, extending BaseSyncRow and implementing HasCustomTable
   * @param csvPath The path to the CSV file
   * @param tableClass The class of the entity
   * @param repo The CsvRepository to use for reading
   * @return A CompletableFuture<Void> representing the completion of the read operation
   */
  public static <T extends BaseSyncRow & HasCustomTable> CompletableFuture<Void> readBaseTableWithCustomTable(
      Path csvPath, Class<T> tableClass, CsvRepository repo) {
    Objects.requireNonNull(csvPath);

    return readBaseTableWithCustomTable(
        csvPath,
        tableClass,
        row -> row.getCustomTableFormId(),
        repo
    );
  }

  /**
   * Reads a base table with a custom table, using a custom function to extract the table form ID.
   *
   * @param <T> The type of the entity, extending BaseSyncRow
   * @param csvPath The path to the CSV file
   * @param tableClass The class of the entity
   * @param customTableExtractor A function to extract the custom table form ID from the entity
   * @param repo The CsvRepository to use for reading
   * @return A CompletableFuture<Void> representing the completion of the read operation
   */
  public static <T extends BaseSyncRow> CompletableFuture<Void> readBaseTableWithCustomTable(
      Path csvPath, Class<T> tableClass, Function<T, String> customTableExtractor, CsvRepository repo) {
    Objects.requireNonNull(csvPath);

    return readBaseTableWithCustomTable(
        str -> new FileInputStreamSupplier(FileUtil.getPathToCSV(csvPath, str)),
        tableClass,
        customTableExtractor,
        repo
    );
  }

  /**
   * Reads a base table with a custom table, using a function to supply input streams.
   *
   * @param <T> The type of the entity, extending BaseSyncRow
   * @param inputStreamSupplier A function that supplies input streams for given file names
   * @param tableClass The class of the entity
   * @param customTableExtractor A function to extract the custom table form ID from the entity
   * @param repo The CsvRepository to use for reading
   * @return A CompletableFuture<Void> representing the completion of the read operation
   */
  public static <T extends BaseSyncRow> CompletableFuture<Void> readBaseTableWithCustomTable(
      Function<String, Supplier<InputStream>> inputStreamSupplier,
      Class<T> tableClass,
      Function<T, String> customTableExtractor,
      CsvRepository repo) {
    Objects.requireNonNull(inputStreamSupplier);

    String filename = FileUtil.getFileName(tableClass);

    return readBaseTable(inputStreamSupplier.apply(filename), tableClass, repo)
        .thenComposeAsync(__ -> CompletableFuture
            .allOf(
                repo
                    .readTypedCsv(tableClass)
                    .orElseThrow(IllegalStateException::new) // should not happen
                    .stream()
                    .map(customTableExtractor)
                    .distinct()
                    .filter(StringUtil::isNotNullAndNotEmpty)
                    .map(formId -> CompletableFuture
                        .supplyAsync(inputStreamSupplier.apply(formId))
                        .thenCompose(stream -> repo.readUntypedCsv(FileUtil.getFileName(formId), stream, true))
                    )
                    .toArray(CompletableFuture<?>[]::new)
            )
        );
  }

  // TODO: move to CsvRepository?
  /**
   * Reads a base table from a CSV file.
   *
   * @param <T> The type of the entity, extending BaseSyncRow
   * @param csvPath The path to the CSV file
   * @param tableClass The class of the entity
   * @param repo The CsvRepository to use for reading
   * @return A CompletableFuture<Void> representing the completion of the read operation
   */
  public static <T extends BaseSyncRow> CompletableFuture<Void> readBaseTable(
      Path csvPath, Class<T> tableClass, CsvRepository repo) {
    Objects.requireNonNull(csvPath);

    String filename = FileUtil.getFileName(tableClass);

    return readBaseTable(new FileInputStreamSupplier(FileUtil.getPathToCSV(csvPath, filename)), tableClass, repo);
  }

  /**
   * Reads a base table using an input stream supplier.
   *
   * @param <T> The type of the entity, extending BaseSyncRow
   * @param inputStreamSupplier A supplier of input streams for the CSV data
   * @param tableClass The class of the entity
   * @param repo The CsvRepository to use for reading
   * @return A CompletableFuture<Void> representing the completion of the read operation
   */
  public static <T extends BaseSyncRow> CompletableFuture<Void> readBaseTable(
      Supplier<InputStream> inputStreamSupplier, Class<T> tableClass, CsvRepository repo) {
    Objects.requireNonNull(inputStreamSupplier);
    Objects.requireNonNull(tableClass);
    Objects.requireNonNull(repo);

    String filename = FileUtil.getFileName(tableClass);

    return CompletableFuture.allOf(
        CompletableFuture
            .supplyAsync(inputStreamSupplier)
            .thenCompose(stream -> repo.readTypedCsv(tableClass, stream, true)),
        CompletableFuture
            .supplyAsync(inputStreamSupplier)
            .thenCompose(stream -> repo.readUntypedCsv(filename, stream, true))
    );
  }

  /**
   * Reads a range CSV file and returns a list of integers.
   *
   * @param csvFile The File object representing the CSV file
   * @return A List of Integer values read from the CSV file, or null if the file is null
   */
  public static List<Integer> readRangeCsv(File csvFile) {
    if (csvFile == null) {
      return null;
    }

    List<String[]> values = null;
    CsvMapper mapper = new CsvMapper().enable(CsvParser.Feature.WRAP_AS_ARRAY);

    try {
      MappingIterator<String[]> iterator = mapper.readerFor(String[].class).readValues(csvFile);
      values = iterator.readAll();
    } catch (IOException e) {
      e.printStackTrace();
    }


    return values.stream()
        .map(value -> {
          if (value.length > 0)
            return Integer.parseInt(value[0]);
          else {
            return null;
          }
        })
        .filter(value -> value != null)
        .collect(Collectors.toList());
  }
}
