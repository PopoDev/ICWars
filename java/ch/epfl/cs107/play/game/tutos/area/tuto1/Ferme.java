package ch.epfl.cs107.play.game.tutos.area.tuto1;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.tutos.area.SimpleArea;

public class Ferme extends SimpleArea {
    @Override
    public String getTitle() {
        return "zelda/Ferme";
    }

    @Override
    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));
    }
}
