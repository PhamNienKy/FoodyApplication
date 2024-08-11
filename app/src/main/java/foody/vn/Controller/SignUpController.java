package foody.vn.Controller;

import foody.vn.Model.UserModel;

public class SignUpController {
    UserModel userModel;
    public SignUpController (){
        userModel = new UserModel();
    }
    public void AddInfoUserController(UserModel userModel, String uid){
        userModel.AddInfoUser(userModel, uid);
    }
}
