package vistas;

import controlador.Controlador;
import data.Data;
import modelos.*;
import utils.Comunicaciones;
import utils.Menus;
import utils.Utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Scanner;

public class main {
    public static final Scanner S = new Scanner(System.in);

    public static void main(String[] args) {
        Controlador controlador = new Controlador();
        do {
            mensajeInicio();
            Object user = menuInicio(controlador);
            if (user != null) {
                menuUsuario(controlador, user);
            }
        } while (true);

    }

    private static void menuUsuario(Controlador controlador, Object user) {
        if (user instanceof Cliente clienteTemp) {
            if (!clienteTemp.isActivado()) Comunicaciones.verificacionDeCorreo(clienteTemp.getEmail());
            menuCliente(controlador, clienteTemp);
        }
        if (user instanceof Trabajador trabajadorTemp) {
            menuTrabajador(controlador, trabajadorTemp);
        }
        if (user instanceof Admin adminTemp) {
            menuAdmin(controlador, adminTemp);
        }
    }

    private static void menuAdmin(Controlador controlador, Admin adminTemp) {

    }

    private static void menuTrabajador(Controlador controlador, Trabajador trabajadorTemp) {
        int op = -1;
        do {
            Menus.pintaMenuPrincipalTrabajador(trabajadorTemp);
        } while (op != 8);
    }

