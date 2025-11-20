package tpi_prog2.main;


public class MenuDisplay {
    public static void mostrarMenuPrincipal() {
        System.out.println("\n-=-=-=-=-= LIBROS -=-=-=-=-=");
        System.out.println("1. Crear libro");
        System.out.println("2. Listar libros");
        System.out.println("3. Actualizar libro");
        System.out.println("4. Eliminar libro");
        System.out.println("\n-=-=-=-=-= FICHAS -=-=-=-=-=");
        System.out.println("5. Crear ficha bibliográfica");
        System.out.println("6. Listar fichas bibliográficas");
        System.out.println("7. Actualizar ficha bibliográfica por ID");
        System.out.println("8. Eliminar ficha bibliográfica por ID");
        System.out.println("\n-=-=-=-=-= OTROS -=-=-=-=-=");
        System.out.println("9. Actualizar ficha bibliográfica por ID de libro");
        System.out.println("10. Eliminar ficha bibliográfica por ID de libro");
        System.out.println("0. SALIR");
        System.out.print("Ingrese una opción: ");
    }
}
