package ch.epfl.cs107.play.game.icwars.actor.unit;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.icwars.actor.unit.action.Attack;
import ch.epfl.cs107.play.game.icwars.actor.unit.action.Mend;
import ch.epfl.cs107.play.game.icwars.actor.unit.action.Wait;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Rocket extends Unit {

    // HP_MAX = 5
    // MOVE_RADIUS = 3
    private final int DAMAGE = 2;  // Area damage

    public Rocket(Faction faction, Area owner, DiscreteCoordinates position) {
        super(faction, owner, position, 5, 3,
                new String[]{"icwars/friendlyRocket", "icwars/enemyRocket"});  // Index 0 : Ally / 1 : Enemy

        initActions(new Attack(this, owner), new Mend(this, owner), new Wait(this, owner));
    }

    /**
     * Attack a Unit with area damage inflicting damage on surrounding units
     * @param other the attacked unit is the center of the area damage
     */
    @Override
    public void attack(Unit other) {
        ICWarsArea area = (ICWarsArea) getOwnerArea();
        for (int index : area.getNeighbourUnits(other)) {
            super.attack(area.getUnitFromIndex(index));
        }
        super.attack(other);
    }

    @Override
    public int getDamage() { return DAMAGE; }

    @Override
    public String toString() {
        return "Rocket" + super.toString();
    }
}
