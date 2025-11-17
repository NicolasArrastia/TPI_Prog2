package tpi_prog2.main;

import tpi_prog2.models.Libro;
import tpi_prog2.models.FichaBibliografica;
import tpi_prog2.service.LibroServiceImpl;
import tpi_prog2.service.FichaBibliograficaServiceImpl;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * MenuHandler para Libros y FichaBibliografica.
 * Maneja toda la interacción de consola.
 */
public class MenuHandler {

    private final Scanner scanner;
    private final LibroServiceImpl libroService;

    public MenuHandler(Scanner scanner, LibroServiceImpl libroService) {
        if (scanner == null) throw new IllegalArgumentException("Scanner no puede ser null");
        if (libroService == null) throw new IllegalArgumentException("LibroService no puede ser null");

        this.scanner = scanner;
        this.libroService = libroService;
    }

    // ============================================================
    // 1. CREAR LIBRO
    // ============================================================
    public void crearLibro() {
        try {
            System.out.print("Título: ");
            String titulo = scanner.nextLine().trim();

            System.out.print("Autor: ");
            String autor = scanner.nextLine().trim();

            System.out.print("Editorial: ");
            String editorial = scanner.nextLine().trim();

            System.out.print("Año de edición (Enter para 0): ");
            String anioStr = scanner.nextLine().trim();
            int anio = anioStr.isEmpty() ? 0 : Integer.parseInt(anioStr);

            FichaBibliografica ficha = null;
            System.out.print("¿Desea agregar una ficha bibliográfica? (s/n): ");
            if (scanner.nextLine().equalsIgnoreCase("s")) {
                ficha = crearFicha();
            }

            Libro libro = new Libro(0, false, titulo, autor, editorial, anio, ficha);
            libroService.insertar(libro);

            System.out.println("Libro creado exitosamente con ID: " + libro.getId());
        } catch (Exception e) {
            System.err.println("Error al crear libro: " + e.getMessage());
        }
    }

    // ============================================================
    // 2. LISTAR LIBROS
    // ============================================================
    public void listarLibros() {
        try {
            System.out.print("¿Desea (1) listar todos o (2) buscar por título/autor?: ");
            int sub = Integer.parseInt(scanner.nextLine().trim());

            List<Libro> libros;

            if (sub == 1) {
                libros = libroService.getAll();
            } else if (sub == 2) {
                System.out.print("Texto a buscar: ");
                String filtro = scanner.nextLine().trim().toLowerCase();
                libros = libroService.getAll().stream()
                        .filter(l ->
                                (l.getTitulo() != null && l.getTitulo().toLowerCase().contains(filtro)) ||
                                (l.getAutor() != null && l.getAutor().toLowerCase().contains(filtro))
                        )
                        .collect(Collectors.toList());
            } else {
                System.out.println("Opción inválida.");
                return;
            }

            if (libros.isEmpty()) {
                System.out.println("No se encontraron libros.");
                return;
            }

            for (Libro l : libros) {
                System.out.println("ID: " + l.getId() +
                        " | Título: " + l.getTitulo() +
                        " | Autor: " + l.getAutor() +
                        " | Editorial: " + l.getEditorial() +
                        " | Año: " + l.getAnioEdicion());

                if (l.getFichaBibliografica() != null) {
                    FichaBibliografica f = l.getFichaBibliografica();
                    System.out.println("   Ficha ID: " + f.getId() +
                            " | ISBN: " + f.getIsbn() +
                            " | Dewey: " + f.getClasificacionDewey() +
                            " | Estantería: " + f.getEstanteria() +
                            " | Idioma: " + f.getIdioma());
                }
            }

        } catch (Exception e) {
            System.err.println("Error al listar libros: " + e.getMessage());
        }
    }

