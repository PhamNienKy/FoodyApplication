package foody.vn.View;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import foody.vn.Controller.LeadTheWayToRestaurantController;
import foody.vn.R;

public class LeadTheWayToRestaurantActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap googleMap;
    MapFragment mapFragment;
    double latitude = 0, longitude = 0;
    SharedPreferences sharedPreferences;
    Location currentLocation;
    LeadTheWayToRestaurantController leadTheWayToRestaurantController;
    String url = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_lead_the_way);

        leadTheWayToRestaurantController = new LeadTheWayToRestaurantController();

        latitude = getIntent().getDoubleExtra("Latitude", 0);
        longitude = getIntent().getDoubleExtra("Longitude", 0);

        sharedPreferences = getSharedPreferences("Location", Context.MODE_PRIVATE);
        currentLocation = new Location("");
        currentLocation.setLatitude(Double.parseDouble(sharedPreferences.getString("Latitude", "15.673")));
        currentLocation.setLongitude(Double.parseDouble(sharedPreferences.getString("Longitude", "108.4121")));

        url = "https://maps.googleapis.com/maps/api/directions/json?units=metric&origin=" + currentLocation.getLatitude() + "," + currentLocation.getLongitude() + "&destination=" + latitude + "," + longitude + "&key=AIzaSyA9qHZ7UUjXILvCUlKA8lnIKkrrHklw1ho";
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;

        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        googleMap.addMarker(markerOptions);

        LatLng restaurantLocation = new LatLng(latitude, longitude);
        MarkerOptions markerRestaurantLocation = new MarkerOptions();
        markerRestaurantLocation.position(restaurantLocation);
        googleMap.addMarker(markerRestaurantLocation);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 12);
        googleMap.moveCamera(cameraUpdate);

        leadTheWayToRestaurantController.showLeadTheWayToRestaurant(googleMap, url);
    }
}
