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

import java.util.List;

import br.ufg.inf.infosaude.R;
import br.ufg.inf.infosaude.adapters.HospitalInfoAdapter;
import br.ufg.inf.infosaude.model.Especialidades;
import br.ufg.inf.infosaude.services.EspecialidadesService;
import br.ufg.inf.infosaude.services.ServicesUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HospitalInfoFragment extends Fragment
        implements Callback<List<Especialidades>> {

        RecyclerView rvEspecialidades;

    private EspecialidadesService mService;

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

            View view = inflater.inflate(R.layout.fragment_hospital_info, container, false);
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
            mService = ServicesUtils.getEspecialidadeService();

            Call<List<Especialidades>> call = mService.getListEspecialidades(latitude, longitude);
            call.enqueue(this);
        }

    @Override
    public void onResponse(Call<List<Especialidades>> call, Response<List<Especialidades>> response) {
        if (response.isSuccessful()) {

            especialidades = response.body();

            if (especialidades != null) {
                HospitalInfoAdapter adapter = new HospitalInfoAdapter(getActivity(), especialidades);
                rvEspecialidades.setAdapter(adapter);
            }
        }else {
            Toast.makeText(super.getActivity(), R.string.conexao_erro, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFailure(Call<List<Especialidades>> call, Throwable t) {
        Toast.makeText(super.getActivity(), R.string.conexao_erro, Toast.LENGTH_LONG).show();
    }
}
