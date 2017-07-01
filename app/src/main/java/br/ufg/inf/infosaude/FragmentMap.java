package br.ufg.inf.infosaude;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import br.ufg.inf.infosaude.utils.MapUtils;

public class FragmentMap extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);

            } else {
                //TODO Verificar essas verificacoes, ver a necessidade de tratar quando ja estao
                //TODO habilitados
            }
        }

        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setIndoorLevelPickerEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);

        Location localizacaoAtual = MapUtils.retornaLocalizacaoAtual(getContext());

        MapUtils.zoomToLocation(mMap, new LatLng(localizacaoAtual.getLatitude(), localizacaoAtual.getLongitude()), MapUtils.ZOOM_FACTOR);
    }

    //FIXME japa, ver a necessidade de ter isso aqui dentro desse fragment
    //FIXME talvez sera necessario colocar essa logica dentro do outro fragment ( O de persistir info sobre os hosp)
//    @Override
//    public void onResponse(Call<List<Hospital>> call, Response<List<Hospital>> response) {
//        int code = response.code();
//        if (code == 200) {
//            List<Hospital> listHospitais = response.body();
//            for(Hospital hospital : listHospitais){
//                LatLng hospitalLoc = new LatLng(hospital.getLatitude(),hospital.getLongitude());
//                mMap.addMarker(new MarkerOptions().position(hospitalLoc).title(hospital.getNome()));
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(hospitalLoc));
//            }
//        } else {
//            Toast.makeText(super.getActivity(), "Não encontrado: " + String.valueOf(code), Toast.LENGTH_LONG).show();
//        }
//    }
//
//    @Override
//    public void onFailure(Call<List<Hospital>> call, Throwable t) {
//        Toast.makeText(super.getActivity(), "Erro ao recuperar hospitais", Toast.LENGTH_LONG).show();
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_act_map, viewGroup, false);
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SupportMapFragment fragment = new SupportMapFragment();
        transaction.replace(R.id.map, fragment);
        transaction.commit();

        fragment.getMapAsync(this);
        return view;
    }
}
