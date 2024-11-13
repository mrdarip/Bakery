package com.mrdarip.bakery.data.entity;

import com.mrdarip.bakery.navigation.NavController;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class Plate {

    private int id;
    private String name;
    private int valoration;
    private Plate requiredPlate;
    private String previewURI;

    public Plate(int id, String name, int valoration, Plate requiredPlate, String previewURI) {
        this.id = id;
        this.name = name;
        this.valoration = valoration;
        this.requiredPlate = requiredPlate;
        this.previewURI = previewURI;
    }

    public Plate(String name, int valoration, Plate requiredPlate, String previewURI) {
        this(-1, name, valoration, requiredPlate, previewURI);
    }

    public Plate getRequiredPlate() {
        return this.requiredPlate;
    }

    public void setRequiredPlate(Plate newRPlateequiredPlate) {
        if (newRPlateequiredPlate == null || newRPlateequiredPlate.isRegistered()) {
            this.requiredPlate = newRPlateequiredPlate;
        } else {
            //this won't happen in a real scenario by user, still, source code can add plates to the database without registering them
            throw new IllegalArgumentException("The required plate must be registered first");
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean hasChildren() {
        return this.requiredPlate != null;
    }

    public boolean isRegisteredAndItsChildren() {
        if (this.hasChildren()) {
            return this.isRegistered() && this.requiredPlate.isRegisteredAndItsChildren();
        } else {
            return this.isRegistered();
        }
    }

    public boolean isRegistered() {
        return this.id != -1;
    }

    public Pane getAsCard() {
        int cardWidth = 100;
        int cardHeight = 100;

        int TextBoxHeight = 25;
        int textBoxWidth = cardWidth;

        int previewHeight = cardHeight - TextBoxHeight;
        int previewWidth = cardWidth;

        ImageView preview = this.getPreviewImageViewCovering(previewWidth, previewHeight);

        Pane card = new Pane();
        card.setPrefSize(cardWidth, cardHeight);
        card.setStyle("-fx-background-color: #c7BEFA;");

        Label nameLbl = new Label(name);
        Label valorationLbl = new Label(this.valoration + " ★");

        HBox textBox = new HBox(nameLbl, valorationLbl);
        textBox.setPadding(new Insets(0, 0, 0, 5));
        textBox.setMaxSize(textBoxWidth, TextBoxHeight);

        VBox cardBox = new VBox(preview, textBox);

        card.getChildren().addAll(cardBox);

        card.setOnMouseClicked(e -> {
            NavController.navigateTo("/com/mrdarip/bakery/ManagePlate.fxml", this);
        });

        return card;
    }

    public ImageView getPreviewImageViewCovering(double previewWidth, double previewHeight) {
        Image image = new Image(previewURI);

        ImageView preview = new ImageView(image);
        preview.setPreserveRatio(true);

        Rectangle2D centerViewport;

        if (image.getWidth() / image.getHeight() > previewWidth / previewHeight) {
            double imgpxPerfxpx = (previewWidth / previewHeight);
            centerViewport = new Rectangle2D(image.getWidth() / 2 - (image.getHeight() * imgpxPerfxpx) / 2, 0, image.getHeight() * imgpxPerfxpx, image.getHeight());
        } else {
            double imgpxPerfxpx = (previewHeight / previewWidth);
            centerViewport = new Rectangle2D(0, image.getHeight() / 2 - (image.getWidth() * imgpxPerfxpx) / 2, image.getWidth(), image.getWidth() * imgpxPerfxpx);
        }

        preview.setFitWidth(previewWidth);
        preview.setFitHeight(previewHeight);

        preview.setViewport(centerViewport);

        return preview;
    }

    public String getName() {
        return name;
    }

    public String getPreviewURI() {
        return previewURI;
    }
}
