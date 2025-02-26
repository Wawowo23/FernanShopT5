package vistas;

import data.DataProductos;
import modelos.Pedido;
import modelos.Producto;

import java.util.ArrayList;

public class mainPrueba {
    public static void main(String[] args) {
        Pedido pedido1 = new Pedido(1);


        System.out.println(pedido1);

        pedido1.setProductos(generaProductos());

        System.out.println(pedido1.getProductos().getFirst());

        Pedido pedido2 = new Pedido(pedido1);

        pedido1.getProductos().clear();

        System.out.println(pedido2);
        System.out.println(pedido1);


        System.out.println(pedido1.numeroArticulos());
        System.out.println(pedido2.numeroArticulos());
    }

    private static ArrayList <Producto> generaProductos() {
        ArrayList <Producto> p = DataProductos.getProductosMock();
        return p;
    }
}
