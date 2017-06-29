package br.ufg.inf.infosaude.model;

/**
 * Created by MarioJr on 28/06/2017.
 */

public class Hospital {

    private String nome;

    private Long latitude;

    private Long longitude;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getLatitude() {
        return latitude;
    }

    public void setLatitude(Long latitude) {
        this.latitude = latitude;
    }

    public Long getLongitude() {
        return longitude;
    }

    public void setLongitude(Long longitude) {
        this.longitude = longitude;
    }
}
