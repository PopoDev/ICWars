package ch.epfl.cs107.play.game.icwars.actor.unit;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.icwars.actor.unit.action.Attack;
import ch.epfl.cs107.play.game.icwars.actor.unit.action.Mend;
import ch.epfl.cs107.play.game.icwars.actor.unit.action.Wait;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Soldier extends Unit {

    // HP_MAX = 5
    // MOVE_RADIUS = 2
    private final int DAMAGE = 2;

    public Soldier(Faction faction, Area owner, DiscreteCoordinates position) {
        super(faction, owner, position, 5, 2,
              new String[]{"icwars/friendlySoldier", "icwars/enemySoldier"});  // Index 0 : Ally / 1 : Enemy

        initActions(new Attack(this, owner), new Mend(this, owner), new Wait(this, owner));
    }

    @Override
    public int getDamage() { return DAMAGE; }

    @Override
    public String toString() {
        return "Soldier" + super.toString();
    }
}
