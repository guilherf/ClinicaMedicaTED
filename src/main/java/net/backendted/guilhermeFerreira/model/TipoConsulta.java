package net.backendted.guilhermeFerreira.model;

public enum TipoConsulta {
    PRIMEIRA_CONSULTA("Primeira consulta"),
    RETORNO("Retorno");

    private final String descricao;

    TipoConsulta(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
