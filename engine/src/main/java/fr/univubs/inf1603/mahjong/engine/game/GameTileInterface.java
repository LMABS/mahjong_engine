package fr.univubs.inf1603.mahjong.engine.game;

import fr.univubs.inf1603.mahjong.engine.rule.AbstractTile;
import fr.univubs.inf1603.mahjong.engine.rule.Wind;

/**
 *
 * @author purpl
 */
public interface GameTileInterface {

    public static final String TILE = "tile";

    /**
     * @return Returns this GameTileInterface's AbstractTile (the face of the tile), will
     * return the HiddenTile instance if this face is not visible
     */
    public AbstractTile getTile();

    /**
     * @return This GameTileInterface's id (order in the deck after shuffle) in the current game.
     */
    public int getGameID();
    
    public Wind getOrientation();
}
