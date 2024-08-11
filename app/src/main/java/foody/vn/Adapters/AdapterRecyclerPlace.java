package foody.vn.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import foody.vn.Model.CommentModel;
import foody.vn.Model.RestaurantBranchesModel;
import foody.vn.Model.RestaurantModel;
import foody.vn.R;
import foody.vn.View.RestaurantDetailsActivity;

public class AdapterRecyclerPlace extends RecyclerView.Adapter<AdapterRecyclerPlace.ViewHolder> {
    List<RestaurantModel> restaurantModelList;
    int resource;
    Context context;
    public AdapterRecyclerPlace(Context context, List<RestaurantModel> restaurentModelList, int resource){
        this.restaurantModelList = restaurentModelList;
        this.resource = resource;
        this.context = context;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtRestaurantName_place;
        TextView txtCommentTitle, txtCommentTitle2;
        TextView txtCommentContent, txtCommentContent2;
        TextView txtCommentRate, txtCommentRate2;
        TextView txtCommentTotal, txtCommentImageTotal, txtAverageRate;
        TextView txtRestaurantAddress_place, txtRestaurantDistance_place;
        Button btnOrder_place;
        ImageView imgRestaurant_place;
        CircleImageView circleUserImage, circleUserImage2;
        LinearLayout containerComment, containerComment2;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtRestaurantName_place = itemView.findViewById(R.id.txtRestaurantName_place);
            btnOrder_place = itemView.findViewById(R.id.btnOrder_place);
            imgRestaurant_place = itemView.findViewById(R.id.imgRestaurant_place);
            txtCommentContent = itemView.findViewById(R.id.txtCommentContent);
            txtCommentContent2 = itemView.findViewById(R.id.txtCommentContent2);
            txtCommentTitle = itemView.findViewById(R.id.txtCommentTitle);
            txtCommentTitle2 = itemView.findViewById(R.id.txtCommentTitle2);
            circleUserImage = itemView.findViewById(R.id.circleUserImage);
            circleUserImage2 = itemView.findViewById(R.id.circleUserImage2);
            containerComment = itemView.findViewById(R.id.containerComment);
            containerComment2 = itemView.findViewById(R.id.containerComment2);
            txtCommentRate = itemView.findViewById(R.id.txtCommentRate);
            txtCommentRate2 = itemView.findViewById(R.id.txtCommentRate2);
            txtCommentTotal = itemView.findViewById(R.id.txtCommentTotal);
            txtCommentImageTotal = itemView.findViewById(R.id.txtCommentImageTotal);
            txtAverageRate = itemView.findViewById(R.id.txtAverageRate);
            txtRestaurantAddress_place = itemView.findViewById(R.id.txtRestaurantAddress_place);
            txtRestaurantDistance_place = itemView.findViewById(R.id.txtRestaurantDistance_place);
            cardView = itemView.findViewById(R.id.cardView_place);

        }
    }
    @NonNull
    @Override
    public AdapterRecyclerPlace.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from((parent).getContext()).inflate(resource, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull AdapterRecyclerPlace.ViewHolder holder, int position) {
        RestaurantModel restaurantModel = restaurantModelList.get(position);
        holder.txtRestaurantName_place.setText(restaurantModel.getRestaurant_name());
        if (restaurantModel.isDelivery()) {
            holder.btnOrder_place.setVisibility(View.VISIBLE);
        }
        if (!restaurantModel.getRestaurant_img().isEmpty()) {
            holder.imgRestaurant_place.setImageBitmap(restaurantModel.getBitmapList().get(0));
        }

        //Get comment list of restaurant
        if (!restaurantModel.getCommentModelList().isEmpty()) {
            CommentModel commentModel = restaurantModel.getCommentModelList().get(0);
            holder.txtCommentTitle.setText(commentModel.getTitle());
            holder.txtCommentContent.setText(commentModel.getContent());
            holder.txtCommentRate.setText(commentModel.getRate() + " ");
            setCommentImage(holder.circleUserImage, commentModel.getUserModel().getUser_image());
            if (restaurantModel.getCommentModelList().size() > 2) {
                CommentModel commentModel2 = restaurantModel.getCommentModelList().get(1);
                holder.txtCommentTitle2.setText(commentModel2.getTitle());
                holder.txtCommentContent2.setText(commentModel2.getContent());
                holder.txtCommentRate2.setText(commentModel2.getRate()+ " ");
                setCommentImage(holder.circleUserImage2, commentModel2.getUserModel().getUser_image());
            }
            holder.txtCommentTotal.setText(restaurantModel.getCommentModelList().size() + " ");

            int commentImageTotal = 0;
            double totalRate = 0;
            //Get average total of restaurant and total picture of comment
            for(CommentModel commentModel1 : restaurantModel.getCommentModelList()){
                commentImageTotal += commentModel1.getImageList().size();
                totalRate += commentModel1.getRate();
            }
            double averageRate = totalRate/ restaurantModel.getCommentModelList().size();
                holder.txtAverageRate.setText(String.format("%.1f", averageRate));
            if(commentImageTotal > 0){
                holder.txtCommentImageTotal.setText(commentImageTotal + " ");
            }
        }else {
            holder.containerComment.setVisibility(View.GONE);
            holder.containerComment2.setVisibility(View.GONE);
            holder.txtCommentTotal.setText(String.valueOf(0));
        }

        //Get restaurant branches and show address and distance
        if(!restaurantModel.getBranchesModelList().isEmpty()){
            RestaurantBranchesModel restaurantBranchesModelTemp = restaurantModel.getBranchesModelList().get(0);
            for (RestaurantBranchesModel restaurantBranchesModel : restaurantModel.getBranchesModelList()){
                if(restaurantBranchesModelTemp.getDistance() > restaurantBranchesModel.getDistance()){
                    restaurantBranchesModelTemp = restaurantBranchesModel;
                }
            }
            holder.txtRestaurantAddress_place.setText(restaurantBranchesModelTemp.getAddress());
            holder.txtRestaurantDistance_place.setText(String.format("%.1f", restaurantBranchesModelTemp.getDistance()) + "km");
        }
        holder.cardView.setOnClickListener(v -> {
            Intent iRestaurantDetails = new Intent(context, RestaurantDetailsActivity.class);
            iRestaurantDetails.putExtra("restaurant", restaurantModel);
            context.startActivity(iRestaurantDetails);
        });
    }
    private void setCommentImage(final CircleImageView circleImageView, String user_image){
        StorageReference storageUserImage = FirebaseStorage.getInstance().getReference().child("user").child(user_image);
        long ONE_MEGABYTE = 1024 * 1024;
        storageUserImage.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            circleImageView.setImageBitmap(bitmap);
        });
    }


    @Override
    public int getItemCount() {
        return restaurantModelList.size();
    }
}
