package dylanJaxon;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
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
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;

public class town implements Initializable {
    @FXML
    public AnchorPane ancCastle, ancShop, ancLumberjack, ancSmith, ancInventory, ancWizard, ancTown, ancUpgrade;

    @FXML
    private Polygon polUpgr, polLumberjackWalls, polLumberjackEntry, polKing, polCastleEntry, polWizardTable, polCastleWalls, polShopWall, polShopEntry, polSmithWalls, polSmithEntry, polWizardWall, polWizardEntry, polCastlePlayer, polShopPlayer, polSmithPlayer, polWizardPlayer, polLumberJackPlayer;

    @FXML
    private Label lblBal, lblContinue, lblStory, lblHerbs, lblLogs, lblRocks, lblPotions, lblHealth, lblBalance, lblDmgProt, lblDmgProtAmnt, lblDurabilityAmnt;

    @FXML
    private ImageView imgInv1, imgInv2, imgInv3, imgInv4, imgInv5, imgEqp1, imgEqp2, imgCastlePlayer, imgShopPlayer, imgSmithPlayer, imgWizardPlayer, imgLumberJackPlayer;

    @FXML private ImageView imgQuest1, imgQuest2, imgQuest3;

    @FXML
    private Pane panCastlePlayer, panShopPlayer, panSmithPlayer, panWizardPlayer, panLumberJackPlayer;

    // Backgrounds of each area (used for scaling)
    @FXML
    private ImageView imgShop, imgTower, imgSmith, imgLumber, imgYard, imgInv, imgUpr0, imgUpr1, imgUpr2, imgUpr3, imgUpr4, imgUpr5, imgUpr6;

    // The "Press E to Interact" for each Anchor Pane
    @FXML
    private Label lblCastleInteract, lblShopInteract, lblSmithInteract, lblLumberInteract, lblWizardInteract, lblUprProt, lblDmgUpr, lblDurUpr, lblRepairCost, lblUprCost;

    // Polygon that you will collide with to have the interact label show up
    @FXML
    private Polygon plyWizardInteract, plyShopInteract, plySmithInteract, plyLumberInteract;

    // Shop Anchor pane
    @FXML
    private AnchorPane ancShopMenu;

    // Objects in shop menu
    @FXML
    private Label lblSell, lblStats, lblResources, lblGold, lblRate, lblSeller, lblSmithUpgrade;
    @FXML
    private ImageView imgLeft, imgRight, imgItem;

    // Current index of the item you're viewing in the shop
    int index = 0;

    // Delay of shop messages (not enough money/can't buy that item)
    int shopDelay = 0;

    // Arrays of each seller's itemIDs
    private final int[] shopID = {6, 7, 8, 9, 16, 20};
    private final int[] lumberID = {21, 22, 23};
    private final int[] smithID = {1, 2, 3, 4, 17, 18, 19};
    private final int[] wizardID = {11, 12, 13, 14, 20};
    ImageView[] playerImages = new ImageView[5];

    ArrayList<ImageView> invImage = new ArrayList<ImageView>();
    ArrayList<ImageView> uprImage = new ArrayList<ImageView>();

    static AnchorPane[] anchorPanes = new AnchorPane[7];

    //saves location for multi part dialogues
    int dialogueLoc = 0;
//    MediaPlayer a;

    // Boolean for if you should run the final cutscene
    public static boolean finalCutscene = false;

    public static void switchPane(AnchorPane pane) {
        for (AnchorPane name : anchorPanes) {
            name.setVisible(false);
        }
        pane.setVisible(true);
    }

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

