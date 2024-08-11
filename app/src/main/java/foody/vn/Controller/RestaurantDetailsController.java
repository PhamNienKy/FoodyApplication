package foody.vn.Controller;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import foody.vn.Controller.Interfaces.RestaurantDetailsInterface;
import foody.vn.Model.WifiRestaurantModel;

public class RestaurantDetailsController {
    WifiRestaurantModel wifiRestaurantModel;
    List<WifiRestaurantModel> wifiRestaurantModelList;
    public RestaurantDetailsController() {
        wifiRestaurantModel = new WifiRestaurantModel();
        wifiRestaurantModelList = new ArrayList<>();
    }
    public void showListOfWifiRestaurants(String restaurantID, final TextView txtWifiName, final TextView txtWifiPassword, final TextView txtDateSubmit){

        RestaurantDetailsInterface restaurantDetailsInterface = new RestaurantDetailsInterface() {
            @Override
            public void showListOfWifiRestaurants(WifiRestaurantModel wifiRestaurantModel) {
                wifiRestaurantModelList.add(wifiRestaurantModel);
                txtWifiName.setText(wifiRestaurantModel.getName());
                txtWifiPassword.setText(wifiRestaurantModel.getPassword());
                txtDateSubmit.setText(wifiRestaurantModel.getDate_submit());

            }
        };
        wifiRestaurantModel.getListOfWifiRestaurants(restaurantID, restaurantDetailsInterface);
    }
}
