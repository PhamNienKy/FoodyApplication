package foody.vn.View.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import foody.vn.Controller.PlaceController;
import foody.vn.R;

public class PlaceFragment extends Fragment {
    PlaceController placeController;
    RecyclerView recyclerPlace;
    ProgressBar progressBar_place;
    SharedPreferences sharedPreferences;
    NestedScrollView nestedScrollView_place;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_place, container, false);
        recyclerPlace = (RecyclerView) view.findViewById(R.id.recycler_place);
        progressBar_place = (ProgressBar) view.findViewById(R.id.progressBar_place);
        nestedScrollView_place = (NestedScrollView) view.findViewById(R.id.nestedScrollView_place);

        sharedPreferences = getContext().getSharedPreferences("location", Context.MODE_PRIVATE);
        Location currentLocation = new Location("");
        currentLocation.setLatitude(Double.parseDouble(sharedPreferences.getString("latitude", "15.673")));
        currentLocation.setLongitude(Double.parseDouble(sharedPreferences.getString("longitude", "108.4121")));
        Log.d("Location", "Current Location: Lat = " + currentLocation.getLatitude() + ", Lng = " + currentLocation.getLongitude());

        placeController = new PlaceController(getContext());

        placeController.getListOfRestaurantController(getContext(), nestedScrollView_place, recyclerPlace, progressBar_place, currentLocation);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
