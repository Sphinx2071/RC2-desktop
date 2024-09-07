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

import edu.uw.cse.ifrcdemo.sharedlib.consts.GenConsts;
import edu.uw.cse.ifrcdemo.sharedlib.consts.VersionConsts;
import edu.uw.cse.ifrcdemo.sharedlib.model.config.AuxiliaryProperty;
import io.github.soc.directories.ProjectDirectories;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * InternalFileStoreUtil is a utility class that manages file storage and profile handling for the application.
 * It provides methods for configuring file paths, managing profiles, and handling data import/export operations.
 *
 * @author [Your Name]
 * @version 1.0
 * @since [The release or version this class was introduced]
 */

public class InternalFileStoreUtil {
  public static final String DEFAULT_PROFILE = "default";

  public static final String PROFILES_PATH = "pf" + VersionConsts.getRc2ReleaseVersionForSentry();
  public static final String XLSX_STORAGE_PATH = "xlsx";
  static final String SNAPSHOT_PATH = "in";
  static final String OUTPUT_PATH = "out";
  static final String TEMP_PATH = "tmp";

  private static final String PACKAGE = "edu.uw.cse.ifrcdemo";
  private static final String APP_GROUP = "RC2";
  public static final String DATA_SUFFIX = "data";

  private static ProjectDirectories directories;
  private static String profileName;
  private static Path snapshotPath;
  private static Path outputPath;
  private static Path tempPath;

  private static String planningModuleName = null;
  private static SharedDataInstance dataInstance = null;

  /**
   * Configures the FileStoreUtil with a planning module name and shared data instance.
   *
   * @param name The name of the planning module
   * @param sharedDataInstance The shared data instance
   */
  public static void configFileStoreUtil(String name, SharedDataInstance sharedDataInstance) {
    planningModuleName = name;
    dataInstance = sharedDataInstance;
  }

  public static AuxiliaryProperty getAuxiliaryProperty() {
    return dataInstance.getAuxiliaryProperty();
  }

  private static ProjectDirectories getProjectDirectories()throws IOException {
    if(planningModuleName == null) {
      throw new IOException("InternalFileStoreUtil has not had it's config function called yet");
    }
    if(directories == null) {
      directories = ProjectDirectories.from(PACKAGE, APP_GROUP, planningModuleName);
      Path dataDir = Paths.get(directories.dataDir);
      if(dataDir.toString().endsWith(DATA_SUFFIX))
        Files.createDirectories(dataDir.getParent());
      else
        Files.createDirectories(dataDir);
    }
    return directories;
  }

  public static Path getProjectPath() throws IOException {
    Path dataDir = Paths.get(getProjectDirectories().dataDir);
    if(dataDir.toString().endsWith(DATA_SUFFIX))
      return dataDir.getParent();
    else
      return dataDir;
  }

  public static String getProfileName() {
    if(profileName == null) {
      profileName = DEFAULT_PROFILE;
    }
    return profileName;
  }

  public static void setProfileName(String newProfileName) {
    profileName = sanitizeProfileName(newProfileName);
  }

  private static String sanitizeProfileName(String newProfileName) {
    return newProfileName.replaceAll("\\W+", GenConsts.UNDERSCORE);
  }


  public static Path getCurrentSnapshotStoragePath() {
    return snapshotPath;
  }

  public static Path getCurrentOutputStoragePath() {
    return outputPath;
  }

  public static Path getCurrentTempStoragePath() {
    return tempPath;
  }

  public static Path getXlsxFormStoragePath() throws IOException{
    return getProfilePath().resolve(XLSX_STORAGE_PATH);
  }

  public static Path getProfilePath() throws IOException {
    return getProjectPath().resolve(PROFILES_PATH).resolve(getProfileName());
  }

  public static Path getProfileSnapshotPath() throws IOException {
    return getProfilePath().resolve(SNAPSHOT_PATH);
  }

  public static boolean createProfilePath(String newProfileName) throws IOException {
    String profileName = sanitizeProfileName(newProfileName);
    Path path = getProjectPath().resolve(PROFILES_PATH).resolve(profileName);
    return path.toFile().mkdirs();
  }

