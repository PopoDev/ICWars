package ch.epfl.cs107.play.game.icwars.actor.player;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.icwars.ICWars;
import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
import ch.epfl.cs107.play.game.icwars.actor.unit.action.Action;
import ch.epfl.cs107.play.game.icwars.gui.ICWarsPlayerGUI;
import ch.epfl.cs107.play.game.icwars.handler.ICWarsInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.List;

public class AIPlayer extends ICWarsPlayer {

    private final Sprite sprite;

    private ICWarsPlayerGUI playerGUI;
    private Action actionToDo;

    private final ICWarsAIPlayerInteractionHandler handler;

    private int counter;
    private boolean counting;
    private DiscreteCoordinates FinalDestination;
    private RealPlayer enemy;
    private DiscreteCoordinates[] EnemyUnitPosition;


    public AIPlayer(Faction faction, Area owner, DiscreteCoordinates position, Unit... units) {
        super(faction, owner, position, units);

        String spriteName;
        if (isAlly()) {
            spriteName = "icwars/allyCursor";
        } else {
            spriteName = "icwars/enemyCursor";
        }

        sprite = new Sprite(spriteName, 1.f, 1.f,this);

        playerGUI = new ICWarsPlayerGUI(ICWars.CAMERA_SCALE_FACTOR, this);
        handler = new ICWarsAIPlayerInteractionHandler();

        this.enemy = enemy;
        EnemyUnitPosition = enemy.getUnitCoordinates();
    }

    @Override
    public void draw(Canvas canvas) {
        if (state != PlayerState.IDLE) {
            sprite.draw(canvas);
        }
        if (state == PlayerState.MOVE_UNIT || state == PlayerState.ACTION_SELECTION) {
            playerGUI.setDestination(getCurrentMainCellCoordinates());
            //playerGUI.setActionsPanel();
            playerGUI.draw(canvas);
        }

        if (state == PlayerState.ACTION) {
            actionToDo.draw(canvas);
        }
    }

    @Override
    public void update(float deltaTime) {
        if(waitFor(1,deltaTime)) {
            updateStates(deltaTime);
        }
        super.update(deltaTime);
    }

    private void updateStates(float deltaTime) {
        //if (isAlly()) System.out.println(state);
        switch(state) {
            case IDLE:
                break;
            case NORMAL:
                moveTo();
                state = PlayerState.MOVE_UNIT;
                break;
            case MOVE_UNIT:
                    moveUnitTo();
                    state = PlayerState.ACTION;
                break;
            case ACTION:
                //actionToDo.doAction(deltaTime, this, keyboard);
                break;
        }
    }

    private boolean moveUnit() {
        if (selectedUnit.changePosition(getCurrentMainCellCoordinates())) {
            selectedUnit.setAvailable(false);  // No longer usable
            return true;
        }
        return false;
    }

    @Override
    public void finishAction() {
        super.finishAction();
        selectedUnit = null;  // Reset selectedUnit
        playerGUI.setSelectedUnit(null);
    }

    //----------------//
    // Interactor
    //----------------//
    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() { return null; }

    /**
     * Makes the AIPlayer slower, so his movement can be visualized by the player
     * @param value
     * @param dt
     * @return
     */
    private boolean waitFor(float value, float dt){
        if(counting){
            counter += dt;
            if(counter < value) {
                counting = false;
                return true;
            }
        } else {
            counter = 0;
            counting = true;
        }
        return false;
    }

    /**
     * Ask other if it accepts interaction with RealPlayer
     * when the state is SELECT_CELL
     * @param other (Interactable). Not null
     */
    @Override
    public void interactWith(Interactable other) {
        if (state == PlayerState.NORMAL) { other.acceptInteraction(handler); }
    }

    private void moveTo(){
        for(Unit unit : units) {
                setCurrentPosition(unit.getCurrentMainCellCoordinates().toVector());
                selectedUnit = unit;
        }
    }

    /**
     * Changes the position of the unit to the closest position of an enemy unit;
     */
    private void moveUnitTo(){
        // Enemy unit position
        int EnemyUnitPositionX = EnemyUnitPosition[getClosestEnemyUnit()].x;
        int EnemyUnitPositionY = EnemyUnitPosition[getClosestEnemyUnit()].y;
        // AIPlayer position
        int x = selectedUnit.getCurrentMainCellCoordinates().x;
        int y = selectedUnit.getCurrentMainCellCoordinates().y;
        // Coordinates of destination, origin on AIPlayer
        int xFromPosition;
        int yFromPosition;

        if((Math.abs(EnemyUnitPositionX - x)) <= selectedUnit.getRange()) {
            xFromPosition = EnemyUnitPositionX - x;
        } else {
            if((EnemyUnitPositionX - x) < 0) {
                xFromPosition = -(selectedUnit.getRange());
            } else {
                xFromPosition = selectedUnit.getRange();
            }
        }

        if((Math.abs(EnemyUnitPositionY - y)) <= selectedUnit.getRange()) {
            yFromPosition = EnemyUnitPositionY - y;
        } else {
            if((EnemyUnitPositionY - y) < 0) {
                yFromPosition = -(selectedUnit.getRange());
            } else {
                yFromPosition = selectedUnit.getRange();
            }
        }
        FinalDestination = new DiscreteCoordinates((x + xFromPosition), (y + yFromPosition));
        changePosition(FinalDestination);
    }

    /**
     * Used to calculate de index of the coordinates of the closest enemy unit;
     * @return index of closest unit;
     */
    private int getClosestEnemyUnit(){
        int x = selectedUnit.getCurrentMainCellCoordinates().x;
        int y = selectedUnit.getCurrentMainCellCoordinates().y;
        double distance;
        double[] distances = new double[EnemyUnitPosition.length];
        double min = distances[0];

        for(int i = 0 ; i < EnemyUnitPosition.length ; ++i){
             distance = Math.sqrt((EnemyUnitPosition[i].x * EnemyUnitPosition[i].x) +
                                  (EnemyUnitPosition[i].y * EnemyUnitPosition[i].y));
             distances[i] = distance;
        }

        for (double v : distances) {
            if (min > v) {
                min = v;
            }
        }

        for(int i = 0; i < distances.length; ++i){
            if(min == distances[i]){
                return i;
            }
        }
        return 0;
    }

    private class ICWarsAIPlayerInteractionHandler implements ICWarsInteractionVisitor {
        @Override
        public void interactWith(Unit unit) {
            if((state == PlayerState.NORMAL) && (areInSameFaction(AIPlayer.this, unit))) {
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
