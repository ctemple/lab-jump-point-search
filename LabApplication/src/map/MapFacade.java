package map;

import exception.InvalideCoordinateException;

/**
 * Created by paloka on 02.06.16.
 */
public interface MapFacade {

    int getXDim();
    int getYDim();
    boolean isPassable(int x, int y) throws InvalideCoordinateException;
}
