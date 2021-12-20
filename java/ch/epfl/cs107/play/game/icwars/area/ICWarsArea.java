package ch.epfl.cs107.play.game.icwars.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.icwars.ICWars;
import ch.epfl.cs107.play.game.icwars.actor.unit.Medic;
import ch.epfl.cs107.play.game.icwars.actor.unit.Rocket;
import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
import ch.epfl.cs107.play.game.icwars.actor.unit.action.Attack;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class ICWarsArea extends Area {

    private ICWarsBehavior behavior;

    // List of Unit inside the area
    private List<Unit> units;
    // List of Units we want to register/unregistered from the area for next update iteration
    private List<Unit> registeredUnits;
    private List<Unit> unregisteredUnits;

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            units = new LinkedList<>();
            registeredUnits = new LinkedList<>();
            unregisteredUnits = new LinkedList<>();

            // Set the behavior map
            behavior = new ICWarsBehavior(window, getTitle());
            setBehavior(behavior);
            createArea();
            return true;
        }
        return false;
    }

    @Override
    public void update(float deltaTime) {
        purgeRegistrationUnits();
        super.update(deltaTime);
    }

    /** Create the area by adding it all actors called by begin method */
    protected abstract void createArea();

    /**
     * Register a unit : will be added at next update
     * @param unit (Unit): the unit to register, not null
     */
    public void registerUnit(Unit unit) {
        registeredUnits.add(unit);
    }

    /**
     * Unregister a unit : will be removed at next update
     * @param unit (Unit): the unit to unregister, not null
     */
    public void unregisterUnit(Unit unit) {
        unregisteredUnits.add(unit);
    }

    final void purgeRegistrationUnits() {
        // Register Units
        units.addAll(registeredUnits);
        registeredUnits.clear();

        // Unregister Units
        units.removeAll(unregisteredUnits);
        unregisteredUnits.clear();
    }

    /**
     * Let the player select the Unit to attack
     * @param attacker the unit who is attacking
     * @param listIndex the previous index selected
     * @return the current index
     */
    public int attackSelection(Unit attacker, int listIndex, Attack action) {
        List<Unit> attackableUnits = getAttackableUnits(attacker);
        if (attackableUnits.isEmpty()) { return -1; }  // The list is empty

        Keyboard keyboard = getKeyboard();
        if (keyboard.get(Keyboard.LEFT).isPressed())  { --listIndex; }
        if (keyboard.get(Keyboard.RIGHT).isPressed()) { ++listIndex; }
        listIndex = Math.floorMod(listIndex, attackableUnits.size());

        action.setAttackedUnit(attackableUnits.get(listIndex));

        return listIndex;
    }

    /**
     * Selects the Unit in range of the attacker with the lowest hp
     * @param attacker the unit who is attacking
     * @return <code>True</code> if a target is found, false otherwise
     */
    public boolean autoAttackSelection(Unit attacker, Attack action){
        List<Unit> attackableUnits = getAttackableUnits(attacker);
        if(attackableUnits.isEmpty()) {
            return false;
        }
        Unit min = attackableUnits.get(0);

        for(Unit attacked : attackableUnits) {
            if (attacked.getHp() <= min.getHp()) {
                min = attacked;
            }
        }

        action.setAttackedUnit(min);
        return true;
     }

    /** Returns all the enemies units in range of attack of the attacker */
    private List<Unit> getAttackableUnits(Unit attacker) {
        List<Unit> attackableUnits = new ArrayList<>();
        for (Unit unit : units) {
            if (attacker.canAttack(unit)) {
                attackableUnits.add(unit);
            }
        }
        return attackableUnits;
    }

    /** Returns the player spawn coordinates */
    public abstract DiscreteCoordinates getPlayerSpawnPosition();
    /** Returns the enemy spawn coordinates */
    public abstract DiscreteCoordinates getEnemySpawnPosition();

    @Override
    public float getCameraScaleFactor() { return ICWars.CAMERA_SCALE_FACTOR; }

    //----------------//
    // Extension
    //----------------//

    /** Returns the list of units that are in cells just nearby (radius of 1) */
    private List<Unit> getNeighbourUnits(Unit centerUnit) {
        List<Unit> unitsAround = new ArrayList<>();
        for (Unit unit : units) {
            if (centerUnit.isNextTo(unit)) {
                unitsAround.add(unit);
            }
        }
        return unitsAround;
    }

    /** Heal the allies units that are around the Medic (radius of 1) */
    public void healUnitsAround(Medic medic, int heal) {
        for (Unit unit : getNeighbourUnits(medic)) {
            if (medic.canHeal(unit)) {
                unit.repair(heal);
            }
        }
    }

    /**
     * Attack an enemy unit inflicting area damage to all units around a radius of 1.
     * AoE can damage allies too.
     * */
    public void attackUnitsAround(Rocket rocket) {
        for (Unit unit : getNeighbourUnits(rocket)) {
            rocket.attack(unit);
        }
    }
}
