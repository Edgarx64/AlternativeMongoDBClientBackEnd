package com.controller;

import com.service.ConvertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class ApplicationController {

    private final ConvertService convertService;

    @Autowired
    public ApplicationController(ConvertService convertService) {
        this.convertService = convertService;
    }

    @GetMapping("/get")
    public String get(@RequestParam("query") String query) {
        return convertService.query(query);
    }
}
