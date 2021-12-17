package ch.epfl.cs107.play.game.icwars.actor.unit.action;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.icwars.actor.player.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
import ch.epfl.cs107.play.window.Keyboard;

public abstract class Action implements Graphics {

    protected final Unit linkedUnit;
    protected final Area area;

    private final String name;
    private final int key;

    public Action(Unit unit, Area area, String name, int key) {
        this.linkedUnit = unit;
        this.area = area;

        this.name = name;
        this.key = key;
    }

    public abstract void doAction(float dt, ICWarsPlayer player, Keyboard keyboard);

    public int getKey() { return key; }

    public String getName() { return name; }
}