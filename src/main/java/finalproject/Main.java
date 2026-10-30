/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * Main function
 *
 * @author minht
 */
public class Main extends Application {

    private final int LEFT = 0; // Direction left
    private final int RIGHT = 1; // Direction right
    private final int UP = 2;// Direction up
    private final int DOWN = 3;// Direction down
    private final int WIDTH_REC = 50;
    private final int HEIGHT_REC = 50;
    private final int RADIUS_BULLET = 5;

    private final int WIDTH_SCENE = 800;
    private final int HEIGHT_SCENE = 600;
    private final Player player = new Player(WIDTH_SCENE / 2, HEIGHT_SCENE - 100, WIDTH_REC, HEIGHT_REC, RADIUS_BULLET);
    private final ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    private final BorderPane root = new BorderPane();

    private final Pane playPane = new Pane();
    private Timeline animation;
    private final IntHolder sizeEnemy = new IntHolder(1); // Hold number of enemies
    private final IntHolder countLife = new IntHolder(2); // Hold number of life points
    private final IntHolder speed = new IntHolder(1); // Hold speed of enemies
    private final IntHolder countEnemy = new IntHolder(1); // Hold number of enemies after be killed
    private final IntHolder tempEnemy = new IntHolder(); // Remember number of enemies

    private final ArrayList<Image> lifeImage = new ArrayList<Image>();
    private final Image endGame = new Image("images/end.png");
    private final Image gameOver = new Image("images/gameover.png");
    private final Image guide = new Image("images/keyboard.jpg");
    private final ImageView imageView1 = new ImageView();
    private final ImageView imageView2 = new ImageView();
    private final ImageView imageView3 = new ImageView();
    private final ImageView imageView4 = new ImageView();
    private final ImageView dislayEndGame = new ImageView();
    private final DataGame data = new DataGame();
    private int redPlayer = 255, greenPlayer = 255, bluePlayer = 0;
    private int redEnemy = 255, greenEnemy = 0, blueEnemy = 0;
    private boolean isPause = false; // Check if user pause game
    private boolean isLoad = false; // Check if user click load game

    private ColorTank colorEnemy = new ColorTank();
    private ColorTank colorPlayer = new ColorTank();

    /**
     * Control the game
     *
     * @param primaryStage primary stage
     */
    @Override
    public void start(Stage primaryStage) {

        // When playPane is clicked
        playPane.setOnMouseClicked(e -> {
            animation.play();
            playPane.requestFocus(); // Set focus on playPane
        });

        // Create enemies
        createEnemy();

        // Create Player
        player.setHeightScene(HEIGHT_SCENE);
        player.setWidthScene(WIDTH_SCENE);
        player.setPane(playPane);
        playPane.getChildren().add(player.getShape());

        // Set root pane       
        root.setTop(getMenuBar(primaryStage));
        root.setRight(getLifeBox());
        root.setCenter(playPane);

        // Animation 
        animation = new Timeline(new KeyFrame(Duration.millis(10), e -> {

            for (Enemy enemy : new ArrayList<>(enemies)) {

                // Player is killed
                playerKilled(enemy);

                // Enemy is killed
                enemyKilled(enemy);

                // Control enemy
                controlEnemy(enemy);
            }

            // Control Player
            controlPlayer();

            // Next game 
            if (enemies.isEmpty()) {
                player.getShape().setX(WIDTH_SCENE / 2);
                player.getShape().setY(HEIGHT_SCENE - 100);
                if (isLoad) {
                    sizeEnemy.setValue(tempEnemy.getValue());
                    countEnemy.setValue(sizeEnemy.getValue());
                    isLoad = false;
                }
                sizeEnemy.setValue(sizeEnemy.getValue() + 1);
                countEnemy.setValue(sizeEnemy.getValue());
                createEnemy();

                enemies.forEach((enemy) -> {
                    enemy.getShape().setFill(Color.rgb(redEnemy, greenEnemy, blueEnemy));
                });

            }

            // End game
            if (sizeEnemy.getValue() == 8) {
                root.getChildren().clear();
                root.setTop(getMenuBar(primaryStage));
                dislayEndGame.setImage(endGame);
                root.setCenter(dislayEndGame);
                animation.stop();
            }

            // Game over
            if (countLife.getValue() <= -1) {
                root.getChildren().clear();
                root.setTop(getMenuBar(primaryStage));
                dislayEndGame.setImage(gameOver);
                root.setCenter(dislayEndGame);
                animation.stop();
            }
        }));

        animation.setCycleCount(Timeline.INDEFINITE);
        animation.pause();

        // Create a scene 
        Scene scene = new Scene(root, WIDTH_SCENE, HEIGHT_SCENE + HEIGHT_REC);
        primaryStage.setTitle("Hit and Run");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Must request focus after the primary stage is displayed
        playPane.requestFocus();
    }

