package com.erni.drools.poc.dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by agst on 23/05/2016.
 */
public class RuleDAO {

    public void SaveRULE() throws IOException {
        // TODO enable jdk >= 7 support to fix that error
        List<String> lines = Files.readAllLines(Paths.get("res/nashorn1.js"));
        lines.add("print('foobar');");
        Files.write(Paths.get("res/nashorn1-modified.js"), lines);
    }
}
