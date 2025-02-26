package vistas;

import controlador.Controlador;
import modelos.Producto;
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

        } while (true);
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
                registroUsuario(controlador);
                break;
            case 3:
                return inicioSesion(controlador);

        }
        return null;
    }

    private static void registroUsuario(Controlador controlador) {
    }

    private static Object inicioSesion(Controlador controlador) {
        System.out.print("Introduzca su email: ");
        String email = S.nextLine();
        System.out.println("Introduzca su contraseña: ");
        String clave = S.nextLine();
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
        System.out.println();
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
