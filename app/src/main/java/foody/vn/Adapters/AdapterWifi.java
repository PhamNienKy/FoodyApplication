package foody.vn.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import foody.vn.Model.WifiRestaurantModel;
import foody.vn.R;

public class AdapterWifi extends RecyclerView.Adapter<AdapterWifi.ViewHolder> {
    Context context;
    int resource;
    List<WifiRestaurantModel> wifiRestaurantModelList;

    public AdapterWifi (Context context, int resource, List<WifiRestaurantModel> wifiRestaurantModelList){
        this.context = context;
        this.resource = resource;
        this.wifiRestaurantModelList = wifiRestaurantModelList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtWifiName, txtWifiPassword, txtDateSubmit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtWifiName = itemView.findViewById(R.id.txtWifiName);
            txtWifiPassword = itemView.findViewById(R.id.txtWifiPassword);
            txtDateSubmit = itemView.findViewById(R.id.txtDateSubmit);
        }
    }

    @NonNull
    @Override
    public AdapterWifi.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(resource, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterWifi.ViewHolder holder, int position) {
        WifiRestaurantModel wifiRestaurantModel = wifiRestaurantModelList.get(position);

        holder.txtWifiName.setText(wifiRestaurantModel.getName());
        holder.txtWifiPassword.setText(wifiRestaurantModel.getPassword());
        holder.txtDateSubmit.setText(wifiRestaurantModel.getDate_submit());
    }

    @Override
    public int getItemCount() {
        return wifiRestaurantModelList.size();
    }
}
