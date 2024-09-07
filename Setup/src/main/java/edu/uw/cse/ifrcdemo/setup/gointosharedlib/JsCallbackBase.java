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

package edu.uw.cse.ifrcdemo.setup.gointosharedlib;

import edu.uw.cse.ifrcdemo.setup.gointosharedlib.ui.xlsx.XlsxPath;
import edu.uw.cse.ifrcdemo.setup.gointosharedlib.util.FxDialogUtil;
import edu.uw.cse.ifrcdemo.sharedlib.consts.GenConsts;
import edu.uw.cse.ifrcdemo.translations.TranslationConsts;
import edu.uw.cse.ifrcdemo.translations.TranslationUtil;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.util.concurrent.ExecutionException;

/**
 * JsCallbackBase is a base class for handling JavaScript callbacks in a JavaFX WebView environment.
 * It provides methods for opening various types of dialogs and executing JavaScript functions.
 *
 * @author [Your Name]
 * @version 1.0
 * @since [The release or version this class was introduced]
 */
public class JsCallbackBase {
    protected final Window window;
    protected final WebEngine webEngine;

    /**
     * Constructs a new JsCallbackBase instance.
     *
     * @param window The JavaFX Window in which dialogs will be displayed
     * @param webEngine The WebEngine used for executing JavaScript
     */
    public JsCallbackBase(Window window, WebEngine webEngine) {
        this.window = window;
        this.webEngine = webEngine;
    }

    /**
     * Opens a file dialog for selecting a CSV file and executes a JavaScript handler function.
     *
     * @param handlerFnName The name of the JavaScript function to call with the selected file path
     */
    public void openFileDialogForCsv(String handlerFnName) {
        if (handlerFnName == null) {
            return;
        }

        File selectedCsv = null;
        try {
            FileChooser chooser = new FileChooser();
            chooser.setTitle(TranslationUtil.getTranslations().getString(TranslationConsts.CHOOSE_CSV_FILE));
            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter(GenConsts.CSV_FILE_EXTENSION, GenConsts.CSV_FILTER));

            selectedCsv = chooser.showOpenDialog(window);

            if (selectedCsv != null) {
                selectedCsv.getAbsolutePath();
            }
        } finally {
            if (selectedCsv != null && selectedCsv.length() > 0) {
                String selectedCsvStr = selectedCsv.getAbsolutePath().replace("\\", "\\\\");
                webEngine.executeScript(handlerFnName + "('" + selectedCsvStr + "')");
            } else {
                webEngine.executeScript(handlerFnName + "()");
            }
        }

    }

    /**
     * Opens a directory chooser dialog and executes a JavaScript handler function.
     *
     * @param handlerFnName The name of the JavaScript function to call with the selected directory path
     */
    public void openSimpleFileDialog(String handlerFnName) {
        if (handlerFnName == null) {
            return;
        }
        String selectedDirectoryStr = null;
        try {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle(TranslationUtil.getTranslations().getString(TranslationConsts.CHOOSE_DIRECTORY));
            File selectedDirectory = chooser.showDialog(window);
            if (selectedDirectory != null) {
                selectedDirectoryStr = selectedDirectory.getAbsolutePath().replace("\\", "\\\\");
            }
        } finally {
            if (selectedDirectoryStr != null && selectedDirectoryStr.length() > 0) {
                webEngine.executeScript(handlerFnName + "('" + selectedDirectoryStr + "')");
            } else {
                webEngine.executeScript(handlerFnName + "()");
            }
        }
    }
    /**
     * Opens an error dialog using FxDialogUtil.
     *
     * @param errorMessage The error message to display
     */
    public void openFxErrorDialog(String errorMessage) {
        if (errorMessage == null) {
            return;
        }
        FxDialogUtil.showErrorDialog(errorMessage);
    }

    /**
     * Opens a warning dialog using FxDialogUtil.
     *
     * @param warnMessage The warning message to display
     */
    public void openFxWarnDialog(String warnMessage) {
        if (warnMessage == null) {
            return;
        }
        FxDialogUtil.showWarningDialog(warnMessage);
    }

    /**
     * Opens an information dialog using FxDialogUtil.
     *
     * @param infoMessage The information message to display
     */
    public void openFxInfoDialog(String infoMessage) {
        if (infoMessage == null) {
            return;
        }
        FxDialogUtil.showInfoDialog(infoMessage);
    }

    /**
     * Opens a confirmation dialog using FxDialogUtil and returns the user's choice.
     *
     * @param confirmMessage The confirmation message to display
     * @return true if the user confirmed, false otherwise
     * @throws ExecutionException If the execution of the dialog fails
     * @throws InterruptedException If the thread is interrupted while waiting for the dialog result
     */
    public boolean openFxConfirmDialog(String confirmMessage) throws ExecutionException, InterruptedException {
        if (confirmMessage == null) {
            return false;
        }

        ButtonType buttonType = FxDialogUtil.showConfirmDialogAndWait(
                TranslationUtil.getTranslations().getString(TranslationConsts.WARNING_DIALOG_TITLE),
                confirmMessage,
                null
        );

        return buttonType == ButtonType.OK;
    }

    /**
     * Clears the WebEngine's history.
     */
    public void clearWebHistory() {
        WebHistory history = webEngine.getHistory();
        history.setMaxSize(0);
        ObservableList<WebHistory.Entry> historyList = history.getEntries();
        assert (historyList.size() == 0);
        if (historyList.size() == 0) {
            history.setMaxSize(100);
        }
    }

    /**
     * Opens a file dialog for selecting an XLSX file.
     *
     * @return An XlsxPath object containing the full path and display name of the selected file, or null if no file was selected
     */
    public XlsxPath openFileDialogForXlsx() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle(TranslationUtil.getTranslations().getString(TranslationConsts.CHOOSE_XLSX_LABEL));
        chooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter(GenConsts.XLSX, GenConsts.XLSX_FILTER));

        File selectedXlsx = chooser.showOpenDialog(window);

        if (selectedXlsx != null) {
            return new XlsxPath(
                    selectedXlsx.getAbsolutePath(),
                    selectedXlsx.getName()
            );
        }

        return null;
    }

    /**
     * Opens a file dialog for selecting a report template file.
     *
     * @return The absolute path of the selected template file, or null if no file was selected
     */
    public String openFileDialogForReportTemplate() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose a Template");
        chooser
                .getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Report Template (.mustache)", "*.mustache"));

        return chooser.showOpenDialog(window).getAbsolutePath();
    }
}
