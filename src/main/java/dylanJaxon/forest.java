/*Dylan & Jaxon
  10/19/2020
  1st are that the player goes through to fight enemies
  This is an RPG game with a storyline. There is turn based combat, with items, classes and enemies
  There is also save files to save your progress
*/
package dylanJaxon;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

public class forest implements Initializable {

    @FXML
    public AnchorPane ancForest1, ancForest2, ancForest3, ancForest4, ancForest5, ancInventory, ancLumberYard;
    @FXML
    private Polygon polA1Left, polA1Right, polA1Entrance, polA1Exit;
    @FXML
    private Polygon polA2Left, polA2Top, polA2Bottom, polA2Entrance, polA2Exit;
    @FXML
    private Polygon polHerbs, polA3Bottom, polA3Top, polA3Entrance, polA3Exit;
    @FXML
    private Polygon polA4Bottom, polA4Top, polA4Right, polA4Entrance, polA4Exit;
    @FXML
    private Polygon polA5Bottom, polA5Top, polA5Entrance, polA5Exit, polRock, polRockBreak;
    @FXML
    private Polygon polA2Wizard, polA4Lumber, polLumberYardWalls, polLumberYardEntry, polLumberYardPlayer, polLogCollect;
    @FXML
    public Polygon polA1Player, polA2Player, polA3Player, polA4Player, polA5Player;
    @FXML
    private Label lblHerbCollect, lblHerbs, lblLogs, lblRocks, lblPotions, lblHealth, lblBalance, lblDmgProt, lblDmgProtAmnt, lblDurabilityAmnt;
    @FXML private ImageView imgQuest1, imgQuest2, imgQuest3;
    @FXML
    private Label lblBreak, lblContinue, lblStory, lblLogCollect;
    @FXML
    private ImageView imgInv1, imgInv2, imgInv3, imgInv4, imgInv5, imgEqp1, imgEqp2, imgRock;
    @FXML
    private ImageView imgA1Player, imgA2Player, imgA3Player, imgA4Player, imgA5Player, imgYard, imgInv, imgLumberYardPlayer;
    @FXML
    private Pane panA1Player, panA2Player, panA3Player, panA4Player, panA5Player, panLumberYardPlayer;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //creating all the needed arrays
    static AnchorPane[] panes = new AnchorPane[7];
    Polygon[] forest1 = new Polygon[4];
    Polygon[] forest2 = new Polygon[6];
    Polygon[] forest3 = new Polygon[4];
    Polygon[] forest4 = new Polygon[6];
    Polygon[] forest5 = new Polygon[4];
    public static Pane[] players = new Pane[6];

    ArrayList<ImageView> invImage = new ArrayList<>();
    ImageView[] playerImages = new ImageView[6];

    //saves location for multi part dialogues
    int dialogueLoc = 0;
   // MediaPlayer a;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //movement control
    public void setX(Node block1, double locX) {
        block1.setTranslateX(locX - block1.getLayoutX());
    }

    public double getX(Node block1) {
        return block1.getTranslateX() + block1.getLayoutX();
    }

    public void setY(Node block1, double locY) {
        block1.setTranslateY(locY - block1.getLayoutY());
    }

    public double getY(Node block1) {
        return block1.getTranslateY() + block1.getLayoutY();
    }

    int x = 0;
    int y = 0;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Timer for movement
    Timeline movement = new Timeline(new KeyFrame(Duration.millis(50), ae -> {
        try {
            move();
        } catch (IOException e) {
            ;
        }
    }));

    // Delay for running encounters
    int encounterDelay = 0;

    int collect = 60;

