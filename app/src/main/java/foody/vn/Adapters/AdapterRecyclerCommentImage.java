package foody.vn.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import foody.vn.Model.CommentModel;
import foody.vn.R;
import foody.vn.View.ShowCommentDetailsActivity;

public class AdapterRecyclerCommentImage extends RecyclerView.Adapter<AdapterRecyclerCommentImage.ViewHolderCommentImage> {
    Context context;
    int layout;
    List<Bitmap> imageList;
    CommentModel commentModel;
    boolean iCommentDetails;
    public AdapterRecyclerCommentImage(Context context, int layout, List<Bitmap> imageList, CommentModel commentModel, boolean iCommentDetails){
        this.context = context;
        this.layout = layout;
        this.imageList = imageList;
        this.commentModel = commentModel;
        this.iCommentDetails = iCommentDetails;
    }
    public static class ViewHolderCommentImage extends RecyclerView.ViewHolder {
        ImageView imgComment;
        TextView txtCommentImgNumber;
        FrameLayout frameCommentImgNumber;
        public ViewHolderCommentImage(@NonNull View itemView) {
            super(itemView);

            imgComment = itemView.findViewById(R.id.imgComment);
            txtCommentImgNumber = itemView.findViewById(R.id.txtCommentImgNumber);
            frameCommentImgNumber = itemView.findViewById(R.id.frameCommentImgNumber);
        }
    }
    @NonNull
    @Override
    public AdapterRecyclerCommentImage.ViewHolderCommentImage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from((parent).getContext()).inflate(layout, parent, false);
        return new AdapterRecyclerCommentImage.ViewHolderCommentImage(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AdapterRecyclerCommentImage.ViewHolderCommentImage holder, int position) {
        holder.imgComment.setImageBitmap(imageList.get(position));
        if(!iCommentDetails){
            if(position == 3){
                int remainingImage = imageList.size() - 4;
                if (remainingImage > 0){
                    holder.frameCommentImgNumber.setVisibility(View.VISIBLE);
                    holder.txtCommentImgNumber.setText("+ " + remainingImage);
                    holder.imgComment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent iCommentDetails = new Intent(context, ShowCommentDetailsActivity.class);
                            iCommentDetails.putExtra("commentModel", commentModel);
                            context.startActivity(iCommentDetails);
                        }
                    });
                }
        }
        }
    }

    @Override
    public int getItemCount() {
        if (!iCommentDetails) {
            return Math.min(imageList.size(), 4);
        } else {
            return imageList.size();
        }
    }
}
