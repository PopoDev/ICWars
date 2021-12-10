package ch.epfl.cs107.play.game.icwars.actor.unit;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;

public class Soldier extends Unit {

    // HP_MAX = 5  // TODO Better a static variable or directly a value in constructor
    // MOVE_RADIUS = 2
    private final int DAMAGE = 2;

    public Soldier(Faction faction, Area owner, DiscreteCoordinates position) {
        super(faction, owner, position, 5, 2);

        String spriteName;
        if (isAlly()) {
            spriteName = "icwars/friendlySoldier";
        } else {
            spriteName = "icwars/enemySoldier";
        }
        setSprite(new Sprite(spriteName, 1.5f, 1.5f, this, null, new Vector(-0.25f, -0.25f)));
    }

    @Override
    protected int getDamage() { return DAMAGE; }

}