    /**
     * Create Enemy
     */
    public void createEnemy() {
        Random randomPostionX = new Random();
        Random randomPostionY = new Random();

        for (int i = 0; i < sizeEnemy.getValue(); i++) {
            randomPostionX.setRandomNumber(0, WIDTH_SCENE - WIDTH_REC);
            randomPostionY.setRandomNumber(0, HEIGHT_SCENE - HEIGHT_REC - 150);

            enemies.add(new Enemy(randomPostionX.getRandomNumber(), randomPostionY.getRandomNumber(), WIDTH_REC, HEIGHT_REC, RADIUS_BULLET));
            enemies.get(i).setHeightScene(HEIGHT_SCENE);
            enemies.get(i).setWidthScene(WIDTH_SCENE);
            enemies.get(i).setPane(playPane);
            enemies.get(i).setSpeed(speed.getValue());

            enemies.get(i).getShape().setFill(Color.rgb(redEnemy, greenEnemy, blueEnemy));

            playPane.getChildren().add(enemies.get(i).getShape());
        }
    }

    /**
     * Create life point
     *
     * @return HBox
     */
    public HBox getLifeBox() {

        HBox lifeBox = new HBox();
        lifeBox.setSpacing(10);
        lifeBox.setPadding(new Insets(20, 20, 20, 20));
        for (int i = 0; i < 3; i++) {
            lifeImage.add(new Image("images/heart.png"));
        }

        imageView1.setImage(lifeImage.get(0));
        imageView2.setImage(lifeImage.get(1));
        imageView3.setImage(lifeImage.get(2));

        imageView1.setFitHeight(50);
        imageView1.setFitWidth(50);
        imageView2.setFitHeight(50);
        imageView2.setFitWidth(50);
        imageView3.setFitHeight(50);
        imageView3.setFitWidth(50);

        lifeBox.getChildren().addAll(imageView1, imageView2, imageView3);
        return lifeBox;
    }

    /**
     * Create menu bar
     *
     * @param primaryStage primary stage
     * @return menu bar
     */
    public MenuBar getMenuBar(Stage primaryStage) {
        MenuBar menuBar = new MenuBar();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());

        // File menu - new, save, load, exit
        Menu fileMenu = new Menu("File");
        MenuItem newMenuItem = new MenuItem("New Game");
        MenuItem saveMenuItem = new MenuItem("Save");
        MenuItem loadMenuItem = new MenuItem("Load");
        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(actionEvent -> Platform.exit());

        fileMenu.getItems().addAll(newMenuItem, saveMenuItem, loadMenuItem, new SeparatorMenuItem(), exitMenuItem);

        Menu settingMenu = new Menu("Setting");
        MenuItem changeColorMenuItem = new MenuItem("Change Color");

        // Speed
        Menu speedEnemy = new Menu("Change Speed Enemy");
        ToggleGroup speedGroup = new ToggleGroup();

        RadioMenuItem speed1 = new RadioMenuItem("Easy");
        RadioMenuItem speed2 = new RadioMenuItem("Normal");
        RadioMenuItem speed3 = new RadioMenuItem("Hard");
        speed1.setSelected(true);
        speedEnemy.getItems().addAll(speed1, speed2, speed3);
        speed1.setToggleGroup(speedGroup);
        speed2.setToggleGroup(speedGroup);
        speed3.setToggleGroup(speedGroup);

        // Add item to menu
        settingMenu.getItems().addAll(changeColorMenuItem, speedEnemy);

        // Help
        Menu helpMenu = new Menu("Help");
        MenuItem helpMenuItem = new MenuItem("How to play");
        helpMenu.getItems().add(helpMenuItem);
        helpMenuItem.setOnAction(e -> {
            Stage window = new Stage();
            animation.pause();
            isPause = true;
            imageView4.setImage(guide);
            StackPane pane = new StackPane();
            pane.getChildren().add(imageView4);
            window.setTitle("How to play");
            window.setScene(new Scene(pane, 600, 400));
            window.show();
        });
        // Add menu to menu bar
        menuBar.getMenus().addAll(fileMenu, settingMenu, helpMenu);

