package ch.epfl.cs107.play.game.icwars.actor.graphics;

import ch.epfl.cs107.play.game.actor.ShapeGraphics;
import ch.epfl.cs107.play.math.Shape;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.awt.*;

//----------------//
// Extension
//----------------//
public class HealthBarGraphics extends ShapeGraphics {

    private Vector anchor;

    public HealthBarGraphics(Shape shape, Color fillColor, Color outlineColor, float thickness, float alpha, float depth) {
        super(shape, fillColor, outlineColor, thickness, alpha, depth);
    }

    /**
     * Sets image anchor location, i.e. where is the center of the image.
     * @param anchor (Vector): image anchor, not null
     */
    public void setAnchor(Vector anchor) {
        this.anchor = anchor;
    }

    /** @return (Vector): image anchor, not null */
    public Vector getAnchor() {
        return anchor;
    }

    @Override
    public void draw(Canvas canvas) {
        //setRelativeTransform(Transform.I.translated(canvas.getPosition().add(getPosition()).add(anchor)));
        //System.out.println(Transform.I.translated(getPosition().add(anchor)));
        //System.out.println(getTransform());
        super.draw(canvas);
        //setRelativeTransform(Transform.I);  // Reset
    }
}
