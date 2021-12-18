package ch.epfl.cs107.play.game.icwars.actor.player;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.game.icwars.handler.ICWarsInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public abstract class ICWarsPlayer extends ICWarsActor implements Interactor {

    // List of Units the player has
    private final List<Unit> units;
    protected final List<Unit> unregisteredUnits;;
    protected Unit selectedUnit;

    protected PlayerState state;

    public ICWarsPlayer(Faction faction, Area owner, DiscreteCoordinates position, Unit... units) {
        super(faction, owner, position);
        this.units = new LinkedList<>(Arrays.asList(units));

        registerUnits(owner, this.units);
        unregisteredUnits = new LinkedList<>();

        setAllUnitAvailable(true);
        state = PlayerState.IDLE;
    }

    /**
     * Register all the player units : will be added at next update
     * @param owner the area in which the units are registered
     * @param units the list of units to be added
     */
    private void registerUnits(Area owner, List<Unit> units) {
        for (Unit unit : units) {
            owner.registerActor(unit);
            ((ICWarsArea)owner).registerUnit(unit);
        }
    }

    /**
     * Unregister a list of units : will be removed at next update
     * @param owner the area in which the units are unregistered
     * @param units the list of units to be removed
     * @param becauseDestroyed <code>true</code> when the units are removed because of taking damage
     */
    private void unregisterUnits(Area owner, List<Unit> units, boolean becauseDestroyed) {
        for (Unit unit : units) {
            if (becauseDestroyed) {
                if (!unit.isDead()) { continue; }
                unregisterUnit(unit);
            }
            owner.unregisterActor(unit);
            ((ICWarsArea)owner).unregisterUnit(unit);
        }
    }

    /** Unregister a unit : it is removed from the player list. */
    public void unregisterUnit(Unit unit) {
        unregisteredUnits.add(unit);
    }

    final void purgeRegistrationUnits() {
        // Unregister Units
        units.removeAll(unregisteredUnits);
        unregisteredUnits.clear();
    }

    /** Center the camera on the player. */
    public void centerCamera() {
        getOwnerArea().setViewCandidate(this);
    }

    /** Unregister this player when it leaves the area. */
    @Override
    public void leaveArea(){
        unregisterUnits(getOwnerArea(), units, false);
        getOwnerArea().unregisterActor(this);
    }

    /** Return <code>true</code> if the ICWarsPlayer has zero unit. */
    public boolean isDefeated() { return units.size() == 0; }

    @Override
    public void update(float deltaTime) {
        unregisterUnits(getOwnerArea(), units, true);
        purgeRegistrationUnits();

        super.update(deltaTime);
    }

    /** Initialize player turn. All his units are made available. */
    public void initTurn() {
        setAllUnitAvailable(true);
    }

    /** Start player turn. Make the camera centered and pass in NORMAL state. */
    public void startTurn() {
        state = PlayerState.NORMAL;
        centerCamera();
    }

    /** Returns <code>true</code> if the player has no more units available. */
    public boolean finishedTurn() {
        return !unitsAreAvailable();
    }

    /** Finish player turn. All units are made unavailable and pass in IDLE state. */
    public void finishTurn() {
        setAllUnitAvailable(false);
        state = PlayerState.IDLE;
    }

    /** When a unit finishes an action, the player pass to NORMAL state. */
    public void finishAction() {
        state = PlayerState.NORMAL;
    }

    /** When a unit interrupt an action, the player pass to ACTION_SELECTION state. */
    public void interruptAction() {
        centerCamera();
        state = PlayerState.ACTION_SELECTION;
    }

    /** Change all units availability. */
    private void setAllUnitAvailable(boolean available) {
        for (Unit unit : units) {
            unit.setAvailable(available);
        }
    }

    /** Returns <code>true</code> if all units are available, <code>false</code> otherwise. */
    private boolean unitsAreAvailable() {
        for (Unit unit : units) {
            if (unit.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a defensive copy of the list so that elements cannot be added or removed from it,
     * but it still contains the address to the Units
     */
    protected List<Unit> getPlayerUnits() {
        return new ArrayList<>(units);
    }

    /** Returns an Array containing the coordinates of the player's units. */
    public DiscreteCoordinates[] getUnitCoordinates(){
        DiscreteCoordinates[] enemyUnitPosition = new DiscreteCoordinates[units.size()];
        int unitIndex = 0;
        for(Unit unit : units){
            enemyUnitPosition[unitIndex] = new DiscreteCoordinates(unit.getCurrentMainCellCoordinates().x,
                    unit.getCurrentMainCellCoordinates().y);
            unitIndex += 1;
        }
        return enemyUnitPosition;
    }

    /** Describe the player state */
    public enum PlayerState {
        IDLE, NORMAL, SELECT_CELL, MOVE_UNIT, ACTION_SELECTION, ACTION;
    }

    /** Returns the player state */
    public PlayerState getState() { return state; }

    @Override
    public void onLeaving(List<DiscreteCoordinates> coordinates) {
        if (state == PlayerState.SELECT_CELL) {
            state = PlayerState.NORMAL;
        }
    }

    //----------------//
    // Interactable
    //----------------//
    @Override
    public boolean takeCellSpace() { return false; }

    @Override
    public boolean isCellInteractable() { return true; }

    @Override
    public boolean isViewInteractable() { return false; }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ICWarsInteractionVisitor)v).interactWith(this);
    }

    //----------------//
    // Interactor
    //----------------//
    @Override
    public boolean wantsCellInteraction() { return true; }

    @Override
    public boolean wantsViewInteraction() { return false; }

}
