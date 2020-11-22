package dylanJaxon;

import java.io.IOException;
import java.io.RandomAccessFile;

public class weapon {
    private int damage, durability, itemID;
    private final int recLength = 12;
    //12 bytes for ints(3 + ints * 4 bytes each)


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //get values
    public int getDamage() {
        return damage;
    }

    public int getDurability() {
        return durability;
    }

    public int getID() {
        return itemID;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //change values
    public void setDamage(int damage1) {
        this.damage = damage1;
    }

    public void setDurability(int durability1) {
        this.durability = durability1;
    }

    public void setID(int ID) {
        this.itemID = ID;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //constructors
    public weapon(int ID) {
        setID(ID);
        getWeapon("item.raf", ID);
    }

    public weapon(int damage, int durability) {
        setDamage(damage);
        setDurability(durability);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //random access files
    public void getWeapon(String file, int record) {
        try {
            //opening desired record
            RandomAccessFile recordFile = new RandomAccessFile(file, "r");
            recordFile.seek(record * recLength);

            //reading the integers
            damage = recordFile.readInt();
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
            recordFile.writeInt(damage);
            recordFile.writeInt(durability);
            recordFile.writeInt(itemID);

            //close the file
            recordFile.close();
        } catch (IOException e) {

        }
    }

    /*
    SWORDS
    ItemID: 0
    Damage: 10
    MaxDurability: 15

    ItemID: 1
    Damage: 15
    MaxDurability: 20

    ItemID: 2
    Damage: 20
    MaxDurability: 20

    ItemID: 3
    Damage: 30
    MaxDurability: 25

    ItemID: 4
    Damage: 40
    MaxDurability: 30

    BOWS
    ItemID: 5
    Damage: 20
    MaxDurability: 10

    ItemID: 6
    Damage: 25
    MaxDurability: 15

    ItemID: 7
    Damage: 30
    MaxDurability: 15

    ItemID: 8
    Damage: 40
    MaxDurability: 20

    ItemID: 9
    Damage: 45
    MaxDurability: 25

    STAVES
    ItemID: 10
    Damage: 25
    MaxDurability: 10

    ItemID: 11
    Damage: 30
    MaxDurability: 10

    ItemID: 12
    Damage: 40
    MaxDurability: 15

    ItemID: 13
    Damage: 45
    MaxDurability: 15

    ItemID: 14
    Damage: 55
    MaxDurability: 20
     */
}
