package utils;

import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import modelos.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import static jakarta.mail.Transport.send;

public class Comunicaciones {
    private static final String host = "smtp.gmail.com";
    private static final String user = "fernanshop.wiwi@gmail.com";
    private static final String pass = "xzgx nnzp dmgt lsur";


    public static boolean enviarCorreoVerificacion(String correoDestino, String token) {
        String contenido;

        //Creamos nuestra variable de propiedades con los datos de nuestro servidor de correo
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        //Obtenemos la sesión en nuestro servidor de correo
        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            @Override
            protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                return new jakarta.mail.PasswordAuthentication(user, pass);
            }
        });

        try {
            //Creamos un mensaje de correo por defecto
            Message message = new MimeMessage(session);
            // Html con CSS
            String htmlContent = """
                    <!DOCTYPE html>\s
                    <html>\s
                    <head>\s
                        <meta charset='utf-8'>\s
                        <meta http-equiv='X-UA-Compatible' content='IE=edge'>\s
                        <title>Correo</title>\s
                        <meta name='viewport' content='width=device-width, initial-scale=1'>\s
                        <style>\s
                            body {\s
                                font-family: Arial, Helvetica, sans-serif;\s
                                background-image: url(/img/fondo.jpg);\s
                            }\s
                    \s
                            #mensaje {\s
                                font-family: 'Franklin Gothic Medium', 'Arial Narrow', Arial, sans-serif;\s
                                font-size: 20px;\s
                                padding: 10px;\s
                                background-color: antiquewhite;\s
                                color: rgb(63, 58, 58);\s
                            }\s
                    \s
                            h1 {\s
                                text-align: center;\s
                                background-color: blueviolet;\s
                                padding: 10px;\s
                                color: white;\s
                                border-radius: 2px;\s
                            }\s
                    \s
                            img {\s
                                width: 40%;\s
                                margin-left: 29%;\s
                            }\s
                    \s
                            #token p {\s
                                text-align: center;\s
                            }\s
                    \s
                            #token {\s
                                background-color: rgb(209, 241, 217);\s
                                padding: 10px;\s
                                font-family: 'Courier New', Courier, monospace;\s
                                color: black;\s
                                font-weight: bold;\s
                            }\s
                    \s
                            #container {\s
                                margin-top: 20px;\s
                            }\s
                    \s
                            div p {\s
                                text-align: center;\s
                                font-style: italic;\s
                                font-size: 20px;\s
                            }\s
                    \s
                            div h3 {\s
                                text-align: center;\s
                                font-family: 'Trebuchet MS', 'Lucida Sans Unicode', 'Lucida Grande', 'Lucida Sans', Arial, sans-serif;\s
                                color: rgb(6, 12, 194);\s
                            }\s
                    \s
                            #containerToken {\s
                                margin-top: 20px;\s
                            }\s
                    \s
                            h2 {\s
                                text-align: center;\s
                                font-style: oblique;\s
                            }\s
                    \s
                            #containerRecordatorio {\s
                                background-color: aliceblue;\s
                                padding: 20px;\s
                                margin-top: 20px;\s
                            }\s
                    \s
                            #recordatorio {\s
                                background-color: brown;\s
                                padding: 10px;\s
                                color: white;\s
                            }\s
                    \s
                            #botones {\s
                                text-align: center;\s
                            }\s
                        </style>\s
                    </head>\s
                    <body>\s
                        <h1> BIENVENIDO A FERNANSHOP </h1>\s
                    
                        <div id="container">\s
                            <div id="mensaje">\s
                            <p> FERNANSHOP es un entorno seguro y te explico porqué:</p>\s
                            <ul>\s
                                <li> Establecemos un método de verificación que permite garantizar la seguridad de nuestro programa</li>\s
                                <li> Este correo es generado debido a que debes verificarte a través de un token. A continuación podrás ver el token y de esa manera verificarte</li>\s
                            </ul>\s
                            </div>\s
                        </div>\s
                        <div id="containerToken">\s
                            <div id="token">\s
                            <p> Este es el token que debes utilizar para verificarte </p>\s
                            <h3> """ + token + """
                            </h3>\s
                            </div>\s
                        </div>\s
                        <div id="containerRecordatorio">\s
                            <h2> RECORDATORIO </h2>\s
                            <div id="recordatorio">\s
                            <p> Es importante que guardes el token en un entorno seguro y en el que sólo tengas acceso tú.</p>\s
                            </div>\s
                        </div>\s
                    </body>\s
                    </html>
                    """;

            // En el mensaje, establecemos el receptor
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(correoDestino));

            // Establecemos el Asunto
            message.setSubject("Correo de verificación de uso en Fernanshop");

            // Establecemos el contenido del correo
            message.setContent(htmlContent, "text/html; charset=utf-8");

            // Añadimos el contenido del mensaje
            /*message.setText("""





                    Debe introducir el siguiente código: """ +  token);*/

            // Intentamos mandar el mensaje
            send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }



    public static boolean enviaMensajeTelegram(Pedido pedido, String idTrabajador) {
        String mensaje = "Se le ha sido asignado el pedido " + pedido.getId() + "\n";
        mensaje += "Detalles del pedido :\n";
        mensaje += "- " + Utils.getEstadoPedido(pedido.getEstado()) + "\n";
        mensaje += "- " + pedido.getFechaPedido() + "\n";
        mensaje += "- " + pedido.getFechaEntregaEstimada() + "\n";
        String direccion;
        String fijo = "https://api.telegram.org/bot7710645977:AAHX19-id6XW7u51pi3ke5d_drylga_KMZw/sendMessage?chat_id=" + idTrabajador + "&text=";
        direccion = fijo + mensaje;
        URL url;
        boolean dev;
        dev = false;
        try {
            url = new URL(direccion);
            URLConnection con = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            dev = true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return dev;
    }


    // a


    public static boolean enviaCorreoCambioEstadoPedidoCliente(Pedido pedido, Cliente cliente) {
        String contenido;

        //Creamos nuestra variable de propiedades con los datos de nuestro servidor de correo
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        //Obtenemos la sesión en nuestro servidor de correo
        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            @Override
            protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                return new jakarta.mail.PasswordAuthentication(user, pass);
            }
        });

        try {
            //Creamos un mensaje de correo por defecto
            Message message = new MimeMessage(session);

            String htmlContent = """
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <meta charset='utf-8'>
                        <meta http-equiv='X-UA-Compatible' content='IE=edge'>
                    
                        <meta name='viewport' content='width=device-width, initial-scale=1'>
                    
                        <style>
                        *{
                             padding: 0px;
                             margin: 0px;
                             font-family: Arial, Helvetica, sans-serif;
                         }
                    
                         #container {
                             width: 100%;
                             height: 1000px;
                             margin: auto;
                             margin-top: 10px;
                            \s
                             border-radius: 15px;
                             background-color: white;
                             text-align: center;
                             color: white;
                         }
                    
                         .caja{
                             border: black solid 4px;
                         }
                    
                         #inicio {
                             width: 90%;
                             height: 40px;
                             margin: auto;
                             margin-top: 10px;
                             margin-bottom: 10px;
                             background-color: rgb(169, 51, 169);
                             font-weight: bold;
                             text-align: center;
                             line-height: 40px;
                         }
                    
                         #mensaje{
                             width: 90%;
                             margin: auto;
                             margin-top: 10px;
                             margin-bottom: 10px;
                             background-color: rgb(52, 52, 180);
                             font-weight: bold;
                             text-align: center;
                         }
                         #mensaje p{
                             padding: 10px;
                         }
                    
                         #detalles {
                             width: 60%;
                             margin-top: 10px;
                             margin-bottom: 30px;
                             margin: auto;
                             background-color: green;
                             font-weight: bold;
                             text-align: center;
                         }
                    
                         #contacto{
                             background-color: rgb(214, 16, 210);
                             width: 200px;
                             margin: 15px auto;
                             font-weight: bold;
                             padding: 15px;
                             flex-wrap: wrap;
                         }
                        </style>
                    </head>
                    <body>
                        <div id="container">
                            <div id="inicio">
                            <h2>Estado del pedido:\s""" + pedido.getId() + """
                    </h2>
                    </div>
                    <div id="mensaje">
                    <p>Hola,\s""" + cliente.getNombre() + """
                    .</p>
                    <p>Tu pedido\s""" + pedido.getId() + """
                    , acaba de ser marcado como\s""" + Utils.getEstadoPedido(pedido.getEstado()) + """
                     .</p>
                     Si desliza hacia abajo podrá ver más información de su pedido.\s
                    
                    </div>
                    <div id="detalles">
                            <p>Pedido:\s""" + pedido.getId() + """
                    </p>
                    <p>Estado:\s""" + pedido.getEstado() + """
                    </p>
                                    <p>Comentario:\s""" + pedido.getComentario() + """
                        </p>
                    <p>Direccion:\s""" + cliente.getDireccion() + """
                    </p>
                    <p>Precio:\s""" + pedido.precioPedidoConIVA(21) + "E" + """
                    </p>
                    <p>Fecha estimada de llegada:\s""" + pedido.getFechaEntregaEstimada() + """
                            </p>\s
                            </div>
                    
                            <div id="contacto">
                            Contactenos en este correo para más información.
                            </div>
                        </div>
                       \s
                    
                    </body>
                    </html>""";

            // En el mensaje, establecemos el receptor
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(cliente.getEmail()));

            // Establecemos el Asunto
            message.setSubject("Actualización de estado del pedido " + pedido.getId() + ": " + pedido.getEstado());

            message.setContent(htmlContent, "text/html; charset=utf-8");

            // Intentamos mandar el mensaje
            send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public static boolean enviaCorreoPedidoAsignado(PedidoClienteDataClass p,Trabajador trabajador) {
        String contenido;
        String productos = "";
        for (Producto producto : p.getProductos()) {
            productos += "- " + producto.getModelo() + "\n";
        }


        //Creamos nuestra variable de propiedades con los datos de nuestro servidor de correo
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        //Obtenemos la sesión en nuestro servidor de correo
        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            @Override
            protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                return new jakarta.mail.PasswordAuthentication(user, pass);
            }
        });

        try {
            //Creamos un mensaje de correo por defecto
            Message message = new MimeMessage(session);

            String htmlContent = """
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <meta charset='utf-8'>
                        <meta http-equiv='X-UA-Compatible' content='IE=edge'>
                    
                        <meta name='viewport' content='width=device-width, initial-scale=1'>
                    
                        <style>
                        body {
                            font-family: 'Courier New', Courier, monospace;
                        }
                    
                        #inicio {
                            border: 4px solid black;
                            text-align: center;
                            background-color:violet;
                            color: white;
                            font-family: 'Trebuchet MS', 'Lucida Sans Unicode', 'Lucida Grande', 'Lucida Sans', Arial, sans-serif;
                        }
                    
                        #container {
                            display: block;
                            text-align: center;
                            padding: 10px;
                            width: 200px;
                            margin: 10px auto;
                           \s
                        }
                    
                        h2 {
                            text-align: center;
                            background-color: slateblue;
                            padding: 20px;
                            border: 4px solid black;
                            color: aliceblue;
                            width: 500px;
                            margin: 20px auto;
                            font-family: 'Lucida Sans', 'Lucida Sans Regular', 'Lucida Grande', 'Lucida Sans Unicode', Geneva, Verdana, sans-serif;
                        }
                        </style></head>
                                                    <body>
                                                        <div id="inicio">
                                                            <h1> DATOS DE TU PEDIDO</h1>
                                                        </div>
                                                        <h2> Pedido [""" + p.getIdPedido() + """
                                                    ]</h2>
                                                        <div id="container">
                                                            <p> Estado:\s""" + Utils.getEstadoPedido(p.getEstado()) + """
                                                            </p>
                                                            <p> Cliente: \s""" + p.getNombreCliente() + """
                                                            </p>
                                                            <p> Dirección: \s""" + p.getDireccion() + """
                                                            </p>
                                                            <p> Localidad: \s""" + p.getLocalidad() + """
                                                    </p>
                                                            <p> Provincia: \s""" + p.getProvincia() + """
                                                            </p>
                                                            <p> Teléfono: \s""" + p.getTelefono() + """
                                                    </p>
                                                            <p> Fecha del pedido: \s""" + p.getFechaPedido() + """
                                                            </p>
                                                            <p> Fecha de entrega estimada: \s""" + p.getFechaEntregaEstimada() + """
                                                            </p>
                                                            <p> Comentario del pedido: \s""" + p.getComentario() + """
                                                            </p>
                                                            <p> Detalles del pedido: </p>
                                                                <ul>
                                                                    <li>""" + productos + """
                                                                     </li>
                                                                </ul>
                                                        </div>
                                                    </body>
                                                    </html>
                    """;

            // En el mensaje, establecemos el receptor
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(trabajador.getEmail()));

            // Establecemos el Asunto
            message.setSubject("Se le ha sido asignado el pedido " + p.getIdPedido());

            message.setContent(htmlContent, "text/html; charset=utf-8");

            // Intentamos mandar el mensaje
            send(message);
        } catch (Exception e) {
            System.out.println();;
        }
        return true;
    }

    public static boolean enviaCorreoClientePedidoConfirmado(Pedido p, Cliente c) {
        String contenido;
        String productos = "";
        for (Producto producto : p.getProductos()) {
            productos += "- " + producto.getModelo() + "\n";
        }


        //Creamos nuestra variable de propiedades con los datos de nuestro servidor de correo
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        //Obtenemos la sesión en nuestro servidor de correo
        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            @Override
            protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                return new jakarta.mail.PasswordAuthentication(user, pass);
            }
        });

        try {
            //Creamos un mensaje de correo por defecto
            Message message = new MimeMessage(session);

            String htmlContent = """
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <meta charset='utf-8'>
                        <meta http-equiv='X-UA-Compatible' content='IE=edge'>
                    
                        <meta name='viewport' content='width=device-width, initial-scale=1'>
                    
                        <style>
                        body{
                            background-color: rgb(188, 105, 188);
                            color: black;
                            margin: 0px;
                            padding: 0px;
                        }
                    
                        #inicio{
                            margin: 20px;
                            margin-top: 40px;
                           \s
                        }
                    
                        #container{
                            margin: 50px;
                        }
                    
                        footer{
                            background-color: rgb(31, 30, 30);
                            color: white;
                            padding: 5px;
                            border: 1px;
                            border-radius: 5px;
                            margin: 20px;
                            margin-top: 40px;
                            width: 50%;
                        }
                    
                        #inicio, #container {
                            background-color: rgb(31, 30, 30) !important;
                            color: white !important;
                            padding: 5px;
                            padding-left: 10px;
                            border: 1px;
                            border-radius: 5px;
                        }
                        </style>
                        <body>
                                    <div id="inicio">
                                        <h1>¡Has realizado un pedido con FernanShop!</h1>
                                   \s
                                    <h3>Gracias """ + c.getNombre() + """
                                    por haber confiado en nosotros para realizar este pedido.</h3>
                                    <h3>A continuación te dejamos los datos de tu pedido</h3>
                                    </div>
                                   \s
                                    <div id="container">
                                        <h4> Pedido\s""" + p.getId() + """
                                        </h4>
                                        <p> Estado:\s""" + Utils.getEstadoPedido(p.getEstado()) + """
                                        </p>
                                        <p> Dirección: \s""" + c.getDireccion() + """
                                        </p>
                                        <p> Localidad: \s""" + c.getLocalidad() + """
                                </p>
                                        <p> Provincia:\s""" + c.getProvincia() + """
                                        </p>
                                        <p> Teléfono:\s""" + c.getTelefono() + """
                    
                                </p>
                                        <p> Fecha de realización del pedido: \s""" + p.getFechaPedido() + """
                                        </p>
                                        <p> Fecha de entrega estimada: \s""" + p.getFechaEntregaEstimada() + """
                                        </p>
                                        <p> Comentario del pedido: \s""" + p.getComentario() + """
                                        </p>
                                        <p> Detalles del pedido: </p>
                                        <p>""" + productos + """
                                    </p>
                                    </div>
                                    <footer>Esperamos que puedas seguir confiado en nosotros para realizar más pedidos.</footer>
                                </body>
                                </html>
                    """;

            // En el mensaje, establecemos el receptor
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(c.getEmail()));

            // Establecemos el Asunto
            message.setSubject("Su pedido " + p.getId() + " ha sido realizado.");

            message.setContent(htmlContent, "text/html; charset=utf-8");

            // Intentamos mandar el mensaje
            send(message);
        } catch (Exception e) {
            System.out.println();
        }
        return true;
    }
}
