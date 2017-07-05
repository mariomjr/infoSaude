package br.ufg.inf.infosaude.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Leonard0Rocha on 04/07/2017.
 */

public class Especialidades {

    @SerializedName("id")
    private Long id;


//    private  Integer area;

    @SerializedName("nome")
    private  String nome;

    @SerializedName("quantidade")
    private  Integer quantidade;

    public Especialidades(String nome, Integer quantidade){
        this.nome = nome;
        this.quantidade = quantidade;
    }

//    public Integer getArea() {
//        return area;
//    }
//
//    public void setArea(Integer area) {
//        this.area = area;
//    }

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
