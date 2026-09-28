USE resqfood;
INSERT INTO Usuario
(idUsuario, nombre, apellido, email, contraseña, telefono, estado)
VALUES
(1, 'Ana', 'Gómez', 'ana@gmail.com', '123456', '2923456789', 'Activo'),
(2, 'Lucas', 'Pérez', 'lucas@gmail.com', 'abcdef', '2929123456', 'Activo');

INSERT INTO Establecimiento
(idEstablecimiento, nombre, direccion, telefono, email, ubicacion, estado)
VALUES
(1, 'Panadería central', 'San Martín 234', '2425985674', 'panaderiacentral@gmail.com', 'Buenos Aires', 'Activo'),
(2, 'Rotisería Don José', 'Avellaneda 987', '2429284828', 'donjorge@gmail.com', 'Córdoba', 'Activo');

INSERT INTO Publicacion
(idPublicacion, idEstablecimiento, nombre, descripcion, cantidad, precio, descuento, fechaVencimiento, fechaPublicacion, horarioRetiro, estado)
VALUES
(1, 2, 'Tarta de verdura', 'Tarta con masa de hojaldre y relleno de verduras', '10', '7000', '20.00', '01-10-2026', '27-09-2026', '20:30', 'Activo'),
(2, 1, 'Medialunas', 'Medialunas dulces elaboradas 100% de manteca', '30', '1500', '15.00', '30-09-2026', '27-09-2026', '12:00', 'Activo');

INSERT INTO Reserva
(idReserva, idUsuario, idPublicacion, fecha, cantidad, horarioRetiro, estado)
VALUES
(1, 1, 2, '28-09-2026', '6', '12:00', 'Activo'),
(2, 2, 1, '29-09-2026', '2', '20:30', 'Activo');

INSERT INTO Calificacion
(idCalificacion, idUsuario, idEstablecimiento, puntuacion, comentario, fecha)
VALUES
(1, 1, 1, '5', 'El producto se encontraba en muy buen estado y su precio y descuentos fueron adecuados', '29-09-2026'),
(2, 2, 2, '3', 'Se podía notar que las medialunas no estaban recién hechas', '29-09-2026');

SELECT * FROM Usuario;

SELECT * FROM Establecimiento;

DELETE FROM Usuario
WHERE idUsuario = 2;
SELECT * FROM Usuario;

DELETE FROM Establecimiento
WHERE idEstablecimiento = 1;
SELECT * FROM Establecimiento;

