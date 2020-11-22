package dylanJaxon;
/*
Dylan & Jaxon
10/19/2020
Combat screen of the game
Here you can click buttons to fight enemies
Uses health & mana to dictate how the battle unfolds
 */

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

public class combat implements Initializable {

    // Images of you and the enemy
    @FXML
    private ImageView imgYou, imgEnemy;

    @FXML
    private ImageView imgBackground;

    // Images of ability icons
    @FXML
    private ImageView imgAttack, imgDefense, imgHeal, imgPunch, imgReturn;

    // Info about an ability when hovered over
    @FXML
    private Label lblAbility, lblInfo, lblButtons, lblStory, lblContinue;

    // Labels for names of you & enemy
    @FXML
    private Label lblEnemy, lblYou;

    // Health/Mana Labels for you and enemy (first letter is who and second is what)
    // ex lblYH: you, health
    @FXML
    private Label lblYH, lblYM, lblEH, lblEM;

    // Int for the player's defensive ability
    private int defenseModifier = 0;
    boolean unfreeze = false;

    // Enemy's variables
    private enemy enemy;

    // Timer for actions so that not everything is instant
    private Timeline wait = new Timeline(new KeyFrame(Duration.seconds(2), ae -> timer()));
    private ImageView active;

   // MediaPlayer a;

