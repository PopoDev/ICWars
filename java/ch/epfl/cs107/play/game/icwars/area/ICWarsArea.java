package ch.epfl.cs107.play.game.icwars.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.icwars.ICWars;
import ch.epfl.cs107.play.game.icwars.actor.unit.Medic;
import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
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
     * @return the index of the attacked Unit and the current index
     */
    public int[] attackSelection(Unit attacker, int listIndex) {
        List<Integer> attackableUnitsIndex = getAttackableUnitsIndex(attacker);
        if (attackableUnitsIndex.isEmpty()) { return new int[] {-1, listIndex}; }  // The list is empty

        Keyboard keyboard = getKeyboard();
        if (keyboard.get(Keyboard.LEFT).isPressed())  { --listIndex; }
        if (keyboard.get(Keyboard.RIGHT).isPressed()) { ++listIndex; }
        listIndex = Math.floorMod(listIndex, attackableUnitsIndex.size());

        return new int[] {attackableUnitsIndex.get(listIndex), listIndex};
    }

    /**
     * Selects the Unit in range of the attacker with the lowest hp
     * @param attacker the unit who is attacking
     * @return the index of the Unit with the lowest hp
     */
    public Unit autoAttackSelection(Unit attacker){
        List<Integer> attackableUnitsIndex = getAttackableUnitsIndex(attacker);
        if(attackableUnitsIndex.isEmpty()) {
            return null;
        }
        Unit min = getUnitFromIndex(0);

        for(Integer index : attackableUnitsIndex){
           attacker = getUnitFromIndex(index);
           if(attacker.getHp() <= min.getHp()){
               min = attacker;
           }
        }
        return min;
     }

    /** Returns the Unit stored at this index in ICWarsArea */
    public Unit getUnitFromIndex(int index) {
        return units.get(index);
    }

    /** Returns all the enemies units in range of attack of the attacker */
    private List<Integer> getAttackableUnitsIndex(Unit attacker) {
        List<Integer> attackableUnitsIndex = new ArrayList<>();
        for (Unit unit : units) {
            if (attacker.canAttack(unit)) {
                attackableUnitsIndex.add(units.indexOf(unit));
            }
        }
        return attackableUnitsIndex;
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

    /** Returns the list of units that are in cells just nearby (Radius of 1) */
    public List<Integer> getNeighbourUnits(Unit selectedUnit) {
        List<Integer> unitsAroundIndex = new ArrayList<>();
        for (Unit unit : units) {
            if (selectedUnit.isNextTo(unit)) {
                unitsAroundIndex.add(units.indexOf(unit));
            }
        }
        return unitsAroundIndex;
    }

    /** Returns the list of units that can be healed */
    public List<Integer> getUnitsToHeal(Medic selectedUnit) {
        List<Integer> unitsToHealIndex = new ArrayList<>();
        for (int index : getNeighbourUnits(selectedUnit)) {
            if (selectedUnit.canHeal(getUnitFromIndex(index))) {
                unitsToHealIndex.add(index);
            }
        }
        return unitsToHealIndex;
    }
}
