package com.erni.drools.poc.controller;

import com.sun.xml.internal.ws.developer.Serialization;
import org.apache.taglibs.standard.resources.Resources;
import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.compiler.lang.DrlDumper;
import org.drools.compiler.lang.api.DescrFactory;
import org.drools.compiler.lang.api.PackageDescrBuilder;
import org.drools.compiler.lang.descr.*;
import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.xml.ResourceEntityResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletContext;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;

@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    ServletContext context;

    @Bean
    public KieContainer kieContainer() throws IOException {
        KieServices ks = KieServices.Factory.get();
        return ks.getKieClasspathContainer();
    }

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
        addDrlFile();
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
/*
    private static void addRule(){
        final KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        Collection<KnowledgePackage> pkgs;
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        final  StatefulKnowledgeSession ksession;
        // this will parse and compile in one step
        // read from file
        kbuilder.add( ResourceFactory.newClassPathResource("rule1.drl"), org.drools.builder.ResourceType.DRL );

        // read second rule from String
        String myRule = "import com.erni.drools.poc.model.Account rule \"Hello World 2\" when  Account( balance < 200 ) then System.out.println(\"Test, Drools!\"); end";
        org.drools.io.Resource myResource = ResourceFactory.newByteArrayResource(myRule.getBytes());
        kbuilder.add(myResource, org.drools.builder.ResourceType.DRL);

        // Check the builder for errors
        if ( kbuilder.hasErrors() ) {
            System.out.println( kbuilder.getErrors().toString() );
            throw new RuntimeException( "Unable to compile drl\"." );
        }

        // get the compiled packages (which are serializable)
        pkgs = kbuilder.getKnowledgePackages();

        // add the packages to a knowledgebase (deploy the knowledge packages).
        kbase.addKnowledgePackages( pkgs );

        ksession = kbase.newStatefulKnowledgeSession();
        ksession.fireAllRules();
        ksession.dispose();
    }
*/
    private void addDrlFile(){
        // -------package section-------
        PackageDescr pkg=new PackageDescr();
        pkg.setName("com.erni.drools.rules");

// -------import section here-------
        ImportDescr importEntry1= new ImportDescr();
        importEntry1.setTarget("com.erni.drools.poc.model.Account");
        pkg.addImport(importEntry1);
        /*ImportDescr importEntry2= new ImportDescr();
        importEntry2.setTarget("com.demo.model.PotentialCustomer");
        pkg.addImport(importEntry2);

        ImportDescr importEntry3= new ImportDescr();
        importEntry3.setTarget("com.demo.model.PaymentMethod");
        pkg.addImport(importEntry3);
        */
//-------global section here-------
        GlobalDescr globalEntry=new GlobalDescr();
        globalEntry.setType("org.slf4j.Logger");
        globalEntry.setIdentifier("logger");
        pkg.addGlobal(globalEntry);

//------- rule section here
        RuleDescr ruleEntry=new RuleDescr();
        ruleEntry.setName("accountBalanceAtLeast2");

// ------- lhs starts here -------
        AndDescr lhs=new AndDescr();

//-------  pattern starts here -------
        PatternDescr patternEntry1=new PatternDescr();
        patternEntry1.setIdentifier("$account");
        patternEntry1.setObjectType("Account");

//------- ExprConstraint starts here -------
        ExprConstraintDescr ecd1=new ExprConstraintDescr();
        ecd1.setExpression("balance");
        ExprConstraintDescr ecd2=new ExprConstraintDescr();
        ecd2.setExpression("200");
//-------  Added exprConstraint into relational expr-------
        RelationalExprDescr red1=new RelationalExprDescr("<",false, null, ecd1, ecd2);
/*
        ExprConstraintDescr ecd3=new ExprConstraintDescr();
        ecd3.setExpression("subTotal");
        ExprConstraintDescr ecd4=new ExprConstraintDescr();
        ecd4.setExpression("300");
        RelationalExprDescr red2=new RelationalExprDescr(">",false, null, ecd3, ecd4);
*/

        patternEntry1.addConstraint(red1);
        //patternEntry1.addConstraint(red2);
        lhs.addDescr(patternEntry1);
/*
        NotDescr notDescr=new NotDescr();
        notDescr.setText("not");


        PatternDescr pattDescr1=new PatternDescr();
        pattDescr1.setObjectType("PotentialCustomer");

        ExprConstraintDescr ecd11=new ExprConstraintDescr();
        ecd11.setExpression("customerName");
        ExprConstraintDescr ecd12=new ExprConstraintDescr();
        ecd12.setExpression("$p.getCustomerName()");
        RelationalExprDescr red11=new RelationalExprDescr("==",false, null, ecd11,ecd12);
        pattDescr1.addConstraint(red11);
        notDescr.addDescr(pattDescr1);

        lhs.addDescr(notDescr);
*/

        ruleEntry.setLhs(lhs);

        pkg.addRule(ruleEntry);
        String s = new DrlDumper().dump( pkg );
        // here drl is in form of String
        createFile(s);
    }

    private void createFile(String s) {
        Writer writer = null;
        URL resourceUrl = null;
        try {
            Enumeration<URL> urls = context.getClassLoader().getResources("rule1.drl");
            resourceUrl = urls.nextElement();
        } catch (IOException ex) {
            System.out.println(ex);
        }
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(new File(resourceUrl.toURI()))));
            writer.write(s);
        }catch(URISyntaxException ex){
            System.out.println(ex);
        }catch (IOException ex) {
            System.out.println(ex);
        } finally {
            try {
                writer.close();
            }catch (Exception ex){
                System.out.println(ex.getMessage());
            }
        }
    }
}
