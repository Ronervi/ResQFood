-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: resqfood
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `administrador`
--

DROP TABLE IF EXISTS `administrador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `administrador` (
  `idAdministrador` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `contrasena` varchar(255) NOT NULL,
  `estado` varchar(20) NOT NULL DEFAULT 'Activo',
  PRIMARY KEY (`idAdministrador`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `administrador`
--

LOCK TABLES `administrador` WRITE;
/*!40000 ALTER TABLE `administrador` DISABLE KEYS */;
INSERT INTO `administrador` VALUES (1,'Administrador','administrador@gmail.com','65536:z1vDVkqOsv2Rg1J+S6fqHQ==:1xVywpRhWyoET8dqQmnZmoJKRFk2eLUnUmSdYGgA0VI=','Activo');
/*!40000 ALTER TABLE `administrador` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `calificacion`
--

DROP TABLE IF EXISTS `calificacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `calificacion` (
  `idCalificacion` int NOT NULL AUTO_INCREMENT,
  `idUsuario` int NOT NULL,
  `idEstablecimiento` int NOT NULL,
  `puntuacion` int NOT NULL,
  `comentario` text,
  `fecha` datetime DEFAULT NULL,
  PRIMARY KEY (`idCalificacion`),
  KEY `fk_calificacion_usuario` (`idUsuario`),
  KEY `fk_calificacion_establecimiento` (`idEstablecimiento`),
  CONSTRAINT `fk_calificacion_establecimiento` FOREIGN KEY (`idEstablecimiento`) REFERENCES `establecimiento` (`idEstablecimiento`),
  CONSTRAINT `fk_calificacion_usuario` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`idUsuario`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `calificacion`
--

LOCK TABLES `calificacion` WRITE;
/*!40000 ALTER TABLE `calificacion` DISABLE KEYS */;
INSERT INTO `calificacion` VALUES (1,4,5,5,'excelente calidad de las facturas','2026-10-18 16:41:09');
/*!40000 ALTER TABLE `calificacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `establecimiento`
--

DROP TABLE IF EXISTS `establecimiento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `establecimiento` (
  `idEstablecimiento` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `direccion` varchar(150) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `ubicacion` varchar(150) DEFAULT NULL,
  `Estado` varchar(20) NOT NULL,
  `contraseña` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`idEstablecimiento`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `establecimiento`
--

LOCK TABLES `establecimiento` WRITE;
/*!40000 ALTER TABLE `establecimiento` DISABLE KEYS */;
INSERT INTO `establecimiento` VALUES (2,'Rotisería Don José','Avellaneda 987','2429284828','donjorge@gmail.com','Córdoba','Activo',NULL),(3,'panaderia azul','toledo','2453675439','panaderiaazul@gmail.com','al frente de la plaza','Aprobado','65536:CQEVPeWEIsIA0ZOfHzN9VA==:9OZ05SG1nDiq8CzkX9xMf65cj5/I7D1d2azQYyCX3cM='),(4,'cafeteria luz','avellaneda','2454367890','cafeterialuz@gmail.com','frente a la municipalidad','Rechazado','65536:AU9tTx7P3kkGAMOBtXDnFw==:SwFvv2FljFU8CLxwvMYs+sxIaRXtZcTgCo08FY3675w='),(5,'panaderia la paz','belgrano 234','2476122298','panaderialapaz@gmail.com','al frente del parque','Aprobado','65536:xMCoc7Guv7hNP+9Pxvaq1w==:6zNQryNegUKPo66LCW8X0fmqGhcgsa4aQV+cROQA55Y=');
/*!40000 ALTER TABLE `establecimiento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `publicacion`
--

DROP TABLE IF EXISTS `publicacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `publicacion` (
  `idPublicacion` int NOT NULL AUTO_INCREMENT,
  `idEstablecimiento` int DEFAULT NULL,
  `nombre` varchar(100) NOT NULL,
  `descripcion` text,
  `cantidad` int NOT NULL,
  `precio` decimal(10,2) NOT NULL,
  `descuento` decimal(5,2) DEFAULT NULL,
  `fechaVencimiento` date DEFAULT NULL,
  `fechaPublicacion` datetime NOT NULL,
  `horarioRetiro` varchar(50) DEFAULT NULL,
  `estado` varchar(20) NOT NULL,
  PRIMARY KEY (`idPublicacion`),
  KEY `fk_publicacion_establecimiento` (`idEstablecimiento`),
  CONSTRAINT `fk_publicacion_establecimiento` FOREIGN KEY (`idEstablecimiento`) REFERENCES `establecimiento` (`idEstablecimiento`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `publicacion`
--

LOCK TABLES `publicacion` WRITE;
/*!40000 ALTER TABLE `publicacion` DISABLE KEYS */;
INSERT INTO `publicacion` VALUES (2,3,'facturas varias','facturas hechas ayer',30,0.00,0.00,'2026-10-19','2026-10-17 19:50:42','20:00-21:00','Disponible'),(3,5,'facturas surtidas','facturas hechas ayer a la tarde',18,500.00,0.00,'2026-10-19','2026-10-18 16:32:52','20:00-21:00','Disponible');
/*!40000 ALTER TABLE `publicacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reserva`
--

DROP TABLE IF EXISTS `reserva`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reserva` (
  `idReserva` int NOT NULL AUTO_INCREMENT,
  `idUsuario` int NOT NULL,
  `idPublicacion` int NOT NULL,
  `fecha` datetime NOT NULL,
  `cantidad` int NOT NULL,
  `horarioRetiro` varchar(50) DEFAULT NULL,
  `estado` varchar(20) NOT NULL,
  PRIMARY KEY (`idReserva`),
  KEY `fk_reserva_usuario` (`idUsuario`),
  KEY `fk_reserva_publicacion` (`idPublicacion`),
  CONSTRAINT `fk_reserva_publicacion` FOREIGN KEY (`idPublicacion`) REFERENCES `publicacion` (`idPublicacion`),
  CONSTRAINT `fk_reserva_usuario` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`idUsuario`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reserva`
--

LOCK TABLES `reserva` WRITE;
/*!40000 ALTER TABLE `reserva` DISABLE KEYS */;
INSERT INTO `reserva` VALUES (1,4,3,'2026-10-18 16:34:41',12,'20:00-21:00','Retirada');
/*!40000 ALTER TABLE `reserva` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `idUsuario` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `apellido` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `contraseña` varchar(255) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `estado` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`idUsuario`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (1,'Ana','Gómez','ana@gmail.com','123456','2923456789','Activo'),(2,'rocio','nervi','rocio@gmail.com','65536:AWVVD7Y2cNtwJUyshfJupQ==:YCCE8LrvtWZWcNkT+arN4FYlnk7XXgBTu/LGQD1LgRA=','3654287650','Activo'),(3,'mario','antin','mario@gmail.com','65536:dhH6lXETLSCwjmCMAMXOug==:za9wl4+eR17/JT63RSUg3IolQzNVWAazc3bIjcpaE1I=','2543675987','Activo'),(4,'Marcelo','Martinez','marcelo@gmail.com','65536:8EyXbNMWEczTmwrl4kwLxA==:oUpB/7INsVvvQzIWozHDPzjQ2rOuD4xwsssbiUtb1iY=','2834764588','Activo');
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-18 17:01:50