    private static void menuCliente(Controlador controlador, Cliente clienteTemp) {
        int op = -1;
        do {
            Menus.pintaMenuPrincipalCliente(clienteTemp);
            try {
                op = Integer.parseInt(S.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("ERROR. El formato introducido es incorrecto");
            }
            Utils.limpiaPantalla();
            switch (op) {
                case 1: // Visionado de catálogo
                    menuVisionadoCatalogo(controlador);
                    break;
                case 2: // Realiza pedido
                    subMenuPedidoCliente(controlador, clienteTemp);
                    break;
                case 3: // Ver pedidos del cliente
                    pintaPedidosCliente(controlador,clienteTemp);
                    break;
                case 4: // Ver mis datos personales
                    pintaDatosClienteParaCliente(clienteTemp);
                    break;
                case 5: // Modificar mis datos personales
                    modificaDatosCliente(clienteTemp,controlador);
                    break;
                case 6:
                    System.out.print("Hasta la próxima");
                    Utils.cargando();
                    break;
                default:
                    System.out.println("Opción introducida incorrecta");
                    break;
            }
            Utils.pulsaParaContinuar();
            Utils.limpiaPantalla();
        } while (op != 6);
    }

    private static void modificaDatosCliente(Cliente clienteTemp, Controlador controlador) {
        int op = -1;
        System.out.println("Estos son tus datos: ");
        pintaDatosClienteParaSeleccionar(clienteTemp);
        System.out.print("Introduzca el número correspondiente al dato que quiere cambiar: ");
        try {
            op = Integer.parseInt(S.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ERROR. El formato que ha introducido es incorrecto");
        }
        if (cambiaDatoCliente(controlador,clienteTemp,op))
            System.out.println("El dato ha sido cambiado correctamente");
        else System.out.println("Ha ocurrido un problema al cambiar sus datos");
    }

    private static boolean cambiaDatoCliente(Controlador controlador, Cliente clienteTemp, int op) {
        switch (op) {
            case 1: // Cambia nombre
                System.out.println("Introduzca su nuevo nombre");
                clienteTemp.setNombre(S.nextLine());
                return true;
            case 2: // Cambia Provincia
                System.out.println("Introduzca su nueva provincia");
                clienteTemp.setProvincia(S.nextLine());
                return true;
            case 3: // Cambia Localidad
                System.out.println("Introduzca su nueva localidad");
                clienteTemp.setLocalidad(S.nextLine());
                return true;
            case 4: // Cambia Dirección
                System.out.println("Introduzca su nueva dirección");
                clienteTemp.setDireccion(S.nextLine());
                return true;
            case 5: // Cambia Teléfono
                int telefono = -1;
                do {
                    System.out.println("Introduzca su nuevo teléfono");
                    try {
                        telefono = Integer.parseInt(S.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("ERROR. El formato introducido es incorrecto.");
                    }
                } while (telefono > 999999999 || telefono < 100000000);
                clienteTemp.setTelefono(telefono);
                return true;
            case 6: // Cambia Email
                boolean correoExistente = false;
                String email = "";
                do {
                    correoExistente = false;
                    System.out.print("Introduzca su nuevo email: ");
                    email = S.nextLine();
                    if (!email.contains("@")) System.out.println("El correo introducido no es válido");
                    if (controlador.buscaClienteByCorreo(email) != null) {
                        System.out.println("El correo introducido ya existe");
                        correoExistente = true;
                    }
                } while (!email.contains("@") || correoExistente);
                clienteTemp.setActivado(false);
                clienteTemp.setEmail(email);
                return true;
            default:
                return false;
        }
    }

    private static void pintaDatosClienteParaSeleccionar(Cliente clienteTemp) {
        System.out.println("=====================================");
        System.out.println();
        System.out.println("1.- Cliente: " + clienteTemp.getNombre());
        System.out.println("2.- Provincia: " + clienteTemp.getProvincia());
        System.out.println("3.- Localidad: " + clienteTemp.getLocalidad());
        System.out.println("4.- Dirección: " + clienteTemp.getDireccion());
        System.out.println("5.- Teléfono: " + clienteTemp.getTelefono());
        System.out.println("6.- Correo electrónico: " + clienteTemp.getEmail());
        System.out.println();
        System.out.println("=====================================");
    }

    private static void pintaDatosClienteParaCliente(Cliente clienteTemp) {
        System.out.println("=====================================");
        System.out.println();
        System.out.println("Cliente: " + clienteTemp.getNombre());
        System.out.println("Provincia: " + clienteTemp.getProvincia());
        System.out.println("Localidad: " + clienteTemp.getLocalidad());
        System.out.println("Dirección: " + clienteTemp.getDireccion());
        System.out.println("Teléfono: " + clienteTemp.getTelefono());
        System.out.println("Correo electrónico: " + clienteTemp.getEmail());
        System.out.println();
        System.out.println("=====================================");

    }

    private static void pintaPedidosCliente(Controlador controlador, Cliente clienteTemp) {
        if (clienteTemp.getPedidos().isEmpty()) System.out.println("Aún no tiene ningún producto en su carrito");
        else {
            Collections.sort(clienteTemp.getPedidos());
            System.out.println("Pedidos pendientes de entrega: ");
            System.out.println("============================");
            for (Pedido p : clienteTemp.getPedidosPendientesEntrega()) {
                System.out.println();
                pintaPedidoParaCliente(p,clienteTemp);
                System.out.println();

            }

            if (!clienteTemp.getPedidosCancelados().isEmpty()) {
                Utils.pulsaParaContinuar();
                System.out.println("Pedidos cancelados: ");
                System.out.println("============================");
                for (Pedido p : clienteTemp.getPedidosCancelados()) {
                    System.out.println();
                    pintaPedidoParaCliente(p, clienteTemp);
                    System.out.println();

                }
            }
            if (!clienteTemp.getPedidosEntregados().isEmpty()) {
                Utils.pulsaParaContinuar();
                System.out.println("Pedidos entregados: ");
                System.out.println("============================");
                for (Pedido p : clienteTemp.getPedidosEntregados()) {
                    System.out.println();
                    pintaPedidoParaCliente(p, clienteTemp);
                    System.out.println();

                }
            }
        }


    }

    private static void pintaPedidoParaCliente(Pedido p, Cliente clienteTemp) {
        System.out.println("============ Id: " + p.getId() + " ============");
        System.out.println();
        System.out.println("Cliente : " + clienteTemp.getNombre());
        System.out.println("Localidad: " + clienteTemp.getLocalidad());
        System.out.println("Dirección de envío: " + clienteTemp.getDireccion());
        System.out.println("Estado del Pedido: " + Data.getEstadoPedido(p.getEstado()));
        System.out.println("Comentario del Pedido: " + p.getComentario());
        System.out.println("Fecha estimada de entrega: " + p.getFechaEntregaEstimada());
        System.out.println();
        System.out.println("Productos del pedido: ");
        for (Producto producto : p.getProductos()) {
            pintaProductoParaPedidoCliente(producto);
        }
        System.out.println();
        System.out.println("Total del pedido: " + p.precioPedidoConIVA() + " euros.");

    }

    private static void pintaProductoParaPedidoCliente(Producto producto) {
        System.out.println("- " + producto.getMarca() + " || " +
                producto.getModelo() + " || " + producto.getPrecio() + " || ");
    }

    private static void subMenuPedidoCliente(Controlador controlador, Cliente cliente) {
        int op = -1;
        do {
            Menus.pintaMenuPedidoCliente();
            try {
                op = Integer.parseInt(S.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("ERROR. El formato introducido no es correcto");
            }
            switch (op) {
                case 1: // Inserta un producto en el carrito
                    insertaProductoEnCarrito(controlador, cliente);
                    break;
                case 2: // Mostramos los productos del carrito del cliente
                    pintaCarritoCliente(cliente);
                    Utils.pulsaParaContinuar();
                    break;
                case 3: // Eliminamos un producto del carro
                    // TODO cambiar la manera de seleccionar el producto y pintar antes el carrito
                    eliminaProductoCarrito(controlador,cliente);
                    break;
                case 4: // Confirmamos el pedido
                    confirmaPedidoCliente(controlador,cliente);
                    break;
                case 5: // Cancelamos el pedido
                    cancelaPedidoCliente(controlador,cliente);
                    break;
                case 6: // Salimos al menú principal
                    System.out.print("Saliendo del menú ");
                    Utils.cargando();
                    break;
                default:
                    System.out.println("Opción introducida incorrecta");
                    break;

            }
        } while (op != 6);
    }

    private static void cancelaPedidoCliente(Controlador controlador, Cliente cliente) {
        if (cliente.getPedidos().isEmpty()) System.out.println("Aún no tiene ningún producto en su carrito");
        else {
            int numPedido = -1;
            pintaPedidosParaSeleccionCliente(cliente);
            System.out.print("Introduzca el pedido que desee cancelar: ");
            try {
                numPedido = Integer.parseInt(S.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("ERROR. El formato introducido es incorrecto");
            }
            if (cliente.cancelaPedido(cliente.getPedidosPendientesEntrega().get(numPedido - 1).getId()))
                System.out.println("Pedido cancelado correctamente");
            else System.out.println("Ha ocurrido un error al cancelar el pedido");
        }
        Utils.pulsaParaContinuar();
    }

    private static void pintaPedidosParaSeleccionCliente(Cliente cliente) {
        Collections.sort(cliente.getPedidos());
        int contador = 1;
        for (Pedido p : cliente.getPedidosPendientesEntrega()) {
            System.out.println();
            System.out.print(contador + ".-");
            pintaPedidoParaSeleccionCliente(p);
            System.out.println();
            contador++;
        }
    }

    private static void pintaPedidoParaSeleccionCliente(Pedido p) {
        System.out.print("Id: " + p.getId() + ". " + Data.getEstadoPedido(p.getEstado()) + "\n");
        for (Producto producto : p.getProductos()) {
            System.out.println("\t- " + producto.getModelo());
        }
    }

    private static void confirmaPedidoCliente(Controlador controlador, Cliente cliente) {
        if (cliente.numProductosCarro() == 0) System.out.println("Aún no tiene ningún producto en su carrito");
        else {
            System.out.println("Bienvenido a la confirmación de su pedido. Este es su carrito actualmente:");
            pintaCarritoCliente(cliente);
            if (preguntaSiNo().equalsIgnoreCase("s")) {
                if (controlador.confirmaPedidoCliente(cliente.getId()))
                    System.out.println("Su pedido ha sido confirmado correctamente");
                else System.out.println("Ha ocurrido un error al confirmar su pedido");
            } else {
                System.out.println("Volviendo al menú");
                Utils.cargando();
            }
        }
        Utils.pulsaParaContinuar();
    }

    private static void eliminaProductoCarrito(Controlador controlador,Cliente cliente) {
        if (cliente.numProductosCarro() == 0) System.out.println("Aún no tiene ningún producto en su carrito");
        else {
            do {
                Utils.limpiaPantalla();
                int numProducto = -1;
                pintaCarritoCliente(cliente);
                System.out.print("Introduce el número del producto deseado: ");
                try {
                    numProducto = Integer.parseInt(S.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("ERROR. El formato introducido es incorrecto");
                }
                if (numProducto < 1 || numProducto >= cliente.getCarro().size())
                    System.out.println("El producto que ha seleccionado no existe");
                else {
                    if (cliente.quitaProductoCarro(cliente.getCarro().get(numProducto - 1).getId()))
                        System.out.println("El producto ha sido borrado de su carrito correctamente");
                    else System.out.println("El producto que ha seleccionado no ha sido encontrado");
                }

            } while (!preguntaSiNo().equalsIgnoreCase("n"));
        }
        Utils.pulsaParaContinuar();
    }

    private static void pintaCarritoCliente(Cliente cliente) {
        if (cliente.numProductosCarro() == 0) System.out.println("Aún no tiene ningún producto en su carrito");
        else {
            int contador = 1;
            System.out.println("Su carrito tiene un total de " + cliente.numProductosCarro() + " productos.");
            System.out.println("=======================================================");
            for (Producto p : cliente.getCarro()) {
                pintaProductoCarritoCliente(p, contador);
                contador++;
            }
            System.out.println();
            System.out.println("Total sin IVA:\t\t " + cliente.precioCarroSinIVA());
            System.out.println("IVA del Pedido:\t\t " + cliente.precioIVACarro());
            System.out.println("Total del Pedido:\t " + cliente.precioCarroConIVA());
        }

    }

    private static void pintaProductoCarritoCliente(Producto p, int contador) {
        System.out.println(contador + "- " + p.getMarca() + " : " + p.getModelo() + " : " + p.getPrecio());
    }

    private static void insertaProductoEnCarrito(Controlador controlador, Cliente cliente) {
        do {
            Utils.limpiaPantalla();
            int numProducto = -1;
            pintaCatalogoParaSeleccion(controlador);
            System.out.print("Introduce el número del producto deseado: ");
            try {
                numProducto = Integer.parseInt(S.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("ERROR. El formato introducido es incorrecto");
            }
            if (numProducto < 0 || numProducto >= controlador.getCatalogo().size())
                System.out.println("El producto que ha seleccionado no existe");
            else {
                cliente.addProductoCarro(controlador.getCatalogo().get(numProducto - 1));
                System.out.println("El producto ha sido añadido correctamente");
            }

        } while (!preguntaSiNo().equalsIgnoreCase("n"));
        Utils.pulsaParaContinuar();
    }

    private static void pintaCatalogoParaSeleccion(Controlador controlador) {
        int contador = 1;
        for (Producto p : controlador.getCatalogo()) {
            pintaProductoParaSeleccion(contador, p);
            contador++;
        }
        System.out.println();
    }

    private static void pintaProductoParaSeleccion(int contador, Producto p) {
        System.out.println(contador + ".- " + p.getMarca() + " - " + p.getModelo() + " - " + p.getPrecio());
    }

    private static void menuVisionadoCatalogo(Controlador controlador) {
        int op = -1;
        do {
            Menus.pintaMenuCatalogoCliente();
            try {
                op = Integer.parseInt(S.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("ERROR. El formato introducido no es válido");
            }
            Utils.limpiaPantalla();
            switch (op) {
                case 1: // Catálogo completo
                    pintaCatalogo(controlador);
                    Utils.pulsaParaContinuar();
                    break;
                case 2: // Búsqueda por marca
                    System.out.print("Introduzca la marca que está buscando: ");
                    String marca = S.nextLine();
                    ArrayList<Producto> resultados = controlador.buscaProductosByMarca(marca);
                    if (resultados.isEmpty()) System.out.println("No hemos encontrados resultados para su búsqueda");
                    else pintaProductos(resultados);
                    Utils.pulsaParaContinuar();
                    break;
                case 3: // Búsqueda por modelo
                    System.out.print("Introduzca el modelo que está buscando: ");
                    String modelo = S.nextLine();
                    resultados = controlador.buscaProductosByModelo(modelo);
                    if (resultados.isEmpty()) System.out.println("No hemos encontrados resultados para su búsqueda");
                    else pintaProductos(resultados);
                    Utils.pulsaParaContinuar();
                    break;
                case 4: // Búsqueda por descripción
                    System.out.print("Introduzca la descripción que está buscando: ");
                    String descripcion = S.nextLine();
                    resultados = controlador.buscaProductosByDescripcion(descripcion);
                    if (resultados.isEmpty()) System.out.println("No hemos encontrados resultados para su búsqueda");
                    else pintaProductos(resultados);
                    Utils.pulsaParaContinuar();
                    break;
                case 5: // Búsqueda por término
                    System.out.print("Introduzca el término que está buscando: ");
                    String termino = S.nextLine();
                    resultados = controlador.buscaProductosByTermino(termino);
                    if (resultados.isEmpty()) System.out.println("No hemos encontrados resultados para su búsqueda");
                    else pintaProductos(resultados);
                    Utils.pulsaParaContinuar();
                    break;
                case 6: // Búsqueda por precio
                    System.out.print("Introduzca el precio mínimo que está buscando: ");
                    float precioMinimo = Float.parseFloat(S.nextLine());
                    System.out.print("Introduzca el precio máximo que está buscando: ");
                    float precioMaximo = Float.parseFloat(S.nextLine());
                    resultados = controlador.buscaProductoByPrecio(precioMinimo, precioMaximo);
                    if (resultados.isEmpty()) System.out.println("No hemos encontrados resultados para su búsqueda");
                    else pintaProductos(resultados);
                    Utils.pulsaParaContinuar();
                    break;
                case 7: // Salir
                    break;
                default:
                    System.out.println("Opción introducida incorrecta");
                    break;
            }
        } while (op != 7);
    }

    private static void pintaProductos(ArrayList<Producto> resultados) {
        String op = "";
        for (int i = 0; i < resultados.size(); i++) {
            pintaProductoSinRegistro(resultados.get(i));
            if ((i + 1) % 5 == 0) {
                op = preguntaSiNo();
                if (op.equalsIgnoreCase("N")) {
                    System.out.println();
                    System.out.print("Regresando");
                    Utils.cargando();
                    i = resultados.size();
                }
            }
        }
    }


    private static Object menuInicio(Controlador controlador) {
        int op = 0;
        System.out.print("""
                \t\tBienvenido a Fernanshop
                =======================================
                1.- Ver Catálogo
                2.- Registro
                3.- Inicio de Sesión
                
                Marque su opción:\s""");
        try {
            op = Integer.parseInt(S.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ERROR. Formato introducido incorrecto");
        }
        switch (op) {
            case 1:
                pintaCatalogo(controlador);
                break;
            case 2:
                registroCliente(controlador);
                break;
            case 3:
                return inicioSesion(controlador);

        }
        return null;
    }

    private static void registroCliente(Controlador controlador) {
        int telefono = 0;
        String email = "";
        boolean correoExistente = false;
        System.out.println("Bienvenido al menú de registro");
        do {
            correoExistente = false;
            System.out.print("Introduzca su email: ");
            email = S.nextLine();
            if (!email.contains("@")) System.out.println("El correo introducido no es válido");
            if (controlador.buscaClienteByCorreo(email) != null) {
                System.out.println("El correo introducido ya existe");
                correoExistente = true;
            }
        } while (!email.contains("@") || correoExistente);
        System.out.print("Introduzca su contraseña: ");
        String clave = S.nextLine();
        System.out.print("Introduzca su nombre: ");
        String nombre = S.nextLine();
        System.out.print("Introduzca su localidad: ");
        String localidad = S.nextLine();
        System.out.print("Introduzca su provincia: ");
        String provincia = S.nextLine();
        System.out.print("Introduzca su dirección: ");
        String direccion = S.nextLine();
        do {
            System.out.print("Introduzca su número de teléfono: ");
            try {
                telefono = Integer.parseInt(S.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("ERROR. Formato erróneo.");
            }
        } while (telefono > 999999999 || telefono < 100000000);

        if (controlador.registraCliente(email, clave, nombre, localidad, provincia, direccion, telefono)) {
            System.out.println("Se ha registrado correctamente");
            System.out.println("Aún tiene que verificar su correo");
            if (preguntaSiNo().equalsIgnoreCase("s")) Comunicaciones.verificacionDeCorreo(email);
        }
        else System.out.println("Ha ocurrido un problema a la hora de registrarse");
        Utils.limpiaPantalla();
    }

    private static Object inicioSesion(Controlador controlador) {
        System.out.print("Introduzca su email: ");
        String email = S.nextLine();
        System.out.print("Introduzca su contraseña: ");
        String clave = S.nextLine();
        Utils.limpiaPantalla();
        return controlador.login(email, clave);
    }

    private static void pintaCatalogo(Controlador controlador) {
        String op = "";
        ArrayList<Producto> productosParaPintar = new ArrayList<>();
        productosParaPintar.addAll(controlador.getCatalogo());
        int cantidad = controlador.getCatalogo().size();
        for (int i = 0; i < cantidad; i++) {
            Producto temp = productosParaPintar.get((int) (Math.random() * productosParaPintar.size()));
            pintaProductoSinRegistro(temp);
            productosParaPintar.remove(temp);

            if ((i + 1) % 5 == 0) {
                op = preguntaSiNo();
                if (op.equalsIgnoreCase("N")) {
                    System.out.println("Volviendo al menú de inicio");
                    Utils.cargando();
                    i = controlador.getCatalogo().size();
                }
            }
        }
        Utils.pulsaParaContinuar();
        Utils.limpiaPantalla();
    }

    private static void pintaProductoSinRegistro(Producto producto) {

        System.out.println("===================================");
        System.out.println("\t" + producto.getMarca() +
                ((producto.getRelevancia() > 9) ? " ⭐" : ""));
        System.out.println("\t" + producto.getModelo());
        System.out.println("\t" + producto.getDescripcion());
        System.out.println("\t" + producto.getPrecio());
        System.out.println("===================================");
        System.out.println();

    }

    private static String preguntaSiNo() {
        String op;
        do {
            System.out.println("¿Quieres continuar?(S/N)");
            op = S.nextLine();
        } while (!op.equalsIgnoreCase("S") && !op.equalsIgnoreCase("N"));
        return op;
    }

    private static void mensajeInicio() {
        System.out.println("""
                ██████╗ ██╗███████╗███╗   ██╗██╗   ██╗███████╗███╗   ██╗██╗██████╗  ██████╗      █████╗     ███████╗███████╗██████╗ ███╗   ██╗ █████╗ ███╗   ██╗███████╗██╗  ██╗ ██████╗ ██████╗\s
                ██╔══██╗██║██╔════╝████╗  ██║██║   ██║██╔════╝████╗  ██║██║██╔══██╗██╔═══██╗    ██╔══██╗    ██╔════╝██╔════╝██╔══██╗████╗  ██║██╔══██╗████╗  ██║██╔════╝██║  ██║██╔═══██╗██╔══██╗
                ██████╔╝██║█████╗  ██╔██╗ ██║██║   ██║█████╗  ██╔██╗ ██║██║██║  ██║██║   ██║    ███████║    █████╗  █████╗  ██████╔╝██╔██╗ ██║███████║██╔██╗ ██║███████╗███████║██║   ██║██████╔╝
                ██╔══██╗██║██╔══╝  ██║╚██╗██║╚██╗ ██╔╝██╔══╝  ██║╚██╗██║██║██║  ██║██║   ██║    ██╔══██║    ██╔══╝  ██╔══╝  ██╔══██╗██║╚██╗██║██╔══██║██║╚██╗██║╚════██║██╔══██║██║   ██║██╔═══╝\s
                ██████╔╝██║███████╗██║ ╚████║ ╚████╔╝ ███████╗██║ ╚████║██║██████╔╝╚██████╔╝    ██║  ██║    ██║     ███████╗██║  ██║██║ ╚████║██║  ██║██║ ╚████║███████║██║  ██║╚██████╔╝██║    \s
                ╚═════╝ ╚═╝╚══════╝╚═╝  ╚═══╝  ╚═══╝  ╚══════╝╚═╝  ╚═══╝╚═╝╚═════╝  ╚═════╝     ╚═╝  ╚═╝    ╚═╝     ╚══════╝╚═╝  ╚═╝╚═╝  ╚═══╝╚═╝  ╚═╝╚═╝  ╚═══╝╚══════╝╚═╝  ╚═╝ ╚═════╝ ╚═╝    \s
                """);
    }
}
