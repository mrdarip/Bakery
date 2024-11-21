package com.mrdarip.bakery.data.entity;

import com.mrdarip.bakery.composables.Card;
import com.mrdarip.bakery.data.DAO.InstructionDao;
import com.mrdarip.bakery.navigation.NavController;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class Plate {

    private int id;
    private final String name;
    private final int valoration;
    private Plate requiredPlate;
    private final String previewURI;

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
        int cardWidth = Card.CARD_WIDTH;
        int cardHeight = Card.CARD_HEIGHT;

        int TextBoxHeight = 50;
        int textBoxWidth = cardWidth;

        int previewHeight = cardHeight - TextBoxHeight;
        int previewWidth = cardWidth;

        ImageView preview = this.getPreviewImageViewCovering(previewWidth, previewHeight);

        Pane card = new Pane();
        card.setPrefSize(cardWidth, cardHeight);
        card.setStyle("-fx-background-color: #c7BEFA;");

        Label nameLbl = new Label(name);
        Label valorationLbl = new Label(this.valoration + "★");

        HBox textBox = new HBox(nameLbl, valorationLbl);
        textBox.setSpacing(8);

        textBox.setPadding(new Insets(0, 0, 0, 5));
        textBox.setMaxSize(textBoxWidth, TextBoxHeight);


        Button viewBtn = new Button("View");
        viewBtn.setOnAction(_ -> NavController.navigateTo("/com/mrdarip/bakery/view/PreviewPlate.fxml", this));
        Button editBtn = new Button("Edit");
        editBtn.setOnAction(_ -> NavController.navigateTo("/com/mrdarip/bakery/view/ManagePlate.fxml", this));

        HBox actionsBox = new HBox(viewBtn, editBtn);

        VBox detailsBox = new VBox(textBox, actionsBox);

        VBox cardBox = new VBox(preview, detailsBox);

        card.getChildren().addAll(cardBox);

        card.setOnMouseClicked(_ -> NavController.navigateTo("/com/mrdarip/bakery/view/ManagePlate.fxml", this));

        return card;
    }

    public ImageView getPreviewImageViewCovering(double previewWidth, double previewHeight) {
        Image image = new Image(previewURI);

        ImageView preview = new ImageView(image);
        preview.setPreserveRatio(true);

        Rectangle2D centerViewport = GetCenteredViewport(image, previewWidth, previewHeight);

        preview.setFitWidth(previewWidth);
        preview.setFitHeight(previewHeight);

        preview.setViewport(centerViewport);

        return preview;
    }

    public ImageView getPreviewImageViewCovering(ReadOnlyDoubleProperty width, ReadOnlyDoubleProperty height) {
        Image image = new Image(previewURI);
        ImageView preview = new ImageView(image);
        preview.setPreserveRatio(true);

        ChangeListener<Number> listener = (obs, oldVal, newVal) -> {
            double previewWidth = width.getValue();
            double previewHeight = height.getValue();
            Rectangle2D centerViewport = GetCenteredViewport(image, previewWidth, previewHeight);

            preview.setViewport(centerViewport);
            preview.setFitWidth(previewWidth);
        };

        width.addListener(listener);
        height.addListener(listener);

        return preview;
    }

    private Rectangle2D GetCenteredViewport(Image image, double previewWidth, double previewHeight) {
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


    public String getName() {
        return name;
    }

    public String getPreviewURI() {
        return previewURI;
    }

    public int getValoration() {
        return valoration;
    }

    public Node getAsScrollableArticle(InstructionDao instructionDao) {
        VBox article = new VBox();
        article.setPrefWidth(VBox.USE_COMPUTED_SIZE);
        article.setSpacing(8);

        ImageView preview = this.getPreviewImageViewCovering(400, 200);
        article.getChildren().add(preview);
        article.getChildren().add(new Label(this.name));
        article.getChildren().add(new Label("Valoration: " + this.valoration + "★"));
        article.getChildren().add(new Label("Instructions:"));
        instructionDao.getInstructionsByPlateId(this.id).forEach(instruction -> {
            article.getChildren().add(new Label("- " + instruction.getInstructionText()));
            //TODO: add sharper instructions as subInstructions
            //TODO: implement TreeView
        });

        ScrollPane scrollPane = new ScrollPane(article);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }
}