    private void timer() {
        // Runs if enemy is dead
        if (imgEnemy.getImage() == enemy.getSprite(2) || imgYou.getImage() == MainApp.you.getSprite(2)) {
            // Changes visibility of objects on screen
            lblAbility.setVisible(false);
            lblInfo.setVisible(false);
            lblButtons.setVisible(true);
            imgReturn.setVisible(true);

            // Resets sprite depending on who is dead
            if (imgEnemy.getImage() == enemy.getSprite(2)) {
                imgYou.setImage(MainApp.you.getSprite(0));

            } else {
                imgEnemy.setImage(enemy.getSprite(0));
            }

            if (!enemy.getName().equals("Orc") && !enemy.getName().equals("Skeleton")) {
                if (enemy.getHealth() <= 75 && enemy.getHealth() > 50) {
                    lblContinue.setVisible(true);
                    lblStory.setVisible(true);
                    lblStory.setText("I guess you’re not the fly I thought you were.  \n" +
                            "You actually have the ability of a rat.  \n" +
                            "But now I will show you true despair.  ");
                } else if (enemy.getHealth() <= 50 && enemy.getHealth() > 25) {
                    lblContinue.setVisible(true);
                    lblStory.setVisible(true);
                    lblStory.setText("I see that you are an opponent that is worthy of my full strength.  \n" +
                            "Not the pest I thought you were.  \n" +
                            "So now I will grace you with the honour of my true form");
                } else if (enemy.getHealth() <= 25 && enemy.getHealth() > 0) {
                    lblContinue.setVisible(true);
                    lblStory.setVisible(true);
                    lblStory.setText("I guess I truly need to be serious.  \n" +
                            "Enough of the chit chat, I will destroy you now.  \n" +
                            "Prepare for your destruction.  ");
                } else if (enemy.getHealth() <= 0) {
                    lblContinue.setVisible(true);
                    lblStory.setVisible(true);
                    lblStory.setText("NOOOOOOOOOO…\n" +
                            "THIS IS IMPOSSIBLE!  I CANNOT DIE!  \n" +
                            "NOT TO A MERE MORTAL LIKE YOUUUUU!\n");
                    MainApp.you.setProgress(6, true);
                }
            }


            // Stops timer
            wait.stop();

        } else if (lblAbility.getText().equals(enemy.getName())) {
            // Runs if the orc or skeleton just took their turn
            // This is checked because you must update different displays at different times
            lblAbility.setVisible(false);
            lblInfo.setVisible(false);
            abilityVisibility(true);

            // Resets enemy image if you have no defense modifiers or you're not a mage
            // Both of these will be false if the enemy is frozen
            if (defenseModifier == 0 || MainApp.you.getClassInt() != 2) {
                imgEnemy.setImage(enemy.getSprite(0));
            }

            // Runs if the display is visible
        } else if (lblAbility.isVisible()) {
            // Updates display, showing enemy's actions
            // This code will run AFTER the else statement below
            lblAbility.setText(enemy.getName());

            // Resets your image
            imgYou.setImage(MainApp.you.getSprite(0));

            // The damage the enemy will do
            int damageDone = 0;

            if (enemy.getMana() >= 7) {
                // Changes name of ability depending on what the enemy is (and changes the height)
                if (enemy.getName().equals("Orc")) {
                    lblInfo.setText("\n uses Strike!");

                } else if (enemy.getName().equals("Skeleton")) {
                    lblInfo.setText("\n uses Quickshot!");

                } else {
                    lblInfo.setText("\n uses Beat Down!");
                }
                lblInfo.setPrefHeight(70);

                int defense = 5;
                // Gets your defense (only if your armour isn't broken)
                if (MainApp.you.getDurability(6) > 0) {
                    defense = MainApp.you.getArmour().getDefense();
                }

                // Damage done by enemy changes based on class
                switch (MainApp.you.getClassInt()) {
                    case 0:
                        // Sends enemy's damage stat - your defense + defenseModifier (for if warriors used strengthen)
                        damageDone = enemyAttack(enemy.getDamage() - (defense + defenseModifier), 0);
                        break;
                    case 1:
                        // Sends enemy's damage stat - your defense and defensiveModifier in evasion (for if you used speed)
                        damageDone = enemyAttack(enemy.getDamage() - defense, defenseModifier);
                        break;
                    case 2:
                        // Only attacks if defenseModifier is 0 (for mages, will be frozen if greater than 0)
                        if (defenseModifier == 0) {
                            // Sends enemy's damage stat - your defense
                            damageDone = enemyAttack(enemy.getDamage() - defense, 0);
                        } else {
                            frozenEnemy();
                        }
                        break;
                }
                // Decreases armour durability (only when you don't miss)
                if (damageDone != 0 && MainApp.you.getDurability(6) > 0) {
                    MainApp.you.increaseDurability(6, -1);
                }

            } else {
                // If you're a mage and defenseModifier isn't 0, the enemy will be froze
                if (MainApp.you.getClassInt() == 2 && defenseModifier > 0) {
                    frozenEnemy();

                } else {
                    // Enemy punches when they have no mana
                    lblInfo.setText("\n uses Punch!");

                    // 0 Damage, which will default to 5 (or 10 if crit)
                    damageDone = enemyAttack(0, defenseModifier);

                    enemy.setMana(enemy.getMana() + 10);
                }
            }
            // Subtracts health
            MainApp.you.setHealth(MainApp.you.getHealth() - damageDone);

            // Checks for loss after enemy attack
            playerLoss();

            updateHM();

        } else {
            // Updates display, showing player's actions
            lblAbility.setVisible(true);
            lblInfo.setVisible(true);

            if (active == imgAttack) {
                // Attacking image
                imgYou.setImage(MainApp.you.getSprite(1));

                if (defenseModifier != 0 && MainApp.you.getClassInt() == 2 && unfreeze) {
                    // Unfreezes enemy if you're a mage that used fireball on a frozen target (and didn't miss)
                    imgEnemy.setImage(enemy.getSprite(0));
                    defenseModifier = 0;
                    unfreeze = false;
                }
            } else if (active == imgDefense) {
                // Defensive image
                imgYou.setImage(MainApp.you.getSprite(3));

                if (defenseModifier != 0 && MainApp.you.getClassInt() == 2) {
                    // Changes to frozen image if modifier isn't 0 and you're a mage
                    imgEnemy.setImage(enemy.getSprite(3));
                }
            } else if (active == imgPunch) {
                // Attacking image
                imgYou.setImage(MainApp.you.getSprite(1));
            }

            // Checks for a win after you attack
            playerWin();

            // Updates the health & mana of both you and the enemy
            updateHM();
        }
    }

