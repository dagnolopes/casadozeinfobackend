package br.com.casadozeinfo.casadozeinfo_base.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tbusuario", schema = "public")
public class Usuario {

    @Id
    @Column(name = "idusuario")
    private Long idusuario;

    @Column(name = "idperfil")
    private Long idperfil;

    @Column(name = "loginusuario")
    private String loginusuario;

    @Column(name = "nomeusuario")
    private String nomeusuario;

    @Column(name = "emailusuario")
    private String emailusuario;

    @Column(name = "senhausuario")
    private String senhausuario;

    @Column(name = "datacadastrousuario")
    private LocalDateTime datacadastrousuario;

    @Column(name = "dataacessousuario")
    private LocalDateTime dataacessousuario;

    @Column(name = "idsistemapadraousuario")
    private Long idsistemapadraousuario;

    @Column(name = "idsistemaultiomoacessousuario")
    private Long idsistemaultiomoacessousuario;

    @Column(name = "statususuario")
    private String statususuario;

    @Column(name = "idcliente")
    private Long idcliente;

    @Column(name = "idcontrato")
    private Long idcontrato;

    @Column(name = "idunidade")
    private Long idunidade;
}
