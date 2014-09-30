package mauveturnip;

import java.io.IOException;
import java.math.BigDecimal;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.BigDecimalStringConverter;

public class ItemDialog {

    private Stage mDialog;

    public ItemDialog() {
        mDialog = new Stage(StageStyle.UTILITY);
        mDialog.initModality(Modality.NONE);
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("Template.fxml"));

            TableView<Item> table = new TableView<Item>(Global.getInstance().getItems());
            table.setEditable(true);

            TableColumn<Item, String> name = new TableColumn<>("name");
            name.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
//            name.setCellFactory(new Callback<TableColumn, TableCell<Item, String>>() {
//                @Override
//                public TableCell call(TableColumn col) {
//                    TableCell cell = new TableCell(){
//                        @Override
//                        public void updateItem(Object item, boolean empty){
//                            if(item !=null){
//                                setText(item.toString());
//                            }
//                        }
//                    };
////                    cell.setAlignment(Pos.TOP_RIGHT);
//                    return cell;
//                }
//            });
            name.setCellFactory(TextFieldTableCell.forTableColumn());
//            name.setOnEditCommit(
//                    new EventHandler<CellEditEvent<Item, String>>() {
//                        @Override
//                        public void handle(CellEditEvent<Item, String> t) {
//                            ((Item) t.getTableView().getItems().get(
//                                    t.getTablePosition().getRow())
//                                    ).setName(t.getNewValue());
//                        }
//                    }
//                    );

            TableColumn<Item, BigDecimal> releaseAmount = new TableColumn<>("releaseAmount");
            releaseAmount.setCellValueFactory(new PropertyValueFactory<Item, BigDecimal>("releaseAmount"));
            releaseAmount.setCellFactory(TextFieldTableCell.forTableColumn(new BigDecimalStringConverter()));
            releaseAmount.setOnEditCommit(
                    new EventHandler<CellEditEvent<Item, BigDecimal>>() {
                        @Override
                        public void handle(CellEditEvent<Item, BigDecimal> t) {
                            Item item = (Item) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow());
                            item.setReleaseAmount(t.getNewValue());
                        }
                    });


            TableColumn<Item, BigDecimal> price = new TableColumn<>("price");
            price.setCellValueFactory(new PropertyValueFactory<Item, BigDecimal>("price"));
//            price.setCellFactory(TextFieldTableCell.forTableColumn());
            price.setCellFactory(TextFieldTableCell.forTableColumn(new BigDecimalStringConverter()));
            price.setOnEditCommit(
                    new EventHandler<CellEditEvent<Item, BigDecimal>>() {
                        @Override
                        public void handle(CellEditEvent<Item, BigDecimal> t) {
                            Item item = (Item) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow());
                            item.setPrice(t.getNewValue());
                        }
                    });


            table.getColumns().addAll(name, releaseAmount, price);

            mDialog.setScene(new Scene(table));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void show() {
        mDialog.show();
    }
}
