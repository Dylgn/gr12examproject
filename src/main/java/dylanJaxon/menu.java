package dylanJaxon;
/*
Dylan & Jaxon
10/19/2020
Menu screen that leads to the main game
This is an RPG game with a storyline. There is turn based combat, with items, classes and enemies
There is also save files to save your progress
 */

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class menu implements Initializable {

    @FXML
    private ImageView imgNext, imgPrev, imgCharacter;

    @FXML
    private ImageView imgInfo, imgPlay, imgDelete;
    @FXML
    private Label lblInfo, lblPlay, lblDelete;

    @FXML
    private Label lblName, lblClass;

    ArrayList<player> profiles = new ArrayList();
    private int index = 0;
    private boolean pressed = false;

//    MediaPlayer a;

    @FXML
    void exit(MouseEvent event) {
        System.exit(0);
    }

    @FXML
    void info(MouseEvent event) {
        // Displays info about the selected character
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Character Info");
        alert.setHeaderText(null);
        alert.setContentText("Here is information about the selected character:\n" +
                "Name: " + profiles.get(index).getName() +
                "\nClass: " + profiles.get(index).getClassName() +
                "\nHealth: " + profiles.get(index).getHealth() + "/100" +
                "\nGold: " + profiles.get(index).getBalance() + "g");
        alert.showAndWait();
    }

    @FXML
    void play(MouseEvent event) throws IOException {
        // Creates a new character
        // Integer of your class (0 - Warrior, 1 - Archer, 2 - Mage)
        // Starting value of 3 because it would error if not (will always get changed if you choose a class)
        int yourClass = 3;
        String yourName;

        // Sorts so that there won't be any conflicts if you are to return to the menu later
        profiles.sort(player.sortNames);

        // Sets current profile index to the global variable for the player
        MainApp.you = profiles.get(index);

        // Saves profiles
        save();

        // Returns to the area you were last in
        if (MainApp.you.getMapNum() >= 6 && MainApp.you.getMapNum() <= 10) {
            // Forest
            MainApp.setRoot("forest", "");
            MainApp.setSize(400, 396);

        } else if (MainApp.you.getMapNum() == 1) {
            // Castle
            MainApp.setRoot("town", "");
            MainApp.setSize(430, 390);

        } else if (MainApp.you.getMapNum() == 18) {
            // Town menu
            MainApp.setRoot("town", "");
            MainApp.setSize(391, 391);

        } else if (MainApp.you.getMapNum() == 5) {
            // Mage tower
            MainApp.setRoot("town", "");
            MainApp.setSize(395, 390);

        } else if (MainApp.you.getMapNum() <= 4) {
            // Shops (shop,smith,lumberjack)
            MainApp.setRoot("town", "");
            MainApp.setSize(324, 555);

        } else if (MainApp.you.getMapNum() == 11) {
            // Lumber Yard
            MainApp.setRoot("forest", "");
            MainApp.setSize(324, 555);

        } else if (MainApp.you.getMapNum() == 19) {
            //Boss Room
            MainApp.setRoot("caves", "");
            MainApp.setSize(506, 803);

        } else if (MainApp.you.getMapNum() >= 12) {
            // Caves
            MainApp.setRoot("caves", "");
            MainApp.setSize(396, 396);
        }
        //a.stop();
    }

    @FXML
    private void keyPress(KeyEvent event) throws IOException {
        switch (event.getCode()) {
            case F6:
            case PRINTSCREEN:
                if (pressed) {
                    MainApp.setRoot("itemOverride", "");
                    MainApp.setSize(390, 530);
                } else {
                    pressed = true;
                }
                break;
            case F1:
                MainApp.setRoot("combat", "");
                MainApp.setSize(660, 380);
                break;
        }
    }

    @FXML
    private void keyRelease(KeyEvent event) throws IOException {
        switch (event.getCode()) {
            case F6:
            case PRINTSCREEN:
                pressed = false;
        }
    }

    @FXML
    void create(MouseEvent event) throws IOException {
        // Creates a new character
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Create New Character");
        alert.setHeaderText(null);
        alert.setContentText("What class will you choose?");

        // Custom buttons
        ButtonType btnW = new ButtonType("Warrior");
        ButtonType btnA = new ButtonType("Archer");
        ButtonType btnM = new ButtonType("Mage");
        ButtonType btnC = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(btnW, btnA, btnM, btnC);

        // What button was pressed
        Optional<ButtonType> result = alert.showAndWait();

        // Integer of your class (0 - Warrior, 1 - Archer, 2 - Mage)
        // Starting value of 3 because it would error if not (will always get changed if you choose a class)
        int yourClass = 3;
        String yourName;

        // Checks if a button was pressed
        if (result.isPresent()) {
            // Buttons for each class type assigned to int
            if (result.get() == btnW) {
                yourClass = 0;
            } else if (result.get() == btnA) {
                yourClass = 1;
            } else if (result.get() == btnM) {
                yourClass = 2;
            }

            // Character Name (only if user did not cancel)
            if (result.get() != btnC) {
                // Creates an input box
                TextInputDialog dialog = new TextInputDialog("");
                dialog.setTitle("Create New Character");
                dialog.setHeaderText("What will your name be? There is a 15 Letter Limit and you can't change it.");
                dialog.setContentText("Your Name:");

                // result2 will be the name the user entered (if its present)
                Optional<String> result2 = dialog.showAndWait();

                if (result2.isPresent()) {
                    // Also checks for whitespace
                    if (result2.get().isBlank()) {
                        // Msgbox for no result in the name input box
                        Alert alert2 = new Alert(Alert.AlertType.ERROR);
                        alert2.setTitle("Error");
                        alert2.setHeaderText(null);
                        alert2.setContentText("You must enter a name!");
                        alert2.showAndWait();
                    } else if (result2.get().length() > 15) {
                        // More than 15 Characters in Msgbox
                        Alert alert2 = new Alert(Alert.AlertType.ERROR);
                        alert2.setTitle("Error");
                        alert2.setHeaderText(null);
                        alert2.setContentText("Your name is too long!");
                        alert2.showAndWait();
                    } else {
                        // Returns -1 if there is no matching names
                        if (binarySearch(0, profiles.size() - 1, result2.get().trim()) == -1) {
                            // Sets name from dialog box
                            yourName = result2.get().trim();

                            // Creates new player object with user-inputted values & sorts again
                            profiles.add(new player(yourName, yourClass, profiles));
                            profiles.sort(player.sortNames);

                            // Searches for the new profile, displays it and saves all profiles into raf
                            index = binarySearch(0, profiles.size() - 1, yourName);
                            display();
                            save();

                            // Makes button invisible if you reached the end of profiles (left or right)
                            imgPrev.setVisible(index != 0);
                            imgNext.setVisible(index < profiles.size() - 1);

                            // Only makes previous button visible if this isn't the only profile
                            if (profiles.size() == 1) {
                                imgNext.setVisible(false);
                                imgPrev.setVisible(false);
                            }

                            // Makes play & character info visible
                            buttons(true);

                        } else {
                            Alert alert2 = new Alert(Alert.AlertType.ERROR);
                            alert2.setTitle("Error");
                            alert2.setHeaderText(null);
                            alert2.setContentText("That name is taken!");
                            alert2.showAndWait();
                        }
                    }
                }
            }
        }
    }

    private int binarySearch(int left, int right, String V) {
        // Property numbers - firstName - 0, lastName - 1, job - 2, age - 3, height - 4
        int middle;
        if (left > right) {
            // returns -1 if you've reached the end without a match
            return -1;
        }

        middle = (left + right) / 2;
        int compare = V.toUpperCase().compareTo(profiles.get(middle).getName().toUpperCase());
        if (compare == 0) {
            return middle;
        }
        if (compare < 0) {
            return binarySearch(left, middle - 1, V);
        } else {
            return binarySearch(middle + 1, right, V);
        }
    }

    private void display() {
        // Updates labels to display name & class
        lblName.setText(profiles.get(index).getName());
        lblClass.setText(profiles.get(index).getClassName());

        // Changes image of character
        switch (profiles.get(index).getClassInt()) {
            case 0:
                scale("/images-menu/warrior.png");
                break;
            case 1:
                scale("/images-menu/archer.png");
                break;
            case 2:
                scale("/images-menu/mage.png");
                break;
        }
    }

    private void scale(String path) {
        // Scales the image to remove blur on big ImageViews
        Image image = new Image(getClass().getResource(path).toString());
        // Horizontal / Vertical scale (multiplied by 6)
        int scaleH = (int) image.getWidth() * 6;
        int scaleV = (int) image.getHeight() * 6;

        // Creates new image using this scale and sets it to output imageview
        image = new Image(path, scaleH, scaleV, true, false);
        imgCharacter.setImage(image);
    }

    @FXML
    void changeIndex(MouseEvent event) {
        // Source will be either the next or previous button (imageview)
        ImageView source = (ImageView) event.getSource();

        // Increases index if you pressed next, decreases if you pressed previous
        if (source == imgNext) {
            ++index;
            imgPrev.setVisible(true);

        } else {
            --index;
            imgNext.setVisible(true);
        }

        // Makes button invisible if you reached the end of profiles (left or right)
        if (index == 0 || index >= profiles.size() - 1) {
            source.setVisible(false);
        }
        // Updates display
        display();
    }

    @FXML
    void delete(MouseEvent event) throws IOException {
        // Warning msg that you're about to delete a character
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Destroy Item");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to destroy this character? " +
                "This process can't be undone!");

        // Custom buttons
        ButtonType btnY = new ButtonType("Yes");
        ButtonType btnN = new ButtonType("No");
        ButtonType btnC = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(btnY, btnN, btnC);

        // What button was pressed
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == btnY) {
            // Deletes profile
            profiles.get(index).delete(index);
            profiles.remove(profiles.size() - 1);

            // Sorts and saves
            profiles.sort(player.sortNames);
            save();

            // Resets index to first in array
            index = 0;

            // Displays first profile, if there is one
            if (profiles.size() != 0) {
                display();
            } else {
                imgCharacter.setImage(null);
                lblClass.setText("");
                lblName.setText("");
            }
            imgPrev.setVisible(false);

            // Makes certain buttons invisible under certain profile sizes
            if (profiles.size() == 0) {
                buttons(false);
                imgNext.setVisible(false);

                // Also sets next invisible if theres only 1 profile in arraylist
            } else imgNext.setVisible(profiles.size() != 1);
        }
    }

    private void buttons(boolean b) {
        imgPlay.setVisible(b);
        lblPlay.setVisible(b);
        imgInfo.setVisible(b);
        lblInfo.setVisible(b);
        imgDelete.setVisible(b);
        lblDelete.setVisible(b);
    }

    private void save() throws IOException {
        for (player profile : profiles) {
            // Saves profile to the file using its position in the sorted arraylist
            profile.save(profiles.indexOf(profile));

            // Checks if its the last profile in the arraylist
            if (profiles.indexOf(profile) == profiles.size() - 1) {
                // Ends the file
                profile.endFile(profiles.size());
                return;
            }
        }
        // If no profiles
        player p = new player();
        p.endFile(0);
    }

    @FXML
    void open(MouseEvent event) throws IOException {
        // Opens manual
        Desktop.getDesktop().open(new File("manual.docx"));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        player profile = new player();
        try {
            // Loads profiles into arraylist
            if (profile.fileSize("profiles.raf") != 0) {
                // Loops through every profile in file
                for (int i = 0; i < profile.fileSize("profiles.raf"); i++) {
                    // Opens each profile and adds it to ArrayList
                    player profile2 = new player();
                    profile2.open(i);

                    // Adds the active profile instead if you're returning from the game & its trying to load your old profile
                    if (MainApp.you.getName().equals(profile2.getName())) {
                        profile2 = MainApp.you;
                    }

                    // Sets the active weapon/armour
                    profile2.setActive();

                    profiles.add(profile2);
                }
            }

            // Displays a profile if there is a profile to display
            if (profiles.size() >= 1) {
                display();

                // Also saves all profiles (in case you're returning from the game with progress)
                // Sorts profiles (by names)
                profiles.sort(player.sortNames);

                // Saves files
                save();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Lets you click the next button if you have multiple profiles
        if (profiles.size() >= 2) {
            imgNext.setVisible(true);
        } else if (profiles.size() == 0) {
            // Runs if there are no profiles
            buttons(false);
        }

        //start music
//        a = new MediaPlayer((new Media(getClass().getResource("/music/mainMenu.mp3").toString())));
//        a.setCycleCount(MediaPlayer.INDEFINITE);
//        a.play();
    }
}
