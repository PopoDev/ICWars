package ch.epfl.cs107.play.game.icwars;

import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.player.AIPlayer;
import ch.epfl.cs107.play.game.icwars.actor.player.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.player.RealPlayer;
import ch.epfl.cs107.play.game.icwars.actor.unit.Soldier;
import ch.epfl.cs107.play.game.icwars.actor.unit.Tank;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.game.icwars.area.Level0;
import ch.epfl.cs107.play.game.icwars.area.Level1;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

import java.util.ArrayList;
import java.util.List;

public class ICWars extends AreaGame {

    public final static float CAMERA_SCALE_FACTOR = 10.f;

    private final String[] areas = {"icwars/Level0", "icwars/Level1"};
    private int areaIndex;

    private GameState gameState;

    private final List<ICWarsPlayer> players = new ArrayList<>();
    private final List<ICWarsPlayer> waitingCurrentRound = new ArrayList<>();
    private final List<ICWarsPlayer> waitingNextRound = new ArrayList<>();
    private ICWarsPlayer currentlyActivePlayer;

    @Override
    public String getTitle() { return "ICWars"; }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        Keyboard keyboard = getCurrentArea().getKeyboard();
        buttonNextLevel(keyboard.get(Keyboard.N));
        buttonReset(keyboard.get(Keyboard.R));

        //----------------//
        // Game dynamics
        //----------------//
        switch (gameState) {
            case INIT :
                waitingCurrentRound.addAll(players);
                gameState = GameState.CHOOSE_PLAYER;
                break;
            case CHOOSE_PLAYER :
                if (waitingCurrentRound.isEmpty()) {
                    gameState = GameState.END_TURN;
                } else {
                    currentlyActivePlayer = waitingCurrentRound.remove(0);
                    gameState = GameState.START_PLAYER_TURN;
                }
                break;
            case START_PLAYER_TURN :
                currentlyActivePlayer.startTurn();
                gameState = GameState.PLAYER_TURN;
                break;
            case PLAYER_TURN :
                if (currentlyActivePlayer.finishedTurn() || keyboard.get(Keyboard.TAB).isPressed()) {
                    gameState = GameState.END_PLAYER_TURN;
                }
                break;
            case END_PLAYER_TURN :
                if (isPlayerDefeated(currentlyActivePlayer)) {
                    removePlayerDefeated(currentlyActivePlayer);
                    currentlyActivePlayer = null;
                } else {
                    if (currentlyActivePlayer != null) { waitingNextRound.add(currentlyActivePlayer); }
                    gameState = GameState.CHOOSE_PLAYER;
                }
                break;
            case END_TURN :
                for (ICWarsPlayer player : players) {
                    if (isPlayerDefeated(player)) {
                        removePlayerDefeated(player);
                    }
                }
                if (waitingNextRound.size() <= 1) {
                    gameState = GameState.END;
                } else {
                    waitingCurrentRound.addAll(waitingNextRound);
                    waitingNextRound.clear();
                    gameState = GameState.CHOOSE_PLAYER;
                }
                break;
            case END :
                nextLevel();
                break;
        }
    }

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            gameState = GameState.INIT;

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

        // Player 1 (Ally)
        RealPlayer player = new RealPlayer(ICWarsActor.Faction.ALLY, area, area.getPlayerSpawnPosition(),
                new Tank(ICWarsActor.Faction.ALLY, area, new DiscreteCoordinates(2, 5)).setName("[A] T1"),
                new Soldier(ICWarsActor.Faction.ALLY, area, new DiscreteCoordinates(3, 5)).setName("[A] S1"));

        players.add(player);
        player.enterArea(area, area.getPlayerSpawnPosition());
        player.centerCamera();

        // Player 2 (Enemy)
        AIPlayer enemyPlayer = new AIPlayer(ICWarsActor.Faction.ENEMY, area, area.getEnemySpawnPosition(),
                new Tank(ICWarsActor.Faction.ENEMY, area, new DiscreteCoordinates(8, 5)).setName("[E] T1"),
                new Soldier(ICWarsActor.Faction.ENEMY, area, new DiscreteCoordinates(9, 5)).setName("[E] S1"));

        players.add(enemyPlayer);
        enemyPlayer.enterArea(area, area.getEnemySpawnPosition());
    }

    private void buttonNextLevel(Button key) {
        if (key.isReleased()) {
            nextLevel();
        }
    }

    private void nextLevel() {
        if (areaIndex < areas.length - 1) {
            System.out.println("Next level");
            gameState = GameState.INIT;

            // Clean the registered actors
            for (ICWarsPlayer player : players) {
                player.leaveArea();
            }
            clearPlayers();

            ++areaIndex;
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
        gameState = GameState.INIT;

        // Clean the registered actors
        for (ICWarsPlayer player : players) {
            player.leaveArea();
        }
        clearPlayers();

        areaIndex = 0;
        initArea(areas[areaIndex]);
    }

    private boolean isPlayerDefeated(ICWarsPlayer player) {
        if (player == null) { return false; }
        return player.isDefeated();
    }

    private void removePlayerDefeated(ICWarsPlayer player) {
        player.leaveArea();  // Unregister the player from the game
        waitingNextRound.remove(player);
        players.remove(player);
    }

    private void clearPlayers() {
        players.clear();
        waitingCurrentRound.clear();
        waitingNextRound.clear();
        currentlyActivePlayer = null;
    }

    private enum GameState {
        INIT, CHOOSE_PLAYER,
        START_PLAYER_TURN, PLAYER_TURN, END_PLAYER_TURN,
        END_TURN, END
    }

}
