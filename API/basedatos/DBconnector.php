<?php

class DBconnector
{
    private $host = 'localhost';
    private $user = 'admin';
    private $password = "vAVAOLcH68gL";
    private $database = "bakery";

    public function dameConexion()
    {
        $conn = new mysqli($this->host, $this->user, $this->password, $this->database);
        $conn->set_charset("utf8");
        if ($conn->connect_error) {
            die("Error al conectar con MYSQL" . $conn->connect_error);
        } else {
            return $conn;
        }
    }
}

?>