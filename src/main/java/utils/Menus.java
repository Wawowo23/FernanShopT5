package utils;

import controlador.Controlador;
import modelos.Admin;
import modelos.Cliente;
import modelos.Trabajador;

public class Menus {
    public static void pintaMenuPrincipalCliente(Cliente clienteTemp) {
        System.out.printf("""
                            Bienvenido a Fernanshop %s. Tienes %d %s
                            ==========================================
                            
                            1.- Consultar el catálogo de productos
                            2.- Realizar un pedido
                            3.- Ver mis pedidos
                            4.- Ver mis datos personales
                            5.- Modificar mis datos personales
                            6.- Salir
                            ==================================
                            
                            Introduzca su opción:\s""", clienteTemp.getNombre(), clienteTemp.getPedidosPendientesEntrega().size(),
                ((clienteTemp.getPedidosPendientesEntrega().size() == 1) ? "pedido pendiente de entrega"
                        : "pedidos pendientes de entrega"));
    }

    public static void pintaMenuCatalogoCliente() {
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
                    ==================================
                    
                    Introduzca su manera deseada de ver el catálogo:\s""");
    }

    public static void pintaMenuPedidoCliente() {
        System.out.print("""
                         Menú de Pedido
                    ========================
                    
                    1.- Inserta un producto en el carro
                    2.- Ver el carro
                    3.- Eliminar un producto del carro
                    4.- Confirmar el pedido
                    5.- Cancelar el pedido
                    6.- Salir
                    ==================================
                    
                    Introduzca la opción deseada:\s""");
    }

    public static void pintaMenuPrincipalTrabajador(Trabajador trabajadorTemp) {
        System.out.printf("""
                Bienvenido %s. Tienes %d %s.
                ===============================
                
                1.- Consultar los pedidos que tengo asignados
                2.- Modificar el estado de un pedido
                3.- Consultar el catálogo de productos
                4.- Modificar un producto
                5.- Ver el histórico de pedidos terminados
                6.- Ver mi perfil
                7.- Modificar mis datos personales
                8.- Salir
                ==================================
                
                Introduzca la opción deseada:\s""",trabajadorTemp.getNombre(),
                trabajadorTemp.getPedidosPendientes().size(),
                ((trabajadorTemp.getPedidosPendientes().size() == 1) ? "pedido pendiente"
                        : "pedidos pendientes"));
    }

    public static void pintaMenuAdmin(Controlador controlador, Admin adminTemp) {
        System.out.printf(""" 
                Bienvenido Administrador. Tienes %d pedido/s sin asignar. Debe asignarlos a un trabajador.
                ========================================================
                                  Estadísticas de la APP            
                Número de clientes: %d            
                Número de trabajadores: %d
                Número de pedidos: %d            
                Número de pedidos pendientes: %d
                Número de pedidos completados o cancelados: %d            
                Número de pedidos sin asignar: %d            
                =========================================================
                """,controlador.numPedidosSinTrabajador(),controlador.numClientes(),controlador.numTrabajadores(),
                controlador.numPedidosTotales(),controlador.numPedidosPendientes(),controlador.numPedidosCompletados(),
                controlador.numPedidosSinTrabajador()
                );
        System.out.print("""
                ================================
                     Menú de operaciones            
                ================================
                            
                1.- Ver todo el catálogo            
                2.- Editar un producto            
                3.- Ver un resumen de todos los clientes            
                4.- Ver un resumen de todos los pedidos            
                5.- Ver un resumen de todos los trabajadores            
                6.- Ver las estadísticas de la aplicación            
                7.- Cambiar el estado de un pedido            
                8.- Dar de alta un trabajador            
                9.- Dar de baja un trabajador            
                10. Asignar un pedido a un trabajador            
                11.- Salir            
                
                Introduzca una opción:\s""");
    }
}
