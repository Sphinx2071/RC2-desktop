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
import edu.uw.cse.ifrcdemo.translations.TranslationConsts;
import edu.uw.cse.ifrcdemo.translations.TranslationUtil;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import static javafx.application.Platform.isFxApplicationThread;
import static javafx.application.Platform.runLater;

/**
 * FxDialogUtil is a utility class for creating and displaying various types of JavaFX dialogs.
 * It provides methods for showing confirmation, information, error, and warning dialogs,
 * as well as file chooser dialogs. This class ensures that dialogs are displayed on the
 * JavaFX Application Thread.
 *
 * @author [Your Name]
 * @version 1.0
 * @since [The release or version this class was introduced]
 */

public class FxDialogUtil {
  private static final Logger logger = LogManager.getLogger(FxDialogUtil.class);

  // initialize the dialog owning window to null, which means "top-level, unowned dialog"
  static {
    owningWindow = null;
  }

  private static Window owningWindow;

  /**
   * Sets the owning window for all dialogs created by this utility.
   *
   * @param newOwningWindow The Window to set as the owner for dialogs
   */
  public static void setOwningWindow(Window newOwningWindow) {
    owningWindow = newOwningWindow;
  }

  /**
   * Gets the current owning window for dialogs.
   *
   * @return The current owning Window
   */
  public static Window getOwningWindow() {
    return owningWindow;
  }

  /**
   * Configures and displays a confirmation dialog.
   *
   * @param title The title of the dialog
   * @param header The header text of the dialog
   * @param content The content text of the dialog
   * @return The ButtonType selected by the user
   */
  private static ButtonType configureConfirmDialogAndWait(String title, String header, String content) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.initOwner(owningWindow);
    alert.setTitle(title);
    alert.setHeaderText(header);
    alert.setContentText(content);
    alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

