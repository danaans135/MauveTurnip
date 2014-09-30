package mauveturnip;

import javafx.scene.control.Control;

public class EquipListItem extends Control {

    private Equipment mEquip;

    public EquipListItem(Equipment equip) {
        super();
        mEquip = equip;
        setSkin(new EquipListItemSkin(this));
    }

    public Equipment getEquip() {
        return mEquip;
    }

    public void setEquip(Equipment equip) {
        mEquip = equip;
    }

}
