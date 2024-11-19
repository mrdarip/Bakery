<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type");

include_once '../basedatos/DBconnector.php';
include_once '../tablas/Plate.php';

//A) Se crea conexión y objeto Plate para actualizar
$database = new DBconnector();
$db = $database->dameConexion();
$updatingPlate = new Plate($db);

//B) Se decodifican los datos de entrada vía JSON
$datos = json_decode(file_get_contents("php://input"));

//C1) Se comprueba que le pasamos las variables correctamente, sin imageURI
if (isset($datos->idPlate) && isset($datos->plateName) && isset($datos->valoration) && isset($datos->idRequiredPlate)) {

    //D) Se rellena el objeto actor con datos
    $updatingPlate->idPlate = $datos->idPlate;
    $updatingPlate->plateName = $datos->plateName;
    $updatingPlate->valoration = $datos->valoration;
    $updatingPlate->idRequiredPlate = $datos->idRequiredPlate;

    //D2) Se rellena imageURI, si se ha pasado
    if (isset($datos->uri_preview)) {
        $updatingPlate->uri_preview = $datos->uri_preview;

    }

    if ($updatingPlate->actualizar()) {//E)Llamamos a la base de datos
        //F) Se envía respuesta y se envían los datos codificados
        http_response_code(200);
        echo json_encode(array("info" => "Plate actualizado"));

    } else {
        http_response_code(503);
        echo json_encode(array("info" => "No se ha podido actualizar"));
    }

} else {//G) En caso de no recibir datos, informa
    http_response_code(400);
    echo json_encode(array("info" => "No se ha podido actualizar. Datos incompletos."));
}

?>