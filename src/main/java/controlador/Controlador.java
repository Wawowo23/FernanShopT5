package controlador;

import data.DataProductos;
import modelos.*;
import utils.Comunicaciones;

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

    }

    public void mock() {
        registraCliente("miguelangelcamaracasado@gmail.com","1234","Miguel Ángel","Torredonjimeno",
                "Jaen","Calle La Cerca",668569596);
        clientes.getFirst().setActivado(true);
        nuevoTrabajador("wawowo23@gmail.com","0000","wiwi",98756321);
        admins.add(new Admin(generaIdAdmin(),"admin","4321","c@"));
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
                c.addProductoCarro(buscaProductoById(idProducto));
            return true;
        }
        return false;
    }

    // Metodo que devuelve un producto según el id introducido
    public Producto buscaProductoById (int id) {
        for (Producto p : catalogo)
            if (p.getId() == id) return p;
        return null;
    }

    // Metodo que transforma el carrito de un cliente en un pedido
    public boolean confirmaPedidoCliente (int idCliente) {
        for (Cliente c : clientes) {
            if (c.getId() == idCliente) {
                Pedido pedidoAgregado = new Pedido(generaIdPedido());
                // Añadimos al pedido creado todos los productos del carrito
                pedidoAgregado.getProductos().addAll(c.getCarro());
                // Vaciamos el carrito
                c.vaciaCarro();
                c.addPedido(pedidoAgregado);
                // Buscamos si hay algún trabajador para asignarle el pedido de manera automática
                Trabajador temp  = buscaTrabajadorCandidatoParaAsignar();
                if (temp != null) asignaPedido(pedidoAgregado.getId(),temp.getId());
                Comunicaciones.enviaCorreoClientePedidoConfirmado(pedidoAgregado,c);
                return true;
            }
        }
        return false;
    }


    // Metodo que busca al trabajador con menos pedidos asignados para asignarle un pedido
    public Trabajador buscaTrabajadorCandidatoParaAsignar() {
        ArrayList<Trabajador> trabajadoresPosibles = getTrabajadoresDeAlta();
        if (trabajadoresPosibles.isEmpty()) return null;
        if (trabajadoresPosibles.size() == 1) return trabajadoresPosibles.getFirst();
        int numPedidosMenor = Integer.MAX_VALUE;
        Trabajador trabajadorElegido = null;
        // Buscamos cual es el trabajador con menos pedidos asignados
        for (Trabajador t : trabajadoresPosibles) {
            if (t.getPedidosPendientes().size() < numPedidosMenor) {
                // Guardamos el trabajador con menos pedidos asignados
                numPedidosMenor = t.getPedidosPendientes().size();
                trabajadorElegido  = t;
            }
        }
        // Comprobamos si hay otro trabajador con la misma cantidad de pedidos que el guardado
        if (hayEmpateTrabajadoresCandidatos(trabajadorElegido)) return null;
        return trabajadorElegido;
    }

    // Metodo que comprueba si hay algún trabajador con la misma cantidad de pedidos que el posible candidato
    private boolean hayEmpateTrabajadoresCandidatos(Trabajador trabajadorElegido) {
        for (Trabajador t : getTrabajadoresDeAlta()) {
            if (t.getId() != trabajadorElegido.getId() &&
                    t.getPedidosPendientes().size() == trabajadorElegido.getPedidosPendientes().size())
                return true;
        }
        return false;
    }

    // Metodo que registra a un cliente en el sistema
    public boolean registraCliente(String email, String clave, String nombre, String localidad, String provincia, String direccion, int telefono) {
        // Comprobamos que no haya ya registrado un cliente con el correo recibido
        if (buscaClienteByCorreo(email) != null) return false;
        return clientes.add(new Cliente(generaIdCliente(),email,clave,nombre,localidad,provincia,direccion,telefono));
    }

    // Metodo que busca a un cliente por su id
    private Cliente buscaClienteById(int id) {
        for (Cliente c : clientes) {
            if (c.getId() == id) return c;
        }
        return null;
    }

    // Metodo que busca los productos que sean de la marca introducida
    public ArrayList<Producto> buscaProductosByMarca(String marca) {
        ArrayList<Producto> productosEncontrados = new ArrayList<>();
        for (Producto p : catalogo)  {
            if (p.getMarca().toLowerCase().contains(marca.toLowerCase()))
                productosEncontrados.add(p);
        }
        return productosEncontrados;
    }

    // Metodo que busca los productos que su modelo corresponda al introducido
    public ArrayList<Producto> buscaProductosByModelo(String modelo) {
        ArrayList<Producto> productosEncontrados = new ArrayList<>();
        for (Producto p : catalogo)  {
            if (p.getModelo().toLowerCase().contains(modelo.toLowerCase()))
                productosEncontrados.add(p);
        }
        return productosEncontrados;
    }

    // Metodo que busca los productos que su descripción corresponda a la introducida
    public ArrayList<Producto> buscaProductosByDescripcion(String descripcion) {
        ArrayList<Producto> productosEncontrados = new ArrayList<>();
        for (Producto p : catalogo)  {
            if (p.getDescripcion().toLowerCase().contains(descripcion.toLowerCase()))
                productosEncontrados.add(p);
        }
        return productosEncontrados;
    }

    // Metodo que devuelve una lista de los productos cuya marca, modelo o descripcion contenga el término introducido
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

    // Metodo que devuelve los productos dentro de un rango de precios
    public ArrayList<Producto> buscaProductoByPrecio(float precioMinimo, float precioMaximo) {
        ArrayList<Producto> resultados = new ArrayList<>();
        for (Producto p : catalogo) {
            if (p.getPrecio() <= precioMaximo && p.getPrecio() >= precioMinimo) resultados.add(p);
        }
        return resultados;
    }

    // Metodo que devuelve todos los pedidos de todos los clientes
    public ArrayList<Pedido> getTodosPedidos() {
        ArrayList<Pedido> todosPedidos = new ArrayList<>();
        for (Cliente c : clientes) {
            for (Pedido p : c.getPedidos()) {
                if (!existePedidoEnLista(todosPedidos,p)) todosPedidos.add(p);
            }
        }
        return todosPedidos;
    }

    // Metodo que comprueba si existe un pedido en una lista
    private boolean existePedidoEnLista(ArrayList<Pedido> todosPedidos, Pedido p) {
        for (Pedido pedido : todosPedidos) {
            if (pedido.getId() == p.getId()) return true;
        }
        return false;
    }

    // Metodo que comprueba si en una lista existe un producto
    private boolean existeProductoEnLista(ArrayList<Producto> productosEncontrados, Producto p) {
        for (Producto producto : productosEncontrados) {
            if (p.getId() == producto.getId()) return true;
        }
        return false;
    }



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

    // Metodo que genera automáticamente la id de un trabajador
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

    // Metodo que busca a un admin por su id
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

    // Metodo que busca un pedido por su id
    public Pedido buscaPedidoById(int id) {
        for (Pedido p : getTodosPedidos()) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    // Metodo que busca a un cliente por su email
    public Cliente buscaClienteByCorreo(String email) {
        for (Cliente c : clientes) {
            if (c.getEmail().equals(email)) return c;
        }
        return null;
    }

    // Metodo que cambia el estado al introducido en los parámetros de un pedido introducido en los parámetros
    public boolean cambiaEstadoPedido(int idPedido, int nuevoEstado) {
        Pedido temp = buscaPedidoById(idPedido);
        if (temp == null) return false;

        return temp.cambiaEstado(nuevoEstado);

    }

    // Metodo que registra a un trabajador con los datos introducidos por los parámetros
    public boolean nuevoTrabajador(String email, String clave, String nombre, int movil) {
        if (emailRepetido(email)) return false;
        return trabajadores.add(new Trabajador(generaIdTrabajador(),nombre,clave,email,movil));

    }

    // Metodo que busca a un trabajador por su email
    public Trabajador buscaTrabajadorByEmail(String email) {
        for (Trabajador t : trabajadores) {
            if (t.getEmail().equals(email)) return t;
        }
        return null;
    }

    // Metodo que busca cual es el trabajador al que se le ha asignado un pedido
    public Trabajador buscaTrabajadorAsignadoAPedido(int idPedido) {
        // Buscamos entre todos los pedidos de todos los trabajadores comprobando las id de los pedidos con la del parámetro
        for (Trabajador t : trabajadores) {
            for (Pedido p : t.getPedidosAsignados())
                if (p.getId() == idPedido) return t;
        }
        return null;
    }

    // Metodo que devuelve una lista de los pedidos que no han sido asignados a un trabajador aún
    public ArrayList<Pedido> pedidosSinTrabajador () {
        ArrayList<Pedido> pedidosSinTrabajador = new ArrayList<>();
        for (Pedido p : getTodosPedidos()) {
            if (buscaTrabajadorAsignadoAPedido(p.getId()) == null) pedidosSinTrabajador.add(p);
        }
        return pedidosSinTrabajador;
    }

    // Metodo que devuelve el número de pedidos que no han sido asignados a trabajadores aún
    public int numPedidosSinTrabajador () {
        return pedidosSinTrabajador().size();
    }

    // Metodo que le asigna un pedido a un trabajador
    public boolean asignaPedido(int idPedido, int idTrabajador) {
        Trabajador trabajador = buscaTrabajadorById(idTrabajador);
        Pedido pedido = buscaPedidoById(idPedido);
        if (trabajador == null || pedido == null) return false;
        Comunicaciones.enviaMensajeTelegram(pedido,trabajador.getIdTelegram());
        Cliente temp = buscaPropietarioPedido(idPedido);
        Comunicaciones.enviaCorreoPedidoAsignado(new PedidoClienteDataClass(pedido.getId(),temp.getId(),temp.getNombre(),
                temp.getProvincia(),temp.getLocalidad(),temp.getDireccion(),temp.getTelefono(),pedido.getFechaPedido(),
                pedido.getFechaEntregaEstimada(), pedido.getEstado(), pedido.getComentario(),
                pedido.getProductos()),trabajador);
        return trabajador.asignaPedido(pedido);
    }

    //  Metodo que devuelve una lista de los pedidos que han sido asignados y están pendientes a un trabajador pasado por los parámetros para pintarlos
    public ArrayList<PedidoClienteDataClass> getPedidosAsignadosTrabajador(int idTrabajador) {
        ArrayList<PedidoClienteDataClass> pedidosAsignados = new ArrayList<>();
        Trabajador temp = buscaTrabajadorById(idTrabajador);
        if (temp == null) return null;
        for (Pedido p : temp.getPedidosPendientes()) {
            Cliente clienteTemp = buscaPropietarioPedido(p.getId());
            if (clienteTemp != null)
                pedidosAsignados.add(new PedidoClienteDataClass(p.getId(),clienteTemp.getId(),
                        clienteTemp.getNombre(), clienteTemp.getProvincia(),clienteTemp.getLocalidad(),
                        clienteTemp.getDireccion(),clienteTemp.getTelefono(),p.getFechaPedido(),
                        p.getFechaEntregaEstimada(), p.getEstado(),p.getComentario(),p.getProductos()));
        }

        return pedidosAsignados;
    }

    // Metodo que busca al cliente propietario de un pedido
    public Cliente buscaPropietarioPedido(int idPedido) {
        for (Cliente c : clientes) {
            for (Pedido p : c.getPedidos())
                if (p.getId() == idPedido) return c;
        }
        return null;
    }

    // Metodo que busca el pedido que le ha sido asignado a un trabajador, pasando las id de ambos por los parámetros
    public Pedido buscaPedidoAsignadoTrabajador (int idTrabajador, int idPedido) {
        Trabajador temp = buscaTrabajadorById(idTrabajador);
        if (temp == null) return null;
        for (Pedido p : temp.getPedidosAsignados())
            if (p.getId() == idPedido) return p;
        return null;
    }

    //  Metodo que devuelve una lista de los pedidos que han sido asignados y completados a un trabajador pasado por los parámetros para pintarlos
    public ArrayList<PedidoClienteDataClass> getPedidosCompletadosTrabajador(int idTrabajador) {
        ArrayList<PedidoClienteDataClass> pedidosAsignados = new ArrayList<>();
        Trabajador temp = buscaTrabajadorById(idTrabajador);
        if (temp == null) return null;
        for (Pedido p : temp.getPedidosCompletos()) {
            Cliente clienteTemp = buscaPropietarioPedido(p.getId());
            if (clienteTemp != null)
                pedidosAsignados.add(new PedidoClienteDataClass(p.getId(),clienteTemp.getId(),
                        clienteTemp.getNombre(), clienteTemp.getProvincia(),clienteTemp.getLocalidad(),
                        clienteTemp.getDireccion(),clienteTemp.getTelefono(),p.getFechaPedido(),
                        p.getFechaEntregaEstimada(), p.getEstado(),p.getComentario(),p.getProductos()));
        }

        return pedidosAsignados;
    }

    //  Metodo que devuelve una lista de todos los pedidos que han sido asignados a
    //  un trabajador pasado por los parámetros para pintarlos
    public ArrayList<PedidoClienteDataClass> getPedidosAsignadosYCompletados(int idTrabajador) {
        ArrayList<PedidoClienteDataClass> resultados = new ArrayList<>();
        resultados.addAll(getPedidosAsignadosTrabajador(idTrabajador));
        resultados.addAll(getPedidosCompletadosTrabajador(idTrabajador));
        return resultados;
    }

    // Metodo que devuelve la cantidad de clientes que hay registrados
    public int numClientes() {
        return clientes.size();
    }

    // Metodo que devuelve la cantidad de trabajadores que hay registrados
    public int numTrabajadores() {
        return trabajadores.size();
    }

    // Metodo que devuelve la cantidad de pedidos que se han realizado
    public int numPedidosTotales() {
        return getTodosPedidos().size();
    }

    // Metodo que devuelve la cantidad de pedidos que aún no han sido entregados o cancelados
    public int numPedidosPendientes() {
        return getPedidosPendientes().size();
    }

    // Metodo que devuelve una lista de pedidos que aún no han sido entregados o cancelados
    private ArrayList<Pedido> getPedidosPendientes() {
        ArrayList<Pedido> pedidosPendientes = new ArrayList<>();
        for (Pedido p : getTodosPedidos()) {
            if (p.getEstado() < 3) pedidosPendientes.add(p);
        }
        return pedidosPendientes;
    }

    // Metodo que devuelve la cantidad de pedidos que han sido completados
    public int numPedidosCompletados() {
        return getPedidosCompletados().size();
    }

    // Metodo que devuelve una lista de pedidos que han sido completados
    private ArrayList<Pedido> getPedidosCompletados() {
        ArrayList<Pedido> pedidosPendientes = new ArrayList<>();
        for (Pedido p : getTodosPedidos()) {
            if (p.getEstado() >= 3) pedidosPendientes.add(p);
        }
        return pedidosPendientes;
    }

    // Metodo que comprueba si un email ha sido utilizado por cualquier usuario ya registrado
    public boolean emailRepetido(String email) {
        for (Admin a : admins) {
            if (a.getEmail().equals(email)) return true;
        }
        for (Trabajador t : trabajadores) {
            if (t.getEmail().equals(email)) return true;
        }
        for (Cliente c : clientes) {
            if (c.getEmail().equals(email)) return true;
        }
        return false;
    }

    // Metodo que devuelve una lista de los trabajadores que están dados de baja
    public ArrayList<Trabajador> getTrabajadoresDeBaja() {
        ArrayList<Trabajador> trabajadoresDeBaja = new ArrayList<>();
        for (Trabajador t : trabajadores) {
            if (!t.isAlta())
                trabajadoresDeBaja.add(t);
        }
        return trabajadoresDeBaja;
    }

    // Metodo que pone a un trabajador de baja
    public boolean darDeBajaTrabajador(int idTrabajador) {
        Trabajador temp = buscaTrabajadorById(idTrabajador);
        if (temp == null) return false;
        temp.setAlta(false);
        return true;
    }

    // Metodo que pone a un trabajador de alta
    public boolean darDeAltaTrabajador(int idTrabajador) {
        Trabajador temp = buscaTrabajadorById(idTrabajador);
        if (temp == null) return false;
        temp.setAlta(true);
        return true;
    }

    // Metodo que devuelve una lista de los trabajadores que están dados de alta
    public ArrayList<Trabajador> getTrabajadoresDeAlta() {
        ArrayList<Trabajador> trabajadoresDeBaja = new ArrayList<>();
        for (Trabajador t : trabajadores) {
            if (t.isAlta())
                trabajadoresDeBaja.add(t);
        }
        return trabajadoresDeBaja;
    }
}
