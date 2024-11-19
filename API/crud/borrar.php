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
$db = $database->dameConexion();
$updatingPlate = new Plate($db);

//B) Se decodifican los datos de entrada vía JSON
$datos = json_decode(file_get_contents("php://input"));

//C) Se comprueba que le pasamos las variables correctamente
if (isset($datos->idPlate)) {
    $updatingPlate->idPlate = $datos->idPlate;  //D) Se rellena el objeto actor con datos necesarios
    if ($updatingPlate->borrar()) {//E)Llamamos a la base de datos
        //F) Se envía respuesta y se envían los datos codificados
        http_response_code(200);
        echo json_encode(array("info" => "Plato borrado con éxito o no está en el sistema!"));
    } else {
        http_response_code(503);
        echo json_encode(array("info" => "No se puede borrar"));
    }
} else {//G) En caso de no recibir datos, informa
    http_response_code(400);
    echo json_encode(array("info" => "No se puede borrar, datos incompletos"));
}
?>