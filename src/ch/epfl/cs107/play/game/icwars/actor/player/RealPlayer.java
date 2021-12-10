package ch.epfl.cs107.play.game.icwars.actor.player;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.icwars.ICWars;
import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
import ch.epfl.cs107.play.game.icwars.gui.ICWarsPlayerGUI;
import ch.epfl.cs107.play.game.icwars.handler.ICWarsInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.List;

public class RealPlayer extends ICWarsPlayer {

    private final Sprite sprite;

    private final int MOVE_DURATION = 5;

    private ICWarsPlayerGUI playerGUI;

    private final ICWarsPlayerInteractionHandler handler;

    public RealPlayer(Faction faction, Area owner, DiscreteCoordinates position, Unit... units) {
        super(faction, owner, position, units);

        String spriteName;
        if (isAlly()) {
            spriteName = "icwars/allyCursor";
        } else {
            spriteName = "icwars/enemyCursor";
        }

        sprite = new Sprite(spriteName, 1.f, 1.f,this);

        playerGUI = new ICWarsPlayerGUI(ICWars.CAMERA_SCALE_FACTOR, this);
        handler = new ICWarsPlayerInteractionHandler();
    }

    @Override
    public void draw(Canvas canvas) {
        if (state != PlayerState.IDLE) {
            sprite.draw(canvas);
        }
        if (state == PlayerState.MOVE_UNIT) {
            playerGUI.setDestination(getCurrentMainCellCoordinates());
            playerGUI.draw(canvas);
        }
    }

    @Override
    public void update(float deltaTime) {
        Keyboard keyboard = getOwnerArea().getKeyboard();

        updateStates();

        if (state == PlayerState.MOVE_UNIT || state == PlayerState.NORMAL || state == PlayerState.SELECT_CELL) {
            moveIfPressed(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
            moveIfPressed(Orientation.UP, keyboard.get(Keyboard.UP));
            moveIfPressed(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
            moveIfPressed(Orientation.DOWN, keyboard.get(Keyboard.DOWN));
        }
        super.update(deltaTime);
    }

    private void updateStates(){
        Keyboard keyboard = getOwnerArea().getKeyboard();
        switch(state) {
            case IDLE:
                break;
            case NORMAL:
                if (keyboard.get(Keyboard.ENTER).isPressed()) {
                    state = PlayerState.SELECT_CELL;
                }
                if (keyboard.get(Keyboard.TAB).isPressed()) {
                    state = PlayerState.IDLE;
                }
                break;
            case SELECT_CELL:
                if (selectedUnit != null) {
                    state = PlayerState.MOVE_UNIT;
                }
                break;
            case MOVE_UNIT:
                if (keyboard.get(Keyboard.ENTER).isPressed()) {
                    moveUnit();
                    state = PlayerState.NORMAL;
                }
                break;
            case ACTION_SELECTION:
            case ACTION:
        }
    }

    private void moveUnit() {
        if (selectedUnit.changePosition(getCurrentMainCellCoordinates())) {
            selectedUnit.setAvailable(false);  // No longer usable
            selectedUnit = null;               // Reset selectedUnit
            playerGUI.setSelectedUnit(null);
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

    /**
     * Ask other if it accepts interaction with RealPlayer
     * when the state is SELECT_CELL
     * @param other (Interactable). Not null
     */
    @Override
    public void interactWith(Interactable other) {
        if (state == PlayerState.SELECT_CELL) { other.acceptInteraction(handler); }
    }

    private class ICWarsPlayerInteractionHandler implements ICWarsInteractionVisitor {
        @Override
        public void interactWith(Unit unit) {
            if((state == PlayerState.SELECT_CELL) && (areInSameFaction(RealPlayer.this, unit))) {
                if (!unit.isAvailable()) {
                    state = PlayerState.NORMAL;
                    return;
                }
                selectedUnit = unit;
                playerGUI.setSelectedUnit(selectedUnit);
            }
        }
    }

}
