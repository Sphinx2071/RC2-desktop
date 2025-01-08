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

package edu.uw.cse.ifrcdemo.setup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;
import edu.uw.cse.ifrcdemo.setup.gointosharedlib.SharedWebInfrastructure;
import edu.uw.cse.ifrcdemo.setup.gointosharedlib.ui.localization.PreferencesLocaleResolver;
import edu.uw.cse.ifrcdemo.setup.gointosharedlib.util.FxDialogUtil;
import edu.uw.cse.ifrcdemo.translations.TranslationConsts;
import edu.uw.cse.ifrcdemo.translations.TranslationUtil;
import io.sentry.Sentry;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import netscape.javascript.JSObject;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.core.MethodParameter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

/**
 * SetupApplication is the main class for the Setup application.
 * It extends JavaFX's Application class and implements Spring's WebMvcConfigurer interface,
 * combining JavaFX GUI capabilities with Spring Boot's web application features.
 *
 * This class initializes the application, sets up the main window, configures Spring beans,
 * and handles application lifecycle events.
 *
 * @author [Your Name]
 * @version 1.0
 * @since [The release or version this class was introduced]
 */
@SpringBootApplication
public class SetupApplication extends Application implements WebMvcConfigurer {

  private static final Logger logger = LogManager.getLogger(SetupApplication.class);

  private static ConfigurableApplicationContext ctx;
  private static SharedWebInfrastructure swi;

  private static ChangeListener<Worker.State> listener;
  private static EventHandler<WindowEvent> closeHandler;

  /**
   * Starts the JavaFX application, setting up the main window and web infrastructure.
   *
   * @param stage The primary stage for this application
   */
  @Override
  public void start(Stage stage) {
    swi = new SharedWebInfrastructure(stage);
    listener = new SetupChangeListener(swi);
    closeHandler = new SetupCloseEventHandler();

    swi.addListenerToWebEngine(listener);
    swi.setCloseHandler(closeHandler);

    stage.setTitle(SetupAppSystem.APP_TITLE);
    stage.getIcons().add(new Image("/static/img/IFRC_Logo.png"));

    swi.loadUrl(SetupAppSystem.START_URL);
    stage.show();
  }

  /**
   * The main entry point for the application.
   * Initializes the system, starts the Spring application context, and launches the JavaFX application.
   *
   * @param args Command line arguments
   */
  public static void main(String[] args) {
    SetupAppSystem.systemInit();
    SetupAppSystem.initAllConfigs();
    try {
      ctx = new SpringApplicationBuilder(SetupApplication.class).headless(false)
              .run(args);

      launch(args);
    } catch (Exception throwable) {
      logger.catching(throwable);
      System.err.println("Sending to Sentry!!!");
      Sentry.capture(throwable);
    }
  }

  /*
   * Bean needed for language change
   */
  /**
   * Configures the locale resolver bean.
   *
   * @param resolver The PreferencesLocaleResolver to use
   * @return The configured LocaleResolver
   */
  @Bean
  public LocaleResolver localeResolver(PreferencesLocaleResolver resolver) {
    return resolver;
  }

  /**
   * Adds interceptors to the Spring MVC request processing pipeline.
   *
   * @param registry The InterceptorRegistry to which the interceptors are added
   */
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new LocaleChangeInterceptor());
    registry.addInterceptor(new HandlerInterceptor() {
      @Override
      public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if (ex != null) {
          Sentry.capture(ex);
        }
      }
    });
  }

  /**
   * Configures a prototype-scoped Logger bean.
   *
   * @param injectionPoint The injection point for the Logger
   * @return A Logger instance
   */
  @Bean
  @Scope(BeanDefinition.SCOPE_PROTOTYPE)
  public Logger logger(InjectionPoint injectionPoint) {
    Class<?> loggingClass = SetupApplication.class;

    MethodParameter methodParameter = injectionPoint.getMethodParameter();
    if (methodParameter != null) {
      // constructor injection
      loggingClass = methodParameter.getContainingClass();
    } else {
      Field field = injectionPoint.getField();

      if (field != null) {
        // field injection
        loggingClass = field.getDeclaringClass();
      }
    }

    return LogManager.getLogger(loggingClass);
  }

  /**
   * Configures the ObjectMapper bean.
   *
   * @return An ObjectMapper instance
   */
  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper;
  }

  /**
   * Configures a prototype-scoped MustacheFactory bean.
   *
   * @param logger The Logger to use
   * @return A MustacheFactory instance
   */
  @Bean
  @Scope(BeanDefinition.SCOPE_PROTOTYPE) // use prototype to prevent template caching
  public MustacheFactory mustacheFactory(Logger logger) {
    return new DefaultMustacheFactory(resourceName -> {
      try {
        return Files.newBufferedReader(Paths.get(resourceName), StandardCharsets.UTF_8);
      } catch (IOException e) {
        logger.catching(Level.INFO, e);
        return null;
      }
    });
  }

  /**
   * SetupChangeListener is an inner class that implements ChangeListener<Worker.State>.
   * It listens for changes in the WebEngine's worker state and sets up JavaScript
   * integration when the page load is successful.
   */
  private class SetupChangeListener implements ChangeListener<Worker.State> {
    private final Scene scene;
    private final WebEngine webEngine;

    /**
     * Constructs a SetupChangeListener with the given SharedWebInfrastructure.
     *
     * @param swi The SharedWebInfrastructure containing the scene and WebEngine
     */
    public SetupChangeListener(SharedWebInfrastructure swi) {
      this.scene = swi.getScene();
      this.webEngine = swi.getWebEngine();
    }

    /**
     * Called when the worker's state changes. If the new state is SUCCEEDED,
     * it sets up JavaScript integration by adding a 'javafx' member to the window
     * object and dispatches a 'javafxReady' event.
     *
     * @param observable The ObservableValue which value changed
     * @param oldValue The old state value
     * @param newValue The new state value
     */
    //todo: do i need a callback for setup?
    @Override
    public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue,
                        Worker.State newValue) {
      if (newValue == Worker.State.SUCCEEDED) {
        JSObject window = (JSObject) webEngine.executeScript("window");
        window.setMember("javafx", new SetupJsCallback(scene.getWindow(), webEngine));
        webEngine.executeScript("window.dispatchEvent(new Event('javafxReady'))");
      }
    }
  }

  /**
   * SetupCloseEventHandler is an inner class that implements EventHandler<WindowEvent>.
   * It handles the window close event, showing a confirmation dialog before closing the application.
   */
  private class SetupCloseEventHandler implements EventHandler<WindowEvent> {

    /**
     * Handles the window close event. Shows a confirmation dialog and, if confirmed,
     * stops and closes the application context. If not confirmed, consumes the event
     * to prevent the window from closing.
     *
     * @param event The WindowEvent to handle
     */
    @Override
    public void handle(WindowEvent event) {
      try {
        ResourceBundle translations = TranslationUtil.getTranslations();
        ButtonType result =
            FxDialogUtil.showConfirmDialogAndWait(
                translations.getString(TranslationConsts.EXIT),
                translations.getString(TranslationConsts.THE_APPLICATION_WILL_CLOSE),
                translations.getString(TranslationConsts.ARE_YOU_SURE_YOU_WANT_TO_EXIT));
        if (result == ButtonType.OK) {
          logger.error("Close Request");
          ctx.stop();
          ctx.close();
          stop();
        } else {
          event.consume();
        }
      } catch (Exception e) {
        logger.catching(e);
        Sentry.capture(e);
      }
    }
  }
}
