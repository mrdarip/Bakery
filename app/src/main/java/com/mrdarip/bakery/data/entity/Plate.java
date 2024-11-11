package com.mrdarip.bakery.data.entity;

import com.mrdarip.bakery.data.DAO.PlateDao;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class Plate {
    private int id;
    private String name;
    private int valoration;
    private Plate requiredPlate;
    private String previewURI;


    protected Plate(int id, String name, int valoration, Plate requiredPlate, String previewURI) {
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRequiredPlate(Plate newRPlateequiredPlate) {
        if (newRPlateequiredPlate == null || newRPlateequiredPlate.isRegistered()) {

            this.requiredPlate = newRPlateequiredPlate;
        } else {
            //this won't happen in a real scenario by user, still, source code can add plates to the database without registering them
            throw new IllegalArgumentException("The required plate must be registered first");
        }
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
        int cardHeight = 101;

        int TextBoxHeight = 1;
        int textBoxWidth = cardWidth;

        int previewHeight = cardHeight - TextBoxHeight;
        int previewWidth = cardWidth;


        Pane card = new Pane();
        card.setPrefSize(cardWidth, cardHeight);

        card.setStyle("-fx-background-color: #f00;");

        Image image = new Image(previewURI); //For obtaining the image width and height later

        ImageView preview = new ImageView(image);
        preview.setPreserveRatio(true);  // Mantener las proporciones de la imagen

        Rectangle2D centerViewport;


        if (image.getWidth() / image.getHeight() > 1) {

            int imgpxPerfxpx = (int) (image.getHeight() / previewHeight);
            centerViewport = new Rectangle2D(
                image.getWidth() / 2 - (double) (imgpxPerfxpx * previewWidth) / 2,
                0,
                imgpxPerfxpx * previewWidth,
                image.getHeight()
            );
        } else {
            preview.setFitWidth(previewWidth);

            int imgpxPerfxpx = (int) (image.getWidth() / previewWidth);
            centerViewport = new Rectangle2D(
                0,
                image.getHeight() / 2 - (double) (imgpxPerfxpx * previewHeight) / 2,
                image.getWidth(),
                previewHeight * imgpxPerfxpx
            );
        }

        preview.setViewport(centerViewport);

        Label nameLbl = new Label(name);
        Label valorationLbl = new Label(this.valoration + " ★");

        HBox textBox = new HBox(nameLbl, valorationLbl);
        textBox.setPadding(new Insets(0, 0, 0, 5));
        textBox.setMaxSize(textBoxWidth, TextBoxHeight);

        VBox cardBox = new VBox(preview, textBox);

        card.getChildren().addAll(cardBox);

        return card;
    }
}
