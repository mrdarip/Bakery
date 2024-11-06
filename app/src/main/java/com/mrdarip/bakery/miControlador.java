package com.mrdarip.bakery;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class miControlador implements Initializable {

    @FXML
    private Label miEtiqueta;

    @FXML
    private void botonPulsado() {
        miEtiqueta.setText("Hola Mundo");
        System.out.println("Hola Mundo!!");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        miEtiqueta.setText("");
    }
}
