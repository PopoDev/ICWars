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

    public ICWarsPlayerGUI(float cameraScaleFactor, ICWarsPlayer player) {
        this.player = (RealPlayer) player;
    }

    public void setSelectedUnit(Unit unit) {
        this.selectedUnit = unit;
    }

    @Override
    public void draw(Canvas canvas) {
        if (selectedUnit == null) { return; }

        int x = (int) player.getPosition().getX();
        int y = (int) player.getPosition().getY();
        selectedUnit.drawRangeAndPathTo(new DiscreteCoordinates(x, y), canvas);
    }
}
