package foody.vn.Model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.List;

public class CommentModel implements Parcelable {
    double rate;
    long like;
    UserModel userModel;
    String content, comment_id, title, user_id;
    List<String> imageList;


    protected CommentModel(Parcel in) {
        rate = in.readDouble();
        like = in.readLong();
        content = in.readString();
        comment_id = in.readString();
        title = in.readString();
        user_id = in.readString();
        imageList = in.createStringArrayList();
        userModel = in.readParcelable(UserModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(rate);
        dest.writeLong(like);
        dest.writeString(content);
        dest.writeString(comment_id);
        dest.writeString(title);
        dest.writeString(user_id);
        dest.writeStringList(imageList);
        dest.writeParcelable(userModel, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CommentModel> CREATOR = new Creator<CommentModel>() {
        @Override
        public CommentModel createFromParcel(Parcel in) {
            return new CommentModel(in);
        }

        @Override
        public CommentModel[] newArray(int size) {
            return new CommentModel[size];
        }
    };
    public CommentModel(){

    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public void setLike(long like) {
        this.like = like;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void addComment (CommentModel commentModel, String restaurantID, List<String> imageList){
        DatabaseReference nodeComment = FirebaseDatabase.getInstance().getReference("comments");
        String commentID = nodeComment.child(restaurantID).push().getKey();

        nodeComment.child(restaurantID).child(commentID).setValue(commentModel).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                if(!imageList.isEmpty()){
                    for(String valueImage: imageList){
                        Uri uri = Uri.fromFile(new File(valueImage));
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference("images/"+uri.getLastPathSegment());
                        storageReference.putFile(uri).addOnCompleteListener(task1 -> {

                        });
                    }
                }
            }
        });

        if(!imageList.isEmpty()){
            for(String valueImage: imageList){
                Uri uri = Uri.fromFile(new File(valueImage));
                FirebaseDatabase.getInstance().getReference("comment_images").child(commentID).push().setValue(uri.getLastPathSegment());
            }
        }
    }
}
