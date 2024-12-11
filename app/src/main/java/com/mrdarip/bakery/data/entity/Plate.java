package com.mrdarip.bakery.data.entity;

import com.mrdarip.bakery.components.Card;
import com.mrdarip.bakery.data.DAO.InstructionDao;
import com.mrdarip.bakery.navigation.NavController;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

        return new Card(
                new Image(this.getPreviewURI()),
                this.name,
                this.valoration + "★",
                "✎",
                "👁",
                ev -> {
                    NavController.navigateTo("/com/mrdarip/bakery/view/EditPlate.fxml", this, null);
                },
                ev -> {
                    NavController.navigateTo("/com/mrdarip/bakery/view/PreviewPlate.fxml", this, null);
                },
                ev -> {
                    NavController.navigateTo("/com/mrdarip/bakery/view/PreviewPlate.fxml", this, null);
                });
    }


    public ImageView getPreviewImageViewCovering(ReadOnlyDoubleProperty width, ReadOnlyDoubleProperty height) {
        Image image = new Image(previewURI);
        ImageView preview = new ImageView(image);
        preview.setPreserveRatio(true);

        ChangeListener<Number> listener = (obs, oldVal, newVal) -> {
            double previewWidth = width.getValue();
            double previewHeight = height.getValue();
            Rectangle2D centerViewport = Card.GetCenteredViewport(image, previewWidth, previewHeight);

            preview.setViewport(centerViewport);
            preview.setFitWidth(previewWidth);
        };

        width.addListener(listener);
        height.addListener(listener);

        listener.changed(null, null, null);

        return preview;
    }

    public ImageView getPreviewImageViewCovering(ReadOnlyDoubleProperty width, double height) {
        return getPreviewImageViewCovering(width, new ReadOnlyDoubleProperty() {
            @Override
            public Object getBean() {
                return null;
            }

            @Override
            public String getName() {
                return "";
            }

            @Override
            public double get() {
                return height;
            }

            @Override
            public void addListener(ChangeListener<? super Number> changeListener) {

            }

            @Override
            public void removeListener(ChangeListener<? super Number> changeListener) {

            }

            @Override
            public void addListener(InvalidationListener invalidationListener) {

            }

            @Override
            public void removeListener(InvalidationListener invalidationListener) {

            }
        });
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

        ImageView preview = Card.getImageViewCovering(new Image(this.previewURI), 300, 200);
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

    @Override
    public String toString() {
        return "Plate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", valoration=" + valoration +
                ", requiredPlate=" + requiredPlate +
                ", previewURI=..." +
                '}';
    }

    public void setName(String newValue) {
        this.name = newValue;
    }

    public static Plate getEmptyPlate() {
        String sampleImage = "data:image/jpeg;base64,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";
        return new Plate(-1, "new Plate", 0, null, sampleImage);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Plate plate) {
            return plate.getId() == this.getId();
        }
        return false;
    }

    public void setPreviewURI(String encodedString) {
        this.previewURI = encodedString;
    }

    public void setValoration(int i) {
        this.valoration = i;
    }
}
