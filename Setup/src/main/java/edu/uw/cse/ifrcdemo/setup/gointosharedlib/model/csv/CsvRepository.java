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

package edu.uw.cse.ifrcdemo.setup.gointosharedlib.model.csv;

import edu.uw.cse.ifrcdemo.sharedlib.model.row.BaseSyncRow;
import edu.uw.cse.ifrcdemo.sharedlib.model.row.UntypedSyncRow;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * CsvRepository is an interface that defines methods for reading CSV data
 * into typed and untyped sync row objects. It provides functionality for
 * both synchronous and asynchronous reading of CSV files, as well as
 * methods to retrieve indexed versions of the data.
 *
 * @author [Your Name]
 * @version 1.0
 * @since [The release or version this class was introduced]
 */
public interface CsvRepository {
  /**
   * Reads an untyped CSV file asynchronously.
   *
   * @param filename The name of the CSV file
   * @param input The InputStream containing the CSV data
   * @param purgeCache Whether to purge the cache before reading
   * @return A CompletableFuture that resolves to a List of UntypedSyncRow objects
   */
  CompletableFuture<List<UntypedSyncRow>> readUntypedCsv(String filename, InputStream input, boolean purgeCache);

  /**
   * Retrieves untyped CSV data synchronously.
   *
   * @param filename The name of the CSV file
   * @return An Optional containing a List of UntypedSyncRow objects, or empty if not found
   */
  Optional<List<UntypedSyncRow>> readUntypedCsv(String filename);

  /**
   * Reads a typed CSV file asynchronously.
   *
   * @param <T> The type of BaseSyncRow
   * @param clazz The Class object representing the type T
   * @param input The InputStream containing the CSV data
   * @param purgeCache Whether to purge the cache before reading
   * @return A CompletableFuture that resolves to a List of BaseSyncRow objects
   */
  <T extends BaseSyncRow> CompletableFuture<List<? extends BaseSyncRow>> readTypedCsv(Class<T> clazz,
                                                                                      InputStream input,
                                                                                      boolean purgeCache);

  /**
   * Retrieves typed CSV data synchronously.
   *
   * @param <T> The type of BaseSyncRow
   * @param clazz The Class object representing the type T
   * @return An Optional containing a List of T objects, or empty if not found
   */
  <T extends BaseSyncRow> Optional<List<T>> readTypedCsv(Class<T> clazz);

  /**
   * Retrieves indexed untyped CSV data synchronously.
   *
   * @param filename The name of the CSV file
   * @return An Optional containing a Map of String keys to UntypedSyncRow objects, or empty if not found
   */
  Optional<Map<String, UntypedSyncRow>> readIndexedUntypedCsv(String filename);

  /**
   * Retrieves indexed typed CSV data synchronously.
   *
   * @param <T> The type of BaseSyncRow
   * @param clazz The Class object representing the type T
   * @return An Optional containing a Map of String keys to T objects, or empty if not found
   */
  <T extends BaseSyncRow> Optional<Map<String, T>> readIndexedTypedCsv(Class<T> clazz);
}
