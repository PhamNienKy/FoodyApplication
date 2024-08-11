package foody.vn.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import foody.vn.Adapters.AdapterViewPagerHomePage;
import foody.vn.R;

public class HomePageActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    ViewPager viewPagerHomePage;
    RadioButton rd_place, rd_food;
    RadioGroup gp_place_food;
    ImageView imgAddRestaurant;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_homepage);

        viewPagerHomePage = findViewById(R.id.viewpage_homepage);
        rd_place = findViewById(R.id.rd_place);
        rd_food = findViewById(R.id.rd_food);
        gp_place_food = findViewById(R.id.gp_place_food);
        imgAddRestaurant = findViewById(R.id.imgAddRestaurant);

        AdapterViewPagerHomePage adapterViewPagerHomePage = new AdapterViewPagerHomePage(getSupportFragmentManager());
        viewPagerHomePage.setAdapter(adapterViewPagerHomePage);
        viewPagerHomePage.addOnPageChangeListener(this);
        gp_place_food.setOnCheckedChangeListener(this);
        imgAddRestaurant.setOnClickListener(this);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position){
            case 0:
                rd_place.setChecked(true);
                break;
            case 1:
                rd_food.setChecked(true);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if(checkedId == R.id.rd_food){
            viewPagerHomePage.setCurrentItem(1);
        } else if (checkedId == R.id.rd_place) {
            viewPagerHomePage.setCurrentItem(0);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id ==R.id.imgAddRestaurant){
            Intent iAddRestaurant = new Intent(HomePageActivity.this, AddRestaurantActivity.class);
            startActivity(iAddRestaurant);
        }
    }
}
