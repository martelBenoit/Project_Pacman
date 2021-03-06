package main;

import java.awt.Color;

/**
 * NormalPill class. <br>
 * Sub class of Pill. Define a NormalPill object
 * @author Benoît Martel
 * @author Yoann Le Dréan
 * @version 1.0
 * @see Pill
 */
class NormalPill extends Pill{

    /**
     * Constructor of the NormalPill class. <br>
     * This constructor is used to create a NormalPill object. <br>
     * In parameter the tile on which is the NormalPill. <br>
     * @param tile the tile on which is the NormalPill.
     */
    NormalPill(Tile tile) {

        super(tile,Color.WHITE,7);
    }
}