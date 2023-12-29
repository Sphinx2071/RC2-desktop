package edu.uw.cse.ifrcdemo.setupfun.ui.reliefconfig;

import edu.uw.cse.ifrcdemo.planningsharedlib.ui.login.LoginFormModel;
import edu.uw.cse.ifrcdemo.setupfun.configuration.reliefconfig.ReliefConfigModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("reliefconfig")
//@SessionAttributes(types = { ReliefConfigModel.class })
public class ReliefConfigController {

    private static final String RELIEF_CONFIG_TEMPLATE = "reliefconfig/reliefConfig";
    private static final Logger logger = LogManager.getLogger(ReliefConfigController.class);



    public ReliefConfigController() {
    }

    @RequestMapping("")
    public ModelAndView ReliefConfigView() {
        ModelAndView modelAndView = new ModelAndView(RELIEF_CONFIG_TEMPLATE);
        modelAndView.addObject("disableButtons", true);

        return modelAndView;
    }
}