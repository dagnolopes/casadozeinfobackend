package br.com.casadozeinfo.casadozeinfo_base.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/private")
public class PrivateApiController {

    @GetMapping("/data")
    public String getPrivateData() {
        // Aqui você pode retornar dados privados para o usuário
        return "Dados privados acessados com sucesso!";
    }

    @GetMapping("/endpoint")
    public String getPrivateendpoint() {
        // Aqui você pode retornar dados privados para o usuário
        return "Dados privados acessados com sucesso (endpoint)!";
    }
}
