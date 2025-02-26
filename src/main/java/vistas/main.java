package vistas;

import controlador.Controlador;
import modelos.Admin;
import modelos.Cliente;
import modelos.Producto;
import modelos.Trabajador;
import utils.Utils;

import java.util.ArrayList;
import java.util.Scanner;

public class main {
    public static final Scanner S = new Scanner(System.in);
    public static void main(String[] args) {
        Controlador controlador = new Controlador();
        do {
            mensajeInicio();
            Object user = menuInicio(controlador);
            if (user != null) {
                menuUsuario(controlador,user);
            }
        } while (true);
    }

    private static void menuUsuario(Controlador controlador, Object user) {
        if (user instanceof Cliente clienteTemp) {
            menuCliente(controlador,clienteTemp);
        }
        if (user instanceof Trabajador trabajadorTemp) {
            menuTrabajador(controlador,trabajadorTemp);
        }
        if (user instanceof Admin adminTemp) {
            menuAdmin(controlador,adminTemp);
        }
    }

    private static void menuAdmin(Controlador controlador, Admin adminTemp) {

    }

    private static void menuTrabajador(Controlador controlador, Trabajador trabajadorTemp) {
    }

    private static void menuCliente(Controlador controlador, Cliente clienteTemp) {

    }

    private static Object menuInicio(Controlador controlador) {
        int op = 0;
        System.out.print("""
                \t\tBienvenido a Fernanshop
                =======================================
                1.- Ver Catálogo
                2.- Registro
                3.- Inicio de Sesión
                
                Marque su opción:\s""");
        try {
            op = Integer.parseInt(S.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ERROR. Formato introducido incorrecto");
        }
        switch (op) {
            case 1:
                pintaCatalogo(controlador);
                break;
            case 2:
                registroCliente(controlador);
                break;
            case 3:
                return inicioSesion(controlador);

        }
        return null;
    }

    private static void registroCliente(Controlador controlador) {
        int telefono = 0;
        String email = "";
        System.out.println("Bienvenido al menú de registro");
        do {
            System.out.print("Introduzca su email: ");
            email = S.nextLine();
            if (!email.contains("@")) System.out.println("El correo introducido no es válido");
        } while (!email.contains("@"));
        System.out.print("Introduzca su contraseña: ");
        String clave = S.nextLine();
        System.out.print("Introduzca su nombre: ");
        String nombre = S.nextLine();
        System.out.print("Introduzca su localidad: ");
        String localidad = S.nextLine();
        System.out.print("Introduzca su provincia: ");
        String provincia = S.nextLine();
        System.out.print("Introduzca su dirección: ");
        String direccion = S.nextLine();
        do {
            System.out.print("Introduzca su número de teléfono: ");
            try {
                telefono = Integer.parseInt(S.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("ERROR. Formato erróneo.");
            }
        } while (telefono > 999999999 || telefono < 100000000);

        if (controlador.registraCliente(email,clave,nombre,localidad,provincia,direccion,telefono))
            System.out.println("Se ha registrado correctamente");
        else System.out.println("Ha ocurrido un problema a la hora de registrarse");
        Utils.limpiaPantalla();
    }

    private static Object inicioSesion(Controlador controlador) {
        System.out.print("Introduzca su email: ");
        String email = S.nextLine();
        System.out.println("Introduzca su contraseña: ");
        String clave = S.nextLine();
        Utils.limpiaPantalla();
        return controlador.login(email,clave);
    }

    private static void pintaCatalogo(Controlador controlador) {
        String op = "";
        ArrayList<Producto> productosParaPintar = new ArrayList<>();
        productosParaPintar.addAll(controlador.getCatalogo());
        int cantidad = controlador.getCatalogo().size();
        for (int i = 0; i < cantidad; i++) {
            Producto temp = productosParaPintar.get((int) (Math.random() * productosParaPintar.size()));
            pintaProductoSinRegistro(temp);
            productosParaPintar.remove(temp);

            if ((i+1) % 5 == 0) {
                op = preguntaSiNo();
                if (op.equalsIgnoreCase("N")) {
                    System.out.println("Volviendo al menú de inicio");
                    Utils.cargando();
                    i = controlador.getCatalogo().size();
                }
            }
        }
        Utils.pulsaParaContinuar();
        Utils.limpiaPantalla();
    }

    private static void pintaProductoSinRegistro(Producto producto) {

        System.out.println("===================================");
        System.out.println("\t" + producto.getMarca() +
                ((producto.getRelevancia() > 9) ? " ⭐":"") );
        System.out.println("\t" + producto.getModelo());
        System.out.println("\t" + producto.getDescripcion());
        System.out.println("\t" + producto.getPrecio());
        System.out.println("===================================");
        System.out.println();

    }

    private static String preguntaSiNo() {
        String op;
        do {
            System.out.println("¿Quieres seguir viendo el catálogo?(S/N)");
            op = S.nextLine();
        } while (!op.equalsIgnoreCase("S") && !op.equalsIgnoreCase("N"));
        return op;
    }

    private static void mensajeInicio() {
        System.out.println("""
                ██████╗ ██╗███████╗███╗   ██╗██╗   ██╗███████╗███╗   ██╗██╗██████╗  ██████╗      █████╗     ███████╗███████╗██████╗ ███╗   ██╗ █████╗ ███╗   ██╗███████╗██╗  ██╗ ██████╗ ██████╗\s
                ██╔══██╗██║██╔════╝████╗  ██║██║   ██║██╔════╝████╗  ██║██║██╔══██╗██╔═══██╗    ██╔══██╗    ██╔════╝██╔════╝██╔══██╗████╗  ██║██╔══██╗████╗  ██║██╔════╝██║  ██║██╔═══██╗██╔══██╗
                ██████╔╝██║█████╗  ██╔██╗ ██║██║   ██║█████╗  ██╔██╗ ██║██║██║  ██║██║   ██║    ███████║    █████╗  █████╗  ██████╔╝██╔██╗ ██║███████║██╔██╗ ██║███████╗███████║██║   ██║██████╔╝
                ██╔══██╗██║██╔══╝  ██║╚██╗██║╚██╗ ██╔╝██╔══╝  ██║╚██╗██║██║██║  ██║██║   ██║    ██╔══██║    ██╔══╝  ██╔══╝  ██╔══██╗██║╚██╗██║██╔══██║██║╚██╗██║╚════██║██╔══██║██║   ██║██╔═══╝\s
                ██████╔╝██║███████╗██║ ╚████║ ╚████╔╝ ███████╗██║ ╚████║██║██████╔╝╚██████╔╝    ██║  ██║    ██║     ███████╗██║  ██║██║ ╚████║██║  ██║██║ ╚████║███████║██║  ██║╚██████╔╝██║    \s
                ╚═════╝ ╚═╝╚══════╝╚═╝  ╚═══╝  ╚═══╝  ╚══════╝╚═╝  ╚═══╝╚═╝╚═════╝  ╚═════╝     ╚═╝  ╚═╝    ╚═╝     ╚══════╝╚═╝  ╚═╝╚═╝  ╚═══╝╚═╝  ╚═╝╚═╝  ╚═══╝╚══════╝╚═╝  ╚═╝ ╚═════╝ ╚═╝    \s
                """);
    }
}
