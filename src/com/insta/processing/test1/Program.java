package com.insta.processing.test1;

import com.temboo.Library.Instagram.RecentlyTaggedMedia;
import com.temboo.Library.Instagram.SearchTags;
import com.temboo.Library.Twitter.Search.Tweets;
import com.temboo.core.TembooSession;
import controlP5.Button;
import controlP5.ControlP5;
import controlP5.Textfield;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.providers.Yahoo;
import de.fhpotsdam.unfolding.utils.MapUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Azzur
 *         <p/>
 *         Created at : 19/09/2014
 *         Project : com.insta.processing.1
 */
public class Program extends PApplet {
    ControlP5 cp5;
    UnfoldingMap currentMap;
    TembooSession session = new TembooSession("senorihl", "Processing", "ee23a86b447a45eba3c7f798bb1aa1b4");
    Textfield textfield;
    String searchField = "i'm at";
    Button button,button2;
    List<AbstractMapProvider> providers = new ArrayList<AbstractMapProvider>();
    PImage buttonimg,buttonimg2;
    List<Marker> markers = new ArrayList<Marker>();
    private boolean isSearching = false;
    boolean sup256 = false;
    OrbitingSystem system = new OrbitingSystem(this);


    public boolean sketchFullScreen() {
        return true;
    }

    public void setup() {
        size(displayWidth, displayHeight, P3D);
        if (frame != null) {
            frame.setResizable(false);
        }

        sketchFullScreen();

        smooth();
        background(0,0,0);
        pushMatrix();
        fill(204,204,204);
        stroke(0,0,0);
        rect(9,9, 202, displayWidth );
        popMatrix();
        PFont font = createFont("arial",20);

        cp5 = new ControlP5(this);



        textfield = cp5.addTextfield("input")
                .setFont(font)
                .setPosition(15,150)
                .setSize(185,40)
                .setFocus(true)
                .setColor(color(255,0,0))
                .setText(searchField)
        ;

        buttonimg2 = loadImage("https://cdn1.iconfinder.com/data/icons/plex-for-android/96/twitter.png");
        button2 = cp5.addButton("submitForm2")
                .setPosition(40, 30)
                .setImage(buttonimg2)
                .setSize(173, 238);


        buttonimg = loadImage("https://cdn1.iconfinder.com/data/icons/windows-8-metro-style/64/google_web_search.png");
        button = cp5.addButton("submitForm")
                .setPosition(70, 200)
                .setImage(buttonimg)
                .setSize(173, 238);

        button = cp5.addButton("zoomOut")
                .setPosition(15, 280)
                .setSize(185, 40);

        button = cp5.addButton("microsoftMap")
                .setPosition(15, 330)
                .setSize(185, 40);

        button = cp5.addButton("googleMap")
                .setPosition(15, 380)
                .setSize(185, 40);

        button = cp5.addButton("yahooMap")
                .setPosition(15, 430)
                .setSize(185, 40);




        AbstractMapProvider msft = new Microsoft.AerialProvider();
        AbstractMapProvider ggle = new Google.GoogleMapProvider();
        AbstractMapProvider yhoo = new Yahoo.RoadProvider();

        providers.add(msft);
        providers.add(ggle);
        providers.add(yhoo);

        currentMap = new UnfoldingMap(this, 210, 10, displayWidth-420, displayHeight, ggle);

        MapUtils.createDefaultEventDispatcher(this, currentMap);

        thread("initMap");


    }

    public void yahooMap() {
        currentMap.mapDisplay.setProvider(providers.get(2));
    }

    public void googleMap() {
        currentMap.mapDisplay.setProvider(providers.get(1));
    }

    public void microsoftMap() {
        currentMap.mapDisplay.setProvider(providers.get(0));
    }


    public void initMap() {
        currentMap.setTweening(true);
        currentMap.setZoomRange(3, 15);
        currentMap.zoomLevel(0);
        updateMap();
    }

    public void mouseMoved() {
        Marker hitMarker = currentMap.getFirstHitMarker(mouseX, mouseY);
        if (hitMarker != null) {
            // Select current marker
            hitMarker.setSelected(true);
        } else {
            // Deselect all other markers
            for (Marker marker : currentMap.getMarkers()) {
                marker.setSelected(false);
            }
        }
    }

