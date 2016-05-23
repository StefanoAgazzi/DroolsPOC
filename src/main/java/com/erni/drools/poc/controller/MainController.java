package com.erni.drools.poc.controller;

import com.erni.drools.poc.dao.RuleDAO;
import org.drools.compiler.lang.DrlDumper;
import org.drools.compiler.lang.api.DescrFactory;
import org.drools.compiler.lang.api.PackageDescrBuilder;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.ReleaseId;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    ServletContext context;

    @RequestMapping(method = RequestMethod.GET)
    public String sayHello(ModelMap model) {
        return "welcome";
    }

    @RequestMapping(value = "/saverule", method = RequestMethod.POST)
    //TODO it is better to use a single Collection to map all the request params instead of using many @RequestParam
    public String result(@RequestParam String when_entity, @RequestParam String when_condition, @RequestParam String when_condition_value, @RequestParam String when_entity_field, Model model) {
        RuleDAO dao = new RuleDAO();
        HashMap<String, String> ruleParams = new HashMap<>();

        //TODO check isBlank()
        ruleParams.put("when_entity", when_entity);
        ruleParams.put("when_condition", when_condition);
        ruleParams.put("when_condition_value", when_condition_value);
        ruleParams.put("when_entity_field", when_entity_field);

        try {
            dao.saveRule(ruleParams, context);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "welcome";
    }

    public KieContainer build() {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        //GROUP_ID, ARTIFACT_ID, VERSION
        ReleaseId rid = kieServices.newReleaseId("com.erni.drools",
                "GeneraliDroolsPOC", "1.0.0");
        kieFileSystem.generateAndWritePomXML(rid);

        kieFileSystem.write("/src/main/resources/suspend_user_with_negative_balance.drl",getResource(kieServices, "suspend_user_with_negative_balance.drl"));

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
        kieFileSystem.write("src/main/resources/suspend_user_with_negative_balance.drl", rules);
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
