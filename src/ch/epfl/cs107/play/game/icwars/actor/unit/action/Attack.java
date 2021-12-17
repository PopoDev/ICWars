package ch.epfl.cs107.play.game.icwars.actor.unit.action;

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.icwars.actor.player.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.unit.Rocket;
import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

public class Attack extends Action {

    private final ImageGraphics cursor;
    private Unit attackedUnit;
    private int listIndex = 0;

    public Attack(Unit unit, Area area) {
        super(unit, area, "(A)ttack", Keyboard.A);

        cursor = new ImageGraphics(ResourcePath.getSprite("icwars/UIpackSheet"), 1f, 1f,
                 new RegionOfInterest(4*18 , 26*18 ,16 ,16));
    }

    @Override
    public void doAction(float dt, ICWarsPlayer player, Keyboard keyboard) {
        int[] actionValues = ((ICWarsArea)area).attackSelection(linkedUnit, listIndex);
        int attackedIndex = actionValues[0];
        listIndex = actionValues[1];

        // The list of enemy units is empty or the key TAB is pressed
        if (attackedIndex < 0 || keyboard.get(Keyboard.TAB).isPressed()) {
            player.interruptAttackAction();
            return;
        }
        attackedUnit = ((ICWarsArea)area).getUnitFromIndex(attackedIndex);

        if (keyboard.get(Keyboard.ENTER).isPressed()) {
            linkedUnit.attack(attackedUnit);

            linkedUnit.setAvailable(false);
            player.centerCamera();
            player.finishAction();
        }
    }

    public boolean doAutoAction(ICWarsPlayer player){
        attackedUnit = ((ICWarsArea)area).autoAttackSelection(linkedUnit);
        if(attackedUnit == null) { return false; }
        linkedUnit.setAvailable(false);
        player.centerCamera();
        player.finishAction();
        linkedUnit.attack(attackedUnit);
        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        if (attackedUnit == null) { return; }

        attackedUnit.centerCamera();
        cursor.setAnchor(canvas.getPosition().add(1,0));
        cursor.draw(canvas);
    }
}
