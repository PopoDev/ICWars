package ch.epfl.cs107.play.game.icwars.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.Collections;
import java.util.List;

public abstract class ICWarsActor extends MovableAreaEntity {

    private final Faction faction;

    public ICWarsActor(Faction faction, Area owner, DiscreteCoordinates position) {
        super(owner, Orientation.UP, position);
        this.faction = faction;
    }

    /**
     * Enter an area. The actor is registered and his position is initialized
     * @param area (Area): initial area, not null
     * @param position (DiscreteCoordinates): initial position, not null
     */
    public void enterArea(Area area, DiscreteCoordinates position){
        area.registerActor(this);
        setOwnerArea(area);
        setCurrentPosition(position.toVector());
    }

    /** Unregister this player when it leaves the area */
    public void leaveArea(){
        getOwnerArea().unregisterActor(this);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    /** Returns <code>true</code> if the actor belongs to ALLY Faction. */
    public boolean isAlly() { return faction == Faction.ALLY; }

    /**
     * Returns <code>true</code> if the two actors are in the same Faction.
     * @param me the first actor
     * @param other the second actor
     */
    public boolean areInSameFaction(ICWarsActor me, ICWarsActor other) {
        return me.faction == other.faction;
    }

    /** An ICWarsActor can belong either to an ally or enemy faction */
    public enum Faction {
        ALLY, ENEMY
    }
}
