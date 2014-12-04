package mauveturnip;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;





import javafx.beans.binding.BooleanBinding;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class FieldMapController {

    private final class BooleanBindingPrice extends BooleanBinding {
        private BigDecimal mPrice;

        {
            super.bind(Global.getInstance().moneyProperty());
        }

        public BooleanBindingPrice(BigDecimal price) {
            mPrice = price;
        }

        @Override
        protected boolean computeValue() {
            return Global.getInstance().getMoney().compareTo(mPrice) < 0;
        }
    }

    public static class CheckPoint {

        private String mName;
        private BigDecimal mPrice;

        public CheckPoint(String name, BigDecimal price) {
            mName = name;
            mPrice = price;
        }


    }

    @FXML
    private AnchorPane fieldMapPane;

    private static final CheckPoint[] checkPoint = {
        new CheckPoint("Daizy", new BigDecimal(50000)),
        new CheckPoint("Rose", new BigDecimal(180000)),
    };

    @FXML
    void initialize() {
//        for (int i = 0; i < 10; i++) {
//            Circle circle = new Circle(120, 503 - i*20, 8);
////            circle.setStyle("check-point");
//            circle.getStyleClass().add("check-point");
//            if (i % 2 == 0) circle.setDisable(true);
//            fieldMapPane.getChildren().add(circle);
//        }
        ObservableList<Node> list = fieldMapPane.getChildrenUnmodifiable();
        List<Circle> checkPointList = new ArrayList<>();
        for (Node node : list) {
            if (node instanceof Circle && node.getStyleClass().contains("check-point")) {
                checkPointList.add((Circle)node);
            }
        }
        Global global = Global.getInstance();
        Tooltip.install(checkPointList.get(0), new Tooltip(checkPoint[0].mName));
        checkPointList.get(0).setOnMouseClicked(ev -> {
            global.setMoney(global.getMoney().subtract(checkPoint[0].mPrice));
        });
        Tooltip.install(checkPointList.get(1), new Tooltip(checkPoint[1].mName));
        checkPointList.get(1).setOnMouseClicked(ev -> {
            global.setMoney(global.getMoney().subtract(checkPoint[1].mPrice));
        });
        checkPointList.get(0).disableProperty().bind(new BooleanBindingPrice(new BigDecimal(50000)));
        checkPointList.get(1).disableProperty().bind(new BooleanBindingPrice(new BigDecimal(180000)));
        checkPointList.get(2).disableProperty().bind(new BooleanBindingPrice(new BigDecimal(3600000)));
        checkPointList.get(3).disableProperty().bind(new BooleanBindingPrice(new BigDecimal(23000000)));
    }
}
