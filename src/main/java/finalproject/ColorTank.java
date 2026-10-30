/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

import java.io.Serializable;

/**
 * This class store the color
 * @author minht
 */
public class ColorTank implements Serializable{

    private int red;
    private int blue;
    private int green;

    public ColorTank() {
        this.red = 0;
        this.green = 0;
        this.blue = 0;
    }

    /**
     * Constructor
     * @param red red color
     * @param green green color
     * @param blue blue color
     */
    public ColorTank(int red, int green, int blue) {
        setRed(red);
        setGreen(green);
        setBlue(blue);
    }
    
    /**
     * Set red color
     * @param red red color
     */
    public void setRed(int red){
        this.red = red;
    }
    
    /**
     * Set green color
     * @param green green color
     */
    public void setGreen(int green){
        this.green = green;
    }
    
    /**
     * Set blue color
     * @param blue blue color
     */
    public void setBlue(int blue){
        this.blue = blue;
    }
    
    /**
     * Get red color
     * @return red color
     */
    public int getRed(){
        return red;
    }
    
    /**
     * Get green color
     * @return green color
     */
    public int getGreen(){
        return green;
    }
    
    /**
     * Get blue color
     * @return blue color
     */
    public int getBlue(){
        return blue;
    }
}
