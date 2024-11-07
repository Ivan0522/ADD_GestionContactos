import java.util.Scanner;

public class Menu {

    public static void main(String[] args) {

        GestionContactos gestionContactos = new GestionContactos();
        Scanner sc = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n>> Gestión de Contactos <<");
            System.out.println("1. Agregar contacto.");
            System.out.println("2. Buscar contacto.");
            System.out.println("3. Modificar contacto.");
            System.out.println("4. Eliminar contacto.");
            System.out.println("5. Exportar contactos a CSV.");
            System.out.println("6. Salir.");
            System.out.print("Seleccione una opción: ");
            opcion = sc.nextInt();
            sc.nextLine(); // Consumir línea

            switch (opcion) {
                case 1:
                    System.out.print(">> Ingrese el nombre: ");
                    String nombre = sc.nextLine();
                    System.out.print(">> Ingrese el teléfono: ");
                    String telefono = sc.nextLine();
                    System.out.print(">> Ingrese la dirección: ");
                    String direccion = sc.nextLine();
                    // Se crea el Contacto y se usa el metodo de Agregar
                    Contacto contacto = new Contacto(nombre, telefono, direccion);
                    gestionContactos.agregarContacto(contacto);
                    break;
                case 2:
                    System.out.print("Ingrese el nombre del contacto a buscar: ");
                    String nombreBuscado = sc.nextLine();
                    // Metodo de Buscar
                    gestionContactos.buscarContacto(nombreBuscado);
                    break;
                case 3:
                    System.out.print("Ingrese el nombre del contacto a modificar: ");
                    String nombreModificar = sc.nextLine();
                    System.out.print("Ingrese el nuevo nombre: ");
                    String nuevoNombre = sc.nextLine();
                    System.out.print("Ingrese el nuevo teléfono: ");
                    String nuevoTelefono = sc.nextLine();
                    System.out.print("Ingrese la nueva dirección: ");
                    String nuevaDireccion = sc.nextLine();
                    // Se crea el NUEVO Contacto y se usa el metodo de Modificar
                    Contacto nuevosDatosContacto = new Contacto(nuevoNombre, nuevoTelefono, nuevaDireccion);
                    gestionContactos.modificarContacto(nombreModificar, nuevosDatosContacto);
                    break;
                case 4:
                    System.out.print("Ingrese el nombre del contacto a eliminar: ");
                    String nombreEliminar = sc.nextLine();
                    // Metodo de Eliminar
                    gestionContactos.eliminarContacto(nombreEliminar);
                    break;
                case 5:
                    gestionContactos.exportarContactosACSV();
                    break;
                case 6:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
                    break;
            }
        } while (opcion != 6);

        sc.close();
    }

}
