package map;

import exception.InvalidCoordinateException;
import exception.MapInitialisationException;

import java.io.File;

public class MapController {

    private Map map;
    private MapFactory mapFactory = new MapFactory();


    /* ------- Factory ------- */

    public MapFacade setEmptyMap(int xDim, int yDim) throws MapInitialisationException {
        this.map    = mapFactory.createEmptyMap(xDim,yDim);
        return this.map;
    }

    public MapFacade setRandomMap(int xDim, int yDim, double pPassable) throws MapInitialisationException {
        this.map    = mapFactory.createRandomMap(xDim,yDim,pPassable);
        return this.map;
    }

    public MapFacade setMazeMap(int xDim, int yDim) throws MapInitialisationException {
        this.map    = mapFactory.createMazeMap(xDim,yDim);
        return this.map;
    }

    public MapFacade setMazeRoomMap(int xDim, int yDim) throws MapInitialisationException {
        this.map    = mapFactory.createMazeRoomMap(xDim,yDim);
        return this.map;
    }

    public MapFacade setSingleRoomMap(int xDim, int yDim) throws MapInitialisationException {
        this.map    = mapFactory.createSingleRoomMap(xDim,yDim);
        return this.map;
    }

    public MapFacade setDoubleRoomMap(int xDim, int yDim) throws MapInitialisationException {
        this.map    = mapFactory.createDoubleRoomMap(xDim,yDim);
        return this.map;
    }

    public MapFacade setLoopRoomMap(int xDim, int yDim) throws MapInitialisationException {
        this.map    = mapFactory.createLoopRoomMap(xDim,yDim);
        return this.map;
    }

    public MapFacade loadMap(File file) throws MapInitialisationException {
        this.map    = mapFactory.loadMap(file);
        return this.map;
    }


    /* ------- Map Operations ------- */

    public void switchPassable(int x, int y) throws InvalidCoordinateException {
        this.map.switchPassable(x,y);
    }
}