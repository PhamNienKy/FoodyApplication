package foody.vn.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import foody.vn.R;

public class AdapterShowSelectedImageComment extends RecyclerView.Adapter<AdapterShowSelectedImageComment.ViewHolderSelectedImage> {
    Context context;
    int resource;
    List<String> list;

    public AdapterShowSelectedImageComment(Context context, int resource, List<String> list) {
        this.context = context;
        this.resource = resource;
        this.list = list;
    }

    @NonNull
    @Override
    public AdapterShowSelectedImageComment.ViewHolderSelectedImage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new AdapterShowSelectedImageComment.ViewHolderSelectedImage(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterShowSelectedImageComment.ViewHolderSelectedImage holder, int position) {
        Uri uri = Uri.parse(list.get(position));
        holder.imageSelectedImage.setImageURI(uri);
        holder.imageDelete.setTag(position);
        holder.imageDelete.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                list.remove(pos);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolderSelectedImage extends RecyclerView.ViewHolder {
        ImageView imageSelectedImage, imageDelete;
        public ViewHolderSelectedImage(@NonNull View itemView) {
            super(itemView);

            imageSelectedImage = itemView.findViewById(R.id.imageSelectedImageCmt);
            imageDelete = itemView.findViewById(R.id.imageDelete);
        }
    }
}