    private void move() throws IOException {
        //moves the player for all panes
        if (ancCastle.isVisible()) {
            setX(panCastlePlayer, getX(panCastlePlayer) + x);
            setY(panCastlePlayer, getY(panCastlePlayer) + y);

            //opens dialogue with the king
            if (collision(polCastlePlayer, polKing)) {
                setX(panCastlePlayer, getX(panCastlePlayer) - x);
                setY(panCastlePlayer, getY(panCastlePlayer) - y);
                movement.stop();

                lblStory.setVisible(true);
                lblContinue.setVisible(true);
                if (MainApp.you.getProgress(6)) {
                    lblStory.setText("What is it Sir " + MainApp.you.getName() + "?");
                    dialogueLoc = 2;
                } else if (MainApp.you.getProgress(2)) {
                    lblStory.setText("You've done well so far soldier.  \n\n" +
                            "You have reached the Demon King's lair, now all \nthat is left is to slay them.  \n\n" +
                            "Gear up and get ready for the final battle\n\n" +
                            "Keep up the good work and good luck soldier.");
                    dialogueLoc = 2;
                } else if (MainApp.you.getProgress(1)) {
                    lblStory.setText("You've done well so far soldier.  \n" +
                            "The Demon King's army is severely weakened and you have almost reached his lair.  \n" +
                            "Keep up the good work and good luck soldier.");
                    dialogueLoc = 2;
                } else if (MainApp.you.getProgress(0)) {
                    lblStory.setText("You've done well so far soldier.  \n" +
                            "But the Demon King's army is still strong. \n" +
                            "Keep up the good work and good luck soldier.");
                    dialogueLoc = 2;
                } else {
                    lblStory.setText("Welcome brave soldier.  \n" +
                            "Our kingdom is in need of our help.");
                    dialogueLoc = 0;
                }
            }
            if (collision(polCastlePlayer, polCastleWalls)) {
                setX(panCastlePlayer, getX(panCastlePlayer) - x);
                setY(panCastlePlayer, getY(panCastlePlayer) - y);
            }
            if (collision(polCastlePlayer, polCastleEntry)) {
                setX(panCastlePlayer, getX(panCastlePlayer) - x);
                setY(panCastlePlayer, getY(panCastlePlayer) - y);
                switchPane(ancTown);
                MainApp.you.setMapNum(18);
                MainApp.setSize(391, 391);
            }
        } else if (ancShop.isVisible()) {
            setX(panShopPlayer, getX(panShopPlayer) + x);
            setY(panShopPlayer, getY(panShopPlayer) + y);

            if (collision(polShopPlayer, polShopWall)) {
                setX(panShopPlayer, getX(panShopPlayer) - x);
                setY(panShopPlayer, getY(panShopPlayer) - y);
            }
            if (collision(polShopPlayer, polShopEntry)) {
                setX(panShopPlayer, getX(panShopPlayer) - x);
                setY(panShopPlayer, getY(panShopPlayer) - y);
                switchPane(ancTown);
                MainApp.setSize(391, 391);
                MainApp.you.setMapNum(18);
            }

            shopCollision(polShopPlayer, plyShopInteract, lblShopInteract, shopID);

        } else if (ancSmith.isVisible()) {
            setX(panSmithPlayer, getX(panSmithPlayer) + x);
            setY(panSmithPlayer, getY(panSmithPlayer) + y);

            if (collision(polSmithPlayer, polSmithWalls)) {
                setX(panSmithPlayer, getX(panSmithPlayer) - x);
                setY(panSmithPlayer, getY(panSmithPlayer) - y);
            }
            if (collision(polSmithPlayer, polSmithEntry)) {
                setX(panSmithPlayer, getX(panSmithPlayer) - x);
                setY(panSmithPlayer, getY(panSmithPlayer) - y);
                switchPane(ancTown);
                MainApp.setSize(391, 391);
                MainApp.you.setMapNum(18);
            }
            if (collision(polSmithPlayer, polUpgr)) {
                lblSmithUpgrade.setVisible(true);
            } else {
                lblSmithUpgrade.setVisible(false);
                ancUpgrade.setVisible(false);
            }

            shopCollision(polSmithPlayer, plySmithInteract, lblSmithInteract, smithID);

        } else if (ancWizard.isVisible()) {
            setX(panWizardPlayer, getX(panWizardPlayer) + x);
            setY(panWizardPlayer, getY(panWizardPlayer) + y);

            if (collision(polWizardPlayer, polWizardWall) || collision(polWizardPlayer, polWizardTable)) {
                setX(panWizardPlayer, getX(panWizardPlayer) - x);
                setY(panWizardPlayer, getY(panWizardPlayer) - y);
            }
            if (collision(polWizardPlayer, polWizardEntry)) {
                setX(panWizardPlayer, getX(panWizardPlayer) - x);
                setY(panWizardPlayer, getY(panWizardPlayer) - y);
                movement.stop();
                MainApp.setRoot("forest", "");
                forest.switchPane(forest.panes[1]);
                MainApp.you.setMapNum(7);
                MainApp.setSize(391, 391);

                setX(forest.players[0], 190);
                setY(forest.players[0], 330);
                setX(forest.players[0], 225);
                setY(forest.players[0], 340);

                setX(forest.players[1], 128);
                setY(forest.players[1], 300);

                //a.stop();
            }

            shopCollision(polWizardPlayer, plyWizardInteract, lblWizardInteract, wizardID);

        } else if (ancLumberjack.isVisible()) {
            setX(panLumberJackPlayer, getX(panLumberJackPlayer) + x);
            setY(panLumberJackPlayer, getY(panLumberJackPlayer) + y);

            if (collision(polLumberJackPlayer, polLumberjackWalls)) {
                setX(panLumberJackPlayer, getX(panLumberJackPlayer) - x);
                setY(panLumberJackPlayer, getY(panLumberJackPlayer) - y);
            }
            if (collision(polLumberJackPlayer, polLumberjackEntry)) {
                setX(panLumberJackPlayer, getX(panLumberJackPlayer) - x);
                setY(panLumberJackPlayer, getY(panLumberJackPlayer) - y);
                switchPane(ancTown);
                MainApp.setSize(391, 391);
                MainApp.you.setMapNum(18);
            }

            shopCollision(polLumberJackPlayer, plyLumberInteract, lblLumberInteract, lumberID);

        }

        // Shop delay to let player read warnings (ex out of money text)
        if (shopDelay > 1) {
            --shopDelay;

        } else if (shopDelay == 1) {
            --shopDelay;
            // Re-displays the stats label
            vendorItem(vendorArray()[index]);
        }
    }

