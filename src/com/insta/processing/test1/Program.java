package com.insta.processing.test1;

import com.temboo.Library.Twitter.Search.Tweets;
import de.fhpotsdam.unfolding.*;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePolygonMarker;
import controlP5.*;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.utils.*;
import org.json.JSONException;
import processing.core.*;
import com.temboo.core.*;
import com.temboo.Library.Twitter.Trends.*;

import java.net.URL;
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
    Textfield textfield;
    String searchField = "#temboo";
    Button button;

    public void setup() {
        size(1000, 600, OPENGL);
        background(0,0,0);
        pushMatrix();
        fill(0 , 0, 0);
        rect(209, 9, 782, 582);
        popMatrix();
        pushMatrix();
        fill(255,255,255);
        stroke(0,0,0);
        rect(9,9, 202, 582 );
        popMatrix();
        noStroke();
        noFill();
        PFont font = createFont("arial",20);

        cp5 = new ControlP5(this);

        textfield = cp5.addTextfield("input")
                .setFont(font)
                .setPosition(15,15)
                .setSize(185,40)
                .setFocus(true)
                .setColor(color(255,0,0))
                .setText(searchField)
        ;
        button = cp5.addButton("submitForm")
                .setPosition(15, 60)
                .setSize(185, 40);
        button = cp5.addButton("resetZoom")
                .setPosition(15,545)
                .setSize(185,40);

        map = new UnfoldingMap(this , 210, 10, 780 , 580, new Google.GoogleTerrainProvider());

        MapUtils.createDefaultEventDispatcher(this, map);
        thread("updateMap");


    }

    public void draw() {

        map.draw();
    }

    public static void main(String[] args) {
        PApplet.main(
                "com.insta.processing.test1.Program"
        );
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

        System.out.println(tweets.size() + " Tweets");
        int i = 0;
        int j = 0;
        for (Tweet status : tweets) {

            if (null != status.getGeo()) {
                Geo geo = status.getGeo();


                markers.add(new Location(geo.latitude, geo.longitude));
                i++;
            } else if (null != status.getPlace()) {

                TweetPlace tweetPlace = status.getPlace();

                polygonMarkers.add(tweetPlace.getLocations());
                j++;

            }

        }

        System.out.println((i+j) + " Localized tweets ("+i +" points, "+j+" polygons)");
    }

    public void submitForm() {
        searchField = textfield.getText();
        thread("updateMap");
    }

    public void updateMap() {

        button.setOff();

        map.getDefaultMarkerManager().clearMarkers();
        markers = new ArrayList<Location>();
        polygonMarkers = new ArrayList<List<Location>>();

        try {
            ArrayList<Tweet> tweets = getTweetSearch(searchField);
            addMarkers(tweets);

            for (Location marker : markers)
                map.addMarker(new TweetMarker(marker, loadImage("C:\\Users\\Rodolphe\\IdeaProjects\\cours\\com.insta.processing.1\\Projet-Twitter-Unfolding\\data\\img\\larry.png")));
            for (List<Location> polygonLocations : polygonMarkers)
                map.addMarker(new SimplePolygonMarker(polygonLocations));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        button.setOn();
    }

    public void resetZoom() {
        map.zoomLevelOut();
    }

}
