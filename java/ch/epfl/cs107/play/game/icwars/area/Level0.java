package ch.epfl.cs107.play.game.icwars.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.player.AIPlayer;
import ch.epfl.cs107.play.game.icwars.actor.player.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.player.RealPlayer;
import ch.epfl.cs107.play.game.icwars.actor.unit.Soldier;
import ch.epfl.cs107.play.game.icwars.actor.unit.Tank;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.List;

public class Level0 extends ICWarsArea {

    @Override
    public String getTitle() { return "icwars/Level0"; }

    @Override
    public String toString() { return "Level 0"; }

    @Override
    protected void createArea() {
        registerActor(new Background(this)) ;
    }

    @Override
    public DiscreteCoordinates getPlayerSpawnPosition() {
        return new DiscreteCoordinates(0, 0);
    }

    @Override
    public DiscreteCoordinates getEnemySpawnPosition() { return new DiscreteCoordinates(7, 4); }

    @Override
    public void initPlayers(List<ICWarsPlayer> players) {
        // Player 1 - RealPlayer (Ally)
        RealPlayer player = new RealPlayer(ICWarsActor.Faction.ALLY, this, this.getPlayerSpawnPosition(),
                new Tank(ICWarsActor.Faction.ALLY, this, new DiscreteCoordinates(2, 5)).setName("[A] T1"),
                new Soldier(ICWarsActor.Faction.ALLY, this, new DiscreteCoordinates(3, 5)).setName("[A] S1"));
        player.changeMoney(100);  // Start Money
        players.add(player);

        // Player 2 - AIPlayer (Enemy)
        AIPlayer enemyPlayer = new AIPlayer(player, ICWarsActor.Faction.ENEMY, this, this.getEnemySpawnPosition(),
                new Tank(ICWarsActor.Faction.ENEMY, this, new DiscreteCoordinates(8, 5)).setName("[E] T1"),
                new Soldier(ICWarsActor.Faction.ENEMY, this, new DiscreteCoordinates(9, 5)).setName("[E] S1"));
        players.add(enemyPlayer);
    }
}
