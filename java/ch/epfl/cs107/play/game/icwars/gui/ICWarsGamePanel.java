package ch.epfl.cs107.play.game.icwars.gui;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.ShapeGraphics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.math.*;
import ch.epfl.cs107.play.math.Polygon;
import ch.epfl.cs107.play.math.Shape;
import ch.epfl.cs107.play.window.Canvas;

import java.awt.*;

//----------------//
// Extension
//----------------//
public class ICWarsGamePanel implements Graphics {

    private final float fontSize;
    private final float cameraScaleFactor;

    private String level;
    private int money;

    /// Sprite and text graphics line
    private final ShapeGraphics background;
    private final TextGraphics levelText, moneyText;

    public ICWarsGamePanel(float cameraScaleFactor) {
        final float height = cameraScaleFactor / 8;
        final float width = cameraScaleFactor / 4;
        this.cameraScaleFactor = cameraScaleFactor;

        fontSize = cameraScaleFactor / 36.0f;

        Shape rect = new Polygon(0, 0, 0, height, width, height, width, 0);
        background = new ShapeGraphics(rect, Color.DARK_GRAY, Color.BLACK, 0f, 0.7f, 3000f);

        levelText = new TextGraphics("", fontSize, Color.WHITE, null, 0.0f,
                true, false, new Vector(0, -0.3f),
                TextAlign.Horizontal.LEFT, TextAlign.Vertical.MIDDLE, 1.0f, 3001f);

        moneyText = new TextGraphics("", fontSize, Color.WHITE, null, 0.0f,
                true, false, new Vector(0, -1.5f * fontSize - 0.3f),
                TextAlign.Horizontal.LEFT, TextAlign.Vertical.MIDDLE, 1.0f, 3001f);
    }

    public void setCurrentLevelName(String levelName) { this.level = levelName; }

    public void setMoney(int money) { this.money = money; }

    @Override
    public void draw(Canvas canvas) {
        // Compute width, height and anchor
        float width = canvas.getXScale();
        float height = canvas.getYScale();

        final Transform transform = Transform.I.translated(canvas.getPosition().add(-width / 2, height / 4 +
                (cameraScaleFactor / 8)));
        background.setRelativeTransform(transform);
        background.draw(canvas);

        final Transform textTransform = Transform.I.translated(canvas.getPosition()
                .add(-width / 2 + .15f, height / 2 - .2f));

        levelText.setRelativeTransform(textTransform);
        levelText.setText("Map : " + level);
        levelText.setFontName("OpenSans-Bold");
        levelText.draw(canvas);

        moneyText.setRelativeTransform(textTransform);
        moneyText.setText("Money : " + money + '$');
        moneyText.setFontName("OpenSans-Bold");
        moneyText.draw(canvas);
    }
}
