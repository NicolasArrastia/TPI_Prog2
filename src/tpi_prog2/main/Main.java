package tpi_prog2.main;

/**
 * Punto de entrada alternativo de la aplicación.
 * Clase simple que delega inmediatamente a AppMenu.
 *
 * Responsabilidad:
 * - Proporcionar un punto de entrada main() estándar
 * - Delegar la ejecución a AppMenu
 *
 * Diferencia con AppMenu.main():
 * - AppMenu.main(): Punto de entrada primario
 * - Main.main(): Punto de entrada alternativo (mismo comportamiento)
 *
 * Ambos métodos hacen exactamente lo mismo:
 * 1. Crean instancia de AppMenu
 * 2. Ejecutan app.run() para iniciar el menú
 *
 * Motivo de mantener dos entradas:
 * - Compatibilidad con IDEs que buscan una clase llamada "Main"
 * - Claridad: AppMenu es el controlador principal de la aplicación
 * - Flexibilidad para ejecutar desde JAR especificando cualquiera de los dos
 */
public class Main {

    /**
     * Punto de entrada alternativo de la aplicación Java.
     * Instancia AppMenu y ejecuta el menú principal.
     *
     * @param args Argumentos de línea de comandos (no usados)
     */
    public static void main(String[] args) {
        AppMenu app = new AppMenu();
        app.run();
    }
}
