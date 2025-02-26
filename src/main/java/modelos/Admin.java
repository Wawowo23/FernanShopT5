package modelos;

public class Admin {
    // Atributos
    private int id;
    private String nombre;
    private String pass;
    private String email;

    // Constructor

    public Admin(int id, String nombre, String pass, String email) {
        this.id = id;
        this.nombre = nombre;
        this.pass = pass;
        this.email = email;
    }

    // Constructor copia

    public Admin(Admin admin) {
        id = admin.id;
        nombre = admin.nombre;
        pass = admin.pass;
        email = admin.email;
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

    // Otros metodos

    // Metodo que comprueba si el correo y contraseña introducidas por un usuario corresponden a las de un admin
    public boolean login (String email, String pass) {
        return this.email.equals(email) && this.pass.equals(pass);
    }


    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", pass='" + pass + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
