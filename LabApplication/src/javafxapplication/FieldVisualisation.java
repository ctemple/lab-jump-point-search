package javafxapplication;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Created by paloka on 01.06.16.
 */
enum FieldVisualisation {
    GRID_POINT("#FFFFFF"),
    OBSTACLE_POINT("#A1A1A1"),

    VISITED_POINT("#D9F0FF"),
    JUMP_POINT("#2298E6"),

    GOAL_POINT("#730B19"),
    START_POINT("#730B19"),
    PATH_POINT("#A12D3C");

    private Color color;

    FieldVisualisation(String paint) {
        color = (Color) Paint.valueOf(paint);
    }

    Color getColor() {
        return color;
    }
}