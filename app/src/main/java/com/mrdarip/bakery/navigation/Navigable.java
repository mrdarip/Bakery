package com.mrdarip.bakery.navigation;

import javafx.stage.Stage;

public interface Navigable {
    String getScreenTitle();
    int getMinWidth();
    int getMinHeight();

    void setOrigin(Navigable origin);

    void setScene(Stage stage);

}
