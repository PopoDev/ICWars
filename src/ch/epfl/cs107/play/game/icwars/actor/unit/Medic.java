package ch.epfl.cs107.play.game.icwars.actor.unit;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.icwars.actor.unit.action.Heal;
import ch.epfl.cs107.play.game.icwars.actor.unit.action.Wait;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

//----------------//
// Extension
//----------------//
public class Medic extends Unit {

    // HP_MAX = 6
    // MOVE_RADIUS = 3
    private final int HEAL = 2;

    public Medic(Faction faction, Area owner, DiscreteCoordinates position) {
        super(faction, owner, position, 6, 3,
                new String[]{"icwars/friendlyMedic", "icwars/enemyMedic"});  // Index 0 : Ally / 1 : Enemy

        initActions(new Wait(this, owner), new Heal(this, owner));
    }

    @Override
    public int getDamage() { return HEAL; }

    @Override
    public String toString() {
        return "Medic" + super.toString();
    }
}