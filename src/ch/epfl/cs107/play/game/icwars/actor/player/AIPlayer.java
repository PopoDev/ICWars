package ch.epfl.cs107.play.game.icwars.actor.player;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
import ch.epfl.cs107.play.game.icwars.actor.unit.action.Action;
import ch.epfl.cs107.play.game.icwars.handler.ICWarsInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.List;

public class AIPlayer extends ICWarsPlayer {

    private final Sprite sprite;

    private Action actionToDo;

    private final ICWarsAIPlayerInteractionHandler handler;

    private int counter = 0;
    private boolean counting = true;
    private RealPlayer enemy;
    private DiscreteCoordinates[] enemyUnitPosition;

    public AIPlayer(RealPlayer enemy, Faction faction, Area owner, DiscreteCoordinates position, Unit... units) {
        super(faction, owner, position, units);

        String spriteName;
        if (isAlly()) {
            spriteName = "icwars/allyCursor";
        } else {
            spriteName = "icwars/enemyCursor";
        }

        sprite = new Sprite(spriteName, 1.f, 1.f,this);

        handler = new ICWarsAIPlayerInteractionHandler();

        this.enemy = enemy;
        enemyUnitPosition = enemy.getUnitCoordinates();
    }

    @Override
    public void draw(Canvas canvas) {
        if (state != PlayerState.IDLE) {
            sprite.draw(canvas);
        }
        /*
        if (state == PlayerState.ACTION) {
            actionToDo.draw(canvas);
        }

         */
    }

    @Override
    public void update(float deltaTime) {
        if(waitFor(100, deltaTime)) {
            updateStates(deltaTime);
        }
        super.update(deltaTime);
    }

    private void updateStates(float deltaTime) {
        switch(state) {
            case IDLE:
                break;
            case NORMAL:
                enemyUnitPosition = enemy.getUnitCoordinates();
                for(Unit unit : units) {
                    if(unit.isAvailable()) {
                        moveTo(unit);
                    }
                }
                state = PlayerState.MOVE_UNIT;
                break;
            case MOVE_UNIT:
                if (selectedUnit.isAvailable()) {
                    moveUnitTo();
                }
                state = PlayerState.ACTION;
                break;
            case ACTION:
                System.out.println(selectedUnit);
                actionToDo = selectedUnit.getActions().get(0);
                if(actionToDo.doAutoAction(this)) {
                    actionToDo.doAutoAction(this);
                } else {
                    actionToDo = selectedUnit.getActions().get(1);
                    actionToDo.doAutoAction(this);
                }
                finishAction();
                break;
        }
    }

    @Override
    public void finishAction() {
        super.finishAction();
        selectedUnit = null;  // Reset selectedUnit
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
        if(counting) {
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

    private void moveTo(Unit unit){
        // System.out.println(unit.getCurrentMainCellCoordinates().toVector());
        setCurrentPosition(unit.getPosition());
        selectedUnit = unit;
    }

    /**
     * Changes the position of the unit to the closest position of an enemy unit;
     */
    private void moveUnitTo() {
        // Enemy unit position
        DiscreteCoordinates enemyPosition = enemyUnitPosition[getClosestEnemyUnit()];
        // AIPlayer position
        DiscreteCoordinates coordinates = selectedUnit.getCurrentMainCellCoordinates();
        int x = coordinates.x;
        int y = coordinates.y;
        // Coordinates of destination, origin on AIPlayer
        Vector offSet = new Vector(0, 0);

        if((Math.abs(enemyPosition.x - x)) <= selectedUnit.getRange()) {
            //System.out.println("X axis: " + (enemyPosition.x -x));
            offSet = offSet.add(new Vector(enemyPosition.x - x, 0));
        } else {
            if((enemyPosition.x - x) < 0) {
                offSet = offSet.add(new Vector(-selectedUnit.getRange(), 0));
            } else {
                offSet = offSet.add(new Vector(selectedUnit.getRange(), 0));
            }
        }

        if((Math.abs(enemyPosition.y - y)) <= selectedUnit.getRange()) {
            offSet = offSet.add(new Vector(0,enemyPosition.y - y));
            //System.out.println("Y axis: " + (enemyPosition.y -y));
        } else {
            if((enemyPosition.y - y) < 0) {
                offSet = offSet.add(new Vector(0,-selectedUnit.getRange()));
            } else {
                offSet = offSet.add(new Vector(0,selectedUnit.getRange()));            }
        }
        DiscreteCoordinates finalDestination = coordinates.jump(offSet);
        selectedUnit.changePosition(finalDestination);

        //selectedUnit.setAvailable(false);  // No longer usable
    }

    /**
     * Used to calculate de index of the coordinates of the closest enemy unit;
     * @return index of closest unit;
     */
    private int getClosestEnemyUnit(){
        DiscreteCoordinates coordinates = selectedUnit.getCurrentMainCellCoordinates();
        double distance;
        double[] distances = new double[enemyUnitPosition.length];
        double min = distances[0];

        for(int i = 0; i < enemyUnitPosition.length ; ++i){
             distance = DiscreteCoordinates.distanceBetween(enemyUnitPosition[i], coordinates);
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
            }
        }
    }
}
