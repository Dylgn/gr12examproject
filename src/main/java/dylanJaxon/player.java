package dylanJaxon;

import javafx.scene.image.Image;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Comparator;

public class player {

    // Player Properties
    private int health, mana, classType; // 4 * 3 = 12
    private String name; // 1 * (15 * 2) = 30

    // Resources
    private int potions, gold, herbs, ore, logs; // 4 * 5 = 20

    // Location on map
    private int mapNum, xPos, yPos; // 3 * 4 = 12

    // 2D array with 7 arrays of 3 indexes (3 * 7) * 4 = 84
    /* 2D array of itemIDs (first), their durability (second) and upgrades (third)
     Slot 5 is activeWeapon, Slot 6 is activeArmour and Slots 0-4 are inventory slots*/
    private int[][] items = {{-1, 0, 0}, {-1, 0, 0}, {-1, 0, 0}, {-1, 0, 0}, {-1, 0, 0}, {0, 0, 0}, {15, 0, 0}};

    //progress Booleans(7 Booleans = 7 bytes)
    private boolean[] progress = new boolean[7];

    //selected inventory slot
    private int selected = 0;

    // 30 + 20 + 12 + 84 + 7
    private final int SIZE = (30 + 20 + 12 + 12 + 84 + 7);

    //stores active weapon & armour(for combat)
    private weapon activeWeapon;
    private armour activeArmour;

    // Images of the sprites
    // COMBAT (0 - stance, 1 - attack, 2 - death, 3 - defense)
    // WORLD (4 - back, 5 - right, 6 - front, 7 - left
    private final ArrayList<Image> SPRITES = new ArrayList<>();

    // Images of the class' abilities (0 - attack, 1 - defense, 2 - punch, 3 - potion)
    private final ArrayList<Image> ABILITIES = new ArrayList<>();

    // Constructors
    public player() {
        health = 0;
        mana = 0;
        potions = 0;
        gold = 0;
        ore = 0;
        logs = 0;
        classType = 0;
        mapNum = 1;
        xPos = 0;
        yPos = 0;
        this.setName("");
    }

    public player(String n, int cT, ArrayList<player> profiles) {
        // Sets health to max (full health)
        // Values that never change
        int MAXHEALTH = 100;
        int MAXMANA = 100;
        health = MAXHEALTH;
        mana = MAXMANA;

        // Player's class type (0 - Warrior, 1 - Archer, 2 - Mage)
        classType = cT;

        potions = 5;
        gold = 0;
        ore = 0;
        logs = 0;

        // Location on map (default is in castle)
        mapNum = 1;
        xPos = 177;
        yPos = 279;

        // Setting Player's Images: checks first if there is already a class of that type created
        // Takes their images instead of creating new ones every time
        // This is done because creating to many characters would lead to an out of memory error
        for (player profile : profiles) {
            // Checks for matching class number
            if (classType == profile.getClassInt()) {
                // Copies its abilities and sprites
                SPRITES.addAll(profile.getAllSprites());
                ABILITIES.addAll(profile.getAllAbilities());
                break;
            }
        }

        // Uses setSprites method if it couldn't find a copy
        if (SPRITES.size() != 8) {
            // Sets the player's sprites (and ability images)
            setSprites();
        }

        // Sets the active weapon/armour to defaults
        switch (cT) {
            case 0:
                activeWeapon = new weapon(0);
                items[5][0] = 0;
                break;
            case 1:
                activeWeapon = new weapon(5);
                items[5][0] = 5;
                break;
            case 2:
                activeWeapon = new weapon(10);
                items[5][0] = 10;
                break;
        }
        // Sets armour
        activeArmour = new armour(15);

        // Gets the weapon / armour's attributes
        activeWeapon.getWeapon("item.raf", activeWeapon.getID());
        activeArmour.getArmour("item.raf", activeArmour.getID());

        // Sets Durability
        items[5][1] = activeWeapon.getDurability();

        items[6][0] = 15;
        items[6][1] = activeArmour.getDurability();

        // Creates name (with whitespace for raf)
        this.setName(n);
    }


    //adds all of the sprites into an array list
    private void setSprites() {
        // Combat sprites
        SPRITES.add(scale("/images-combat/" + getClassName().toLowerCase() + "-stance.png"));
        SPRITES.add(scale("/images-combat/" + getClassName().toLowerCase() + "-attack.png"));
        SPRITES.add(scale("/images-combat/" + getClassName().toLowerCase() + "-death.png"));
        SPRITES.add(scale("/images-combat/" + getClassName().toLowerCase() + "-defense.png"));

        // World sprites
        SPRITES.add(scale("/images-world/" + getClassName().toLowerCase() + "-back.png"));
        SPRITES.add(scale("/images-world/" + getClassName().toLowerCase() + "-right.png"));
        SPRITES.add(scale("/images-world/" + getClassName().toLowerCase() + "-front.png"));
        SPRITES.add(scale("/images-world/" + getClassName().toLowerCase() + "-left.png"));

        // Ability icons
        ABILITIES.add(scale("/images-combat/" + getClassName().toLowerCase() + "-0.png"));
        ABILITIES.add(scale("/images-combat/" + getClassName().toLowerCase() + "-1.png"));
        ABILITIES.add(scale("/images-combat/punch.png"));
        ABILITIES.add(scale("/images-combat/potion.png"));
    }

