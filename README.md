# Sistema de Gestión de Libros y Fichas Bibliográficas

Aplicación de consola en Java que permite **gestionar libros** y sus **fichas bibliográficas** asociadas, utilizando una **relación 1→1 unidireccional** (Libro → FichaBibliografica) persistida en **MySQL** mediante **JDBC** y el patrón **DAO + Service**.

---

## 1. Descripción del dominio

El sistema modela el dominio de una **librería / biblioteca simple**, donde:

- Cada **Libro** tiene:

  - título
  - autor
  - editorial
  - año de edición
  - (opcionalmente) una **ficha bibliográfica** asociada

- Cada **FichaBibliografica** tiene:
  - ISBN
  - Clasificación Dewey
  - Estantería
  - Idioma

La relación es:

- A nivel de dominio: **Libro → FichaBibliografica (0..1 : 0..1)**
- A nivel de base de datos: la tabla `Libro` tiene una columna `id_ficha` con `UNIQUE`, que implementa la relación 1→1 y permite `NULL` (un libro puede no tener ficha).

---

## 2. Requisitos

### Software

- **Java**: JDK 21
- **MySQL**: 8.4
- **Conector JDBC de MySQL**: `mysql-connector-j` agregado al classpath / proyecto
- \*\*Opcional :
  - Apache NetBeans / IntelliJ / Eclipse
  - Cliente MySQL (MySQL Workbench o línea de comandos)

### Configuración por defecto de la conexión

Definida en `tpi_prog2.config.DatabaseConnection`:

- **URL**: `jdbc:mysql://localhost:3307/libreria`
- **Usuario**: `root`
- **Contraseña**: _(vacía)_

Estos valores pueden sobreescribirse con system properties:

## 3. Estructura del proyecto

Paquetes principales:
tpi_prog2.config
.DatabaseConnection
.TransactionManager

tpi_prog2.models
.Base
.Libro
.FichaBibliografica

tpi_prog2.dao
.GenericDAO<T>
.FichaBibliograficaDAO
.LibroDAO

tpi_prog2.service
.GenericService<T>
.FichaBibliograficaServiceImpl
.LibroServiceImpl

tpi_prog2.main
.Main
.AppMenu
.MenuDisplay
.MenuHandler
.TestConexion

sql
.init.sql
.inserts.sql

## 4. Cómo ejecutar el SQL

Abrir una consola de MySQL o MySQL Workbench.

Ejecutar el script de creación de base y tablas: init.sql.

Ejecutar el script de inserción de datos: inserts.sql.

Desde la línea de comandos
mysql -u root -p

Luego dentro del cliente:
SOURCE /ruta/al/proyecto/sql/init.sql;
SOURCE /ruta/al/proyecto/sql/inserts.sql;

El script init.sql:
Elimina la BD si existe:
DROP DATABASE IF EXISTS libreria;

Crea la BD:
CREATE DATABASE libreria;

Crea las tablas:
.FichaBibliografica
.Libro (con id_ficha INT UNIQUE, FK a FichaBibliografica(id_ficha))

El script inserts.sql:
Inserta fichas bibliográficas de ejemplo.

Inserta libros de ejemplo asociados a esas fichas.

## 5. Pasos para crear la base: init.sql y inserts.sql

Orden sugerido:
Ejecutar init.sql

Crea la base libreria.

Crea las tablas con sus PK, FK, índices e índices únicos.

Ejecutar inserts.sql

Inserta datos de prueba en:

FichaBibliografica

Libro

Verificar luego:
USE libreria;
SELECT _ FROM FichaBibliografica;
SELECT _ FROM Libro;

## 6. Cómo compilar

Importar el proyecto como Proyecto Java.

Asegurarse de que el conector JDBC de MySQL esté agregado al proyecto:

En NetBeans: Libraries → Add JAR/Folder → seleccionar mysql-connector-j-xx.jar.

Configurar el proyecto para usar Java 17+.

Luego simplemente usar Run sobre la clase:
tpi_prog2.main.Main

## 8. Credenciales de prueba y conexión

Por defecto, la aplicación intenta conectarse con:
Host: localhost

Puerto: 3307

Base de datos: libreria

Usuario: root

Contraseña: (vacía)

Si tu MySQL corre en otro puerto (por ejemplo 3306), tenés dos opciones:
Cambiar la constante en DatabaseConnection:

private static final String URL =
System.getProperty("db.url", "jdbc:mysql://localhost:3306/libreria");

## 9. Menú principal y flujo básico

Al ejecutar la aplicación, se muestra el siguiente menú:

```
-=-=-=-=-= LIBROS -=-=-=-=-=
1. Crear libro
2. Listar libros
3. Actualizar libro
4. Eliminar libro

-=-=-=-=-= FICHAS -=-=-=-=-=
5. Crear ficha bibliográfica
6. Listar fichas bibliográficas
7. Actualizar ficha bibliográfica por ID
8. Eliminar ficha bibliográfica por ID

-=-=-=-=-= OTROS -=-=-=-=-=
9. Actualizar ficha bibliográfica por ID de libro
10. Eliminar ficha bibliográfica por ID de libro
0. SALIR
```

Flujo básico sugerido
Listar fichas bibliográficas
Opción 6 → para ver las fichas iniciales de ejemplo.

Listar libros
Opción 2 → permite listar todos o filtrar por título/autor.

Crear un libro
Opción 1 → se solicitan:

Título

Autor

Editorial

Año de edición

Si se desea crear una ficha asociada (se crea y se persiste antes del libro).

Actualizar un libro
Opción 3 → permite:

Cambiar título, autor, editorial, año.

Actualizar o crear ficha asociada al libro.

Eliminar un libro
Opción 4 → baja lógica (campo eliminado = TRUE), sin borrar la ficha asociada.

Gestionar fichas de forma independiente

Crear ficha suelta: opción 5

Listar fichas: opción 6

Actualizar por ID: opción 7

Eliminar por ID: opción 8

Actualizar ficha de un libro: opción 9

Eliminar ficha de un libro (de forma segura): opción 10

## 10. Test de conexión

La clase tpi_prog2.main.TestConexion permite verificar la conexión a la base:
Si la conexión es exitosa, se mostrará información del usuario, base de datos y driver.

## 11. Enlace del video

https://youtu.be/ssby3XFB6Kw
