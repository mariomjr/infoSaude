package br.ufg.inf.infosaude.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import br.ufg.inf.infosaude.R;
import br.ufg.inf.infosaude.utils.MapUtils;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class MapFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener{

    private GoogleMap mMap;

    private static GoogleApiClient mGoogleApiClient;

    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE =1;

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

 //       MapUtils.zoomToLocation(mMap, new LatLng(localizacaoAtual.getLatitude(), localizacaoAtual.getLongitude()), MapUtils.ZOOM_FACTOR);
        MapUtils.zoomToLocation(mMap, new LatLng(-16.6773716, -49.2458615), MapUtils.ZOOM_FACTOR);

        initApiPlace();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                super.getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(Place.TYPE_COUNTRY).setCountry("BR")
                .setTypeFilter(Place.TYPE_HOSPITAL)
                .build();
        autocompleteFragment.setFilter(typeFilter);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName());//get place details here
            }

            @Override
            public void onError(Status status) {

                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(super.getContext(), data);
                Log.i(TAG, "Place:" + place.toString());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(super.getContext(), data);
                Log.i(TAG, status.getStatusMessage());
            } else if (requestCode == RESULT_CANCELED) {

            }
        }
    }

    private void initApiPlace() {
        mGoogleApiClient = new GoogleApiClient
                .Builder(super.getContext())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(), this)
                .build();
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
        View view = inflater.inflate(R.layout.fragment_map, viewGroup, false);
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SupportMapFragment fragment = new SupportMapFragment();
        transaction.replace(R.id.map, fragment);
        transaction.commit();

        fragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
