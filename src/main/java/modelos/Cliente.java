package modelos;

import java.util.ArrayList;

public class Cliente {
    // Atributos
    private int id;
    private String email;
    private String clave;
    private String nombre;
    private String localidad;
    private String provincia;
    private String direccion;
    private int telefono;
    private boolean activado;
    private ArrayList<Pedido> pedidos;
    private ArrayList<Producto> carro;

    // Constructor

    public Cliente(int id, String email, String clave, String nombre, String localidad, String provincia, String direccion, int telefono) {
        this.id = id;
        this.email = email;
        this.clave = clave;
        this.nombre = nombre;
        this.localidad = localidad;
        this.provincia = provincia;
        this.direccion = direccion;
        this.telefono = telefono;
        activado = false;
        pedidos = new ArrayList<>();
        carro = new ArrayList<>();
    }

    // Constructor copia

    public Cliente (Cliente cliente) {
        id = cliente.id;
        email = cliente.email;
        clave = cliente.clave;
        nombre = cliente.nombre;
        localidad = cliente.localidad;
        provincia = cliente.provincia;
        direccion = cliente.direccion;
        telefono = cliente.telefono;
        activado = cliente.activado;
        pedidos = new ArrayList<>(cliente.pedidos);
        carro = new ArrayList<>(cliente.carro);
    }

    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
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

    public ArrayList<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(ArrayList<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public ArrayList<Producto> getCarro() {
        return carro;
    }

    public void setCarro(ArrayList<Producto> carro) {
        this.carro = carro;
    }

    public boolean isActivado() {
        return activado;
    }

    public void setActivado(boolean activado) {
        this.activado = activado;
    }

    // Otros métodos

    // Metodo que comprueba si el usuario y contraseña introducidas por un usuario corresponden a las de un cliente
    public boolean login (String email, String pass) {
        return this.email.equals(email) && clave.equals(pass);
    }

    // Metodo que añade un producto al carrito de un cliente
    public void addProductoCarro (Producto p) {
        carro.add(p);
    }

    // Metodo que quita un producto del carro de un cliente
    public boolean quitaProductoCarro (int idProducto) {
        for (Producto p : carro) {
            // Comprobamos si podemos encontrar el producto cuya id ha sido introducida
            if (p.getId() == idProducto) {
                carro.remove(p);
                return true;
            }
        }
        return false;
    }

    // Metodo que calcula la cantidad de producto que hay en el carro de un cliente
    public int numProductosCarro () {
        return carro.size();
    }

    // Metodo que vacia el carro completo de un cliente
    public void vaciaCarro () {
        carro.clear();
    }

    // Metodo que añade un pedido al perfil de un cliente
    public void addPedido(Pedido pedido) {
        pedidos.add(pedido);
    }

    // Metodo que calcula el precio total de un carro sin iva
    public float precioCarroSinIVA () {
        float precio = 0;
        for (Producto p : carro) {
            precio += p.getPrecio();
        }
        return precio;
    }

    // Metodo que calcula el precio del iva de un carro
    public float precioIVACarro (float iva) {
        return (float) (precioCarroSinIVA() * iva) / 100;
    }

    // Metodo que calcula el precio de un carro con el IVA incluido
    public float precioCarroConIVA (float iva) {
        return precioCarroSinIVA() + precioIVACarro(iva);
    }

    // Metodo que comprueba si existe un producto en un carro
    public boolean existeProductoEnCarro (int idProducto) {
        for (Producto p : carro) {
            if (p.getId() == idProducto) return true;
        }
        return false;
    }

    public ArrayList<Pedido> getPedidosPendientesEntrega () {
        ArrayList<Pedido> pedidosPendientes = new ArrayList<>();
        for (Pedido p : pedidos) {
            if (p.getEstado() != 3 && p.getEstado() != 4) pedidosPendientes.add(p);
        }
        return pedidosPendientes;
    }

    public ArrayList<Pedido> getPedidosCancelados () {
        ArrayList<Pedido> pedidosPendientes = new ArrayList<>();
        for (Pedido p : pedidos) {
            if (p.getEstado() == 4) pedidosPendientes.add(p);
        }
        return pedidosPendientes;
    }

    public ArrayList<Pedido> getPedidosEntregados () {
        ArrayList<Pedido> pedidosPendientes = new ArrayList<>();
        for (Pedido p : pedidos) {
            if (p.getEstado() == 3) pedidosPendientes.add(p);
        }
        return pedidosPendientes;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", clave='" + clave + '\'' +
                ", nombre='" + nombre + '\'' +
                ", localidad='" + localidad + '\'' +
                ", provincia='" + provincia + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono=" + telefono +
                ", pedidos=" + (pedidos.toString()) +
                ", carro=" + (carro.toString()) +
                '}';
    }



    // TODO no sé si estoy haciendo demasiados metodos nuevos
    public boolean cancelaPedido(int id) {
        for (Pedido p : pedidos) {
            if (p.getId() == id) {
                p.setEstado(4);
                return true;
            }
        }
        return false;
    }
}
