package ch.epfl.cs107.play.game.icwars.actor;

import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public abstract class ICWarsActor extends MovableAreaEntity {

    private Faction faction;

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

    protected boolean isAlly() {
        return faction == Faction.ALLY;
    }

    /** An ICWarsActor can belong either to an ally or enemy faction */
    public enum Faction {
        ALLY, ENEMY
    }

    public enum States{
        IDLE, NORMAL, SELECT_CELL, MOVE_UNIT, ACTION_SELECTION, ACTION
    }
}
