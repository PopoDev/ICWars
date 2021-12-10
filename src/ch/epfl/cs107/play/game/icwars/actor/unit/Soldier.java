package ch.epfl.cs107.play.game.icwars.actor.unit;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Soldier extends Unit {

    private Sprite sprite;
    private String spriteName;

    // HP_MAX = 5  // TODO Better a static variable or directly a value in constructor
    // MOVE_RADIUS = 2
    private final int DAMAGE = 2;

    public Soldier(Faction faction, Area owner, DiscreteCoordinates position) {
        super(faction, owner, position, 5, 2);
        if (isAlly()) {
            spriteName = "icwars/friendlySoldier";
        } else {
            spriteName = "icwars/enemySoldier";
        }
        sprite = new Sprite(spriteName, 1.5f, 1.5f, this, null, new Vector(-0.25f, -0.25f));

        setName("");  // TODO When do you give a name to Unit ?
    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }

    @Override
    protected int getDamage() { return DAMAGE; }

}
