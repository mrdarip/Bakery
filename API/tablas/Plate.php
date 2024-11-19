<?php

class Plate
{
    public $idPlate;
    public $plateName;
    public $valoration;
    public $idRequiredPlate;
    public $uri_preview;
    private $tabla = "Plate";
    private $conn;

    public function __construct($db)
    {
        $this->conn = $db;
    }

    function leer()
    {
        if ($this->idPlate >= 0) {
            $stmt = $this->conn->prepare("
			SELECT * FROM " . $this->tabla . " WHERE idPlate = ?");
            $stmt->bind_param("i", $this->idPlate);
        } else { //Si no se le pasa id correcto hace un SELECT masivo
            $stmt = $this->conn->prepare("SELECT * FROM " . $this->tabla);
        }
        $stmt->execute();
        $result = $stmt->get_result();
        return $result;
    }

    function leerPlatosConNotaMenorQue()
    {
        $stmt = $this->conn->prepare("
           SELECT * FROM " . $this->tabla . " WHERE valoration < ?");
        $stmt->bind_param("i", $this->valoration);

        $stmt->execute();
        $result = $stmt->get_result();
        return $result;
    }

    function insertar()
    {
        $stmt = $this->conn->prepare("
		    INSERT INTO " . $this->tabla . "(`plateName`, `valoration`, `idRequiredPlate`, `uri_preview`)
			VALUES(?,?,?,?)");

        $this->plateName = strip_tags($this->plateName);
        $this->valoration = strip_tags($this->valoration);
        $this->idRequiredPlate = strip_tags($this->idRequiredPlate);
        $this->uri_preview = strip_tags($this->uri_preview);

        $stmt->bind_param("siis", $this->plateName, $this->valoration, $this->idRequiredPlate, $this->uri_preview);
        if ($stmt->execute()) {
            return true;
        }
        return false;
    }


    function actualizar()
    {
        $this->plateName = strip_tags($this->plateName);
        $this->valoration = strip_tags($this->valoration);
        $this->idRequiredPlate = strip_tags($this->idRequiredPlate);
        $this->idPlate = strip_tags($this->idPlate);

        if (isset($this->uri_preview)) {
            $this->uri_preview = strip_tags($this->uri_preview);
            $stmt = $this->conn->prepare("
            UPDATE " . $this->tabla . "
            SET plateName = ?, uri_preview = ?, valoration = ?, idRequiredPlate = ?
            WHERE idPlate = ?");
            $stmt->bind_param("sssii", $this->plateName, $this->uri_preview, $this->valoration, $this->idRequiredPlate, $this->idPlate);

        } else {
            $stmt = $this->conn->prepare("
            UPDATE " . $this->tabla . "
            SET plateName = ?, valoration = ?, idRequiredPlate = ?
            WHERE idPlate = ?");
            $stmt->bind_param("ssii", $this->plateName, $this->valoration, $this->idRequiredPlate, $this->idPlate);

        }

        if ($stmt->execute()) {
            return true;
        }

        return false;
    }


    function borrar()
    {
        $this->idPlate = strip_tags($this->idPlate);

        $stmt = $this->conn->prepare("
			DELETE FROM " . $this->tabla . " 
			WHERE idPlate = ?");

        $this->idPlate = strip_tags($this->idPlate);
        $stmt->bind_param("i", $this->idPlate);
        if ($stmt->execute()) {
            return true;
        }

        return false;
    }

}

?>