/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

import java.util.ArrayList;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * This class is abstract class of player class and enemy class
 * @author minht
 */
public abstract class Tank {

    protected int countUp = 0; // Count the number of bullets go up
    protected int countDown = 0;// Count the number of bullets go down
    protected int countLeft = 0;// Count the number of bullets go left
    protected int countRight = 0;// Count the number of bullets go right
    protected ArrayList<Circle> bulletUp = new ArrayList<Circle>(); // Bullets go up
    protected ArrayList<Circle> bulletDown = new ArrayList<Circle>();// Bullets go down
    protected ArrayList<Circle> bulletLeft = new ArrayList<Circle>();// Bullets go left
    protected ArrayList<Circle> bulletRight = new ArrayList<Circle>();// Bullets go right
    protected Rectangle shape; // Shape
    protected int speed; // Speed
    protected ColorTank color; // Color

    protected int xBullet = 0; // Position x of bullet
    protected int yBullet = 0; // Position y of bullet
    protected int radiusBullet; // Radius of bullet
    protected int widthScene; // Width of scene
    protected int heightScene; // Height of scene

    protected boolean isShoot = false; // True if the bullet is shoot
    protected boolean isShootUp = false; // True if the bullet is shoot up
    protected boolean isShootDown = false;// True if the bullet is shoot down
    protected boolean isShootLeft = false;// True if the bullet is shoot left
    protected boolean isShootRight = false;// True if the bullet is shoot right
    protected boolean isLeft = false; // True if move left
    protected boolean isRight = false;// True if move right
    protected boolean isUp = false;// True if move up
    protected boolean isDown = false;// True if move down
    protected Pane pane; // Playing Pane
    protected int width, height; // width and height of the shape (Rectangle)

    /**
     * Move left
     */
    public abstract void moveLeft();

    /**
     * Move right
     */
    public abstract void moveRight();

    /**
     * Move Up
     */
    public abstract void moveUp();

    /**
     * Move Down
     */
    public abstract void moveDown();

}
