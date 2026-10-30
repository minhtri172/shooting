/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class create the input file and output file. Throws exception if the
 * file's name is not a binary file
 *
 * @author minht
 */
public class DataGame {

    private String fileInputName;
    private String fileOutputName;
    private FileInputStream input;

    /**
     * Constructor Data Game
     *
     * @param fileInputName input file's name
     * @param fileOutputName output file's name
     */
    public DataGame(String fileInputName, String fileOutputName) throws IllegalArgumentException {
        setFileInputName(fileInputName);
        setFileOutputName(fileOutputName);
    }

    /**
     * Default Constructor
     */
    DataGame() {

    }

    /**
     * Set input file's name
     *
     * @param fileName input file's name
     */
    public void setFileInputName(String fileName) throws IllegalArgumentException {
        if (fileName.contains(".")) {
            if (fileName.substring(fileName.lastIndexOf(".")).equals(".dat")) {
                this.fileInputName = fileName;
            } else {
                throw new IllegalArgumentException("Invalid File Name.\nFile must be \"name.dat\"");
            }
        } else {
            throw new IllegalArgumentException("Invalid File Name.\nFile must be \"name.dat\"");
        }
    }

    /**
     * Set output file's name
     *
     * @param fileName output file's name
     */
    public void setFileOutputName(String fileName) throws IllegalArgumentException {
        if (fileName.contains(".")) {
            if (fileName.substring(fileName.lastIndexOf(".")).equals(".dat")) {
                this.fileOutputName = fileName;
            } else {
                throw new IllegalArgumentException("Invalid File Name.\nFile must be \"name.dat\"");
            }
        } else {
            throw new IllegalArgumentException("Invalid File Name.\nFile must be \"name.dat\"");
        }
    }

    /**
     * Save game
     *
     * @param p player
     * @param e enemy
     * @param sizeEnemy the size of enemies
     * @param countLife the point of life that player has
     * @param colorPlayer color of player
     * @param colorEnemy color of enemy
     * @param speed speed of enemy
     * @throws java.io.IOException file does not exist or disconnected
     */
    public void save(Player p, ArrayList<Enemy> e, IntHolder sizeEnemy, IntHolder tempEnemy,
            IntHolder countLife, ColorTank colorPlayer, ColorTank colorEnemy, IntHolder speed) throws IOException {

        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileOutputName));) {
            // Output values to the file
            output.writeObject(sizeEnemy);
            output.writeObject(tempEnemy);
            output.writeObject(countLife);

            output.writeInt((int) p.getShape().getX());
            output.writeInt((int) p.getShape().getY());
            output.writeObject(colorPlayer);
            output.writeObject(colorEnemy);
            output.writeObject(speed);
            e.forEach((enemy) -> {
                try {
                    output.writeInt((int) enemy.getShape().getX());
                    output.writeInt((int) enemy.getShape().getY());
                } catch (IOException ex) {
                    Logger.getLogger(DataGame.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

        }

    }

    /**
     * Load game
     *
     * @param p player
     * @param e enemy
     * @param sizeEnemy the size of enemies
     * @param countLife the point of life that player has
     * @param colorPlayer color of player
     * @param colorEnemy color of enemy
     * @param speed speed of enemy
     * @throws IOException file does not exist or disconnected
     * @throws java.lang.ClassNotFoundException Class not exist
     */
    public void load(Player p, ArrayList<Enemy> e, IntHolder sizeEnemy, IntHolder tempEnemy,
            IntHolder countLife, ColorTank colorPlayer, ColorTank colorEnemy, IntHolder speed) throws IOException, ClassNotFoundException {

        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(fileInputName));) {
            // Read values from the file         
            int count = 0;
            IntHolder sizeE = (IntHolder) (input.readObject());
            IntHolder tempE = (IntHolder) (input.readObject());
            IntHolder countL = (IntHolder) (input.readObject());
            sizeEnemy.setValue(sizeE.getValue());
            tempEnemy.setValue(tempE.getValue());
            countLife.setValue(countL.getValue());
            p.getShape().setX(input.readInt());
            p.getShape().setY(input.readInt());

            ColorTank temp1 = (ColorTank) (input.readObject());
            colorPlayer.setRed(temp1.getRed());
            colorPlayer.setGreen(temp1.getGreen());
            colorPlayer.setBlue(temp1.getBlue());
            
            ColorTank temp2 = (ColorTank) (input.readObject());
            colorEnemy.setRed(temp2.getRed());
            colorEnemy.setGreen(temp2.getGreen());
            colorEnemy.setBlue(temp2.getBlue());

            IntHolder temp3 = (IntHolder) (input.readObject());
            speed.setValue(temp3.getValue());
            
            while (count < sizeEnemy.getValue()) {
                int x = input.readInt();
                int y = input.readInt();
                e.add(new Enemy(x, y, 50, 50, 5));
                count++;
            }

        }
    }
}
