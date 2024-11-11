package com.mrdarip.bakery.data.entity;

import com.mrdarip.bakery.data.DAO.PlateDao;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

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
    
    public Plate getRequiredPlate(){
        return this.requiredPlate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setRequiredPlate(Plate newRPlateequiredPlate){
        if(newRPlateequiredPlate == null || newRPlateequiredPlate.isRegistered()){
            
            this.requiredPlate = newRPlateequiredPlate;
        }
        else{
            //this won't happen in a real scenario by user, still, source code can add plates to the database without registering them
            throw new IllegalArgumentException("The required plate must be registered first");
        }
    }

    public boolean hasChildren(){
        return this.requiredPlate != null;
    }
    
    public boolean isRegisteredAndItsChildren(){
        if(this.hasChildren()){
            return this.isRegistered() && this.requiredPlate.isRegisteredAndItsChildren();
        }else{
            return this.isRegistered();
        }
    }

    public boolean isRegistered(){
        return this == null || this.id != -1;
    }

    public Pane getAsCard() {
        Pane card = new Pane();

        ImageView preview = new ImageView(previewURI);
        preview.setFitWidth(100);
        preview.setFitHeight(100);

        Label nameLbl = new Label(name);
        nameLbl.getStyleClass().add("plate-name");

        Label valorationLbl = new Label("Valoración: " + valoration);
        valorationLbl.getStyleClass().add("plate-valoration");

        card.getChildren().addAll(preview, nameLbl, valorationLbl);

        return card;
    }
}
