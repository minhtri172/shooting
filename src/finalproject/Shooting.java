/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

/**
 * Control movement of the bullet
 * @author minht
 */
public interface Shooting {

    /**
     * Shoot the bullet
     */
    public abstract void shoot();

    /**
     * Shoot up
     */
    public abstract void shootUp();

     /**
     * Shoot down
     */
    public abstract void shootDown();

     /**
     * Shoot right
     */
    public abstract void shootRight();

     /**
     * Shoot left
     */
    public abstract void shootLeft();
}
