package javafxapplication;

import core.exception.InvalidCoordinateException;
import core.exception.MapInitialisationException;
import core.exception.NoPathFoundException;
import core.map.MapController;
import core.util.Tuple2;
import core.util.Vector;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the whole JavaFX application.
 *
 * @author Danny Rademacher, Patrick Loka
 * @version 1.0
 * @since 1.0
 */
public class ApplicationController implements Initializable {
    @FXML
    private BorderPane mainPane;

    @FXML
    private MenuItem emptyMapMenuItem, randomMapMenuItem, mazeMapMenuItem, mazeRoomMapMenuItem, singleRoomMapMenuItem, doubleRoomMapMenuItem, loopRoomMapMenuItem;

    @FXML
    private MenuItem openMapMenuItem, saveMapMenuItem;

    @FXML
    private MenuItem editMapMenuItem;

    @FXML
    private ToggleGroup heuristicToggleGroup, movingRuleToggleGroup, shortestPathToggleGroup;

    @FXML
    private RadioMenuItem zeroHeuristicMenuItem, manhattanHeuristicMenuItem, euclideanHeuristicMenuItem, gridHeuristicMenuItem;

    @FXML
    private RadioMenuItem orthogonalOnlyMovingRuleMenuItem, cornerCuttingMovingRuleMenuItem, noCornerCuttingMovingRuleMenuItem;

    @FXML
    private RadioMenuItem aStarShortestPathMenuItem, aStarBBShortestPathMenuItem, jpsShortestPathMenuItem, jpsBBShortestPathMenuItem, jpsPlusShortestPathMenuItem, jpsPlusBBShortestPathMenuItem;

    @FXML
    private CheckMenuItem viewClosedList, viewOpenList, viewPath, viewDetails;

    // TODO: make names consistent
    @FXML
    private MenuItem runRunMenuItem, runSetStartMenuItem, runSetGoalMenuItem, runEnterStartMenuItem, runEnterGoalMenuItem, runPreprocessMenuItem, runShowResultsMenuItem;

    @FXML
    private Canvas gridCanvas, closedListCanvas, openListCanvas, pathCanvas, detailsCanvas;

    private Stage primaryStage;
    private MapHolder mapHolder;

    private MapController mapController;

    private DialogExecuter dialogExecuter = new DialogExecuter();

    private BooleanProperty mapModified = new SimpleBooleanProperty(false);

    /* ------- Initialisation ------- */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.primaryStage = (Stage) resources.getObject(null);

        //Init mapController;
        this.mapController = new MapController();

        //Init mapHolder
        this.mapHolder = new MapHolder(this.gridCanvas, this.closedListCanvas, this.openListCanvas, this.pathCanvas, this.detailsCanvas);

        final EventHandler<KeyEvent> keyEventHandler = e -> {
            if (e.getCode() == KeyCode.RIGHT) this.mapHolder.moveCamera(new Vector(1, 0));
            if (e.getCode() == KeyCode.LEFT) this.mapHolder.moveCamera(new Vector(-1, 0));
            if (e.getCode() == KeyCode.UP) this.mapHolder.moveCamera(new Vector(0, -1));
            if (e.getCode() == KeyCode.DOWN) this.mapHolder.moveCamera(new Vector(0, 1));
            e.consume();
        };

        mainPane.sceneProperty().addListener((observableScene, oldScene, newScene) -> {
            if (oldScene != null) oldScene.removeEventHandler(KeyEvent.KEY_PRESSED, keyEventHandler);
            if (newScene != null) newScene.addEventHandler(KeyEvent.KEY_PRESSED, keyEventHandler);
        });


