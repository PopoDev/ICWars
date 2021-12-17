package ch.epfl.cs107.play.game.icwars.actor.unit.action;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.icwars.actor.player.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

//----------------//
// Extension
//----------------//
public class Heal extends Action {

    public Heal(Unit unit, Area area) {
        super(unit, area, "(H)eal", Keyboard.H);
    }

    private void healUnitsAround() {
        ((ICWarsArea)area).getUnitsAround();
    }

    @Override
    public void doAction(float dt, ICWarsPlayer player, Keyboard keyboard) {

    }

    public boolean doAutoAction(ICWarsPlayer player) {
        linkedUnit.setAvailable(false);
        player.finishAction();
        return true;
    }

    @Override
    public void draw(Canvas canvas) {

    }
}