    @FXML
    void ability(MouseEvent event) {
        ImageView source = (ImageView) event.getSource();

        // If you're an archer, checks if you are trying to increase your defenseModifier past 40% miss chance
        if (source == imgDefense && MainApp.you.getClassInt() == 1 && defenseModifier == 40) {
            lblAbility.setText("");
            lblInfo.setText(" You can't use this ability \n because you're already at \n the maximum miss chance!");
            lblInfo.setPrefHeight(102);
        } else if (source == imgHeal && MainApp.you.getPotions() <= 0) {
            // Out of potions code
            lblAbility.setText("");
            lblInfo.setText(" You can't use this ability \n because you don't have \n enough any potions!");
            lblInfo.setPrefHeight(102);

        } else if (source == imgHeal && MainApp.you.getHealth() == 100 && MainApp.you.getMana() == 100) {
            // Useless potion code
            lblAbility.setText("");
            lblInfo.setText(" You can't use this ability \n because your health \n and mana is full!");
            lblInfo.setPrefHeight(102);

            // Only uses ability if you have enough mana
        } else if (manaCost(getAbilityName(source)) <= MainApp.you.getMana()) {
            // Makes abilities disappear
            abilityVisibility(false);
            active = source;

            // Displays what ability you used
            displayDetails(MainApp.you.getName(), Color.WHITE, "\n uses " + getAbilityName(source) + "!");
            lblInfo.setPrefHeight(70);

            // Mana Change
            MainApp.you.setMana(MainApp.you.getMana() - manaCost(getAbilityName(source)));

            // If you have above 100 mana reset to 100
            if (MainApp.you.getMana() > 100) {
                MainApp.you.setMana(100);
            }

            // Attack Code
            if (source == imgAttack) {
                // Your damage done will be your weapon damage - the enemy's defense
                // Could possibly chance to special chance (critical hits / misses)
                int damageDone;

                // Damage when your weapon isn't broken
                if (MainApp.you.getDurability(5) > 0) {
                    damageDone = specialChance(((MainApp.you.getWeapon().getDamage() + (MainApp.you.getUpgrade() * 2)) - enemy.getDefense()), 0);

                    // Decreases durability (5 is weapon)
                    MainApp.you.increaseDurability(5, -1);
                } else {
                    // Broken weapon damage
                    damageDone = specialChance(5, 0);
                }

                // Unfreeze is true false if you're doing damage (didn't miss), you're a mage and freeze is active
                // Boolean is used for removing freeze effect on fireball
                if (damageDone != 0 && MainApp.you.getClassInt() == 2 && defenseModifier != 0) {
                    unfreeze = true;
                }

                // Subtracts health
                enemy.setHealth(enemy.getHealth() - damageDone);

                wait.play();

                // Defense Code
            } else if (source == imgDefense) {
                switch (MainApp.you.getClassInt()) {
                    case 0:
                        // Warrior's ability (+20% base defense) (also rounds to an integer)
                        defenseModifier += Math.round((double) MainApp.you.getArmour().getDefense() * 0.2);
                        break;
                    case 1:
                        //Archer's ability (+10% miss chance)
                        defenseModifier += 10;
                        break;
                    case 2:
                        //Mage's ability (freezes enemy)
                        defenseModifier = 2;
                        break;
                }

                wait.play();

                // Potion Code
            } else if (source == imgHeal) {
                displayDetails(MainApp.you.getName(), Color.WHITE, "\n uses a " + getAbilityName(imgHeal) + "!");
                lblInfo.setPrefHeight(70);

                // Sets health to 100 if you're less than 20 health away from max (as to not go past 100 hp)
                if (MainApp.you.getHealth() >= 80) {
                    MainApp.you.setHealth(100);
                } else {
                    // Regular health increase
                    MainApp.you.setHealth(MainApp.you.getHealth() + 20);
                }
                MainApp.you.increasePotions(-1);

                wait.play();

                // Punch code
            } else if (source == imgPunch) {
                // Sends damage of 0 so that it doesn't get affected by range (so that it will always deal 5 damage
                int damageDone = specialChance(0, 0);
                enemy.setHealth(enemy.getHealth() - damageDone);

                wait.play();
            }
        } else {
            // Out of mana code
            lblAbility.setText("");
            lblInfo.setText(" You can't use this ability \n because you don't have \n enough mana!");
            lblInfo.setPrefHeight(102);
        }
    }

