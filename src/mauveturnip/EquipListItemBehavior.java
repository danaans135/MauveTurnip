package mauveturnip;

import java.util.List;

import javafx.scene.input.MouseEvent;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;

public class EquipListItemBehavior extends BehaviorBase<EquipListItem> {

    public EquipListItemBehavior(EquipListItem equipListItem, List<KeyBinding> paramList) {
        super(equipListItem, paramList);
    }

    @Override
    public void mouseEntered(MouseEvent ev) {
        getControl().setStyle("-fx-background-color:#66ffcc");
    }

    @Override
    public void mouseExited(MouseEvent ev) {
        getControl().setStyle("-fx-background-color:#ffaacc");
    }

}
