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
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

public class MainFrame {
//    public class MyCell extends ListCell<Item> {
//
//        private Button mBuyButton = new Button("buy");
//        private Button mSellButton = new Button("sell");
//        {
//            mBuyButton.setOnAction((ev) -> System.out.println("i="+getItem().getName()));
//        }
//
//        @Override
//        protected void updateItem(Item item, boolean empty) {
//            super.updateItem(item, empty);
//            if (empty) {
//                setGraphic(null);
//            } else {
////                HBox hbox1 = new HBox(new Label("item:"+item.getName()), new Label("item:"+item.getName()));
////                mBuyButton = new Button("buy");
////                buyButton.setOnAction((ev) -> {
////                    System.out.println("buy");
////                });
//                HBox hbox2 = new HBox();
////                mBuyButton.setText("buy");
////                hbox2.getChildren().addAll(mBuyButton, mSellButton);
////                VBox vbox = new VBox(hbox1, hbox2);
////                mBuyButton.setText("buy");
//                setGraphic(mBuyButton);
//            }
//        }
//
//    }

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
//            mFieldScroll.setVvalue(mFieldScroll.getVmax());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
                    return Global.getInstance().getMoney().compareTo(equip.getItem().getPrice()) < 0;
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
        BigDecimal res = Global.getInstance().getMoney().subtract(equip.getItem().getPrice());
        Global.getInstance().setMoney(res);
        // 所有数増
        equip.setAmount(equip.getAmount() + 1);
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


}
