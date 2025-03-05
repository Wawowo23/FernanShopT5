package controlador;

import data.Data;
import data.DataProductos;
import modelos.*;

import java.util.ArrayList;

public class Controlador {
    // Atributos
    private ArrayList<Cliente> clientes;
    private ArrayList<Trabajador> trabajadores;
    private ArrayList<Admin> admins;
    private ArrayList<Producto> catalogo;

    // Constructor

    public Controlador() {
        clientes = new ArrayList<>();
        trabajadores = new ArrayList<>();
        admins = new ArrayList<>();
        catalogo = DataProductos.getProductosMock();
        mock();
    }

    private void mock() {
        clientes.add(new Cliente(generaIdCliente(),"a@","1234","amai","el villar",
                "jaen","calle calle",123456789));
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

    // Metodo que devuelve un objeto dependiendo de el email y la contraseña que sean introducidos
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

    // Metodo que añade un producto al carrito de un cliente
    public boolean addProductoCarrito(Cliente cliente, int idProducto) {
        for (Cliente c : clientes) {
            if (c.getId() == cliente.getId())
                for (Producto p : DataProductos.getProductosMock()) {
                    if (p.getId() == idProducto) return c.getCarro().add(p);
                }
        }
        return false;
    }

    // Metodo que devuelve un producto según el id introducido
    public Producto buscaProductoById (int id) {
        for (Producto p : catalogo)
            if (p.getId() == id) return p;
        return null;
    }

    // TODO preguntar a Carlos que hace este método
    // TODO confirma el carrito de un cliente y lo vuelve un pedido
    public boolean confirmaPedidoCliente (int idCliente) {
        for (Cliente c : clientes) {
            if (c.getId() == idCliente) {
                Pedido pedidoAgregado = new Pedido(generaIdPedido());
                pedidoAgregado.getProductos().addAll(c.getCarro());
                c.vaciaCarro();
                return true;
            }
        }
        return false;
    }

    // TODO intentar que sea en un mismo bucle
    public Trabajador buscaTrabajadorCandidatoParaAsignar() {
        int numPedidosMenor = Integer.MAX_VALUE;
        int contadorTrabajadoresIguales = 0;
        Trabajador trabajadorElegido = null;
        for (Trabajador t : trabajadores) {
            if (t.getPedidosPendientes().size() < numPedidosMenor) {
                numPedidosMenor = t.getPedidosPendientes().size();
                trabajadorElegido  = t;
            }
        }
        /*for (Trabajador t : trabajadores) {
            if (t.getPedidosPendientes().size() == numPedidosMenor) {
                trabajadorElegido = t;
                contadorTrabajadoresIguales++;
            }
        }*/
        if (hayEmpateTrabajadoresCandidatos(trabajadorElegido)) return null;
        return trabajadorElegido;
    }

    private boolean hayEmpateTrabajadoresCandidatos(Trabajador trabajadorElegido) {
        for (Trabajador t : trabajadores) {
            if (t.getPedidosPendientes().size() == trabajadorElegido.getPedidosPendientes().size()) return true;
        }
        return false;
    }

    public boolean registraCliente(String email, String clave, String nombre, String localidad, String provincia, String direccion, int telefono) {
        int id;
        do {
            id = generaIdCliente();
        } while (buscaClienteById(id) != null);
        return clientes.add(new Cliente(id,email,clave,nombre,localidad,provincia,direccion,telefono));
    }

    private Cliente buscaClienteById(int id) {
        for (Cliente c : clientes) {
            if (c.getId() == id) return c;
        }
        return null;
    }

    public ArrayList<Producto> buscaProductosByMarca(String marca) {
        ArrayList<Producto> productosEncontrados = new ArrayList<>();
        for (Producto p : catalogo)  {
            if (p.getMarca().toLowerCase().contains(marca.toLowerCase()))
                productosEncontrados.add(p);
        }
        return productosEncontrados;
    }
    public ArrayList<Producto> buscaProductosByModelo(String modelo) {
        ArrayList<Producto> productosEncontrados = new ArrayList<>();
        for (Producto p : catalogo)  {
            if (p.getModelo().toLowerCase().contains(modelo.toLowerCase()))
                productosEncontrados.add(p);
        }
        return productosEncontrados;
    }
    public ArrayList<Producto> buscaProductosByDescripcion(String descripcion) {
        ArrayList<Producto> productosEncontrados = new ArrayList<>();
        for (Producto p : catalogo)  {
            if (p.getDescripcion().toLowerCase().contains(descripcion.toLowerCase()))
                productosEncontrados.add(p);
        }
        return productosEncontrados;
    }
    public ArrayList<Producto> buscaProductosByTermino(String termino) {
        ArrayList <Producto> resultadosMarca = buscaProductosByMarca(termino);
        ArrayList <Producto> resultadosModelo = buscaProductosByModelo(termino);
        ArrayList <Producto> resultadosDescripcion = buscaProductosByDescripcion(termino);
        ArrayList<Producto> productosEncontrados = new ArrayList<>();

        for (Producto p : resultadosMarca) {
            if (!existeProductoEnLista(productosEncontrados,p)) productosEncontrados.add(p);
        }
        for (Producto p : resultadosModelo) {
            if (!existeProductoEnLista(productosEncontrados,p)) productosEncontrados.add(p);
        }
        for (Producto p : resultadosDescripcion) {
            if (!existeProductoEnLista(productosEncontrados,p)) productosEncontrados.add(p);
        }

        return productosEncontrados;
    }

    public ArrayList<Producto> buscaProductoByPrecio(float precioMinimo, float precioMaximo) {
        ArrayList<Producto> resultados = new ArrayList<>();
        for (Producto p : catalogo) {
            if (p.getPrecio() <= precioMaximo && p.getPrecio() >= precioMinimo) resultados.add(p);
        }
        return resultados;
    }

    public ArrayList<Pedido> getTodosPedidos() {
        ArrayList<Pedido> todosPedidos = new ArrayList<>();
        for (Cliente c : clientes) {
            for (Pedido p : c.getPedidos()) {
                if (!existePedidoEnLista(todosPedidos,p)) todosPedidos.add(p);
            }
        }
        return todosPedidos;
    }

    private boolean existePedidoEnLista(ArrayList<Pedido> todosPedidos, Pedido p) {
        for (Pedido pedido : todosPedidos) {
            if (pedido.getId() == p.getId()) return true;
        }
        return false;
    }

    private boolean existeProductoEnLista(ArrayList<Producto> productosEncontrados, Producto p) {
        for (Producto producto : productosEncontrados) {
            if (p.getId() == producto.getId()) return true;
        }
        return false;
    }

    //TODO hacer que estos comprueben si los ids existen

    // Metodo que genera automáticamente el id de un cliente
    private int generaIdCliente () {
        int id;
        do {
            id = (int) (Math.random() * 1000000) + 2000000;
        } while (buscaClienteById(id) != null);
        return id;
    }



    // Metodo que genera automáticamente el id de un trabajador
    private int generaIdTrabajador () {
        int id;
        do {
            id = (int) (Math.random() * 1000000) + 3000000;
        } while (buscaTrabajadorById(id) != null);
        return id;
    }

    private Trabajador buscaTrabajadorById(int id) {
        for (Trabajador t : trabajadores) {
            if (t.getId() == id) return t;
        }
        return null;
    }

    // Metodo que genera automáticamente el id de un admin
    private int generaIdAdmin () {
        int id;
        do {
            id = (int) (Math.random() * 1000000) + 4000000;
        } while (buscaAdminById(id) != null);
        return id;
    }

    private Admin buscaAdminById(int id) {
        for (Admin a : admins) {
            if (a.getId() == id) return a;
        }
        return null;
    }


    // Metodo que genera automáticamente el id de un pedido
    private int generaIdPedido () {
        int id;
        do {
            id = (int) (Math.random() * 1000000) + 5000000;
        } while (buscaPedidoById(id) != null);
        return id;
    }

    private Pedido buscaPedidoById(int id) {
        for (Pedido p : getTodosPedidos()) {
            if (p.getId() == id) return p;
        }
        return null;
    }


}
