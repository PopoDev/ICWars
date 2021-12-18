package ch.epfl.cs107.play.game.icwars.actor.unit.action;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.icwars.actor.player.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.unit.Medic;
import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

//----------------//
// Extension
//----------------//
public class Heal extends Action {

    private final int healAmount;

    public Heal(Unit unit, Area area) {
        super(unit, area, "(H)eal", Keyboard.H);
        setPrice(40);

        healAmount = unit.getDamage();  // Damage for a Medic is the amount of heal he gives
    }

    private void healUnitsAround() {
        for (int index : ((ICWarsArea)area).getUnitsToHeal((Medic) linkedUnit)) {
            Unit healedUnit = ((ICWarsArea)area).getUnitFromIndex(index);
            healedUnit.repair(healAmount);
        }
    }

    @Override
    public void doAction(float dt, ICWarsPlayer player, Keyboard keyboard) {
        super.doAction(dt, player, keyboard);
        healUnitsAround();
        linkedUnit.setAvailable(false);
        player.finishAction();
    }

    @Override
    public boolean doAutoAction(ICWarsPlayer player) { return false; }

    @Override
    public void draw(Canvas canvas) {}
}
