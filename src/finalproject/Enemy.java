/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

import java.io.Serializable;
import java.util.ArrayList;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * This class create enemy shape, control movement of enemy.
 *
 * @author minht
 */
public class Enemy extends Tank implements Shooting {

    private Random direct;
    private Random positionX;
    private Random positionY;
    private boolean isSetDirection = false;
    private boolean isStop = false;
    private int timeShoot = 0;

    /**
     * Create enemy
     *
     * @param xPos x position of player
     * @param yPos y position of player
     * @param width width of player
     * @param height height of player
     * @param radius radius of the bullet (circle)
     */
    public Enemy(int xPos, int yPos, int width, int height, int radius) {
        shape = new Rectangle(xPos, yPos, width, height);
        color = new ColorTank(255, 0, 0);
        shape.setFill(Color.rgb(color.getRed(), color.getGreen(), color.getBlue()));
        speed = 1;
        this.width = width;
        this.height = height;
        this.radiusBullet = radius;
        direct = new Random();
        positionX = new Random();
        positionY = new Random();

        xBullet = (int) shape.getX();
        yBullet = (int) shape.getY();

    }

    /**
     * Shoot bullet (Up, down, left, right)
     */
    @Override
    public void shoot() {
        if (isUp) {
            bulletUp.add(new Circle(xBullet + width / 2, yBullet + height / 2, radiusBullet));
            pane.getChildren().add(bulletUp.get(countUp));
            countUp++;
            shootUp();
        }

        if (isDown) {
            bulletDown.add(new Circle(xBullet + width / 2, yBullet + height / 2, radiusBullet));
            pane.getChildren().add(bulletDown.get(countDown));
            countDown++;
            shootDown();
        }

        if (isLeft) {
            bulletLeft.add(new Circle(xBullet + width / 2, yBullet + height / 2, radiusBullet));
            pane.getChildren().add(bulletLeft.get(countLeft));
            countLeft++;
            shootLeft();
        }

        if (isRight) {
            bulletRight.add(new Circle(xBullet + width / 2, yBullet + height / 2, radiusBullet));
            pane.getChildren().add(bulletRight.get(countRight));
            countRight++;
            shootRight();
        }
    }

    /**
     * Direction up of bullet. Delete bullet when it hit top.
     */
    @Override
    public void shootUp() {
        for (int i = 0; i < countUp; i++) {
            if (!bulletUp.isEmpty()) {
                bulletUp.get(i).setCenterY(bulletUp.get(i).getCenterY() - (speed + 2));
                if (bulletUp.get(i).getCenterY() < 0) {
                    pane.getChildren().remove(bulletUp.get(0));
                    bulletUp.remove(0);
                    countUp--;
                }
            }
        }
    }

    /**
     * Direction down of bullet. Delete bullet when it hit bottom.
     */
    @Override
    public void shootDown() {
        for (int i = 0; i < countDown; i++) {
            if (!bulletDown.isEmpty()) {
                bulletDown.get(i).setCenterY(bulletDown.get(i).getCenterY() + (speed + 2));
                if (bulletDown.get(i).getCenterY() > heightScene) {
                    pane.getChildren().remove(bulletDown.get(0));
                    bulletDown.remove(0);
                    countDown--;
                }
            }
        }
    }

    /**
     * Direction left of bullet. Delete bullet when it hit left.
     */
    @Override
    public void shootLeft() {
        for (int i = 0; i < countLeft; i++) {
            if (!bulletLeft.isEmpty()) {
                bulletLeft.get(i).setCenterX(bulletLeft.get(i).getCenterX() - (speed + 2));
                if (bulletLeft.get(i).getCenterX() < 0) {
                    pane.getChildren().remove(bulletLeft.get(0));
                    bulletLeft.remove(0);
                    countLeft--;
                }
            }
        }
    }

    /**
     * Direction right of bullet. Delete bullet when it hit right.
     */
    @Override
    public void shootRight() {
        for (int i = 0; i < countRight; i++) {
            if (!bulletRight.isEmpty()) {
                bulletRight.get(i).setCenterX(bulletRight.get(i).getCenterX() + (speed + 2));
                if (bulletRight.get(i).getCenterX() > widthScene) {
                    pane.getChildren().remove(bulletRight.get(0));
                    bulletRight.remove(0);
                    countRight--;
                }
            }
        }
    }

    // Mutator 
    /**
     * Set speed of enemy.
     *
     * @param speed speed of enemy.
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * Set enemy stop
     *
     * @param stop enemy stop moving
     */
    public void setIsStop(boolean stop) {
        this.isStop = stop;
    }

    /**
     * Set time to shoot
     */
    public void setTimeShoot() {
        timeShoot++;
    }

    /**
     * Set true if direction is set
     *
     * @param isSetDirection Check if the setDirection is set or not
     */
    public void setIsSetDirection(boolean isSetDirection) {
        this.isSetDirection = isSetDirection;
    }

