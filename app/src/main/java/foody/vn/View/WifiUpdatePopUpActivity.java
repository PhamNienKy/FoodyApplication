package foody.vn.View;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import foody.vn.Controller.WifiUpdateController;
import foody.vn.Model.WifiRestaurantModel;
import foody.vn.R;

public class WifiUpdatePopUpActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edWifiName, edWifiPassword;
    Button btnAgreeUpdateWifi;

    WifiUpdateController wifiUpdateController;
    String restaurantID;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_wifi_update_popup);

        edWifiName = (EditText) findViewById(R.id.edWifiName);
        edWifiPassword = (EditText) findViewById(R.id.edWifiPassword);
        btnAgreeUpdateWifi = (Button) findViewById(R.id.btnAgreeUpdateWifi);

        btnAgreeUpdateWifi.setOnClickListener(this);
        restaurantID = getIntent().getStringExtra("restaurantID");

        wifiUpdateController = new WifiUpdateController(this);
    }

    @Override
    public void onClick(View v) {
        String wifiName = edWifiName.getText().toString();
        String wifiPassword = edWifiPassword.getText().toString();

        if(!wifiName.trim().isEmpty() && !wifiPassword.trim().isEmpty()){
            Calendar calendar = Calendar.getInstance();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String dateSubmit = simpleDateFormat.format(calendar.getTime());

            WifiRestaurantModel wifiRestaurantModel = new WifiRestaurantModel();
            wifiRestaurantModel.setName(wifiName);
            wifiRestaurantModel.setPassword(wifiPassword);
            wifiRestaurantModel.setDate_submit(dateSubmit);
            wifiUpdateController.addWifiRestaurant(this, wifiRestaurantModel, restaurantID);
        } else {
            Toast.makeText(this, getResources().getString(R.string.error_adding_wifi), Toast.LENGTH_SHORT).show();
        }

    }
}
