package foody.vn.Model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import foody.vn.Controller.Interfaces.MenuInterface;

public class MenuModel {
    String menu_id;
    String menu_name;
    List<FoodModel> foodModelList;

    public List<FoodModel> getFoodModelList() {
        return foodModelList;
    }

    public void setFoodModelList(List<FoodModel> foodModelList) {
        this.foodModelList = foodModelList;
    }
    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public void getListOfMenuByID (String restaurantID, final MenuInterface menuInterface){
        DatabaseReference nodeRestaurantMenu  = FirebaseDatabase.getInstance().getReference().child("restaurant_menus").child(restaurantID);
        nodeRestaurantMenu.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                final List<MenuModel> menuModels = new ArrayList<>();

                for(DataSnapshot valueMenu : snapshot.getChildren()){
                    final MenuModel menuModel = new MenuModel();

                    DatabaseReference nodeMenu = FirebaseDatabase.getInstance().getReference().child("menus").child(valueMenu.getKey());
                    nodeMenu.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshotMenu) {
                            String menuID = snapshotMenu.getKey();
                            menuModel.setMenu_id(menuID);
                            menuModel.setMenu_name(snapshotMenu.getValue(String.class));
                            List<FoodModel> foodModels = new ArrayList<>();
                            Log.d("menuID", menuID);

                            for (DataSnapshot valueFood : snapshot.child(menuID).getChildren()){
                                FoodModel foodModel = valueFood.getValue(FoodModel.class);
                                foodModel.setFood_id(valueFood.getKey());
                                foodModels.add(foodModel);
                                Log.d("foodModel", foodModel.getFood_name());
                            }

                            menuModel.setFoodModelList(foodModels);
                            menuModels.add(menuModel);
                            menuInterface.getListOfMenuModel(menuModels);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
