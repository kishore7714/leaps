package com.leaps.error;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/error")
public class ErrorController {

    @Autowired
    private ErrorService errorService;

    @GetMapping("/{id}")
    public String geterrorbyid(@PathVariable String id) {
        return errorService.getErrorById(id);
    }
}
