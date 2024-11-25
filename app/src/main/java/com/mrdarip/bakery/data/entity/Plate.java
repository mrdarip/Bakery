package com.mrdarip.bakery.data.entity;

import com.mrdarip.bakery.composables.Card;
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

        return new Card(
                new Image(this.getPreviewURI()),
                this.name,
                "Valoration: " + this.valoration + "★",
                "Edit",
                "View",
                ev -> {
                    NavController.navigateTo("/com/mrdarip/bakery/view/ManagePlate.fxml", this, null);
                },
                ev -> {
                    NavController.navigateTo("/com/mrdarip/bakery/view/ViewPlate.fxml", this, null);
                },
                ev -> {
                    NavController.navigateTo("/com/mrdarip/bakery/view/SelectPlate.fxml", this, null);
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
}
