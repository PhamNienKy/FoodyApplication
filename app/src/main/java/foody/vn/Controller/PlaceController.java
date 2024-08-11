package foody.vn.Controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import foody.vn.Adapters.AdapterRecyclerPlace;
import foody.vn.Controller.Interfaces.PlaceInterface;
import foody.vn.Model.RestaurantModel;
import foody.vn.R;

public class PlaceController {
    Context context;
    RestaurantModel restaurantModel;
    int item = 3;
    public PlaceController(Context context){
        this.context = context;
        restaurantModel = new RestaurantModel();
    }
    public void getListOfRestaurantController(Context context, NestedScrollView nestedScrollView_place, RecyclerView recyclerPlace, ProgressBar progressBar, Location currentLocation){

        final List<RestaurantModel> restaurantModelList = new ArrayList<>();
        AdapterRecyclerPlace adapterRecyclerPlace;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerPlace.setLayoutManager(layoutManager);
        adapterRecyclerPlace = new AdapterRecyclerPlace(context, restaurantModelList, R.layout.custom_layout_recyclerview_place);
        recyclerPlace.setAdapter(adapterRecyclerPlace);

        progressBar.setVisibility(View.VISIBLE);

        final PlaceInterface placeInterface = new PlaceInterface() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void getListOfRestaurantModel(RestaurantModel restaurantModel) {
                List<Bitmap> bitmaps = new ArrayList<>();
                for(String picture_link : restaurantModel.getRestaurant_img()) {
                    StorageReference storagePicture = FirebaseStorage.getInstance().getReference().child("images").child(picture_link);
                    long ONE_MEGABYTE = 1024 * 1024;
                    storagePicture.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            bitmaps.add(bitmap);
                            restaurantModel.setBitmapList(bitmaps);

                            if(restaurantModel.getBitmapList().size() == restaurantModel.getRestaurant_img().size()){
                                restaurantModelList.add(restaurantModel);
                                adapterRecyclerPlace.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        };

        nestedScrollView_place.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(v.getChildAt(v.getChildCount() - 1) != null){
                    if(scrollY >= (v.getChildAt(v.getChildCount() -1)).getMeasuredHeight() - v.getMeasuredHeight()){
                        item += 3;
                        restaurantModel.getListOfRestaurant(placeInterface, currentLocation, item, item - 3);
                    }
                }
            }
        });
        restaurantModel.getListOfRestaurant(placeInterface, currentLocation, item, 0);
    }
}
