package ch.epfl.cs107.play.game.icwars.actor.player;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaGraph;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.icwars.ICWars;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
import ch.epfl.cs107.play.game.icwars.area.ICWarsRange;
import ch.epfl.cs107.play.game.icwars.gui.ICWarsPlayerGUI;
import ch.epfl.cs107.play.game.icwars.handler.ICWarsInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.List;

public class RealPlayer extends ICWarsPlayer {

    private Sprite sprite;
    private String spriteName;
    private Canvas canvas;

    private final int MOVE_DURATION = 5;

    private Unit selectedUnit;
    private ICWarsPlayerGUI playerGUI;

    public RealPlayer(Faction faction, Area owner, DiscreteCoordinates position, Unit... units) {
        super(faction, owner, position, units);

        if (isAlly()) {
            spriteName = "icwars/allyCursor";
        } else {
            spriteName = "icwars/enemyCursor";
        }

        sprite = new Sprite(spriteName, 1.f, 1.f,this);

        playerGUI = new ICWarsPlayerGUI(ICWars.CAMERA_SCALE_FACTOR, this);

        startTurn();
    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
        playerGUI.draw(canvas);
    }

    @Override
    public void update(float deltaTime) {
        Keyboard keyboard= getOwnerArea().getKeyboard();

        updateStates();

        if(state == PlayerState.MOVE_UNIT || state == PlayerState.NORMAL || state == PlayerState.SELECT_CELL) {
            moveIfPressed(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
            moveIfPressed(Orientation.UP, keyboard.get(Keyboard.UP));
            moveIfPressed(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
            moveIfPressed(Orientation.DOWN, keyboard.get(Keyboard.DOWN));
        }
        super.update(deltaTime);
    }

    private void updateStates(){
        Keyboard keyboard= getOwnerArea().getKeyboard();
        switch(state) {
            case IDLE:
                break;
            case NORMAL:
                if(keyboard.get(Keyboard.ENTER).isDown()) {
                    state = PlayerState.SELECT_CELL;
                }
                if(keyboard.get(Keyboard.TAB).isDown()) {
                    state = PlayerState.IDLE;
                }
                break;
            case SELECT_CELL:
                if(selectedUnit != null) {
                    state = PlayerState.MOVE_UNIT;
                }
                break;
            case MOVE_UNIT:
                if(keyboard.get(Keyboard.ENTER).isDown()) {
                    selectedUnit.changePosition(new DiscreteCoordinates((int) getPosition().x,(int)getPosition().y));
                    state = PlayerState.NORMAL;
                }
            case ACTION_SELECTION:
            case ACTION:
        }
    }

    /**
     * Orientate and Move this player in the given orientation if the given button is down
     * @param orientation (Orientation): given orientation, not null
     * @param key (Button): button key corresponding to the given orientation, not null
     */
    private void moveIfPressed(Orientation orientation, Button key){
        if(key.isDown()) {
            if (!isDisplacementOccurs()) {
                orientate(orientation);
                move(MOVE_DURATION);
            }
        }
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return null;
    }

    @Override
    public void interactWith(Interactable other) {

    }


    private class ICWarsPlayerInteractionHandler implements ICWarsInteractionVisitor {
        @Override
        public void interactWith(Unit unit){
            if((state == PlayerState.SELECT_CELL) && (unit.isAlly())){
                selectedUnit = unit;
                unit.drawRangeAndPathTo(getCurrentMainCellCoordinates(), canvas);
            }
        }
    }
}
