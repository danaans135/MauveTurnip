package mauveturnip;

import java.io.IOException;
import java.math.BigDecimal;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Global {

    private static Global sInstance = new Global();
//    private LongProperty money = new SimpleLongProperty();
    private ObjectProperty<BigDecimal> money = new SimpleObjectProperty<BigDecimal>();
    private ObjectProperty<BigDecimal> totalIncome = new SimpleObjectProperty<BigDecimal>();

    private ObjectProperty<BigDecimal> deltaPerSec = new SimpleObjectProperty<BigDecimal>();
    public ObjectProperty<BigDecimal> deltaPerSecProperty() { return deltaPerSec; }
    public BigDecimal getDeltaPerSec() { return deltaPerSec.get(); }
    public void setDeltaPerSec(BigDecimal deltaPerSec) { this.deltaPerSec.set(deltaPerSec); }

//    private StringProperty message = new SimpleStringProperty();
//    public StringProperty messageProperty() { return message; }
//    public String getMessage() { return message.get(); }
//    public void setMessage(String message) { this.message.set(message); }
//    public void printMessage(String message) {
//        if (getMessage() != null && 0 < getMessage().length()) {
//            setMessage(getMessage() + System.lineSeparator() + message);
//        } else {
//            setMessage(message);
//        }
//    }

    private ObjectProperty<BigDecimal> buyCount = new SimpleObjectProperty<BigDecimal>();
    public ObjectProperty<BigDecimal> buyCountProperty() { return buyCount; }
    public BigDecimal getBuyCount() { return buyCount.get(); }
    public void setBuyCount(BigDecimal buyCount) { this.buyCount.set(buyCount); }

    private ListProperty<String> messageList = new SimpleListProperty<String>(FXCollections.observableArrayList());
    public ListProperty<String> messageListProperty() { return messageList; }
    public ObservableList<String> getMessageList() { return messageList.get(); }
    public void setMessageList(ObservableList<String> messageList) { this.messageList.set(messageList); }
    public void printMessage(String message) {
        getMessageList().add(0, message);
        if (getMessageList().size() > 10) getMessageList().remove(10);
    }

    private Timeline mLoop;
    private ObservableList<Item> items = FXCollections.observableArrayList();
    private ObservableList<Equipment> equipList = FXCollections.observableArrayList();

    private Global() {}

    public static Global getInstance() {
        return sInstance ;
    }

    public ObjectProperty<BigDecimal> moneyProperty() {
        return money;
    }

    public BigDecimal getMoney() {
        return money.get();
    }

    public void setMoney(BigDecimal money) {
        this.money.set(money);
    }

    public ObjectProperty<BigDecimal> totalIncomeProperty() {
        return totalIncome;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome.get();
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome.set(totalIncome);
    }

    public void init(Stage stage) throws IOException {

        int fps = 30;
        Duration frameAmt = Duration.millis(1000/fps);
        KeyFrame keyFrame = new KeyFrame(frameAmt, ev -> mainloop());
        mLoop = new Timeline();
        mLoop.getKeyFrames().add(keyFrame);
        mLoop.setCycleCount(Animation.INDEFINITE);

        setMoney(new BigDecimal("10"));
        setTotalIncome(new BigDecimal("0"));

        getItems().add(new Item("Ari", 0, 10));
        getItems().add(new Item("Tombo", 0, 200));
        getItems().add(new Item("Hachi", 3000, 3200));
        getItems().add(new Item("Kamikiri", 70000, 60000));
        getItems().add(new Item("Kuwagata", 2000000, 1200000));
        getItems().add(new Item("Kabuto", 14000000, 10000000));

        setBuyCount(new BigDecimal("1"));
        // アリ、トンボ、ハチ、クワガタ、カブトムシ、カミキリ、チョウ、バッタ
//        getEquipList().add(new Equipment(new Item("A1")));
//        getEquipList().add(new Equipment(new Item("A2")));

        Parent root = FXMLLoader.load(getClass().getResource("MainFrame.fxml"));
        stage.setScene(new Scene(root));
    }

//    private boolean tmpFlag = false;
    private void mainloop() {
//        double deltaPerSec = 1.0;
//        double deltaPerSec = calcDeltaPerSec();
        setDeltaPerSec(BigDecimal.valueOf(calcDeltaPerSec()));
        BigDecimal deltaPerFrame = getDeltaPerSec().divide(BigDecimal.valueOf(30), 3, BigDecimal.ROUND_HALF_UP);
//        BigDecimal deltaPerFrame = new BigDecimal(String.format("%.3f", (deltaPerSec/30)));
        setMoney(getMoney().add(deltaPerFrame));
        setTotalIncome(getTotalIncome().add(deltaPerFrame));

        for (Item item : getItems()) {
            if (!isEquipment(item)) {
                if (isReleaseItem(item)) {
                    getEquipList().add(new Equipment(item));
                    printMessage(item.getName() + "が使用可能になった。");
                }
            }
        }

    }

    private double calcDeltaPerSec() {
        double deltaPerSec = 0.0;
        for (Equipment equip : getEquipList()) {
            BigDecimal multiply = equip.getItem().getPrice()
                    .multiply(BigDecimal.valueOf(equip.getAmount()))
                    .divide(BigDecimal.valueOf(10), 3, BigDecimal.ROUND_HALF_UP);
            deltaPerSec += multiply.doubleValue();
        }
        return deltaPerSec;
    }

    private boolean isReleaseItem(Item item) {
        if (item.getReleaseAmount().compareTo(BigDecimal.ZERO) >= 0) {
            return getTotalIncome().compareTo(item.getReleaseAmount()) >= 0;
        }
        return Math.random() < 0.01;
    }

    private boolean isEquipment(Item item) {
        boolean hit = false;
        for (Equipment equip : getEquipList()) {
            if (item.equals(equip.getItem())) {
                hit = true;
                break;
            }
        }
        return hit;
    }

    public void start() {
        mLoop.play();
    }

    public ObservableList<Item> getItems() {
        return items;
    }

    public void setItems(ObservableList<Item> items) {
        this.items = items;
    }

    public ObservableList<Equipment> getEquipList() {
        return equipList;
    }

    public void setEquipList(ObservableList<Equipment> equipList) {
        this.equipList = equipList;
    }
    public void reset() {
        getEquipList().clear();
        setMoney(new BigDecimal(100));
        setTotalIncome(new BigDecimal(0));
    }

}
