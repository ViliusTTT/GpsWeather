package com.easygo.vilius.pasiklydauapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Klase, kurioje apdorojamas google map ir lokacijos radimo veiksmai
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    public static final String TAG = "Kursinis_";   //Tagas
    private GoogleMap mMap;                         //GoogleMap objektas
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;//Leidimo prasymo uzklausos kodas
    Geocoder geocoder = new Geocoder(this, Locale.getDefault()); //Geocoder objektas, randantis adresa
    String postalCode;                                          //Pasto indeksas
    private Location mLastLocation;                             //Paskutine zinoma vietove
    public LocationManager mLocationManager;                    //LocationManager objektas
    double latitude;                                            //Platuma
    double longitude;                                           //Ilguma
    int updates;                                                //Atnaujinimai
    String x="Location not found, restart!";                    //Pranesimas isvedamas neradus koordinaciu
    String x2="";

    /**
     * onCreate metodas vykdanttis visus veiksmus,paleidziantis google map serviza
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "OnCreate");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        updates = 0;
        handlePermissionsAndGetLocation();
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /**
     * Praso leidimu reikalingiems veiksmams
     * @param requestCode - uzklausos kodas
     * @param permissions - reikalingi leidimai
     * @param grantResults - rezultatai
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Accepted
                    getLocation();
                } else {
                    // Denied
                    Toast.makeText(MapsActivity.this, "LOCATION Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * Uzklausia leidimu, juos gavus gauna dabartine vietove
     */
    private void handlePermissionsAndGetLocation() {
        Log.v(TAG, "handlePermissionsAndGetLocation");
        int hasWriteContactsPermission = checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
        getLocation();//if already has permission
    }

    /**
     * Grazina dabartine vietove
     */
    protected void getLocation() {
        Log.v(TAG, "GetLocation");
        int LOCATION_REFRESH_TIME = 1000;
        int LOCATION_REFRESH_DISTANCE = 5;

        if (!(checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            Log.v(TAG, "Has permission");
            mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                    LOCATION_REFRESH_DISTANCE, mLocationListener);
            Location loc =mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(loc!=null) {
                latitude = loc.getLatitude();
                longitude = loc.getLongitude();

            }
        } else {
            Log.v(TAG, "Does not have permission");
        }

    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.v(TAG, "Location Change");

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    /**
     * onClick metodas sms,istorijos, oru mygtuko apdorojimui
     * @param v - View objektas
     */
    public void onClick(View v){
        if(v.getId()==R.id.sms_btn) {
            Intent intent = new Intent(getBaseContext(), SmsActivity.class);
            String l= x+" coordinates "+longitude+" longitude, "+latitude+" latitude ";
            intent.putExtra("adresas", l);
            if(latitude==0)
                Toast.makeText(getApplicationContext(), "Incorrect coordinates", Toast.LENGTH_LONG).show();
            else {
                startActivity(intent);
            }

        }
        if(v.getId()==R.id.email_btn){
            Intent intent2 = new Intent(getBaseContext(), HistoryActivity.class);
            String l= x+" coordinates "+longitude+" longitude, "+latitude+" latitude ";
            String l2= x2;
            intent2.putExtra("adresas", l);
            intent2.putExtra("tikadresas", l2);
            intent2.putExtra("postal", postalCode);
            intent2.putExtra("latitude", latitude);
            intent2.putExtra("longitude", longitude);
            startActivity(intent2);
        }
        if(v.getId()==R.id.weather_btn){
            Intent intent3 = new Intent(getBaseContext(), WeatherActivity.class);
            String l= x+" coordinates "+longitude+" longitude, "+latitude+" latitude ";
            String l2= x2;
            intent3.putExtra("adresas", l);
            intent3.putExtra("tikadresas", l2);
            intent3.putExtra("postal", postalCode);
            intent3.putExtra("latitude", String.valueOf(latitude));
            intent3.putExtra("longitude", String.valueOf(longitude));
            if(latitude==0)
                Toast.makeText(getApplicationContext(), "Incorrect coordinates", Toast.LENGTH_LONG).show();
            else {
                startActivity(intent3);
            }
        }
     


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

        try {
            List<android.location.Address> addreses=geocoder.getFromLocation(latitude,longitude,1);
            if( addreses.size()>0) {
                x = "I am at " + addreses.get(0).getAddressLine(0) + " , " + addreses.get(0).getCountryName();
                x2=addreses.get(0).getAddressLine(0)+"   ";
           postalCode=addreses.get(0).getPostalCode();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        // Add a marker in Sydney and move the camera

        LatLng sydney = new LatLng(latitude, longitude);
        Marker m =mMap.addMarker(new MarkerOptions().position(sydney).title(x));

        m.showInfoWindow();
       float zoomLevel = 17; //This goes up to 21
        LatLng latLng = new LatLng(latitude, longitude);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
    }
}
