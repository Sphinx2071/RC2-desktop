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

import com.sun.javafx.webkit.WebConsoleListener;
import edu.uw.cse.ifrcdemo.setup.gointosharedlib.util.FxDialogUtil;
import io.sentry.Sentry;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class provides a shared infrastructure for web-based JavaFX applications.
 * It encapsulates common web components like WebView, WebEngine, and Scene,
 * and provides methods to interact with these components.
 */
public class SharedWebInfrastructure {

    private static final Logger logger = LogManager.getLogger(SharedWebInfrastructure.class);

    private final WebView browser;
    private final WebEngine webEngine;
    private final Scene scene;
    private final Stage stage;

    /**
     * Constructs a SharedWebInfrastructure instance.
     * Initializes WebView, WebEngine, and Scene, and sets up error handling and console logging.
     *
     * @param stage The primary stage for this JavaFX application
     */
    public SharedWebInfrastructure(Stage stage) {
        this.stage = stage;
        this.browser = new WebView();
        this.webEngine = browser.getEngine();
        this.scene = new Scene(new Group());

        FxDialogUtil.setOwningWindow(stage);

        browser.setPrefSize(BaseAppSystem.PREF_WIDTH, BaseAppSystem.PREF_HEIGHT);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(browser);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        webEngine.setOnError(evt -> {
            logger.error(evt.getException());
            Sentry.capture(evt.getException());
        });

        WebConsoleListener.setDefaultListener((webView, message, lineNumber, sourceId) -> {
            Level level = Level.INFO;
            // there has to be a better way to do this
            if (message.startsWith("Error:")) {
                level = Level.ERROR;
            }

            logger.log(level, "source: {} line: {} message: {}", sourceId, lineNumber, message);
        });
        scene.setRoot(scrollPane);
        stage.setScene(scene);

    }

    public Scene getScene() {
        return scene;
    }

    public WebEngine getWebEngine() {
        return webEngine;
    }

    /**
     * Adds a ChangeListener to the WebEngine's load worker state property.
     * This listener will be notified of changes in the loading state of the web page.
     *
     * @param listener The ChangeListener to be added
     */
    public void addListenerToWebEngine(ChangeListener<Worker.State> listener) {
        webEngine.getLoadWorker().stateProperty().addListener(listener);
    }

    /**
     * Sets a close handler for the stage. This handler will be called when
     * a close request is made on the stage (e.g., when the user tries to close the window).
     *
     * @param handler The EventHandler to be set as the close handler
     */
    public void setCloseHandler(EventHandler<WindowEvent> handler) {
        stage.setOnCloseRequest(handler);
    }

    /**
     * Loads a specified URL in the WebEngine.
     *
     * @param url The URL to be loaded
     */
    public void loadUrl(String url) {
        webEngine.load(url);
    }

}
