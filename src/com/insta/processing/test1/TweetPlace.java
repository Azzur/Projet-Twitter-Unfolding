package com.insta.processing.test1;

import de.fhpotsdam.unfolding.geo.Location;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rodolphe
 *         <p/>
 *         Created at : 22/09/2014
 *         Project : com.insta.processing.1
 */
public class TweetPlace {

    private String type;
    private List<Location> locations = new ArrayList<Location>();

    @Override
    public String toString() {
        return "TweetPlace{" +
                "type='" + type + '\'' +
                ", locations=" + locations +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void addLocations(Location location) {
        this.locations.add(location);
    }

    public static TweetPlace parseJSON(String input) throws JSONException {
        TweetPlace place = new TweetPlace();

        JSONObject obj = (new JSONObject(input)).getJSONObject("bounding_box");
        JSONArray coordinates = obj.getJSONArray("coordinates").getJSONArray(0);
        int nb = coordinates.length();

        for (int i = 0 ; i < nb ; i++) {
            JSONArray aLocation = coordinates.getJSONArray(i);
            Location geo = new Location(aLocation.getDouble(1) , aLocation.getDouble(0));

            place.addLocations(geo);

        }

        place.setType(obj.optString("type"));


        return place;
    }
}
