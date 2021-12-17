package ch.epfl.cs107.play.game.icwars.actor.graphics;

import ch.epfl.cs107.play.game.actor.ShapeGraphics;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.math.Polygon;
import ch.epfl.cs107.play.math.Shape;

import java.awt.Color;

//----------------//
// Extension
//----------------//
public class HealthBarGraphics extends ShapeGraphics {

    private final float relativeWidth;
    protected final float relativeHeight;
    protected final float barHeight;

    public HealthBarGraphics(float width, float height, Sprite sprite) {
        super(null, null, null, 0f, 1.0f, 3000f);
        setFillColor(Color.GREEN);

        relativeWidth = .085f;
        relativeHeight = sprite.getHeight()/2 + .2f;
        barHeight = height;

        setBarWidth(width);
    }

    public void setBarWidth(float width) {
        Shape rect = new Polygon(relativeWidth, relativeHeight, relativeWidth, relativeHeight + barHeight,
                relativeWidth + width, relativeHeight + barHeight, relativeWidth + width, relativeHeight);
        setShape(rect);
    }
}
