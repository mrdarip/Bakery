package com.mrdarip.bakery.composables;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class Card {
    public static final int cardWidth = 200;
    public static final int cardHeight = 150;

    public static Pane simpleCard(ImageView icon, String title, String description, EventHandler<MouseEvent> eventHandler) {
        HBox cardContent = new HBox();
        cardContent.setAlignment(Pos.CENTER);
        cardContent.setSpacing(16);

        icon.setFitWidth(32);
        icon.setFitHeight(32);
        cardContent.getChildren().add(icon);

        VBox cardText = new VBox(new Label(title), new Label(description));
        cardText.setAlignment(Pos.CENTER);
        cardContent.getChildren().addAll(cardText);


        StackPane outputPane = new StackPane(cardContent);
        outputPane.setPrefWidth(cardWidth);
        outputPane.setPrefHeight(cardHeight);
        outputPane.setStyle("-fx-background-color: gray;");
        outputPane.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
        return outputPane;
    }
}
