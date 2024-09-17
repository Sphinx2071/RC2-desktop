package edu.uw.cse.ifrcdemo.setup.ui.login;

import org.apache.logging.log4j.Logger;
import org.apache.wink.json4j.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.TemplateEngine;

import javax.validation.Valid;
import java.io.IOException;
import java.util.prefs.BackingStoreException;
import java.util.prefs.InvalidPreferencesFormatException;

import static edu.uw.cse.ifrcdemo.setup.ui.login.LoginFormModel.createLogin;

@Controller
@RequestMapping("/login")
@SessionAttributes(types={LoginFormModel.class})
public class LoginController {
    private static final String LOGIN = "login";

    private final Logger logger;
    private final TemplateEngine templateEngine;

    public LoginController(Logger logger,  TemplateEngine templateEngine){
        this.logger = logger;
        this.templateEngine = templateEngine;
    }

    @Valid
    @ModelAttribute("Login")
    public LoginFormModel loginForm(){
        return createLogin();
    }

    @GetMapping
    public ModelAndView loginView(
            @Valid @ModelAttribute("Login") LoginFormModel loginFormModel, BindingResult bindingResult, SessionStatus sessionStatus)
            throws IOException, JSONException, BackingStoreException, InvalidPreferencesFormatException {

        ModelAndView loginModelAndView = new ModelAndView(LOGIN);

        loginModelAndView.addObject("login", loginFormModel);

        return loginModelAndView;
    }

}
