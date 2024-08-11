package foody.vn.Controller;

import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;
import java.util.concurrent.ExecutionException;

import foody.vn.View.DownloadPolyLine;
import foody.vn.View.ParserPolyLine;

public class LeadTheWayToRestaurantController {
    ParserPolyLine parserPolyLine;
    DownloadPolyLine downloadPolyLine;

    public LeadTheWayToRestaurantController(){

    }
    public void showLeadTheWayToRestaurant(GoogleMap googleMap, String url){
        parserPolyLine = new ParserPolyLine();

        downloadPolyLine = new DownloadPolyLine();
        downloadPolyLine.execute(url);

        try {
            String dataJSON = downloadPolyLine.get();
            Log.d("kiemtra", dataJSON);
            List<LatLng> listLntList = ParserPolyLine.getListOfLocation(dataJSON);

            PolylineOptions polylineOptions = new PolylineOptions();
            for (LatLng latLng : listLntList){
                polylineOptions.add(latLng);
            }
            Polyline polyline = googleMap.addPolyline(polylineOptions);

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}