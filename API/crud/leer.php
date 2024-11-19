<?php
//Se han activado los ERRORES (displayError) en el servidor!!!
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Headers: Content-Type");
header("Content-Type: application/json; charset=UTF-8");

include_once '../basedatos/DBconnector.php';
include_once '../tablas/Plate.php';


//A) Se crea conexión y objeto act
$database = new DBconnector();
$conex = $database->dameConexion();
$updatingPlate = new Plate($conex);

//B) Se establecen criterios de búsqueda
//EJ: LEER POR idPlate
//Verificamos que se le está pasando una variable con isset:
//Si es un número buscará ese idPlate, si lo encuentra muestra el actor, si no, informará que NO lo ha encontrado
//Si no le pasamos la variable idPlate, o no tiene valor (?idPlate=) devolverá -1 y lo mostrará todo
if (isset($_GET['idPlate']))
    $updatingPlate->idPlate = $_GET['idPlate'];
else
    $updatingPlate->idPlate = -1; //Mostrará todo

$result = $updatingPlate->leer();//Mostrará o bien el idPlate buscado o bien todo

//OTRA POSIBLE FUNCIONALIDAD: LEER VIVOS
if (isset($_GET['notaMenorQue'])) {
    $updatingPlate->valoration = $_GET['notaMenorQue'];
    $result = $updatingPlate->leerPlatosConNotaMenorQue();
}
//Tal cual está montado no contempla el uso de ambas a la vez

//C) Se leen los datos devueltos y se guardan en un array
if ($result->num_rows > 0) {
    if ($result->num_rows == 1)
        $unsoloplate = true; //Esta variable la usa la APP de JAVAFX

    $listaActores = array();
    while ($plate = $result->fetch_assoc()) { //Crea un array asociativo con cada actor
        extract($plate); //Exporta las variables de un array
        $datosExtraidos = array(
            "idPlate" => $idPlate,
            "plateName" => $plateName,
            "valoration" => $valoration,
            "idRequiredPlate" => $idRequiredPlate,
            "uri_preview" => $uri_preview
        );
        array_push($listaActores, $datosExtraidos); //Hace un append al final de la lista 
    }
    //D) Se envía respuesta y se envían los datos codificados
    http_response_code(200);
    if ($unsoloplate)//Con este cambio funcionará también para la API con JAVAFX
        //donde solo queremos sacar los actores de uno en uno, no todos
        echo json_encode($datosExtraidos);
    else
        echo json_encode($listaActores);
} else { //E) En caso de no recibir datos, informa
    http_response_code(404);
    echo json_encode(
        array("info" => "No se encontraron datos")
    );
}