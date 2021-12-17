package ch.epfl.cs107.play.game.icwars.actor.player;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.game.icwars.handler.ICWarsInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public abstract class ICWarsPlayer extends ICWarsActor implements Interactor {

    // List of Units the player has
    protected final List<Unit> units;
    protected final List<Unit> unregisteredUnits;;
    protected Unit selectedUnit;

    protected PlayerState state;

    public ICWarsPlayer(Faction faction, Area owner, DiscreteCoordinates position, Unit... units) {
        super(faction, owner, position);
        this.units = new LinkedList<>(Arrays.asList(units));

        registerUnits(owner, this.units);
        unregisteredUnits = new LinkedList<>();

        state = PlayerState.IDLE;
    }

    private void registerUnits(Area owner, List<Unit> units) {
        for (Unit unit : units) {
            owner.registerActor(unit);
            ((ICWarsArea)owner).registerUnit(unit);
        }
    }

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

    /** Unregister a unit : it is removed from the player list */
    public void unregisterUnit(Unit unit) {
        unregisteredUnits.add(unit);
    }

    final void purgeRegistrationUnits() {
        // Unregister Units
        units.removeAll(unregisteredUnits);
        unregisteredUnits.clear();
    }

    /** Center the camera on the player */
    public void centerCamera() {
        getOwnerArea().setViewCandidate(this);
    }

    /** Unregister this player when it leaves the area */
    @Override
    public void leaveArea(){
        unregisterUnits(getOwnerArea(), units, false);
        getOwnerArea().unregisterActor(this);
    }

    /** Return <code>true</code> if the ICWarsPlayer has zero unit */
    public boolean isDefeated() { return units.size() == 0; }

    @Override
    public void update(float deltaTime) {
        unregisterUnits(getOwnerArea(), units, true);
        purgeRegistrationUnits();

        super.update(deltaTime);
    }

    public void startTurn() {
        state = PlayerState.NORMAL;
        centerCamera();
        setAllUnitAvailable(true);
    }

    public boolean finishedTurn() {
        return !unitsAreAvailable();
    }

    public void finishTurn() {
        state = PlayerState.IDLE;
    }

    public void finishAction() {
        state = PlayerState.NORMAL;
    }

    public void interruptAttackAction() {
        centerCamera();
        state = PlayerState.ACTION_SELECTION;
    }

    public void setAllUnitAvailable(boolean available) {
        for (Unit unit : units) {
            unit.setAvailable(available);
        }
    }

    private boolean unitsAreAvailable() {
        for (Unit unit : units) {
            if (unit.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    public DiscreteCoordinates[] getUnitCoordinates(){
        DiscreteCoordinates[] EnemyUnitPosition = new DiscreteCoordinates[units.size()];
        int unitIndex = 0;
        for(Unit unit : units){
            EnemyUnitPosition[unitIndex] = new DiscreteCoordinates(unit.getCurrentMainCellCoordinates().x,
                    unit.getCurrentMainCellCoordinates().y);
            unitIndex += 1;
        }
        return EnemyUnitPosition;
    }

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

    public enum PlayerState {
        IDLE, NORMAL, SELECT_CELL, MOVE_UNIT, ACTION_SELECTION, ACTION;
    }

    public PlayerState getState() { return state; }

}
