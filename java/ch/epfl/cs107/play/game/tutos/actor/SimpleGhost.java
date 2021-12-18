package ch.epfl.cs107.play.game.tutos.actor;

import ch.epfl.cs107.play.game.actor.Entity;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.awt.*;

public class SimpleGhost extends Entity {

    private float hp;  // Energy level can't be below 0
    private final float HP_DEFAULT = 10f;
    private final float MOVE_DISTANCE = 0.05f;

    private Sprite sprite;
    private TextGraphics hpText;

    public SimpleGhost(Vector position, String spriteName) {
        super(position);
        strengthen();

        sprite = new Sprite(spriteName, 1, 1.f, this);

        hpText = new TextGraphics(Integer.toString((int)hp), 0.4f, Color.BLUE);
        hpText.setParent(this);
        this.hpText.setAnchor(new Vector(-0.3f, 0.1f));
    }

    public boolean isWeak() {
        return hp <= 0;
    }

    public void strengthen() {
        hp = HP_DEFAULT;
    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
        hpText.draw(canvas);
    }

    @Override
    public void update(float deltaTime) {
        hp -= deltaTime;
        if (isWeak()) { hp = 0; }

        hpText.setText(Integer.toString((int)hp));  // Refresh the text
    }

    public void moveUp() { setCurrentPosition(getPosition().add(0.f, MOVE_DISTANCE)); }

    public void moveDown() { setCurrentPosition(getPosition().add(0.f, -MOVE_DISTANCE)); }

    public void moveRight() { setCurrentPosition(getPosition().add(MOVE_DISTANCE, 0.f)); }

    public void moveLeft() { setCurrentPosition(getPosition().add(-MOVE_DISTANCE, 0.f)); }

}
