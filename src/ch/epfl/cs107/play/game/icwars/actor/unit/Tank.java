package ch.epfl.cs107.play.game.icwars.actor.unit;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Tank extends Unit {

    private final Sprite sprite;
    private String spriteName;

    // HP_MAX = 10
    // MOVE_RADIUS = 4
    private final int DAMAGE = 7;

    public Tank(Faction faction, Area owner, DiscreteCoordinates position) {
        super(faction, owner, position, 10, 4);
        if (isAlly()) {
            spriteName = "icwars/friendlyTank";
        } else {
            spriteName = "icwars/enemyTank";
        }
        sprite = new Sprite(spriteName, 1.5f, 1.5f, this, null, new Vector(-0.25f, -0.25f));
    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }

    @Override
    protected int getDamage() { return DAMAGE; }

}
