package com.HerexFullStack.Escuela.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @GetMapping("/holaseg")
    public String secHelloworld(){
        return "Hola mundo conseguridad.";
    }

    @GetMapping("/holanoseg")
    public String noSecHelloWorld(){
        return "Hola mundo sin seguridad.";
    }
}
