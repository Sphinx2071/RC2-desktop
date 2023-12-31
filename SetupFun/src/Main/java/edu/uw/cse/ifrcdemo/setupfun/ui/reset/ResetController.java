package edu.uw.cse.ifrcdemo.setupfun.ui.reset;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ResetController {

    private static final String RESET_TEMPLATE = "resetserver/resetServer";
    private static final Logger logger = LogManager.getLogger(ResetController.class);



    public ResetController() {
    }

    //todo: this button needs to call LoginTask and ResetTask from SharedLib, both use an object that use swing
    @RequestMapping("/resetserver")
    public ModelAndView ResetView() {
        ModelAndView modelAndView = new ModelAndView(RESET_TEMPLATE);

        return modelAndView;
    }


}