        //Init Menu
        initEmptyMapMenuItem();
        initRandomMapMenuItem();
        initMazeMapMenuItem();
        initMazeRoomMapMenuItem();
        initSingleRoomMapMenuItem();
        initDoubleRoomMapMenuItem();
        initLoopRoomMapMenuItem();
        initOpenMapMenuItem();
        initSaveMapMenuItem();
        initEditMapMenuItem();
        initHeuristicToggleGroup();
        initMovingRuleToggleGroup();
        initShortestPathToggleGroup();
        initRunRunMenuItem();
        initRunSetStartPointMenuItem();
        initRunSetGoalPointMenuItem();
        initRunEnterStartPointMenuItem();
        initRunEnterGoalPointMenuItem();
        initPreprocessRunMenuItem();
        initRunShowResultsMenuItem();
        initViews();
        // initKeyEventListener();
    }

    private void initEmptyMapMenuItem() {
        emptyMapMenuItem.setOnAction(event -> {
            Vector dimension = dialogExecuter.executeMapDimensionDialog("New Empty Map");
            try {
                this.mapHolder.setMap(mapController.setEmptyMap(dimension));
                setEditMapMode();
            } catch (MapInitialisationException e) {
                e.printStackTrace();
                //Todo: emptyMapMenuItem.setOnAction - MapInitialisationException
            }
        });
    }


    private void initRandomMapMenuItem() {
        randomMapMenuItem.setOnAction(event -> {
            Tuple2<Vector, Double> params = dialogExecuter.executeRandomMapDialog();
            try {
                this.mapHolder.setMap(mapController.setRandomMap(params.getArg1(), params.getArg2()));
                setEditMapMode();
            } catch (MapInitialisationException e) {
                e.printStackTrace();
                //Todo: randomMapMenuItem.setOnAction - MapInitialisationException
            }
        });
    }

    private void initMazeMapMenuItem() {
        mazeMapMenuItem.setOnAction(event -> {
            Vector dimension = dialogExecuter.executeMapDimensionDialog("New Maze Map");
            try {
                this.mapHolder.setMap(mapController.setMazeMap(dimension));
                setEditMapMode();
            } catch (MapInitialisationException e) {
                e.printStackTrace();
                //Todo: mazeMapMenuItem.setOnAction - MapInitialisationException
            }
        });
    }

    private void initMazeRoomMapMenuItem() {
        mazeRoomMapMenuItem.setOnAction(event -> {
            Tuple2<Vector, Integer> params = dialogExecuter.executeRoomNumberMapDialog();
            try {

                this.mapHolder.setMap(mapController.setMazeRoomMap(params.getArg1(), params.getArg2()));
                setEditMapMode();
            } catch (MapInitialisationException e) {
                e.printStackTrace();
                //Todo: mazeRoomMapMenuItem.setOnAction - MapInitialisationException
            }
        });
    }

    private void initSingleRoomMapMenuItem() {
        singleRoomMapMenuItem.setOnAction(event -> {
            Vector dimension = dialogExecuter.executeMapDimensionDialog("New Single Room Map");
            try {
                this.mapHolder.setMap(mapController.setSingleRoomMap(dimension));
                setEditMapMode();
            } catch (MapInitialisationException e) {
                e.printStackTrace();
                //Todo: singleRoomMapMenuItem.setOnAction - MapInitialisationException
            }
        });
    }

    private void initDoubleRoomMapMenuItem() {
        doubleRoomMapMenuItem.setOnAction(event -> {
            Vector dimension = dialogExecuter.executeMapDimensionDialog("New Double Room Map");
            try {
                this.mapHolder.setMap(mapController.setDoubleRoomMap(dimension));
                setEditMapMode();
            } catch (MapInitialisationException e) {
                e.printStackTrace();
                //Todo: doubleRoomMapMenuItem.setOnAction - MapInitialisationException
            }
        });
    }

    private void initLoopRoomMapMenuItem() {
        loopRoomMapMenuItem.setOnAction(event -> {
            Vector dimension = dialogExecuter.executeMapDimensionDialog("New Loop Room Map");
            try {
                this.mapHolder.setMap(mapController.setLoopRoomMap(dimension));
                setEditMapMode();
            } catch (MapInitialisationException e) {
                e.printStackTrace();
                //Todo: loopRoomMapMenuItem.setOnAction - MapInitialisationException
            }
        });
    }

    private void initOpenMapMenuItem() {
        openMapMenuItem.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Map Files", "*.map"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));
            File selectedFile = fileChooser.showOpenDialog(this.primaryStage);
            if (selectedFile != null) {
                try {
                    this.mapHolder.setMap(mapController.loadMap(selectedFile));
                    setEditMapMode();
                } catch (MapInitialisationException e) {
                    e.printStackTrace();
                    //Todo: openMapMenuItem.setOnAction - MapInitialisationException
                }
            }
        });
    }

    private void initSaveMapMenuItem() {
        saveMapMenuItem.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Current Map");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Map Files", "*.map"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));
            File selectedFile = fileChooser.showSaveDialog(this.primaryStage);
            if (selectedFile != null) {
                this.mapController.saveMap(selectedFile);
            }
        });
    }

    private void initEditMapMenuItem() {
        editMapMenuItem.setOnAction(event -> setEditMapMode());
    }

    private void initHeuristicToggleGroup() {
        heuristicToggleGroup.selectedToggleProperty().addListener((ov, oldT, newT) -> {
            if (newT == this.zeroHeuristicMenuItem) this.mapController.setZeroHeuristic();
            if (newT == this.manhattanHeuristicMenuItem) this.mapController.setManhattanHeuristic();
            if (newT == this.gridHeuristicMenuItem) this.mapController.setGridHeuristic();
            if (newT == this.euclideanHeuristicMenuItem) this.mapController.setEuclideanHeuristic();
        });
        this.heuristicToggleGroup.selectToggle(this.gridHeuristicMenuItem);
    }

    private void initMovingRuleToggleGroup() {
        movingRuleToggleGroup.selectedToggleProperty().addListener((ov, oldT, newT) -> {
            if (newT == this.orthogonalOnlyMovingRuleMenuItem) this.mapController.setOrthogonalNeighborMovingRule();
            if (newT == this.cornerCuttingMovingRuleMenuItem) this.mapController.setAllNeighborMovingRule();
            if (newT == this.noCornerCuttingMovingRuleMenuItem) this.mapController.setUncutNeighborMovingRule();
            if (oldT != newT) mapModified.set(true);
        });
        this.movingRuleToggleGroup.selectToggle(this.noCornerCuttingMovingRuleMenuItem);
    }

    private void initShortestPathToggleGroup() {
        shortestPathToggleGroup.selectedToggleProperty().addListener((ov, oldT, newT) -> {
            if (newT == this.aStarShortestPathMenuItem) this.mapController.setAStarShortestPath();
            if (newT == this.aStarBBShortestPathMenuItem) this.mapController.setAStarBBShortestPath();
            if (newT == this.jpsShortestPathMenuItem) this.mapController.setJPSShortestPath();
            if (newT == this.jpsBBShortestPathMenuItem) this.mapController.setJPSBBShortestPath();
            if (newT == this.jpsPlusShortestPathMenuItem) this.mapController.setJPSPlusShortestPath();
            if (newT == this.jpsPlusBBShortestPathMenuItem) this.mapController.setJPSPlusBBShortestPath();
            if (oldT != newT) mapModified.set(true);
        });
        this.shortestPathToggleGroup.selectToggle(this.aStarShortestPathMenuItem);
    }

    private void initRunRunMenuItem() {
        runRunMenuItem.setOnAction(event -> {
            try {
                this.mapHolder.setShortestPath(this.mapController.runShortstPath(this.mapHolder.getStartPoint(), this.mapHolder.getGoalPoint()));
            } catch (NoPathFoundException noPathFoundException) {
                dialogExecuter.executeAlertDialog("No path found.", "There is no path between the chosen start and goal point.");
            }
        });

        runRunMenuItem.disableProperty().bind((runPreprocessMenuItem.disableProperty().and(this.mapHolder.hasGoalPoint.and(this.mapHolder.hasStartPoint))).not());
    }


    private void initRunSetStartPointMenuItem() {
        runSetStartMenuItem.setOnAction(event -> {
            this.mapHolder.setOnMouseClickedCallback((coordinate) -> {
                if (!this.mapHolder.isPassable(coordinate)) return;
                this.mapHolder.setStartPoint(coordinate);
                this.mapHolder.setOnMouseClickedCallback(null);
            });
        });
    }

    private void initRunSetGoalPointMenuItem() {
        runSetGoalMenuItem.setOnAction(event -> {
            this.mapHolder.setOnMouseClickedCallback((coordinate) -> {
                if (!this.mapHolder.isPassable(coordinate) || (this.mapHolder.getStartPoint() != null && this.mapHolder.getStartPoint().equals(coordinate)))
                    return;
                this.mapHolder.setGoalPoint(coordinate);
                this.mapHolder.setOnMouseClickedCallback(null);
            });
        });
    }


    private void initRunEnterStartPointMenuItem() {
        runEnterStartMenuItem.setOnAction(event -> {
            Vector coordinate = dialogExecuter.executePositionDialog("Enter Start Point");
            if (!this.mapHolder.isPassable(coordinate)) {
                dialogExecuter.executeAlertDialog("No valid point.", "This chosen point is invalid.");
                return;
            }
            this.mapHolder.setStartPoint(coordinate);
        });
    }

    private void initRunEnterGoalPointMenuItem() {
        runEnterGoalMenuItem.setOnAction(event -> {
            // TODO: abbrechen (X drücken) sollte keine Exception werfen
            Vector coordinate = dialogExecuter.executePositionDialog("Enter Goal Point");
            if (!this.mapHolder.isPassable(coordinate)) {
                dialogExecuter.executeAlertDialog("No valid point.", "This chosen point is invalid.");
                return;
            }
            this.mapHolder.setGoalPoint(coordinate);
        });
    }


    private void initPreprocessRunMenuItem() {
        BooleanBinding proprocessingNeeded = aStarShortestPathMenuItem.selectedProperty().or(jpsShortestPathMenuItem.selectedProperty());

        runPreprocessMenuItem.disableProperty().bind(proprocessingNeeded.or(mapModified.not()));

        runPreprocessMenuItem.setOnAction(event -> {
            this.mapController.preprocessShortestPath();
            dialogExecuter.executeAlertDialog("Preprocessing", "You successfully completed the proprocessing computation.");
            this.mapHolder.setOnMouseClickedCallback(null);
            mapModified.set(false);
        });
    }


    private void initRunShowResultsMenuItem() {
        runShowResultsMenuItem.disableProperty().bind(this.mapHolder.hasShortestPathResult.not());

        runShowResultsMenuItem.setOnAction(event -> {
            dialogExecuter.executeAlertDialog("Shortest Path Result", this.mapHolder.getShortestPathResult());
        });
    }





    /* ------- View Selects ------- */

    private void initViews() {


        closedListCanvas.visibleProperty().bind(viewClosedList.selectedProperty());
        openListCanvas.visibleProperty().bind(viewOpenList.selectedProperty());
        pathCanvas.visibleProperty().bind(viewPath.selectedProperty());
        detailsCanvas.visibleProperty().bind(viewDetails.selectedProperty());

        closedListCanvas.setMouseTransparent(true);
        openListCanvas.setMouseTransparent(true);
        pathCanvas.setMouseTransparent(true);
        detailsCanvas.setMouseTransparent(true);
    }


    /* ------- Mode Setter ------- */

    private void setEditMapMode() {
        this.mapHolder.setOnMouseClickedCallback((coordinate) -> {
            try {
                this.mapController.switchPassable(coordinate);
                this.mapHolder.updateField(coordinate);
                mapModified.set(true);
            } catch (InvalidCoordinateException e) {
                e.printStackTrace();
                //Todo: setEditMapMode.mapConroller.updateField - InvalidCoordinateException
            }
        });
        mapModified.set(true);
        this.mapHolder.refreshMap();
    }



    /* ------- Callbacks ------- */

    private interface OnStartGoalSetCallback {
        void call(Vector start, Vector goal);
    }
}