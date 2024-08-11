package foody.vn.Controller;

import java.util.List;

import foody.vn.Model.CommentModel;

public class CommentController {
    CommentModel commentModel;
    public CommentController () {
        this.commentModel = commentModel;
    }

    public void addComment(CommentModel commentModel, String restaurantID, List<String> pictureList){
        commentModel.addComment(commentModel, restaurantID, pictureList);
    }
}
