package foody.vn.Controller;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import foody.vn.Adapters.AdapterWifi;
import foody.vn.Controller.Interfaces.RestaurantDetailsInterface;
import foody.vn.Model.WifiRestaurantModel;
import foody.vn.R;

public class WifiUpdateController {
    Context context;
    WifiRestaurantModel wifiRestaurantModel;
    List<WifiRestaurantModel> wifiRestaurantModelList;

    public WifiUpdateController(Context context){
        this.context = context;
        wifiRestaurantModel = new WifiRestaurantModel();
    }

    public void showListOfWifi (String restaurantID, RecyclerView recyclerWifi){
        wifiRestaurantModelList = new ArrayList<>();
        RestaurantDetailsInterface restaurantDetailsInterface = new RestaurantDetailsInterface() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void showListOfWifiRestaurants(WifiRestaurantModel wifiRestaurantModel) {
                wifiRestaurantModelList.add(wifiRestaurantModel);

                AdapterWifi adapterWifi = new AdapterWifi(context, R.layout.layout_wifi_restaurant_details, wifiRestaurantModelList);
                recyclerWifi.setAdapter(adapterWifi);
                adapterWifi.notifyDataSetChanged();

            }
        };
        wifiRestaurantModel.getListOfWifiRestaurants(restaurantID, restaurantDetailsInterface);
    }
    public void addWifiRestaurant(Context context, WifiRestaurantModel wifiRestaurantModel, String restaurantID){
        wifiRestaurantModel.addRestaurantWifi(context, wifiRestaurantModel, restaurantID);
    }
}