    private void move() throws IOException {
        //moves the player for all panes
        if (ancForest1.isVisible()) {
            setX(panA1Player, getX(panA1Player) + x);
            setY(panA1Player, getY(panA1Player) + y);

            //checks for collision with entry/exit
            if (collision(polA1Player, polA1Entrance)) {
                setX(panA1Player, getX(panA1Player) - x);
                setY(panA1Player, getY(panA1Player) - y);
                movement.stop();
                MainApp.setRoot("town", "");
                MainApp.you.setMapNum(18);
                town.switchPane(town.anchorPanes[6]);
                MainApp.setSize(391, 391);
            //    a.stop();
            }
            if (collision(polA1Player, polA1Exit)) {
                switchPane(ancForest2);
                setX(panA1Player, getX(panA1Player) - x);
                setY(panA1Player, getY(panA1Player) - y);
                MainApp.you.setMapNum(7);
            }

            //checks for collision with walls
            for (Polygon name : forest1) {
                if (collision(polA1Player, name)) {
                    setX(panA1Player, getX(panA1Player) - x);
                    setY(panA1Player, getY(panA1Player) - y);
                }
            }

        } else if (ancForest2.isVisible()) {
            setX(panA2Player, getX(panA2Player) + x);
            setY(panA2Player, getY(panA2Player) + y);

            //checks for collision with entry/exit
            if (collision(polA2Player, polA2Wizard)) {
                setX(panA2Player, getX(panA2Player) - x);
                setY(panA2Player, getY(panA2Player) - y);
                movement.stop();
                MainApp.setRoot("town", "");
                MainApp.setSize(395, 390);
                MainApp.you.setMapNum(5);
                town.switchPane(town.anchorPanes[3]);
                //a.stop();
            }
            if (collision(polA2Player, polA2Entrance)) {
                switchPane(ancForest1);
                setX(panA2Player, getX(panA2Player) - x);
                setY(panA2Player, getY(panA2Player) - y);
                MainApp.you.setMapNum(6);
            }
            if (collision(polA2Player, polA2Exit)) {
                switchPane(ancForest3);
                setX(panA2Player, getX(panA2Player) - x);
                setY(panA2Player, getY(panA2Player) - y);
                MainApp.you.setMapNum(8);
            }

            //checks for collision with walls
            for (Polygon name : forest2) {
                if (collision(polA2Player, name)) {
                    setX(panA2Player, getX(panA2Player) - x);
                    setY(panA2Player, getY(panA2Player) - y);
                }
            }

        } else if (ancForest3.isVisible()) {
            setX(panA3Player, getX(panA3Player) + x);
            setY(panA3Player, getY(panA3Player) + y);

            //checks for collision with entry/exit
            if (collision(polA3Player, polA3Entrance)) {
                switchPane(ancForest2);
                setX(panA3Player, getX(panA3Player) - x);
                setY(panA3Player, getY(panA3Player) - y);

                MainApp.you.setMapNum(7);
            }
            if (collision(polA3Player, polA3Exit)) {
                switchPane(ancForest4);
                setX(panA3Player, getX(panA3Player) - x);
                setY(panA3Player, getY(panA3Player) - y);
                MainApp.you.setMapNum(9);
            }

            //checks for collision with walls
            for (Polygon name : forest3) {
                if (collision(polA3Player, name)) {
                    setX(panA3Player, getX(panA3Player) - x);
                    setY(panA3Player, getY(panA3Player) - y);
                }
            }

            //show text for collecting herbs
            lblHerbCollect.setVisible(collision(polA3Player, polHerbs));

            if (!lblHerbCollect.isVisible()) {
                // Removes text when you leave the collection area so that it won't re-appear upon re-entering that area
                lblHerbCollect.setText("");

                // Runs if the herb label is visible, has nothing in it and you're ready to collect
            } else if (lblHerbCollect.getText().equals("") && collect == 60) {
                lblHerbCollect.setText("Press E to interact");
            }

        } else if (ancForest4.isVisible()) {
            setX(panA4Player, getX(panA4Player) + x);
            setY(panA4Player, getY(panA4Player) + y);

            //checks for collision with entry/exit
            if (collision(polA4Player, polA4Entrance)) {
                switchPane(ancForest3);
                setX(panA4Player, getX(panA4Player) - x);
                setY(panA4Player, getY(panA4Player) - y);
                MainApp.you.setMapNum(8);

            }
            if (collision(polA4Player, polA4Exit)) {
                switchPane(ancForest5);
                setX(panA4Player, getX(panA4Player) - x);
                setY(panA4Player, getY(panA4Player) - y);
                MainApp.you.setMapNum(10);
            }
            if (collision(polA4Player, polA4Lumber)) {
                // Saves your position at the entrance
                MainApp.you.setX(69);
                MainApp.you.setY(245);

                setX(panA4Player, getX(panA4Player) - x);
                setY(panA4Player, getY(panA4Player) - y);
                MainApp.you.setMapNum(11);
                MainApp.setSize(324, 555);
                switchPane(ancLumberYard);
            }

            //checks for collision with walls
            for (Polygon name : forest4) {
                if (collision(polA4Player, name)) {
                    setX(panA4Player, getX(panA4Player) - x);
                    setY(panA4Player, getY(panA4Player) - y);
                }
            }

        } else if (ancForest5.isVisible()) {
            setX(panA5Player, getX(panA5Player) + x);
            setY(panA5Player, getY(panA5Player) + y);

            //checks for collision with entry/exit
            if (collision(polA5Player, polA5Entrance)) {
                switchPane(ancForest4);
                setX(panA5Player, getX(panA5Player) - x);
                setY(panA5Player, getY(panA5Player) - y);
                MainApp.you.setMapNum(9);

            }
            if (collision(polA5Player, polA5Exit)) {
                setX(panA5Player, getX(panA5Player) - x);
                setY(panA5Player, getY(panA5Player) - y);
                movement.stop();
                MainApp.setRoot("caves", "");
                MainApp.you.setMapNum(12);
                MainApp.setSize(396, 396);
                //a.stop();
            }

            lblBreak.setVisible(collision(polA5Player, polRockBreak) && MainApp.you.getProgress(4) && imgRock.isVisible());

            //shows dialogue when arriving at the rock
            if (collision(polA5Player, polRock) && imgRock.isVisible()) {
                setX(panA5Player, getX(panA5Player) - x);
                setY(panA5Player, getY(panA5Player) - y);
                movement.stop();
                lblContinue.setVisible(true);
                lblStory.setVisible(true);
                lblStory.setText("There is a boulder blocking your way into the cave.  \n" +
                        "It is too heavy to be moved, you’ll need to break it.  \n" +
                        "Though it looks like you will need a new tool to accomplish \n such a difficult task.");
            }

            //checks for collision with walls
            for (Polygon name : forest5) {
                if (collision(polA5Player, name)) {
                    setX(panA5Player, getX(panA5Player) - x);
                    setY(panA5Player, getY(panA5Player) - y);
                }
            }
        } else if (ancLumberYard.isVisible()) {
            setX(panLumberYardPlayer, getX(panLumberYardPlayer) + x);
            setY(panLumberYardPlayer, getY(panLumberYardPlayer) + y);

            //show text for collecting herbs
            lblLogCollect.setVisible(collision(polLumberYardPlayer, polLogCollect) && MainApp.you.getProgress(3));

            if (!lblLogCollect.isVisible()) {
                // Removes text when you leave the collection area so that it won't re-appear upon re-entering that area
                lblLogCollect.setText("");

            } else if (lblLogCollect.getText().equals("") && collect == 60) {
                lblLogCollect.setText("Press E to interact");
            }

            if (collision(polLumberYardPlayer, polLumberYardWalls)) {
                setX(panLumberYardPlayer, getX(panLumberYardPlayer) - x);
                setY(panLumberYardPlayer, getY(panLumberYardPlayer) - y);
            }
            if (collision(polLumberYardPlayer, polLumberYardEntry)) {
                setX(panLumberYardPlayer, getX(panLumberYardPlayer) - x);
                setY(panLumberYardPlayer, getY(panLumberYardPlayer) - y);
                switchPane(ancForest4);
                MainApp.you.setMapNum(9);
                MainApp.setSize(391, 391);
            }
        }

        if (encounterDelay < 30) {
            // Increases until it reaches 30 (1.5 seconds)
            ++encounterDelay;
        }

        // Random chance for encounter when walking (runs every 1.5 seconds you move)
        if ((x != 0 || y != 0) && encounterDelay == 302) {

            // Random chance for encounter (1 in 8 every 1.5 seconds)
            if (ThreadLocalRandom.current().nextInt(1, 8 + 1) == 1) {
                // Saves location
                MainApp.you.setX((int) getX(getCharacterPane()));
                MainApp.you.setY((int) getY(getCharacterPane()));

                // Starts random encounter
                MainApp.setRoot("combat", "");
                //a.stop();

                // Resets collect counter as to not start 2 encounters
                collect = 0;

                // Stops movement
                x = 0;
                y = 0;
                movement.stop();
            } else {
                encounterDelay = 0;
            }
        }

        if (collect < 60) {
            collect++;
        }

        //delay between collecting logs
        if (lblLogCollect.getText().equals("Press E to interact") && ancLumberYard.isVisible()) {
            return;
        } else if (ancLumberYard.isVisible()) {
            if (collect == 20) {
                int rand = ThreadLocalRandom.current().nextInt(1, 5 + 1);
                if (rand == 1) {
                    // Saves location
                    MainApp.you.setX((int) getX(getCharacterPane()));
                    MainApp.you.setY((int) getY(getCharacterPane()));

                    // Starts random encounter
                    MainApp.setRoot("combat", "");
                  //  a.stop();
                }
            } else if (collect == 60) {
                lblLogCollect.setText("Press E to interact");
                return;
            }
        }
        //delay between collecting herbs
        if (lblHerbCollect.getText().equals("Press E to interact") && ancForest3.isVisible()) {
            return;
        } else if (ancForest3.isVisible()) {
            if (collect == 20) {
                int rand = ThreadLocalRandom.current().nextInt(1, 5 + 1);
                if (rand == 1) {
                    // Saves location
                    MainApp.you.setX((int) getX(getCharacterPane()));
                    MainApp.you.setY((int) getY(getCharacterPane()));

                    // Starts random encounter
                    MainApp.setRoot("combat", "");

                    // Stops movement
                    x = 0;
                    y = 0;
                    movement.stop();
                    //a.stop();
                }
            } else if (collect == 60) {
                lblHerbCollect.setText("Press E to interact");
                return;
            }
        }
    }

