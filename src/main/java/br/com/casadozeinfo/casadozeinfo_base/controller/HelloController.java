package br.com.casadozeinfo.casadozeinfo_base.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Bem-vindo à Casa Doze Info!";
    }
}
