package ch.epfl.cs107.play.game.icwars.actor.unit;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.ICWars;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.graphics.HealthBarGraphics;
import ch.epfl.cs107.play.game.icwars.actor.graphics.HealthLostBarGraphics;
import ch.epfl.cs107.play.game.icwars.actor.unit.action.Action;
import ch.epfl.cs107.play.game.icwars.area.ICWarsBehavior;
import ch.epfl.cs107.play.game.icwars.area.ICWarsRange;
import ch.epfl.cs107.play.game.icwars.handler.ICWarsInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class Unit extends ICWarsActor implements Interactor {

    private final Sprite sprite;
    private String name;
    private int hp;
    private final int HP_MAX;
    private final int MOVE_RADIUS;
    private boolean available;

    private final UnitInteractionHandler handler;
    private final List<Action> actions;
    private int defenseStars;

    private ICWarsRange range;

    public Unit(ICWarsActor.Faction faction, Area owner, DiscreteCoordinates position,
                int hpMax, int moveRadius, String[] spriteNames) {
        super(faction, owner, position);

        this.HP_MAX = hpMax;
        this.hp = hpMax;
        this.MOVE_RADIUS = moveRadius;

        String spriteName = spriteNames[isAlly() ? 0 : 1];
        sprite = new Sprite(spriteName, 1.5f, 1.5f, this, null, new Vector(-0.25f, -0.25f));

        range = setRange(position);

        actions = new LinkedList<>();
        handler = new UnitInteractionHandler();

        // Extension
        hpBar = new HealthBarGraphics(barWidth, barHeight, sprite);
        hpBar.setParent(this);
        hpLostBar = new HealthLostBarGraphics(barWidth, 0, barHeight, sprite);
        hpLostBar.setParent(this);
    }

    public String getName() { return name; }

    public Unit setName(String name) {
        this.name = name;
        return this;
    }

    public int getHp() { return hp; }

    public int getLostHp() { return HP_MAX - hp; }

    public boolean isDead() { return hp <= 0; }

    @Override
    public String toString() {
        return " {" + "name='" + name + '\'' + ", hp=" + hp + "}";
    }

    /** Center the camera on the unit */
    public void centerCamera() {
        getOwnerArea().setViewCandidate(this);
    }

    /**
     * The unit losoe an amount of health
     * @param amount amount of damage the unit take
     */
    private void takeDamage(int amount) {
        // Damage can't be negative --> Unit would gain health
        int damage = (amount < defenseStars) ? 0 : amount - defenseStars;
        hp -= damage;
        if (hp < 0) { hp = 0; }

        changeHpBar();
    }

    /**
     * The unit gain an amount of health
     * @param amount amount of health the unit gain
     */
    public void repair(int amount) {
        hp += amount;
        if (hp > HP_MAX) { hp = HP_MAX; }

        changeHpBar();
    }

    /**
     * The unit attack an enemy making him loose health
     * @param other the attacked unit
     */
    public void attack(Unit other) { other.takeDamage(this.getDamage()); }

    public void attack(Unit other, int damage) { other.takeDamage(damage); }

    public boolean canAttack(Unit attacked) {
        return canReach(attacked) && !areInSameFaction(this, attacked);
    }

    public boolean canReach(Unit unit) {
        return range.nodeExists(unit.getCurrentMainCellCoordinates());
    }

    public boolean isNextTo(Unit centerUnit) {
        return Math.abs(getCurrentMainCellCoordinates().x - centerUnit.getCurrentMainCellCoordinates().x) <= 1 &&
               Math.abs(getCurrentMainCellCoordinates().y - centerUnit.getCurrentMainCellCoordinates().y) <= 1 &&
               this != centerUnit;
    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
        hpBar.draw(canvas);
        if (hp < HP_MAX) { hpLostBar.draw(canvas); }
    }

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

    @Override
    public boolean changePosition(DiscreteCoordinates newPosition) {
        if(range.nodeExists(newPosition) && super.changePosition(newPosition)) {
            range = setRange(newPosition);
            return true;
        }
        return false;
    }

    private ICWarsRange setRange(DiscreteCoordinates position) {
        int fromX = position.x;
        int fromY = position.y;

        ICWarsRange range = new ICWarsRange();

        for (int x = -MOVE_RADIUS; x <= MOVE_RADIUS; ++x) {
            // Out of the scope of the Area in the X axis
            if ((x+fromX) < 0 || (x+fromX) > getOwnerArea().getWidth() - 1) { continue; }

            for (int y = -MOVE_RADIUS; y <= MOVE_RADIUS; ++y) {
                // Out of the scope of the Area in the Y axis
                if ((y+fromY) < 0 || (y+fromY) > getOwnerArea().getHeight() - 1) { continue; }

                boolean hasLeftEdge = x > -MOVE_RADIUS && (x+fromX) > 0;
                boolean hasUpEdge = y < MOVE_RADIUS && (y+fromY) < getOwnerArea().getHeight() - 1;
                boolean hasRightEdge = x < MOVE_RADIUS && (x+fromX) < getOwnerArea().getWidth() - 1;
                boolean hasDownEdge = y > -MOVE_RADIUS && (y+fromY) > 0;
                range.addNode(new DiscreteCoordinates(x + fromX, y + fromY),
                        hasLeftEdge, hasUpEdge, hasRightEdge, hasDownEdge);
            }
        }
        return range;
    }

    public int getRange() {
        return MOVE_RADIUS;
    }

    public void setAvailable(boolean available) {
        this.available = available;
        if (available) {
            sprite.setAlpha(1.f);
        } else {
            sprite.setAlpha(0.5f);
        }
    }

    public boolean isAvailable() { return available; }

    //-------------------------//
    // Specific to a Unit type
    //-------------------------//
    public abstract int getDamage();

    protected void initActions(Action... actions) {
        // Order of actions is important for doAutoAction
        this.actions.addAll(List.of(actions));  // Immutable List
    }

    public List<Action> getActions() {
        // Defensive copy to prevent element to be added or removed. An Action is immutable
        return new ArrayList<>(actions);
    }

    public Action browseActions() {
        Keyboard keyboard = getOwnerArea().getKeyboard();
        for (Action action : actions) {
            if (keyboard.get(action.getKey()).isPressed()) {
                return action;
            }
        }
        return null;
    }

    //----------------//
    // Interactable
    //----------------//
    @Override
    public boolean takeCellSpace() { return true; }

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
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return null;
    }

    @Override
    public boolean wantsCellInteraction() { return true; }

    @Override
    public boolean wantsViewInteraction() { return false; }

    @Override
    public void interactWith(Interactable other) {
        other.acceptInteraction(handler);
    }

    private class UnitInteractionHandler implements ICWarsInteractionVisitor {
        @Override
        public void interactWith(ICWarsBehavior.ICWarsCell cell) {
            defenseStars = cell.getType().getDefenseStar();
        }
    }

    //----------------//
    // Extension
    //----------------//

    private final HealthBarGraphics hpBar;
    private final HealthLostBarGraphics hpLostBar;
    private final float barWidth = ICWars.CAMERA_SCALE_FACTOR / 12;
    private final float barHeight = ICWars.CAMERA_SCALE_FACTOR / 60;

    private void changeHpBar() {
        float width = barWidth * ((float)hp / (float)HP_MAX);
        hpBar.setBarWidth(width);
        hpLostBar.setBarWidth(width);
    }
}