    //scale sprites to the actual size we need
    private Image scale(String path) {
        // Scales the image to remove blur on big ImageViews
        Image image = new Image(getClass().getResource(path).toString());

        // Horizontal / Vertical scale (multiplied by 6)
        int scaleH = (int) image.getWidth() * 6;
        int scaleV = (int) image.getHeight() * 6;

        // Creates new image using this scale and sets it to output imageview
        return new Image(getClass().getResource(path).toString(), scaleH, scaleV, true, false);
    }

    // Get

    public String getName() {
        // Returns name
        return name.trim();
    }

    public int getHealth() {
        return health;
    }

    public int getMana() {
        return mana;
    }

    public int getClassInt() {
        return classType;
    }

    public String getClassName() {
        // Returns the name of your class
        switch (classType) {
            case 0:
                return "Warrior";

            case 1:
                return "Archer";

            case 2:
                return "Mage";
        }

        return "Warrior";
    }

    public weapon getWeapon() {
        return activeWeapon;
    }

    public armour getArmour() {
        return activeArmour;
    }

    public Image getSprite(int n) {
        return SPRITES.get(n);
    }

    public Image getAbility(int n) {
        return ABILITIES.get(n);
    }

    public ArrayList<Image> getAllSprites() {
        return SPRITES;
    }

    public ArrayList<Image> getAllAbilities() {
        return ABILITIES;
    }

    public int getItemID(int loc) {
        return items[loc][0];
    }

    public int getDurability(int loc) {
        return items[loc][1];
    }

    public int getDamage(int loc) {
        return items[loc][2];
    }

    public int getPotions() {
        return potions;
    }

    public int getSelected() {
        return selected;
    }

    public int getMapNum() {
        return mapNum;
    }

    public int getX() {
        return xPos;
    }

    public int getY() {
        return yPos;
    }

    /*loc -> 0 = start of game, 1 = boulder 1, 2 = boulder 2, 3 = axe, 4 = pickaxe, 5 = upgraded pickaxe, 6 = beat final boss*/
    public boolean getProgress(int loc) {
        return progress[loc];
    }

    public int getBalance() {
        return gold;
    }

    public int getHerbs() {
        return herbs;
    }

    public int getLogs() {
        return logs;
    }

    public int getStones() {
        return ore;
    }

    // Set

    public void setName(String n) {
        // 15 Character max for names
        StringBuffer buffer = new StringBuffer(n);
        buffer.setLength(15);

        name = buffer.toString();
    }

    public void setHealth(int h) {
        health = h;
    }

    public void setMana(int m) {
        mana = m;
    }

    public void setMapNum(int map) {
        mapNum = map;
    }

    public void setX(int x) {
        xPos = x;
    }

    public void setY(int y) {
        yPos = y;
    }

    public void setSelected(int s) {
        selected = s;
    }

    public void setID(int loc, int ID) {
        items[loc][0] = ID;
    }

    public void setDurability(int loc, int d) {
        items[loc][1] = d;
    }

    public void setUpgrade(int loc, int u) {
        items[loc][2] = u;
    }

    public void setActive() {
        // Creates new weapon using its itemID
        activeWeapon = new weapon(items[5][0]);
        activeArmour = new armour(items[6][0]);
    }

    //loc -> 0 = start of game, 1= boulder 1, 2 = boulder 2, 3 = axe, 4 = pickaxe, 5 = upgraded pickaxe, 6 = beat final boss
    public void setProgress(int loc, boolean state) {
        progress[loc] = state;
    }

    public void setHerbs(int hrb) {
        herbs = hrb;
    }

    public void setLogs(int lgs) {
        logs = lgs;
    }

    public void setStones(int or) {
        ore = or;
    }

    // Increments

    public void increaseHerbs(int h) {
        herbs += h;
    }

    public void increaseLogs(int l) {
        logs += l;
    }

    public void increaseStones(int s) {
        ore += s;
    }

    public void increaseDurability(int loc, int d) {
        items[loc][1] += d;
    }

    public void increasePotions(int p) {
        potions += p;
    }

    public void increaseBalance(int g) {
        gold += g;
    }

    public void decreaseBalance(int g) {
        gold -= g;
    }

    //file read and write
    public void save(int pos) throws IOException {
        // Opens the files and goes to the bottom of the file
        RandomAccessFile file = new RandomAccessFile("profiles.raf", "rw");
        file.seek(pos * SIZE);

        // Writes each property to the file
        file.writeChars(name);
        file.writeInt(health);
        file.writeInt(mana);
        file.writeInt(potions);
        file.writeInt(gold);
        file.writeInt(herbs);
        file.writeInt(ore);
        file.writeInt(logs);
        file.writeInt(classType);
        file.writeInt(mapNum);
        file.writeInt(xPos);
        file.writeInt(yPos);

        // Writes all 2D array elements to file
        for (int i = 0; i <= 6; i++) {
            // Writes ItemID, Durability and Upgrades
            file.writeInt(items[i][0]);
            file.writeInt(items[i][1]);
            file.writeInt(items[i][2]);
        }

        for (boolean name : progress) {
            file.writeBoolean(name);
        }

        file.close();
    }

