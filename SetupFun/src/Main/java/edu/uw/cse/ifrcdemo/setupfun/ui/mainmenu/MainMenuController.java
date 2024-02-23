package edu.uw.cse.ifrcdemo.setupfun.ui.mainmenu;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainMenuController {

    private static final String MAIN_MENU_TEMPLATE = "mainmenu/mainMenu";
    private static final Logger logger = LogManager.getLogger(MainMenuController.class);



    public MainMenuController() {
    }


    @RequestMapping(value={"/mainmenu" , "/"})
    public ModelAndView MainMenuView() {
        ModelAndView modelAndView = new ModelAndView(MAIN_MENU_TEMPLATE);
        modelAndView.addObject("disableButtons", true);

        return modelAndView;
    }
}