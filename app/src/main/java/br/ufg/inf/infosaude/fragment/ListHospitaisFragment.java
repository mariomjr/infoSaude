package br.ufg.inf.infosaude.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import br.ufg.inf.infosaude.R;
import br.ufg.inf.infosaude.adapters.ListHospitalAdapter;
import br.ufg.inf.infosaude.model.Especialidades;
import br.ufg.inf.infosaude.model.Usuario;
import br.ufg.inf.infosaude.services.HospitalService;
import br.ufg.inf.infosaude.services.ServicesUtils;
import br.ufg.inf.infosaude.services.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.password;

public class ListHospitaisFragment extends Fragment
        implements Callback<List<Especialidades>> {

        RecyclerView rvEspecialidades;

        private HospitalService mService;

        List<Especialidades> especialidades;


        private Double latitude;
        private Double longitude;

        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container,
                                 Bundle savedInstanceState) {

            String nomeHospital = getArguments().getString("nome");
            String enderecoHospital = getArguments().getString("endereco");
            latitude =  getArguments().getDouble("latitude");
            longitude =  getArguments().getDouble("longitude");



            View view = inflater.inflate(R.layout.fragment_hospitais_list, container, false);
            rvEspecialidades =  (RecyclerView) view.findViewById(R.id.rv_list);
            rvEspecialidades.setHasFixedSize(true);

            TextView nomeHospitalView = (TextView) view.findViewById(R.id.nomeHospital);
            nomeHospitalView.setText(nomeHospital);

            TextView enderecoHospitalView = (TextView) view.findViewById(R.id.enderecoHospital);
            enderecoHospitalView.setText(enderecoHospital);

            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            rvEspecialidades.setLayoutManager(llm);

            return view;
        }

        @Override
        public void onStart (){
            super.onStart();
            mService = ServicesUtils.getHospitalService();

            Call<List<Especialidades>> call = mService.getListEspecialidades(latitude, longitude);
            call.enqueue(this);
        }

    @Override
    public void onResponse(Call<List<Especialidades>> call, Response<List<Especialidades>> response) {
        if (response.isSuccessful()) {

            especialidades = response.body();

            if (especialidades != null) {
                ListHospitalAdapter adapter = new ListHospitalAdapter(getActivity(), especialidades);
                rvEspecialidades.setAdapter(adapter);
            }
        }else {
            Toast.makeText(super.getActivity(), R.string.conexao_erro, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFailure(Call<List<Especialidades>> call, Throwable t) {

    }
}
