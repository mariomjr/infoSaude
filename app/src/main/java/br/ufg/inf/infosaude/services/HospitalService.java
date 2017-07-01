package br.ufg.inf.infosaude.services;

import java.util.List;

import br.ufg.inf.infosaude.model.Hospital;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by MarioJr on 28/06/2017.
 */

public interface HospitalService {

    @GET("hospitais")
    Call<List<Hospital>> getListHospitais();
}
