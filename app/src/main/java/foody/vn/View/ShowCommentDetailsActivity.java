package foody.vn.View;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import foody.vn.Adapters.AdapterRecyclerCommentImage;
import foody.vn.Model.CommentModel;
import foody.vn.R;

public class ShowCommentDetailsActivity extends AppCompatActivity {
    CircleImageView circleImageView;
    TextView txtCommentTitle, txtCommentContent, txtCommentRate;
    RecyclerView recyclerCommentImage;
    List<Bitmap> bitmapList;
    CommentModel commentModel;
    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_layout_comment);

        circleImageView = findViewById(R.id.circleUserImage);
        txtCommentTitle = findViewById(R.id.txtCommentTitle);
        txtCommentContent = findViewById(R.id.txtCommentContent);
        txtCommentRate = findViewById(R.id.txtCommentRate);
        recyclerCommentImage = findViewById(R.id.recyclerCommentImage);

        commentModel = getIntent().getParcelableExtra("commentModel");
        bitmapList = new ArrayList<>();

        txtCommentTitle.setText(commentModel.getTitle());
        txtCommentContent.setText(commentModel.getContent());
        txtCommentRate.setText(commentModel.getRate() + " ");
        setCommentImage(circleImageView, commentModel.getUserModel().getUser_image());
        for(String imageLink : commentModel.getImageList()){
            StorageReference storageUserImage = FirebaseStorage.getInstance().getReference().child("images").child(imageLink);
            long ONE_MEGABYTE = 1024 * 1024;
            storageUserImage.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                bitmapList.add(bitmap);
                if(bitmapList.size() == commentModel.getImageList().size()){
                    AdapterRecyclerCommentImage adapterRecyclerCommentImage = new AdapterRecyclerCommentImage(this, R.layout.custom_layout_comment_image, bitmapList, commentModel,true);
                    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(ShowCommentDetailsActivity.this, 2);
                    recyclerCommentImage.setLayoutManager(layoutManager);
                    recyclerCommentImage.setAdapter(adapterRecyclerCommentImage);
                    adapterRecyclerCommentImage.notifyDataSetChanged();
                }
            });
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
