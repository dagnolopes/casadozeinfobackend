package br.com.casadozeinfo.casadozeinfo_base.dto;

public class UsuarioLoginDTO {
    private String login;
    private String senha; // se for o caso de autenticar

    public UsuarioLoginDTO() {}

    public UsuarioLoginDTO(String login, String senha) {
        this.login = login;
        this.senha = senha;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