  public static List<String> getProfilesList() throws IOException {
    Path profileParentDir = getProjectPath().resolve(PROFILES_PATH);

    // Make sure that the default profile always exists
    if (!Files.exists(profileParentDir)) {
      createProfilePath(DEFAULT_PROFILE);
    }

    List<String> profileNames;
    try (Stream<Path> paths = Files.walk(profileParentDir, 1)) {
      profileNames = paths
          .filter(path -> !path.equals(profileParentDir))
          .filter(Files::isDirectory)
          .map(path -> path.getName(path.getNameCount() - 1).toString())
          .sorted(String::compareToIgnoreCase)
          .collect(Collectors.toList());
    }

    return profileNames;
  }

  /**
   * Reimports data from a selected directory into the current profile.
   *
   * @param selectedDirectory The File object representing the directory to import from
   * @return A CompletableFuture<Void> representing the completion of the import operation
   * @throws IOException If there's an error during the import process
   * @throws IllegalAccessException If the utility hasn't been properly configured
   */
  public static CompletableFuture<Void> reimportData(File selectedDirectory) throws IOException, IllegalAccessException {
    return importData(getProfileName(), selectedDirectory);
  }

  /**
   * Imports data from a selected directory into a specified profile.
   *
   * @param profileName The name of the profile to import into
   * @param selectedDirectory The File object representing the directory to import from
   * @return A CompletableFuture<Void> representing the completion of the import operation
   * @throws IOException If there's an error during the import process
   * @throws IllegalAccessException If the utility hasn't been properly configured
   */
  public static CompletableFuture<Void> importData(String profileName, File selectedDirectory) throws IOException, IllegalAccessException {
    if(dataInstance == null) {
      throw new IOException("InternalFileStoreUtil has not had it's config function called yet");
    }

    List<String> missingTables = CsvFileUtil.checkDirForRequiredFiles(selectedDirectory.toPath(), dataInstance.getModuleType());
    if (!missingTables.isEmpty()) {
      throw new FileNotFoundException("Missing " + String.join(", ", missingTables));
    }
    // clear old profile information
    setNoProfile();

    // setup new profile
    setProfileName(profileName);
    setCurrentSnapshotPath();
    FileUtils.copyDirectory(selectedDirectory, snapshotPath.toFile());

    return dataInstance.loadInputDataSource(getProfilePath(), snapshotPath);
  }

  /**
   * Clears the current profile information.
   *
   * @throws IllegalAccessException If the utility hasn't been properly configured
   */
  public static void setNoProfile() throws IllegalAccessException {
    if(dataInstance == null) {
      throw new IllegalAccessException("InternalFileStoreUtil has not had it's config function called yet");
    }

    profileName = null;
    snapshotPath = null;
    outputPath = null;
    tempPath = null;
    dataInstance.clearRepos();
  }

//////////////////////////////////////////////

  public static void setCurrentSnapshotPath() throws IOException {
    setCurrentPath(SNAPSHOT_PATH);
  }

  public static void setCurrentOutputPath() throws IOException {
    setCurrentPath(OUTPUT_PATH);
  }

  public static void setCurrentTempPath() throws IOException {
    setCurrentPath(TEMP_PATH);
  }

  private static void setCurrentPath(String valueForPath) throws IOException {
    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmm");
    Date date = new Date();

    // TODO: WRB make sure impossible for second folder

    switch (valueForPath) {
    case SNAPSHOT_PATH:
      snapshotPath = getProfilePath().resolve(valueForPath).resolve(dateFormat.format(date));
      snapshotPath.toFile().mkdirs();
      break;
    case OUTPUT_PATH:
      outputPath = getProfilePath().resolve(valueForPath).resolve(dateFormat.format(date));
      outputPath.toFile().mkdirs();
      break;
    case TEMP_PATH:
      tempPath = getProfilePath().resolve(valueForPath).resolve(dateFormat.format(date));
      tempPath.toFile().mkdirs();
      break;
    }
  }
}
