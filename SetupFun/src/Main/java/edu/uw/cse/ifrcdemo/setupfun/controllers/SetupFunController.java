package edu.uw.cse.ifrcdemo.setupfun.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SetupFunController {

    private static final String MAIN_MENU_TEMPLATE = "mainmenu/mainMenu";

    public SetupFunController(){}

    @RequestMapping ("/mainmenu")
    public ModelAndView MainMenuView(){
        ModelAndView modelAndView = new ModelAndView(MAIN_MENU_TEMPLATE);
        modelAndView.addObject("disableButtons", true);

        return modelAndView;
    }
}
