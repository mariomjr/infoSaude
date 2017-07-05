package br.ufg.inf.infosaude.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import br.ufg.inf.infosaude.R;
import br.ufg.inf.infosaude.utils.GPSTracker;
import br.ufg.inf.infosaude.utils.MapUtils;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class MapFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnInfoWindowClickListener{

    private GoogleMap mMap;

    private static GoogleApiClient mGoogleApiClient;

    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE =1;

    private static final int PROXIMITY_RADIUS =5000;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private static final String GOOGLE_BROWSER_API_KEY= "AIzaSyA016QgQU5d_sClXD6nc7iQg8Jo3CF1_k0";

    public static final String LATITUDE = "lat";
    public static final String LONGITUDE = "lng";
    public static final String GEOMETRY = "geometry";
    public static final String NAME = "name";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        checkLocationPermission();

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
       // mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);

        Location localizacaoAtual = MapUtils.retornaLocalizacaoAtual(getContext());
        GPSTracker location = new GPSTracker(getContext());

        MapUtils.zoomToLocation(mMap, new LatLng(location.getLatitude(), location.getLongitude()), MapUtils.ZOOM_FACTOR);
      //  MapUtils.zoomToLocation(mMap, new LatLng(-16.6773716, -49.2458615), MapUtils.ZOOM_FACTOR);

        loadNearByPlaces(location.getLatitude(),location.getLongitude());

        initApiPlace();
        mGoogleApiClient.connect();
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this.getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this.getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                super.getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(Place.TYPE_HOSPITAL)
                .build();
        autocompleteFragment.setFilter(typeFilter);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                LatLng latLong = place.getLatLng();
                MapUtils.zoomToLocation(mMap, new LatLng(latLong.latitude, latLong.longitude), MapUtils.ZOOM_FACTOR);
                mMap.addMarker(new MarkerOptions().position(latLong).title(place.getName().toString()));
            }

            @Override
            public void onError(Status status) {

                Log.i(TAG, "Erro: " + status);
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

    private void loadNearByPlaces(double latitude, double longitude) {

        ArrayList resultList = null;
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            String type = "hospital";
            StringBuilder sb =
                    new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            sb.append("location=").append(latitude).append(",").append(longitude);
            sb.append("&radius=").append(PROXIMITY_RADIUS);
            sb.append("&types=").append(type);
            sb.append("&sensor=true");
            sb.append("&key=" + GOOGLE_BROWSER_API_KEY);

            URL url = new URL(sb.toString());

            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        double latitudeAux, longitudeAux;
        String placeName = "";
        String endereco = "";
        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("results");

            for (int i = 0; i < predsJsonArray.length(); i++) {
                JSONObject place = predsJsonArray.getJSONObject(i);


                if (!place.isNull(NAME)) {
                    placeName = place.getString(NAME);
                }

                endereco = place.getString("vicinity");

                latitudeAux = place.getJSONObject(GEOMETRY).getJSONObject("location")
                        .getDouble(LATITUDE);
                longitudeAux = place.getJSONObject(GEOMETRY).getJSONObject("location")
                        .getDouble(LONGITUDE);
                //reference = place.getString(REFERENCE);
                //icon = place.getString(ICON);

                MarkerOptions markerOptions = new MarkerOptions();
                LatLng latLng = new LatLng(latitudeAux, longitudeAux);
                markerOptions.position(latLng);
                markerOptions.title(placeName);
                markerOptions.snippet(endereco);
                markerOptions.icon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                mMap.addMarker(markerOptions);

            }
        } catch (JSONException e) {
            // Log.e(LOG_TAG, "Cannot process JSON results", e);
        }
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
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

   // @Override
    //public boolean onMarkerClick(Marker marker) {
        //Log.d(TAG, "Lat.-"+marker.getPosition().latitude+" Long.-"+marker.getPosition().longitude);
   //     return false;
   // }

    @Override
    public void onInfoWindowClick(Marker marker) {

        Bundle bundle=new Bundle();
        bundle.putString("nome", marker.getTitle());
        bundle.putString("endereco", marker.getSnippet());
        bundle.putDouble("latitude", marker.getPosition().latitude);
        bundle.putDouble("longitude", marker.getPosition().longitude);

        ListHospitaisFragment nextFrag= new ListHospitaisFragment();
        nextFrag.setArguments(bundle);
        this.getFragmentManager().beginTransaction()
                .replace(R.id.frame, nextFrag)
                .addToBackStack("MapFragment")
                .commit();
        Log.d(TAG, "Lat.-"+marker.getPosition().latitude+" Long.-"+marker.getPosition().longitude);
    }

    @Override
    public void onResume() {
        //mMap.onResume();
        super.onResume();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        getFragmentManager().beginTransaction().remove(this).commit();
    }

}
