package edu.uw.cse.ifrcdemo.setup.ui.mainmenu;

import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainMenuController {

    private static final String MAIN_MENU_TEMPLATE = "mainmenu/mainMenu";
    private final Logger logger;

    public MainMenuController(Logger logger){
        this.logger = logger;
    }

    @RequestMapping(value={"/mainmenu" , "/"})
    public ModelAndView MainMenuView() {
        ModelAndView modelAndView = new ModelAndView(MAIN_MENU_TEMPLATE);

        //this is to disable noRegMode on mainMenu.html.
        modelAndView.addObject("disableButtons", true);

        return modelAndView;
    }
}
