package foody.vn.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import foody.vn.Model.SelectImageCommentModel;
import foody.vn.R;

public class AdapterSelectImageComment extends RecyclerView.Adapter<AdapterSelectImageComment.ViewHolderSelectImage> {
    Context context;
    int resource;
    List<SelectImageCommentModel> listPath;

    public AdapterSelectImageComment(Context context, int resource, List<SelectImageCommentModel> listPath) {
        this.context = context;
        this.listPath = listPath;
        this.resource = resource;
    }

    @NonNull
    @Override
    public ViewHolderSelectImage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(resource, parent, false);
        return new ViewHolderSelectImage(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder (@NonNull ViewHolderSelectImage holder, int position) {
        final SelectImageCommentModel selectImageCommentModel = listPath.get(position);
        Uri uri = Uri.parse(selectImageCommentModel.getPath());
        holder.imageView.setImageURI(uri);
        holder.checkBox.setChecked(selectImageCommentModel.isCheck());
        holder.checkBox.setOnClickListener(v -> {
            CheckBox checkBox = (CheckBox) v;
            listPath.get(position).setCheck(checkBox.isChecked());
        });
    }

    @Override
    public int getItemCount() {
        return listPath.size();
    }

    public static class ViewHolderSelectImage extends RecyclerView.ViewHolder {
        ImageView imageView;
        CheckBox checkBox;
        public ViewHolderSelectImage(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgSelectImageCmt);
            checkBox = itemView.findViewById(R.id.checkboxSelectImageCmt);
        }
    }
}