        // Set menu actions        
        // Color Player
        changeColorMenuItem.setOnAction(e -> {
            animation.pause();
            isPause = true;
            Stage window = new Stage();
            GridPane pane = new GridPane();

            pane.setHgap(30);
            pane.setVgap(10);
            pane.setAlignment(Pos.TOP_CENTER);
            Rectangle r1 = new Rectangle(50, 50, 50, 50);
            Rectangle r2 = new Rectangle(50, 50, 50, 50);

            pane.getColumnConstraints().add(new ColumnConstraints(100)); // column 0 is 100 wide
            pane.getColumnConstraints().add(new ColumnConstraints(400)); // column 1 is 200 wide

            pane.add(new Label("Player"), 1, 0);

            // Red
            pane.add(new Label("Red: "), 0, 1);
            Slider slRedPlayer = new Slider();
            slRedPlayer.setBlockIncrement(1);
            slRedPlayer.setValue(0);
            slRedPlayer.setShowTickLabels(true);
            slRedPlayer.setMin(0);
            slRedPlayer.setMax(255);
            pane.add(slRedPlayer, 1, 1);

            // Blue
            pane.add(new Label("Blue: "), 0, 3);
            Slider slBluePlayer = new Slider();
            slBluePlayer.setValue(0);
            slBluePlayer.setBlockIncrement(1);
            slBluePlayer.setShowTickLabels(true);
            slBluePlayer.setMin(0);
            slBluePlayer.setMax(255);
            pane.add(slBluePlayer, 1, 3);

            // Green
            pane.add(new Label("Green: "), 0, 2);
            Slider slGreenPlayer = new Slider();
            slGreenPlayer.setValue(0);
            slGreenPlayer.setBlockIncrement(1);
            slGreenPlayer.setShowTickLabels(true);
            slGreenPlayer.setMin(0);
            slGreenPlayer.setMax(255);
            pane.add(slGreenPlayer, 1, 2);
            pane.add(r1, 3, 2);

            slRedPlayer.setValue(colorPlayer.getRed());
            slGreenPlayer.setValue(colorPlayer.getGreen());
            slBluePlayer.setValue(colorPlayer.getBlue());
            r1.setFill(Color.rgb(redPlayer, greenPlayer, bluePlayer));

            slRedPlayer.valueProperty().addListener(ov -> {
                redPlayer = (int) slRedPlayer.getValue();
                greenPlayer = (int) slGreenPlayer.getValue();
                bluePlayer = (int) slBluePlayer.getValue();

                r1.setFill(Color.rgb(redPlayer, greenPlayer, bluePlayer));

                player.getShape().setFill(r1.getFill());
            });
            slGreenPlayer.valueProperty().addListener(ov -> {

                redPlayer = (int) slRedPlayer.getValue();
                greenPlayer = (int) slGreenPlayer.getValue();
                bluePlayer = (int) slBluePlayer.getValue();

                r1.setFill(Color.rgb(redPlayer, greenPlayer, bluePlayer));

                player.getShape().setFill(r1.getFill());
            });
            slBluePlayer.valueProperty().addListener(ov -> {

                redPlayer = (int) slRedPlayer.getValue();
                greenPlayer = (int) slGreenPlayer.getValue();
                bluePlayer = (int) slBluePlayer.getValue();

                r1.setFill(Color.rgb(redPlayer, greenPlayer, bluePlayer));

                player.getShape().setFill(r1.getFill());
            });

            pane.add(new Label("Enemy"), 1, 5);

            // Red
            pane.add(new Label("Red: "), 0, 6);
            Slider slRedEnemy = new Slider();
            slRedEnemy.setValue(0);
            slRedEnemy.setBlockIncrement(1);
            slRedEnemy.setShowTickLabels(true);
            slRedEnemy.setMin(0);
            slRedEnemy.setMax(255);
            pane.add(slRedEnemy, 1, 6);

            // Blue
            pane.add(new Label("Blue: "), 0, 8);
            Slider slBlueEnemy = new Slider();
            slBlueEnemy.setValue(0);
            slBlueEnemy.setBlockIncrement(1);
            slBlueEnemy.setShowTickLabels(true);
            slBlueEnemy.setMin(0);
            slBlueEnemy.setMax(255);
            pane.add(slBlueEnemy, 1, 8);

            // Green
            pane.add(new Label("Green: "), 0, 7);
            Slider slGreenEnemy = new Slider();
            slGreenEnemy.setValue(0);
            slGreenEnemy.setBlockIncrement(1);
            slGreenEnemy.setShowTickLabels(true);
            slGreenEnemy.setMin(0);
            slGreenEnemy.setMax(255);
            pane.add(slGreenEnemy, 1, 7);
            pane.add(r2, 3, 7);

            slRedEnemy.setValue(colorEnemy.getRed());
            slGreenEnemy.setValue(colorEnemy.getGreen());
            slBlueEnemy.setValue(colorEnemy.getBlue());
            r2.setFill(Color.rgb(redEnemy, greenEnemy, blueEnemy));

            slRedEnemy.valueProperty().addListener(ov -> {

                redEnemy = (int) slRedEnemy.getValue();
                greenEnemy = (int) slGreenEnemy.getValue();
                blueEnemy = (int) slBlueEnemy.getValue();

                r2.setFill(Color.rgb(redEnemy, greenEnemy, blueEnemy));

                enemies.forEach((enemy) -> {
                    enemy.getShape().setFill(r2.getFill());
                });

            });
            slGreenEnemy.valueProperty().addListener(ov -> {

                redEnemy = (int) slRedEnemy.getValue();
                greenEnemy = (int) slGreenEnemy.getValue();
                blueEnemy = (int) slBlueEnemy.getValue();

                r2.setFill(Color.rgb(redEnemy, greenEnemy, blueEnemy));

                enemies.forEach((enemy) -> {
                    enemy.getShape().setFill(r2.getFill());
                });
            });
            slBlueEnemy.valueProperty().addListener(ov -> {

                redEnemy = (int) slRedEnemy.getValue();
                greenEnemy = (int) slGreenEnemy.getValue();
                blueEnemy = (int) slBlueEnemy.getValue();

                r2.setFill(Color.rgb(redEnemy, greenEnemy, blueEnemy));

                enemies.forEach((enemy) -> {
                    enemy.getShape().setFill(r2.getFill());
                });
            });
            window.setTitle("Preferences");
            window.setScene(new Scene(pane, 800, 400));
            window.show();
        });

