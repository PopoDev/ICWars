package ch.epfl.cs107.play.game.icwars;

import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.player.RealPlayer;
import ch.epfl.cs107.play.game.icwars.actor.unit.Soldier;
import ch.epfl.cs107.play.game.icwars.actor.unit.Tank;
import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.game.icwars.area.Level0;
import ch.epfl.cs107.play.game.icwars.area.Level1;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

import java.util.List;

public class ICWars extends AreaGame {

    public final static float CAMERA_SCALE_FACTOR = 10.f;

    private final String[] areas = {"icwars/Level0", "icwars/Level1"};
    private int areaIndex;

    private RealPlayer realPlayer;
    private Unit[] allyUnits;

    @Override
    public String getTitle() { return "ICWars"; }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        Keyboard keyboard = getCurrentArea().getKeyboard();
        buttonNextLevel(keyboard.get(Keyboard.N));
        buttonReset(keyboard.get(Keyboard.R));

        if (keyboard.get(Keyboard.U).isReleased()) {
            realPlayer.selectUnit(1);
        }
    }

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            createAreas();
            areaIndex = 0;
            initArea(areas[areaIndex]);
            return true;
        }
        return false;
    }

    @Override
    public void end() {
        System.out.println("Game Over");
    }

    private void createAreas() {
        addArea(new Level0());
        addArea(new Level1());
    }

    private void initArea(String areaTitle) {
        ICWarsArea area = (ICWarsArea) setCurrentArea(areaTitle, true);
        DiscreteCoordinates coords = area.getPlayerSpawnPosition();

        allyUnits = new Unit[] {
                new Soldier(ICWarsActor.Faction.ALLY, area, new DiscreteCoordinates(3, 5)),
                new Tank(ICWarsActor.Faction.ALLY, area, new DiscreteCoordinates(2, 5))
        };

        realPlayer = new RealPlayer(ICWarsActor.Faction.ALLY, area, coords, allyUnits);
        realPlayer.enterArea(area, coords);
        realPlayer.centerCamera();
    }

    private void buttonNextLevel(Button key) {
        if (key.isReleased()) {
            nextLevel();
        }
    }

    private void nextLevel() {
        if (areaIndex < areas.length - 1) {
            System.out.println("Next level");
            ++areaIndex;
            realPlayer.leaveArea();
            initArea(areas[areaIndex]);
        } else {
            end();
        }
    }

    private void buttonReset(Button key) {
        if (key.isReleased()) {
            reset();
        }
    }

    private void reset() {
        System.out.println("Reset");
        // Clean the registered actors
        realPlayer.leaveArea();

        areaIndex = 0;
        initArea(areas[areaIndex]);
    }

}
