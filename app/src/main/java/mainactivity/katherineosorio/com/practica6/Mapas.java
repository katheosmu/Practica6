package mainactivity.katherineosorio.com.practica6;

import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class Mapas extends FragmentActivity implements
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    LocationListener {

    private GoogleMap map;
    private CameraUpdate cameraUpdate;

    //private DataBaseManager Manager;
    private Cursor cursor;

    private final LatLng LOCATION_HOME = new LatLng(6.205662, -75.588472);
    private final LatLng LOCATION_UDEA = new LatLng(6.253041, -75.564574);

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private LatLng currentLocation;

    private boolean newLocationReady = false;

    Bundle bun;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapas);
        bun = savedInstanceState;
        //Manager = new DataBaseManager(getApplicationContext());

        map =((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
        //map.addMarker(new MarkerOptions().position(LOCATION_HOME).title("My Home").snippet("Carrera 44 # 4 sur 35").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        //map.addMarker(new MarkerOptions().position(LOCATION_UDEA).title("Universit of Antoquia").snippet("Calle 67 # 53-108"));

        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        cameraUpdate = CameraUpdateFactory.newLatLngZoom(LOCATION_HOME, 8);
        map.animateCamera(cameraUpdate);
        cargarRest();
        buildGoogleApiClient();
        createLocationRequest();
    }

    public void onClickHome(View view){
        map.addMarker(new MarkerOptions()
                .position(LOCATION_HOME)
                .title("home")
                .snippet("Dirección")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        cameraUpdate = CameraUpdateFactory.newLatLngZoom(LOCATION_HOME, 16);
        map.animateCamera(cameraUpdate);
    }

    public void onClick(View view){
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        cameraUpdate = CameraUpdateFactory.newLatLngZoom(LOCATION_UDEA, 12);
        map.animateCamera(cameraUpdate);
    }

    public void onClickCurrent (View vew){
        onConnected(bun);
        Toast.makeText(getApplicationContext(), "bla bla", Toast.LENGTH_SHORT).show();
        if(newLocationReady){
            Toast.makeText(getApplicationContext(), "no hay ubcacion ", Toast.LENGTH_SHORT).show();
            map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLocation, 16);
            map.animateCamera(cameraUpdate);
        }
    }

    public void cargarRest(){
        DataBaseManager Manager = MainActivity.getManager();
        cursor = Manager.cargarCursorContactos();
        if (cursor.moveToFirst()){
            do{
                String dbnombre = cursor.getString(cursor.getColumnIndex(Manager.CN_NAME)).toString();
                String dblatitud = cursor.getString(cursor.getColumnIndex(Manager.CN_LAT)).toString();
                String dblongitud = cursor.getString(cursor.getColumnIndex(Manager.CN_LONGI)).toString();
                float lat = Float.parseFloat(dblatitud);
                float longitud = Float.parseFloat(dblongitud);
                final LatLng LOCATION_VAR = new LatLng(lat,longitud);
                Toast.makeText(getApplicationContext(), dbnombre, Toast.LENGTH_SHORT).show();
                map.addMarker(new MarkerOptions()
                        .position(LOCATION_VAR)
                        .title(dbnombre)
                        .snippet(dblatitud+", "+dblongitud)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }while (cursor.moveToNext());

        }
        else{
            Toast.makeText(getApplicationContext(), "no hay Restaurantes ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Toast.makeText(getApplicationContext(), "on Connected", Toast.LENGTH_SHORT).show();
        if(mLastLocation !=null){
            setNewLocation(mLastLocation);
            newLocationReady = true;
        }else{
            //LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) { }

    @Override
    public void onLocationChanged(Location location) {
        setNewLocation((location));
        newLocationReady = true;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }

    @Override
    public void onProviderEnabled(String provider) { }

    @Override
    public void onProviderDisabled(String provider) { }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) { }

    protected synchronized void  buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    protected void createLocationRequest(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void setNewLocation (Location location){
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        currentLocation = new LatLng(latitude,longitude);
        map.addMarker(new MarkerOptions()
                .position(currentLocation)
                .title("Find me here!")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mGoogleApiClient.isConnected()){
            //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
            mGoogleApiClient.disconnect();
        }
    }

    public void verPosicionActual(View view) {
        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locListener = new MiLocationListener();
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
    }

    public class MiLocationListener implements LocationListener {
        public void onLocationChanged(Location loc) {
            map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            loc.getLatitude();
            loc.getLongitude();

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(loc.getLatitude(), loc.getLongitude()), 15));

            LatLng pos_nosotros = new LatLng(loc.getLatitude(),loc.getLongitude());
            map.addMarker(new MarkerOptions().title("Aquí estoy yo").position(pos_nosotros));

        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub

        }
    }
}
