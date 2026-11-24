package net.backendted.guilhermeFerreira.model;

import jakarta.persistence.*;

@Entity
@Table(name = "profissionais")
public class Profissional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String especialidade;

    @Column(nullable = false)
    private String crm;

    @Column(unique = true)
    private String email;

    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusProfissional status = StatusProfissional.DISPONIVEL;

    public Profissional() {}

    public Profissional(String nome, String especialidade, String crm) {
        this.nome = nome;
        this.especialidade = especialidade;
        this.crm = crm;
        this.status = StatusProfissional.DISPONIVEL;
    }

    public Profissional(String nome, String especialidade, String crm, String email, String senha) {
        this.nome = nome;
        this.especialidade = especialidade;
        this.crm = crm;
        this.email = email;
        this.senha = senha;
        this.status = StatusProfissional.DISPONIVEL;
    }

    public String getStatusDescricao() {
        return status != null ? status.getDescricao() : "Disponível";
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }
    public String getCrm() { return crm; }
    public void setCrm(String crm) { this.crm = crm; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public StatusProfissional getStatus() { return status; }
    public void setStatus(StatusProfissional status) { this.status = status; }
}
