package br.ufg.inf.infosaude.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import br.ufg.inf.infosaude.R;
import br.ufg.inf.infosaude.adapters.ListHospitalAdapter;
import br.ufg.inf.infosaude.model.Especialidades;

public class ListHospitaisFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_hospitais_list, container, false);
            RecyclerView rvEspecialidades =  (RecyclerView) view.findViewById(R.id.rv_list);
            rvEspecialidades.setHasFixedSize(true);

            List<Especialidades> especialidades = null;

            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            rvEspecialidades.setLayoutManager(llm);

            LatLng latlong = new LatLng(2121,2121);
            especialidades = buscarHospitais(latlong);
            ListHospitalAdapter adapter = new ListHospitalAdapter(getActivity(), especialidades);
            rvEspecialidades.setAdapter( adapter );


            return view;
        }

    private List<Especialidades> buscarHospitais(LatLng latlong) {
        return null;
    }
}
