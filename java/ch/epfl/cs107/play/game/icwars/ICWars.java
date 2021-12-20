package ch.epfl.cs107.play.game.icwars;

import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.player.AIPlayer;
import ch.epfl.cs107.play.game.icwars.actor.player.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.player.RealPlayer;
import ch.epfl.cs107.play.game.icwars.actor.unit.*;
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
        Keyboard keyboard = getCurrentArea().getKeyboard();
        buttonNextLevel(keyboard.get(Keyboard.N));
        buttonReset(keyboard.get(Keyboard.R));

        // Game dynamic
        updateGameState(keyboard);

        super.update(deltaTime);
    }

    /** Manage the game dynamic by updating the GameState */
    private void updateGameState(Keyboard keyboard) {
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
                    currentlyActivePlayer.finishTurn();
                    gameState = GameState.END_PLAYER_TURN;
                }
                break;
            case END_PLAYER_TURN :
                if (isPlayerDefeated(currentlyActivePlayer)) {
                    removePlayer(currentlyActivePlayer);
                    currentlyActivePlayer = null;
                } else {
                    if (currentlyActivePlayer != null) { waitingNextRound.add(currentlyActivePlayer); }
                    gameState = GameState.CHOOSE_PLAYER;
                }
                break;
            case END_TURN :
                for (ICWarsPlayer player : players) {
                    if (isPlayerDefeated(player)) {
                        removePlayer(player);
                    } else {
                        player.initTurn();
                    }
                }
                if (waitingNextRound.size() <= 1) {
                    gameState = GameState.END;
                } else {
                    waitingCurrentRound.addAll(waitingNextRound);
                    waitingNextRound.clear();
                    addMoneyEachTurn(MONEY_EACH_TURN);
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

    /** Add all the Levels */
    private void createAreas() {
        addArea(new Level0());
        addArea(new Level1());
    }

    /**
     * Initialize an area and add the players
     * @param areaTitle the title of the area
     */
    private void initArea(String areaTitle) {
        ICWarsArea area = (ICWarsArea) setCurrentArea(areaTitle, true);

        // Player 1 (Ally)
        RealPlayer player = new RealPlayer(ICWarsActor.Faction.ALLY, area, area.getPlayerSpawnPosition(),
                new Tank(ICWarsActor.Faction.ALLY, area, new DiscreteCoordinates(1, 4)).setName("[A] T1"),
                new Soldier(ICWarsActor.Faction.ALLY, area, new DiscreteCoordinates(2, 5)).setName("[A] S1"),
                new Medic(ICWarsActor.Faction.ALLY, area, new DiscreteCoordinates(1, 5)).setName("[A] M1"),
                new Rocket(ICWarsActor.Faction.ALLY, area, new DiscreteCoordinates(2, 4)).setName("[A] R1"));

        players.add(player);
        player.enterArea(area, area.getPlayerSpawnPosition());
        player.centerCamera();

        // Player 2 (Enemy)
        AIPlayer enemyPlayer = new AIPlayer(player, ICWarsActor.Faction.ENEMY, area, area.getEnemySpawnPosition(),
                new Tank(ICWarsActor.Faction.ENEMY, area, new DiscreteCoordinates(8, 5)).setName("[E] T1"),
                new Soldier(ICWarsActor.Faction.ENEMY, area, new DiscreteCoordinates(7, 5)).setName("[E] S1"),
                new Rocket(ICWarsActor.Faction.ENEMY, area, new DiscreteCoordinates(8, 4)).setName("[E] R1"),
                new Tank(ICWarsActor.Faction.ENEMY, area, new DiscreteCoordinates(7, 4)).setName("[E] T2"));

        players.add(enemyPlayer);
        enemyPlayer.enterArea(area, area.getEnemySpawnPosition());
    }

    /** Start next Level when the key N is released
     * @param key the key to press (N)
     */
    private void buttonNextLevel(Button key) {
        if (key.isReleased()) {
            nextLevel();
        }
    }

    /**
     * Start the next Level.
     * All the remaining Actors are removed and a new area is initialized
     */
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

    /** Reset the game the key R is released
     * @param key the key to press (R)
     */
    private void buttonReset(Button key) {
        if (key.isReleased()) {
            reset();
        }
    }

    /** Reset the game.
     * All Actors are removed and the first Level is initialized.
     */
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

    /** Returns <code>true</code> if the player has no more units. */
    private boolean isPlayerDefeated(ICWarsPlayer player) {
        if (player == null) { return false; }
        return player.isDefeated();
    }

    /** Unregister the player from the area and the game. */
    private void removePlayer(ICWarsPlayer player) {
        player.leaveArea();  // Unregister the player from the game
        waitingNextRound.remove(player);
        players.remove(player);
    }

    /** Clear all the collections storing players */
    private void clearPlayers() {
        players.clear();
        waitingCurrentRound.clear();
        waitingNextRound.clear();
        currentlyActivePlayer = null;
    }

    /** Describe the course of a game */
    private enum GameState {
        INIT, CHOOSE_PLAYER,
        START_PLAYER_TURN, PLAYER_TURN, END_PLAYER_TURN,
        END_TURN, END
    }

    //----------------//
    // Extension
    //----------------//
    private final int MONEY_EACH_TURN = 100;

    /** Give money to all RealPlayers after a round */
    private void addMoneyEachTurn(int amount) {
        for (ICWarsPlayer player : players) {
            if (player instanceof RealPlayer) {  // Only for real player for now
                ((RealPlayer) player).changeMoney(amount);
            }
        }
    }

}
