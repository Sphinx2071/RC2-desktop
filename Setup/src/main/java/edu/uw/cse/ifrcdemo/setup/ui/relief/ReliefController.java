package edu.uw.cse.ifrcdemo.setup.ui.relief;

import edu.uw.cse.ifrcdemo.sharedlib.model.datattype.AuthorizationType;
import org.apache.logging.log4j.Logger;
import org.apache.wink.json4j.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.TemplateEngine;

import javax.validation.Valid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.prefs.BackingStoreException;
import java.util.prefs.InvalidPreferencesFormatException;

import static edu.uw.cse.ifrcdemo.setup.ui.relief.ReliefConfigFormModel.createConfig;

@Controller
@RequestMapping("/reliefconfig")
@SessionAttributes(types={ReliefConfigFormModel.class})
public class ReliefController {
    private static final String RELIEF_CONFIG_VIEW ="reliefconfig/reliefConfig";
    private final Logger logger;
    private final TemplateEngine templateEngine;

    public ReliefController(Logger logger, TemplateEngine templateEngine){
        this.logger = logger;
        this.templateEngine = templateEngine;
    }

    @Valid
    @ModelAttribute("reliefConfigFormModel")
    public ReliefConfigFormModel newReliefConfigFormModel(){
        return createConfig();
    }

    @GetMapping
    public ModelAndView reliefServerConfig(
            @Valid @ModelAttribute("reliefConfigFormModel") ReliefConfigFormModel reliefConfigFormModel, BindingResult bindingResult, SessionStatus sessionStatus)
            throws IOException, JSONException, BackingStoreException, InvalidPreferencesFormatException {

        ModelAndView reliefModelAndView = new ModelAndView(RELIEF_CONFIG_VIEW);

        reliefConfigFormModel.setAuthorizationTypeList(Arrays.asList(AuthorizationType.values()));
        reliefConfigFormModel.setRegistrationModeList(new ArrayList<>());

        reliefModelAndView.addObject("reliefConfigFormModel", reliefConfigFormModel);
        return reliefModelAndView;
    }

    @PostMapping
    public ModelAndView updateForm(@Valid @ModelAttribute("reliefConfigFormModel") ReliefConfigFormModel reliefConfigFormModel, BindingResult bindingResult, SessionStatus sessionStatus)
            throws IOException, JSONException, BackingStoreException, InvalidPreferencesFormatException{

        ModelAndView modelAndView = new ModelAndView(RELIEF_CONFIG_VIEW);
        modelAndView.addObject("reliefConfig", reliefConfigFormModel);
        return modelAndView;
    }
}
