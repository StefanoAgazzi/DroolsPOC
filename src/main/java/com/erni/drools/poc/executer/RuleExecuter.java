package com.erni.drools.poc.executer;


import com.erni.drools.poc.model.Account;
import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatelessKnowledgeSession;

public class RuleExecuter {
	public static final void main(String[] args) {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("rule.drl"), ResourceType.DRL);
		KnowledgeBase kbase = kbuilder.newKnowledgeBase();
		StatelessKnowledgeSession ksession = kbase.newStatelessKnowledgeSession();
		Account account = new Account(200);
		account.withdraw(150);
		ksession.execute(account);

	}
}