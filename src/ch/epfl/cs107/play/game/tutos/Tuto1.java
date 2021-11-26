package ch.epfl.cs107.play.game.tutos;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.tutos.actor.SimpleGhost;
import ch.epfl.cs107.play.game.tutos.area.tuto1.Ferme;
import ch.epfl.cs107.play.game.tutos.area.tuto1.Village;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

import java.util.HashMap;

public class Tuto1 extends AreaGame {

    private SimpleGhost player;
    private String beginArea = "zelda/Ferme";
    private String otherArea = "zelda/Village";
    private Area currentArea;

    @Override
    public String getTitle() {
        return "Tuto1";
    }

    private void createAreas() {
        addArea(new Village());
        addArea(new Ferme());
    }

    private void switchArea() {
        currentArea.unregisterActor(player);  // First unregister the player

        if (currentArea.getTitle().equals(beginArea)) {  // Then change the currentArea
            currentArea = setCurrentArea(otherArea, false);
        } else {
            currentArea = setCurrentArea(beginArea, false);
        }

        currentArea.registerActor(player);  // Register the player in the new area
        currentArea.setViewCandidate(player);
        player.strengthen();
    }

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {

            createAreas();
            currentArea = setCurrentArea(beginArea, true);

            player = new SimpleGhost(new Vector(18, 7), "ghost.1");
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

        Keyboard keyboard = getWindow().getKeyboard();

        if (isKeyDown(keyboard, Keyboard.UP)) { player.moveUp(); }
        if (isKeyDown(keyboard, Keyboard.DOWN)) { player.moveDown(); }
        if (isKeyDown(keyboard, Keyboard.RIGHT)) { player.moveRight(); }
        if (isKeyDown(keyboard, Keyboard.LEFT)) { player.moveLeft(); }
    }

    @Override
    public void end() {
    }

    private boolean isKeyDown(Keyboard keyboard, int keyCode) {
        Button key = keyboard.get(keyCode);
        return key.isDown();
    }
}
