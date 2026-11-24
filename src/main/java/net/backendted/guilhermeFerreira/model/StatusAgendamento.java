package net.backendted.guilhermeFerreira.model;

public enum StatusAgendamento {
    AGUARDANDO("Aguardando", 3),
    CONFIRMADO("Confirmado", 2),
    EM_ATENDIMENTO("Em atendimento", 1),
    ATENDIDO("Atendido", 4),
    CANCELADO("Cancelado", 5);

    private final String descricao;
    private final int prioridade;

    StatusAgendamento(String descricao, int prioridade) {
        this.descricao = descricao;
        this.prioridade = prioridade;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getPrioridade() {
        return prioridade;
    }
}
