package mauveturnip;


import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import com.sun.javafx.scene.control.skin.BehaviorSkinBase;

public class EquipListItemSkin extends BehaviorSkinBase<EquipListItem, EquipListItemBehavior> {

    protected EquipListItemSkin(EquipListItem equipListItem) {
        super(equipListItem, new EquipListItemBehavior(equipListItem, null));
//        getChildren().add(new Label("hoge"));
//        getSkinnable().setStyle("-fx-background-color:#ffaadd");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EquipCell.fxml"));
            Parent equipCell = loader.load();
            EquipCell ctrl = loader.getController();
            getChildren().add(equipCell);
            ctrl.setEquipment(getSkinnable().getEquip());
//            Parent equipCell = FXMLLoader.load(getClass().getResource("EquipCell.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
