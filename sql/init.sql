DROP DATABASE IF EXISTS libreria;
CREATE DATABASE libreria;
USE libreria;

-- ========================
-- Tabla FichaBibliografica
-- ========================
CREATE TABLE FichaBibliografica (
    id_ficha INT AUTO_INCREMENT PRIMARY KEY,
    eliminado BOOLEAN NOT NULL DEFAULT FALSE,
    isbn VARCHAR(17) UNIQUE,
    clasificacionDewey VARCHAR(20),
    estanteria VARCHAR(20),
    idioma VARCHAR(30)
);

-- Índice
CREATE INDEX idx_ficha_isbn ON FichaBibliografica(isbn);

-- ========================
-- Tabla Libro
-- ========================
CREATE TABLE Libro (
    id_libro INT AUTO_INCREMENT PRIMARY KEY,
    eliminado BOOLEAN NOT NULL DEFAULT FALSE,
    titulo VARCHAR(150) NOT NULL,
    autor VARCHAR(120) NOT NULL,
    editorial VARCHAR(100),
    anioEdicion INT,
    id_ficha INT UNIQUE,  -- 1→1 unidireccional (Libro → Ficha)
    FOREIGN KEY (id_ficha) REFERENCES FichaBibliografica(id_ficha)
        ON UPDATE CASCADE
        ON DELETE SET NULL
);

-- Índice
CREATE INDEX idx_libro_titulo ON Libro(titulo);
