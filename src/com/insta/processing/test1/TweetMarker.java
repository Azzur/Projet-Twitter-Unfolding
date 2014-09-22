package com.insta.processing.test1;

import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractMarker;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 * @author Rodolphe
 *         <p/>
 *         Created at : 22/09/2014
 *         Project : com.insta.processing.1
 */
public class TweetMarker extends AbstractMarker {

    PImage img;
    String text;

    public TweetMarker(Location location, PImage img) {
        super(location);
        this.img = img;
//        this.text = text;
    }

    @Override
    public void draw(PGraphics pg, float x, float y) {

        pg.pushStyle();
        pg.imageMode(PConstants.CORNER);
        pg.image(img, x - img.width/2, y - img.height/2);
//        if (isSelected()) pg.text(text, (float) (x - pg.textWidth(text) / 2), y + 4);
        pg.popStyle();
    }


    @Override
    protected boolean isInside(float checkX, float checkY, float x, float y) {
        return checkX > x && checkX < x + img.width && checkY > y && checkY < y + img.height;
    }

}
