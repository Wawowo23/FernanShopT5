package utils;

import java.util.Scanner;

public class Utils {
    // Metodo que se encarga de limpiar la pantalla de todos los textos que contenga
    public static void limpiaPantalla () {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    // Metodo que se encarga de mostrar un mensaje que pide que se presione una tecla para continuar
    public static void pulsaParaContinuar () {
        var s = new Scanner(System.in);
        System.out.println("Pulse para continuar...");
        s.nextLine();
    }

    // Metodo que se encarga de mostrar un mensaje de cerrado de sesión
    public static void cargando() {
        for (int i = 3; i >= 0; i--) {
            System.out.print(".");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
        System.out.println();
    }


}
