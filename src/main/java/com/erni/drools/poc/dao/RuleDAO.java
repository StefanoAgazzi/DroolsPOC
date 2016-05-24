package com.erni.drools.poc.dao;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

/**
 * Created by agst on 23/05/2016.
 */
public class RuleDAO {

    public void saveRule(HashMap<String, String> ruleParams, ServletContext context) throws IOException {
        String path = "edited_rule.drl";
        context.getClassLoader().getResource(path);

        String when_entity = ruleParams.get("when_entity");
        String when_condition = ruleParams.get("when_condition");
        String when_condition_value = ruleParams.get("when_condition_value");
        String when_entity_field = ruleParams.get("when_entity_field");

        String rule_left_side = "";
        if ("account".equalsIgnoreCase(when_entity) && "balance".equalsIgnoreCase(when_entity_field)){
            rule_left_side = "$account : Account( " + when_entity_field  + " " + when_condition + " "  + when_condition_value + " )";
        }

//      List<String> lines = Files.readAllLines(Paths.get(context.getRealPath("/WEB-INF/classes/"+path)));

        String new_line = System.lineSeparator();

        String rule =
                "package  com.erni.drools.poc " + new_line +

                        "import com.erni.drools.poc.model.Account;" + new_line +
                        "import com.erni.drools.poc.model.User;" + new_line +

                        "rule \"accountBalanceAtLeast\"" + new_line +
                        "when" + new_line +
                        rule_left_side + new_line +
                        "then" + new_line +
                        "System.out.println(\"Warning! money running out!\");" + new_line +
                        "end";


        Files.write(Paths.get(context.getRealPath("/WEB-INF/classes/"+path)), rule.getBytes());
    }
}