        //Speed
        speed1.setOnAction(e -> {
            speed.setValue(1);
            enemies.forEach((enemy) -> {
                enemy.setSpeed(speed.getValue());
            });
        });

        speed2.setOnAction(e -> {

            speed.setValue(2);
            enemies.forEach((enemy) -> {
                enemy.setSpeed(speed.getValue());
            });
        });

        speed3.setOnAction(e -> {

            speed.setValue(3);
            enemies.forEach((enemy) -> {
                enemy.setSpeed(speed.getValue());
            });
        });

        // New Game
        newMenuItem.setOnAction(e -> {
            speed.setValue(1);
            speed1.setSelected(true);
            redPlayer = 255;
            greenPlayer = 255;
            bluePlayer = 0;
            player.getShape().setFill(Color.rgb(redPlayer, greenPlayer, bluePlayer));
            redEnemy = 255;
            greenEnemy = 0;
            blueEnemy = 0;
            sizeEnemy.setValue(1);
            countLife.setValue(2);
            root.getChildren().clear();
            player.setIsLeft(false);
            player.setIsRight(false);
            player.setIsUp(false);
            player.setIsDown(false);
            playPane.setDisable(false);

            animation.pause();
            isPause = true;
            playPane.getChildren().clear();
            enemies.clear();
            createEnemy();

            player.getShape().setX(WIDTH_SCENE / 2);
            player.getShape().setY(HEIGHT_SCENE - 100);

            playPane.getChildren().add(player.getShape());
            root.setTop(menuBar);
            root.setCenter(playPane);
            root.setRight(getLifeBox());
        });

