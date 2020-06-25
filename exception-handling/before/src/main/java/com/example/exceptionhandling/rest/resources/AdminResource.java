package com.example.exceptionhandling.rest.resources;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/admin")
public class AdminResource {

    @GetMapping(path = "/demo", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String demo() {
        return new JSONObject()
                .put("timestamp", LocalDateTime.now())
                .put("message", "Access granted")
                .toString();
    }
}
