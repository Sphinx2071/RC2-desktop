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

//todo: remove, old imports
/*import edu.uw.cse.ifrcdemo.setup.gointosharedlib.localization.PreferencesLocaleResolver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.core.MethodParameter;
import org.springframework.web.servlet.LocaleResolver;

import java.lang.reflect.Field;*/

@SpringBootApplication
public class SetupApplication extends Application implements WebMvcConfigurer {
    private static final Logger logger = LogManager.getLogger(SetupApplication.class);
    private static ConfigurableApplicationContext ctx;
    private static SharedWebInfrastructure swi;
    private static ChangeListener<Worker.State> listener;
    private static EventHandler<WindowEvent> closeHandler;

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

    // todo: remove, old main
    /*public static void main(String[] args) {
        SpringApplication.run(SetupApplication.class, args);
    }*/

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

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper;
    }

    @Bean
    public LocaleResolver localeResolver(PreferencesLocaleResolver resolver) {
        return resolver;
    }

    private class SetupChangeListener implements ChangeListener<Worker.State> {
        private final Scene scene;
        private final WebEngine webEngine;

        public SetupChangeListener(SharedWebInfrastructure swi) {
            this.scene = swi.getScene();
            this.webEngine = swi.getWebEngine();
        }
        /*todo: JSCallback is attempting to access a data repo in order call getCurrentProfile().....why do this? */
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

    private class SetupCloseEventHandler implements EventHandler<WindowEvent> {
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
