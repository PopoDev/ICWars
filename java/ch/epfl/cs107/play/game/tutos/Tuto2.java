package ch.epfl.cs107.play.game.tutos;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.tutos.actor.GhostPlayer;
import ch.epfl.cs107.play.game.tutos.area.tuto2.Ferme;
import ch.epfl.cs107.play.game.tutos.area.tuto2.Village;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public class Tuto2 extends AreaGame {

    private GhostPlayer player;
    private String beginArea = "zelda/Ferme";
    private DiscreteCoordinates beginAreaCoords = new DiscreteCoordinates(2, 10);
    private String otherArea = "zelda/Village";
    private DiscreteCoordinates otherAreaCoords = new DiscreteCoordinates(5, 15);
    private Area currentArea;

    public static final float DEFAULT_SCALE_FACTOR = 13.f;

    @Override
    public String getTitle() {
        return "Tuto2";
    }

    private void createAreas() {
        addArea(new Village());
        addArea(new Ferme());
    }

    private void switchArea() {
        if (currentArea.getTitle().equals(beginArea)) {  // Then change the currentArea
            currentArea = setCurrentArea(otherArea, false);
            player.enterArea(currentArea, otherAreaCoords);
        } else {
            currentArea = setCurrentArea(beginArea, false);
            player.enterArea(currentArea, beginAreaCoords);
        }

        currentArea.setViewCandidate(player);
        player.strengthen();
    }

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {

            createAreas();
            currentArea = setCurrentArea(beginArea, true);

            player = new GhostPlayer(currentArea, Orientation.DOWN, beginAreaCoords, "ghost.1");
            currentArea.registerActor(player);
            currentArea.setViewCandidate(player);

            return true;
        }
        else return false;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (player.isWeak()) { switchArea(); }
    }

    @Override
    public void end() {
    }
}
