package ch.epfl.cs107.play.game.icwars.actor.unit;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.icwars.actor.unit.action.Attack;
import ch.epfl.cs107.play.game.icwars.actor.unit.action.Mend;
import ch.epfl.cs107.play.game.icwars.actor.unit.action.Wait;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Tank extends Unit {

    // HP_MAX = 10
    // MOVE_RADIUS = 4
    private final int DAMAGE = 7;

    public Tank(Faction faction, Area owner, DiscreteCoordinates position) {
        super(faction, owner, position, 10, 4,
              new String[]{"icwars/friendlyTank", "icwars/enemyTank"});  // Index 0 : Ally / 1 : Enemy

        initActions(new Attack(this, owner), new Mend(this, owner), new Wait(this, owner));
    }

    @Override
    public int getDamage() { return DAMAGE; }

    @Override
    public String toString() {
        return "Tank" + super.toString();
    }
}