    /**
     * Set random numbers
     *
     * @param min min value
     * @param max max value
     */
    public void setRandomNumber(int min, int max) {
        direct.setRandomNumber(min, max);
    }

    /**
     * Set X position random
     *
     * @param min min value
     * @param max max value
     */
    public void setPositionX(int min, int max) {
        positionX.setRandomNumber(min, max);
    }

    /**
     * Set X position random
     *
     * @param min min value
     * @param max max value
     */
    public void setPositionY(int min, int max) {
        positionY.setRandomNumber(min, max);
    }

    /**
     * Set true if the enemy shoot
     *
     * @param isShoot check if the enemy shoot a bullet
     */
    public void setIsShoot(boolean isShoot) {
        this.isShoot = isShoot;
    }

    /**
     * Set true if the player go left
     *
     * @param isLeft check if enemy move left
     */
    public void setIsLeft(boolean isLeft) {
        this.isLeft = isLeft;
    }

    /**
     * Set true if the player go right
     *
     * @param isRight check if enemy move right
     */
    public void setIsRight(boolean isRight) {
        this.isRight = isRight;
    }

    /**
     * Set true if the player go up
     *
     * @param isUp check if enemy move up
     */
    public void setIsUp(boolean isUp) {
        this.isUp = isUp;
    }

    /**
     * Set true if the player go down
     *
     * @param isDown check if enemy move down
     */
    public void setIsDown(boolean isDown) {
        this.isDown = isDown;
    }

    /**
     * Set pane that add the player node
     *
     * @param pane pane contains the enemy
     */
    public void setPane(Pane pane) {
        this.pane = pane;
    }

    /**
     * Set width scene
     *
     * @param width width of scene
     */
    public void setWidthScene(int width) {
        this.widthScene = width;
    }

    /**
     * Set height Scene
     *
     * @param height height of scene
     */
    public void setHeightScene(int height) {
        this.heightScene = height;
    }

    // Accessor 
    /**
     * Get Speed
     *
     * @return speed
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Get true if the bullet is shoot
     *
     * @return isStop
     */
    public boolean getIsStop() {
        return isStop;
    }

    /**
     * Get time shoot
     *
     * @return the time to shoot
     */
    public int getTimeShoot() {
        return timeShoot;
    }

    /**
     * Return true if the direction is set
     *
     * @return isSetDirection
     */
    public boolean getIsSetDirection() {
        return isSetDirection;
    }

    /**
     * Return the direct (left, right, up, down)
     *
     * @return direction
     */
    public Random getDirection() {
        return direct;
    }

    /**
     * Return position X
     *
     * @return positionX
     */
    public Random getPositionX() {
        return positionX;
    }

    /**
     * Return position Y
     *
     * @return positionY
     */
    public Random getPositionY() {
        return positionY;
    }

    /**
     * Return true if the enemy shoot
     *
     * @return isShoot
     */
    public boolean getIsShoot() {
        return isShoot;
    }

    /**
     * Return true if the player go left
     *
     * @return isLeft
     */
    public boolean getIsLeft() {
        return isLeft;
    }

    /**
     * Return true if the player go right
     *
     * @return isRight
     */
    public boolean getIsRight() {
        return isRight;
    }

    /**
     * Return true if the player go up
     *
     * @return isUp
     */
    public boolean getIsUp() {
        return isUp;
    }

    /**
     * Return true if the player go down
     *
     * @return isDown
     */
    public boolean getIsDown() {
        return isDown;
    }

    /**
     * Get enemy's shape
     *
     * @return shape
     */
    public Rectangle getShape() {
        return shape;
    }

    /**
     * Get bullets (shoot up)
     *
     * @return bulletUp
     */
    public ArrayList<Circle> getUp() {
        return bulletUp;
    }

    /**
     * Get bullets (shoot down)
     *
     * @return bulletDown
     */
    public ArrayList<Circle> getDown() {
        return bulletDown;
    }

    /**
     * Get bullets (shoot left)
     *
     * @return bulletLeft
     */
    public ArrayList<Circle> getLeft() {
        return bulletLeft;
    }

    /**
     * Get bullets (shoot right)
     *
     * @return bulletRight
     */
    public ArrayList<Circle> getRight() {
        return bulletRight;
    }

    /**
     * Get Pane
     *
     * @return pane
     */
    public Pane getPane() {
        return pane;
    }

    // Overwrite function
    /**
     * Enemy Move left.
     */
    @Override
    public void moveLeft() {

        shape.setX(shape.getX() - speed);
        xBullet = (int) shape.getX();

    }

    /**
     * Enemy Move right.
     */
    @Override
    public void moveRight() {

        shape.setX(shape.getX() + speed);
        xBullet = (int) shape.getX();

    }

    /**
     * Enemy Move up.
     */
    @Override
    public void moveUp() {

        shape.setY(shape.getY() - speed);
        yBullet = (int) shape.getY();

    }

    /**
     * Enemy Move down.
     */
    @Override
    public void moveDown() {

        shape.setY(shape.getY() + speed);
        yBullet = (int) shape.getY();

    }
}
