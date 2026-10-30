/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class create a random number in the range
 * @author minht
 */
public class Random implements Serializable{

    private int randomNum;
    private int min, max;

    /**
     * Default constructor
     */
    public Random() {
        setRandomNumber(0, 0);
    }

    /**
     * Constructor
     * @param min min value
     * @param max max value
     */
    public Random(int min, int max) {
        setRandomNumber(min, max);
    }

    /**
     * Create a random number
     * @param min min value
     * @param max max value
     */
    public void setRandomNumber(int min, int max) {
        setMinValue(min);
        setMaxValue(max);
        randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    /**
     * Set min value
     * @param min min value
     */
    public void setMinValue(int min) {
        this.min = min;
    }

    /**
     * Set max value
     * @param max max value
     */
    public void setMaxValue(int max) {
        this.max = max;
    }

    /**
     * 
     * @return a random number 
     */
    public int getRandomNumber() {
        return randomNum;
    }
}
