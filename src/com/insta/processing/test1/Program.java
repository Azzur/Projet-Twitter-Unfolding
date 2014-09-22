package com.insta.processing.test1;

import com.temboo.Library.Twitter.Search.Tweets;
import de.fhpotsdam.unfolding.*;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePolygonMarker;
import controlP5.*;
import de.fhpotsdam.unfolding.utils.*;
import org.json.JSONException;
import processing.core.*;
import com.temboo.core.*;
import com.temboo.Library.Twitter.Trends.*;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Rodolphe
 *         <p/>
 *         Created at : 19/09/2014
 *         Project : com.insta.processing.1
 */
public class Program extends PApplet {
    ControlP5 cp5;
    UnfoldingMap map;
    TembooSession session = new TembooSession("senorihl", "Processing", "ee23a86b447a45eba3c7f798bb1aa1b4");
    List<Location> markers = new ArrayList<Location>();
    List<List<Location>> polygonMarkers = new ArrayList<List<Location>>();



    public void setup() {
        size(800, 600, OPENGL);
        map = new UnfoldingMap(this , 190, 10, 590 , 580);

        MapUtils.createDefaultEventDispatcher(this, map);
        try {

            ArrayList<Tweet> tweets = getTweetSearch("I'm at");
            addMarkers(tweets);


            tweets = getTweetSearch("#temboo");
            addMarkers(tweets);

            for (Location marker : markers)
                map.addMarker(new TweetMarker(marker, loadImage("C:\\Users\\Rodolphe\\Downloads\\1411406170_678111-map-marker-48.png")));
            for (List<Location> polygonLocations : polygonMarkers)
                map.addMarker(new SimplePolygonMarker(polygonLocations));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void draw() {
        map.draw();
    }

    public static void main(String[] args) {
        PApplet.main(
                "com.insta.processing.test1.Program"
        );
    }

    String getTwitterTrend() {
        Place placeChoreo = new Place(session);
        placeChoreo.setAccessToken("89814238-xX3Grmyv8jteVfPCkjM203OKAMfDWhXjPop3OUCp9");
        placeChoreo.setID("1");
        placeChoreo.setAccessTokenSecret("OSJ0anNnFfSMrIWlUREKjku8z0Un4PKfdl3O8gbIT5R1E");
        placeChoreo.setConsumerSecret("lINt19NMyVCHZP6Gqs0LyKibuv9DnanMzDqqNPXHkTFEjjrwt8");
        placeChoreo.setConsumerKey("OrniWZuYBj5Nns3SqbbY7Wb7T");
        PlaceResultSet set = placeChoreo.run();

        return set.getResponse();
    }

    ArrayList<Tweet> getTweetSearch(String search) throws JSONException {
        Tweets tweetsChoreo = new Tweets(session);
        tweetsChoreo.setAccessToken("89814238-xX3Grmyv8jteVfPCkjM203OKAMfDWhXjPop3OUCp9");
        tweetsChoreo.setAccessTokenSecret("OSJ0anNnFfSMrIWlUREKjku8z0Un4PKfdl3O8gbIT5R1E");
        tweetsChoreo.setConsumerSecret("lINt19NMyVCHZP6Gqs0LyKibuv9DnanMzDqqNPXHkTFEjjrwt8");
        tweetsChoreo.setConsumerKey("OrniWZuYBj5Nns3SqbbY7Wb7T");
        tweetsChoreo.setQuery(search);
        tweetsChoreo.setCount(200);

        String result = tweetsChoreo.run().getResponse();

        return Tweet.parseJSON(result);

    }

    void addMarkers(ArrayList<Tweet> tweets) {
        for (Tweet status : tweets) {

            if (null != status.getGeo()) {
                Geo geo = status.getGeo();


                markers.add(new Location(geo.latitude, geo.longitude));

            } else if (null != status.getPlace()) {

                TweetPlace tweetPlace = status.getPlace();

                polygonMarkers.add(tweetPlace.getLocations());


            }

        }
    }

}
