package ch.epfl.cs107.play.game.icwars.actor.graphics;

import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.math.Polygon;
import ch.epfl.cs107.play.math.Shape;

import java.awt.Color;


public class HealthLostBarGraphics extends HealthBarGraphics {

    private final float totalWidth;

    public HealthLostBarGraphics(float totalWidth, float width, float height, Sprite sprite) {
        super(width, height, sprite);
        setFillColor(Color.RED);

        this.totalWidth = totalWidth;

        this.setBarWidth(width);
    }

    @Override
    public void setBarWidth(float width) {
        Shape rect = new Polygon(totalWidth, relativeHeight, totalWidth, relativeHeight + barHeight,
                width, relativeHeight + barHeight, width, relativeHeight);
        setShape(rect);
    }
}