    // ============================================================
    // 3. ACTUALIZAR LIBRO
    // ============================================================
    public void actualizarLibro() {
        try {
            System.out.print("ID del libro: ");
            int id = Integer.parseInt(scanner.nextLine().trim());

            Libro libro = libroService.getById(id);
            if (libro == null) {
                System.out.println("Libro no encontrado.");
                return;
            }

            System.out.print("Nuevo título (actual: " + libro.getTitulo() + "): ");
            String tx = scanner.nextLine().trim();
            if (!tx.isEmpty()) libro.setTitulo(tx);

            System.out.print("Nuevo autor (actual: " + libro.getAutor() + "): ");
            tx = scanner.nextLine().trim();
            if (!tx.isEmpty()) libro.setAutor(tx);

            System.out.print("Nueva editorial (actual: " + libro.getEditorial() + "): ");
            tx = scanner.nextLine().trim();
            if (!tx.isEmpty()) libro.setEditorial(tx);

            System.out.print("Nuevo año (actual: " + libro.getAnioEdicion() + "): ");
            tx = scanner.nextLine().trim();
            if (!tx.isEmpty()) libro.setAnioEdicion(Integer.parseInt(tx));

            actualizarFichaDeLibro(libro);

            libroService.actualizar(libro);
            System.out.println("Libro actualizado.");
        } catch (Exception e) {
            System.err.println("Error al actualizar libro: " + e.getMessage());
        }
    }

    // ============================================================
    // 4. ELIMINAR LIBRO
    // ============================================================
    public void eliminarLibro() {
        try {
            System.out.print("ID a eliminar: ");
            int id = Integer.parseInt(scanner.nextLine());
            libroService.eliminar(id);
            System.out.println("Libro eliminado.");
        } catch (Exception e) {
            System.err.println("Error al eliminar libro: " + e.getMessage());
        }
    }

    // ============================================================
    // 5. CREAR FICHA INDEPENDIENTE
    // ============================================================
    public void crearFichaIndependiente() {
        try {
            FichaBibliografica f = crearFicha();
            libroService.getFichaService().insertar(f);
            System.out.println("Ficha creada con ID: " + f.getId());
        } catch (Exception e) {
            System.err.println("Error al crear ficha: " + e.getMessage());
        }
    }

    // ============================================================
    // 6. LISTAR FICHAS
    // ============================================================
    public void listarFichas() {
        try {
            List<FichaBibliografica> fichas = libroService.getFichaService().getAll();

            if (fichas.isEmpty()) {
                System.out.println("No hay fichas.");
                return;
            }

            for (FichaBibliografica f : fichas) {
                System.out.println("ID: " + f.getId() +
                        " | ISBN: " + f.getIsbn() +
                        " | Dewey: " + f.getClasificacionDewey() +
                        " | Estantería: " + f.getEstanteria() +
                        " | Idioma: " + f.getIdioma());
            }

        } catch (Exception e) {
            System.err.println("Error al listar fichas: " + e.getMessage());
        }
    }

    // ============================================================
    // 7. ACTUALIZAR FICHA POR ID
    // ============================================================
    public void actualizarFichaPorId() {
        try {
            System.out.print("ID de ficha: ");
            int id = Integer.parseInt(scanner.nextLine());

            FichaBibliografica f = libroService.getFichaService().getById(id);
            if (f == null) {
                System.out.println("Ficha no encontrada.");
                return;
            }

            System.out.print("ISBN (actual: " + f.getIsbn() + "): ");
            String tx = scanner.nextLine().trim();
            if (!tx.isEmpty()) f.setIsbn(tx);

            System.out.print("Dewey (actual: " + f.getClasificacionDewey() + "): ");
            tx = scanner.nextLine().trim();
            if (!tx.isEmpty()) f.setClasificacionDewey(tx);

            System.out.print("Estantería (actual: " + f.getEstanteria() + "): ");
            tx = scanner.nextLine().trim();
            if (!tx.isEmpty()) f.setEstanteria(tx);

            System.out.print("Idioma (actual: " + f.getIdioma() + "): ");
            tx = scanner.nextLine().trim();
            if (!tx.isEmpty()) f.setIdioma(tx);

            libroService.getFichaService().actualizar(f);
            System.out.println("Ficha actualizada.");

        } catch (Exception e) {
            System.err.println("Error al actualizar ficha: " + e.getMessage());
        }
    }

