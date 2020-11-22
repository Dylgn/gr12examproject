package dylanJaxon;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class itemOverride implements Initializable {
    @FXML
    private TextField txtShrtSrdDmg, txtLngSrdDmg, txtSqrSrdDmg, txtKntSrdDmg, txtKngSrdDmg, txtKngSrdDur, txtShrtSrdDur, txtLngSrdDur, txtSqrSrdDur, txtKntSrdDur;
    @FXML
    private TextField txtShrtBowDmg, txtHntBowDmg, txtRcvBowDmg, txtLngBowDmg, txtElvBowDmg, txtShrtBowDur, txtHntBowDur, txtRcvBowDur, txtLngBowDur, txtElvBowDur;

    @FXML
    private TextField txtWdnStfDmg, txtQtzStfDmg, txtAprStfDmg, txtSapStfDmg, txtMasStfDmg, txtWdnStfDur, txtQtzStfDur, txtAprStfDur, txtSapStfDur, txtMasStfDur;

    @FXML
    private TextField txtCltAmrPrt, txtLthAmrPrt, txtChnAmrPrt, txtPltAmrPrt, txtKntAmrPrt, txtCltAmrDur, txtLthAmrDur, txtChnAmrDur, txtPltAmrDur, txtKntAmrDur;

    //create an array of the item id's
    weapon[] weapons = new weapon[15];
    armour[] armours = new armour[5];

    //set and retrieve values from the file
    public void read() {
        for (int i = 0; i < 15; i++) {
            weapons[i] = new weapon(i);
            weapons[i].getWeapon("item.raf", i);
        }
        for (int i = 15; i < 20; i++) {
            armours[i - 15] = new armour(i);
            armours[i - 15].getArmour("item.raf", i);
        }
    }

    public void write() {
        for (weapon name : weapons) {
            name.write("item.raf", name.getID());
        }
        for (armour name : armours) {
            name.write("item.raf", name.getID());
        }
    }


    //return to the main menu
    @FXML
    void btnReturn(ActionEvent event) throws IOException {
        MainApp.setRoot("menu", "");
        MainApp.setSize(375, 375);
    }

    //update the values then write them to the file
    @FXML
    void btnUpload(ActionEvent event) {
        save(0, txtShrtSrdDmg, txtShrtSrdDur);
        save(1, txtLngSrdDmg, txtLngSrdDur);
        save(2, txtSqrSrdDmg, txtSqrSrdDur);
        save(3, txtKntSrdDmg, txtKntSrdDur);
        save(4, txtKngSrdDmg, txtKngSrdDur);
        save(5, txtShrtBowDmg, txtShrtBowDur);
        save(6, txtHntBowDmg, txtHntBowDur);
        save(7, txtRcvBowDmg, txtRcvBowDur);
        save(8, txtLngBowDmg, txtLngBowDur);
        save(9, txtElvBowDmg, txtElvBowDur);
        save(10, txtWdnStfDmg, txtWdnStfDur);
        save(11, txtQtzStfDmg, txtQtzStfDur);
        save(12, txtAprStfDmg, txtAprStfDur);
        save(13, txtSapStfDmg, txtSapStfDur);
        save(14, txtMasStfDmg, txtMasStfDur);
        save(15, txtCltAmrPrt, txtCltAmrDur);
        save(16, txtLthAmrPrt, txtLthAmrDur);
        save(17, txtChnAmrPrt, txtChnAmrDur);
        save(18, txtPltAmrPrt, txtPltAmrDur);
        save(19, txtKntAmrPrt, txtKntAmrDur);
        write();
    }

    //save the values from the text box
    private void save(int loc, TextField damageText, TextField durabilityText) {
        if (!(loc > 14)) {
            weapons[loc].setDamage(Integer.parseInt(damageText.getText()));
            weapons[loc].setDurability(Integer.parseInt(durabilityText.getText()));
            weapons[loc].setID(loc);
        } else {
            armours[loc - 15].setDefense(Integer.parseInt(damageText.getText()));
            armours[loc - 15].setDurability(Integer.parseInt(durabilityText.getText()));
            armours[loc - 15].setID(loc);
        }

    }

    //write the values into the text boxes
    private void load(int loc, TextField damageText, TextField durabilityText) {
        if (!(loc > 14)) {
            damageText.setText("" + weapons[loc].getDamage());
            durabilityText.setText("" + weapons[loc].getDurability());
        } else {
            damageText.setText("" + armours[loc - 15].getDefense());
            durabilityText.setText("" + armours[loc - 15].getDurability());
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //write the initial values
        read();
        load(0, txtShrtSrdDmg, txtShrtSrdDur);
        load(1, txtLngSrdDmg, txtLngSrdDur);
        load(2, txtSqrSrdDmg, txtSqrSrdDur);
        load(3, txtKntSrdDmg, txtKntSrdDur);
        load(4, txtKngSrdDmg, txtKngSrdDur);
        load(5, txtShrtBowDmg, txtShrtBowDur);
        load(6, txtHntBowDmg, txtHntBowDur);
        load(7, txtRcvBowDmg, txtRcvBowDur);
        load(8, txtLngBowDmg, txtLngBowDur);
        load(9, txtElvBowDmg, txtElvBowDur);
        load(10, txtWdnStfDmg, txtWdnStfDur);
        load(11, txtQtzStfDmg, txtQtzStfDur);
        load(12, txtAprStfDmg, txtAprStfDur);
        load(13, txtSapStfDmg, txtSapStfDur);
        load(14, txtMasStfDmg, txtMasStfDur);
        load(15, txtCltAmrPrt, txtCltAmrDur);
        load(16, txtLthAmrPrt, txtLthAmrDur);
        load(17, txtChnAmrPrt, txtChnAmrDur);
        load(18, txtPltAmrPrt, txtPltAmrDur);
        load(19, txtKntAmrPrt, txtKntAmrDur);
    }
}