    private void shopCollision(Polygon player, Polygon interact, Label label, int[] shopIDs) {
        // Collision with polygon to show interact label (wont show if the shop is already open)
        if (collision(player, interact) && !ancShopMenu.isVisible()) {
            label.setVisible(true);
            vendorItem(shopIDs[index]);

        } else {
            label.setVisible(false);

            // Removes shop menu when you leave the interaction polygon
            if (!collision(player, interact)) {
                ancShopMenu.setVisible(false);
                index = 0;
                imgLeft.setVisible(false);
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

            case E:
                if (lblShopInteract.isVisible()) {
                    // Creates the shop (values change depending on where you are)
                    shopObjects("Shopkeeper", "Herbs", 5, MainApp.you.getHerbs());
                    moveShop(275);
                    imageChange(imgItem, shopID[0]);

                } else if (lblSmithInteract.isVisible()) {
                    shopObjects("Blacksmith", "Stones", 15, MainApp.you.getStones());
                    moveShop(275);
                    imageChange(imgItem, smithID[0]);

                } else if (lblLumberInteract.isVisible()) {
                    shopObjects("Lumberjack", "Logs", 10, MainApp.you.getLogs());
                    moveShop(15);
                    imageChange(imgItem, lumberID[0]);

                } else if (lblWizardInteract.isVisible()) {
                    shopObjects("Wizard", "Herbs", 5, MainApp.you.getHerbs());
                    moveShop(125);
                    imageChange(imgItem, wizardID[0]);
                } else if (collision(polSmithPlayer, polUpgr)) {
                    //shows the shop
                    ancUpgrade.setVisible(true);
                    //unselects all items then selects item 5
                    MainApp.you.setSelected(-1);
                    for (ImageView name : uprImage) {
                        name.setEffect(null);
                    }
                    imgUpr5.setEffect(dropShadow);
                    MainApp.you.setSelected(6);
                    updateUpr(5);

                    //displays cost of upgrades
                    lblUprCost.setText("Upgrade Cost: " + (5 + (MainApp.you.getUpgrade())) + "g");
                    lblBal.setText("Gold: " + MainApp.you.getBalance());

                    //shows all image items
                    imageChange(imgUpr0, MainApp.you.getItemID(0));
                    imageChange(imgUpr1, MainApp.you.getItemID(1));
                    imageChange(imgUpr2, MainApp.you.getItemID(2));
                    imageChange(imgUpr3, MainApp.you.getItemID(3));
                    imageChange(imgUpr4, MainApp.you.getItemID(4));
                    imageChange(imgUpr5, MainApp.you.getItemID(5));
                    imageChange(imgUpr6, MainApp.you.getItemID(6));

                    if (MainApp.you.getUpgrade() >= 5) {
                        lblUprCost.setText("Upgrade Cost: MAX LEVEL");
                    } else {
                        lblUprCost.setText("Upgrade Cost: " + (5 + (MainApp.you.getUpgrade() * 5)) + "g");
                    }

                    if (MainApp.you.getItemID(MainApp.you.getSelected() - 1) > 14) {
                        weapon weapon = new weapon(MainApp.you.getItemID(MainApp.you.getSelected() - 1));
                        if (weapon.getDurability() == MainApp.you.getDurability(MainApp.you.getSelected() - 1)) {
                            lblRepairCost.setText("Repair Cost: MAX DURABILITY");
                        }
                    } else {
                        armour armour = new armour(MainApp.you.getItemID(MainApp.you.getSelected() - 1));
                        if (armour.getDurability() == MainApp.you.getDurability(MainApp.you.getSelected() - 1)) {
                            lblRepairCost.setText("Repair Cost: MAX DURABILITY");
                        }
                    }
                }
                break;
            //Continues dialogue
            case SPACE:
                if (lblStory.isVisible()) {
                    if (dialogueLoc == 2) {
                        if (!MainApp.you.getProgress(0)) {
                            MainApp.you.setProgress(0, true);
                        }
                        dialogueLoc = 0;
                        lblStory.setVisible(false);
                        lblContinue.setVisible(false);

                        lblContinue.setLayoutY(195);
                        lblStory.setLayoutY(215);
                        movement.setCycleCount(Timeline.INDEFINITE);
                        movement.play();
                    } else if (dialogueLoc == 1) {
                        lblStory.setText("Upon returning successfully, \n" +
                                " the kingdom will award you the position of a knight.  \n" +
                                "As well as two thousand gold and a manor in the city.  \n" +
                                "Thank you and I wish you good luck.  ");
                        dialogueLoc++;

                    } else if (dialogueLoc == 0) {
                        lblStory.setText("The Demon King is invading from the forest\nand has slain most of our army.  \n" +
                                "Though in the process, their soldiers\nhave been severely weakened.  \n" +
                                "Your job will be to finish them all off for us.  \n" +
                                "Their lair is in the cave on the other side of the forest.");
                        dialogueLoc++;
                    }
                }
                break;
            //opens and closes inventory
            case ESCAPE:
                if (lblStory.isVisible() || ancShopMenu.isVisible() || ancUpgrade.isVisible()) {
                    return;
                }
                if (ancInventory.isVisible()) {
                    newArea();

                } else {
                    // Updating
                    lblPotions.setText("You Have: " + MainApp.you.getPotions() + " Potions");
                    lblHealth.setText("Health: " + MainApp.you.getHealth());
                    lblBalance.setText("Gold: " + MainApp.you.getBalance() + "g");

                    lblHerbs.setText("You Have: " + MainApp.you.getHerbs() + " Herbs");
                    lblLogs.setText("You Have: " + MainApp.you.getLogs() + " Logs");
                    lblRocks.setText("You Have: " + MainApp.you.getStones() + " Stone");

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

                    // Resets selected item to your equipped weapon
                    MainApp.you.setSelected(-1);
                    for (ImageView name : invImage) {
                        name.setEffect(null);
                    }
                    imgEqp1.setEffect(dropShadow);
                    update(5);

                    switchPane(ancInventory);
                    MainApp.setSize(435, 435);
                }
                break;
            case F1:
                MainApp.you.increaseBalance(100);
        }
    }

    private void newArea() {
        switch (MainApp.you.getMapNum()) {
            case 1:
                switchPane(ancCastle);
                MainApp.setSize(430, 390);
                break;
            case 2:
                switchPane(ancShop);
                MainApp.setSize(324, 555);
                break;
            case 3:
                switchPane(ancSmith);
                MainApp.setSize(324, 555);
                break;
            case 4:
                switchPane(ancLumberjack);
                MainApp.setSize(324, 555);
                break;
            case 5:
                switchPane(ancWizard);
                MainApp.setSize(395, 390);
                break;
            case 18:
                switchPane(ancTown);
                MainApp.setSize(390, 390);
                break;
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
                break;
        }
    }

// Shop Section
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void shopObjects(String seller, String selling, int rate, int resourceCount) {
        // Sets up shop
        lblSeller.setText(seller);
        lblSell.setText("Sell " + selling);
        lblRate.setText(rate + "g / each");
        lblResources.setText(selling + ": " + resourceCount);
        lblGold.setText("Gold: " + MainApp.you.getBalance() + "g");

        // Only right button visible as it starts at first index
        imgLeft.setVisible(false);
        imgRight.setVisible(true);

        ancShopMenu.setVisible(true);
    }

    private void moveShop(int x) {
        setX(ancShopMenu, x);
        setY(ancShopMenu, 15);
    }

    @FXML
    void shopIndex(MouseEvent event) {
        ImageView source = (ImageView) event.getSource();
        // vendorArray() gets the array of items the current vendor is selling
        int[] itemIDs = vendorArray();

        if (source == imgRight) {
            // Increases index and changes the item image
            ++index;
            vendorItem(itemIDs[index]);

            // Checks if the current index is the last index in the array
            if (Arrays.binarySearch(itemIDs, itemIDs[index]) == itemIDs.length - 1) {
                // Disables right button if last
                imgRight.setVisible(false);
            }

            // Re-enables opposite button
            imgLeft.setVisible(true);

        } else if (source == imgLeft) {
            // Decrease index and changes item image
            --index;
            vendorItem(itemIDs[index]);

            // Checks if te current index is the first index in the array
            if (Arrays.binarySearch(itemIDs, itemIDs[index]) == 0) {
                // Disables left button if first
                imgLeft.setVisible(false);
            }

            // Re-enables opposite button
            imgRight.setVisible(true);
        }
    }

    private void vendorItem(int itemID) {
        // Creates the new weapon/amour depending on its itemID
        if (itemID >= 0 && itemID < 15) {
            weapon weapon = new weapon(itemID);

            // Shows stats on screen
            lblStats.setText("Damage: " + weapon.getDamage() +
                    "\nDurability: " + weapon.getDurability() +
                    "\nCost: " + cost(itemID) + "g");

        } else if (itemID >= 15 && itemID <= 19) {
            armour armour = new armour(itemID);

            // Shows stats on screen
            lblStats.setText("Defense: " + armour.getDefense() +
                    "\nDurability: " + armour.getDurability() +
                    "\nCost: " + cost(itemID) + "g");

        } else if (itemID >= 20) {
            // only show cost for non weapon/armour items
            lblStats.setText("Cost: " + cost(itemID) + "g");
        }

        imageChange(imgItem, itemID);
    }

    private int[] vendorArray() {
        // Returns the array of what the seller is selling
        switch (lblSeller.getText()) {
            // Array of the item IDs the vendor is selling is dependent on who the seller is
            case "Shopkeeper":
                return shopID;

            case "Blacksmith":
                return smithID;

            case "Wizard":
                return wizardID;

            default /*Lumberjack*/:
                // needs to be default or else it would error: (itemIDs may not have been initialized)
                return lumberID;
        }
    }

    private int cost(int itemID) {
        // Returns the cost of each itemID
        switch (itemID) {
            case 0:
                return 0;
            case 1:
                return 85;
            case 2:
                return 150;
            case 3:
                return 250;
            case 4:
                return 325;
            case 5:
                return 0;
            case 6:
                return 90;
            case 7:
                return 190;
            case 8:
                return 250;
            case 9:
                return 350;
            case 10:
                return 0;
            case 11:
                return 100;
            case 12:
                return 200;
            case 13:
                return 300;
            case 14:
                return 400;
            case 15:
                return 0;
            case 16:
                return 100;
            case 17:
                return 200;
            case 18:
                return 350;
            case 19:
                return 400;
            // Custom Items
            case 20: // Potions
                return 4;
            case 21: // Axe
                return 100;
            case 22: // Pickaxe
                return 150;
            case 23: // Upgraded Pickaxe
                return 350;
        }
        return 0;
    }

    private boolean preventPurchase(int ID) {
        // Prevents you from buying if the ID falls between the min and max
        switch (MainApp.you.getClassInt()) {
            case 0:
                // Returns true for warriors if between 5-14 (bows and staves)
                return ID >= 5 && ID <= 14;
            case 1:
                // Return true for archers if between 0-4 or 10-14
                return (ID >= 0 && ID <= 4) || (ID >= 10 && ID <= 14);
            default /*2*/:
                // Returns true for mages if between 0-9
                return ID >= 0 && ID <= 9;
        }
    }

    private boolean duplicate(int ID) {
        switch (ID) {
            case 21:
                return MainApp.you.getProgress(3);
            case 22:
                return MainApp.you.getProgress(4);
            case 23:
                return MainApp.you.getProgress(5);
            default:
                return true;
        }
    }

    @FXML
    void buy(MouseEvent event) {
        // Array of items that the vendor is selling (gets cost of current index and compares to balance)
        if (cost(vendorArray()[index]) > MainApp.you.getBalance()) {
            // Not enough money code
            lblStats.setText("You don't have\nenough money!");

            // 2.5 Second Delay
            shopDelay = 50;
        } else {
            // Index of the open spot in your inventory
            int openSpot = -1;
            for (int i = 0; i <= 5; i++) {
                // If the itemID is -1 (empty slot, openSlot becomes i)
                if (MainApp.you.getItemID(i) == -1) {
                    openSpot = i;
                    break;
                }
            }

            if (preventPurchase(vendorArray()[index])) {
                // Item belongs to another class
                lblStats.setText("Your class can't\nuse this item!");
                shopDelay = 50;

            } else if (vendorArray()[index] >= 21) {
                // Checks if you already have that item
                if (duplicate(vendorArray()[index])) {
                    lblStats.setText("You already have\nthis item!");
                    shopDelay = 50;

                } else {
                    // Checks if you have bought the item below it
                    // This is checked so that you can't skip quest items
                    // -29 will get you the index of the quest item below it in the progress array
                    if (vendorArray()[index] >= 22 && !MainApp.you.getProgress(vendorArray()[index] - 19)) {
                        lblStats.setText("You must purchase\nthe previous item\nfirst!");
                        shopDelay = 50;

                    } else {
                        // Sets progress to true so that you can use the item
                        MainApp.you.setProgress(vendorArray()[index] - 18, true);

                        MainApp.you.increaseBalance(-cost(vendorArray()[index]));
                        lblGold.setText("Gold: " + MainApp.you.getBalance() + "g");
                    }
                }
            } else if (openSpot == -1) {
                // Full inventory
                lblStats.setText("Your inventory \nis full!");
                shopDelay = 50;
            } else {
                // Decreases balance (negative cost) & Updates label
                MainApp.you.increaseBalance(-cost(vendorArray()[index]));
                lblGold.setText("Gold: " + MainApp.you.getBalance() + "g");

                if (vendorArray()[index] == 20) {
                    // Buying potions
                    MainApp.you.increasePotions(1);
                    lblPotions.setText("You have: " + MainApp.you.getPotions() + " Potions");
                } else {
                    // Regular items Setting ID
                    MainApp.you.setID(openSpot, vendorArray()[index]);

                    // Durability dependent on if its armour or weapon
                    if (vendorArray()[index] >= 15 && vendorArray()[index] <= 19) {
                        // Gets the durability of the new armour
                        MainApp.you.setDurability(openSpot, new armour(vendorArray()[index]).getDurability());
                    } else {
                        // Gets the durability of the new weapon
                        MainApp.you.setDurability(openSpot, new weapon(vendorArray()[index]).getDurability());
                    }
                }
            }
        }
    }

    @FXML
    void sell(MouseEvent event) {
        // Chooses which resource to sell depending on the seller name
        switch (lblSeller.getText()) {
            case "Wizard":
                // Wizard also sells herbs (no break;)
            case "Shopkeeper":
                // Selling herbs
                if (MainApp.you.getHerbs() == 0) {
                    lblStats.setText("You don't have\nany herbs to sell!");
                    shopDelay = 50;

                } else {
                    // Increases balance by the amount of herbs
                    MainApp.you.increaseBalance(MainApp.you.getHerbs() * 5);
                    MainApp.you.setHerbs(0);

                    shopDelay = 50;

                    lblResources.setText("Herbs: 0");
                    lblGold.setText("Gold: " + MainApp.you.getBalance() + "g");
                }
                break;
            case "Blacksmith":
                // Selling stones
                if (MainApp.you.getStones() == 0) {
                    lblStats.setText("You don't have\nany stones to sell!");
                    shopDelay = 50;

                } else {
                    // Increases balance by the amount of stones * 15
                    MainApp.you.increaseBalance(MainApp.you.getStones() * 15);
                    MainApp.you.setStones(0);

                    shopDelay = 50;

                    lblResources.setText("Stones: 0");
                    lblGold.setText("Gold: " + MainApp.you.getBalance() + "g");
                    ;
                }
                break;
            case "Lumberjack":
                // Selling logs
                if (MainApp.you.getLogs() == 0) {
                    lblStats.setText("You don't have\nany logs to sell!");
                    shopDelay = 50;

                } else {
                    // Increases balance by the amount of logs * 10
                    MainApp.you.increaseBalance(MainApp.you.getLogs() * 10);
                    MainApp.you.setLogs(0);

                    shopDelay = 50;

                    lblResources.setText("Logs: 0");
                    lblGold.setText("Gold: " + MainApp.you.getBalance() + "g");
                }
                break;
        }
    }

    // Shop Section
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @FXML
    void btnUpr(MouseEvent event) {
        // Source
        ImageView source = (ImageView) event.getSource();

        // Sets selected (used for equipping items)
        if (uprImage.indexOf(source) >= 8) {
            MainApp.you.setSelected(-1);
        } else {
            MainApp.you.setSelected(uprImage.indexOf(source) + 1);
        }

        // Removes all current effects
        for (ImageView name : uprImage) {
            name.setEffect(null);
        }
        // Add dropshadow effect
        source.setEffect(dropShadow);

        // Updates damage/defense
        updateUpr(uprImage.indexOf(source));
        if (MainApp.you.getUpgrade() >= 5) {
            lblUprCost.setText("Upgrade Cost: MAX LEVEL");
        } else {
            lblUprCost.setText("Upgrade Cost: " + (5 + (MainApp.you.getUpgrade() * 5)) + "g");
        }

        //checks if durability is max when selecting an item
        boolean durabilityMax = false;
        int ID = MainApp.you.getItemID(MainApp.you.getSelected() - 1);
        int dur = MainApp.you.getDurability(MainApp.you.getSelected() - 1);

        if (ID > 14) {
            if (new weapon(ID).getDurability() == dur) {
                durabilityMax = true;
            }
        } else {
            if (new armour(ID).getDurability() == dur) {
                durabilityMax = true;
            }
        }

        //sets text to say durability is max, if max durability
        if (durabilityMax) {
            lblRepairCost.setText("Repair Cost: MAX DURABILITY");
        } else {
            lblRepairCost.setText("Repair Cost: 5");
        }
    }

    @FXML
    void btnUpgrade(MouseEvent event) {
        // Makes sure the slot isn't empty
        if (MainApp.you.getItemID(MainApp.you.getSelected() - 1) != -1) {

            if ((MainApp.you.getBalance()) < (MainApp.you.getUpgrade() * 5)) {
                return;
            }
            if (MainApp.you.getUpgrade() >= 5) {
                return;
            }

            MainApp.you.increaseBalance(-(5 + (MainApp.you.getUpgrade() * 5)));
            lblBal.setText("Gold: " + MainApp.you.getBalance());
            MainApp.you.upgrade();
            lblUprCost.setText("Upgrade Cost: " + (5 + (MainApp.you.getUpgrade() * 5)) + "g");
            updateUpr(MainApp.you.getSelected() - 1);
            if (MainApp.you.getUpgrade() == 5) {
                lblUprCost.setText("Upgrade Cost: MAX LEVEL");
            }
        }
    }

    @FXML
    void btnRepair(MouseEvent event) {
        // Makes sure the slot isn't empty
        if (MainApp.you.getItemID(MainApp.you.getSelected() - 1) != -1) {

            if (MainApp.you.getItemID(MainApp.you.getSelected() - 1) > 14) {
                weapon weapon = new weapon(MainApp.you.getItemID(MainApp.you.getSelected() - 1));
                if (weapon.getDurability() == MainApp.you.getDurability(MainApp.you.getSelected() - 1)) {
                    return;
                }
            } else {
                armour armour = new armour(MainApp.you.getItemID(MainApp.you.getSelected() - 1));
                if (armour.getDurability() == MainApp.you.getDurability(MainApp.you.getSelected() - 1)) {
                    return;
                }
            }
            if (MainApp.you.getBalance() < 5) {
                return;
            }

            MainApp.you.repair();
            updateUpr(MainApp.you.getSelected() - 1);
            lblRepairCost.setText("Repair Cost: MAX DURABILITY");
            MainApp.you.increaseBalance(-5);
            lblBal.setText("Gold: " + MainApp.you.getBalance());
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //return to menu
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
            case 1:
                return panCastlePlayer;
            case 2:
                return panShopPlayer;
            case 3:
                return panSmithPlayer;
            case 4:
                return panLumberJackPlayer;
            case 5:
                return panWizardPlayer;
            default:
                // Happens if the user saved in the room selection screen (returns empty pane)
                Pane pane = new Pane();
                setX(pane, 0);
                setY(pane, 0);
                return pane;
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //sets view to desired location
    @FXML
    void btnSmith(ActionEvent event) {
        MainApp.setSize(324, 555);
        switchPane(ancSmith);
        MainApp.you.setMapNum(3);
        movement.stop();
        lblStory.setLayoutY(0);
        lblContinue.setLayoutY(225);
        lblStory.setVisible(true);
        lblContinue.setVisible(true);
        lblStory.setText("Need your weapons,  armour or tool’s upgraded?  \n" +
                "Well you’ve come to the right place, I’m the best smith for the next 5 miles\n" +
                "So how can I help you on this fine, fine day?");
        dialogueLoc = 2;
    }

    @FXML
    void btnShop(ActionEvent event) {
        MainApp.setSize(324, 555);
        switchPane(ancShop);
        MainApp.you.setMapNum(2);
        movement.stop();
        lblStory.setLayoutY(0);
        lblContinue.setLayoutY(225);
        lblStory.setVisible(true);
        lblContinue.setVisible(true);
        lblStory.setText("Welcome to Stonehovel’s one and only general store.  \n" +
                "We’ve got the best wares and shares for all your needs.  \n" +
                "So, what is it that I can do for ya good sir?");
        dialogueLoc = 2;
    }

    @FXML
    void btnForest(ActionEvent event) throws IOException {
        movement.stop();
        MainApp.setRoot("forest", "");
        MainApp.you.setMapNum(6);
        //a.stop();
    }

    @FXML
    void btnLumberJack(ActionEvent event) {
        MainApp.setSize(324, 555);
        switchPane(ancLumberjack);
        MainApp.you.setMapNum(4);
        movement.stop();
        lblStory.setLayoutY(0);
        lblContinue.setLayoutY(225);
        lblStory.setVisible(true);
        lblContinue.setVisible(true);

        if (MainApp.you.getLogs() == 0) {
            lblStory.setText("So, got any logs for me kid.  \n\n" +
                    "I guess not, come back when you got some for me.  \n\n" +
                    "If you got any logs I’ll buy ‘em off ya for 10 gold each");
            dialogueLoc = 2;
        } else {
            lblStory.setText("So, got any logs for me kid.  \n\n" +
                    "You do, do ya.  Here hand em’ to me \nand I’ll give you the gold I owe ya \n\n" +
                    "By the way, if you got anymore logs \nI’ll buy ‘em off ya for 10 gold each");
            dialogueLoc = 2;
        }
    }

    @FXML
    void btnCastle(ActionEvent event) {
        MainApp.setSize(430, 390);
        switchPane(ancCastle);
        MainApp.you.setMapNum(1);
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
            // Add Drop Shadow effect
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

    //used to update the text on the right of the inventory
    private void updateUpr(int loc) {
        //sets the item id of the item being used
        int ID = MainApp.you.getItemID(loc);
        //if id is -1 there is no item, so return, if its greater than 14 it is armor, else it is a weapon
        if (ID == -1) {
            lblDmgUpr.setText(":");
            lblDurUpr.setText(":");
        } else if (ID > 14) {
            lblUprProt.setText("Protection");
            armour name = new armour(MainApp.you.getItemID(loc));

            lblDmgUpr.setText(": " + (name.getDefense() + (MainApp.you.getDamage(loc) * 2)));
            lblDurUpr.setText(": " + (MainApp.you.getDurability(loc)));
        } else {
            lblUprProt.setText("Damage");
            weapon name = new weapon(MainApp.you.getItemID(loc));

            lblDmgUpr.setText(": " + (name.getDamage() + (MainApp.you.getDamage(loc) * 2)));
            lblDurUpr.setText(": " + (MainApp.you.getDurability(loc)));
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
            case 20:
                Image image20 = new Image(getClass().getResource("/Item-Sprites/potion.png").toString());
                imageView.setImage(image20);
                break;
            case 21:
                Image image21 = scale("/Item-Sprites/axe.png");
                imageView.setImage(image21);
                break;
            case 22:
                Image image22 = scale("/Item-Sprites/pickaxe.png");
                imageView.setImage(image22);
                break;
            case 23:
                Image image23 = scale("/Item-Sprites/pickaxe-upgrade.png");
                imageView.setImage(image23);
                break;
            case -1:
                // Resets imageview
                imageView.setImage(null);
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


    private Image scale(String path) {
        // Scales the image to remove blur on big ImageViews
        Image image = new Image(getClass().getResource(path).toString());

        // Horizontal / Vertical scale (multiplied by 6)
        int scaleH = (int) image.getWidth() * 6;
        int scaleV = (int) image.getHeight() * 6;

        // Creates new image using this scale and sets it to output imageview
        return new Image(getClass().getResource(path).toString(), scaleH, scaleV, true, false);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Well rested!
        MainApp.you.setHealth(100);

        // Potion updating
        lblPotions.setText("You Have: " + MainApp.you.getPotions() + " Potions");

        movement.setCycleCount(Timeline.INDEFINITE);
        movement.play();

        anchorPanes[0] = ancCastle;
        anchorPanes[1] = ancShop;
        anchorPanes[2] = ancSmith;
        anchorPanes[3] = ancWizard;
        anchorPanes[4] = ancLumberjack;
        anchorPanes[5] = ancInventory;
        anchorPanes[6] = ancTown;

        //loading images into inventory
        invImage.add(imgInv1);
        invImage.add(imgInv2);
        invImage.add(imgInv3);
        invImage.add(imgInv4);
        invImage.add(imgInv5);
        invImage.add(imgEqp1);
        invImage.add(imgEqp2);

        uprImage.add(imgUpr0);
        uprImage.add(imgUpr1);
        uprImage.add(imgUpr2);
        uprImage.add(imgUpr3);
        uprImage.add(imgUpr4);
        uprImage.add(imgUpr5);
        uprImage.add(imgUpr6);

        MainApp.you.setSelected(-1);
        for (ImageView name : invImage) {
            name.setEffect(null);
        }
        imgEqp1.setEffect(dropShadow);
        update(5);

        lblHerbs.setText("You Have: " + MainApp.you.getHerbs() + " Herbs");
        lblLogs.setText("You Have: " + MainApp.you.getLogs() + " Logs");
        lblRocks.setText("You Have: " + MainApp.you.getStones() + " Stone");

        //adding all player imageViews into array
        playerImages[0] = imgCastlePlayer;
        playerImages[1] = imgShopPlayer;
        playerImages[2] = imgSmithPlayer;
        playerImages[3] = imgWizardPlayer;
        playerImages[4] = imgLumberJackPlayer;

        //loads images into upgrade menu
        imageChange(imgUpr0, MainApp.you.getItemID(0));
        imageChange(imgUpr1, MainApp.you.getItemID(1));
        imageChange(imgUpr2, MainApp.you.getItemID(2));
        imageChange(imgUpr3, MainApp.you.getItemID(3));
        imageChange(imgUpr4, MainApp.you.getItemID(4));
        imageChange(imgUpr5, MainApp.you.getItemID(5));
        imageChange(imgUpr6, MainApp.you.getItemID(6));
        lblBal.setText("Gold: " + MainApp.you.getBalance());

        //set image to correct class
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

        lblRocks.setText("You Have: " + MainApp.you.getStones() + " Stone");

        // Removes image blur
        imgShop.setImage(scale("/maps/Shop.png"));
        imgTower.setImage(scale("/maps/wizardTower.png"));
        imgSmith.setImage(scale("/maps/blackmith.png"));
        imgLumber.setImage(scale("/maps/lumberJack.png"));
        imgYard.setImage(scale("/maps/lumberClearing.png"));
        imgInv.setImage(scale("/Images-Inventory/panel_beige.png"));

        // Changes to specified pane when returning from combat or save file
        newArea();

        // Sets your position in this map
        setX(getCharacterPane(), MainApp.you.getX());
        setY(getCharacterPane(), MainApp.you.getY());

        //set health to 100 everytime you enter the town
        MainApp.you.setHealth(100);

        if (MainApp.you.getProgress(6) && finalCutscene) {
            dialogueLoc = 2;

            // Final Cutscene
            lblStory.setVisible(true);
            lblContinue.setVisible(true);
            lblStory.setText("Good job Soldier and thank you\nfor killing the Demon King and their army.\n\n" +
                    "From today onward ou will be known as \nSir " + MainApp.you.getName() + ", and will be a knight of this kingdom.\n\n" +
                    "As promised I will award you with\n2000 gold and a manor in the city.");
            MainApp.you.increaseBalance(2000);
        }

        //start music
//        a = new MediaPlayer((new Media(getClass().getResource("/music/town.mp3").toString())));
//        a.setCycleCount(MediaPlayer.INDEFINITE);
//        a.play();
    }
}

