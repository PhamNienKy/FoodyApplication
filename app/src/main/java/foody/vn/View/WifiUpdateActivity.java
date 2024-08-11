package foody.vn.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import foody.vn.Controller.WifiUpdateController;
import foody.vn.R;

public class WifiUpdateActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnUpdateWifi;
    RecyclerView recyclerWifi;
    WifiUpdateController updateWifiController;
    String restaurantID;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_wifi_update);

        btnUpdateWifi = (Button) findViewById(R.id.btnUpdateWifi);
        recyclerWifi = (RecyclerView) findViewById(R.id.recyclerWifi);

        restaurantID = getIntent().getStringExtra("restaurantID");

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerWifi.setLayoutManager(layoutManager);

        updateWifiController = new WifiUpdateController(this);
        updateWifiController.showListOfWifi(restaurantID, recyclerWifi);

        btnUpdateWifi.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent iPopUp = new Intent(this, WifiUpdatePopUpActivity.class);
        iPopUp.putExtra("restaurantID", restaurantID);
        startActivity(iPopUp);
    }
}
