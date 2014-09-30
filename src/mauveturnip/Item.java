package mauveturnip;

import java.math.BigDecimal;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Data;

public class Item {

    private StringProperty name = new SimpleStringProperty();
    private BigDecimal releaseAmount;
    private ObjectProperty<BigDecimal> price = new SimpleObjectProperty<BigDecimal>();
    public ObjectProperty<BigDecimal> priceProperty() {
        return price;
    }

    public BigDecimal getPrice() {
        return price.get();
    }

    public void setPrice(BigDecimal price) {
        this.price.set(price);
    }

    public Item(String name, BigDecimal releaseAmount) {
        this(name, releaseAmount, BigDecimal.valueOf(10));
    }

    public Item(String name, BigDecimal releaseAmount, BigDecimal price) {
        super();
        this.name.set(name);
        this.price.set(price);
        this.releaseAmount = releaseAmount;
    }

    public Item(String name, long releaseAmount, long price) {
        this(name, BigDecimal.valueOf(releaseAmount), BigDecimal.valueOf(price));
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public BigDecimal getReleaseAmount() {
        return releaseAmount;
    }

    public void setReleaseAmount(BigDecimal releaseAmount) {
        this.releaseAmount = releaseAmount;
    }

}
