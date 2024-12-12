package com.mrdarip.bakery.navigation;

import javafx.scene.image.Image;
import javafx.stage.Stage;

public interface Navigable {
    String getScreenTitle();

    Image getScreenIcon();
    int getMinWidth();
    int getMinHeight();

    void setOrigin(Navigable origin);

    void setStage(Stage stage);

    void rebuildUI();

    void close();

}
