/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

import java.io.Serializable;

/**
 * This class hold the integer
 * @author minht
 */
public class IntHolder implements Serializable {

    private int value = 0;

    public IntHolder() {

    }

    public IntHolder(int value) {
        setValue(value);
    }

    /**
     * Set value
     * @param value value
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Get value
     * @return value 
     */
    public int getValue() {
        return value;
    }
}