        // Save Game
        saveMenuItem.setOnAction(e -> {
            animation.pause();
            isPause = true;
            GridPane pane = new GridPane();
            pane.setAlignment(Pos.CENTER);
            pane.setHgap(20);
            pane.setVgap(20);
            Stage window = new Stage();
            Label lbError = new Label();
            TextField tfFileName = new TextField();
            Button btSave = new Button("Save");
            pane.add(new Label("File Name: "), 0, 0);
            pane.add(tfFileName, 1, 0);
            pane.add(btSave, 1, 1);
            pane.add(lbError, 1, 2);

            btSave.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    if (!tfFileName.getText().trim().isEmpty()) {
                        try {

                            data.setFileOutputName(tfFileName.getText());
                            if (redEnemy != - 1 && greenEnemy != -1 && blueEnemy != -1) {
                                colorEnemy = new ColorTank(redEnemy, greenEnemy, blueEnemy);
                            }
                            if (redPlayer != - 1 && greenPlayer != -1 && bluePlayer != -1) {
                                colorPlayer = new ColorTank(redPlayer, greenPlayer, bluePlayer);
                            }
                            tempEnemy.setValue(sizeEnemy.getValue());
                            data.save(player, enemies, countEnemy, tempEnemy, countLife, colorPlayer, colorEnemy, speed);
                            window.close();
                        } catch (IllegalArgumentException ex) {
                            lbError.setText(ex.getMessage());
                            lbError.setTextFill(Color.RED);
                        } catch (IOException ex) {
                            lbError.setText("Invalid File Name! " + ex.toString());
                            lbError.setTextFill(Color.RED);
                        }
                    }
                }
            });
            window.setTitle("Save Game");
            window.setScene(new Scene(pane, 300, 250));
            window.show();
        });

        // Load Game
        loadMenuItem.setOnAction(e -> {
            animation.pause();
            isPause = true;
            GridPane pane = new GridPane();
            pane.setAlignment(Pos.CENTER);
            pane.setHgap(20);
            pane.setVgap(20);
            Stage window = new Stage();
            Label lbError = new Label();
            TextField tfFileName = new TextField();
            Button btLoad = new Button("Load");
            pane.add(new Label("File Name: "), 0, 0);
            pane.add(tfFileName, 1, 0);
            pane.add(btLoad, 1, 1);
            pane.add(lbError, 1, 2);

            btLoad.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    if (!tfFileName.getText().trim().isEmpty()) {
                        try {
                            isLoad = true;

                            data.setFileInputName(tfFileName.getText());

                            enemies.forEach((enemy) -> {
                                playPane.getChildren().remove(enemy.getShape());
                            });
                            enemies.clear();
                            data.load(player, enemies, countEnemy, tempEnemy, countLife, colorPlayer, colorEnemy, speed);
                            sizeEnemy.setValue(countEnemy.getValue());
                            playPane.getChildren().clear();

                            redPlayer = colorPlayer.getRed();
                            greenPlayer = colorPlayer.getGreen();
                            bluePlayer = colorPlayer.getBlue();
                            player.getShape().setFill(Color.rgb(redPlayer, greenPlayer, bluePlayer));

                            playPane.getChildren().add(player.getShape());
                            for (int i = 0; i < sizeEnemy.getValue(); i++) {
                                enemies.get(i).setHeightScene(HEIGHT_SCENE);
                                enemies.get(i).setWidthScene(WIDTH_SCENE);
                                enemies.get(i).setPane(playPane);

                                enemies.get(i).getShape().setFill(Color.rgb(colorEnemy.getRed(), colorEnemy.getGreen(), colorEnemy.getBlue()));
                                redEnemy = colorEnemy.getRed();
                                greenEnemy = colorEnemy.getGreen();
                                blueEnemy = colorEnemy.getBlue();

                                playPane.getChildren().add(enemies.get(i).getShape());
                            }
                            if (countLife.getValue() == 1) {
                                imageView3.setImage(null);
                            } else if (countLife.getValue() == 0) {
                                imageView3.setImage(null);
                                imageView2.setImage(null);
                            }
                            enemies.forEach((enemy) -> {
                                enemy.setSpeed(speed.getValue());
                            });
                            if (speed.getValue() == 1) {
                                speed1.setSelected(true);
                            }
                            if (speed.getValue() == 2) {
                                speed2.setSelected(true);
                            }
                            if (speed.getValue() == 3) {
                                speed3.setSelected(true);
                            }
                            window.close();
                        } catch (IllegalArgumentException ex) {
                            lbError.setText(ex.getMessage());
                            lbError.setTextFill(Color.RED);
                        } catch (IOException ex) {
                            lbError.setText("Invalid File Name! " + ex.toString());
                            lbError.setTextFill(Color.RED);
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            });
            window.setTitle("Load Game");
            window.setScene(new Scene(pane, 300, 250));
            window.show();
        });
        return menuBar;
    }

    /**
     * Player is killed
     *
     * @param enemy enemy
     */
    public void playerKilled(Enemy enemy) {
        if (player.getShape().getX() >= enemy.getShape().getX() - WIDTH_REC
                && player.getShape().getX() <= enemy.getShape().getX() + WIDTH_REC) {
            if (player.getShape().getY() >= enemy.getShape().getY() - HEIGHT_REC
                    && player.getShape().getY() <= enemy.getShape().getY() + HEIGHT_REC) {
                playPane.getChildren().clear();
                player.getUp().clear();
                player.getDown().clear();
                player.getLeft().clear();
                player.getRight().clear();
                player.setPlayerShoot(false);
                player.getShape().setX(WIDTH_SCENE / 2);
                player.getShape().setY(HEIGHT_SCENE - 100);
                player.setXBullet();
                player.setYBullet();
                playPane.getChildren().add(player.getShape());
                enemies.clear();
                createEnemy();
                animation.pause();
                isPause = true;
                if (countLife.getValue() == 1) {
                    imageView3.setImage(null);
                } else if (countLife.getValue() == 0) {
                    imageView2.setImage(null);
                } else {
                    imageView1.setImage(null);
                }
                countLife.setValue(countLife.getValue() - 1);
            }
        }
        enemy.getUp().forEach((bulletEnemy) -> {
            if (bulletEnemy.getCenterX() >= player.getShape().getX() && bulletEnemy.getCenterX() <= player.getShape().getX() + WIDTH_REC) {
                if (bulletEnemy.getCenterY() >= player.getShape().getY() && bulletEnemy.getCenterY() <= player.getShape().getY() + HEIGHT_REC) {
                    playPane.getChildren().clear();
                    player.getUp().clear();
                    player.getDown().clear();
                    player.getLeft().clear();
                    player.getRight().clear();
                    player.setPlayerShoot(false);
                    player.getShape().setX(WIDTH_SCENE / 2);
                    player.getShape().setY(HEIGHT_SCENE - 100);
                    player.setXBullet();
                    player.setYBullet();
                    playPane.getChildren().add(player.getShape());
                    createEnemy();
                    animation.pause();
                    isPause = true;
                    if (countLife.getValue() == 1) {
                        imageView3.setImage(null);
                    } else if (countLife.getValue() == 0) {
                        imageView2.setImage(null);
                    } else {
                        imageView1.setImage(null);
                    }
                    countLife.setValue(countLife.getValue() - 1);
                }
            }
        });
        enemy.getDown().forEach((bulletEnemy) -> {
            if (bulletEnemy.getCenterX() >= player.getShape().getX() && bulletEnemy.getCenterX() <= player.getShape().getX() + WIDTH_REC) {
                if (bulletEnemy.getCenterY() >= player.getShape().getY() && bulletEnemy.getCenterY() <= player.getShape().getY() + HEIGHT_REC) {
                    playPane.getChildren().clear();
                    player.getUp().clear();
                    player.getDown().clear();
                    player.getLeft().clear();
                    player.getRight().clear();
                    player.setPlayerShoot(false);
                    player.getShape().setX(WIDTH_SCENE / 2);
                    player.getShape().setY(HEIGHT_SCENE - 100);
                    player.setXBullet();
                    player.setYBullet();
                    playPane.getChildren().add(player.getShape());
                    enemies.clear();
                    createEnemy();
                    animation.pause();
                    isPause = true;
                    if (countLife.getValue() == 1) {
                        imageView3.setImage(null);
                    } else if (countLife.getValue() == 0) {
                        imageView2.setImage(null);
                    } else {
                        imageView1.setImage(null);
                    }
                    countLife.setValue(countLife.getValue() - 1);
                }
            }
        });
        enemy.getLeft().forEach((bulletEnemy) -> {
            if (bulletEnemy.getCenterX() >= player.getShape().getX() && bulletEnemy.getCenterX() <= player.getShape().getX() + WIDTH_REC) {
                if (bulletEnemy.getCenterY() >= player.getShape().getY() && bulletEnemy.getCenterY() <= player.getShape().getY() + HEIGHT_REC) {
                    playPane.getChildren().clear();
                    player.getUp().clear();
                    player.getDown().clear();
                    player.getLeft().clear();
                    player.getRight().clear();
                    player.setPlayerShoot(false);
                    player.getShape().setX(WIDTH_SCENE / 2);
                    player.getShape().setY(HEIGHT_SCENE - 100);
                    player.setXBullet();
                    player.setYBullet();
                    playPane.getChildren().add(player.getShape());
                    enemies.clear();
                    createEnemy();
                    animation.pause();
                    isPause = true;
                    if (countLife.getValue() == 1) {
                        imageView3.setImage(null);
                    } else if (countLife.getValue() == 0) {
                        imageView2.setImage(null);
                    } else {
                        imageView1.setImage(null);
                    }
                    countLife.setValue(countLife.getValue() - 1);
                }
            }
        });
        enemy.getRight().forEach((bulletEnemy) -> {
            if (bulletEnemy.getCenterX() >= player.getShape().getX() && bulletEnemy.getCenterX() <= player.getShape().getX() + WIDTH_REC) {
                if (bulletEnemy.getCenterY() >= player.getShape().getY() && bulletEnemy.getCenterY() <= player.getShape().getY() + HEIGHT_REC) {
                    playPane.getChildren().clear();
                    player.getUp().clear();
                    player.getDown().clear();
                    player.getLeft().clear();
                    player.getRight().clear();
                    player.setPlayerShoot(false);
                    player.getShape().setX(WIDTH_SCENE / 2);
                    player.getShape().setY(HEIGHT_SCENE - 100);
                    player.setXBullet();
                    player.setYBullet();
                    playPane.getChildren().add(player.getShape());
                    enemies.clear();
                    createEnemy();
                    animation.pause();
                    isPause = true;
                    if (countLife.getValue() == 1) {
                        imageView3.setImage(null);
                    } else if (countLife.getValue() == 0) {
                        imageView2.setImage(null);
                    } else {
                        imageView1.setImage(null);
                    }
                    countLife.setValue(countLife.getValue() - 1);
                }
            }
        });
    }

    /**
     * Enemy is killed
     *
     * @param enemy enemy
     */
    public void enemyKilled(Enemy enemy) {
        for (Circle bulletPlayer : new ArrayList<>(player.getUp())) {
            if (bulletPlayer.getCenterX() >= enemy.getShape().getX() && bulletPlayer.getCenterX() <= enemy.getShape().getX() + WIDTH_REC) {
                if (bulletPlayer.getCenterY() >= enemy.getShape().getY() && bulletPlayer.getCenterY() <= enemy.getShape().getY() + HEIGHT_REC) {
                    playPane.getChildren().remove(enemy.getShape());
                    playPane.getChildren().remove(bulletPlayer);
                    enemies.remove(enemy);
                    countEnemy.setValue(countEnemy.getValue() - 1);
                    player.getUp().remove(bulletPlayer);
                    player.reduceCountUp();
                    enemy.getUp().forEach((bulletEnemy) -> {
                        playPane.getChildren().remove(bulletEnemy);
                    });
                    enemy.getDown().forEach((bulletEnemy) -> {
                        playPane.getChildren().remove(bulletEnemy);
                    });
                    enemy.getLeft().forEach((bulletEnemy) -> {
                        playPane.getChildren().remove(bulletEnemy);
                    });
                    enemy.getRight().forEach((bulletEnemy) -> {
                        playPane.getChildren().remove(bulletEnemy);
                    });
                    enemy.getUp().clear();
                    enemy.getDown().clear();
                    enemy.getLeft().clear();
                    enemy.getRight().clear();
                    player.setPlayerShoot(false);
                    enemy.setIsStop(true);
                }
            }
        }

        for (Circle bulletPlayer : new ArrayList<>(player.getDown())) {
            if (bulletPlayer.getCenterX() >= enemy.getShape().getX() && bulletPlayer.getCenterX() <= enemy.getShape().getX() + WIDTH_REC) {
                if (bulletPlayer.getCenterY() >= enemy.getShape().getY() && bulletPlayer.getCenterY() <= enemy.getShape().getY() + HEIGHT_REC) {
                    playPane.getChildren().remove(enemy.getShape());
                    playPane.getChildren().remove(bulletPlayer);
                    player.getDown().remove(bulletPlayer);
                    player.reduceCountDown();
                    enemies.remove(enemy);
                    countEnemy.setValue(countEnemy.getValue() - 1);
                    enemy.getUp().forEach((bulletEnemy) -> {
                        playPane.getChildren().remove(bulletEnemy);
                    });
                    enemy.getDown().forEach((bulletEnemy) -> {
                        playPane.getChildren().remove(bulletEnemy);
                    });
                    enemy.getLeft().forEach((bulletEnemy) -> {
                        playPane.getChildren().remove(bulletEnemy);
                    });
                    enemy.getRight().forEach((bulletEnemy) -> {
                        playPane.getChildren().remove(bulletEnemy);
                    });
                    enemy.getUp().clear();
                    enemy.getDown().clear();
                    enemy.getLeft().clear();
                    enemy.getRight().clear();
                    player.setPlayerShoot(false);
                    enemy.setIsStop(true);
                }
            }
        }

        for (Circle bulletPlayer : new ArrayList<>(player.getLeft())) {
            if (bulletPlayer.getCenterX() >= enemy.getShape().getX() && bulletPlayer.getCenterX() <= enemy.getShape().getX() + WIDTH_REC) {
                if (bulletPlayer.getCenterY() >= enemy.getShape().getY() && bulletPlayer.getCenterY() <= enemy.getShape().getY() + HEIGHT_REC) {
                    playPane.getChildren().remove(enemy.getShape());
                    playPane.getChildren().remove(bulletPlayer);
                    player.getLeft().remove(bulletPlayer);
                    player.reduceCountLeft();
                    enemies.remove(enemy);
                    countEnemy.setValue(countEnemy.getValue() - 1);
                    enemy.getUp().forEach((bulletEnemy) -> {
                        playPane.getChildren().remove(bulletEnemy);
                    });
                    enemy.getDown().forEach((bulletEnemy) -> {
                        playPane.getChildren().remove(bulletEnemy);
                    });
                    enemy.getLeft().forEach((bulletEnemy) -> {
                        playPane.getChildren().remove(bulletEnemy);
                    });
                    enemy.getRight().forEach((bulletEnemy) -> {
                        playPane.getChildren().remove(bulletEnemy);
                    });
                    enemy.getUp().clear();
                    enemy.getDown().clear();
                    enemy.getLeft().clear();
                    enemy.getRight().clear();
                    player.setPlayerShoot(false);
                    enemy.setIsStop(true);
                }
            }
        }

        for (Circle bulletPlayer : new ArrayList<>(player.getRight())) {
            if (bulletPlayer.getCenterX() >= enemy.getShape().getX() && bulletPlayer.getCenterX() <= enemy.getShape().getX() + WIDTH_REC) {
                if (bulletPlayer.getCenterY() >= enemy.getShape().getY() && bulletPlayer.getCenterY() <= enemy.getShape().getY() + HEIGHT_REC) {
                    playPane.getChildren().remove(enemy.getShape());
                    playPane.getChildren().remove(bulletPlayer);
                    player.getRight().remove(bulletPlayer);
                    player.reduceCountRight();
                    enemies.remove(enemy);
                    countEnemy.setValue(countEnemy.getValue() - 1);
                    enemy.getUp().forEach((bulletEnemy) -> {
                        playPane.getChildren().remove(bulletEnemy);
                    });
                    enemy.getDown().forEach((bulletEnemy) -> {
                        playPane.getChildren().remove(bulletEnemy);
                    });
                    enemy.getLeft().forEach((bulletEnemy) -> {
                        playPane.getChildren().remove(bulletEnemy);
                    });
                    enemy.getRight().forEach((bulletEnemy) -> {
                        playPane.getChildren().remove(bulletEnemy);
                    });
                    enemy.getUp().clear();
                    enemy.getDown().clear();
                    enemy.getLeft().clear();
                    enemy.getRight().clear();
                    player.setPlayerShoot(false);
                    enemy.setIsStop(true);
                }

            }
        }
    }

    /**
     * Control enemy
     *
     * @param enemy enemy
     */
    public void controlEnemy(Enemy enemy) {

        // Set time shoot the bullet
        enemy.setTimeShoot();
        if (enemy.getTimeShoot() % 50 == 0) {
            if (!enemy.getIsStop()) {
                enemy.setIsShoot(true);
                enemy.shoot();
            }

        }

        if (enemy.getIsShoot()) {
            enemy.shootUp();
            enemy.shootDown();
            enemy.shootLeft();
            enemy.shootRight();
        }

        if (!enemy.getIsSetDirection()) {
            enemy.setRandomNumber(0, 4);
            enemy.setPositionX(0, WIDTH_SCENE - WIDTH_REC);
            enemy.setPositionY(0, HEIGHT_SCENE - HEIGHT_REC);
        }

        switch (enemy.getDirection().getRandomNumber()) {
            case LEFT:
                enemy.setIsLeft(true);
                enemy.setIsRight(false);
                enemy.setIsUp(false);
                enemy.setIsDown(false);
                enemy.setIsSetDirection(true);
                if (enemy.getShape().getX() < 0) {
                    enemy.setIsSetDirection(false);
                } else {
                    enemy.moveLeft();
                }
                if (enemy.getShape().getX() == enemy.getPositionX().getRandomNumber()) {
                    enemy.setIsSetDirection(false);
                }
                break;
            case RIGHT:
                enemy.setIsLeft(false);
                enemy.setIsRight(true);
                enemy.setIsUp(false);
                enemy.setIsDown(false);
                enemy.setIsSetDirection(true);
                if (enemy.getShape().getX() > WIDTH_SCENE - WIDTH_REC) {
                    enemy.setIsSetDirection(false);
                } else {
                    enemy.moveRight();
                }
                if (enemy.getShape().getX() == enemy.getPositionX().getRandomNumber()) {
                    enemy.setIsSetDirection(false);
                }
                break;
            case UP:
                enemy.setIsLeft(false);
                enemy.setIsRight(false);
                enemy.setIsUp(true);
                enemy.setIsDown(false);
                enemy.setIsSetDirection(true);
                if (enemy.getShape().getY() < 0) {
                    enemy.setIsSetDirection(false);
                } else {
                    enemy.moveUp();
                }
                if (enemy.getShape().getY() == enemy.getPositionY().getRandomNumber()) {
                    enemy.setIsSetDirection(false);
                }
                break;
            case DOWN:
                enemy.setIsLeft(false);
                enemy.setIsRight(false);
                enemy.setIsUp(false);
                enemy.setIsDown(true);
                enemy.setIsSetDirection(true);
                if (enemy.getShape().getY() > HEIGHT_SCENE - HEIGHT_REC) {
                    enemy.setIsSetDirection(false);
                } else {
                    enemy.moveDown();
                }
                if (enemy.getShape().getY() == enemy.getPositionY().getRandomNumber()) {
                    enemy.setIsSetDirection(false);
                }
                break;
            default:
                break;
        }
    }

    /**
     * Control player
     */
    public void controlPlayer() {
        if (player.getIsShoot()) {
            player.shootUp();
            player.shootDown();
            player.shootLeft();
            player.shootRight();
        }

        if (player.getIsLeft()) {
            player.moveLeft();
        }
        if (player.getIsRight()) {
            player.moveRight();
        }
        if (player.getIsDown()) {
            player.moveDown();
        }
        if (player.getIsUp()) {
            player.moveUp();
        }
        playPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {

                if (e.getCode() == KeyCode.LEFT) {
                    player.setIsLeft(true);
                    player.setIsShootUp(false);
                    player.setIsShootDown(false);
                    player.setIsShootLeft(true);
                    player.setIsShootRight(false);
                }

                if (e.getCode() == KeyCode.RIGHT) {
                    player.setIsRight(true);
                    player.setIsShootUp(false);
                    player.setIsShootDown(false);
                    player.setIsShootLeft(false);
                    player.setIsShootRight(true);
                }

                if (e.getCode() == KeyCode.UP) {
                    player.setIsUp(true);
                    player.setIsShootUp(true);
                    player.setIsShootDown(false);
                    player.setIsShootLeft(false);
                    player.setIsShootRight(false);
                }

                if (e.getCode() == KeyCode.DOWN) {
                    player.setIsDown(true);
                    player.setIsShootUp(false);
                    player.setIsShootDown(true);
                    player.setIsShootLeft(false);
                    player.setIsShootRight(false);
                }

                if (e.getCode() == KeyCode.SPACE) {
                    if (isPause) {
                        animation.play();
                    }
                    player.setIsShoot(true);
                    player.shoot();
                    player.setPlayerShoot(true);
                }
            }
        });

        playPane.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                if (e.getCode() == KeyCode.LEFT) {
                    player.setIsLeft(false);
                }

                if (e.getCode() == KeyCode.RIGHT) {
                    player.setIsRight(false);
                }

                if (e.getCode() == KeyCode.UP) {
                    player.setIsUp(false);
                }

                if (e.getCode() == KeyCode.DOWN) {
                    player.setIsDown(false);
                }

            }
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
