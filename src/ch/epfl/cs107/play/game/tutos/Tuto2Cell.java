package ch.epfl.cs107.play.game.tutos;

import ch.epfl.cs107.play.game.tutos.Tuto2Behavior.Tuto2CellType;
import ch.epfl.cs107.play.game.areagame.Cell;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;

public class Tuto2Cell extends Cell {

    private Tuto2CellType type;
    private boolean isWalkable;

    public Tuto2Cell(int x, int y, Tuto2CellType type) {
        super(x, y);
        this.type = type;
        this.isWalkable = type.isWalkable;
    }

    @Override
    protected boolean canLeave(Interactable entity) { return true; }

    @Override
    protected boolean canEnter(Interactable entity) { return isWalkable; }

    @Override
    public boolean isCellInteractable() { return true; }

    @Override
    public boolean isViewInteractable() { return false; }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
    }
}
