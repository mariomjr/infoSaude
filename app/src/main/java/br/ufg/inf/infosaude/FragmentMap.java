package br.ufg.inf.infosaude;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import br.ufg.inf.infosaude.model.Hospital;
import br.ufg.inf.infosaude.services.HospitalService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentMap extends SupportMapFragment implements OnMapReadyCallback, Callback<List<Hospital>>{

    private GoogleMap mMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng goiania = new LatLng(-16.6773716,-49.2458615);
        mMap.addMarker(new MarkerOptions().position(goiania).title("Marcador Praça Universitaria"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(goiania));


        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://private-9eeb1f-infosaude.apiary-mock.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        HospitalService hospitalService = retrofit.create(HospitalService.class);

        Call<List<Hospital>> call = hospitalService.getListHospitais();
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<List<Hospital>> call, Response<List<Hospital>> response) {
        int code = response.code();
        if (code == 200) {
            List<Hospital> listHospitais = response.body();
            for(Hospital hospital : listHospitais){
                LatLng hospitalLoc = new LatLng(hospital.getLatitude(),hospital.getLongitude());
                mMap.addMarker(new MarkerOptions().position(hospitalLoc).title(hospital.getNome()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(hospitalLoc));
            }
        } else {
            Toast.makeText(super.getActivity(), "Não encontrado: " + String.valueOf(code), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFailure(Call<List<Hospital>> call, Throwable t) {
        Toast.makeText(super.getActivity(), "Erro ao recuperar hospitais", Toast.LENGTH_LONG).show();
    }
}
