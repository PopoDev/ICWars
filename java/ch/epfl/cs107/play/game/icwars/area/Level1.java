package ch.epfl.cs107.play.game.icwars.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.player.AIPlayer;
import ch.epfl.cs107.play.game.icwars.actor.player.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.player.RealPlayer;
import ch.epfl.cs107.play.game.icwars.actor.unit.Medic;
import ch.epfl.cs107.play.game.icwars.actor.unit.Rocket;
import ch.epfl.cs107.play.game.icwars.actor.unit.Soldier;
import ch.epfl.cs107.play.game.icwars.actor.unit.Tank;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.List;

public class Level1 extends ICWarsArea {

    @Override
    public String getTitle() { return "icwars/Level1"; }

    @Override
    public String toString() { return "Level 1"; }

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
                new Tank(ICWarsActor.Faction.ALLY, this, new DiscreteCoordinates(3, 4)).setName("[A] T1"),
                new Tank(ICWarsActor.Faction.ALLY, this, new DiscreteCoordinates(3, 6)).setName("[A] T2"),
                new Soldier(ICWarsActor.Faction.ALLY, this, new DiscreteCoordinates(3, 5)).setName("[A] S1"),
                new Medic(ICWarsActor.Faction.ALLY, this, new DiscreteCoordinates(2, 5)).setName("[A] M1"),
                new Rocket(ICWarsActor.Faction.ALLY, this, new DiscreteCoordinates(2, 4)).setName("[A] R1"),
                new Rocket(ICWarsActor.Faction.ALLY, this, new DiscreteCoordinates(2, 6)).setName("[A] R2"));
        player.changeMoney(200);  // Start Money
        players.add(player);

        // Player 2 - AIPlayer (Enemy)
        AIPlayer enemyPlayer = new AIPlayer(player, ICWarsActor.Faction.ENEMY, this, this.getEnemySpawnPosition(),
                new Soldier(ICWarsActor.Faction.ENEMY, this, new DiscreteCoordinates(8, 4)).setName("[E] S1"),
                new Soldier(ICWarsActor.Faction.ENEMY, this, new DiscreteCoordinates(8, 5)).setName("[E] S2"),
                new Soldier(ICWarsActor.Faction.ENEMY, this, new DiscreteCoordinates(8, 6)).setName("[E] S3"),
                new Tank(ICWarsActor.Faction.ENEMY, this, new DiscreteCoordinates(9, 4)).setName("[E] T1"),
                new Tank(ICWarsActor.Faction.ENEMY, this, new DiscreteCoordinates(9, 6)).setName("[E] T2"),
                new Rocket(ICWarsActor.Faction.ENEMY, this, new DiscreteCoordinates(9, 5)).setName("[E] R1"));
        players.add(enemyPlayer);
    }
}
