package ch.epfl.cs107.play.game.icwars.actor.player;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.List;

public abstract class ICWarsPlayer extends ICWarsActor {

    protected List<Unit> units;

    public ICWarsPlayer(Faction faction, Area owner, DiscreteCoordinates position, Unit... units) {
        super(faction, owner, position);
        this.units = List.of(units);
        registerUnits(owner, this.units);
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

    @Override
    public boolean takeCellSpace() { return false; }

    @Override
    public boolean isCellInteractable() { return true; }

    @Override
    public boolean isViewInteractable() { return false; }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {

    }
}
