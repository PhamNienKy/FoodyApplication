package foody.vn.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import foody.vn.Adapters.AdapterComment;
import foody.vn.Controller.MenuController;
import foody.vn.Controller.RestaurantDetailsController;
import foody.vn.Model.RestaurantModel;
import foody.vn.Model.UtilityModel;
import foody.vn.R;


public class RestaurantDetailsActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {
    TextView txtRestaurantName, txtRestaurantAddress, txtOperatingTime, txtOperatingStatus, txtPriceLimit;
    TextView txtTotalImage, txtTotalCheckin, txtTotalComment, txtTotalSave, txtTitleToolbar;
    TextView txtWifiName, txtWifiPassword, txtDateSubmit;
    Button btnComment;
    ImageView imgRestaurant, imgPlayTrailer;
    VideoView videoTrailer;
    RestaurantModel restaurantModel;

    Toolbar toolbar;

    RecyclerView recyclerViewComment, recyclerMenu;

    AdapterComment adapterComment;
    GoogleMap googleMap;
    MapFragment mapFragment;
    LinearLayout frameUtility, frameWifi;
    View frameFeatures;
    RestaurantDetailsController restaurantDetailsController;
    MenuController menuController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_restaurant_details);

        restaurantModel = getIntent().getParcelableExtra("restaurant");

        txtRestaurantName = findViewById(R.id.txtRestaurantName);
        txtRestaurantAddress = findViewById(R.id.txtRestaurantAddress);
        txtOperatingTime = findViewById(R.id.txtOperatingTime);
        txtOperatingStatus = findViewById(R.id.txtOperatingStatus);
        txtTotalImage = findViewById(R.id.txtTotalImage);
        txtTotalCheckin = findViewById(R.id.txtTotalCheckin);
        txtTotalComment =findViewById(R.id.txtTotalComment);
        txtTotalSave = findViewById(R.id.txtTotalSave);
        txtPriceLimit =findViewById(R.id.txtPriceLimit);
        imgRestaurant = findViewById(R.id.imgRestaurant);
        txtTitleToolbar = findViewById(R.id.txtTitleToolbar);
        toolbar = findViewById(R.id.toolbar);
        recyclerViewComment = findViewById(R.id.recyclerRestaurantDetailsComment);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        frameUtility = findViewById(R.id.frameUtility);
        txtWifiName = findViewById(R.id.txtWifiName);
        txtWifiPassword = findViewById(R.id.txtWifiPassword);
        txtDateSubmit = findViewById(R.id.txtDateSubmit);
        frameWifi = findViewById(R.id.frameWifi);
        frameFeatures = findViewById(R.id.frameFeatures);
        btnComment = findViewById(R.id.btnComment);
        videoTrailer = findViewById(R.id.videoTrailer);
        imgPlayTrailer = findViewById(R.id.imgPlayTrailer);
        recyclerMenu = findViewById(R.id.recyclerMenu);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        restaurantDetailsController = new RestaurantDetailsController();
        menuController = new MenuController();

        mapFragment.getMapAsync(this);

        ShowRestaurantDetails();

        frameFeatures.setOnClickListener(this);
        btnComment.setOnClickListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    private void ShowRestaurantDetails(){
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

        String currentTime = dateFormat.format(calendar.getTime());
        String openTime = restaurantModel.getOpen_time();
        String closeTime = restaurantModel.getClose_time();

        try {
            Date openDate = dateFormat.parse(openTime);
            Date closeDate = dateFormat.parse(closeTime);
            Date currentDate = dateFormat.parse(currentTime);

            if(currentDate.after(openDate) && currentDate.before(closeDate)){
                txtOperatingStatus.setText(getString(R.string.currently_open));
            } else {
                txtOperatingStatus.setText(getString(R.string.closed));
            }

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        txtTitleToolbar.setText(restaurantModel.getRestaurant_name());

        txtRestaurantName.setText(restaurantModel.getRestaurant_name());
        txtRestaurantAddress.setText(restaurantModel.getBranchesModelList().get(0).getAddress());
        txtOperatingTime.setText(restaurantModel.getOpen_time() + " - " + restaurantModel.getClose_time());
        txtTotalImage.setText(restaurantModel.getRestaurant_img().size() + " ");
        txtTotalComment.setText(restaurantModel.getCommentModelList().size() + " ");
        txtOperatingTime.setText(openTime + " - " + closeTime);

        DownloadUtility();

        if(restaurantModel.getMax_price() != 0 && restaurantModel.getMin_price() != 0){
            NumberFormat numberFormat = new DecimalFormat("###,###");
            String minPrice = numberFormat.format(restaurantModel.getMin_price()) + " đ";
            String maxPrice = numberFormat.format(restaurantModel.getMax_price()) + " đ";
            txtPriceLimit.setText(minPrice + " - " + maxPrice);
        } else {
            txtPriceLimit.setVisibility(View.INVISIBLE);
        }


        //download trailer video
        if(restaurantModel.getIntro_video() != null){
            videoTrailer.setVisibility(View.VISIBLE);
            imgRestaurant.setVisibility(View.GONE);
            imgPlayTrailer.setVisibility(View.VISIBLE);
            StorageReference storageVideo = FirebaseStorage.getInstance().getReference().child("videos").child(restaurantModel.getIntro_video());
            storageVideo.getDownloadUrl().addOnSuccessListener(uri -> {
                videoTrailer.setVideoURI(uri);
                videoTrailer.seekTo(1);
            });
            imgPlayTrailer.setOnClickListener(v -> {
                MediaController mediaController = new MediaController(this);
                videoTrailer.setMediaController(mediaController);
                videoTrailer.start();
                imgPlayTrailer.setVisibility(View.GONE);
            });

        } else {
            videoTrailer.setVisibility(View.GONE);
            imgRestaurant.setVisibility(View.VISIBLE);
            imgPlayTrailer.setVisibility(View.GONE);
        }

        //download images
        StorageReference storageImage = FirebaseStorage.getInstance().getReference().child("images").child(restaurantModel.getRestaurant_img().get(0));
        long ONE_MEGABYTE = 1024 * 1024;
        storageImage.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            imgRestaurant.setImageBitmap(bitmap);
        });

        //Load list of restaurant comment
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewComment.setLayoutManager(layoutManager);
        adapterComment = new AdapterComment(this, R.layout.custom_layout_comment, restaurantModel.getCommentModelList());
        recyclerViewComment.setAdapter(adapterComment);
        adapterComment.notifyDataSetChanged();

        NestedScrollView nestedScrollViewDetails = findViewById(R.id.nestedScrollViewDetails);
        nestedScrollViewDetails.smoothScrollBy(0, 0);

        restaurantDetailsController.showListOfWifiRestaurants(restaurantModel.getRestaurant_id(), txtWifiName, txtWifiPassword, txtDateSubmit);
        frameWifi.setOnClickListener(v -> {
            Intent iWifi = new Intent(RestaurantDetailsActivity.this, WifiUpdateActivity.class);
            iWifi.putExtra("restaurantID", restaurantModel.getRestaurant_id());
            startActivity(iWifi);
        });

        menuController.getListOfMenuByID(this, restaurantModel.getRestaurant_id(), recyclerMenu);

    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;

        double latitude = restaurantModel.getBranchesModelList().get(0).getLatitude();
        double longitude = restaurantModel.getBranchesModelList().get(0).getLongitude();

        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(restaurantModel.getRestaurant_name());
        Log.d("restaurant", latLng + "");

        googleMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 12);
        googleMap.moveCamera(cameraUpdate);
    }

    private void DownloadUtility(){
        for(String utilityID : restaurantModel.getUtility()){
            DatabaseReference nodeUtility = FirebaseDatabase.getInstance().getReference().child("utility_management").child(utilityID);
            nodeUtility.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UtilityModel utilityModel = dataSnapshot.getValue(UtilityModel.class);

                    StorageReference storageImage = FirebaseStorage.getInstance().getReference().child("utility").child(utilityModel.getUtility_image());
                    long ONE_MEGABYTE = 1024 * 1024;
                    storageImage.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        ImageView imageUtility = new ImageView(RestaurantDetailsActivity.this);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(50, 50);
                        layoutParams.setMargins(10, 10, 10, 10);
                        imageUtility.setLayoutParams(layoutParams);
                        imageUtility.setScaleType(ImageView.ScaleType.FIT_XY);
                        imageUtility.setPadding(5, 5, 5, 5);

                        imageUtility.setImageBitmap(bitmap);
                        frameUtility.addView(imageUtility);
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.frameFeatures){
            Intent iLeadTheWay = new Intent(RestaurantDetailsActivity.this, LeadTheWayToRestaurantActivity.class);
            iLeadTheWay.putExtra("latitude", restaurantModel.getBranchesModelList().get(0).getLatitude());
            iLeadTheWay.putExtra("longitude", restaurantModel.getBranchesModelList().get(0).getLongitude());
            startActivity(iLeadTheWay);
        } else if (id == R.id.btnComment){
            Intent iComment = new Intent(RestaurantDetailsActivity.this, CommentActivity.class);
            iComment.putExtra("restaurantID", restaurantModel.getRestaurant_id());
            iComment.putExtra("restaurantName", restaurantModel.getRestaurant_name());
            iComment.putExtra("restaurantAddress", restaurantModel.getBranchesModelList().get(0).getAddress());
            startActivity(iComment);
        }
    }
}
