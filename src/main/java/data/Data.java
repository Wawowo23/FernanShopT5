package data;

public class Data {
    public static String getEstadoPedido(int estado) {
        return switch (estado) {
            case 0 -> "Procesando";
            case 1 -> "En preparación";
            case 2 -> "Enviado";
            case 3 -> "Retrasado";
            case 4 -> "Cancelado";
            case 5 -> "En reparto";
            case 6 -> "Entregado";
            case 7 -> "Completado";
            default -> "-1";
        };
    }

    public static float IVA () {
        return 0.16f;
    }
}
