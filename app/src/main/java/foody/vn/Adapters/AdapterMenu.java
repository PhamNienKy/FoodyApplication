package foody.vn.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import foody.vn.Model.MenuModel;
import foody.vn.R;

public class AdapterMenu extends RecyclerView.Adapter<AdapterMenu.HolderMenu> {
    Context context;
    List<MenuModel> menuModelList;

    public AdapterMenu(Context context, List<MenuModel> menuModelList) {
        this.context = context;
        this.menuModelList = menuModelList;
    }

    @NonNull
    @Override
    public AdapterMenu.HolderMenu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_layout_menu, parent, false);
        return new HolderMenu(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull AdapterMenu.HolderMenu holder, int position) {
        MenuModel menuModel = menuModelList.get(position);
        holder.txtMenu.setText(menuModel.getMenu_name());
        holder.recyclerFood.setLayoutManager(new LinearLayoutManager(context));
        AdapterFood adapterFood = new AdapterFood(context, menuModel.getFoodModelList());
        holder.recyclerFood.setAdapter(adapterFood);
        adapterFood.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return menuModelList.size();
    }

    public static class HolderMenu extends RecyclerView.ViewHolder {
        TextView txtMenu;
        RecyclerView recyclerFood;
        public HolderMenu(@NonNull View itemView) {
            super(itemView);

            txtMenu = itemView.findViewById(R.id.txtMenuName);
            recyclerFood = itemView.findViewById(R.id.recyclerFood);
        }
    }
}
