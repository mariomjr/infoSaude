package br.ufg.inf.infosaude.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;

import br.ufg.inf.infosaude.R;
import br.ufg.inf.infosaude.adapters.DataAdapter;
import br.ufg.inf.infosaude.model.Favorito;
import br.ufg.inf.infosaude.services.FavoritoService;
import br.ufg.inf.infosaude.services.ServicesUtils;
import br.ufg.inf.infosaude.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteActivity extends BaseActivity implements Callback<List<Favorito>> {

    private RecyclerView recyclerView;
    private List<Favorito> data;
    private DataAdapter adapter;
    private FavoritoService mService;
    protected SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorito);
        initViews();
    }

    private void initViews() {
        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        session = new SessionManager(getApplicationContext());

        loadJSON();
    }

    private void loadJSON() {
        HashMap<String, String> usuario = session.getUserDetails();

        mService = ServicesUtils.getFavoritoService();

        Call<List<Favorito>> call = mService.getListFavoritos(usuario.get(SessionManager.KEY_EMAIL));

        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<List<Favorito>> call, Response<List<Favorito>> response) {
        if (response.isSuccessful()) {
            data = response.body();
            adapter = new DataAdapter(data);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onFailure(Call<List<Favorito>> call, Throwable t) {

    }
}
