package edu.uw.cse.ifrcdemo.setup.ui.login;

import edu.uw.cse.ifrcdemo.setup.gointosharedlib.util.FxDialogUtil;
import edu.uw.cse.ifrcdemo.setup.gointosharedlib.util.InternalFileStoreUtil;
import edu.uw.cse.ifrcdemo.setup.ui.common.services.SyncClientService;
import edu.uw.cse.ifrcdemo.sharedlib.util.ErrorUtil;
import edu.uw.cse.ifrcdemo.translations.TranslationConsts;
import edu.uw.cse.ifrcdemo.translations.TranslationUtil;
import edu.uw.cse.ifrcdemo.xlsxconverterserver.handler.FormUploadConsumer;
import edu.uw.cse.ifrcdemo.xlsxconverterserver.handler.MappingFormUploadConsumer;
import javafx.application.Platform;
import javafx.scene.control.ButtonType;
import jdk.nashorn.internal.runtime.UnwarrantedOptimismException;
import org.apache.logging.log4j.Logger;
import org.apache.wink.json4j.JSONException;
import org.opendatakit.aggregate.odktables.rest.entity.PrivilegesInfo;
import org.opendatakit.suitcase.model.CloudEndpointInfo;
import org.opendatakit.sync.client.SyncClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.TemplateEngine;

import javax.validation.Valid;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.prefs.BackingStoreException;
import java.util.prefs.InvalidPreferencesFormatException;

import static edu.uw.cse.ifrcdemo.setup.ui.login.LoginFormModel.createLogin;

@Controller
@RequestMapping("login")
public class LoginController {
    private static final String LOGIN_VIEW ="login";
    private final SyncClientService syncClientService;


    @Autowired
    public LoginController(SyncClientService syncClientService) {
        this.syncClientService = syncClientService;
    }

    @GetMapping
    public ModelAndView loginConfig(
            @Valid @ModelAttribute("loginFormModel") LoginFormModel loginFormModel, BindingResult bindingResult, SessionStatus sessionStatus)
            throws IOException, JSONException, BackingStoreException, InvalidPreferencesFormatException{

        ModelAndView loginModelAndView = new ModelAndView(LOGIN_VIEW);
        loginModelAndView.addObject(loginFormModel);

        return loginModelAndView;
    }

    @PostMapping("/login")
    public String handleLogin(@ModelAttribute LoginFormModel loginForm, Model model)
            throws IOException, JSONException, BackingStoreException, InvalidPreferencesFormatException{
        try {
            // Initialize and verify server connection using SyncClient
            syncClientService.initializeServer(
                    loginForm.getServerUrl(),
                    loginForm.getUsername(),
                    loginForm.getPassword()
            );

            // If we get here, login was successful
            return "redirect:/setup/complete";
        } catch (Exception e) {
            model.addAttribute("error", "Server connection failed: " + e.getMessage());
            return LOGIN_VIEW;
        }
    }
}


/*@Controller
public class LoginController {
    private final SyncClientService syncClientService;
    private final FormUploadConsumer formUploadConsumer;

    @Autowired
    public LoginController(SyncClientService syncClientService) {
        this.syncClientService = syncClientService;
        this.formUploadConsumer = new MappingFormUploadConsumer();
    }

    @PostMapping("/login")
    public String handleLogin(@ModelAttribute LoginFormModel loginForm, Model model) {
        try {
            // Initialize and verify server connection using SyncClient
            syncClientService.initializeServer(loginForm.getServerUrl(),
                    loginForm.getUsername(),
                    loginForm.getPassword());

            // Create and execute XlsxServerRunnable
            XlsxServerRunnable serverSetup = new XlsxServerRunnable(
                    (Execption e) -> model.addAttribute("error", e.getMessage()),
                    formUploadConsumer
            );

            CompletableFuture.runAsync(serverSetup)
                    .exceptionally(e -> {
                        model.addAttribute("error", "Form upload failed: " + e.getMessage());
                        return null;
                    });

            return "redirect:/setup/complete";
        } catch (Exception e) {
            model.addAttribute("error", "Server connection failed: " + e.getMessage());
            return "setup/login";
        }


    }




    private static class XlsxServerRunnable implements Runnable {
        private final Consumer<Exception> initExceptionHandler;
        private final FormUploadConsumer formUploadConsumer;
        private final SyncClient syncClient;

        private XlsxServerRunnable(Consumer<Exception> initExceptionHandler, FormUploadConsumer formUploadConsumer, SyncClient syncClient) {
            this.initExceptionHandler = initExceptionHandler;
            this.formUploadConsumer = formUploadConsumer;
            this.syncClient = syncClient;
        }

        @Override
        public void run() {
            try {
                // Use syncClient for server operations
                formUploadConsumer.accept();
            } catch (Exception e) {
                initExceptionHandler.accept(e);
            }
        }
    }
}*/

