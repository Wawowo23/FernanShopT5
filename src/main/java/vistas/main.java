package vistas;

import controlador.Controlador;
import modelos.*;
import utils.Comunicaciones;
import utils.Menus;
import utils.Utils;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
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

    // Función que gestiona la clase de usuario que ha iniciado sesión y lo lleva su respectivo menú
    private static void menuUsuario(Controlador controlador, Object user) {
        if (user instanceof Admin) {
            menuAdmin(controlador);
        }

        if (user instanceof Trabajador trabajadorTemp) {
            menuTrabajador(controlador, trabajadorTemp);
        }

        if (user instanceof Cliente clienteTemp) {

            menuCliente(controlador, clienteTemp);
        }
    }

    // Función que gestiona todas las funciones de un admin
    private static void menuAdmin(Controlador controlador) {
        int op = -1;
        
        do {
            Menus.pintaMenuAdmin(controlador);
            
            try {
                op = Integer.parseInt(S.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("ERROR. Formato introducido incorrecto.");
            }
            switch (op) {
                case 1: // Pintado del catálogo                
                    pintaMenuAdminCatalogo(controlador);
                    break;
                case 2: // Edición de un producto
                    editaProductoAdmin(controlador);
                    break;
                case 3: // Resumen de todos los clientes                
                    pintaResumenClientes(controlador);
                    break;
                case 4: // Resumen de todos los pedidos
                    pintaResumenPedidos(controlador);
                    break;
                case 5: // Resumen de todos los trabajadores                
                    pintaResumenTrabajadores(controlador);
                    break;
                case 6: // Estadísticas de la aplicación
                    pintaEstadisticasAplicacion(controlador);
                    break;
                case 7: // Cambiado del estado de un pedido
                    cambiaEstadoPedidoAdmin(controlador);
                    break;
                case 8: // Dar de alta a un trabajador
                    darAltaTrabajador(controlador);
                    break;
                case 9: // Dar de baja a un trabajador                
                    darBajaTrabajador(controlador);
                    break;
                case 10: // Asignación de un pedido a un trabajador
                    asignaPedidosPorAdmin(controlador);
                    break;
                case 11: // Salida                
                    System.out.println("Saliendo");
                    Utils.cargando();
                    break;
                default:
                    System.out.println("Opción no válida");
            }
            Utils.pulsaParaContinuar();
            Utils.limpiaPantalla();
        } while (op != 11);
    }

    // Función que gestiona la asignación manual de pedidos a trabajadores
    private static void asignaPedidosPorAdmin(Controlador controlador) {
        ArrayList<Trabajador> trabajadoresDeAlta = controlador.getTrabajadoresDeAlta();
        ArrayList<Pedido> pedidosNoAsignados = controlador.pedidosSinTrabajador();
        if (trabajadoresDeAlta.isEmpty()) System.out.println("Aún no hay trabajadores");
        if (pedidosNoAsignados.isEmpty()) System.out.println("Aún no hay pedidos");
        if (!pedidosNoAsignados.isEmpty() && !trabajadoresDeAlta.isEmpty()){
            Collections.sort(pedidosNoAsignados);
            Pedido pedidoElegido = seleccionaPedidoParaAsignar(pedidosNoAsignados);
            if (pedidoElegido == null) System.out.println("El pedido elegido no existe");
            else {
                Trabajador trabajadorElegido = seleccionaTrabajadorParaAsignar(trabajadoresDeAlta);
                if (trabajadorElegido == null) System.out.println("El trabajador elegido no existe");
                else {
                    if (controlador.asignaPedido(pedidoElegido.getId(),trabajadorElegido.getId()))
                        System.out.println("El pedido ha sido asignado correctamente");
                    else System.out.println("Ha ocurrido un problema al asignar el pedido");
                }
            }


        }
    }

    // Función por la cual se selecciona un pedido entre una lista de estos
    private static Pedido seleccionaPedidoParaAsignar(ArrayList<Pedido> pedidosNoAsignados) {
        int pedidoElegido = -1;
        pintaPedidosParaAsignacion(pedidosNoAsignados);
        System.out.print("Elige el pedido: ");
        try {
            pedidoElegido = Integer.parseInt(S.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ERROR. El formato introducido es incorrecto.");
        }
        // Comprobamos que el pedido elegido realmente exista
        if (pedidoElegido <= 0 || pedidoElegido > pedidosNoAsignados.size()) return null;
        return pedidosNoAsignados.get(pedidoElegido - 1);
    }

    // Función que pinta los pedidos para que el admin los asigne
    private static void pintaPedidosParaAsignacion(ArrayList<Pedido> pedidosNoAsignados) {
        int contador = 1;

        for (Pedido p : pedidosNoAsignados) {
            System.out.println(contador + ".- Id: " + p.getId() + ". " + p.getEstado() + " - (" +
                    p.getFechaPedido() + ") -> (" + p.getFechaEntregaEstimada() + ").");
            contador++;
        }
    }

    // Función que sirve para que el admin seleccione un trabajador para asignarle un pedido
    private static Trabajador seleccionaTrabajadorParaAsignar(ArrayList<Trabajador> trabajadoresDeAlta) {
        int trabajadorElegido = -1;
        pintaTrabajadoresParaAsignacion(trabajadoresDeAlta);
        System.out.print("Seleccione un trabajador: ");
        try {
            trabajadorElegido = Integer.parseInt(S.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ERROR. El formato introducido es incorrecto.");
        }
        // Comprobamos si el trabajador elegido realmente existe
        if (trabajadorElegido <= 0 || trabajadorElegido > trabajadoresDeAlta.size()) return null;
        return trabajadoresDeAlta.get(trabajadorElegido - 1);
    }

    // Función que pinta los trabajadores disponibles para asignarle un pedido
    private static void pintaTrabajadoresParaAsignacion(ArrayList<Trabajador> trabajadores) {
        int contador = 1;
        for (Trabajador t : trabajadores) {
            System.out.println(contador + ".- Id: " + t.getId() + ". " + t.getNombre() + " - " + t.getMovil() +
                    ". Pedidos asignados pendientes: " + t.numPedidosPendientes());
            contador++;
        }
    }

    // Función que gestiona el apartado de dar de baja un trabajador
    private static void darBajaTrabajador(Controlador controlador) {
        ArrayList<Trabajador> trabajadoresDeAlta = controlador.getTrabajadoresDeAlta();
        if (trabajadoresDeAlta.isEmpty()) System.out.println("No hay trabajadores para dar de baja.");
        else {
            int trabajadorElegido = -1;
            pintaResumenTrabajadoresParaSeleccion(trabajadoresDeAlta);
            System.out.print("Elige al trabajador: ");
            try {
                trabajadorElegido = Integer.parseInt(S.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("ERROR. El formato introducido es incorrecto.");
            }
            // Comprobamos que el trabajador elegido realmente exista
            if (trabajadorElegido != -1 && (trabajadorElegido > 0 && trabajadorElegido <= trabajadoresDeAlta.size())) {
                if (controlador.darDeBajaTrabajador(trabajadoresDeAlta.get(trabajadorElegido - 1).getId()))
                    System.out.println("El trabajador ha sido dado de baja correctamente");
                else System.out.println("Ha ocurrido un problema al dar de baja al trabajador");
            } else System.out.println("El trabajador seleccionado no existe");
        }
    }

    // Función que pinta un resumen de los trabajadores a modo de menú de selección
    private static void pintaResumenTrabajadoresParaSeleccion(ArrayList<Trabajador> trabajadores) {
        int contador = 1;
        for (Trabajador t : trabajadores) {
            System.out.println(contador + ".- " + "Id: " + t.getId() + " - " + t.getNombre() + " - " + t.getMovil() + " - " + t.getEmail());
            contador++;
        }
        System.out.println();
    }

    // Función que gestiona el apartado de dar de alta a un trabajador
    private static void darAltaTrabajador(Controlador controlador) {
        int op = -1;
            System.out.print("""
                *********************************************
                  1.- Dar de alta un trabajador ya registrado
                  2.- Registrar un nuevo trabajador
                *********************************************
                Introduzca una opción:\s""");
        try {
            op = Integer.parseInt(S.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ERROR. El formato introducido es incorrecto.");
        }
        if (op < 1 || op > 2) System.out.println("Opción introducida incorrecta");
        if (op == 1) {
            int trabajadorElegido = -1;
            ArrayList<Trabajador> trabajadoresDeBaja = controlador.getTrabajadoresDeBaja();
            if (trabajadoresDeBaja.isEmpty()) System.out.println("No hay trabajadores para dar de alta en este momento");
            pintaResumenTrabajadoresParaSeleccion(trabajadoresDeBaja);
            System.out.print("Elige al trabajador: ");
            try {
                trabajadorElegido = Integer.parseInt(S.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("ERROR. El formato introducido es incorrecto.");
            }
            if (trabajadorElegido != -1 && (trabajadorElegido > 0 && trabajadorElegido <= trabajadoresDeBaja.size())) {
                if (controlador.darDeAltaTrabajador(trabajadoresDeBaja.get(trabajadorElegido - 1).getId()))
                    System.out.println("El trabajador ha sido dado de alta correctamente");
                else System.out.println("Ha ocurrido un problema al dar de alta al trabajador");
            } else System.out.println("El trabajador seleccionado no existe");
        }
        if (op == 2) {
            registraNuevoTrabajador(controlador);
        }
    }

    // Función que reúne todos los datos requeridos para registrar un trabajador
    private static void registraNuevoTrabajador(Controlador controlador) {
        int telefono = -1;
        String email = "";
        boolean emailRepetido = false;
        System.out.print("Introduzca el nombre del trabajador: ");
        String nombre = S.nextLine();
        System.out.print("Introduzca el clave del trabajador: ");
        String clave = S.nextLine();
        do {
            System.out.println("Introduzca el teléfono del trabajador");
            try {
                telefono = Integer.parseInt(S.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("ERROR. Formato introducido incorrecto.");
            }
        } while (telefono > 999999999 || telefono < 100000000);
        do { // Comprobamos que el correo introducido no esté ya registrado
            emailRepetido = false;
            System.out.println("Introduzca el email del trabajador");
            email = S.nextLine();
            if (!email.contains("@")) System.out.println("El email que ha introducido no es válido");
            if (controlador.emailRepetido(email)) {
                System.out.println("Ese email ya ha sido registrado");
                emailRepetido = true;
            }
        } while (emailRepetido || !email.contains("@"));
        if (controlador.nuevoTrabajador(email, clave, nombre, telefono))
            System.out.println("El trabajador ha sido registrado correctamente");
        else System.out.println("Ha ocurrido un error al registrar al trabajador");
    }

    // Función que permite al admin cambiar el estado de un pedido
    private static void cambiaEstadoPedidoAdmin(Controlador controlador) {
        if (controlador.getTodosPedidos().isEmpty()) System.out.println("No hay pedidos realizados aún");
        else {
            int idPedido = -1;
            pintaResumenPedidos(controlador);
            System.out.print("Introduce el id del pedido: ");
            try {
                idPedido = Integer.parseInt(S.nextLine());
            } catch (NumberFormatException e ) {
                System.out.println("ERROR. El formato introducido es incorrecto.");
            }
            if (idPedido != -1) {
                Pedido temp = controlador.buscaPedidoById(idPedido);
                if (modificaPedido(controlador,temp)){
                    try {
                        Comunicaciones.enviaCorreoCambioEstadoPedidoCliente(temp,controlador.buscaPropietarioPedido(temp.getId()));
                    } catch (RuntimeException e) {
                        System.out.println("No se ha encontrado el correo del cliente");
                    }
                    System.out.println("Se ha modificado correctamente el pedido");
                }
                else System.out.println("Ha ocurrido un error al modificar el pedido");
            }
        }
    }


    // Función que pinta las estadísticas de la aplicación
    private static void pintaEstadisticasAplicacion(Controlador controlador) {
        System.out.printf(""" 
                ========================================================
                                  Estadísticas de la APP            
                 Número de clientes: %d            
                 Número de trabajadores: %d
                 Número de pedidos: %d            
                 Número de pedidos pendientes: %d
                 Número de pedidos completados o cancelados: %d            
                 Número de pedidos sin asignar: %d            
                =========================================================
                """,controlador.numClientes(),controlador.numTrabajadores(),
                controlador.numPedidosTotales(),controlador.numPedidosPendientes(),controlador.numPedidosCompletados(),
                controlador.numPedidosSinTrabajador()
        );
    }

    // Función que pinta un resumen de todos los trabajadores para el admin
    private static void pintaResumenTrabajadores(Controlador controlador) {
        if (controlador.getTrabajadores().isEmpty()) System.out.println("Aún no hay trabajadores dados de alta");
        else {
            for (Trabajador t : controlador.getTrabajadores()) {
                System.out.println("Estos son todos los trabajadores registrados: ");
                pintaResumenTrabajadorAdmin(t);
            }
            System.out.println();
        }
    }

    // Función que pinta los datos de un trabajador en una linea para el admin
    private static void pintaResumenTrabajadorAdmin(Trabajador t) {
        System.out.println("Id: " + t.getId() + ".- " + t.getNombre() + " - " + t.getMovil() + " - " + t.getEmail() + ". " +
                ((t.isAlta()) ? "Alta.":"Baja." + " Pedidos asignados pendientes: " + t.numPedidosPendientes()));
    }

    // Función que pinta un resumen de todos los pedidos para el admin
    private static void pintaResumenPedidos(Controlador controlador) {
        if (controlador.numPedidosTotales() == 0) System.out.println("Aún no hay ningún pedido realizado");
        else {
            Collections.sort(controlador.getTodosPedidos());
            System.out.println("Estos son todos los pedidos realizados: ");
            for (Pedido p : controlador.getTodosPedidos()) {
                pintaResumenPedidoParaAdmin(p);
            }
            System.out.println();
        }
    }

    // Función que pinta los datos de un pedido en una linea para el admin
    private static void pintaResumenPedidoParaAdmin(Pedido p) {
        System.out.println("Id: " + p.getId() + ".- " + Utils.getEstadoPedido(p.getEstado()) + ". (" + p.getFechaPedido() +
                ") -> (" + p.getFechaEntregaEstimada() + "). " + p.getComentario());
    }

    // Función que pinta un resumen de todos los clientes para el admin
    private static void pintaResumenClientes(Controlador controlador) {
        if (controlador.getClientes().isEmpty()) System.out.println("No hay clientes registrados aún.");
        else {
            System.out.println("Estos son todos los clientes registrados: ");
            for (Cliente c : controlador.getClientes()) {
                pintaResumenClienteParaAdmin(c);
            }
            System.out.println();
        }
    }

    // Función que pinta los datos de un cliente en una linea para el admin
    private static void pintaResumenClienteParaAdmin(Cliente c) {
        System.out.println("Id: " + c.getId() + " .- " + c.getNombre() + " - " + c.getEmail() + " - " + c.getDireccion());

    }

    // Función que permite a un admin editar los distintos datos de un producto
    private static void editaProductoAdmin(Controlador controlador) {
        System.out.print("Introduzca el producto que está buscando: ");
        String termino = S.nextLine();
        // Pintamos todos los productos que contengan el término introducido por el admin
        ArrayList <Producto> resultados = controlador.buscaProductosByTermino(termino);
        if (resultados.isEmpty()) System.out.println("No hemos encontrados resultados para su búsqueda");
        else {
            pintaProductosParaSeleccion(resultados);
            int productoSeleccionado = -1;
            System.out.print("Selecciona el producto que quieres modificar: ");
            try {
                productoSeleccionado = Integer.parseInt(S.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("ERROR. Formato introducido incorrecto.");
            }
            // Comprobamos que el producto seleccionado exista
            if (productoSeleccionado != -1 &&
                    (productoSeleccionado > 0 && productoSeleccionado <= resultados.size())) {
                Producto temp = resultados.get(productoSeleccionado - 1);
                if (menuModificacionProductoParaAdmin(temp))
                    System.out.println("El producto ha sido modificado correctamente.");
                else System.out.println("Ha habido un problema al modificar el producto.");
            }

        }
    }

    // Función que le muestra al admin los datos del producto que haya seleccionado y le permite cambiarlos
    private static boolean menuModificacionProductoParaAdmin(Producto temp) {
        int op = -1;
        System.out.println("Este es el producto que has seleccionado");
        pintaProductoParaSeleccionAdmin(temp);
        System.out.print("Elija el dato que quiere cambiar: ");
        try {
            op = Integer.parseInt(S.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ERROR. El formato introducido es incorrecto.");
        }
        return modificaProductoAdmin(op,temp);
    }

    // Función que gestiona los cambios de los distintos atributos de un producto
    private static boolean modificaProductoAdmin(int op, Producto temp) {
        switch (op) {
            case 1:
                System.out.print("Introduce la nueva marca: ");
                temp.setMarca(S.nextLine());
                return true;

            case 2:
                System.out.print("Introduce el nuevo modelo: ");
                temp.setModelo(S.nextLine());
                return true;
            case 3:
                float precio = -1;
                do {
                    System.out.print("Introduce el nuevo precio: ");
                    try {
                        precio = Float.parseFloat(S.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("ERROR. El formato introducido incorrecto.");
                    }
                } while (precio <= 0);
                temp.setPrecio(precio);
                return true;

            case 4:
                int relevancia = -1;
                do {
                    System.out.print("Introduce la nueva relevancia: ");
                    try {
                        relevancia  = Integer.parseInt(S.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("ERROR. El formato introducido incorrecto.");
                    }
                } while (relevancia <= 0);
                temp.setRelevancia(relevancia);
                return true;

            case 5:
                System.out.print("Introduce la nueva descripción: ");
                temp.setDescripcion(S.nextLine());
                return true;

            default:
                return false;
        }
    }

    // Función que pinta los datos de un producto para que un admin los seleccione
    private static void pintaProductoParaSeleccionAdmin(Producto p) {
        System.out.println("****************************");
        System.out.println("1.- Marca: " + p.getMarca());
        System.out.println("2.- Modelo: " + p.getModelo());
        System.out.println("3.- Precio: " + p.getPrecio());
        System.out.println("4.- Relevancia: " + p.getRelevancia());
        System.out.println("5.- Descripción: " + p.getDescripcion());
        System.out.println("****************************\n");
    }


    // Función que pinta el catálogo completo para el admin
    private static void pintaMenuAdminCatalogo(Controlador controlador) {
        int contador = 1;
        for (Producto p : controlador.getCatalogo()) {
            System.out.println();
            pintaProductoParaAdmin(p);
            System.out.println();
            if (contador % 5 == 0)
                if (Utils.preguntaSiNo().equalsIgnoreCase("n")) break;
            contador++;
        }
    }

    // Función que pinta los datos de un producto para un admin
    private static void pintaProductoParaAdmin(Producto p) {
        System.out.println("****************************");
        System.out.println("Id: " + p.getId());
        System.out.println("Marca: " + p.getMarca());
        System.out.println("Modelo: " + p.getModelo());
        System.out.println("Precio: " + p.getPrecio());
        System.out.println("Relevancia: " + p.getRelevancia());
        System.out.println("Descripción: " + p.getDescripcion());
        System.out.println("****************************");
    }


    // Función que gestiona todas las funciones del trabajador
    private static void menuTrabajador(Controlador controlador, Trabajador trabajadorTemp) {
        int op = -1;
        do {
            Menus.pintaMenuPrincipalTrabajador(trabajadorTemp);
            try {
                op = Integer.parseInt(S.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("ERROR. El formato introducido es incorrecto");
            }
            Utils.limpiaPantalla();
            switch (op) {
                case 1: // Consultar Pedidos Asignados
                    pintaPedidosAsignadosTrabajador(controlador,trabajadorTemp);
                    break;
                case 2: // Modificar el estado de un pedido
                    modificaEstadoPedidoTrabajador(controlador,trabajadorTemp);
                    break;
                case 3: // Consultar el catálogo de productos
                    menuVisionadoCatalogo(controlador);
                    break;
                case 4: // Modificar un producto
                    modificaProductoTrabajador(controlador);
                    break;
                case 5: // Ver el historial de pedidos terminados del trabajador
                    pintaPedidosCompletadosTrabajador(controlador,trabajadorTemp);
                    break;
                case 6: // Ver el perfil del trabajador
                    verDatosTrabajador(trabajadorTemp);
                    break;
                case 7: // Modificar los datos personales del trabajador
                    modificaDatosPersonalesTrabajador(controlador,trabajadorTemp);
                    break;
                case 8: // Salir
                    System.out.println("Hasta la próxima");
                    Utils.cargando();
                    break;
                default:
                    System.out.println("Opción introducida incorrecta");
                    break;

            }
            Utils.pulsaParaContinuar();
            Utils.limpiaPantalla();
        } while (op != 8);
    }

    // Función que permite al trabajador cambiar sus datos personales de su perfil
    private static void modificaDatosPersonalesTrabajador(Controlador controlador, Trabajador trabajadorTemp) {
        int op = -1;
        // Mostramos los datos del trabajador
        System.out.println("Estos son tus datos personales");
        System.out.println("1.- Nombre: " + trabajadorTemp.getNombre());
        System.out.println("2.- Email: " + trabajadorTemp.getEmail());
        System.out.println("3.- Móvil: " + trabajadorTemp.getMovil());
        System.out.println();
        System.out.print("Introduce la opción a cambiar: ");
        try {
            op = Integer.parseInt(S.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ERROR. El formato introducido es incorrecto");
        }
        if (cambiaDatoTrabajador(op,trabajadorTemp,controlador)) System.out.println("Se han cambiado los datos correctamente");
        else System.out.println("Ha habido un error al cambiar los datos");

    }

    // Función que gestiona los cambios en los datos personales del trabajador
    private static boolean cambiaDatoTrabajador(int op, Trabajador trabajadorTemp, Controlador controlador) {
        switch (op) { // Comprobamos cual es el dato que quiere cambiar el trabajador
            case 1:
                System.out.print("Introduzca su nuevo nombre: ");
                trabajadorTemp.setNombre(S.nextLine());
                return true;
            case 2:
                String email = "";
                // Comprobamos que el trabajador introduzca un correo que no exista
                System.out.print("Introduzca su nuevo correo electrónico: ");
                email = S.nextLine();
                if (!email.contains("@") || controlador.emailRepetido(email)) {
                    System.out.println("El correo que ha introducido no es válido.");
                    return false;
                }
                trabajadorTemp.setEmail(email);
                return true;
            case 3:
                int telefono = 0;
                do {
                    System.out.println("Introduzca su nuevo teléfono");
                    try {
                        telefono = Integer.parseInt(S.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("ERROR. El formato introducido es incorrecto.");
                    }
                } while (telefono > 999999999 || telefono < 100000000);
                trabajadorTemp.setMovil(telefono);
                return true;
            default:
                return false;
        }
    }

    // Función que pinta los datos de un trabajador
    private static void verDatosTrabajador(Trabajador trabajadorTemp) {
        System.out.println("Estos son tus datos personales: ");
        System.out.println("======================================");
        System.out.println("Nombre: " + trabajadorTemp.getNombre());
        System.out.println("Email: " + trabajadorTemp.getEmail());
        System.out.println("Movil: " + trabajadorTemp.getMovil());
        System.out.println("======================================");
    }

    // Función que pinta todos los pedidos asignados a un trabajador que están asignados
    private static void pintaPedidosCompletadosTrabajador(Controlador controlador, Trabajador trabajadorTemp) {
        // Comprobamos si hay pedidos completados
        if (trabajadorTemp.getPedidosCompletos().isEmpty()) System.out.println("No hay pedidos completos.");
        else {
            System.out.println("Estos son todos tus pedidos completados " + trabajadorTemp.getNombre());
            int contador = 1;
            System.out.println("Estos son tus pedidos asignados (completados y pendientes)");
            Collections.sort(trabajadorTemp.getPedidosAsignados());
            for (PedidoClienteDataClass p : controlador.getPedidosCompletadosTrabajador(trabajadorTemp.getId())) {
                System.out.println();
                System.out.print(contador + ".- ");
                pintaPedidoParaTrabajador(p);

            }
        }
    }

    // Función que permite a un trabajador modificar los atributos de un producto
    private static void modificaProductoTrabajador(Controlador controlador) {
        System.out.print("Introduzca el producto que está buscando: ");
        String termino = S.nextLine();
        ArrayList <Producto> resultados = controlador.buscaProductosByTermino(termino);
        // Comprobamos si hay productos que contengan en término que ha introducido el trabajador
        if (resultados.isEmpty()) System.out.println("No hemos encontrados resultados para su búsqueda");
        else {
            pintaProductosParaSeleccion(resultados);
            int productoSeleccionado = -1;
            System.out.print("Selecciona el producto que quieres modificar: ");
            try {
                productoSeleccionado = Integer.parseInt(S.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("ERROR. Formato introducido incorrecto.");
            }
            // Comprobamos que el producto elegido por el trabajador exista
            if (productoSeleccionado != -1 &&
                    (productoSeleccionado > 0 && productoSeleccionado <= resultados.size())) {

                Producto temp = resultados.get(productoSeleccionado - 1);
                if (menuModificacionProducto(temp))
                    System.out.println("El producto ha sido modificado correctamente.");
                else System.out.println("Ha habido un problema al modificar el producto.");
            }

        }
    }

    // Función que gestiona todos los cambios que un trabajador le puede hacer a un trabajo
    private static boolean menuModificacionProducto(Producto temp) {
        int op = -1;
        System.out.printf("""
                1.- Modelo: %s
                2.- Marca: %s
                3.- Descripción: %s
                4.- Precio: %f
                
                Introduce la opción:\s""",temp.getModelo(),temp.getMarca(),
                temp.getDescripcion(),temp.getPrecio());
        try {
            op = Integer.parseInt(S.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ERROR. El formato introducido es incorrecto.");
        }
        switch (op) {
            case 1:
                System.out.print("Introduce el nuevo modelo: ");
                temp.setModelo(S.nextLine());
                return true;
            case 2:
                System.out.print("Introduce la nueva marca: ");
                temp.setMarca(S.nextLine());
                return true;
            case 3:
                System.out.print("Introduce la nueva descripción: ");
                temp.setDescripcion(S.nextLine());
                return true;
            case 4:
                float precio = -1;
                do {
                    System.out.print("Introduce el nuevo precio: ");
                    try {
                        precio = Float.parseFloat(S.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("ERROR. El formato introducido incorrecto.");
                    }
                } while (precio <= 0);
                temp.setPrecio(precio);
                return true;
            default:
                return false;
        }
    }

    // Función que pinta distintos productos para un menú de selección
    private static void pintaProductosParaSeleccion(ArrayList<Producto> resultados) {
        int contador = 1;
        for (Producto p : resultados) {
            pintaProductoParaSeleccion(contador,p);
            contador++;
        }
        System.out.println();
    }

    // Función que gestiona la modificación del estado de un pedido para un trabajador
    private static void modificaEstadoPedidoTrabajador(Controlador controlador, Trabajador trabajadorTemp) {
        if (trabajadorTemp.getPedidosAsignados().isEmpty()) System.out.println("No tienes pedidos asignados");
        else {
            int numPedido = -1;
            pintaPedidosAsignadosTrabajador(controlador, trabajadorTemp);
            System.out.print("Introduce el pedido: ");
            try {
                numPedido = Integer.parseInt(S.nextLine());
            } catch (NumberFormatException e ) {
                System.out.println("ERROR. El formato introducido es incorrecto.");
            }
            if (numPedido > 0 && numPedido <= trabajadorTemp.getPedidosAsignados().size()) {
                Pedido temp = trabajadorTemp.getPedidosAsignados().get(numPedido - 1);
                if (modificaPedido(controlador,temp)){
                    try {
                        Comunicaciones.enviaCorreoCambioEstadoPedidoCliente(temp,controlador.buscaPropietarioPedido(temp.getId()));
                    } catch (RuntimeException e) {
                        System.out.println("No se ha encontrado el correo del cliente");
                    }
                    System.out.println("Se ha modificado correctamente el pedido");

                }
                else System.out.println("Ha ocurrido un error al modificar el pedido");
            }
        }

    }

    // Función que gestiona la modificación de un atributo de un pedido.
    private static boolean modificaPedido(Controlador controlador, Pedido temp) {
        int op = -1;
        System.out.print("""
                =============================================
                 1.- Modifica el estado del pedido
                 2.- Añadir un comentario al pedido
                 3.- Modificar la fecha de entrega estimada
                =============================================
                
                Introduce una opción:\s""");
        try {
            op = Integer.parseInt(S.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ERROR. El formato introducido es incorrecto.");
        }
        switch (op) {
            case 1:
                int nuevoEstado = -1;
                System.out.print("""
                        1.- En preparación
                        2.- Enviado
                        3.- Entregado
                        4.- Cancelado
                        """);
                System.out.print("Introduce el nuevo estado: ");
                try {
                    nuevoEstado = Integer.parseInt(S.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("ERROR. El formato introducido es incorrecto.");
                }
                return controlador.cambiaEstadoPedido(temp.getId(),nuevoEstado);
            case 2:
                System.out.print("Introduzca el comentario que quiere añadir: ");
                temp.setComentario(S.nextLine());
                return true;
            case 3:
                LocalDate nuevaFecha = null;
                System.out.print("Introduzca la nueva fecha para el paquete (yyyy-mm-dd): ");
                try {
                    nuevaFecha = LocalDate.parse(S.nextLine());
                } catch (DateTimeException e) {
                    System.out.println("ERROR. Formato introducido incorrecto.");
                }
                return temp.cambiaFechaEntrega(nuevaFecha);
            default:
                return false;
        }
    }

    // Función que pinta todos los pedidos que tiene un trabajador asignado
    private static void pintaPedidosAsignadosTrabajador(Controlador controlador, Trabajador trabajadorTemp) {
        if (trabajadorTemp.getPedidosAsignados().isEmpty()) System.out.println("No tienes pedidos asignados");
        else {
            int contador = 1;
            System.out.println("Estos son tus pedidos asignados (completados y pendientes)");
            Collections.sort(trabajadorTemp.getPedidosAsignados());
            for (PedidoClienteDataClass p : controlador.getPedidosAsignadosYCompletados(trabajadorTemp.getId())) {
                System.out.println();
                System.out.print(contador + ".- ");
                pintaPedidoParaTrabajador(p);

            }
        }
    }

    // Función quepinta todos los datos de un pedido y su cliente para un trabajador
    private static void pintaPedidoParaTrabajador(PedidoClienteDataClass p) {
        System.out.print("============== " + p.getIdPedido() + " ==============\n");
        System.out.println("Nombre: " + p.getNombreCliente());
        System.out.println("Dirección: " + p.getDireccion());
        System.out.println("Localidad: " + p.getLocalidad());
        System.out.println("Provincia: " + p.getProvincia());
        System.out.println("Teléfono: " + p.getTelefono());
        System.out.println("Fecha del pedido: " + p.getFechaPedido());
        System.out.println("Fecha de entrega estimada: " + p.getFechaEntregaEstimada());
        System.out.println("Estado: " + Utils.getEstadoPedido(p.getEstado()));
        System.out.println("Comentario: " + p.getComentario());
        System.out.println("Detalles del pedido: ");
        for (Producto producto : p.getProductos()) {
            pintaProductoParaPedido(producto);
        }
    }

    // Función que gestiona todas las funciones de los clientes
    private static void menuCliente(Controlador controlador, Cliente clienteTemp) {

        int op = -1;
        do {
            if (!clienteTemp.isActivado())
                if (verificacionDeCorreoCliente(clienteTemp.getEmail()))
                    clienteTemp.setActivado(true);
                else return;
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

    // Función que gestiona el cambio de datos personales de un cliente
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

    // Función por la cual el cliente introduce su nuevo dato para cambiar.
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
                    if (controlador.emailRepetido(email)) {
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

    // Función que pinta los datos de un cliente a modo de menú de selección
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

    // Función que pinta los datos de un cliente
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

    // Función que pinta los pedidos de un cliente
    private static void pintaPedidosCliente(Controlador controlador, Cliente clienteTemp) {
        if (clienteTemp.getPedidos().isEmpty()) System.out.println("Aún no tiene ningún producto en su carrito");
        else {
            // Primero ordenamos los pedidos
            Collections.sort(clienteTemp.getPedidos());
            // Pintamos los pedidos que aún están pendientes de entrega
            System.out.println("Pedidos pendientes de entrega: ");
            System.out.println("============================");
            for (Pedido p : clienteTemp.getPedidosPendientesEntrega()) {
                System.out.println();
                pintaPedidoParaCliente(p,clienteTemp);
                System.out.println();

            }

            // Pintamos los pedidos cancelados si hay
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

            // Pintamos los pedidos entregados si hay
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

    // Función que pinta los datos del pedido de un cliente
    private static void pintaPedidoParaCliente(Pedido p, Cliente clienteTemp) {
        System.out.println("============ Id: " + p.getId() + " ============");
        System.out.println();
        System.out.println("Cliente : " + clienteTemp.getNombre());
        System.out.println("Localidad: " + clienteTemp.getLocalidad());
        System.out.println("Dirección de envío: " + clienteTemp.getDireccion());
        System.out.println("Estado del Pedido: " + Utils.getEstadoPedido(p.getEstado()));
        System.out.println("Comentario del Pedido: " + p.getComentario());
        System.out.println("Fecha estimada de entrega: " + p.getFechaEntregaEstimada());
        System.out.println();
        System.out.println("Productos del pedido: ");
        for (Producto producto : p.getProductos()) {
            pintaProductoParaPedido(producto);
        }
        System.out.println();
        System.out.println("Total del pedido: " + p.precioPedidoConIVA(21) + " euros.");

    }

    // Función que pinta los datos de un producto a modo de resumen.
    private static void pintaProductoParaPedido(Producto producto) {
        System.out.println("- " + producto.getMarca() + " || " +
                producto.getModelo() + " || " + producto.getPrecio() + " || ");
    }

    // Función que gestiona todas las funciones que un cliente puede realizar en cuanto a sus pedidos
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

    // Función que cancela el pedido de un cliente
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
            // Comprobamos que el pedido elegido existe
            if (numPedido > 0 && numPedido <= cliente.getPedidosPendientesEntrega().size()) {
            if (cliente.cancelaPedido(cliente.getPedidosPendientesEntrega().get(numPedido - 1).getId()))
                System.out.println("Pedido cancelado correctamente");
            else System.out.println("Ha ocurrido un error al cancelar el pedido");
            } else System.out.println("Ese pedido no existe");
        }
        Utils.pulsaParaContinuar();
    }

    //Función que pinta los pedidos a modo de menú de selección para el cliente
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

    // Función que pinta los atributos de un pedido para un cliente
    private static void pintaPedidoParaSeleccionCliente(Pedido p) {
        System.out.print("Id: " + p.getId() + ". " + Utils.getEstadoPedido(p.getEstado()) + "\n");
        for (Producto producto : p.getProductos()) {
            System.out.println("\t- " + producto.getModelo());
        }
    }

    // Función que permite a un cliente convertir su carrito en un pedido
    private static void confirmaPedidoCliente(Controlador controlador, Cliente cliente) {
        if (cliente.numProductosCarro() == 0) System.out.println("Aún no tiene ningún producto en su carrito");
        else {
            System.out.println("Bienvenido a la confirmación de su pedido. Este es su carrito actualmente:");
            pintaCarritoCliente(cliente);
            if (Utils.preguntaSiNo().equalsIgnoreCase("s")) {
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

    // Función que elimina un producto del carrito de un cliente
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
                // Comprobamos que el producto seleccionado existe
                if (numProducto < 1 || numProducto >= cliente.getCarro().size())
                    System.out.println("El producto que ha seleccionado no existe");
                else {
                    if (cliente.quitaProductoCarro(cliente.getCarro().get(numProducto - 1).getId()))
                        System.out.println("El producto ha sido borrado de su carrito correctamente");
                    else System.out.println("El producto que ha seleccionado no ha sido encontrado");
                }

            } while (Utils.preguntaSiNo().equalsIgnoreCase("s"));
        }
        Utils.pulsaParaContinuar();
    }

    // Función que pinta todos los productos que tiene un cliente en su carrito
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
            System.out.println("IVA del Pedido:\t\t " + cliente.precioIVACarro(21));
            System.out.println("Total del Pedido:\t " + cliente.precioCarroConIVA(21));
        }

    }

    // Función que pinta los atributos de un producto para el carrito de un cliente
    private static void pintaProductoCarritoCliente(Producto p, int contador) {
        System.out.println(contador + "- " + p.getMarca() + " : " + p.getModelo() + " : " + p.getPrecio());
    }

    // Función que permite al cliente meter un producto en su carrito
    private static void insertaProductoEnCarrito(Controlador controlador, Cliente cliente) {
        do {
            Utils.limpiaPantalla();
            int numProducto = -1;
            System.out.print("Introduzca el término que está buscando: ");
            String termino = S.nextLine();
            ArrayList<Producto> resultados = controlador.buscaProductosByTermino(termino);
            if (resultados.isEmpty()) System.out.println("No hemos encontrados resultados para su búsqueda");
            else {
                pintaCatalogoParaSeleccion(resultados);
                System.out.print("Introduce el número del producto deseado: ");
                try {
                    numProducto = Integer.parseInt(S.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("ERROR. El formato introducido es incorrecto");
                }
                // Comprobamos si el producto elegido existe
                if (numProducto <= 0 || numProducto > resultados.size())
                    System.out.println("El producto que ha seleccionado no existe");
                else {
                    if (controlador.addProductoCarrito(cliente, resultados.get(numProducto - 1).getId()))
                        System.out.println("El producto ha sido añadido correctamente");
                    else System.out.println("Ha ocurrido un problema al añadir el producto");
                }

            }

        } while (Utils.preguntaSiNo().equalsIgnoreCase("s")) ;
        Utils.pulsaParaContinuar();
    }

    // Función que pinta el catálogo a modo de menú de selección para  el cliente
    private static void pintaCatalogoParaSeleccion(ArrayList<Producto> resultados) {
        int contador = 1;
        for (Producto p : resultados) {
            pintaProductoParaSeleccion(contador, p);
            contador++;
        }
        System.out.println();
    }

    // Función que pinta los atributos de un producto paraun menú de selección
    private static void pintaProductoParaSeleccion(int contador, Producto p) {
        System.out.println(contador + ".- " + p.getMarca() + " - " + p.getModelo() + " - " + p.getPrecio());
    }

    // Función que gestiona todas las maneras en las que un cliente puede ver el catálogo de productos
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
                    try {
                        float precioMinimo = Float.parseFloat(S.nextLine());
                        System.out.print("Introduzca el precio máximo que está buscando: ");
                        float precioMaximo = Float.parseFloat(S.nextLine());
                        resultados = controlador.buscaProductoByPrecio(precioMinimo, precioMaximo);
                        if (resultados.isEmpty()) System.out.println("No hemos encontrados resultados para su búsqueda");
                        else pintaProductos(resultados);
                        Utils.pulsaParaContinuar();
                    } catch (NumberFormatException e) {
                        System.out.println("ERROR. El formato introducido es incorrecto.");
                    }
                    break;
                case 7: // Salir
                    break;
                default:
                    System.out.println("Opción introducida incorrecta");
                    break;
            }
        } while (op != 7);
    }

    // Función que pinta todos los productos que haya almacenados en una lista pasada por los parámetros
    private static void pintaProductos(ArrayList<Producto> resultados) {
        String op = "";
        for (int i = 0; i < resultados.size(); i++) {
            pintaProductoSinRegistro(resultados.get(i));
            if ((i + 1) % 5 == 0) {
                op = Utils.preguntaSiNo();
                if (op.equalsIgnoreCase("N")) {
                    System.out.println();
                    System.out.print("Regresando");
                    Utils.cargando();
                    i = resultados.size();
                }
            }
        }
    }


    // Función que gestiona el menú inicial que sale al iniciar el programa
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
            case 1: // Pinta catálogo sin estar registrado
                pintaCatalogo(controlador);
                break;
            case 2: // Registramos a un cliente
                registroCliente(controlador);
                break;
            case 3: // Inicio de sesión
                return inicioSesion(controlador);
            default:
                System.out.println("Opción introducida incorrecta");
                break;

        }
        return null;
    }

    // Función que gestiona toda la petición de datos para registrar a un cliente
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
            if (controlador.emailRepetido(email)) {
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
            if (Utils.preguntaSiNo().equalsIgnoreCase("s"))
                if (verificacionDeCorreoCliente(email))
                    controlador.buscaClienteByCorreo(email).setActivado(true);
        }
        else System.out.println("Ha ocurrido un problema a la hora de registrarse");
        Utils.limpiaPantalla();
    }

    // Función que verifica el correo de un cliente que aún no está activado
    private static boolean verificacionDeCorreoCliente(String email) {
        int numToken = (int) (Math.random() * 1000000);
        String token = "FS-" + numToken;
        String tokenIntroducido = "";
        try {
            Comunicaciones.enviarCorreoVerificacion(email,token);
        } catch (RuntimeException e) {
            System.out.println("No hemos podido encontrar su correo");
            return false;
        }
        do {
            System.out.print("Inserte el token que hemos enviado a su correo: ");
            tokenIntroducido = S.nextLine();
        } while (!token.equals(tokenIntroducido));
        System.out.println("Su correo ha sido verificado correctamente");
        return true;
    }

    // Función que gestiona el inicio de sesión pidiendo el email y la contraseña
    private static Object inicioSesion(Controlador controlador) {
        System.out.print("Introduzca su email: ");
        String email = S.nextLine();
        System.out.print("Introduzca su contraseña: ");
        String clave = S.nextLine();
        Utils.limpiaPantalla();
        Object user = controlador.login(email, clave);
        if (user == null) System.out.println("Contraseña y/o email introducidos incorrectos");
        return user;
    }

    // Función que pinta todos los productos del catálogo de 5 en 5
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
                op = Utils.preguntaSiNo();
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

    // Función que pinta los datos de un producto cuando el usuario no ha iniciado sesión
    private static void pintaProductoSinRegistro(Producto producto) {

        System.out.println("===================================");
        if (producto.getRelevancia() > 9) System.out.println("\t******* Promo Especial *******");
        System.out.println("ID: " + producto.getId());
        System.out.println("Marca: " +
                ((producto.getRelevancia() > 9) ? " ⭐ " : "") +
                producto.getMarca() +
                ((producto.getRelevancia() > 9) ? " ⭐" : ""));
        System.out.println("Modelo: " + producto.getModelo());
        System.out.println("Descripción: " + producto.getDescripcion());
        System.out.println("Precio: " + producto.getPrecio());
        System.out.println("===================================");
        System.out.println();

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
