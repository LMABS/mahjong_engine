package fr.univubs.inf1603.mahjong.engine;

import java.util.Collection;

/**
 * Representation of a pattern in a ({@link PlayerSet})
 */
public class IdentifiedPattern {
    /**
     * Pattern identified
     */
    private IdentifiablePattern pattern;
    /**
     * Tiles composing the pattern
     */
    private Collection<GameTile> tiles;
}
