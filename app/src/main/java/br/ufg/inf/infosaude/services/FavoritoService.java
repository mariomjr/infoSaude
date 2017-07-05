package br.ufg.inf.infosaude.services;

import java.util.List;

import br.ufg.inf.infosaude.model.Favorito;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by MarioJr on 28/06/2017.
 */

public interface FavoritoService {

    @GET("usuarios/{email}/favoritos")
    Call<List<Favorito>> getListFavoritos(@Query("email") String email);
}
