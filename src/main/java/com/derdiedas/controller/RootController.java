package com.derdiedas.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller which simply exposes the root path so a nice message is displayed when the software is accessed via browser.
 * A link to the API documentation is given.
 */
@RestController
@RequestMapping(path = "/")
public class RootController {

    @GetMapping(path = "")
    public String home() {
        return "Welcome to the DerDieDas software. Please access our <a href=\"/docs/api/index.html\">API documentation</a> for using understanding our software. You can also access our <a href=\"/docs/apidocs/index.html\">Javadoc here.</a>.";
    }
}
