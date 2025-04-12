package br.com.casadozeinfo.casadozeinfo_base.dto;

public class AccessTokenDTO {
    private String accessToken;

    public AccessTokenDTO() {}

    public AccessTokenDTO(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
