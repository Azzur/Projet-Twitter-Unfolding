package com.insta.processing.test1;

import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractMarker;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 * @author Azzur
 *         <p/>
 *         Created at : 22/09/2014
 *         Project : com.insta.processing.1
 */
public class PlaceMarker extends AbstractMarker {

    PImage img;
    String text ="";
    PImage mediaUrl;
    String author = "";
    PApplet engine;

    public PlaceMarker(Location location, PImage img) {
        super(location);
        this.img = img;
    }

    public void setMediaInfos(String text, PImage mediaUrl, String author) {
        this.text = text;
        this.mediaUrl = mediaUrl;
        this.author = author;

    }

    @Override
    public void draw(PGraphics pg, float x, float y) {

        pg.pushStyle();
        pg.imageMode(PConstants.CORNER);
        pg.image(img, x - img.width/2, y - img.height/2);
        if (isSelected())  {

            engine.fill(0);
            engine.stroke(0);

            float largeur = 160;


            engine.rect(210, engine.displayHeight-largeur,engine.displayWidth-200,largeur);

            if (mediaUrl != null) {
                engine.image(mediaUrl,220,engine.displayHeight-largeur,150,150);
            }

            /*
            text = text.replaceAll("[^\\x00\\x08\\x0B\\x0C\\x0E-\\x1F]*", "?");
             */


            engine.fill(0);
            try {
                engine.fill(255);
                engine.text(text, 200+180, engine.displayHeight-largeur+50);
                engine.text(author, 200+180,engine.displayHeight-largeur+5,engine.displayWidth-200-160-10,40 );

            } catch (Exception e) {
                System.out.println( e.getClass().getName()+" : " + e.getMessage()+"" +"\r\n\ttext : "+text+"\r\n\tauthor : "+author);
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
        pg.popStyle();
    }


    @Override
    protected boolean isInside(float checkX, float checkY, float x, float y) {
        return checkX > x && checkX < x + img.width && checkY > y && checkY < y + img.height;
    }

    public void setEngine(PApplet engine) {
        this.engine = engine;
    }

    @Override
    public String toString() {
        return "PlaceMarker{" +
                "img=" + img +
                ", text='" + text + '\'' +
                ", mediaUrl=" + mediaUrl +
                ", author='" + author + '\'' +
                '}';
    }
}
