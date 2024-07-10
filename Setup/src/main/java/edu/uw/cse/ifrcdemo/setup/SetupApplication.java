package edu.uw.cse.ifrcdemo.setup;

import edu.uw.cse.ifrcdemo.setup.gointosharedlib.localization.PreferencesLocaleResolver;
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

import java.lang.reflect.Field;

@SpringBootApplication
public class SetupApplication {
    private static final Logger logger = LogManager.getLogger(SetupApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SetupApplication.class, args);
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
    public LocaleResolver localeResolver(PreferencesLocaleResolver resolver) {
        return resolver;
    }
}
