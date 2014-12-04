package mauveturnip;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

public class MainFrame {

    private static final Logger logger = Logger.getLogger(MainFrame.class.getName());

    @FXML
    private Label mMoneyLabel;

    @FXML
    private ListView<Item> mItemListView;

    @FXML
    private VBox mItemBoxVBox;

    @FXML
    private Label mTotalIncomeLabel;

    @FXML
    private Label mDeltaPerSecLabel;

    @FXML
    private MenuItem mResetMenuItem;

    @FXML
    private ScrollPane mFieldScroll;

    @FXML
    private TextArea mMsgTextArea;

    @FXML
    private ToggleGroup buyCount;

    @FXML
    private HBox buyCountBox;

    @FXML
    void initialize() {
        System.out.println("b");
//        Global.getInstance().init();

        Global global = Global.getInstance();
        Bindings.bindBidirectional(
                mMoneyLabel.textProperty(),
                global.moneyProperty(),
                new BigDecimalStringConverter());
        Bindings.bindBidirectional(
                mTotalIncomeLabel.textProperty(),
                global.totalIncomeProperty(),
                new BigDecimalStringConverter());
        Bindings.bindBidirectional(
                mDeltaPerSecLabel.textProperty(),
                global.deltaPerSecProperty(),
                new BigDecimalStringConverter());

//        mMsgTextArea.textProperty().bind(Global.getInstance().messageProperty());
//        Global.getInstance().messageProperty().addListener((ev, o, n) -> {
//            mMsgTextArea.positionCaret(mMsgTextArea.getText().length() - 1);
//        });
        StringBinding messageBinding = new StringBinding() {
            {
                super.bind(Global.getInstance().messageListProperty());
            }
            @Override
            protected String computeValue() {
                return String.join("\n", Global.getInstance().getMessageList());
            }
        };
        mMsgTextArea.textProperty().bind(messageBinding);
        Global.getInstance().messageListProperty().addListener(new ListChangeListener<String>(){

            @Override
            public void onChanged(ListChangeListener.Change<? extends String> arg0) {
                mMsgTextArea.positionCaret(mMsgTextArea.getText().length() - 1);
            }

        });
//        mMsgTextArea.caretPositionProperty().addListener(new ChangeListener<Integer>() {
//
//            @Override
//            public void changed(ObservableValue<? extends Integer> arg0,
//                    Integer arg1, Integer arg2) {
//
//            }
//        });

        global.getEquipList().addListener((ListChangeListener<Equipment>) ev -> {
            onChangeEquipList(ev);
        });

        try {
            Parent fieldMap = FXMLLoader.load(getClass().getResource("FieldMap.fxml"));
            mFieldScroll.setContent(fieldMap);
            Platform.runLater(() -> mFieldScroll.setVvalue(0.9));
//            mFieldScroll.setVvalue(mFieldScroll.getVmax());
        } catch (IOException e) {
            e.printStackTrace();
        }

        buyCount = new ToggleGroup();
//        buyCount.selectedToggleProperty().addListener((ev, oldToggle, newToggle) -> {
//            onChangeBuyCount(newToggle);
//        });
        ToggleButton x1Button = new ToggleButton("x1");
//        x1Button.setToggleGroup(buyCount);
//        BooleanBinding bx = new BooleanBinding() {
        class Hoge extends BooleanBinding {
            private BigDecimal mCount;
             public Hoge(BigDecimal count) {
                mCount = count;
                super.bind(Global.getInstance().buyCountProperty());
            }
            @Override
            protected boolean computeValue() {
                return  Global.getInstance().getBuyCount().equals(mCount);
            }
        };
//        x1Button.selectedProperty().bind(new Hoge(new BigDecimal("1")));
        x1Button.setOnAction(ev -> {
            Global.getInstance().setBuyCount(new BigDecimal("1"));
        });
        buyCount.getToggles().add(x1Button);
        buyCountBox.getChildren().add(x1Button);
        ToggleButton x10Button = new ToggleButton("x10");
//        x10Button.selectedProperty().bind(new Hoge(new BigDecimal("10")));
        x10Button.setOnAction(ev -> {
            Global.getInstance().setBuyCount(new BigDecimal("10"));
        });
        buyCount.getToggles().add(x10Button);
        buyCountBox.getChildren().add(x10Button);
    }

//    private void onChangeBuyCount(Toggle newToggle) {
//        if (newToggle != null) {
////            Global.getInstance().setBuyCount(newToggle.getUserData());
//            System.out.println(newToggle.toString());
//
//        }
//    }

    private void onChangeEquipList(Change<? extends Equipment> ev) {
        mItemBoxVBox.getChildren().clear();
        for (Equipment equip : ev.getList()) {
//                mItemBoxVBox.getChildren().add(createEquipCell(equip));
            EquipListItem equipListItem = new EquipListItem(equip);
            BooleanBinding b = new BooleanBinding() {
                {
                    super.bind(Global.getInstance().moneyProperty());
                }
                @Override
                protected boolean computeValue() {
                    BigDecimal price = equip.getItem().getPrice().multiply(Global.getInstance().getBuyCount());
                    return Global.getInstance().getMoney().compareTo(price) < 0;
                }
            };
            equipListItem.disableProperty().bind(b);
            equipListItem.setOnMouseClicked(ev1 -> {
                onClickEquipListItem(equip);
            });
            mItemBoxVBox.getChildren().add(equipListItem);
        }
    }

    private void onClickEquipListItem(Equipment equip) {
        // 所持金減
        BigDecimal price = equip.getItem().getPrice().multiply(Global.getInstance().getBuyCount());
        BigDecimal res = Global.getInstance().getMoney().subtract(price);
        Global.getInstance().setMoney(res);
        // 所有数増
        equip.setAmount(equip.getAmount() + Global.getInstance().getBuyCount().intValue());
//        Global.getInstance().printMessage(equip.getItem().getName() + "が1増えた。");

    }

    @FXML
    void onActionQuit(ActionEvent event) {
        logger.info(() -> "called");
        Platform.exit();
        System.exit(0);
    }

    @FXML
    void onActionReset(ActionEvent event) {
        logger.info(() -> "called");
        Global.getInstance().reset();
    }

    @FXML
    void onActionItemManager(ActionEvent event) {
        logger.info(() -> "called");
        new ItemDialog().show();
    }

    private final class BigDecimalStringConverter extends StringConverter<BigDecimal> {

        private String mFormat;

        public BigDecimalStringConverter() {
            this("%,3.0f");
        }

        public BigDecimalStringConverter(String format) {
            super();
            mFormat = format;
        }

        @Override
        public String toString(BigDecimal val) {
            return String.format(mFormat, val);
        }

        @Override
        public BigDecimal fromString(String str) {
            return new BigDecimal(str);
        }
    }


}