    private int enemyAttack(int damageDone, int missChance) {
        // Subtracts mana
        enemy.setMana(enemy.getMana() - 7);

        // Changes to fighting image
        imgEnemy.setImage(enemy.getSprite(1));

        // Gets the damage the enemy will (chance for critical and miss)
        return specialChance(damageDone, missChance);
    }

    private void playerWin() {
        // Player winning code
        if (enemy.getHealth() <= 0) {
            enemy.setHealth(0);

            // Shows death image
            imgEnemy.setImage(enemy.getSprite(2));
            imgEnemy.setFitHeight(80);

            // Gold for winning
            int gold = 10;

            // Map number (easier to read)
            int map = MainApp.you.getMapNum();

            // Map Increment
            int inc = 3;

            // More gold for winning in harder areas
            for (int g = 6; g <= map; g += inc) {
                // Increases by 1-5 and will run more times the further you are in the game
                gold += ThreadLocalRandom.current().nextInt(1, 5 + 1);

                // Decrease increment in caves (enemies get stronger faster in caves)
                if (g == 12) {
                    inc = 2;
                }
            }

            // Increases gold
            MainApp.you.increaseBalance(gold);

            // Sets final progress to true
            if (map == 19) {
                MainApp.you.setProgress(6, true);
            }

            lblButtons.setText("You win! +" + gold + " Gold.");
            updateHM();
        }
    }

    private void playerLoss() {
        // Player losing code
        if (MainApp.you.getHealth() <= 0) {
            MainApp.you.setHealth(0);

            // Death image
            imgYou.setImage(MainApp.you.getSprite(2));
            imgYou.setFitHeight(80);

            int gold = MainApp.you.getBalance() / 5;
            // Decreases gold
            MainApp.you.increaseBalance(-gold);

            lblButtons.setText("You lose! -" + gold + " Gold.");
            updateHM();
        }
    }

    private void updateHM() {
        // Updates health & mana of both you and the enemy
        lblYH.setText("Health: " + MainApp.you.getHealth() + "/100");
        lblYM.setText("Mana: " + MainApp.you.getMana() + "/100");
        lblEH.setText("Health: " + enemy.getHealth() + "/100");
        lblEM.setText("Mana: " + enemy.getMana() + "/100");
    }

    private int specialChance(int damageDone, int evasion) {
        // Random chance for range of damage 5 from your weapon damage
        int range = ThreadLocalRandom.current().nextInt(-5, 5 + 1);

        // Updates damage, using the range
        damageDone += range;

        // Base damage of 5 (anything less than 5 will be put to 5
        if (damageDone < 5) {
            damageDone = 5;
        }
        lblInfo.setPrefHeight(102);

        // Random chance for miss and critical hits
        int chances = ThreadLocalRandom.current().nextInt(1, 100 + 1);

        // Miss if 1 - 10 (increases chance based on evasion, which is used by archers)
        if (chances <= 10 + evasion) {
            lblInfo.setText(lblInfo.getText() + "\n But it missed!");
            return 0; //Will still consume mana on a miss

            // Critical for 91 or higher
        } else if (chances >= 91) {
            lblInfo.setText(lblInfo.getText() + " Dealing \n " + damageDone * 2 + " damage! Critical Hit!");

            // Increase damageDone by 100%
            return damageDone * 2;

        } else {
            lblInfo.setText(lblInfo.getText() + " Dealing \n " + damageDone + " damage!");

            // Between miss and critical chances (returns regular damage which was passed through)
            return damageDone;
        }
    }

