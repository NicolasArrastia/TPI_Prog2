package tpi_prog2.main;

import java.util.Scanner;
import tpi_prog2.dao.LibroDAO;
import tpi_prog2.dao.FichaBibliograficaDAO;
import tpi_prog2.service.LibroServiceImpl;
import tpi_prog2.service.FichaBibliograficaServiceImpl;

public class AppMenu {

    private final Scanner scanner;
    private final MenuHandler menuHandler;
    private boolean running;

    public AppMenu() {
        this.scanner = new Scanner(System.in);

        LibroServiceImpl libroService = createLibroService();

        this.menuHandler = new MenuHandler(scanner, libroService);

        this.running = true;
    }

    public static void main(String[] args) {
        AppMenu app = new AppMenu();
        app.run();
    }

    public void run() {
        while (running) {
            try {
                MenuDisplay.mostrarMenuPrincipal();
                int opcion = Integer.parseInt(scanner.nextLine());
                processOption(opcion);
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Por favor, ingrese un numero.");
            }
        }
        scanner.close();
    }

    private void processOption(int opcion) {
        switch (opcion) {
            case 1 -> menuHandler.crearLibro();
            case 2 -> menuHandler.listarLibros();
            case 3 -> menuHandler.actualizarLibro();
            case 4 -> menuHandler.eliminarLibro();

            case 5 -> menuHandler.crearFichaIndependiente();
            case 6 -> menuHandler.listarFichas();
            case 7 -> menuHandler.actualizarFichaPorId();
            case 8 -> menuHandler.eliminarFichaPorId();

            case 9 -> menuHandler.actualizarFichaDeLibro();
            case 10 -> menuHandler.eliminarFichaDeLibro();

            case 0 -> {
                System.out.println("Saliendo...");
                running = false;
            }
            default -> System.out.println("Opcion no valida.");
        }
    }

    private LibroServiceImpl createLibroService() {
        FichaBibliograficaDAO fichaDAO = new FichaBibliograficaDAO();
        LibroDAO libroDAO = new LibroDAO();

        FichaBibliograficaServiceImpl fichaService = new FichaBibliograficaServiceImpl(fichaDAO);
        return new LibroServiceImpl(libroDAO, fichaService);
    }
}
