package com.sms;

import com.sms.Model;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage){
        // Shows login View
        Model.getInstance().getViewFactory().showLogin();
    }
}
