package com.example.transformertracker.fragments;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.transformertracker.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GetLocationFragment extends FillFormFragment {

    private GoogleMap mMap;
   // private GoogleMap.OnCameraIdleListener onCameraIdleListener;
    private ImageView imgPin;
    private FloatingActionButton floatingActionButton;
    LatLng latLng;
    private OnFragmentInteractionListener mListener;




    @Override
    public void getMapFragment(SupportMapFragment mapFragment, View view, OnFragmentInteractionListener fragmentInteractionListener) {
        mListener = fragmentInteractionListener;
        floatingActionButton = view.findViewById(R.id.fabGetDetails);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLatLng(latLng);


            }
        });


    }




    private void getLatLng(LatLng latLng){

        mListener.onFragmentInteraction(latLng);
        Toast.makeText(getActivity(), "position : " + latLng.latitude + ", " + latLng.longitude, Toast.LENGTH_LONG).show();
        getDialog().dismiss();




    }




}
