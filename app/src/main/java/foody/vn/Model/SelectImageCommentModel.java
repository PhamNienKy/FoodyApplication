package foody.vn.Model;

public class SelectImageCommentModel {
    String path;
    boolean isCheck;
    public SelectImageCommentModel(String path, boolean isCheck) {
        this.path = path;
        this.isCheck = isCheck;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }



}