    private void frozenEnemy() {
        // Decreases modifier
        --defenseModifier;

        // Updates info
        lblInfo.setText(lblInfo.getText() + "\n But is frozen!");
        lblInfo.setPrefHeight(102);
    }

    private int manaCost(String name) {
        // Returns the mana cost of the specified ability name
        switch (name) {
            case "Strike":
                return 0;

            case "Quickshot":
                return 10;

            case "Fireball":
                return 15;

            case "Strengthen":
                return 25;

            case "Swiftness":
                return 15;

            case "Freeze":
                return 20;

            // Negative mana costs give you mana
            case "Potion":
                return -30;

            case "Punch":
                return -10;
        }
        return 0;
    }

    private void showSprite(Image stance, String name, ImageView output) {
        // Sets the stance image into the desired imageview and sets the proper height
        output.setImage(stance);
        output.setFitHeight(110);

        if (output == imgYou) {
            // Sets name & abilities to their respective object
            lblYou.setText(name);

            imgAttack.setImage(MainApp.you.getAbility(0));
            imgDefense.setImage(MainApp.you.getAbility(1));
            imgPunch.setImage(MainApp.you.getAbility(2));
            imgHeal.setImage(MainApp.you.getAbility(3));
        } else {
            // Only sets name for enemy showSprite
            lblEnemy.setText(name);
        }
    }

    private void abilityVisibility(boolean b) {
        imgAttack.setVisible(b);
        imgDefense.setVisible(b);
        imgHeal.setVisible(b);
        imgPunch.setVisible(b);
        lblButtons.setVisible(b);
    }

    private void displayDetails(String ability, Paint color, String info) {
        // Displays the ability and info labels, showing their ability name & what they do
        // Also will display what ability was used when casting an ability
        lblAbility.setText(ability);
        lblAbility.setTextFill(color);
        lblInfo.setText(info);
    }

    private String getAbilityName(ImageView source) {
        // Returns the name of the ability based on what button was clicked and your class
        if (source == imgAttack) {
            switch (MainApp.you.getClassInt()) {
                // Attack abilities
                case 0:
                    return "Strike";
                case 1:
                    return "Quickshot";
                case 2:
                    return "Fireball";
            }
        } else if (source == imgDefense) {
            switch (MainApp.you.getClassInt()) {
                // Defense abilities
                case 0:
                    return "Strengthen";
                case 1:
                    return "Swiftness";
                case 2:
                    return "Freeze";
            }
        } else if (source == imgHeal) {
            // Potion
            return "Potion";

        } else if (source == imgPunch) {
            // Punching on no mana
            return "Punch";

        } else if (source == imgYou) {
            // Hover over yourself to show your stats
            return "Player";

        } else {
            // Return button
            return "Return";
        }
        // Needed return at end of method, shouldn't be reachable
        return "";
    }

