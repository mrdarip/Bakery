package com.mrdarip.bakery.components;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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

    public Card(Image image, String title, EventHandler<MouseEvent> onCrdClick) {
        this(image, title, null, null, null, ev -> {
        }, ev -> {
        }, onCrdClick);
    }

    public Card(Image image, String title, String subtitle, String primaryLabel, String secondaryLabel, EventHandler<ActionEvent> onPrimary, EventHandler<ActionEvent> onSecondary, EventHandler<MouseEvent> onCardCLick) {
        int TextBoxHeight = 50;
        int textBoxWidth = CARD_WIDTH;

        int previewHeight = CARD_HEIGHT - TextBoxHeight;
        int previewWidth = CARD_WIDTH;

        ImageView preview = getImageViewCovering(image, previewWidth, previewHeight);

        Pane card = new Pane();
        card.setPrefSize(CARD_WIDTH, CARD_HEIGHT);
        card.setStyle("-fx-background-color: #c7BEFA;");

        Label titleLbl = new Label(title);
        Label subtitleLbl = new Label(subtitle);

        HBox textBox = new HBox(titleLbl, subtitleLbl);
        textBox.setSpacing(8);
        textBox.setPadding(new Insets(0, 0, 0, 5));
        textBox.setMaxSize(textBoxWidth, TextBoxHeight);

        HBox actionsBox = new HBox();

        Button viewBtn;
        if (primaryLabel != null) {
            viewBtn = new Button(primaryLabel);
            viewBtn.setOnAction(onPrimary);

            actionsBox.getChildren().add(viewBtn);
        }

        Button editBtn;
        if (secondaryLabel != null) {
            editBtn = new Button(secondaryLabel);
            editBtn.setOnAction(onSecondary);

            actionsBox.getChildren().add(editBtn);
        }


        VBox detailsBox = new VBox(textBox, actionsBox);

        VBox cardBox = new VBox(preview, detailsBox);

        card.getChildren().addAll(cardBox);

        card.setOnMouseClicked(onCardCLick);

        getChildren().add(card);
    }


    public static ImageView getImageViewCovering(Image image, double previewWidth, double previewHeight) {
        ImageView preview = new ImageView(image);
        preview.setPreserveRatio(true);

        Rectangle2D centerViewport = GetCenteredViewport(image, previewWidth, previewHeight);

        preview.setFitWidth(previewWidth);
        preview.setFitHeight(previewHeight);

        preview.setViewport(centerViewport);

        return preview;
    }

    public static Rectangle2D GetCenteredViewport(Image image, double previewWidth, double previewHeight) {
        Rectangle2D centerViewport;

        if (image.getWidth() / image.getHeight() > previewWidth / previewHeight) {
            double imgpxPerfxpx = (previewWidth / previewHeight);
            centerViewport = new Rectangle2D(image.getWidth() / 2 - (image.getHeight() * imgpxPerfxpx) / 2, 0, image.getHeight() * imgpxPerfxpx, image.getHeight());
        } else {
            double imgpxPerfxpx = (previewHeight / previewWidth);
            centerViewport = new Rectangle2D(0, image.getHeight() / 2 - (image.getWidth() * imgpxPerfxpx) / 2, image.getWidth(), image.getWidth() * imgpxPerfxpx);
        }
        return centerViewport;
    }

    //Text card
    public Card(String content, EventHandler<MouseEvent> onClick) {
        setPrefSize(CARD_WIDTH, CARD_HEIGHT);
        setOnMouseClicked(onClick);
        getChildren().add(new Label(content));
    }
}