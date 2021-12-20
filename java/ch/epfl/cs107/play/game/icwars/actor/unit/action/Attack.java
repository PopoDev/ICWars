package ch.epfl.cs107.play.game.icwars.actor.unit.action;

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.icwars.actor.player.ICWarsPlayer;
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

    public void setAttackedUnit(Unit attackedUnit) {
        this.attackedUnit = attackedUnit;
    }

    @Override
    public void doAction(float dt, ICWarsPlayer player, Keyboard keyboard) {
        super.doAction(dt, player, keyboard);

        int attackedIndex = ((ICWarsArea)area).attackSelection(linkedUnit, listIndex, this);

        // The list of enemy units is empty or the key TAB is pressed
        if (attackedIndex < 0 || keyboard.get(Keyboard.TAB).isPressed()) {
            player.interruptAction();
            return;
        } else {
            listIndex = attackedIndex;
        }

        if (keyboard.get(Keyboard.ENTER).isPressed()) {
            linkedUnit.attack(attackedUnit);
            linkedUnit.setAvailable(false);
            attackedUnit = null;
            player.centerCamera();
            player.finishAction();
        }
    }

    /**
     * Resolve automatically the action of the AI player;
     * @param player AI player;
     * @return true if there's a unit to attack, otherwise false;
     */
    @Override
    public boolean doAutoAction(ICWarsPlayer player){
        if(! ((ICWarsArea)area).autoAttackSelection(linkedUnit, this) ) { return false; }  // No target found

        linkedUnit.attack(attackedUnit);
        // Don't finish directly to let the cursor some time to draw
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
