package com.insta.processing.test1;

import com.temboo.core.TembooSession;
import com.thoughtworks.xstream.*;
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
import org.json.JSONException;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * The type Program.
 * @author Azzur          <p/>
 *         Created at : 19/09/2014
 *         Project : com.insta.processing.1
 */
public class Program extends PApplet {

    /**
     * The Cp 5.
     */
    ControlP5 cp5;
    /**
     * The Current map.
     */
    UnfoldingMap currentMap;
    /**
     * The Session.
     */
    TembooSession session = new TembooSession("azzzzur", "TwitterApp", "f1912a4b69b04e9c84c7b153bf089873");
    /**
     * The Textfield.
     */
    Textfield textfield;
    /**
     * The Search field.
     */
    String searchField = "i'm at";
    /**
     * The Button.
     */
    Button button, /**
     * The Button 2.
     */
    button2;
    /**
     * The Providers.
     */
    List<AbstractMapProvider> providers = new ArrayList<AbstractMapProvider>();
    /**
     * The Buttonimg.
     */
    PImage buttonimg, /**
     * The Buttonimg 2.
     */
    buttonimg2;
    /**
     * The Markers.
     */
    List<Marker> markers = new ArrayList<Marker>();
    private boolean isSearching = false;
    /**
     * The Sup 256.
     */
    boolean sup256 = false;
    /**
     * The System.
     */
    OrbitingSystem system = new OrbitingSystem(this);
    ArrayList<Locatable> localisations = new ArrayList<Locatable>();


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
        fill(0,0,0);
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

        buttonimg2 = loadImage("http://s8.postimg.org/wlul1sf7l/insta.png");
        button2 = cp5.addButton("submitForm2")
                .setPosition(50, 30)
                .setImage(buttonimg2)
                .setSize(173, 238);


        buttonimg = loadImage("https://cdn1.iconfinder.com/data/icons/windows-8-metro-style/64/google_web_search.png");
        button = cp5.addButton("submitForm")
                .setPosition(70, 200)
                .setImage(buttonimg)
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

        button = cp5.addButton("pngsaver")
                .setPosition(15, 480)
                .setSize(185, 40);

        button = cp5.addButton("xmlsaver")
                .setPosition(15, 530)
                .setSize(185, 40);

        AbstractMapProvider msft = new Microsoft.AerialProvider();
        AbstractMapProvider ggle = new Google.GoogleMapProvider();
        AbstractMapProvider yhoo = new Yahoo.RoadProvider();

        providers.add(msft);
        providers.add(ggle);
        providers.add(yhoo);

        currentMap = new UnfoldingMap(this, 210, 10, displayWidth-210, displayHeight-170, ggle);

        MapUtils.createDefaultEventDispatcher(this, currentMap);
        thread("initMap");
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
            fill(0, 51, 154);
            textSize(18);
            text("Loading ...",(displayWidth - textWidth("Loading ...")) / 2, displayHeight / 2+100, 400);
            popMatrix();


            noStroke();
            noFill();
            fill(255);
        }

    }


    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        PApplet.main(
                "com.insta.processing.test1.Program"
        );
    }

    /** Events handler */

    /*
    Save PNG
     */
    public void pngsaver() {
        JFrame parentFrame = new JFrame();
        JFileChooser fileChooser = new JFileChooser();
        int userSelection = fileChooser.showSaveDialog(parentFrame);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            save(fileToSave.getAbsolutePath()+".png");
        }
    }

    /**
     * Update map.
     */


    public void updateMap() {
        isSearching = true;
        currentMap.getDefaultMarkerManager().clearMarkers();

        if(markers.size() <= 0)
        {

            try {
                localisations.clear();
                localisations.addAll(Tweet.getTweetSearch(searchField, session));
                localisations.addAll(InstagramMedia.getInstagramMedias(searchField, session));

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

    /**
     * Init map.
     */

    /*
    Save XML
     */
    public void xmlsaver() throws IOException {

        XStream xstream = new XStream();
        JFrame parentFrame = new JFrame();
        JFileChooser fileChooser = new JFileChooser();
        int userSelection = fileChooser.showSaveDialog(parentFrame);
        if (userSelection == JFileChooser.APPROVE_OPTION)
        {
            File fileToSave = fileChooser.getSelectedFile();
            FileWriter fw = new FileWriter(fileToSave.getAbsoluteFile()+".xml");
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(xstream.toXML(localisations));
            bw.close();
        }

    }

    public void initMap() {
        currentMap.setTweening(true);
        currentMap.setZoomRange(3, 15);
        currentMap.zoomLevel(0);
        updateMap();
    }

    /** Button Callbacks */

    public void zoomOut() {
        currentMap.zoomLevelOut();
    }

    /**
     * Yahoo map.
     */
    public void yahooMap() {
        if (isSearching) {
            pushMatrix();
            fill(0, 51, 154);
            textSize(18);
            text("Loading ...",displayWidth / 2-30, displayHeight / 2+100);
            stroke(180);
            translate(displayWidth / 2, displayHeight / 2,200);
            rotateX(radians(frameCount));
            rotateY(radians(frameCount));
            box(50);
            popMatrix();
        }
        currentMap.mapDisplay.setProvider(providers.get(2));
    }

    /**
     * Google map.
     */
    public void googleMap() {
        if (isSearching) {
            pushMatrix();
            fill(0, 51, 154);
            textSize(18);
            text("Loading ...",displayWidth / 2-30, displayHeight / 2+100);
            stroke(180);
            translate(displayWidth / 2, displayHeight / 2,200);
            rotateX(radians(frameCount));
            rotateY(radians(frameCount));
            box(50);
            popMatrix();
        }
        currentMap.mapDisplay.setProvider(providers.get(1));
    }

    /**
     * Microsoft map.
     */
    public void microsoftMap() {
        if (isSearching) {
            pushMatrix();
            fill(0, 51, 154);
            textSize(18);
            text("Loading ...",displayWidth / 2-30, displayHeight / 2+100);
            stroke(180);
            translate(displayWidth / 2, displayHeight / 2,200);
            rotateX(radians(frameCount));
            rotateY(radians(frameCount));
            box(50);
            popMatrix();
        }
        currentMap.mapDisplay.setProvider(providers.get(0));

    }

    /**
     * Submit form.
     */
    public void submitForm() {
        if (!searchField.equalsIgnoreCase(textfield.getText())) {
            searchField = textfield.getText();
            markers = new ArrayList<Marker>();
        }

        System.out.println("Search for \""+searchField+"\"");

        textfield.setFocus(true);
        thread("updateMap");
    }

}

