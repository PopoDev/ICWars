package ch.epfl.cs107.play.game.icwars.gui;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.icwars.actor.player.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.player.RealPlayer;
import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;


public class ICWarsPlayerGUI implements Graphics {

    private final RealPlayer player;
    private Unit selectedUnit;
    private DiscreteCoordinates destination;

    public static final float FONT_SIZE = 20.f;
    private final ICWarsActionsPanel actionsPanel;
    private final ICWarsInfoPanel infoPanel;

    public ICWarsPlayerGUI(float cameraScaleFactor, ICWarsPlayer player) {
        this.player = (RealPlayer) player;

        actionsPanel = new ICWarsActionsPanel(cameraScaleFactor);
        infoPanel = new ICWarsInfoPanel(cameraScaleFactor);
    }

    public void setSelectedUnit(Unit unit) {
        this.selectedUnit = unit;
    }

    public void setDestination(DiscreteCoordinates destination) { this.destination = destination; }

    public ICWarsActionsPanel getActionsPanel() { return actionsPanel; }

    public ICWarsInfoPanel getInfoPanel() { return infoPanel; }

    @Override
    public void draw(Canvas canvas) {
        ICWarsPlayer.PlayerState state = player.getState();

        if (selectedUnit != null) {
            if (state == ICWarsPlayer.PlayerState.MOVE_UNIT) {
                selectedUnit.drawRangeAndPathTo(destination, canvas);
            }
            if (state == ICWarsPlayer.PlayerState.ACTION_SELECTION) {
                actionsPanel.draw(canvas);
            }
        }
        if (state == ICWarsPlayer.PlayerState.NORMAL || state == ICWarsPlayer.PlayerState.SELECT_CELL) {
            infoPanel.draw(canvas);
        }

    }

}
