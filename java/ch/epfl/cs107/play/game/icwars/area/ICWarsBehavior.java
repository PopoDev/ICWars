package ch.epfl.cs107.play.game.icwars.area;

import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.handler.ICWarsInteractionVisitor;
import ch.epfl.cs107.play.window.Window;

public class ICWarsBehavior extends AreaBehavior {

    /**
     * Default ICWarsBehavior Constructor
     * @param window (Window): graphic context, not null
     * @param name   (String): name of the behavior image, not null
     */
    public ICWarsBehavior(Window window, String name) {
        super(window, name);

        int height = getHeight();
        int width = getWidth();
        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                ICWarsCellType type = ICWarsCellType.toType(getRGB(height-1-y, x));
                setCell(x, y, new ICWarsCell(x, y, type));
            }
        }
    }

    public enum ICWarsCellType {
        NONE(0, 0),         // Should never be used except in the toType method
        ROAD(-16777216, 0), // the second value is the number of defense stars
        PLAIN(-14112955, 1),
        WOOD(-65536, 3),
        RIVER(-16776961, 0),
        MOUNTAIN(-256, 4),
        CITY(-1,2);

        final int type;
        final int defenseStars;

        ICWarsCellType(int type, int defenseStars) {
            this.type = type;
            this.defenseStars = defenseStars;
        }

        public static ICWarsCellType toType(int type) {
            for (ICWarsCellType cellType : ICWarsCellType.values()) {
                if (cellType.type == type) { return cellType; }
            }
            return NONE;
        }

        public int getDefenseStar() { return defenseStars; }

        public String typeToString() {
            String str = toString();
            return str.charAt(0) + str.substring(1).toLowerCase();
        }
    }

    /** Cell adapted to the ICWars game */
    public class ICWarsCell extends AreaBehavior.Cell {

        private final ICWarsCellType type;

        /**
         * Default ICWarsCell constructor
         *
         * @param x (int): x-coordinate of this cell
         * @param y (int): y-coordinate of this cell
         * @param type (ICWarsCellType): type of this cell following the enum
         * @see ICWarsCellType
         */
        public ICWarsCell(int x, int y, ICWarsCellType type) {
            super(x, y);
            this.type = type;
        }

        public ICWarsCellType getType() { return type; }

        //----------------//
        // AreaBehavior
        //----------------//
        @Override
        protected boolean canLeave(Interactable entity) { return true; }

        @Override
        protected boolean canEnter(Interactable entity) {
            boolean hasEntitiesTakingCellSpace = false;
            for (Interactable interactable : entities) {
                if (interactable.takeCellSpace()) {
                    hasEntitiesTakingCellSpace = true;
                    break;
                }
            }
            return ! (entity.takeCellSpace() && hasEntitiesTakingCellSpace);
        }

        //----------------//
        // Interactable
        //----------------//
        @Override
        public boolean isCellInteractable() { return true; }

        @Override
        public boolean isViewInteractable() { return false; }

        @Override
        public void acceptInteraction(AreaInteractionVisitor v) {
            ((ICWarsInteractionVisitor)v).interactWith(this);
        }
    }
}
