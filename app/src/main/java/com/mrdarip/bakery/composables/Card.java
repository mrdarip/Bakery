package com.mrdarip.bakery.composables;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class Card extends StackPane {
    public static final int CARD_WIDTH = 200;
    public static final int CARD_HEIGHT = 150;
    private static final String CARD_STYLE = "-fx-background-color: gray;";

    public Card(ImageView icon, String title, String description, EventHandler<MouseEvent> eventHandler) {
        super();
        if (icon == null || title == null || description == null || eventHandler == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }

        HBox cardContent = new HBox();
        cardContent.setAlignment(Pos.CENTER);
        cardContent.setSpacing(16);

        icon.setFitWidth(32);
        icon.setFitHeight(32);
        cardContent.getChildren().add(icon);

        VBox cardText = new VBox(new Label(title), new Label(description));
        cardText.setAlignment(Pos.CENTER);
        cardContent.getChildren().add(cardText);


        setPrefWidth(CARD_WIDTH);
        setPrefHeight(CARD_HEIGHT);
        setStyle(CARD_STYLE);
        addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
        getChildren().add(cardContent);
    }
}