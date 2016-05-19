package com.erni.drools.poc.controller;

import com.sun.xml.internal.ws.developer.Serialization;
import org.apache.taglibs.standard.resources.Resources;
import org.drools.KnowledgeBase;
import org.drools.compiler.lang.DrlDumper;
import org.drools.compiler.lang.api.DescrFactory;
import org.drools.compiler.lang.api.PackageDescrBuilder;
import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.io.InputStream;

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
        build();
        model.addAttribute("greeting", "Hello again");
        return "welcome";
    }

    public KieContainer build() {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        //GROUP_ID, ARTIFACT_ID, VERSION
        ReleaseId rid = kieServices.newReleaseId("com.erni.drools",
                "GeneraliDroolsPOC", "1.0.0");
        kieFileSystem.generateAndWritePomXML(rid);

        kieFileSystem.write("/src/main/resources/rule1.drl",getResource(kieServices, "rule1.drl"));

        addRule(kieFileSystem);

        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();
        if (kieBuilder.getResults().hasMessages(Message.Level.ERROR)) {
            throw new RuntimeException("Build Errors:\n" +
                    kieBuilder.getResults().toString());
        }
        KieContainer kieContainer = kieServices.newKieContainer(rid);
        KieSession session = kieContainer.newKieSession();
        return kieServices.newKieContainer(rid);
    }

    private void addRule(KieFileSystem kieFileSystem) {
        PackageDescrBuilder packageDescrBuilder = DescrFactory.newPackage();
        packageDescrBuilder
                .name("com.erni.drools.poc")
                .newRule()
                .name("Rule 2")
                .lhs()
                .pattern("com.erni.drools.poc.model.Account").constraint("balance < 18").end()
                .end()
                .rhs("System.out.println(\"Warning! money running out!\");")
                .end();

        String rules = new DrlDumper().dump(packageDescrBuilder.getDescr());
        kieFileSystem.write("src/main/resources/rule1.drl", rules);
    }

    private Resource getResource(KieServices kieServices, String resourcePath) {
        try {
            InputStream is = com.google.common.io.Resources.getResource(resourcePath).openStream(); //guava
            return kieServices.getResources()
                    .newInputStreamResource(is)
                    .setResourceType(ResourceType.DRL);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load drools resource file.", e);
        }
    }

}
