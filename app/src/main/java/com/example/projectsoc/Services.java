package com.example.projectsoc;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;


import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class Services extends Fragment implements OnMapReadyCallback {

    private GoogleMap mGoogleMap;
    private View mView;
    private FusedLocationProviderClient client;

    private Address address = null;

    private String latitude;
    private String longtitude;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.services_fragment,container,false);
        requestPermission();
        client = LocationServices.getFusedLocationProviderClient(getActivity());
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MapView mMapView = (MapView) mView.findViewById(R.id.map);
        if(mMapView != null){
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());

        mGoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (ActivityCompat.checkSelfPermission(getContext(), ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            return;
        }

        client.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if( location != null ){
                    latitude = String.valueOf(location.getLatitude());
                    longtitude = String.valueOf(location.getLongitude());
                    LatLng positionBasic = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longtitude));
                    CameraPosition cameraPositionBasic = CameraPosition.builder().target(positionBasic).zoom(17).bearing(0)
                            .tilt(45).build();
                    googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPositionBasic));
                    MarkerOptions options = new MarkerOptions().position(positionBasic).title("TY!");
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                    mGoogleMap.addMarker(options);

                    if (getActivity().getIntent().getStringExtra("PolohaPsa")!= null){
                        LatLng position = new LatLng(geoLocate().getLatitude(), geoLocate().getLongitude());

                        MarkerOptions optionsForHome = new MarkerOptions().position(position).title("DOMOV PSÍKA");
                        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                        CameraPosition cameraPosition = CameraPosition.builder().target(position).zoom(16).bearing(0)
                                .tilt(45).build();
                        mGoogleMap.addMarker(optionsForHome);
                        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }
                }
            }
        });


    }

    public void requestPermission(){
        ActivityCompat.requestPermissions(getActivity(),new String[]{ACCESS_FINE_LOCATION},1);
    }

    public Address geoLocate(){
        String searchString = getActivity().getIntent().getStringExtra("PolohaPsa");
        Geocoder geocoder = new Geocoder(getContext());
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString,1);
        }catch (IOException e){
            e.printStackTrace();
        }
        if (list.size()>0){
            address = list.get(0);
            System.out.println(address.toString());
        }
        return address;
    }
}
