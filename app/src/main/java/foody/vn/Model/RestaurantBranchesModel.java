package foody.vn.Model;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class RestaurantBranchesModel implements Parcelable {
    String address;
    double latitude, longitude, distance;
    public RestaurantBranchesModel() {

    }

    protected RestaurantBranchesModel(Parcel in) {
        address = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        distance = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeDouble(distance);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RestaurantBranchesModel> CREATOR = new Creator<RestaurantBranchesModel>() {
        @Override
        public RestaurantBranchesModel createFromParcel(Parcel in) {
            return new RestaurantBranchesModel(in);
        }

        @Override
        public RestaurantBranchesModel[] newArray(int size) {
            return new RestaurantBranchesModel[size];
        }
    };

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double haversine(Location location1, Location location2) {
        final int R = 6371; // Earth radius in km

        double latDistance = Math.toRadians(location2.getLatitude() - location1.getLatitude());
        double lonDistance = Math.toRadians(location2.getLongitude() - location1.getLongitude());
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(location1.getLatitude())) * Math.cos(Math.toRadians(location2.getLatitude()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
