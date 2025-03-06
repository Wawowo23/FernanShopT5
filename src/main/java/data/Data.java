package data;

public class Data {
    public static String getEstadoPedido(int estado) {
        return switch (estado) {
            case 0 -> "Procesando";
            case 1 -> "En preparación";
            case 2 -> "Enviado";
            case 3 -> "Entregado";
            case 4 -> "Cancelado";

            default -> "-1";
        };
    }

    public static float IVA () {
        return 0.16f;
    }
}
