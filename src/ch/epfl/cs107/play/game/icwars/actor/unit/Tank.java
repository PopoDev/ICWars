package ch.epfl.cs107.play.game.icwars.actor.unit;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Tank extends Unit {

    private Sprite sprite;
    private String spriteName;

    private final int HP_MAX = 10;
    private final int DAMAGE = 7;
    private final int MOVE_RADIUS = 4;

    public Tank(Faction faction, Area owner, DiscreteCoordinates position) {
        super(faction, owner, position);
        if (isAlly()) {
            spriteName = "icwars/friendlyTank";
        } else {
            spriteName = "icwars/enemyTank";
        }
        sprite = new Sprite(spriteName, 1.5f, 1.5f, this, null, new Vector(-0.25f, -0.25f));

        setHp(HP_MAX);
    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
    }


    @Override
    protected int getHpMax() { return HP_MAX; }

    @Override
    protected int getDamage() { return DAMAGE; }

    @Override
    protected int getMoveRadius() { return MOVE_RADIUS; }

}