    Optional<ButtonType> result = alert.showAndWait();
    return result.get();
  }

   /**
   * Shows a confirmation dialog and waits for user input.
   *
   * @param title The title of the dialog
   * @param header The header text of the dialog
   * @param content The content text of the dialog
   * @return The ButtonType selected by the user
   * @throws ExecutionException If the computation threw an exception
   * @throws InterruptedException If the current thread was interrupted while waiting
   */
  public static ButtonType showConfirmDialogAndWait(String title, String header, String content)
      throws ExecutionException, InterruptedException {
    FutureTask<ButtonType> futureTask = new FutureTask(new Callable<ButtonType>() {
      @Override
      public ButtonType call() throws Exception {
        ResourceBundle translations = TranslationUtil.getTranslations();
        return FxDialogUtil.configureConfirmDialogAndWait(title, header, content);
      }
    });

    if (isFxApplicationThread()) {
      return configureConfirmDialogAndWait(title, header, content);
    } else {
      Platform.runLater(futureTask);
      return futureTask.get();
    }
  }

  /**
   * Shows an information dialog.
   *
   * @param header The header text of the dialog
   */
  public static void showInfoDialog(String header) {
    showDialog(Alert.AlertType.INFORMATION,
        TranslationUtil.getTranslations().getString(TranslationConsts.DONE_LABEL), header, null);
  }

  /**
   * Shows a dialog of the specified type.
   *
   * @param type The Alert.AlertType of the dialog
   * @param title The title of the dialog
   * @param header The header text of the dialog
   * @param content The content text of the dialog
   */
  public static void showDialog(Alert.AlertType type, String title, String header, String content) {
    Runnable r = new Runnable() {
      @Override
      public void run() {
        Alert alert = new Alert(type);
        alert.initOwner(owningWindow);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.show();
      }
    };

    if (isFxApplicationThread()) {
      r.run();
    } else {
      runLater(r);
    }
  }

  /**
   * Shows an error dialog.
   *
   * @param header The header text of the dialog
   * @param content The content text of the dialog
   */
  public static void showErrorDialog(String header, String content) {
    showDialog(Alert.AlertType.ERROR,
        TranslationUtil.getTranslations().getString(TranslationConsts.ERROR_LABEL), header,
        content);
  }

  /**
   * Shows an error dialog.
   *
   * @param header The header text of the dialog
   */
  public static void showErrorDialog(String header) {
    showDialog(Alert.AlertType.ERROR,
        TranslationUtil.getTranslations().getString(TranslationConsts.ERROR_LABEL), header, null);
  }

  /**
   * Shows a warning dialog.
   *
   * @param header The header text of the dialog
   */
  public static void showWarningDialog(String header) {
    showDialog(Alert.AlertType.WARNING,
        TranslationUtil.getTranslations().getString(TranslationConsts.WARNING_DIALOG_TITLE), header,
        null);
  }

  /**
   * Shows a warning dialog.
   *
   * @param header The header text of the dialog
   * @param content The content text of the dialog
   */
  public static void showWarningDialog(String header, String content) {
    showDialog(Alert.AlertType.WARNING,
        TranslationUtil.getTranslations().getString(TranslationConsts.WARNING_DIALOG_TITLE), header,
        content);
  }

  /**
   * Shows a scrolling dialog with expandable content.
   *
   * @param type The Alert.AlertType of the dialog
   * @param title The title of the dialog
   * @param header The header text of the dialog
   * @param content The content text of the dialog
   * @param messageLabel The label for the expandable message area
   * @param message The message to display in the expandable area
   * @param setExpanded Whether the expandable area should be initially expanded
   */
  public static void showScrollingDialog(Alert.AlertType type, String title, String header,
      String content, String messageLabel, String message, boolean setExpanded) {

    Runnable r = new Runnable() {
      @Override
      public void run() {
        Alert alert = new Alert(type);
        alert.initOwner(owningWindow);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        Label label = new Label(messageLabel);

        TextArea textArea = new TextArea(message);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);
        alert.getDialogPane().setExpanded(setExpanded);

        alert.show();
      }
    };

    if (isFxApplicationThread()) {
      r.run();
    } else {
      runLater(r);
    }
  }

  /**
   * Shows a scrolling dialog with expandable content.
   *
   * @param type The Alert.AlertType of the dialog
   * @param title The title of the dialog
   * @param header The header text of the dialog
   * @param content The content text of the dialog
   * @param messageLabel The label for the expandable message area
   * @param message The message to display in the expandable area
   */
  public static void showScrollingDialog(Alert.AlertType type, String title, String header,
                                         String content, String messageLabel, String message) {
    showScrollingDialog(type, title, header, content, messageLabel, message, false);
  }

  /**
   * Shows a scrolling exception dialog.
   *
   * @param header The header text of the dialog
   * @param e The exception to display
   */
  public static void showScrollingExceptionDialog(String header, Exception e) {
    showScrollingExceptionDialog(header, e, false);
  }

  /**
   * Shows a scrolling exception dialog.
   *
   * @param header The header text of the dialog
   * @param e The exception to display
   * @param setExpanded Whether the exception details should be initially expanded
   */
  public static void showScrollingExceptionDialog(String header, Exception e, boolean setExpanded) {
    showScrollingExceptionDialog(header, null, e, setExpanded);
  }

  /**
   * Shows a scrolling exception dialog.
   *
   * @param header The header text of the dialog
   * @param content The content text of the dialog
   * @param e The exception to display
   */
  public static void showScrollingExceptionDialog(String header, String content, Exception e) {
    showScrollingExceptionDialog(header, content, e, false);
  }

  /**
   * Shows a scrolling exception dialog.
   *
   * @param header The header text of the dialog
   * @param content The content text of the dialog
   * @param e The exception to display
   * @param setExpanded Whether the exception details should be initially expanded
   */
  public static void showScrollingExceptionDialog(String header, String content, Exception e, boolean setExpanded) {
    ResourceBundle translations = TranslationUtil.getTranslations();

    String messageLabel = translations.getString(TranslationConsts.EXCEPTION_STACK_TRACE);

    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    e.printStackTrace(pw);
    String exceptionText = sw.toString();

    showScrollingDialog(Alert.AlertType.ERROR,
        translations.getString(TranslationConsts.ERROR_LABEL), header, content, messageLabel,
        exceptionText, setExpanded);
  }

  /**
   * Shows a scrolling error dialog with expandable content.
   *
   * @param header The header text of the dialog
   * @param content The content text of the dialog
   * @param messageLabel The label for the expandable message area
   * @param message The message to display in the expandable area
   * @param setExpanded Whether the expandable area should be initially expanded
   */
  public static void showScrollingErrorDialog(String header, String content, String messageLabel,
      String message, boolean setExpanded) {
    showScrollingDialog(Alert.AlertType.ERROR,
        TranslationUtil.getTranslations().getString(TranslationConsts.ERROR_LABEL), header, content,
        messageLabel, message, setExpanded);
  }

  /**
   * Shows a scrolling error dialog with expandable content.
   *
   * @param header The header text of the dialog
   * @param content The content text of the dialog
   * @param messageLabel The label for the expandable message area
   * @param message The message to display in the expandable area
   */
  public static void showScrollingErrorDialog(String header, String content, String messageLabel,
                                              String message) {
    showScrollingErrorDialog(header, content, messageLabel, message, false);
  }

  /**
   * Shows a scrolling warning dialog with expandable content.
   *
   * @param header The header text of the dialog
   * @param content The content text of the dialog
   * @param messageLabel The label for the expandable message area
   * @param message The message to display in the expandable area
   */
  public static void showScrollingWarningDialog(String header, String content, String messageLabel,
      String message) {
    showScrollingDialog(Alert.AlertType.WARNING,
        TranslationUtil.getTranslations().getString(TranslationConsts.WARNING_DIALOG_TITLE), header,
        content, messageLabel, message);
  }

  /**
   * Shows a file chooser dialog.
   *
   * @return The selected Path, or null if no selection was made
   * @throws ExecutionException If the computation threw an exception
   * @throws InterruptedException If the current thread was interrupted while waiting
   */
  public static Path showFileChooserDialog() throws ExecutionException, InterruptedException {
    if (Platform.isFxApplicationThread()) {
      return configureFileChooserDialog();
    } else {
      return showFileChooserDialogAsync().get();
    }
  }

  /**
   * Shows a file chooser dialog asynchronously.
   *
   * @return A CompletableFuture that will resolve to the selected Path
   */
  public static CompletableFuture<Path> showFileChooserDialogAsync() {
    return CompletableFuture.supplyAsync(FxDialogUtil::configureFileChooserDialog, Platform::runLater);
  }

  /**
   * Configures and displays a file chooser dialog.
   *
   * @return The selected Path, or null if no selection was made
   */
  private static Path configureFileChooserDialog() {
    Path selectedDirectoryPath = null;

    DirectoryChooser chooser = new DirectoryChooser();
    chooser.setTitle(TranslationUtil.getTranslations().getString(TranslationConsts.CHOOSE_DIRECTORY));
    File selectedDirectory = chooser.showDialog(new Stage());
    if (selectedDirectory != null) {
      selectedDirectoryPath = selectedDirectory.toPath();
    }

    logger.debug("FileChooser ChooseDirectory: {}", selectedDirectoryPath);

    return selectedDirectoryPath;
  }

  /**
   * Shows a template chooser dialog.
   *
   * @return The selected Path, or null if no selection was made
   * @throws ExecutionException If the computation threw an exception
   * @throws InterruptedException If the current thread was interrupted while waiting
   */
  public static Path showTemplateChooserDialog() throws ExecutionException, InterruptedException {

    FutureTask<Path> futureTask = new FutureTask(new Callable<Path>() {
      @Override
      public Path call() throws Exception {
        return FxDialogUtil.configureTemplateChooserDialog();
      }
    });

    if (isFxApplicationThread()) {
      return configureTemplateChooserDialog();
    } else {
      Platform.runLater(futureTask);
      return futureTask.get();
    }
  }

  /**
   * Configures and displays a template chooser dialog.
   *
   * @return The selected Path, or null if no selection was made
   */
  private static Path configureTemplateChooserDialog() {
    Path selectedTemplatePath = null;

    FileChooser chooser = new FileChooser();
    chooser.setTitle(TranslationUtil.getTranslations().getString(TranslationConsts.CHOOSE_TEMPLATE_LOCATION_MSG ));
    FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Template Extension", GenConsts.ZIP_FILE_EXTENSION);
    chooser.setSelectedExtensionFilter(extensionFilter);
    File selectedZip = chooser.showOpenDialog(new Stage());;
    if (selectedZip != null) {
      selectedTemplatePath = selectedZip.toPath();
    }

    return selectedTemplatePath;
  }

}

