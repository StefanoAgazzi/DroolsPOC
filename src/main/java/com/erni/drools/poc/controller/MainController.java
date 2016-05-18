package com.erni.drools.poc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class MainController {

    @RequestMapping(method = RequestMethod.GET)
    public String sayHello(ModelMap model) {
        model.addAttribute("greeting", "Hello from Generali Drools POC");
        return "welcome";
    }


    @RequestMapping(value = "/helloagain", method = RequestMethod.GET)
    public String sayHelloAgain(ModelMap model) {
        model.addAttribute("greeting", "Hello again");
        return "welcome";
    }

    @RequestMapping(value = "/saveconfig", method = RequestMethod.POST)
    public String result(@RequestParam String text1, @RequestParam String text2, Model model) {

        model.addAttribute("greeting", "Hello again");
        return "welcome";
    }

}
