package ch.epfl.cs107.play.game.icwars.actor.unit.action;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.icwars.actor.player.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

public class Wait extends Action {

    public Wait(Unit unit, Area area) {
        super(unit, area, "(W)ait", Keyboard.W);
    }

    @Override
    public void doAction(float dt, ICWarsPlayer player, Keyboard keyboard) {
        linkedUnit.setAvailable(false);
        player.finishAction();
    }

    @Override
    public void draw(Canvas canvas) {}
}
