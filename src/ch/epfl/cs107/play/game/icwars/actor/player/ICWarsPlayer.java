package ch.epfl.cs107.play.game.icwars.actor.player;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.game.icwars.handler.ICWarsInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.List;

public abstract class ICWarsPlayer extends ICWarsActor implements Interactor {

    private List<Unit> units;
    protected Unit selectedUnit;

    protected PlayerState state;

    public ICWarsPlayer(Faction faction, Area owner, DiscreteCoordinates position, Unit... units) {
        super(faction, owner, position);
        this.units = List.of(units);
        registerUnits(owner, this.units);
        state = PlayerState.IDLE;
    }

    private void registerUnits(Area owner, List<Unit> units) {
        for (Unit unit : units) {
            owner.registerActor(unit);
        }
        ((ICWarsArea)owner).registerUnits(units);
    }

    private void unregisterUnits(Area owner, List<Unit> units, boolean becauseDestroyed) {
        for (Unit unit : units) {
            if (becauseDestroyed) {
                if (!unit.isDead()) { return; }
                units.remove(unit);
            }
            owner.unregisterActor(unit);
            ((ICWarsArea)owner).unregisterUnit(unit);
        }
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

        super.update(deltaTime);
    }

    public void startTurn() {
        state = PlayerState.NORMAL;
        centerCamera();
        setAllUnitAvailable(true);
        //les unités redeviennent dispo ?
    }

    public boolean finishedTurn() {
        if (!unitsAreAvailable()) {
            state = PlayerState.IDLE;
            return true;
        }
        return false;
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

    @Override
    public void onLeaving(List<DiscreteCoordinates> coordinates) {
        state = PlayerState.NORMAL;
    }

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

    @Override
    public boolean wantsCellInteraction() { return true; }

    @Override
    public boolean wantsViewInteraction() { return false; }

    public enum PlayerState {
        IDLE, NORMAL, SELECT_CELL, MOVE_UNIT, ACTION_SELECTION, ACTION;
    }

    // TODO Useful or make attribute protected ?
    protected void setState(PlayerState state) {
        this.state = state;
    }

    protected PlayerState getState() { return state; }

}
