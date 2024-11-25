package com.mrdarip.bakery.navigation;

public interface Navigable {
    String getScreenTitle();
    int getMinWidth();
    int getMinHeight();

    void setOrigin(Navigable origin);
}
