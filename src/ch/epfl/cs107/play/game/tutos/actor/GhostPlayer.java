package ch.epfl.cs107.play.game.tutos.actor;

import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.awt.*;
import java.util.Collections;
import java.util.List;

public class GhostPlayer extends MovableAreaEntity {

    private float hp;  // Energy level can't be below 0
    private final float HP_DEFAULT = 10f;
    private final float MOVE_DISTANCE = 0.05f;

    private Sprite sprite;
    private TextGraphics hpText;

    /// Animation duration in frame number
    private final static int ANIMATION_DURATION = 8;

    public GhostPlayer(Area owner, Orientation orientation, DiscreteCoordinates coordinates, String spriteName) {
        super(owner, orientation, coordinates);
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

    public void enterArea(Area area, DiscreteCoordinates position) {
        getOwnerArea().unregisterActor(this);  // First unregister from the owner area
        setOwnerArea(area);  // Then change area

        area.registerActor(this);
        setCurrentPosition(position.toVector());
        resetMotion();
    }

    @Override
    public void update(float deltaTime) {
        hp -= deltaTime;
        if (isWeak()) { hp = 0; }
        hpText.setText(Integer.toString((int)hp));  // Refresh the text

        Keyboard keyboard = getOwnerArea().getKeyboard();

        Orientation lastOrientation = getOrientation();
        Orientation newOrientation = null;  // If stays null --> no movement
        if (isKeyDown(keyboard, Keyboard.UP))    { newOrientation = Orientation.UP; }
        if (isKeyDown(keyboard, Keyboard.DOWN))  { newOrientation = Orientation.DOWN; }
        if (isKeyDown(keyboard, Keyboard.RIGHT)) { newOrientation = Orientation.RIGHT; }
        if (isKeyDown(keyboard, Keyboard.LEFT))  { newOrientation = Orientation.LEFT; }

        if (newOrientation != null) {
            if (lastOrientation == newOrientation) {
                move(ANIMATION_DURATION);
            } else {
                orientate(newOrientation);
            }
        }

        super.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
        hpText.draw(canvas);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean takeCellSpace() { return true; }

    @Override
    public boolean isCellInteractable() { return true; }

    @Override
    public boolean isViewInteractable() { return true; }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
    }

    private boolean isKeyDown(Keyboard keyboard, int keyCode) {
        Button key = keyboard.get(keyCode);
        return key.isDown();
    }
}
