package mauveturnip;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Global.getInstance().init(stage);
        Global.getInstance().start();
        stage.show();
    }

    public static void main(String[] args) {
        launch(Main.class);
    }

}
