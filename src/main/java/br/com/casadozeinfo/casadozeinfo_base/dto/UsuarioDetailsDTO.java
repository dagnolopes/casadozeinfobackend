package br.com.casadozeinfo.casadozeinfo_base.dto;

public class UsuarioDetailsDTO {
    private String loginusuario;

    public UsuarioDetailsDTO() {}

    public UsuarioDetailsDTO(String loginusuario) {
        this.loginusuario = loginusuario;
    }

    public String getLoginusuario() {
        return loginusuario;
    }

    public void setLoginusuario(String loginusuario) {
        this.loginusuario = loginusuario;
    }
}
