package ch.epfl.cs107.play.game.icwars.actor.unit.action;

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.icwars.actor.player.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.player.RealPlayer;
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
        setPrice(50);

        cursor = new ImageGraphics(ResourcePath.getSprite("icwars/UIpackSheet"), 1f, 1f,
                 new RegionOfInterest(4*18 , 26*18 ,16 ,16));
    }

    private void attackAction(ICWarsPlayer player) {
        linkedUnit.attack(attackedUnit);
        linkedUnit.setAvailable(false);
        player.centerCamera();
        player.finishAction();
    }

    @Override
    public void doAction(float dt, ICWarsPlayer player, Keyboard keyboard) {
        super.doAction(dt, player, keyboard);

        int[] actionValues = ((ICWarsArea)area).attackSelection(linkedUnit, listIndex);
        int attackedIndex = actionValues[0];
        listIndex = actionValues[1];

        // The list of enemy units is empty or the key TAB is pressed
        if (attackedIndex < 0 || keyboard.get(Keyboard.TAB).isPressed()) {
            player.interruptAction();
            return;
        }
        attackedUnit = ((ICWarsArea)area).getUnitFromIndex(attackedIndex);

        if (keyboard.get(Keyboard.ENTER).isPressed()) {
            attackAction(player);
        }
    }

    /**
     * Resolve automatically the action of the AI player;
     * @param player AI player;
     * @return true if there's a unit to attack, otherwise false;
     */
    public boolean doAutoAction(ICWarsPlayer player){
        attackedUnit = ((ICWarsArea)area).autoAttackSelection(linkedUnit);
        //move  the AIPlayer to the attacked unit (to make it clearer to the player);
        if(attackedUnit != null){
            player.setCurrentPosition(attackedUnit.getCurrentMainCellCoordinates().toVector());
        }
        if(attackedUnit == null) { return false; }

        attackAction(player);
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
