package foody.vn.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import foody.vn.Adapters.AdapterShowSelectedImageComment;
import foody.vn.Controller.CommentController;
import foody.vn.Model.CommentModel;
import foody.vn.R;

public class CommentActivity extends AppCompatActivity implements View.OnClickListener {
    TextView txtRestaurantName, txtRestaurantAddress, txtPostComment;
    Toolbar toolbarComment;
    ImageButton btnSelectImage;
    RecyclerView recyclerSelectedImage;
    EditText edTitleComment, edContentComment;
    String restaurantId;
    List<String> listSelectedImage;
    SharedPreferences sharedPreferences;
    CommentController commentController;
    AdapterShowSelectedImageComment adapterShowSelectedImageComment;

    final int REQUEST_SELECT_IMAGE_COMMENT = 1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_comment);

        restaurantId = getIntent().getStringExtra("restaurantID");
        String restaurantName = getIntent().getStringExtra("restaurantName");
        String restaurantAddress = getIntent().getStringExtra("restaurantAddress");

        sharedPreferences = getSharedPreferences("saveLogin", MODE_PRIVATE);

        txtRestaurantName = findViewById(R.id.txtRestaurantName_comment);
        txtRestaurantAddress = findViewById(R.id.txtRestaurantAddress_comment);
        txtPostComment = findViewById(R.id.txtPostComment);
        toolbarComment = findViewById(R.id.toolbarComment);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        edTitleComment = findViewById(R.id.edTitleComment);
        edContentComment = findViewById(R.id.edContentComment);
        recyclerSelectedImage = findViewById(R.id.recyclerSelectedImage);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerSelectedImage.setLayoutManager(layoutManager);

        listSelectedImage = new ArrayList<>();
        commentController = new CommentController();

        toolbarComment.setTitle("");
        setSupportActionBar(toolbarComment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txtRestaurantName.setText(restaurantName);
        txtRestaurantAddress.setText(restaurantAddress);

        btnSelectImage.setOnClickListener(this);
        txtPostComment.setOnClickListener(this);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btnSelectImage){
            Intent iSelectImageComment = new Intent(this, SelectImageCommentActivity.class);
            startActivityForResult(iSelectImageComment, REQUEST_SELECT_IMAGE_COMMENT);
        }
        if(id == R.id.txtPostComment){
            CommentModel commentModel = new CommentModel();
            String title = edTitleComment.getText().toString();
            String content = edContentComment.getText().toString();
            String userId = sharedPreferences.getString("userID", "");

            commentModel.setTitle(title);
            commentModel.setContent(content);
            commentModel.setRate(0);
            commentModel.setLike(0);
            commentModel.setUser_id (userId);

            commentController.addComment(commentModel, restaurantId, listSelectedImage);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_SELECT_IMAGE_COMMENT && resultCode == RESULT_OK){
            listSelectedImage = data.getStringArrayListExtra("listSelectedImage");
            adapterShowSelectedImageComment = new AdapterShowSelectedImageComment(this, R.layout.custom_layout_selected_image_comment, listSelectedImage);
            recyclerSelectedImage.setAdapter(adapterShowSelectedImageComment);
            adapterShowSelectedImageComment.notifyDataSetChanged();
        }
    }
}