    @FXML
    void hoverOpen(MouseEvent event) {
        // Getting what label you're hovering over
        ImageView source = (ImageView) event.getSource();
        String name = getAbilityName(source);

        // Only runs if you're not in the attack rotation (imgAttack will be invisible during this time)
        // Runs if hovering over imgReturn as imgAttack will be invisible at that time
        if (imgAttack.isVisible() || source == imgReturn) {
            String damageRange;

            // Different damage range depending on weapon durability
            if (MainApp.you.getDurability(5) > 0) {
                damageRange = (((MainApp.you.getWeapon().getDamage() + (MainApp.you.getUpgrade() * 2)) - 5) + "-" + ((MainApp.you.getWeapon().getDamage() + (MainApp.you.getUpgrade() * 2)) + 5));
            } else {
                damageRange = "5 - 10";
            }

            // Shows info of each ability
            switch (name) {
                case "Strike":
                    displayDetails(name, Color.ORANGE, "\t\t\t\t0 Mana\n Strike the target, dealing " +
                            "\n between " + damageRange + " damage.");
                    break;
                case "Quickshot":
                    displayDetails(name, Color.ORANGE, "\t\t\t\t10 Mana\n Shoots an arrow, dealing " +
                            "\n between " + damageRange + " damage.");
                    lblInfo.setPrefHeight(102);
                    break;
                case "Fireball":
                    displayDetails(name, Color.RED, "\t\t\t\t15 Mana\n Shoots a fireball, dealing " +
                            "\n between " + damageRange + " damage.");
                    lblInfo.setPrefHeight(102);
                    break;
                case "Strengthen":
                    displayDetails(name, Color.YELLOW, "\t\t\t\t25 Mana\n Strengthen up, increasing" +
                            "\n base defense by 20%.");
                    lblInfo.setPrefHeight(102);
                    break;
                case "Swiftness":
                    displayDetails(name, Color.LIGHTBLUE, "\t\t\t\t15 Mana\n Speed up, increasing" +
                            "\n miss chance by 10%. " +
                            "\n Can stack up to 4 times.");
                    lblInfo.setPrefHeight(131);
                    break;
                case "Freeze":
                    displayDetails(name, Color.LIGHTBLUE, "\t\t\t\t20 Mana\n Freezes the enemy, stopping" +
                            "\n their next 2 attacks. Fireball" +
                            "\n will cancel this effect.");
                    lblInfo.setPrefHeight(131);
                    break;
                case "Potion":
                    displayDetails(name, Color.RED, "\t\t\t\t" + MainApp.you.getPotions() + " Potions\n Drink a potion," +
                            "\n Healing 20 health and" +
                            "\n restoring 30 mana.");
                    lblInfo.setPrefHeight(131);
                    break;
                case "Punch":
                    displayDetails(name, Color.WHITE, "\t\t\t\t0 Mana\n Punches the enemy," +
                            "\n dealing 5 damage and" +
                            "\n restoring 10 mana.");
                    lblInfo.setPrefHeight(131);
                    break;
                case "Return":
                    displayDetails(name, Color.YELLOW, "\n Return to the world.");
                    lblInfo.setPrefHeight(70);
                    break;
                case "Player":
                    displayDetails(MainApp.you.getName(), Color.WHITE, "\n Weapon Durability: " + MainApp.you.getDurability(5) +
                            "\n Armour Durability: " + MainApp.you.getDurability(6));
                    lblInfo.setPrefHeight(102);
                    break;
            }
            lblAbility.setVisible(true);
            lblInfo.setVisible(true);
        }
    }

    @FXML
    private void keyPress(KeyEvent event) throws IOException {
        switch (event.getCode()) {
            case SPACE:
                if (MainApp.you.getProgress(6)) {
                    MainApp.you.setMapNum(1);
                    MainApp.you.setX(177);
                    MainApp.you.setY(294);
                    town.finalCutscene = true;
                    MainApp.setRoot("town", "");
                } else {
                    lblStory.setVisible(false);
                    lblContinue.setVisible(false);
                }
        }
    }

    @FXML
    void hoverExit(MouseEvent event) {
        ImageView source = (ImageView) event.getSource();
        // Only runs if you're not in the attack rotation (imgAttack will be invisible during this time)
        // Will close anyways if you're not hovering over imgYou because only imgYou will be visible during the attack rotation
        if (source != imgYou || imgAttack.isVisible()) {
            lblAbility.setVisible(false);
            lblInfo.setVisible(false);
        }
    }

