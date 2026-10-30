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
 * This class create player shape, control movement of player.
 *
 * @author minht
 */
public class Player extends Tank implements Shooting{

    private boolean playerShoot = false;

    /**
     * Create player
     *
     * @param xPos x position of player
     * @param yPos y position of player
     * @param width width of player
     * @param height height of player
     * @param radius radius of the bullet (circle)
     */
    public Player(int xPos, int yPos, int width, int height, int radius) {
        shape = new Rectangle(xPos, yPos, width, height);
        color = new ColorTank(255,255,0);
        shape.setFill(Color.rgb(color.getRed(), color.getGreen(), color.getBlue()));
        speed = 2;
        this.width = width;
        this.height = height;
        this.radiusBullet = radius;

        xBullet = (int) shape.getX();
        yBullet = (int) shape.getY();

    }

    /**
     * Shoot bullet (Up, down, left, right)
     */
    @Override
    public void shoot() {

        // Shoot the bullet up
        if (isShootUp) {
            if (!playerShoot) {
                bulletUp.add(new Circle(xBullet + width / 2, yBullet + height / 2, radiusBullet));
                pane.getChildren().add(bulletUp.get(countUp));
                countUp++;
                shootUp();
            }
        }

        // Shoot the bullet down
        if (isShootDown) {
            if (!playerShoot) {
                bulletDown.add(new Circle(xBullet + width / 2, yBullet + height / 2, radiusBullet));
                pane.getChildren().add(bulletDown.get(countDown));
                countDown++;
                shootDown();
            }
        }

        // Shoot the bullet left
        if (isShootLeft) {
            if (!playerShoot) {
                bulletLeft.add(new Circle(xBullet + width / 2, yBullet + height / 2, radiusBullet));
                pane.getChildren().add(bulletLeft.get(countLeft));
                countLeft++;
                shootLeft();
            }
        }

        // Shoot the bullet right
        if (isShootRight) {
            if (!playerShoot) {
                bulletRight.add(new Circle(xBullet + width / 2, yBullet + height / 2, radiusBullet));
                pane.getChildren().add(bulletRight.get(countRight));
                countRight++;
                shootRight();
            }
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
                    playerShoot = false;
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
                    playerShoot = false;
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
                    playerShoot = false;
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
                    playerShoot = false;
                    pane.getChildren().remove(bulletRight.get(0));
                    bulletRight.remove(0);
                    countRight--;
                }
            }
        }
    }

    // Mutator 
    /**
     * Reduce 1 bullet (left)
     */
    public void setXBullet(){
        this.xBullet = (int) shape.getX();
    }
    
    public void setYBullet(){
        this.yBullet = (int) shape.getY();
    }
    public void reduceCountLeft() {
        countLeft--;
    }

    /**
     * Reduce 1 bullet (right)
     */
    public void reduceCountRight() {
        countRight--;
    }

    /**
     * Reduce 1 bullet (up)
     */
    public void reduceCountUp() {
        countUp--;
    }

    /**
     * Reduce 1 bullet (down)
     */
    public void reduceCountDown() {
        countDown--;
    }

    /**
     * Set true if the player shoot
     * @param shoot check if player shoot a bullet
     */
    public void setPlayerShoot(boolean shoot) {
        this.playerShoot = shoot;
    }

    /**
     * Set true if the bullet is launch
     *
     * @param isShoot check if the bullet is shoot
     */
    public void setIsShoot(boolean isShoot) {
        this.isShoot = isShoot;
    }

    /**
     * Set true if the player go left
     *
     * @param isLeft check if player move left
     */
    public void setIsLeft(boolean isLeft) {
        this.isLeft = isLeft;
    }

    /**
     * Set true if the player go right
     *
     * @param isRight if player move right
     */
    public void setIsRight(boolean isRight) {
        this.isRight = isRight;
    }

    /**
     * Set true if the player go up
     *
     * @param isUp check if player move up
     */
    public void setIsUp(boolean isUp) {
        this.isUp = isUp;
    }

    /**
     * Set true if the player go down
     *
     * @param isDown check if player move down
     */
    public void setIsDown(boolean isDown) {
        this.isDown = isDown;
    }

    /**
     * Set pane that add the player node
     *
     * @param pane pane
     */
    public void setPane(Pane pane) {
        this.pane = pane;
    }

    /**
     * Set true if the bullet shoot up
     *
     * @param isShootUp check if the bullets go up
     */
    public void setIsShootUp(boolean isShootUp) {
        this.isShootUp = isShootUp;
    }

    /**
     * Set true if the bullet shoot down
     *
     * @param isShootDown check if the bullets go down
     */
    public void setIsShootDown(boolean isShootDown) {
        this.isShootDown = isShootDown;
    }

    /**
     * Set true if the bullet shoot left
     *
     * @param isShootLeft check if the bullets go left
     */
    public void setIsShootLeft(boolean isShootLeft) {
        this.isShootLeft = isShootLeft;
    }

    /**
     * Set true if the bullet shoot right
     *
     * @param isShootRight check if the bullets go right
     */
    public void setIsShootRight(boolean isShootRight) {
        this.isShootRight = isShootRight;
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
     * Get player's shape
     *
     * @return player's shape
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
     * Get pane
     *
     * @return pane
     */
    public Pane getPane() {
        return pane;
    }

    /**
     * Return true if the bullet is shoot
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
     * Return true if the player go up.
     *
     * @return isUp
     */
    public boolean getIsUp() {
        return isUp;
    }

    /**
     * Return true if the player go down.
     *
     * @return isDown
     */
    public boolean getIsDown() {
        return isDown;
    }

    // Overwrite function
    /**
     * Player Move left.
     */
    @Override
    public void moveLeft() {
        if (shape.getX() > 0) {
            shape.setX(shape.getX() - speed);
            xBullet = (int) shape.getX();
        }
    }

    /**
     * Player Move right.
     */
    @Override
    public void moveRight() {
        if (shape.getX() < widthScene - width) {
            shape.setX(shape.getX() + speed);
            xBullet = (int) shape.getX();
        }
    }

    /**
     * Player Move up.
     */
    @Override
    public void moveUp() {
        if (shape.getY() > 0) {
            shape.setY(shape.getY() - speed);
            yBullet = (int) shape.getY();
        }
    }

    /**
     * Player Move down.
     */
    @Override
    public void moveDown() {
        if (shape.getY() < heightScene - height) {
            shape.setY(shape.getY() + speed);
            yBullet = (int) shape.getY();
        }
    }

}
