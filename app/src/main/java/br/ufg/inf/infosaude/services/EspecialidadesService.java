package br.ufg.inf.infosaude.services;

import java.util.List;

import br.ufg.inf.infosaude.model.Especialidades;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by astr1x on 05/07/17.
 */

public interface EspecialidadesService {

    @GET("especialidades/")
    Call<List<Especialidades>> getListEspecialidades(@Query("latitudade") Double latitude, @Query("longitude") Double longitude);
}