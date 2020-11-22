package dylanJaxon;

import java.io.IOException;
import java.io.RandomAccessFile;

public class armour {

    private int defense, durability, itemID;
    private final int recLength = 12;
    //12 bytes for ints(3 ints * 4 bytes each)


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //get values
    public int getDefense() {
        return defense;
    }

    public int getDurability() {
        return durability;
    }

    public int getID() {
        return itemID;
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //change values
    public void setDefense(int defense1) {
        this.defense = defense1;
    }

    public void setDurability(int durability1) {
        this.durability = durability1;
    }

    public void setID(int ID) {
        this.itemID = ID;
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //constructors
    public armour(int ID) {
        // Sets item ID and gets its stats
        setID(ID);
        getArmour("item.raf", ID);
    }

    public armour(int protection, int durability) {
        setDefense(protection);
        setDurability(durability);

    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //random access files
    public void getArmour(String file, int record) {
        try {
            //opening desired record
            RandomAccessFile recordFile = new RandomAccessFile(file, "r");
            recordFile.seek(record * recLength);

            //reading the integers
            defense = recordFile.readInt();
            durability = recordFile.readInt();
            itemID = recordFile.readInt();

            //close the file
            recordFile.close();
        } catch (IOException e) {

        }
    }

    public void write(String file, int record) {
        try {
            //opening desired record
            RandomAccessFile recordFile = new RandomAccessFile(file, "rw");
            recordFile.seek(record * recLength);

            //write all values
            recordFile.writeInt(defense);
            recordFile.writeInt(durability);
            recordFile.writeInt(itemID);

            //close the file
            recordFile.close();
        } catch (IOException e) {

        }
    }

    /*
    ARMOUR
    ItemID: 0
    Defense: 5
    Durability: 20

    ItemID: 1
    Defense: 10
    Durability: 25

    ItemID: 2
    Defense: 20
    Durability: 30

    ItemID: 3
    Defense: 25
    Durability: 40

    ItemID: 4
    Defense: 30
    Durability: 45
     */
}
