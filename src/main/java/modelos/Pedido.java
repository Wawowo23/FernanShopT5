package modelos;

import data.Data;

import java.time.LocalDate;
import java.util.ArrayList;

public class Pedido implements Comparable<Pedido>{
    // Atributos
    private int id;
    private LocalDate fechaPedido;
    private LocalDate fechaEntregaEstimada;
    private LocalDate deliveryDate;
    private int estado;
    private String comentario;
    private ArrayList<Producto> productos;

    // Constructor

    public Pedido(int id) {
        this.id = id;
        fechaPedido = LocalDate.now();
        fechaEntregaEstimada = fechaPedido.plusDays(5);
        deliveryDate = null;
        estado = 0;
        comentario = "";
        productos = new ArrayList<>();
    }

    // Constructor copia

    public Pedido (Pedido pedido) {
        id = pedido.id;
        fechaPedido = pedido.fechaPedido;
        fechaEntregaEstimada = pedido.fechaEntregaEstimada;
        deliveryDate = pedido.deliveryDate;
        estado = pedido.estado;
        comentario = pedido.comentario;
        productos = new ArrayList<>(pedido.productos);
    }

    // Getters y Setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDate fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public LocalDate getFechaEntregaEstimada() {
        return fechaEntregaEstimada;
    }

    public void setFechaEntregaEstimada(LocalDate fechaEntregaEstimada) {
        this.fechaEntregaEstimada = fechaEntregaEstimada;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public ArrayList<Producto> getProductos() {
        return productos;
    }

    public void setProductos(ArrayList<Producto> productos) {
        this.productos = productos;
    }

    // Otros metodos

    // Metodo que cambia el estado de un paquete
    public boolean cambiaEstado (int nuevoEstado) {
        if (nuevoEstado < 0 || nuevoEstado > 4) return false;
        setEstado(nuevoEstado);
        return true;
    }

    // Metodo que cambia la fecha estimada de entrega de un paquete
    public boolean cambiaFechaEntrega (LocalDate nuevaFecha) {
        if (nuevaFecha.isBefore(fechaPedido)) return false;
        setFechaEntregaEstimada(nuevaFecha);
        return true;
    }

    // Metodo que calcula el precio total de un pedido sin IVA
    public float calculaPrecioSinIVA () {
        float precioTotal = 0;
        for (Producto producto : productos) {
            if (producto != null) precioTotal += producto.getPrecio();
        }
        return precioTotal;
    }

    // Metodo que calcula el IVA de un pedido
    public float calculaIVAPedido() {
        return calculaPrecioSinIVA() * Data.IVA();
    }

    // Metodo que calcula el precio total de un pedido incluyendo el IVA.
    public float calculaPrecioPedidoConIVA() {
        return calculaIVAPedido() + calculaPrecioSinIVA();
    }

    // Metodo que calcula el número de articulos que tiene un pedido
    public int numeroArticulos() {
        return productos.size();
    }

    // Metodo que busca un producto dentro de un pedido según la Id pasada por los parámetros
    public Producto buscaProducto (int idProducto) {
        for (Producto p : productos) {
            if (p.getId() == idProducto) return p;
        }
        return null;
    }

    // Metodo que añade un producto a un pedido
    public void addProducto(Producto producto) {
        productos.add(producto);
    }

    // Metodo que calcula el precio total de un carro sin iva
    public float precioPedidoSinIVA () {
        float precio = 0;
        for (Producto p : productos) {
            precio += p.getPrecio();
        }
        return precio;
    }

    // Metodo que calcula el precio del iva de un carro
    public float precioIVAPedido () {
        return (float) (precioPedidoSinIVA() * Data.IVA());
    }

    // Metodo que calcula el precio de un carro con el IVA incluido
    public float precioPedidoConIVA () {
        return precioPedidoSinIVA() + precioIVAPedido();
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", fechaPedido=" + fechaPedido +
                ", fechaEntregaEstimada=" + fechaEntregaEstimada +
                ", deliveryDate=" + deliveryDate +
                ", estado=" + estado + " " + Data.getEstadoPedido(estado) +
                ", comentario='" + comentario + '\'' +
                ", productos=" + productos +
                '}';
    }

    @Override
    public int compareTo(Pedido pedido) {
        return this.fechaPedido.compareTo(pedido.getFechaPedido());
    }

}
