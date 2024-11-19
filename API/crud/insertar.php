<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type");

include_once '../basedatos/DBconnector.php';
include_once '../tablas/Plate.php';

//A) Se crea conexión y objeto act
$database = new DBconnector();
$conex = $database->dameConexion();
$updatingPlate = new Plate($conex);

//B) Se decodifican los datos de entrada vía JSON
$datos = json_decode(file_get_contents("php://input"));

//C) Se comprueba que le pasamos las variables correctamente
if (isset($datos->plateName) && isset($datos->valoration) && isset($datos->idRequiredPlate) && isset($datos->uri_preview)) {

    //D) Se rellena el objeto actor con datos salvo el id
    $updatingPlate->plateName = $datos->plateName;
    $updatingPlate->valoration = $datos->valoration;
    $updatingPlate->idRequiredPlate = $datos->idRequiredPlate;
    $updatingPlate->uri_preview = $datos->uri_preview;

    //DEBUG: echo "Nombres es.".$act->plateName;

    //E) Llamamos a la base de datos
    if ($updatingPlate->insertar()) {
        //F) Se envía respuesta y se envían los datos codificados
        http_response_code(201);
        echo json_encode(array("info" => "Plato Creado!"));
    } else {
        http_response_code(503);
        echo json_encode(array("info" => "No se puede crear"));
    }
} else { //D) En caso de no recibir datos, informa
    http_response_code(400);
    echo json_encode(array("info" => "No se puede crear, falta algo!"));
}

?>