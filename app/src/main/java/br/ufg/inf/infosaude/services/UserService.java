package br.ufg.inf.infosaude.services;

import br.ufg.inf.infosaude.model.Usuario;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by astr1x on 01/07/17.
 */

public interface UserService {

    @GET("usuarios/")
    Call<Usuario> getUser(@Query("email") String email, @Query("password") String password);

    @POST("usuarios")
    Call<Usuario> createUser(@Body Usuario usuario);
}