    //collision check
    public boolean collision(Shape block1, Shape block2) {
        //If the objects can be changed to shapes just see if they intersect
        Shape a = Shape.intersect(block1, block2);
        return a.getBoundsInLocal().getWidth() != -1;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //key press and quick speed override

    int speed = 5;

    @FXML
    public void keyPress(KeyEvent event) {
        switch (event.getCode()) {
            //changes Direction
            case W:
                y = -speed;
                x = 0;
                switch (MainApp.you.getClassInt()) {
                    case 0:
                        Image image = new Image(getClass().getResource("/images-world/warrior-back.png").toString());
                        for (ImageView name : playerImages) {
                            name.setImage(image);
                        }
                        break;
                    case 1:
                        Image image1 = new Image(getClass().getResource("/images-world/archer-back.png").toString());
                        for (ImageView name : playerImages) {
                            name.setImage(image1);
                        }
                        break;
                    case 2:
                        Image image2 = new Image(getClass().getResource("/images-world/mage-back.png").toString());
                        for (ImageView name : playerImages) {
                            name.setImage(image2);
                        }
                        break;
                }
                break;
            case A:
                x = -speed;
                y = 0;
                switch (MainApp.you.getClassInt()) {
                    case 0:
                        Image image = new Image(getClass().getResource("/images-world/warrior-left.png").toString());
                        for (ImageView name : playerImages) {
                            name.setImage(image);
                        }
                        break;
                    case 1:
                        Image image1 = new Image(getClass().getResource("/images-world/archer-left.png").toString());
                        for (ImageView name : playerImages) {
                            name.setImage(image1);
                        }
                        break;
                    case 2:
                        Image image2 = new Image(getClass().getResource("/images-world/mage-left.png").toString());
                        for (ImageView name : playerImages) {
                            name.setImage(image2);
                        }
                        break;
                }
                break;
            case S:
                y = speed;
                x = 0;
                switch (MainApp.you.getClassInt()) {
                    case 0:
                        Image image = new Image(getClass().getResource("/images-world/warrior-front.png").toString());
                        for (ImageView name : playerImages) {
                            name.setImage(image);
                        }
                        break;
                    case 1:
                        Image image1 = new Image(getClass().getResource("/images-world/archer-front.png").toString());
                        for (ImageView name : playerImages) {
                            name.setImage(image1);
                        }
                        break;
                    case 2:
                        Image image2 = new Image(getClass().getResource("/images-world/mage-front.png").toString());
                        for (ImageView name : playerImages) {
                            name.setImage(image2);
                        }
                        break;
                }
                break;
            case D:
                x = speed;
                y = 0;
                switch (MainApp.you.getClassInt()) {
                    case 0:
                        Image image = new Image(getClass().getResource("/images-world/warrior-right.png").toString());
                        for (ImageView name : playerImages) {
                            name.setImage(image);
                        }
                        break;
                    case 1:
                        Image image1 = new Image(getClass().getResource("/images-world/archer-right.png").toString());
                        for (ImageView name : playerImages) {
                            name.setImage(image1);
                        }
                        break;
                    case 2:
                        Image image2 = new Image(getClass().getResource("/images-world/mage-right.png").toString());
                        for (ImageView name : playerImages) {
                            name.setImage(image2);
                        }
                        break;
                }
                break;

            //break the boulder and collect herbs
            case E:
                if (collision(polLumberYardPlayer, polLogCollect) && ancLumberYard.isVisible() && collect == 60 && MainApp.you.getProgress(3)) {
                    MainApp.you.setLogs(MainApp.you.getLogs() + 1);
                    collect = 0;
                    lblLogCollect.setText("+1 Logs");
                    lblLogs.setText("You Have: " + MainApp.you.getLogs() + " Logs");
                }

                if (collision(polA5Player, polRockBreak) && MainApp.you.getProgress(4)) {
                    MainApp.you.setProgress(1, true);
                    imgRock.setVisible(false);
                }

                if (collision(polA3Player, polHerbs) && ancForest3.isVisible() && collect == 60) {
                    MainApp.you.setHerbs(MainApp.you.getHerbs() + 1);
                    collect = 0;
                    lblHerbCollect.setText("+1 Herbs");
                    lblHerbs.setText("You Have: " + MainApp.you.getHerbs() + " Herbs");
                }
                break;

            //Continues dialogue
            case SPACE:
                if (lblStory.isVisible()) {
                    if (dialogueLoc == 1) {
                        dialogueLoc = 0;
                        lblStory.setVisible(false);
                        lblContinue.setVisible(false);

                        movement.setCycleCount(Timeline.INDEFINITE);
                        movement.play();
                    } else if (dialogueLoc == 0) {
                        lblStory.setText("Talk to the townsfolk to see if anybody can help you.");
                        dialogueLoc++;
                    }
                }
                break;

            //opens and closes inventory
            case ESCAPE:
                if (ancInventory.isVisible()) {
                    switch (MainApp.you.getMapNum()) {
                        case 6:
                            switchPane(ancForest1);
                            MainApp.setSize(400, 396);
                            break;
                        case 7:
                            switchPane(ancForest2);
                            MainApp.setSize(400, 396);
                            break;
                        case 8:
                            switchPane(ancForest3);
                            MainApp.setSize(400, 396);
                            break;
                        case 9:
                            switchPane(ancForest4);
                            MainApp.setSize(400, 396);
                            break;
                        case 10:
                            switchPane(ancForest5);
                            MainApp.setSize(400, 396);
                            break;
                        case 11:
                            switchPane(ancLumberYard);
                            MainApp.setSize(324, 555);
                            break;
                    }
                } else {
                    // Updating
                    lblPotions.setText("You Have: " + MainApp.you.getPotions() + " Potions");
                    lblHealth.setText("Health: " + MainApp.you.getHealth());
                    lblBalance.setText("Gold: " + MainApp.you.getBalance() + "g");

                    // Makes quest images visible depending on which items you have
                    if (MainApp.you.getProgress(3)) {
                        imgQuest1.setVisible(true);
                    } if (MainApp.you.getProgress(4)) {
                        imgQuest2.setVisible(true);
                    } if (MainApp.you.getProgress(5)) {
                        imgQuest3.setVisible(true);
                    }

                    // Inventory slot updating
                    for (int i = 0; i <= 6; i++) {
                        imageChange(invImage.get(i), MainApp.you.getItemID(i));
                    }

                    switchPane(ancInventory);
                    MainApp.setSize(435, 435);
                }
        }
    }

    @FXML
    public void keyRelease(KeyEvent event) {
        switch (event.getCode()) {
            case W:
            case S:
                y = 0;
                break;
            case A:
            case D:
                x = 0;
        }
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //return to main menu
    @FXML
    private void btnReturn() throws IOException {
        // Saves position in game
        MainApp.you.setX((int) getX(getCharacterPane()));
        MainApp.you.setY((int) getY(getCharacterPane()));

        //sends you back to the main menu
        movement.stop();
        MainApp.setRoot("menu", "");
        MainApp.setSize(375, 375);
        //a.stop();
    }

    private Pane getCharacterPane() {
        switch (MainApp.you.getMapNum()) {
            case 6:
                return panA1Player;
            case 7:
                return panA2Player;
            case 8:
                return panA3Player;
            case 9:
                return panA4Player;
            case 10:
                return panA5Player;
            default /*11*/:
                return panLumberYardPlayer;
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //change what the player sees
    public static void switchPane(AnchorPane pane) {
        for (AnchorPane name : panes) {
            name.setVisible(false);
        }
        pane.setVisible(true);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Inventory

    //creates an outline for selected item
    DropShadow dropShadow = new DropShadow();

    //switches the item to the appropriate slot
    @FXML
    private void btnEquip(MouseEvent event) {
        if (MainApp.you.getSelected() == -1) {
            return;
        }
        MainApp.you.equip();
        switch (MainApp.you.getSelected()) {
            case 1:
                imageChange(imgInv1, MainApp.you.getItemID(0));
                imageChange(imgEqp1, MainApp.you.getItemID(5));
                imageChange(imgEqp2, MainApp.you.getItemID(6));
                break;
            case 2:
                imageChange(imgInv2, MainApp.you.getItemID(1));
                imageChange(imgEqp1, MainApp.you.getItemID(5));
                imageChange(imgEqp2, MainApp.you.getItemID(6));
                break;
            case 3:
                imageChange(imgInv3, MainApp.you.getItemID(2));
                imageChange(imgEqp1, MainApp.you.getItemID(5));
                imageChange(imgEqp2, MainApp.you.getItemID(6));
                break;
            case 4:
                imageChange(imgInv4, MainApp.you.getItemID(3));
                imageChange(imgEqp1, MainApp.you.getItemID(5));
                imageChange(imgEqp2, MainApp.you.getItemID(6));
                break;
            case 5:
                imageChange(imgInv5, MainApp.you.getItemID(4));
                imageChange(imgEqp1, MainApp.you.getItemID(5));
                imageChange(imgEqp2, MainApp.you.getItemID(6));
                break;
        }
        update(MainApp.you.getSelected() - 1);
    }

    //displays stat in selected slot and outlines the item
    @FXML
    void slot(MouseEvent event) {
        // Source
        ImageView source = (ImageView) event.getSource();

        // Only selects if not null
        if (source.getImage() != null) {
            // Sets selected (used for equipping items)
            if (invImage.indexOf(source) >= 5) {
                MainApp.you.setSelected(-1);
            } else {
                MainApp.you.setSelected(invImage.indexOf(source) + 1);
            }

            // Removes all current effects
            for (ImageView name : invImage) {
                name.setEffect(null);
            }
            // Add dropshadow effect
            source.setEffect(dropShadow);

            // Updates damage/defense
            update(invImage.indexOf(source));
        }
    }


    //used to update the text on the right of the inventory
    private void update(int loc) {
        //sets the item id of the item being used
        int ID = MainApp.you.getItemID(loc);
        //if id is -1 there is no item, so return, if its greater than 14 it is armor, else it is a weapon
        if (ID == -1) {
            lblDmgProtAmnt.setText(":");
            lblDurabilityAmnt.setText(":");
        } else if (ID > 14) {
            lblDmgProt.setText("Protection");
            armour name = new armour(MainApp.you.getItemID(loc));

            lblDmgProtAmnt.setText(": " + (name.getDefense() + (MainApp.you.getDamage(loc) * 2)));
            lblDurabilityAmnt.setText(": " + (MainApp.you.getDurability(loc)));
        } else {
            lblDmgProt.setText("Damage");
            weapon name = new weapon(MainApp.you.getItemID(loc));

            lblDmgProtAmnt.setText(": " + (name.getDamage() + (MainApp.you.getDamage(loc) * 2)));
            lblDurabilityAmnt.setText(": " + (MainApp.you.getDurability(loc)));
        }
    }

    //change the image when switching items
    private void imageChange(ImageView imageView, int itemID) {
        //set the new image id
        switch (itemID) {
            case 0:
                Image image0 = new Image(getClass().getResource("/Item-Sprites/shortSword.png").toString());
                imageView.setImage(image0);
                break;
            case 1:
                Image image1 = new Image(getClass().getResource("/Item-Sprites/longSword.png").toString());
                imageView.setImage(image1);
                break;
            case 2:
                Image image2 = new Image(getClass().getResource("/Item-Sprites/squireSword.png").toString());
                imageView.setImage(image2);
                break;
            case 3:
                Image image3 = new Image(getClass().getResource("/Item-Sprites/knightSword.png").toString());
                imageView.setImage(image3);
                break;
            case 4:
                Image image4 = new Image(getClass().getResource("/Item-Sprites/kingSword.png").toString());
                imageView.setImage(image4);
                break;
            case 5:
                Image image5 = new Image(getClass().getResource("/Item-Sprites/shortBow.png").toString());
                imageView.setImage(image5);
                break;
            case 6:
                Image image6 = new Image(getClass().getResource("/Item-Sprites/hunterBow.png").toString());
                imageView.setImage(image6);
                break;
            case 7:
                Image image7 = new Image(getClass().getResource("/Item-Sprites/recurveBow.png").toString());
                imageView.setImage(image7);
                break;
            case 8:
                Image image8 = new Image(getClass().getResource("/Item-Sprites/longBow.png").toString());
                imageView.setImage(image8);
                break;
            case 9:
                Image image9 = new Image(getClass().getResource("/Item-Sprites/elvenBow.png").toString());
                imageView.setImage(image9);
                break;
            case 10:
                Image image10 = new Image(getClass().getResource("/Item-Sprites/woodenStaff.png").toString());
                imageView.setImage(image10);
                break;
            case 11:
                Image image11 = new Image(getClass().getResource("/Item-Sprites/quartzStaff.png").toString());
                imageView.setImage(image11);
                break;
            case 12:
                Image image12 = new Image(getClass().getResource("/Item-Sprites/apprenticeStaff.png").toString());
                imageView.setImage(image12);
                break;
            case 13:
                Image image13 = new Image(getClass().getResource("/Item-Sprites/saphireStaff.png").toString());
                imageView.setImage(image13);
                break;
            case 14:
                Image image14 = new Image(getClass().getResource("/Item-Sprites/masterStaff.png").toString());
                imageView.setImage(image14);
                break;
            case 15:
                Image image15 = new Image(getClass().getResource("/Item-Sprites/clothArmour.png").toString());
                imageView.setImage(image15);
                break;
            case 16:
                Image image16 = new Image(getClass().getResource("/Item-Sprites/leatherArmour.png").toString());
                imageView.setImage(image16);
                break;
            case 17:
                Image image17 = new Image(getClass().getResource("/Item-Sprites/chainArmour.png").toString());
                imageView.setImage(image17);
                break;
            case 18:
                Image image18 = new Image(getClass().getResource("/Item-Sprites/plateArmour.png").toString());
                imageView.setImage(image18);
                break;
            case 19:
                Image image19 = new Image(getClass().getResource("/Item-Sprites/knightArmour.png").toString());
                imageView.setImage(image19);
                break;
        }
    }

    @FXML
    void btnDestroy(MouseEvent event) {
        // Warning msg that you're about to destroy an item
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Destroy Item");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to destroy the selected item? " +
                "The item will be gone forever and you won't get your money back!");

        // Custom buttons
        ButtonType btnY = new ButtonType("Yes");
        ButtonType btnN = new ButtonType("No");
        ButtonType btnC = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(btnY, btnN, btnC);

        // What button was pressed
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == btnY) {
            if (MainApp.you.getSelected() == -1) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Destroy Item");
                alert.setHeaderText(null);
                alert.setContentText("You can't destroy an equipped item!");
                alert.showAndWait();
            } else {
                // Resets the values of the item array
                MainApp.you.setID(MainApp.you.getSelected() - 1, -1);
                MainApp.you.setDurability(MainApp.you.getSelected() - 1, 0);
                MainApp.you.setUpgrade(MainApp.you.getSelected() - 1, 0);

                // Resets the inventory image
                invImage.get(MainApp.you.getSelected() - 1).setImage(null);

                // Makes your equipped weapon the selected image
                MainApp.you.setSelected(-1);
                for (ImageView name : invImage) {
                    name.setEffect(null);
                }
                // Add dropshadow effect
                imgEqp1.setEffect(dropShadow);

                // Updates damage/defense
                update(5);
            }
        }
    }

    @FXML
    void btnPotion(MouseEvent event) {
        // Increases health and takes away a potion
        if (MainApp.you.getHealth() == 100) {
            // Doesn't do anything if your health is full
            return;
        } else if (MainApp.you.getHealth() >= 80 && MainApp.you.getPotions() != 0) {
            // Sets to 100 as to not go over 100 health
            MainApp.you.setHealth(100);

        } else if (MainApp.you.getHealth() < 80 && MainApp.you.getPotions() != 0) {
            MainApp.you.setHealth(MainApp.you.getHealth() + 20);
        }
        MainApp.you.increasePotions(-1);
        // Updates text
        lblPotions.setText("You Have: " + MainApp.you.getPotions() + " Potions");
        lblHealth.setText("Health: " + MainApp.you.getHealth());
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private Image scale(String path) {
        // Scales the image to remove blur on big ImageViews
        Image image = new Image(getClass().getResource(path).toString());

        // Horizontal / Vertical scale (multiplied by 6)
        int scaleH = (int) image.getWidth() * 6;
        int scaleV = (int) image.getHeight() * 6;

        // Creates new image using this scale and sets it to output imageview
        return new Image(getClass().getResource(path).toString(), scaleH, scaleV, true, false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Potion updating
        lblPotions.setText("You Have: " + MainApp.you.getPotions() + " Potions");

        //starting timer
        movement.setCycleCount(Timeline.INDEFINITE);
        movement.play();

        //adding all AnchorPanes to the array
        panes[0] = ancForest1;
        panes[1] = ancForest2;
        panes[2] = ancForest3;
        panes[3] = ancForest4;
        panes[4] = ancForest5;
        panes[5] = ancInventory;
        panes[6] = ancLumberYard;

        //adding wall polygons to their arrays
        forest1[0] = polA1Entrance;
        forest1[1] = polA1Exit;
        forest1[2] = polA1Left;
        forest1[3] = polA1Right;

        forest2[0] = polA2Entrance;
        forest2[1] = polA2Exit;
        forest2[2] = polA2Bottom;
        forest2[3] = polA2Top;
        forest2[4] = polA2Left;
        forest2[5] = polA2Wizard;

        forest3[0] = polA3Entrance;
        forest3[1] = polA3Exit;
        forest3[2] = polA3Top;
        forest3[3] = polA3Bottom;

        forest4[0] = polA4Entrance;
        forest4[1] = polA4Exit;
        forest4[2] = polA4Top;
        forest4[3] = polA4Bottom;
        forest4[4] = polA4Right;
        forest4[5] = polA4Lumber;

        forest5[0] = polA5Entrance;
        forest5[1] = polA5Exit;
        forest5[2] = polA5Top;
        forest5[3] = polA5Bottom;

        //adding players into array for control in other scenes
        players[0] = panA1Player;
        players[1] = panA2Player;
        players[2] = panA3Player;
        players[3] = panA4Player;
        players[4] = panA5Player;
        players[5] = panLumberYardPlayer;

        //adding all player imageViews into array
        playerImages[0] = imgA1Player;
        playerImages[1] = imgA2Player;
        playerImages[2] = imgA3Player;
        playerImages[3] = imgA4Player;
        playerImages[4] = imgA5Player;
        playerImages[5] = imgLumberYardPlayer;

        //set image to correct class
        switch (MainApp.you.getClassInt()) {
            case 0:
                Image image = new Image(getClass().getResource("/images-world/warrior-front.png").toString());
                for (ImageView name : playerImages) {
                    name.setImage(image);
                }
                break;
            case 1:
                Image image1 = new Image(getClass().getResource("/images-world/archer-front.png").toString());
                for (ImageView name : playerImages) {
                    name.setImage(image1);
                }
                break;
            case 2:
                Image image2 = new Image(getClass().getResource("/images-world/mage-front.png").toString());
                for (ImageView name : playerImages) {
                    name.setImage(image2);
                }
                break;
        }

        //loading images and values into inventory
        invImage.add(imgInv1);
        invImage.add(imgInv2);
        invImage.add(imgInv3);
        invImage.add(imgInv4);
        invImage.add(imgInv5);
        invImage.add(imgEqp1);
        invImage.add(imgEqp2);

        MainApp.you.setSelected(-1);
        for (ImageView name : invImage) {
            name.setEffect(null);
        }
        imgEqp1.setEffect(dropShadow);
        update(5);

        imageChange(imgInv1, MainApp.you.getItemID(0));
        imageChange(imgInv2, MainApp.you.getItemID(1));
        imageChange(imgInv3, MainApp.you.getItemID(2));
        imageChange(imgInv4, MainApp.you.getItemID(3));
        imageChange(imgInv5, MainApp.you.getItemID(4));
        imageChange(imgEqp1, MainApp.you.getItemID(5));
        imageChange(imgEqp2, MainApp.you.getItemID(6));

        lblHerbs.setText("You Have: " + MainApp.you.getHerbs() + " Herbs");
        lblLogs.setText("You Have: " + MainApp.you.getLogs() + " Logs");
        lblRocks.setText("You Have: " + MainApp.you.getStones() + " Stone");

        //setting rock to invisible if broken
        if (MainApp.you.getProgress(1)) {
            imgRock.setVisible(false);
        }

        imgYard.setImage(scale("/maps/lumberClearing.png"));
        imgInv.setImage(scale("/Images-Inventory/panel_beige.png"));

        // Changes to specified pane when returning from combat or save file
        switch (MainApp.you.getMapNum()) {
            case 6:
                switchPane(ancForest1);
                break;
            case 7:
                switchPane(ancForest2);
                setX(forest.players[0], 190);
                setY(forest.players[0], 330);
                break;
            case 8:
                switchPane(ancForest3);
                setX(forest.players[0], 190);
                setY(forest.players[0], 330);

                setX(forest.players[1], 350);
                setY(forest.players[1], 230);
                break;
            case 9:
                switchPane(ancForest4);
                setX(forest.players[0], 190);
                setY(forest.players[0], 330);

                setX(forest.players[1], 350);
                setY(forest.players[1], 230);

                setX(forest.players[2], 340);
                setY(forest.players[2], 220);
                break;
            case 10:
                switchPane(ancForest5);
                setX(forest.players[0], 190);
                setY(forest.players[0], 330);

                setX(forest.players[1], 350);
                setY(forest.players[1], 230);

                setX(forest.players[2], 340);
                setY(forest.players[2], 220);

                setX(forest.players[3], 330);
                setY(forest.players[3], 320);
                break;
            case 11:
                switchPane(ancLumberYard);
                setX(forest.players[0], 190);
                setY(forest.players[0], 330);

                setX(forest.players[1], 350);
                setY(forest.players[1], 230);

                setX(forest.players[2], 340);
                setY(forest.players[2], 220);

                setX(forest.players[3], 330);
                setY(forest.players[3], 60);
        }

        // Sets your position in this map 346 51
        setX(getCharacterPane(), MainApp.you.getX());
        setY(getCharacterPane(), MainApp.you.getY());

        //start music
//        a = new MediaPlayer((new Media(getClass().getResource("/music/forest.mp3").toString())));
//        a.setCycleCount(MediaPlayer.INDEFINITE);
//        a.play();
    }
}