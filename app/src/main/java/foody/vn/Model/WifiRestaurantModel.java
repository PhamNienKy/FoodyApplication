package foody.vn.Model;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import foody.vn.Controller.Interfaces.RestaurantDetailsInterface;
import foody.vn.R;

public class WifiRestaurantModel {
    String name, password, date_submit;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDate_submit() {
        return date_submit;
    }

    public void setDate_submit(String date_submit) {
        this.date_submit = date_submit;
    }

    public void getListOfWifiRestaurants(String restaurantID, RestaurantDetailsInterface restaurantDetailsInterface){
        DatabaseReference nodeWifiRestaurant = FirebaseDatabase.getInstance().getReference().child("wifi_restaurants").child(restaurantID);

        nodeWifiRestaurant.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String previousChildName) {
                WifiRestaurantModel wifiRestaurantModel = dataSnapshot.getValue(WifiRestaurantModel.class);
                restaurantDetailsInterface.showListOfWifiRestaurants(wifiRestaurantModel);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String previousChildName) {
                WifiRestaurantModel wifiRestaurantModel = dataSnapshot.getValue(WifiRestaurantModel.class);
                restaurantDetailsInterface.showListOfWifiRestaurants(wifiRestaurantModel);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void addRestaurantWifi(final Context context, WifiRestaurantModel wifiRestaurantModel, String restaurantID){
        DatabaseReference nodeWifiRestaurant = FirebaseDatabase.getInstance().getReference().child("wifi_restaurants").child(restaurantID);
        nodeWifiRestaurant.push().setValue(wifiRestaurantModel, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(context, context.getResources().getString(R.string.add_wifi_success), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
