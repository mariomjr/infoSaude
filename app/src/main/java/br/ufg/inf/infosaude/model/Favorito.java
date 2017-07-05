package br.ufg.inf.infosaude.model;

/**
 * Created by astr1x on 05/07/17.
 */

public class Favorito {

    private Long id;
    private String nomeHospital;

    public String getNomeHospital() {
        return nomeHospital;
    }

    public void setNomeHospital(String nomeHospital) {
        this.nomeHospital = nomeHospital;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
