package ch.epfl.cs107.play.game.icwars.actor.unit;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Path;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.area.ICWarsRange;
import ch.epfl.cs107.play.game.icwars.handler.ICWarsInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Queue;

// TODO Should be an inner class of ICWarsPlayer ?
public abstract class Unit extends ICWarsActor {

    private String name;
    private int hp;
    private final int HP_MAX;
    private final int MOVE_RADIUS;
    private boolean available;

    private ICWarsRange range;

    public Unit(ICWarsActor.Faction faction, Area owner, DiscreteCoordinates position, int hpMax, int moveRadius) {
        super(faction, owner, position);

        this.HP_MAX = hpMax;
        this.hp = hpMax;
        this.MOVE_RADIUS = moveRadius;

        int fromX = position.x;
        int fromY = position.y;
        range = new ICWarsRange();
        for (int x = -MOVE_RADIUS; x <= MOVE_RADIUS; ++x) {
            // Out of the scope of the Area in the X axis
            if ((x+fromX) < 0 || (x+fromX) > getOwnerArea().getWidth()) { continue; }

            for (int y = -MOVE_RADIUS; y <= MOVE_RADIUS; ++y) {
                // Out of the scope of the Area in the Y axis
                if ((y+fromY) < 0 || (y+fromY) > getOwnerArea().getHeight()) { continue; }

                boolean hasLeftEdge = x > -MOVE_RADIUS && (x+fromX) > 0;
                boolean hasUpEdge = y < MOVE_RADIUS && (y+fromY) < getOwnerArea().getHeight();
                boolean hasRightEdge = x < MOVE_RADIUS && (x+fromX) < getOwnerArea().getWidth();
                boolean hasDownEdge = y > -MOVE_RADIUS && (y+fromY) > 0;
                range.addNode(new DiscreteCoordinates(x + fromX, y + fromY),
                        hasLeftEdge, hasUpEdge, hasRightEdge, hasDownEdge);
            }
        }
    }

    private String getName() { return name; }
    public Unit setName(String name) {
        this.name = name;
        return this;
    }

    private int getHp() { return hp; }

    public boolean isDead() { return hp <= 0; }

    /**
     * The unit losoe an amount of health
     * @param amount amount of damage the unit take
     */
    private void takeDamage(int amount) {
        hp -= amount;
        if (hp < 0) { hp = 0; }
    }

    /**
     * The unit gain an amount of health
     * @param amount amount of health the unit gain
     */
    private void repair(int amount) {
        hp += amount;
        if (hp > HP_MAX) { hp = HP_MAX; }
    }

    /**
     * The unit attack an enemy making him loose health
     * @param other the attacked unit
     */
    private void attack(Unit other) { other.takeDamage(this.getDamage()); }

    /**
     * Draw the unit's range and a path from the unit position to destination
     * @param destination path destination
     * @param canvas canvas
     */
    public void drawRangeAndPathTo(DiscreteCoordinates destination, Canvas canvas) {
        range.draw(canvas);
        Queue<Orientation> path = range.shortestPath(getCurrentMainCellCoordinates(), destination);
        //Draw path only if it exists (destination inside the range)
        if (path != null) {
            new Path(getCurrentMainCellCoordinates().toVector(), path).draw(canvas);
        }
    }

    private boolean canMove(int radius) { return radius <= MOVE_RADIUS; }

    @Override
    public boolean changePosition(DiscreteCoordinates newPosition){
        if(range.nodeExists(newPosition)) {
            return super.changePosition(newPosition);
        }
        return false;
    }

    public void setAvailable(boolean available) { this.available = available; }

    public boolean isAvailable() { return available; }

    //-------------------------//
    // Specific to a Unit type
    //-------------------------//
    protected abstract int getDamage();

    //----------------//
    // Interactable
    //----------------//
    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }
}
