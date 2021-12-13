package ch.epfl.cs107.play.game.icwars.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.icwars.ICWars;
import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

import java.util.ArrayList;
import java.util.List;

public abstract class ICWarsArea extends Area {

    private ICWarsBehavior behavior;

    // List of Unit inside the area
    private final List<Unit> units = new ArrayList<>();

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            // Set the behavior map
            behavior = new ICWarsBehavior(window, getTitle());
            setBehavior(behavior);
            createArea();
            return true;
        }
        return false;
    }

    /** Create the area by adding it all actors called by begin method */
    protected abstract void createArea();

    public void registerUnits(List<Unit> units) {
        for (Unit unit : units) {
            registerUnit(unit);
        }
    }

    public void registerUnit(Unit unit) {
        units.add(unit);
    }

    public void unregisterUnit(Unit unit) {
        units.remove(unit);
    }

    public int[] attackSelection(Unit attacker, int listIndex) {
        List<Integer> attackableUnitsIndex = getUnitsIndex(attacker);
        if (attackableUnitsIndex.isEmpty()) { return new int[] {-1, listIndex}; }  // The list is empty

        Keyboard keyboard = getKeyboard();
        if (keyboard.get(Keyboard.LEFT).isPressed())  { --listIndex; }
        if (keyboard.get(Keyboard.RIGHT).isPressed()) { ++listIndex; }
        listIndex = Math.floorMod(listIndex, attackableUnitsIndex.size());

        return new int[] {attackableUnitsIndex.get(listIndex), listIndex};
    }

    public Unit getUnitFromIndex(int index) {
        return units.get(index);
    }

    private List<Integer> getUnitsIndex(Unit attacker) {
        List<Integer> attackableUnitsIndex = new ArrayList<>();
        for (Unit unit : units) {
            if (attacker.canAttack(unit)) {
                attackableUnitsIndex.add(units.indexOf(unit));
            }
        }
        return attackableUnitsIndex;
    }

    public abstract DiscreteCoordinates getPlayerSpawnPosition();
    public abstract DiscreteCoordinates getEnemySpawnPosition();

    @Override
    public float getCameraScaleFactor() { return ICWars.CAMERA_SCALE_FACTOR; }
}
