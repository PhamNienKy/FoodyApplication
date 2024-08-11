package foody.vn.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import foody.vn.Controller.Order;
import foody.vn.Model.FoodModel;
import foody.vn.R;

public class AdapterFood extends RecyclerView.Adapter<AdapterFood.HolderFood> {
    Context context;
    List<FoodModel>  foodModelList;
    public static List<Order> orderList = new ArrayList<>();

    public AdapterFood (Context context, List<FoodModel> foodModelList) {
        this.context = context;
        this.foodModelList = foodModelList;

    }

    @NonNull
    @Override
    public AdapterFood.HolderFood onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_layout_food, parent, false);
        return new HolderFood(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull AdapterFood.HolderFood holder, int position) {
        FoodModel foodModel = foodModelList.get(position);
        holder.txtFoodName.setText(foodModel.getFood_name());

        holder.txtFoodNumber.setTag(0);
        holder.imgPlus.setOnClickListener(v -> {
            int count = Integer.parseInt(holder.txtFoodNumber.getTag().toString());
            count ++;
            holder.txtFoodNumber.setText(String.valueOf(count));
            holder.txtFoodNumber.setTag(count);

            Order orderTag = (Order) holder.imgMinus.getTag();
            if(orderTag != null){
                AdapterFood.orderList.remove(orderTag);
            }

            Order order = new Order();
            order.setQuantity(count);
            order.setNameFood(foodModel.getFood_name());

            holder.imgPlus.setTag(order);

            AdapterFood.orderList.add(order);

            for(Order order1 : AdapterFood.orderList){
                Log.d("kiemtra", " " + order1.getNameFood() + " - " + order1.getQuantity());
            }
        });

        holder.imgMinus.setOnClickListener(v -> {
            int count = Integer.parseInt(holder.txtFoodNumber.getTag().toString());
            if (count > 0) {
                count --;
            }
            if (count == 0){
                Order order = (Order) v.getTag();
                AdapterFood.orderList.remove(order);
            }

            holder.txtFoodNumber.setText(String.valueOf(count));
            holder.txtFoodNumber.setTag(count);
        });
    }

    @Override
    public int getItemCount() {
        return foodModelList.size();
    }

    public static class HolderFood extends RecyclerView.ViewHolder {
        TextView txtFoodName, txtFoodNumber;
        ImageView imgMinus, imgPlus;


        public HolderFood(@NonNull View itemView) {
            super(itemView);

            txtFoodName = itemView.findViewById(R.id.txtFoodName);
            txtFoodNumber = itemView.findViewById(R.id.txtFoodNumber);
            imgMinus = itemView.findViewById(R.id.imgMinus);
            imgPlus = itemView.findViewById(R.id.imgPlus);
        }
    }
}
