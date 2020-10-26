package com.example.transformertracker.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
;
import com.example.transformertracker.R;
import com.example.transformertracker.model.Transformer;
import com.example.transformertracker.services.Services;
import com.example.transformertracker.view.AddUtility;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapFragments extends Fragment implements OnMapReadyCallback, AddUtility.OnFragmentInteractionListener {
    private static final String TAG = "ChildTrackingFragment";
//    private MyFragmentListenerImpl mFragmentCallback;
    private SupportMapFragment mapFragment;

    private GoogleMap mMap;
    private View root;
    private List<LatLng> latLngs;
    private TextView txt_position, txt_distanceToPoint;
    private FloatingActionButton mFloatingActionButton;
    private int counter = 0;
    private LatLngBounds.Builder builder = new LatLngBounds.Builder();
    private LatLng mLatLng;
    private int mZoom;


    public static MapFragments newInstance() {
        return new MapFragments();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root =  inflater.inflate(R.layout.location_map, container, false);
        initializingView();
        return root;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
//            mFragmentCallback = (MyFragmentListenerImpl) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

       checkLocationPermission();
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


    }

    private void refreshMap() {
        mMap.clear();
        mapFragment.onResume();
    }

    private void initializingView(){
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mFloatingActionButton = root.findViewById(R.id.fab_clock1);
        FloatingActionButton trrain_fbtn = root.findViewById(R.id.terrain);
        FloatingActionButton zonebtn = root.findViewById(R.id.categoryS);
//        trrain_fbtn.setOnClickListener();
        getAssets();

        mFloatingActionButton.setOnClickListener(v -> {
            Log.d(TAG, "initializingView: initializing");
            displayForm();
//            mFragmentCallback.onFabButtonClicked();
        });

    }

    private void getAssets(){
        ArrayList<Transformer> txList = new ArrayList<>();
        Log.d(TAG, "getAssets: called before getting transformers");
        LiveData<List<Transformer>> txLive = Services.getInstance(getActivity()).getMapDetails();
        txLive.observe(this, tx ->
        {
            Log.d(TAG, "getAssets: this show be observed1" + tx.get(0).name);

            displayOMap(tx);
        });
    }

    private void displayOMap(List<Transformer> list){

        for(Transformer tx : list){
            Log.d(TAG, "getAssets: this show be observed");
            Log.d(TAG, "displayOMap: " + tx.name);
            LatLng latLng = new LatLng(tx.latitude, tx.longitude);
            String details = tx.name + "\n" + tx.feeder +
                    "\n" + tx.rating + "\n" + tx.zone;
            setMarkerProperties(latLng, details);

        }
    }

    private void setMarkerProperties(LatLng latLng, String checkName){
//        int color = new Random().nextInt(360);
        Log.d(TAG, "setMarkerProperties: something marker");
        mZoom = 50;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,20));
         Marker marker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(checkName)
        );

        marker.showInfoWindow();


    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private final static int PERMISSION_REQUEST_LOCATION = 1234;
    private void checkLocationPermission() {
        Log.d(TAG, "checkLocationPermission() called");
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(getActivity(), "Please give me location permissions", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_REQUEST_LOCATION);
            }
        }
        else
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
          buildGoogleApiClient();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult() called with: requestCode = [" + requestCode + "], permissions = [" + permissions + "], grantResults = [" + grantResults + "]");
        switch (requestCode) {
            case PERMISSION_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
                       buildGoogleApiClient();
                    }
                } else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSION_REQUEST_LOCATION);
                }
                return;
            }
        }
    }

    private FusedLocationProviderClient mFusedLocationClient;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LatLng mCurrentLiveLocation;
    private LocationCallback mLocationCallbacks = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                if (location != null) {
                    setLatLng(location);

                    }
                }
            }
        };


    public synchronized void buildGoogleApiClient() {
        Log.d(TAG, "buildGoogleApiClient() called");
        mGoogleApiClient = new GoogleApiClient.Builder(root.getContext())
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        Log.d(TAG, "onConnected() called with: bundle = [" + bundle + "]");
                        mLocationRequest = new LocationRequest();
                        mLocationRequest.setInterval(100);
                        mLocationRequest.setFastestInterval(100);
                        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                        if (ContextCompat.checkSelfPermission(root.getContext(),
                                Manifest.permission.ACCESS_FINE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED)
                            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallbacks, Looper.myLooper());



                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        Log.d(TAG, "onConnectionSuspended() called with: i = [" + i + "]");
                    }
                })
                .addOnConnectionFailedListener(connectionResult -> Log.d(TAG, "onConnectionFailed() called with: connectionResult = [" + connectionResult + "]"))
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    public String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.getDefault());
        String time = sdf.format(new Date());
        return time;
    }

    public void setLatLng(Location location){
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        this.mLatLng = latLng;
    }

    public LatLng getLatlng(){
        return mLatLng;
    }

    public void displayForm(){
        Transformer tx = new Transformer();
        try {
            tx.latitude  =mLatLng.latitude;
            tx.longitude = mLatLng.longitude;
            tx.time = getTime();
            Bundle bundle = new Bundle();
            bundle.putParcelable(AddUtility.ARG_PARAM1, tx);
            AddUtility addUtility = new AddUtility();
            addUtility.setArguments(bundle);
            addUtility.show(getFragmentManager(), "Add Marker");
            Log.d(TAG, "displayForm: ");
        }
        catch (Exception e){
            Log.d(TAG, "displayForm: " + e.getMessage());
            Toast.makeText(getContext(), "Please turn on GPS to get current Location ", Toast.LENGTH_SHORT).show();
        }
    }
}
