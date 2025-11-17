package tpi_prog2.main;

/**
 * Clase utilitaria para mostrar el menú de la aplicación.
 * Solo contiene métodos estáticos de visualización.
 *
 * Responsabilidades:
 * - Mostrar el menú principal con todas las opciones disponibles
 * - Formato de salida consistente
 *
 * IMPORTANTE: Esta clase NO lee entrada del usuario.
 * Solo imprime el menú; AppMenu procesa la opción elegida.
 */
public class MenuDisplay {

    /**
     * Muestra el menú principal de Libros y Fichas Bibliográficas.
     *
     * Opciones de Libros (1-4):
     * 1. Crear libro
     * 2. Listar libros (todos o búsqueda por título/autor)
     * 3. Actualizar libro (incluye gestión de ficha bibliográfica)
     * 4. Eliminar libro (soft delete, no elimina ficha asociada)
     *
     * Opciones de Fichas Bibliográficas (5-10):
     * 5. Crear ficha bibliográfica independiente
     * 6. Listar fichas bibliográficas
     * 7. Actualizar ficha bibliográfica por ID
     * 8. Eliminar ficha bibliográfica por ID (PELIGROSO)
     * 9. Actualizar ficha bibliográfica por ID de libro
     * 10. Eliminar ficha bibliográfica por ID de libro (SEGURO)
     *
     * Opción de salida:
     * 0. Salir
     */
    public static void mostrarMenuPrincipal() {
        System.out.println("\n========= MENU =========");
        System.out.println("1. Crear libro");
        System.out.println("2. Listar libros");
        System.out.println("3. Actualizar libro");
        System.out.println("4. Eliminar libro");
        System.out.println("5. Crear ficha bibliográfica");
        System.out.println("6. Listar fichas bibliográficas");
        System.out.println("7. Actualizar ficha bibliográfica por ID");
        System.out.println("8. Eliminar ficha bibliográfica por ID");
        System.out.println("9. Actualizar ficha bibliográfica por ID de libro");
        System.out.println("10. Eliminar ficha bibliográfica por ID de libro");
        System.out.println("0. Salir");
        System.out.print("Ingrese una opción: ");
    }
}
