package foody.vn.Model;

import android.graphics.Bitmap;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import foody.vn.Controller.Interfaces.PlaceInterface;

public class RestaurantModel implements Parcelable {
    boolean delivery;
    String open_time, close_time, restaurant_name, intro_video, restaurant_id;
    List<String> utility, restaurant_img;
    List<CommentModel> commentModelList;
    List<MenuModel> menuList;
    List<RestaurantBranchesModel> branchesModelList;
    List<Bitmap> bitmapList;
    long likes, max_price, min_price;

    public List<MenuModel> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<MenuModel> menuList) {
        this.menuList = menuList;
    }

    public long getMax_price() {
        return max_price;
    }

    public void setMax_price(long max_price) {
        this.max_price = max_price;
    }

    public long getMin_price() {
        return min_price;
    }

    public void setMin_price(long min_price) {
        this.min_price = min_price;
    }

    protected RestaurantModel(Parcel in) {
        delivery = in.readByte() != 0;
        open_time = in.readString();
        close_time = in.readString();
        restaurant_name = in.readString();
        intro_video = in.readString();
        restaurant_id = in.readString();
        utility = in.createStringArrayList();
        restaurant_img = in.createStringArrayList();
        likes = in.readLong();
        max_price = in.readLong();
        min_price = in.readLong();
        branchesModelList = new ArrayList<RestaurantBranchesModel>();
        in.readTypedList(branchesModelList, RestaurantBranchesModel.CREATOR);
        commentModelList = new ArrayList<CommentModel>();
        in.readTypedList(commentModelList, CommentModel.CREATOR);
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeByte((byte) (delivery ? 1 : 0));
        dest.writeString(open_time);
        dest.writeString(close_time);
        dest.writeString(restaurant_name);
        dest.writeString(intro_video);
        dest.writeString(restaurant_id);
        dest.writeStringList(utility);
        dest.writeStringList(restaurant_img);
        dest.writeLong(likes);
        dest.writeLong(max_price);
        dest.writeLong(min_price);
        dest.writeTypedList(branchesModelList);
        dest.writeTypedList(commentModelList);
    }

    public static final Creator<RestaurantModel> CREATOR = new Creator<RestaurantModel>() {
        @Override
        public RestaurantModel createFromParcel(Parcel in) {
            return new RestaurantModel(in);
        }

        @Override
        public RestaurantModel[] newArray(int size) {
            return new RestaurantModel[size];
        }
    };

    public List<Bitmap> getBitmapList() {
        return bitmapList;
    }

    public void setBitmapList(List<Bitmap> bitmapList) {
        this.bitmapList = bitmapList;
    }

    public List<RestaurantBranchesModel> getBranchesModelList() {
        return branchesModelList;
    }

    public void setBranchesModelList(List<RestaurantBranchesModel> branchesModelList) {
        this.branchesModelList = branchesModelList;
    }

    public List<CommentModel> getCommentModelList() {
        return commentModelList;
    }

    public void setCommentModelList(List<CommentModel> commentModelList) {
        this.commentModelList = commentModelList;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public List<String> getRestaurant_img() {
        return restaurant_img;
    }

    public void setRestaurant_img(List<String> restaurant_img) {
        this.restaurant_img = restaurant_img;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public RestaurantModel() {
        nodeRoot = FirebaseDatabase.getInstance().getReference();
    }

    public boolean isDelivery() {
        return delivery;
    }

    public void setDelivery(boolean delivery) {
        this.delivery = delivery;
    }

    public String getOpen_time() {
        return open_time;
    }

    public void setOpen_time(String open_time) {
        this.open_time = open_time;
    }

    public String getClose_time() {
        return close_time;
    }

    public void setClose_time(String close_time) {
        this.close_time = close_time;
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public String getIntro_video() {
        return intro_video;
    }

    public void setIntro_video(String intro_video) {
        this.intro_video = intro_video;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public List<String> getUtility() {
        return utility;
    }

    public void setUtility(List<String> utility) {
        this.utility = utility;
    }

    DatabaseReference nodeRoot;

    private DataSnapshot dataRoot;

    public void getListOfRestaurant(final PlaceInterface placeInterface, final Location currentLocation, final int nextItem, final int initItem) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataRoot = dataSnapshot;
                getRestaurantList(dataSnapshot, placeInterface, currentLocation, nextItem, initItem);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        if (dataRoot != null) {
            getRestaurantList(dataRoot, placeInterface, currentLocation, nextItem, initItem);
        } else {
            nodeRoot.addListenerForSingleValueEvent(valueEventListener);
        }


    }

    private void getRestaurantList(DataSnapshot dataSnapshot, PlaceInterface placeInterface, Location currentLocation, int nextItem, int existedItem) {
        DataSnapshot dataSnapshotRestaurant = dataSnapshot.child("restaurants");
        //Get list of restaurants
        int i = 0;
        for (DataSnapshot valueRestaurant : dataSnapshotRestaurant.getChildren()) {
            if (i == nextItem) {
                break;
            }
            if (i < existedItem) {
                i++;
                continue;
            }
            i++;
            RestaurantModel restaurantModel = valueRestaurant.getValue(RestaurantModel.class);
            restaurantModel.setRestaurant_id(valueRestaurant.getKey());
            //Get a list of restaurant images by id
            DataSnapshot dataSnapshotRestaurantImg = dataSnapshot.child("restaurant_images").child(valueRestaurant.getKey());
            List<String> imgList = new ArrayList<>();
            for (DataSnapshot valueRestaurantImg : dataSnapshotRestaurantImg.getChildren()) {
                imgList.add(valueRestaurantImg.getValue(String.class));
            }
            restaurantModel.setRestaurant_img(imgList);


            //Get a list of restaurant comments
            DataSnapshot snapshotComment = dataSnapshot.child("comments").child(restaurantModel.getRestaurant_id());
            List<CommentModel> commentList = new ArrayList<>();
            for (DataSnapshot valueComment : snapshotComment.getChildren()) {
                CommentModel commentModel = valueComment.getValue(CommentModel.class);
                commentModel.setComment_id(valueComment.getKey());
                UserModel userModel = dataSnapshot.child("users").child(commentModel.getUser_id()).getValue(UserModel.class);
                commentModel.setUserModel(userModel);

                List<String> commentImageList = new ArrayList<>();
                DataSnapshot snapshotNodeCommentImages = dataSnapshot.child("comment_images").child(commentModel.getComment_id());
                for (DataSnapshot valueCommentImage : snapshotNodeCommentImages.getChildren()) {
                    commentImageList.add(valueCommentImage.getValue(String.class));
                }
                commentModel.setImageList(commentImageList);
                commentList.add(commentModel);
            }
            restaurantModel.setCommentModelList(commentList);

            //Get restaurant branches
            DataSnapshot snapshotBranches = dataSnapshot.child("restaurant_branches").child(restaurantModel.getRestaurant_id());
            List<RestaurantBranchesModel> restaurantBranchesModels = new ArrayList<>();
            for (DataSnapshot valueBranches : snapshotBranches.getChildren()) {
                RestaurantBranchesModel restaurantBranchesModel = valueBranches.getValue(RestaurantBranchesModel.class);
                Location restaurantLocation = new Location("");
                restaurantLocation.setLatitude(restaurantBranchesModel.getLatitude());
                restaurantLocation.setLongitude(restaurantBranchesModel.getLongitude());

                double distance = restaurantBranchesModel.haversine(currentLocation, restaurantLocation);
                restaurantBranchesModel.setDistance(distance);
                restaurantBranchesModels.add(restaurantBranchesModel);
            }

            restaurantModel.setBranchesModelList(restaurantBranchesModels);


            placeInterface.getListOfRestaurantModel(restaurantModel);
        }
    }
}
