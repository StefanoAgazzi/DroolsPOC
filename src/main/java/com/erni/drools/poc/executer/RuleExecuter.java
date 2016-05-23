package com.erni.drools.poc.executer;


import com.erni.drools.poc.model.Account;
import com.erni.drools.poc.model.User;
import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatelessKnowledgeSession;

import java.math.BigDecimal;

public class RuleExecuter {
    public static final void main(String[] args) {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        //suspend_user_with_negative_balance
        kbuilder.add(ResourceFactory.newClassPathResource("rule.drl"), ResourceType.DRL);
        KnowledgeBase kbase = kbuilder.newKnowledgeBase();
        StatelessKnowledgeSession ksession = kbase.newStatelessKnowledgeSession();
        User user = new User();
        user.setId(1L);
        Account account = new Account(200, user.getId());
        account.withdraw(750);
        ksession.execute(account);
    }

//    public void executeDRL(String rulePath) {
////        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
////        kbuilder.add(ResourceFactory.newClassPathResource(rulePath), ResourceType.DRL);
////        KnowledgeBase kbase = kbuilder.newKnowledgeBase();
////        StatelessKnowledgeSession ksession = kbase.newStatelessKnowledgeSession();
////        User user = new User();
////        user.setId(1L);
////        Account account = new Account(new BigDecimal(200), user.getId());
////        account.withdraw(new BigDecimal(150));
////        ksession.execute(account);
//    }
}