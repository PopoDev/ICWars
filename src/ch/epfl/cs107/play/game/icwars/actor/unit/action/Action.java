package ch.epfl.cs107.play.game.icwars.actor.unit.action;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.icwars.actor.player.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.player.RealPlayer;
import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
import ch.epfl.cs107.play.window.Keyboard;

public abstract class Action implements Graphics {

    protected final Unit linkedUnit;
    protected final Area area;

    private final String name;
    private final int key;

    private int price;

    public Action(Unit unit, Area area, String name, int key) {
        this.linkedUnit = unit;
        this.area = area;

        this.name = name;
        this.key = key;
    }

    public void doAction(float dt, ICWarsPlayer player, Keyboard keyboard) {
        if (!hasEnoughMoney((RealPlayer)player)) {
            player.interruptAction();
        }
    }

    public abstract boolean doAutoAction(ICWarsPlayer player);

    public int getKey() { return key; }

    public String getName() { return name; }

    protected void setPrice(int price) { this.price = price; }

    public int getPrice() { return price; }

    protected boolean hasEnoughMoney(RealPlayer player) {
        if (player.getMoney() >= price) {
            return true;
        } else {
            System.out.println("Not enough money to do this action! " +
                    "Balance : " + player.getMoney() + " | Price : " + price);
            return false;
        }
    }
}