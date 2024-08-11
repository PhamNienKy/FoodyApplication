package foody.vn.Controller;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import foody.vn.Adapters.AdapterMenu;
import foody.vn.Controller.Interfaces.MenuInterface;
import foody.vn.Model.MenuModel;

public class MenuController {
    MenuModel menuModel;
    public MenuController () {
        menuModel = new MenuModel();
    }

    public void getListOfMenuByID (Context context, String restaurantID, RecyclerView recyclerMenu){
        recyclerMenu.setLayoutManager(new LinearLayoutManager(context));

        MenuInterface menuInterface = new MenuInterface() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void getListOfMenuModel(List<MenuModel> menuModelList) {
                AdapterMenu adapterMenu = new AdapterMenu(context, menuModelList);
                recyclerMenu.setAdapter(adapterMenu);
                adapterMenu.notifyDataSetChanged();
            }
        };
        menuModel.getListOfMenuByID(restaurantID, menuInterface);
    }
}
