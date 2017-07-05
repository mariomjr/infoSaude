package br.ufg.inf.infosaude.model;

/**
 * Created by Leonard0Rocha on 04/07/2017.
 */

public class Especialidades {

    private  String nome;
    private  Integer quantidade;

    public Especialidades(String nome, Integer quantidade){
        this.nome = nome;
        this.quantidade = quantidade;
    }

    public String getNome() {
        return nome;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
