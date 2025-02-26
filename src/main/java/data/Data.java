package data;

public class Data {
    public static String getEstadoPedido(int estado) {
        return switch (estado) {
            case 0 -> "Procesando";
            case 1 -> "Enviado";
            case 2 -> "Retrasado";
            case 3 -> "Cancelado";
            case 4 -> "En reparto";
            case 5 -> "Entregado";
            case 6 -> "Completado";
            default -> "-1";
        };
    }

    public static float IVA () {
        return 0.16f;
    }
}