    // ============================================================
    // 8. ELIMINAR FICHA POR ID
    // ============================================================
    public void eliminarFichaPorId() {
        try {
            System.out.print("ID de ficha: ");
            int id = Integer.parseInt(scanner.nextLine());
            libroService.getFichaService().eliminar(id);
            System.out.println("Ficha eliminada.");
        } catch (Exception e) {
            System.err.println("Error al eliminar ficha: " + e.getMessage());
        }
    }

    // ============================================================
    // 9. ACTUALIZAR FICHA DE LIBRO (MENÚ)
    // ============================================================
    public void actualizarFichaDeLibro() {
        try {
            System.out.print("ID del libro: ");
            int libroId = Integer.parseInt(scanner.nextLine());

            Libro libro = libroService.getById(libroId);
            if (libro == null) {
                System.out.println("Libro no encontrado.");
                return;
            }

            actualizarFichaDeLibro(libro);
            libroService.actualizar(libro);

            System.out.println("Ficha actualizada correctamente.");

        } catch (Exception e) {
            System.err.println("Error al actualizar ficha del libro: " + e.getMessage());
        }
    }

    // ============================================================
    // 10. ELIMINAR FICHA DEL LIBRO (SEGURO)
    // ============================================================
    public void eliminarFichaDeLibro() {
        try {
            System.out.print("ID del libro: ");
            int libroId = Integer.parseInt(scanner.nextLine());

            Libro libro = libroService.getById(libroId);
            if (libro == null) {
                System.out.println("Libro no encontrado.");
                return;
            }

            FichaBibliografica ficha = libro.getFichaBibliografica();
            if (ficha == null) {
                System.out.println("El libro no tiene ficha asociada.");
                return;
            }

            libro.setFichaBibliografica(null);
            libroService.actualizar(libro);

            libroService.getFichaService().eliminar(ficha.getId());
            System.out.println("Ficha eliminada y libro actualizado.");

        } catch (Exception e) {
            System.err.println("Error al eliminar ficha de libro: " + e.getMessage());
        }
    }

    // ============================================================
    // MÉTODOS PRIVADOS
    // ============================================================
    private FichaBibliografica crearFicha() {
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine().trim();

        System.out.print("Dewey: ");
        String dewey = scanner.nextLine().trim();

        System.out.print("Estantería: ");
        String est = scanner.nextLine().trim();

        System.out.print("Idioma: ");
        String idioma = scanner.nextLine().trim();

        return new FichaBibliografica(0, false, isbn, dewey, est, idioma);
    }

    private void actualizarFichaDeLibro(Libro libro) throws Exception {
        FichaBibliografica ficha = libro.getFichaBibliografica();
        FichaBibliograficaServiceImpl fichaService = libroService.getFichaService();

        if (ficha != null) {
            System.out.print("Actualizar ficha existente (ID " + ficha.getId() + ")? (s/n): ");
            if (!scanner.nextLine().equalsIgnoreCase("s")) return;

            System.out.print("ISBN (" + ficha.getIsbn() + "): ");
            String tx = scanner.nextLine().trim();
            if (!tx.isEmpty()) ficha.setIsbn(tx);

            System.out.print("Dewey (" + ficha.getClasificacionDewey() + "): ");
            tx = scanner.nextLine().trim();
            if (!tx.isEmpty()) ficha.setClasificacionDewey(tx);

            System.out.print("Estantería (" + ficha.getEstanteria() + "): ");
            tx = scanner.nextLine().trim();
            if (!tx.isEmpty()) ficha.setEstanteria(tx);

            System.out.print("Idioma (" + ficha.getIdioma() + "): ");
            tx = scanner.nextLine().trim();
            if (!tx.isEmpty()) ficha.setIdioma(tx);

            fichaService.actualizar(ficha);
        } else {
            System.out.print("El libro no tiene ficha. ¿Crear una? (s/n): ");
            if (scanner.nextLine().equalsIgnoreCase("s")) {
                FichaBibliografica nueva = crearFicha();
                fichaService.insertar(nueva);
                libro.setFichaBibliografica(nueva);
            }
        }
    }
}
