package mauveturnip;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;

import javafx.beans.binding.Bindings;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

public class EquipCell {
    @FXML
    private Label mNameLabel;

    @FXML
    private Label mAmountLabel;

    @FXML
    private Label mPriceLabel;

    @FXML
    private ImageView mImageView;

    private Equipment mEquipment;

    public Equipment getEquipment() {
        return mEquipment;
    }

    public void setEquipment(Equipment equipment) {
        mEquipment = equipment;
        mNameLabel.textProperty().bind(mEquipment.getItem().nameProperty());
//        mAmountLabel.textProperty().bind(mEquipment.getAmount());
        Bindings.bindBidirectional(mAmountLabel.textProperty(), mEquipment.amountProperty(), new NumberStringConverter());
        Bindings.bindBidirectional(mPriceLabel.textProperty(), mEquipment.getItem().priceProperty(), new PriceStringConverter());
        BufferedImage img = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        img.createGraphics().fillOval(1, 1, 30, 30);
        WritableImage fxImage = SwingFXUtils.toFXImage(img, null);
//        Image img = new Image(arg0, arg1);
        mImageView.setImage(fxImage );
    }

    private final class PriceStringConverter extends StringConverter<BigDecimal> {
        @Override
        public String toString(BigDecimal val) {
            return String.format("%,3.0f", val);
        }

        @Override
        public BigDecimal fromString(String str) {
            return new BigDecimal(str);
        }
    }

}
