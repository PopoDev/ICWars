package ch.epfl.cs107.play.game.icwars.actor.unit;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

// TODO Should be an inner class of ICWarsPlayer ?
public abstract class Unit extends ICWarsActor {

    private String name;
    private int hp;
    private final int HP_MAX;
    private final int MOVE_RADIUS;

    public Unit(ICWarsActor.Faction faction, Area owner, DiscreteCoordinates position, int hpMax, int moveRadius) {
        super(faction, owner, position);

        this.HP_MAX = hpMax;
        this.hp = hpMax;
        this.MOVE_RADIUS = moveRadius;
    }

    private String getName() { return name; }
    protected void setName(String name) { this.name = name; }

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

    private boolean canMove(int radius) { return radius <= MOVE_RADIUS; }

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
