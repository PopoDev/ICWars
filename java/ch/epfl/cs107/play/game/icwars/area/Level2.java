package ch.epfl.cs107.play.game.icwars.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.player.AIPlayer;
import ch.epfl.cs107.play.game.icwars.actor.player.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.player.RealPlayer;
import ch.epfl.cs107.play.game.icwars.actor.unit.Rocket;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.List;

public class Level2 extends ICWarsArea {

    @Override
    public String getTitle() { return "icwars/Level2"; }  // We didn't make new images. Same as Level1

    @Override
    public String toString() { return "RocketLeague"; }

    @Override
    protected void createArea() {
        registerActor(new Background(this)) ;
    }

    @Override
    public DiscreteCoordinates getPlayerSpawnPosition() {
        return new DiscreteCoordinates(9, 6);
    }

    @Override
    public DiscreteCoordinates getEnemySpawnPosition() { return new DiscreteCoordinates(10, 6); }

    @Override
    public void initPlayers(List<ICWarsPlayer> players) {
        // Player 1 - RealPlayer (Ally)
        RealPlayer player = new RealPlayer(ICWarsActor.Faction.ALLY, this, this.getPlayerSpawnPosition(),
                new Rocket(ICWarsActor.Faction.ALLY, this, new DiscreteCoordinates(5, 3)).setName("[A] R1"),
                new Rocket(ICWarsActor.Faction.ALLY, this, new DiscreteCoordinates(5, 4)).setName("[A] R2"),
                new Rocket(ICWarsActor.Faction.ALLY, this, new DiscreteCoordinates(5, 5)).setName("[A] R3"),
                new Rocket(ICWarsActor.Faction.ALLY, this, new DiscreteCoordinates(5, 6)).setName("[A] R4"),
                new Rocket(ICWarsActor.Faction.ALLY, this, new DiscreteCoordinates(5, 7)).setName("[A] R5"),
                new Rocket(ICWarsActor.Faction.ALLY, this, new DiscreteCoordinates(5, 8)).setName("[A] R6"),
                new Rocket(ICWarsActor.Faction.ALLY, this, new DiscreteCoordinates(6, 3)).setName("[A] R7"),
                new Rocket(ICWarsActor.Faction.ALLY, this, new DiscreteCoordinates(6, 4)).setName("[A] R8"),
                new Rocket(ICWarsActor.Faction.ALLY, this, new DiscreteCoordinates(6, 5)).setName("[A] R9"),
                new Rocket(ICWarsActor.Faction.ALLY, this, new DiscreteCoordinates(6, 6)).setName("[A] R10"),
                new Rocket(ICWarsActor.Faction.ALLY, this, new DiscreteCoordinates(6, 7)).setName("[A] R11"),
                new Rocket(ICWarsActor.Faction.ALLY, this, new DiscreteCoordinates(6, 8)).setName("[A] R12"));
        player.changeMoney(1234);  // Start Money
        players.add(player);

        // Player 2 - AIPlayer (Enemy)
        AIPlayer enemyPlayer = new AIPlayer(player, ICWarsActor.Faction.ENEMY, this, this.getEnemySpawnPosition(),
                new Rocket(ICWarsActor.Faction.ENEMY, this, new DiscreteCoordinates(12, 3)).setName("[E] R1"),
                new Rocket(ICWarsActor.Faction.ENEMY, this, new DiscreteCoordinates(12, 4)).setName("[E] R2"),
                new Rocket(ICWarsActor.Faction.ENEMY, this, new DiscreteCoordinates(12, 5)).setName("[E] R3"),
                new Rocket(ICWarsActor.Faction.ENEMY, this, new DiscreteCoordinates(12, 6)).setName("[E] R4"),
                new Rocket(ICWarsActor.Faction.ENEMY, this, new DiscreteCoordinates(12, 7)).setName("[E] R5"),
                new Rocket(ICWarsActor.Faction.ENEMY, this, new DiscreteCoordinates(12, 8)).setName("[E] R6"),
                new Rocket(ICWarsActor.Faction.ENEMY, this, new DiscreteCoordinates(13, 3)).setName("[E] R7"),
                new Rocket(ICWarsActor.Faction.ENEMY, this, new DiscreteCoordinates(13, 4)).setName("[E] R8"),
                new Rocket(ICWarsActor.Faction.ENEMY, this, new DiscreteCoordinates(13, 5)).setName("[E] R9"),
                new Rocket(ICWarsActor.Faction.ENEMY, this, new DiscreteCoordinates(13, 6)).setName("[E] R10"),
                new Rocket(ICWarsActor.Faction.ENEMY, this, new DiscreteCoordinates(13, 7)).setName("[E] R11"),
                new Rocket(ICWarsActor.Faction.ENEMY, this, new DiscreteCoordinates(13, 8)).setName("[E] R12"));
        players.add(enemyPlayer);
    }
}
