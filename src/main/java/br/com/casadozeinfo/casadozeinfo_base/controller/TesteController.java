package br.com.casadozeinfo.casadozeinfo_base.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TesteController {

    @GetMapping("/api/teste")
    public String testarCors() {
        return "Requisição CORS bem-sucedida!";
    }
}
