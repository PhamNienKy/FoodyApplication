package foody.vn.View;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ParserPolyLine {
    public static List<LatLng> getListOfLocation(String dataJSON){
        List<LatLng> latLngs = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(dataJSON);
            JSONArray routes = jsonObject.getJSONArray("routes");
            for (int i = 0; i < routes.length(); i++) {
                JSONObject jsonObjectArray = routes.getJSONObject(i);
                JSONObject overviewPolyline = jsonObjectArray.getJSONObject("overview_polyline");

                String point = overviewPolyline.getString("points");
                Log.d("kiemtra", point);

                latLngs = PolyUtil.decode(point);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return latLngs;
    }
}
