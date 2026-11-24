package net.backendted.guilhermeFerreira.model;

import jakarta.persistence.*;

@Entity
@Table(name = "convenios")
public class Convenio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private boolean aceito = true;

    public Convenio() {}

    public Convenio(String nome, String descricao, boolean aceito) {
        this.nome = nome;
        this.descricao = descricao;
        this.aceito = aceito;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public boolean isAceito() { return aceito; }
    public void setAceito(boolean aceito) { this.aceito = aceito; }
}