    @FXML
    void leave(MouseEvent event) throws IOException {
        // Resets mana
        MainApp.you.setMana(100);

        // Returns to the area you were last in (on win)
        if (MainApp.you.getHealth() != 0) {

            if (MainApp.you.getMapNum() < 11) {
                // Forest
                MainApp.setRoot("forest", "");
                MainApp.setSize(396, 400);
            } else if (MainApp.you.getMapNum() == 11) {
                // Lumberyard
                MainApp.setRoot("forest", "");
                MainApp.setSize(324, 555);

            } else if (MainApp.you.getMapNum() == 19) {
                // Boss room of cave
                MainApp.setRoot("caves", "");
                MainApp.setSize(506, 803);

            } else if (MainApp.you.getMapNum() >= 12) {
                // Caves
                MainApp.setRoot("caves", "");
                MainApp.setSize(396, 396);
            }
        } else {
            // Sends you back to castle on loss
            MainApp.you.setMapNum(1);
            MainApp.you.setX(177);
            MainApp.you.setY(294);

            MainApp.setRoot("town", "");
        }
        //a.stop();
    }

    private enemy newEnemy(int oA, int oD, int sA, int sD, int r) {
        // Chooses randomly between orc and skeleton
        int c = ThreadLocalRandom.current().nextInt(3, 4+1);

        if (c == 3) { // Orc
            return new enemy(oA, oD, r, c);

        } else { // Skeleton
            return new enemy(sA, sD, r, c);
        }
    }

    @FXML
    void quit(MouseEvent event) {
        // Alert for quitting game
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Exit");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to quit? Make sure you have saved your progress.");
        ButtonType btnYes = new ButtonType("Yes");
        ButtonType btnNo = new ButtonType("No");
        ButtonType btnCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(btnYes, btnNo, btnCancel);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == btnYes) {
            // Exits if you press okay
            System.exit(0);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MainApp.setSize(380, 660);
        wait.setCycleCount(3);

        // Shows your player
        showSprite(MainApp.you.getSprite(0), MainApp.you.getName(), imgYou);

        int map = MainApp.you.getMapNum();

        //Images
        Image forest = new Image(getClass().getResource("/images-combat/forest-bg.png").toString());
        Image caves = new Image(getClass().getResource("/images-combat/caves-bg.png").toString());

        int c;

        // Different stats depending on the area you were in
        if (map >= 6 && map <= 8) {
            enemy = newEnemy(8, 10, 14, 6, 2);
            imgBackground.setImage(forest);

        } else if (map >= 9 && map <= 11) {
            enemy = newEnemy(13, 14, 18, 10, 2);
            imgBackground.setImage(forest);

        } else if (map >= 12 && map <= 13) {
            enemy = newEnemy(23, 18, 30, 14, 3);
            imgBackground.setImage(caves);

        } else if (map >= 14 && map <= 15) {
            enemy = newEnemy(29, 23, 37, 19, 3);
            imgBackground.setImage(caves);

        } else if (map >= 16 && map <= 17) {
            enemy = newEnemy(35,30,45,20,4);
            imgBackground.setImage(caves);

        } else if (map == 19) {
            enemy = new enemy();
            imgBackground.setImage(caves);

            lblContinue.setVisible(true);
            lblStory.setVisible(true);
            lblStory.setText("So you’re the fly that’s been buzzing around killing my soldiers.  \n" +
                    "Now you’ve come to me to get yourself killed have you?\n" +
                    "Well I can grant your wish at any time.  ");
        }

        showSprite(enemy.getSprite(0), enemy.getName(), imgEnemy);

        updateHM();

        MainApp.you.setSelected(6);

        //a = new MediaPlayer((new Media(getClass().getResource("/music/combat.mp3").toString())));
        //a.setCycleCount(MediaPlayer.INDEFINITE);
        //a.play();
    }
}
