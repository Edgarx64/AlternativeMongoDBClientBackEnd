package com.controller;

import com.manager.SQLQueryToMongoDBManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationController {

    private final SQLQueryToMongoDBManager manager;

    @Autowired
    public ApplicationController(SQLQueryToMongoDBManager manager) {
        this.manager = manager;
    }

    @GetMapping("/get")
    public String get(@RequestParam("query") String query) {
        return manager.getDataByQuery(query);
    }
}
