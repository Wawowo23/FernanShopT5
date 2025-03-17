package modelos;

import java.time.LocalDate;
import java.util.ArrayList;

public class PedidoClienteDataClass {
    private int idPedido;
    private int idCliente;
    private String nombreCliente;
    private String provincia;
    private String localidad;
    private String direccion;
    private int telefono;
    private LocalDate fechaPedido;
    private LocalDate fechaEntregaEstimada;
    private int estado;
    private String comentario;
    private ArrayList<Producto> productos;

    // Constructor


    public PedidoClienteDataClass(int idPedido, int idCliente, String nombreCliente, String provincia, String localidad, String direccion, int telefono, LocalDate fechaPedido, LocalDate fechaEntregaEstimada, int estado, String comentario, ArrayList<Producto> productos) {

        this.idPedido = idPedido;
        this.idCliente = idCliente;
        this.nombreCliente = nombreCliente;
        this.provincia = provincia;
        this.localidad = localidad;
        this.direccion = direccion;
        this.telefono = telefono;
        this.fechaPedido = fechaPedido;
        this.fechaEntregaEstimada = fechaEntregaEstimada;
        this.estado = estado;
        this.comentario = comentario;
        this.productos = productos;
    }

    // Getters y Setters

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
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

    @Override
    public String toString() {
        return "PedidoClienteDataClass{" +
                "idPedido=" + idPedido +
                ", idCliente=" + idCliente +
                ", nombreCliente='" + nombreCliente + '\'' +
                ", provincia='" + provincia + '\'' +
                ", localidad='" + localidad + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono=" + telefono +
                ", fechaPedido=" + fechaPedido +
                ", fechaEntregaEstimada=" + fechaEntregaEstimada +
                ", estado=" + estado +
                ", comentario='" + comentario + '\'' +
                ", productos=" + productos +
                '}';
    }
}
