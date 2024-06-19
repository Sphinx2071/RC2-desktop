package edu.uw.cse.ifrcdemo.setup.ui.config;

import edu.uw.cse.ifrcdemo.setup.model.config.ConfigDto;
import edu.uw.cse.ifrcdemo.setup.model.config.Configurable;
import edu.uw.cse.ifrcdemo.sharedlib.model.datattype.Module;
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
import java.util.*;
import java.util.prefs.BackingStoreException;
import java.util.prefs.InvalidPreferencesFormatException;

import static edu.uw.cse.ifrcdemo.setup.model.config.ConfigFactory.createConfig;

@Controller
@RequestMapping("/serverconfig")
@SessionAttributes(types = { ConfigDto.class })
public class ConfigController {
    private static final String SERVER_CONFIG = "serverconfig/serverConfig";
    private static final String RELIEF_CONFIG = "reliefconfig/reliefConfig";
    private static final String HEALTH_CONFIG = "healthconfig/healthConfig";

    private final Logger logger;
    private final TemplateEngine templateEngine;

    public ConfigController(Logger logger, TemplateEngine templateEngine) {
        this.logger = logger;
        this.templateEngine = templateEngine;
    }

    @Valid
    @ModelAttribute("ConfigDto")
    public ConfigDto newConfig(){return new ConfigDto();}


    @GetMapping("")
    public ModelAndView selectModule(
            @Valid @ModelAttribute("ConfigDto") ConfigDto configDto, BindingResult bindingResult, SessionStatus sessionStatus)
            throws IOException, JSONException, BackingStoreException, InvalidPreferencesFormatException {

        ModelAndView configDtoModelAndView = new ModelAndView(SERVER_CONFIG);

        configDto.setModuleList(Arrays.asList(Module.values()));

        configDtoModelAndView.addObject("configDto", configDto);

        return configDtoModelAndView;
    }

    @PostMapping("processModule")
    public ModelAndView processModule(
            @Valid @ModelAttribute("ConfigDto") ConfigDto configDto, BindingResult bindingResult) {

        if (configDto.getModule().equals(Module.RELIEF)) {
            Configurable config = createConfig(configDto);

            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("config", config);
            modelAndView.setViewName("redirect:/" + RELIEF_CONFIG);
            return modelAndView;
        } else if (configDto.getModule().equals(Module.HEALTH)){
            Configurable config = createConfig(configDto);

            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("config", config);
            modelAndView.setViewName("redirect:/" + HEALTH_CONFIG);
            return modelAndView;
        }else {
            throw new IllegalArgumentException("Invalid Module");
        }
    }
}