    public void open(int pos) throws IOException {
        // Opens the file (read-only) and gets the specified position
        RandomAccessFile file = new RandomAccessFile("profiles.raf", "r");
        file.seek(pos * SIZE);

        // Read name first
        name = new String(charConvert(15, file));

        health = file.readInt();
        mana = file.readInt();
        potions = file.readInt();
        gold = file.readInt();
        herbs = file.readInt();
        ore = file.readInt();
        logs = file.readInt();
        classType = file.readInt();
        mapNum = file.readInt();
        xPos = file.readInt();
        yPos = file.readInt();

        for (int i = 0; i <= 6; i++) {
            // ItemID
            items[i][0] = file.readInt();
            // Durability
            items[i][1] = file.readInt();
            // Upgrades
            items[i][2] = file.readInt();
        }

        for (int i = 0; i < 7; i++) {
            progress[i] = file.readBoolean();
        }

        file.close();

        setActive();
        setSprites();
    }

    private char[] charConvert(int charSize, RandomAccessFile file) throws IOException {
        // Creates an array of chars to create the string
        char[] chars = new char[charSize];

        // Loops through all the chars
        for (int i = 0; i < chars.length; i++) {
            chars[i] = file.readChar();
        }
        // Returns the array of chars
        return chars;
    }

    public int fileSize(String file) {
        int numR = 0;
        try {
            RandomAccessFile recordFile = new RandomAccessFile(file, "r");
            numR = (int) (recordFile.length() / SIZE);
            //System.out.println(recordFile.length());
            //System.out.println(OBJECTSIZE);
            //System.out.println(numR);

        } catch (Exception ex) {
        }
        return numR;
    }

    public void delete(int pos) throws IOException {
        //move the last record from the file to the top and removes the empty space at the end
        open(numRecord("profiles.raf") - 1);
        save(pos);

        try {
            RandomAccessFile recordFile = new RandomAccessFile("profiles.raf", "rw");
            recordFile.setLength(recordFile.length() - SIZE);
            recordFile.close();
        } catch (IOException ex) {

        }
    }

    public int numRecord(String file) {
        int numR = 0;
        try {
            RandomAccessFile recordFile = new RandomAccessFile(file, "r");
            numR = (int) (recordFile.length() / SIZE);

        } catch (Exception ex) {
        }
        return numR;
    }

    public void endFile(int indexes) throws IOException {
        RandomAccessFile file = new RandomAccessFile("profiles.raf", "rw");
        file.setLength(indexes * SIZE);
    }

    // Comparator thats sorts by name
    public static Comparator<player> sortNames = new Comparator<player>() {
        @Override
        public int compare(player p1, player p2) {
            // Gets first names of each person
            String name1 = p1.getName().toUpperCase();
            String name2 = p2.getName().toUpperCase();

            // Descending Order
            return name1.compareTo(name2);
        }
    };

    //equip item
    public void equip() {
        int itemID = -1;

        //sets the current item id(for differentiating weapons and armour)
        if (selected == -1) {
            return;
        }

        if (items[selected - 1][0] == -1) {
            return;
        }
        itemID = items[selected - 1][0];


        //sets selected to land on the index of the object i want
        selected--;
        //retrieves the values of the currently selected item
        int currentID = items[selected][0];
        int currentDur = items[selected][1];
        int currentUpr = items[selected][2];

        //equips the item to the correct location(based on itemID(greater than 14 is armour))
        if (itemID > 14) {
            items[selected][0] = items[6][0];
            items[selected][1] = items[6][1];
            items[selected][2] = items[6][2];

            items[6][0] = currentID;
            items[6][1] = currentDur;
            items[6][2] = currentUpr;
        } else {
            items[selected][0] = items[5][0];
            items[selected][1] = items[5][1];
            items[selected][2] = items[5][2];

            items[5][0] = currentID;
            items[5][1] = currentDur;
            items[5][2] = currentUpr;
        }
        //setting it back to original value to prevent errors
        selected++;
        setActive();
    }

    //upgrade Item
    public void upgrade() {
        items[selected - 1][2] += 1;
    }

    //retrieve amount of upgrades on selected Item
    public int getUpgrade() {
        return items[selected - 1][2];
    }

    //repair Item
    public void repair() {
        if (items[selected - 1][0] > 14) {
            weapon weapon = new weapon(items[selected - 1][0]);
            items[selected - 1][1] = weapon.getDurability();
        } else {
            armour armour = new armour(items[selected - 1][0]);
            items[selected - 1][1] = armour.getDurability();
        }
    }
}

