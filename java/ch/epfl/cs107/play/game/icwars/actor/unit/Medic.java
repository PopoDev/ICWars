package ch.epfl.cs107.play.game.icwars.actor.unit;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.icwars.actor.unit.action.Heal;
import ch.epfl.cs107.play.game.icwars.actor.unit.action.Mend;
import ch.epfl.cs107.play.game.icwars.actor.unit.action.Wait;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

//----------------//
// Extension
//----------------//
public class Medic extends Unit {

    // HP_MAX = 6
    // MOVE_RADIUS = 2
    private final int HEAL = 2;

    public Medic(Faction faction, Area owner, DiscreteCoordinates position) {
        super(faction, owner, position, 6, 2,
                new String[]{"icwars/friendlyMedic", "icwars/enemyMedic"});  // Index 0 : Ally / 1 : Enemy

        initActions(new Heal(this, owner), new Mend(this, owner), new Wait(this, owner));
    }

    /**
     * Return whether the Medic can heal this unit.
     * A Medic can't heal himself and enemy units
     */
    public boolean canHeal(Unit unit) {
        return unit != this && areInSameFaction(this, unit);
    }

    /** Return the amount the Medic can heal */
    @Override
    public int getDamage() { return HEAL; }

    @Override
    public String toString() {
        return "Medic" + super.toString();
    }
}
