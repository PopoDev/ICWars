package ch.epfl.cs107.play.game.icwars.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.player.AIPlayer;
import ch.epfl.cs107.play.game.icwars.actor.player.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.player.RealPlayer;
import ch.epfl.cs107.play.game.icwars.actor.unit.Medic;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.List;

public class Level3 extends ICWarsArea {

    @Override
    public String getTitle() { return "icwars/Level3"; }  // We didn't make new images. Same as Level1

    @Override
    public String toString() { return "Peace"; }

    @Override
    protected void createArea() {
        registerActor(new Background(this)) ;
    }

    @Override
    public DiscreteCoordinates getPlayerSpawnPosition() {
        return new DiscreteCoordinates(2, 5);
    }

    @Override
    public DiscreteCoordinates getEnemySpawnPosition() { return new DiscreteCoordinates(17, 5); }

    @Override
    public void initPlayers(List<ICWarsPlayer> players) {
        // Player 1 - RealPlayer (Ally)
        RealPlayer player = new RealPlayer(ICWarsActor.Faction.ALLY, this, this.getPlayerSpawnPosition(),
                new Medic(ICWarsActor.Faction.ALLY, this, new DiscreteCoordinates(2, 3)).setName("[A] R1"),
                new Medic(ICWarsActor.Faction.ALLY, this, new DiscreteCoordinates(2, 4)).setName("[A] R2"),
                new Medic(ICWarsActor.Faction.ALLY, this, new DiscreteCoordinates(2, 5)).setName("[A] R3"),
                new Medic(ICWarsActor.Faction.ALLY, this, new DiscreteCoordinates(2, 6)).setName("[A] R4"),
                new Medic(ICWarsActor.Faction.ALLY, this, new DiscreteCoordinates(2, 7)).setName("[A] R5"),
                new Medic(ICWarsActor.Faction.ALLY, this, new DiscreteCoordinates(2, 8)).setName("[A] R6"));
        player.changeMoney(1234);  // Start Money
        players.add(player);

        // Player 2 - AIPlayer (Enemy)
        AIPlayer enemyPlayer = new AIPlayer(player, ICWarsActor.Faction.ENEMY, this, this.getEnemySpawnPosition(),
                new Medic(ICWarsActor.Faction.ENEMY, this, new DiscreteCoordinates(9, 3)).setName("[E] R1"),
                new Medic(ICWarsActor.Faction.ENEMY, this, new DiscreteCoordinates(9, 4)).setName("[E] R2"),
                new Medic(ICWarsActor.Faction.ENEMY, this, new DiscreteCoordinates(9, 5)).setName("[E] R3"),
                new Medic(ICWarsActor.Faction.ENEMY, this, new DiscreteCoordinates(9, 6)).setName("[E] R4"),
                new Medic(ICWarsActor.Faction.ENEMY, this, new DiscreteCoordinates(9, 7)).setName("[E] R5"),
                new Medic(ICWarsActor.Faction.ENEMY, this, new DiscreteCoordinates(9, 8)).setName("[E] R6"));
        players.add(enemyPlayer);
    }
}
