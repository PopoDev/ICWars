package ch.epfl.cs107.play.game.icwars.actor.unit;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

// TODO Should be an inner class of ICWarsPlayer ?
public abstract class Unit extends ICWarsActor {

    private String name;
    private int hp;

    public Unit(ICWarsActor.Faction faction, Area owner, DiscreteCoordinates position) {
        super(faction, owner, position);
    }

    private String getName() { return name; }
    protected void setName(String name) { this.name = name; }

    private int getHp() { return hp; }
    protected void setHp(int hp) { this.hp = hp; }

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
        if (hp > getHpMax()) { hp = getHpMax(); }
    }

    /**
     * The unit attack an enemy making him loose health
     * @param other the attacked unit
     */
    private void attack(Unit other) { other.takeDamage(this.getDamage()); }

    //-------------------------//
    // Specific to a Unit type
    //-------------------------//
    // TODO Is this a good conception ? Use getters or override methods in subclass or can we use constructor?
    protected abstract int getHpMax();
    protected abstract int getDamage();
    protected abstract int getMoveRadius();

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
