package foody.vn.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import foody.vn.Model.CommentModel;
import foody.vn.R;

public class AdapterComment extends RecyclerView.Adapter<AdapterComment.ViewHolder> {
    Context context;
    int layout;

    List<CommentModel> commentModelList;
    public AdapterComment(Context context, int layout, List<CommentModel> commentModelList){
        this.context = context;
        this.layout = layout;
        this.commentModelList = commentModelList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageView;
        TextView txtCommentTitle, txtCommentContent, txtCommentRate;
        RecyclerView recyclerCommentImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.circleUserImage);
            txtCommentTitle = itemView.findViewById(R.id.txtCommentTitle);
            txtCommentContent = itemView.findViewById(R.id.txtCommentContent);
            txtCommentRate = itemView.findViewById(R.id.txtCommentRate);
            recyclerCommentImage = itemView.findViewById(R.id.recyclerCommentImage);
        }
    }
    @NonNull
    @Override
    public AdapterComment.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from((parent).getContext()).inflate(layout, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    public void onBindViewHolder(@NonNull AdapterComment.ViewHolder holder, int position) {
        CommentModel commentModel = commentModelList.get(position);
        holder.txtCommentTitle.setText(commentModel.getTitle());
        holder.txtCommentContent.setText(commentModel.getContent());
        holder.txtCommentRate.setText(commentModel.getRate() + " ");
        setCommentImage(holder.circleImageView, commentModel.getUserModel().getUser_image());
        final List<Bitmap> bitmapList = new ArrayList<>();

        for(String imageLink : commentModel.getImageList()){
            StorageReference storageUserImage = FirebaseStorage.getInstance().getReference().child("images").child(imageLink);
            long ONE_MEGABYTE = 1024 * 1024;
            storageUserImage.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                bitmapList.add(bitmap);
                if(bitmapList.size() == commentModel.getImageList().size()){
                    AdapterRecyclerCommentImage adapterRecyclerCommentImage = new AdapterRecyclerCommentImage(context, R.layout.custom_layout_comment_image, bitmapList, commentModel, false);
                    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 2);
                    holder.recyclerCommentImage.setLayoutManager(layoutManager);
                    holder.recyclerCommentImage.setAdapter(adapterRecyclerCommentImage);
                    adapterRecyclerCommentImage.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        int commentNumber = commentModelList.size();
        if (commentNumber > 5) {
            return 5;
        } else {
            return commentModelList.size();
        }
    }
    private void setCommentImage(final CircleImageView circleImageView, String user_image){
        StorageReference storageUserImage = FirebaseStorage.getInstance().getReference().child("user").child(user_image);
        long ONE_MEGABYTE = 1024 * 1024;
        storageUserImage.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            circleImageView.setImageBitmap(bitmap);
        });
    }
}
