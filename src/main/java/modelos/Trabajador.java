package modelos;

import java.util.ArrayList;

public class Trabajador {
    // Atributos
    private int id;
    private String nombre;
    private String pass;
    private String email;
    private int movil;
    private ArrayList<Pedido> pedidosAsignados;

    // Constructor

    public Trabajador(int id, String nombre, String pass, String email, int movil) {
        this.id = id;
        this.nombre = nombre;
        this.pass = pass;
        this.email = email;
        this.movil = movil;
        pedidosAsignados = null;
    }

    // Constructor copia

    public Trabajador(Trabajador trabajador) {
        id = trabajador.id;
        nombre = trabajador.nombre;
        pass = trabajador.pass;
        email = trabajador.email;
        movil = trabajador.movil;
        pedidosAsignados = trabajador.pedidosAsignados;
    }

    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getMovil() {
        return movil;
    }

    public void setMovil(int movil) {
        this.movil = movil;
    }

    public ArrayList<Pedido> getPedidosAsignados() {
        return pedidosAsignados;
    }

    public void setPedidosAsignados(ArrayList<Pedido> pedidosAsignados) {
        this.pedidosAsignados = pedidosAsignados;
    }

    // Otros metodos

    // Metodo que comprueba si el correo y contraseña introducidas por un usuario corresponden a las de un trabajador
    public boolean login (String email, String pass) {
        return this.email.equals(email) && this.pass.equals(pass);
    }

    // Metodo que busca un pedido que todavía no ha sido completado entre los pedidos asignados de un trabajador
    public Pedido buscaPedidoAsignadoPendiente (int idPedido) {
        for (Pedido p : getPedidosPendientes()) {
            if (p != null && p.getId() == idPedido) return p;
        }
        return null;
    }

    // Metodo que busca un pedido que ha sido completado entre los pedidos asignados de un trabajador
    public Pedido buscaPedidoAsignadoCompleto (int idPedido) {
        for (Pedido p : getPedidosCompletos()) {
            if (p != null && p.getId() == idPedido) return p;
        }
        return null;
    }

    // Metodo que añade un pedido a la lista de pedidos asignados de un trabajador
    public boolean asignaPedido (Pedido pedido) {
        if (pedido == null) return false;
        for (Pedido p : pedidosAsignados) {
            if (p.getId() == pedido.getId()) return false;
        }
        pedidosAsignados.add(pedido);
        return true;
    }

    // Metodo que devuelve un array de todos los pedidos pendientes que tiene un trabajador
    public ArrayList<Pedido> getPedidosPendientes () {
        ArrayList<Pedido> pedidosPendientes = new ArrayList<>();
        for (Pedido p : pedidosAsignados) {
            if (p.getEstado() != 6) pedidosPendientes.add(p);
        }
        return pedidosPendientes;
    }

    // Metodo que devuelve un array de todos los pedidos completos que tiene un trabajador
    public ArrayList<Pedido> getPedidosCompletos () {
        ArrayList<Pedido> pedidosCompletos = new ArrayList<>();
        for (Pedido p : pedidosAsignados) {
            if (p.getEstado() == 6) pedidosCompletos.add(p);
        }
        return pedidosCompletos;
    }

    // Metodo que devuelve la cantidad de pedidos pendientes que tiene un trabajador
    public int numPedidosPendientes() {
        return getPedidosPendientes().size();
    }

    @Override
    public String toString() {
        return "Trabajador{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", pass='" + pass + '\'' +
                ", email='" + email + '\'' +
                ", movil=" + movil +
                ", pedidosAsignados=" + pedidosAsignados +
                '}';
    }
}
