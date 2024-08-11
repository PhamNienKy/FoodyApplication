package foody.vn.View;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import foody.vn.Model.AddMenuModel;
import foody.vn.Model.FoodModel;
import foody.vn.Model.MenuModel;
import foody.vn.Model.RestaurantBranchesModel;
import foody.vn.Model.RestaurantModel;
import foody.vn.Model.UtilityModel;
import foody.vn.R;

public class AddRestaurantActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    int RESULT_IMG1 = 111;
    int RESULT_IMG2 = 112;
    int RESULT_IMG3 = 113;
    int RESULT_IMG4 = 114;
    int RESULT_IMG5 = 115;
    int RESULT_IMG6 = 116;
    int RESULT_VIDEO = 200;
    int RESULT_IMG_MENU = 300;

    Button btnOpenTime, btnCloseTime, btnAddRestaurant;
    EditText edRestaurantName, edMaxPrice, edMinPrice;
    RadioGroup rdgDeliveryStatus;
    String openTime;
    String closeTime;
    String area;
    static String restaurantID;
    Spinner spinnerArea;
    Toolbar toolbarAddRestaurant;

    LinearLayout frameUtility, frameBranch;
    LinearLayout frameContainsBranch, frameContainsMenu;

    List<String> areaList;
    List<String> menuList;
    List<String> selectedUtilityList;
    List<String> branchList;

    List<MenuModel> menuModelList;
    List<AddMenuModel> addMenuModelList;

    List<Bitmap> capImage;
    List<Uri> restaurantImg;
    ImageView imgTemp;
    Uri videoSelected;


    ArrayAdapter<String> adapterArea;
    VideoView videoView;
    ImageView imgVideo;
    ImageView imgRestaurantImage1, imgRestaurantImage2, imgRestaurantImage3;
    ImageView imgRestaurantImage4, imgRestaurantImage5, imgRestaurantImage6;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_restaurant);

        btnOpenTime = findViewById(R.id.btnOpenTime);
        btnCloseTime = findViewById(R.id.btnCloseTime);
        edMaxPrice = findViewById(R.id.edMaxPrice);
        edMinPrice = findViewById(R.id.edMinPrice);
        edRestaurantName = findViewById(R.id.edRestaurantName);
        rdgDeliveryStatus = findViewById(R.id.rdgDeliveryStatus);

        spinnerArea = findViewById(R.id.spinnerArea);
        frameUtility = findViewById(R.id.frameUtility);
        frameBranch = findViewById(R.id.frameCloneBranch);
        frameContainsBranch = findViewById(R.id.frameContainsBranch);
        frameContainsMenu = findViewById(R.id.frameContainsMenu);
        btnAddRestaurant = findViewById(R.id.btnAddRestaurant);

        imgVideo = findViewById(R.id.imgVideo);
        videoView = findViewById(R.id.videoView);
        imgRestaurantImage1 = findViewById(R.id.imgRestaurantImage1);
        imgRestaurantImage2 = findViewById(R.id.imgRestaurantImage2);
        imgRestaurantImage3 = findViewById(R.id.imgRestaurantImage3);
        imgRestaurantImage4 = findViewById(R.id.imgRestaurantImage4);
        imgRestaurantImage5 = findViewById(R.id.imgRestaurantImage5);
        imgRestaurantImage6 = findViewById(R.id.imgRestaurantImage6);
        toolbarAddRestaurant = findViewById(R.id.toolbarAddRestaurant);

        toolbarAddRestaurant.setTitle("");
        setSupportActionBar(toolbarAddRestaurant);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        areaList = new ArrayList<>();
        menuList = new ArrayList<>();
        selectedUtilityList = new ArrayList<>();
        branchList = new ArrayList<>();
        menuModelList = new ArrayList<>();
        addMenuModelList = new ArrayList<>();
        capImage = new ArrayList<>();
        restaurantImg = new ArrayList<>();

        CloneBranch();
        CloneMenu();

        adapterArea = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, areaList);
        spinnerArea.setAdapter(adapterArea);
        adapterArea.notifyDataSetChanged();

        GetListOfArea();
        GetListOfUtility();

        btnOpenTime.setOnClickListener(this);
        btnCloseTime.setOnClickListener(this);
        spinnerArea.setOnItemSelectedListener(this);

        imgVideo.setOnClickListener(this);
        imgRestaurantImage1.setOnClickListener(this);
        imgRestaurantImage2.setOnClickListener(this);
        imgRestaurantImage3.setOnClickListener(this);
        imgRestaurantImage4.setOnClickListener(this);
        imgRestaurantImage5.setOnClickListener(this);
        imgRestaurantImage6.setOnClickListener(this);

        btnAddRestaurant.setOnClickListener(this);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void CloneBranch (){
        @SuppressLint("InflateParams") final View view = LayoutInflater.from(AddRestaurantActivity.this).inflate(R.layout.layout_clone_branch, null);
        ImageButton btnAddBranch = view.findViewById(R.id.btnAddBranch);
        final ImageButton btnRemoveBranch = view.findViewById(R.id.btnRemoveBranch);

        btnAddBranch.setOnClickListener(v -> {
            EditText edtBranchName = view.findViewById(R.id.edBranchName);
            String branchName = edtBranchName.getText().toString();

            v.setVisibility(View.GONE);
            btnRemoveBranch.setVisibility(View.VISIBLE);
            btnRemoveBranch.setTag(branchName);

            branchList.add(branchName);
            Log.d("kiemtra", " " + branchList.size());
            CloneBranch();
        });

        btnRemoveBranch.setOnClickListener(v -> {
            String branchName = v.getTag().toString();
            branchList.remove(branchName);
            frameContainsBranch.removeView(view);
        });
        frameContainsBranch.addView(view);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_IMG1 && resultCode == RESULT_OK){
            Uri uri = data.getData();
            imgRestaurantImage1.setImageURI(uri);
            restaurantImg.add(uri);
        }
        else if(requestCode == RESULT_IMG2 && resultCode == RESULT_OK){
            Uri uri = data.getData();
            imgRestaurantImage2.setImageURI(uri);
            restaurantImg.add(uri);
        }
        else if(requestCode == RESULT_IMG3 && resultCode == RESULT_OK){
            Uri uri = data.getData();
            imgRestaurantImage3.setImageURI(uri);
            restaurantImg.add(uri);
        }
        else if(requestCode == RESULT_IMG4 && resultCode == RESULT_OK){
            Uri uri = data.getData();
            imgRestaurantImage4.setImageURI(uri);
            restaurantImg.add(uri);
        }
        else if(requestCode == RESULT_IMG5 && resultCode == RESULT_OK){
            Uri uri = data.getData();
            imgRestaurantImage5.setImageURI(uri);
            restaurantImg.add(uri);
        }
        else if(requestCode == RESULT_IMG6 && resultCode == RESULT_OK){
            Uri uri = data.getData();
            imgRestaurantImage6.setImageURI(uri);
            restaurantImg.add(uri);
        }
        else if (requestCode == RESULT_VIDEO && resultCode == RESULT_OK) {
            imgVideo.setVisibility(View.GONE);
            Uri uri = data.getData();
            videoSelected = uri;
            videoView.setVideoURI(uri);
            videoView.start();
        }
        else if (requestCode == RESULT_IMG_MENU && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imgTemp.setImageBitmap(bitmap);
            capImage.add(bitmap);
        }

    }

    private void CloneMenu (){
        @SuppressLint("InflateParams") View view = LayoutInflater.from(AddRestaurantActivity.this).inflate(R.layout.layout_clone_menu, null);
        final Spinner spinnerMenu = view.findViewById(R.id.spinnerMenu);
        Button btnAddMenu = view.findViewById(R.id.btnAddMenu);
        final EditText edFoodName = view.findViewById(R.id.edFoodName);
        EditText edPrice = view.findViewById(R.id.edPrice);
        ImageView imageTakeAPhoto = view.findViewById(R.id.imgTakeAPhoto);
        imgTemp = imageTakeAPhoto;

        ArrayAdapter<String> adapterMenu = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, menuList);
        spinnerMenu.setAdapter(adapterMenu);
        adapterMenu.notifyDataSetChanged();
        if(menuModelList.isEmpty()){
            GetListOfMenu(adapterMenu);
        }

        imageTakeAPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, RESULT_IMG_MENU);
        });

        btnAddMenu.setOnClickListener(v -> {
            v.setVisibility(View.GONE);

            long time = Calendar.getInstance().getTimeInMillis();
            String imgName = time + ".jpg";

            int position = spinnerMenu.getSelectedItemPosition();
            String menuID = menuModelList.get(position).getMenu_id();

            FoodModel foodModel = new FoodModel();
            foodModel.setFood_name(edFoodName.getText().toString());
            foodModel.setPrice(Long.parseLong(edPrice.getText().toString()));
            foodModel.setImage_food(imgName);

            AddMenuModel addMenuModel = new AddMenuModel();
            addMenuModel.setMenu_id(menuID);
            addMenuModel.setFoodModel(foodModel);

            addMenuModelList.add(addMenuModel);
            CloneMenu();
        });

        frameContainsMenu.addView(view);
    }
    private void GetListOfArea (){
        FirebaseDatabase.getInstance().getReference().child("areas").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot areaSnapshot : snapshot.getChildren()){
                    String areaName = areaSnapshot.getKey();
                    areaList.add(areaName);
                }

                adapterArea.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void GetListOfMenu (final ArrayAdapter<String> adapterMenu){
        FirebaseDatabase.getInstance().getReference().child("menus").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot menuSnapshot : snapshot.getChildren()){
                    MenuModel menuModel = new MenuModel();
                    String key = menuSnapshot.getKey();
                    String value = menuSnapshot.getValue(String.class);

                    menuModel.setMenu_id(key);
                    menuModel.setMenu_name(value);

                    menuModelList.add(menuModel);
                    menuList.add(value);
                }

                adapterMenu.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void GetListOfUtility (){
        FirebaseDatabase.getInstance().getReference().child("utility_management").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot utilitySnapshot : snapshot.getChildren()){
                    String utilityID = utilitySnapshot.getKey();
                    UtilityModel utilityModel = utilitySnapshot.getValue(UtilityModel.class);
                    utilityModel.setUtility_id(utilityID);

                    CheckBox checkBox = new CheckBox(AddRestaurantActivity.this);
                    checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    checkBox.setText(utilityModel.getUtility_name());
                    checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        String utilityID1 = utilityModel.getUtility_id();
                        if(isChecked){
                            selectedUtilityList.add(utilityID1);
                        }
                        else{
                            selectedUtilityList.remove(utilityID1);
                        }
                        Log.d("kiemtra", " " + selectedUtilityList.size());
                    });
                    frameUtility.addView(checkBox);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);


        if(id == R.id.imgRestaurantImage1){
            SelectImageFromGallery(RESULT_IMG1);
        }
        else if (id == R.id.imgRestaurantImage2) {
            SelectImageFromGallery(RESULT_IMG2);
        }
        else if (id == R.id.imgRestaurantImage3) {
            SelectImageFromGallery(RESULT_IMG3);
        }
        else if (id == R.id.imgRestaurantImage4) {
            SelectImageFromGallery(RESULT_IMG4);
        }
        else if (id == R.id.imgRestaurantImage5) {
            SelectImageFromGallery(RESULT_IMG5);
        }
        else if (id == R.id.imgRestaurantImage6) {
            SelectImageFromGallery(RESULT_IMG6);
        }
        else if (id == R.id.imgVideo) {
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Video ..."), RESULT_VIDEO);
        }
        else if(id == R.id.btnOpenTime){

            TimePickerDialog timePickerDialog = new TimePickerDialog(AddRestaurantActivity.this, (view, hourOfDay, minute1) -> {
                if(minute1 < 10){
                   openTime = hourOfDay + ":0" + minute1;
                }
                else{
                    openTime = hourOfDay + ":" + minute1;
                }
                ((Button) v).setText(openTime);
            }, hour, minute, true);
            timePickerDialog.show();
        }
        else if(id == R.id.btnCloseTime){
            TimePickerDialog timePickerDialog = new TimePickerDialog(AddRestaurantActivity.this, (view, hourOfDay, minute2) -> {
                if(minute2 < 10){
                    closeTime = hourOfDay + ":0" + minute2;
                }
                else{
                    closeTime = hourOfDay + ":" + minute2;
                }
                ((Button) v).setText(closeTime);
            }, hour, minute, true);
            timePickerDialog.show();
        } else if (id == R.id.btnAddRestaurant) {
            AddRestaurant();

        }
    }


    private void AddRestaurant(){

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.processing));
        progressDialog.setIndeterminate(true);

        progressDialog.show();
        String errorMessage = getString(R.string.error_signup_message);


        String restaurantName = edRestaurantName.getText().toString();
        int idRadioSelected = rdgDeliveryStatus.getCheckedRadioButtonId();
        boolean deliveryStatus;
        deliveryStatus = idRadioSelected == R.id.rdDelivery;

        String inputMax = edMaxPrice.getText().toString();
        long maxPrice;
        if (inputMax.isEmpty()) {
            maxPrice = 0;
        }
        else {
            maxPrice = Long.parseLong(edMaxPrice.getText().toString());
        }
        String inputMin = edMaxPrice.getText().toString();
        long minPrice;
        if (inputMin.isEmpty()) {
            minPrice = 0;
        }
        else {
            minPrice = Long.parseLong(edMinPrice.getText().toString());
        }

        if(restaurantName.trim().isEmpty()){
            errorMessage += " " + getString(R.string.restaurant_name);
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        } else if (maxPrice < minPrice) {
            Toast.makeText(this, getString(R.string.error_price_added_message), Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        } else{
            DatabaseReference nodeRoot = FirebaseDatabase.getInstance().getReference();
            DatabaseReference nodeRestaurant = nodeRoot.child("restaurants");
            restaurantID = nodeRestaurant.push().getKey();

            nodeRoot.child("areas").child(area).push().setValue(restaurantID);

            for (String branch : branchList) {
                String urlGeoCoding = "https://maps.googleapis.com/maps/api/geocode/json?address=" + branch.replace(" ", "%20") + "&key=AIzaSyA9qHZ7UUjXILvCUlKA8lnIKkrrHklw1ho";
                DownloadLocation downloadLocation = new DownloadLocation();
                downloadLocation.execute(urlGeoCoding);
            }

            RestaurantModel restaurantModel = new RestaurantModel();
            restaurantModel.setRestaurant_name(restaurantName);
            restaurantModel.setMax_price(maxPrice);
            restaurantModel.setMin_price(minPrice);
            restaurantModel.setOpen_time(openTime);
            restaurantModel.setClose_time(closeTime);
            restaurantModel.setDelivery(deliveryStatus);
            restaurantModel.setIntro_video(restaurantID + ".mp4");
            restaurantModel.setUtility(selectedUtilityList);
            restaurantModel.setLikes(0);

            if(videoSelected != null){
                FirebaseStorage.getInstance().getReference().child("videos/" + videoSelected.getLastPathSegment()).putFile(videoSelected);
            }

            if(!restaurantImg.isEmpty()) {
                for (Uri imgRestaurant : restaurantImg) {
                    Log.d("kiemtra", " " + imgRestaurant.getLastPathSegment());
                    FirebaseStorage.getInstance().getReference().child("images/" + imgRestaurant.getLastPathSegment()).putFile(imgRestaurant);
                    nodeRoot.child("restaurant_images").child(restaurantID).push().setValue(imgRestaurant.getLastPathSegment());
                }
            }

            for (int i = 0; i < addMenuModelList.size(); i++) {
                nodeRoot.child("restaurant_menus").child(restaurantID).child(addMenuModelList.get(i).getMenu_id()).push().setValue(addMenuModelList.get(i).getFoodModel());

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                Bitmap bitmap = capImage.get(i);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] data = byteArrayOutputStream.toByteArray();

                FirebaseStorage.getInstance().getReference().child("images/" + addMenuModelList.get(i).getFoodModel().getImage_food()).putBytes(data);
            }
            nodeRestaurant.child(restaurantID).setValue(restaurantModel).addOnSuccessListener(unused -> {
                progressDialog.dismiss();
                Toast.makeText(AddRestaurantActivity.this, getString(R.string.success_restaurant_added_message), Toast.LENGTH_SHORT).show();
            });
        }

    }

    static class DownloadLocation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder stringBuilder = new StringBuilder();
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return stringBuilder.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("kiemtra", " " + s);
            try {
                JSONObject jsonObject = new JSONObject();
                JSONArray results = jsonObject.getJSONArray("results");
                for(int i = 0; i < results.length(); i++) {
                    JSONObject object = results.getJSONObject(i);
                    String address = object.getString("formatted_address");
                    JSONObject geometry = object.getJSONObject("geometry");
                    JSONObject location = geometry.getJSONObject("location");
                    double latitude = location.getDouble("lat");
                    double longitude = location.getDouble("lng");

                    RestaurantBranchesModel restaurantBranchesModel = new RestaurantBranchesModel();
                    restaurantBranchesModel.setAddress(address);
                    restaurantBranchesModel.setLatitude(latitude);
                    restaurantBranchesModel.setLongitude(longitude);
                    Log.d("kiemtra", " " + latitude + " - " + longitude + " - " + address);

                    FirebaseDatabase.getInstance().getReference().child("restaurant_branches").child(restaurantID).setValue(restaurantBranchesModel);
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void SelectImageFromGallery (int requestCode){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image ..."), requestCode);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.spinnerArea){
            area = areaList.get(position);
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
