package ch.epfl.cs107.play.game.icwars.handler;

import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.actor.player.RealPlayer;
import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
import ch.epfl.cs107.play.game.icwars.area.ICWarsBehavior;

public interface ICWarsInteractionVisitor extends AreaInteractionVisitor {
    /**
     * @param unit
     */
    default void interactWith(Unit unit) {}

    default void interactWith(RealPlayer realPlayer) {}

    default void interactWith(ICWarsBehavior.ICWarsCell cell) {}

    @Override
    default void interactWith(Interactable other) {}
}
