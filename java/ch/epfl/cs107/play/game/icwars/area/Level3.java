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
    public String toString() { return "IC Peace"; }

    @Override
    protected void createArea() {
        registerActor(new Background(this)) ;
    }

    @Override
    public DiscreteCoordinates getPlayerSpawnPosition() {
        return new DiscreteCoordinates(7, 6);
    }

    @Override
    public DiscreteCoordinates getEnemySpawnPosition() { return new DiscreteCoordinates(7, 6); }

    @Override
    public void initPlayers(List<ICWarsPlayer> players) {
        int damageTest = 4;  // To test healing

        // Player 1 - RealPlayer (Ally)
        RealPlayer player = new RealPlayer(ICWarsActor.Faction.ALLY, this, this.getPlayerSpawnPosition(),
                new Medic(ICWarsActor.Faction.ALLY, this, new DiscreteCoordinates(3, 3)).takeDamageTest(damageTest).setName("[A] M1"),
                new Medic(ICWarsActor.Faction.ALLY, this, new DiscreteCoordinates(4, 3)).takeDamageTest(damageTest).setName("[A] M2"),
                new Medic(ICWarsActor.Faction.ALLY, this, new DiscreteCoordinates(5, 3)).takeDamageTest(damageTest).setName("[A] M3"),
                new Medic(ICWarsActor.Faction.ALLY, this, new DiscreteCoordinates(4, 4)).takeDamageTest(damageTest).setName("[A] M4"),
                new Medic(ICWarsActor.Faction.ALLY, this, new DiscreteCoordinates(4, 5)).takeDamageTest(damageTest).setName("[A] M5"),
                new Medic(ICWarsActor.Faction.ALLY, this, new DiscreteCoordinates(4, 6)).takeDamageTest(damageTest).setName("[A] M6"),
                new Medic(ICWarsActor.Faction.ALLY, this, new DiscreteCoordinates(4, 7)).takeDamageTest(damageTest).setName("[A] M7"),
                new Medic(ICWarsActor.Faction.ALLY, this, new DiscreteCoordinates(3, 8)).takeDamageTest(damageTest).setName("[A] M8"),
                new Medic(ICWarsActor.Faction.ALLY, this, new DiscreteCoordinates(4, 8)).takeDamageTest(damageTest).setName("[A] M9"),
                new Medic(ICWarsActor.Faction.ALLY, this, new DiscreteCoordinates(5, 8)).takeDamageTest(damageTest).setName("[A] M10"));
        player.changeMoney(1234);  // Start Money
        players.add(player);

        // Player 2 - AIPlayer (Enemy)
        AIPlayer enemyPlayer = new AIPlayer(player, ICWarsActor.Faction.ENEMY, this, this.getEnemySpawnPosition(),
                new Medic(ICWarsActor.Faction.ENEMY, this, new DiscreteCoordinates(10, 3)).setName("[E] M1"),
                new Medic(ICWarsActor.Faction.ENEMY, this, new DiscreteCoordinates(9, 3)).setName("[E] M2"),
                new Medic(ICWarsActor.Faction.ENEMY, this, new DiscreteCoordinates(8, 3)).setName("[E] M3"),
                new Medic(ICWarsActor.Faction.ENEMY, this, new DiscreteCoordinates(7, 4)).setName("[E] M4"),
                new Medic(ICWarsActor.Faction.ENEMY, this, new DiscreteCoordinates(7, 5)).setName("[E] M5"),
                new Medic(ICWarsActor.Faction.ENEMY, this, new DiscreteCoordinates(7, 6)).setName("[E] M6"),
                new Medic(ICWarsActor.Faction.ENEMY, this, new DiscreteCoordinates(7, 7)).setName("[E] M7"),
                new Medic(ICWarsActor.Faction.ENEMY, this, new DiscreteCoordinates(8, 8)).setName("[E] M8"),
                new Medic(ICWarsActor.Faction.ENEMY, this, new DiscreteCoordinates(9, 8)).setName("[E] M9"),
                new Medic(ICWarsActor.Faction.ENEMY, this, new DiscreteCoordinates(10, 8)).setName("[E] M10"));
        players.add(enemyPlayer);
    }
}
