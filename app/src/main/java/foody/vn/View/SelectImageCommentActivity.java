package foody.vn.View;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import foody.vn.Adapters.AdapterSelectImageComment;
import foody.vn.Model.SelectImageCommentModel;
import foody.vn.R;

public class SelectImageCommentActivity extends AppCompatActivity implements View.OnClickListener {
    TextView txtDone;
    List<SelectImageCommentModel> listPath;
    List<String> listSelectedImage;
    RecyclerView recyclerSelectImageCmt;
    Toolbar toolbarSelectImageCmt;
    AdapterSelectImageComment adapterSelectImageComment;

    @SuppressLint("InlinedApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_select_image_comment);

        listPath = new ArrayList<>();
        listSelectedImage = new ArrayList<>();


        txtDone = findViewById(R.id.txtDone);
        recyclerSelectImageCmt = findViewById(R.id.recyclerSelectImageCmt);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        adapterSelectImageComment =  new AdapterSelectImageComment(this, R.layout.custom_layout_select_image_comment, listPath);
        recyclerSelectImageCmt.setLayoutManager(layoutManager);
        recyclerSelectImageCmt.setAdapter(adapterSelectImageComment);

        toolbarSelectImageCmt = findViewById(R.id.toolbarSelectImageCmt);
        toolbarSelectImageCmt.setTitle("");
        setSupportActionBar(toolbarSelectImageCmt);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        int checkReadExStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES);
        if(checkReadExStorage != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 1);
        } else {
            getAllImagePath();
        }

       txtDone.setOnClickListener(this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getAllImagePath();
            }
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    public void getAllImagePath(){
        String [] projection ={MediaStore.Images.Media.DATA};
        Uri uri = MediaStore.Images.Media.INTERNAL_CONTENT_URI;
        Uri uriEX = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        @SuppressLint("Recycle") Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        @SuppressLint("Recycle") Cursor cursorEX = getContentResolver().query(uriEX, projection, null, null, null);
        cursor.moveToFirst();
        while(cursor.moveToNext()){
            @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            SelectImageCommentModel selectImageCommentModel = new SelectImageCommentModel(path, false);

            listPath.add(selectImageCommentModel);
            adapterSelectImageComment.notifyDataSetChanged();
            cursor.moveToNext();
        }
        while (cursorEX.moveToNext()){
            @SuppressLint("Range") String pathEX = cursorEX.getString(cursorEX.getColumnIndex(MediaStore.Images.Media.DATA));
            SelectImageCommentModel selectImageCommentModelEX = new SelectImageCommentModel(pathEX, false);

            listPath.add(selectImageCommentModelEX);
            adapterSelectImageComment.notifyDataSetChanged();
            cursor.moveToNext();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.txtDone){
            for(SelectImageCommentModel value : listPath){
                if(value.isCheck()){
                    listSelectedImage.add(value.getPath());
                }
            }

            Intent data = getIntent();
            data.putStringArrayListExtra("listSelectedImage", (ArrayList<String>) listSelectedImage);

            setResult(RESULT_OK, data);
            finish();
        }
    }
}
