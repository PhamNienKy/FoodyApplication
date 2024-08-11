package foody.vn.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import foody.vn.View.Fragments.FoodFragment;
import foody.vn.View.Fragments.PlaceFragment;

public class AdapterViewPagerHomePage extends FragmentPagerAdapter {

    FoodFragment foodFragment;
    PlaceFragment placeFragment;
    public AdapterViewPagerHomePage (FragmentManager fm){
        super(fm);
        foodFragment = new FoodFragment();
        placeFragment = new PlaceFragment();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return placeFragment;
            case 1:
                return foodFragment;
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
