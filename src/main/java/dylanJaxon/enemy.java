package dylanJaxon;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class enemy {
    // Properties
    private int health = 100, mana = 100;
    private final int damage;
    private final int defense;

    private String name;

    // Images of the SPRITES in combat (0 - stance, 1 - attack, 2 - death, 3 - frozen)  
    private final ArrayList<Image> SPRITES = new ArrayList<>();

    // Constructor
    public enemy() {
        //Boss
        // Predefined stats for boss
        name = "???";
        damage = 60;
        defense = 50;

        SPRITES.add(scale("/images-combat/boss-stance.png"));
        SPRITES.add(scale("/images-combat/boss-attack.png"));
        SPRITES.add(scale("/images-combat/boss-death.png"));
        SPRITES.add(scale("/images-combat/boss-frozen.png"));
    }

    public enemy(int attack, int protection, int range, int c) {
        // Random stat modifier
        damage = ThreadLocalRandom.current().nextInt(attack - range,attack + range +1);
        defense = ThreadLocalRandom.current().nextInt(protection - range, protection + range +1);

        switch (c) {
            case 3:
                // Orc (low damage, high defense)
                name = "Orc";
                break;
            case 4:
                // Skeleton (high damage, low defense)
                name = "Skeleton";
                break;
        }

        SPRITES.add(scale("/images-combat/" + name.toLowerCase() + "-stance.png"));
        SPRITES.add(scale("/images-combat/" + name.toLowerCase() + "-attack.png"));
        SPRITES.add(scale("/images-combat/" + name.toLowerCase() + "-death.png"));
        SPRITES.add(scale("/images-combat/" + name.toLowerCase() + "-frozen.png"));
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

    private Image image(String path) {
        return new Image(getClass().getResource(path).toString());
    }

    // Get

    public Image getSprite(int n) {
        return SPRITES.get(n);
    }

    public String getName() {
        return name;
    }

    public int getDamage() {
        return damage;
    }

    public int getDefense() {
        return defense;
    }

    public int getHealth() {
        return health;
    }

    public int getMana() {
        return mana;
    }

    // Set

    public void setHealth(int n) {
        health = n;
    }

    public void setMana(int n) {
        mana = n;
    }
}
