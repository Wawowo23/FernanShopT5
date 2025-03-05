package vistas;

import controlador.Controlador;
import modelos.Admin;
import modelos.Cliente;
import modelos.Producto;
import modelos.Trabajador;
import utils.Menus;
import utils.Utils;

import java.util.ArrayList;
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
    }

    private static void menuCliente(Controlador controlador, Cliente clienteTemp) {
        int op = -1;
        do {
            System.out.printf("""
                            Bienvenido a Fernanshop %s. Tienes %d %s
                            ==========================================
                            
                            1.- Consultar el catálogo de productos
                            2.- Realizar un pedido
                            3.- Ver mis pedidos
                            4.- Ver mis datos personales
                            5.- Modificar mis datos personales
                            6.- Salir
                            
                            Introduzca su opción:\s""", clienteTemp.getNombre(), clienteTemp.getPedidosPendientes().size(),
                    ((clienteTemp.getPedidosPendientes().size() == 1) ? "pedido pendiente de entrega"
                            : "pedidos pendientes de entrega"));
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
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    System.out.print("Hasta la próxima");
                    Utils.cargando();
                    break;
                default:
                    System.out.println("Opción introducida incorrecta");
                    break;
            }
        } while (op != 6);
    }

    private static void subMenuPedidoCliente(Controlador controlador, Cliente cliente) {
        int op = -1;
        do {
            System.out.print("""
                         Menú de Pedido
                    ========================
                    
                    1.- Inserta un producto en el carro
                    2.- Ver el carro
                    3.- Eliminar un producto del carro
                    4.- Confirmar el pedido
                    5.- Cancelar el pedido
                    6.- Salir
                    
                    Introduzca la opción deseada:\s""");
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
                    break;
                case 3: // Eliminamos un producto del carro
                    // TODO cambiar la manera de seleccionar el producto y pintar antes el carrito
                    eliminaProductoCarrito(controlador,cliente);
                    break;
                case 4: // Confirmamos el pedido
                    break;
                case 5: // Cancelamos el pedido
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

    private static void eliminaProductoCarrito(Controlador controlador,Cliente cliente) {
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
                if (cliente.quitaProductoCarro(controlador.getCatalogo().get(numProducto - 1).getId()))
                    System.out.println("El producto ha sido borrado de su carrito correctamente");
                else System.out.println("El producto que ha seleccionado no ha sido encontrado");
            }

        } while (!preguntaSiNo().equalsIgnoreCase("n"));
        Utils.pulsaParaContinuar();
    }

    private static void pintaCarritoCliente(Cliente cliente) {
        System.out.println("Su carrito tiene un total de " + cliente.getCarro().size() + " productos.");
        System.out.println("=======================================================");
        for (Producto p : cliente.getCarro()) {
            pintaProductoCarritoCliente(p);
        }
        System.out.println();
        System.out.println("Total sin IVA:\t\t " + cliente.precioCarroSinIVA());
        System.out.println("IVA del Pedido:\t\t " + cliente.precioIVACarro());
        System.out.println("Total del Pedido:\t " + cliente.precioCarroConIVA());
        Utils.pulsaParaContinuar();
        Utils.limpiaPantalla();
    }

    private static void pintaProductoCarritoCliente(Producto p) {
        System.out.println("- " + p.getMarca() + " : " + p.getModelo() + " : " + p.getPrecio());
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
            System.out.print("""
                    Menú de visionado de catálogo
                    =============================
                    
                    1.- Ver todo el catálogo
                    2.- Búsqueda por marca
                    3.- Búsqueda por modelo
                    4.- Búsqueda por descripción
                    5.- Búsqueda por término
                    6.- Búsqueda por precio
                    7.- Salir
                    
                    Introduzca su manera deseada de ver el catálogo:\s""");
            try {
                op = Integer.parseInt(S.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("ERROR. El formato introducido no es válido");
            }
            Utils.limpiaPantalla();
            switch (op) {
                case 1: // Catálogo completo
                    pintaCatalogo(controlador);
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
        System.out.println("Bienvenido al menú de registro");
        do {
            System.out.print("Introduzca su email: ");
            email = S.nextLine();
            if (!email.contains("@")) System.out.println("El correo introducido no es válido");
        } while (!email.contains("@"));
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

        if (controlador.registraCliente(email, clave, nombre, localidad, provincia, direccion, telefono))
            System.out.println("Se ha registrado correctamente");
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