    public void draw() {

        background(0);
        currentMap.draw();
        currentMap.setActive(false);
        textfield.setFocus(true);

        if (frameCount%256 == 0)
            sup256 = !sup256;


        if (isSearching) {
            lights();

            pushMatrix();
                noFill();
                stroke( 0 );
                translate(displayWidth / 2, displayHeight / 2,400);
                rotateX(radians(frameCount%360));
                rotateY(radians(frameCount));
                box(50);
            popMatrix();


            pushMatrix();
                stroke(0xff3f729b);
                fill(64,153,255);
                translate(displayWidth / 2, displayHeight / 2,400);
                rotateX(-radians(frameCount%360));
                rotateZ(-radians(frameCount));
                box(40);
            popMatrix();


            pushMatrix();
                translate(displayWidth / 2, displayHeight / 2, 0);
                system.draw();
            popMatrix();


            noStroke();
            noFill();
            fill(255);
        }

    }


    public static void main(String[] args) {
        PApplet.main(
                "com.insta.processing.test1.Program"
        );
    }

    ArrayList<Tweet> getTweetSearch(String search) throws JSONException {
        Tweets tweetsChoreo = new Tweets(session);
        tweetsChoreo.setAccessToken("2775368387-j63m9FFKfDGMH7FSqVYTglvN7ZcJdsuxe1MpwSz");
        tweetsChoreo.setAccessTokenSecret("JfFDFqaJsa2CNNVY1my20TkGaDSzxmT695p3vnGv2Pdzz");
        tweetsChoreo.setConsumerSecret("l1EvbO2LYN5FCWrMoFxT1bI7GkbteYlBBqsebrYbDiEEBVzHZ3");
        tweetsChoreo.setConsumerKey("6m1HphSLGe99BnixQGCPmG4W0");
        tweetsChoreo.setQuery(search);
        tweetsChoreo.setCount(200);

        String result = tweetsChoreo.run().getResponse();

        return Tweet.parseJSON(result);

    }

    ArrayList<InstagramMedia> getInstagramMedias(String search) throws JSONException {
        System.out.println("Retrieving Instragram Media");
        ArrayList<InstagramMedia> medias = new ArrayList<InstagramMedia>();
        SearchTags searchTags = new SearchTags(session);
        searchTags.setAccessToken("53267369.1fb234f.9bfabe4bbfc746278a4db7f1c3cc3467");
        searchTags.setQuery(search);

        String result = searchTags.run().getResponse();

        JSONObject jsonObject = new JSONObject(result);
        JSONArray data = jsonObject.getJSONArray("data");
        if (data.length() > 0) {
            RecentlyTaggedMedia media = new RecentlyTaggedMedia(session);
            media.setAccessToken(/*oAuth.run().getAccessToken()*/"53267369.1fb234f.9bfabe4bbfc746278a4db7f1c3cc3467");
            media.setTagName(data.getJSONObject(0).getString("name"));
            media.setClientID("15c2b97cc14d47768b9ab1052efeb016");
            

            result = media.run().getResponse();
            jsonObject = new JSONObject(result);
            data = jsonObject.getJSONArray("data");
            if (data.length() > 0) {
                for (int i = 0 ; i < data.length() ; i++) {
                    JSONObject instagramJSON = data.getJSONObject(i);
                    JSONObject location = instagramJSON.getJSONObject("location");
                    if (location  != null) {

                        System.out.println(instagramJSON);
                        InstagramMedia instagramMedia = new InstagramMedia();
                        instagramMedia.setGeo(location.getDouble("latitude"), location.getDouble("longitude"));
                        instagramMedia.setMediaInfos(
                                (instagramJSON.getJSONObject("caption") != null ? instagramJSON.getJSONObject("caption").getString("text") : ""),
                                instagramJSON.getJSONObject("images").getJSONObject("thumbnail").getString("url"),
                                instagramJSON.getJSONObject("user").getString("username")
                        );
                        medias.add(instagramMedia);
                    }
                }

            }
            System.out.println(data.length() + "founded, "+medias.size()+" localized");

        }

        return medias;
    }

    public void submitForm() {
        if (!searchField.equalsIgnoreCase(textfield.getText())) {
            searchField = textfield.getText();
            markers = new ArrayList<Marker>();
        }

        System.out.println("Search for \""+searchField+"\"");

        textfield.setText("");
        textfield.setFocus(true);
        thread("updateMap");
    }

    public void updateMap() {
        isSearching = true;
        currentMap.getDefaultMarkerManager().clearMarkers();

        if(markers.size() <= 0)
        {

            try {
                ArrayList<Locatable> localisations = new ArrayList<Locatable>();
                localisations.addAll(getTweetSearch(searchField));
                localisations.addAll(getInstagramMedias(searchField));

                for (Locatable localisation : localisations) {
                    Marker marker = localisation.getMarker(this);
                    markers.add(marker);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        isSearching = false;
        currentMap.addMarkers(markers);
    }

    public void zoomOut() {
        currentMap.zoomLevelOut();
    }

}
