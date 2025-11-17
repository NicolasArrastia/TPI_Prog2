-- ========================
-- INSERTS INICIALES
-- ========================

-- Fichas
INSERT INTO FichaBibliografica (isbn, clasificacionDewey, estanteria, idioma)
VALUES
('9780140449136', '873.1', 'E12', 'Español'),
('9789500750124', '821.1', 'B03', 'Español'),
('9780307474278', '823.9', 'F21', 'Inglés'),
('9789876123456', '500.2', 'C19', 'Español'),
('9788497592208', '863.3', 'D07', 'Español'),
('9788420674170', '840.9', 'E01', 'Francés'),
('9780307277671', '813.5', 'A14', 'Inglés');

-- Libros
INSERT INTO Libro (titulo, autor, editorial, anioEdicion, id_ficha)
VALUES
('La Odisea', 'Homero', 'Alianza', 2010, 1),
('El Quijote', 'Miguel de Cervantes', 'Santillana', 2005, 2),
('1984', 'George Orwell', 'Penguin Books', 2013, 3),
('Breves respuestas a las grandes preguntas', 'Stephen Hawking', 'Crítica', 2018, 4),
('El Principito', 'Antoine de Saint-Exupéry', 'Debolsillo', 2008, 5),
('Madame Bovary', 'Gustave Flaubert', 'Alianza', 2012, 6),
('The Road', 'Cormac McCarthy', 'Vintage', 2006, 7);
