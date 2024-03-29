package ch.epfl.cs107.play.game.icwars.actor.player;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.icwars.ICWars;
import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
import ch.epfl.cs107.play.game.icwars.actor.unit.action.Action;
import ch.epfl.cs107.play.game.icwars.area.ICWarsBehavior;
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

    private final ICWarsPlayerGUI playerGUI;
    private Action actionToDo;

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

        money = 0;
        playerGUI.getGamePanel().setMoney(money);
        playerGUI.getGamePanel().setCurrentLevelName(owner.toString());
    }

    @Override
    public void draw(Canvas canvas) {
        if (state != PlayerState.IDLE) {
            sprite.draw(canvas);
            playerGUI.draw(canvas);
        }

        if (state == PlayerState.ACTION) {
            actionToDo.draw(canvas);
        }
    }

    @Override
    public void update(float deltaTime) {
        Keyboard keyboard = getOwnerArea().getKeyboard();

        updateStates(deltaTime);

        if (state == PlayerState.MOVE_UNIT || state == PlayerState.NORMAL || state == PlayerState.SELECT_CELL) {
            moveIfPressed(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
            moveIfPressed(Orientation.UP, keyboard.get(Keyboard.UP));
            moveIfPressed(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
            moveIfPressed(Orientation.DOWN, keyboard.get(Keyboard.DOWN));
        }

        if (state == PlayerState.MOVE_UNIT) {
            playerGUI.setDestination(getCurrentMainCellCoordinates());
        }
        if (state == PlayerState.ACTION_SELECTION) {
            playerGUI.getActionsPanel().setActions(selectedUnit.getActions());
        }

        super.update(deltaTime);
    }

    private void updateStates(float deltaTime) {
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
                    if (moveUnit()) {
                        state = PlayerState.ACTION_SELECTION;
                    } else {
                        state = PlayerState.NORMAL;
                    }
                }
                break;
            case ACTION_SELECTION:
                actionToDo = selectedUnit.browseActions();
                if (actionToDo != null) {
                    state = PlayerState.ACTION;
                }
                break;
            case ACTION:
                actionToDo.doAction(deltaTime, this, keyboard);
                break;
        }
    }

    /** Move the unit to the player current position */
    private boolean moveUnit() {
        return selectedUnit.changePosition(getCurrentMainCellCoordinates());
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
    public void finishAction() {
        super.finishAction();
        changeMoney(-actionToDo.getPrice());
        selectedUnit.setAvailable(false);  // No longer usable
        selectedUnit = null;  // Reset selectedUnit
        playerGUI.setSelectedUnit(null);
    }

    @Override
    public void onLeaving(List<DiscreteCoordinates> coordinates) {
        super.onLeaving(coordinates);
        playerGUI.getInfoPanel().setUnit(null);
    }

    //----------------//
    // Interactor
    //----------------//
    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() { return null; }

    /**
     * Ask an Interactable if it accepts interaction with RealPlayer
     * @param other (Interactable). Not null
     */
    @Override
    public void interactWith(Interactable other) {
        if (!isDisplacementOccurs()) {
            other.acceptInteraction(handler);
        }
    }

    private class ICWarsPlayerInteractionHandler implements ICWarsInteractionVisitor {
        @Override
        public void interactWith(Unit unit) {
            if ((state == PlayerState.SELECT_CELL) && (areInSameFaction(RealPlayer.this, unit))) {
                if (!unit.isAvailable()) {
                    state = PlayerState.NORMAL;
                    return;
                }
                selectedUnit = unit;
                playerGUI.setSelectedUnit(selectedUnit);
            }
            if (!isDisplacementOccurs()) {
                playerGUI.getInfoPanel().setUnit(unit);
            }
        }

        @Override
        public void interactWith(ICWarsBehavior.ICWarsCell cell) {
            playerGUI.getInfoPanel().setCurrentCell(cell.getType());
        }
    }

    //----------------//
    // Extension
    //----------------//
    private int money;

    /**
     * Add/Remove money from the player balance.
     * @param amount the amount to be added, can be negative (removing money)
     */
    public void changeMoney(int amount) {
        money += amount;
        playerGUI.getGamePanel().setMoney(money);
    }

    /** Returns the player money */
    public int getMoney() { return money; }
}
