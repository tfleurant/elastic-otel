package com.example.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicController {

    private final Logger logger = LoggerFactory.getLogger(BasicController.class);

    @GetMapping("/hello")
    public void hello(){
        logger.info("OK");
    }
}
