package ch.epfl.cs107.play.game.icwars.actor.unit.action;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.icwars.actor.player.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

public class Mend extends Action {

    private final int PRICE_FACTOR = 10;

    public Mend(Unit unit, Area area) {
        super(unit, area, "(M)end", Keyboard.M);
    }

    private void setRepairPrice() {
        int repairPrice = linkedUnit.getLostHp() * PRICE_FACTOR;
        setPrice(repairPrice);
    }

    @Override
    public void doAction(float dt, ICWarsPlayer player, Keyboard keyboard) {
        setRepairPrice();
        super.doAction(dt, player, keyboard);

        linkedUnit.repair(linkedUnit.getLostHp());

        linkedUnit.setAvailable(false);
        player.finishAction();
    }

    @Override
    public boolean doAutoAction(ICWarsPlayer player) { return false; }

    @Override
    public void draw(Canvas canvas) {}
}
