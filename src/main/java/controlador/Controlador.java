package controlador;

import data.Data;
import data.DataProductos;
import modelos.Admin;
import modelos.Cliente;
import modelos.Producto;
import modelos.Trabajador;

import java.util.ArrayList;

public class Controlador {
    // Atributos
    private ArrayList<Cliente> clientes;
    private ArrayList<Trabajador> trabajadores;
    private ArrayList<Admin> admins;
    private ArrayList<Producto> catalogo;

    // Constructor

    public Controlador() {
        clientes = null;
        trabajadores = null;
        admins = null;
        catalogo = DataProductos.getProductosMock();
    }

    // Getters y Setters


    public ArrayList<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(ArrayList<Cliente> clientes) {
        this.clientes = clientes;
    }

    public ArrayList<Trabajador> getTrabajadores() {
        return trabajadores;
    }

    public void setTrabajadores(ArrayList<Trabajador> trabajadores) {
        this.trabajadores = trabajadores;
    }

    public ArrayList<Admin> getAdmins() {
        return admins;
    }

    public void setAdmins(ArrayList<Admin> admins) {
        this.admins = admins;
    }

    public ArrayList<Producto> getCatalogo() {
        return catalogo;
    }

    public void setCatalogo(ArrayList<Producto> catalogo) {
        this.catalogo = catalogo;
    }

    // Otros metodos

    public Object login (String email, String clave) {
        for (Admin admin : admins) {
            if (admin.login(email,clave)) return admin;
        }
        for (Trabajador trabajador : trabajadores) {
            if (trabajador.login(email,clave)) return trabajador;
        }
        for (Cliente cliente : clientes) {
            if (cliente.login(email,clave)) return cliente;
        }
        return null;
    }

    // Metodo que genera automáticamente el id de un cliente
    private int generaIdCliente () {
        return (int) (Math.random() * 1000000) + 2000000;
    }

    // Metodo que genera automáticamente el id de un trabajador
    private int generaIdTrabajador () {
        return (int) (Math.random() * 1000000) + 3000000;
    }

    // Metodo que genera automáticamente el id de un admin
    private int generaIdAdmin () {
        return (int) (Math.random() * 1000000) + 4000000;
    }


    // Metodo que genera automáticamente el id de un pedido
    private int generaIdPedido () {
        return (int) (Math.random() * 1000000) + 60000000;
    }


}
