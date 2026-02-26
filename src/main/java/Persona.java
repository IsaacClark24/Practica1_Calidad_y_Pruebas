import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Persona {
    private String nombre;
    private int edad;
    private double estatura;
    private double peso;


    public Persona(){

    }

    public Persona(String nombre, int edad, double estatura, double peso){
        this.nombre = nombre;
        this.edad = edad;
        this.estatura = estatura;
        this.peso = peso;
    }

    public long calcularDiasVividos(String fechaNacimientoStr){
        // Definimos el formato dd/MM/yyyy
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fechanacimiento = LocalDate.parse(fechaNacimientoStr, formato);
        LocalDate hoy = LocalDate.now();

        if (fechanacimiento.isAfter(hoy)) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser futura.");
        }

        return ChronoUnit.DAYS.between(fechanacimiento, hoy);
    }

    public double calcularIMC (){
        if(this.estatura <= 0){
            throw new ArithmeticException("Error: No se puede dividir por cero.");
        }
        return this.peso/ Math.pow(this.estatura,2);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public double getEstatura() {
        return estatura;
    }

    public void setEstatura(double estatura) {
        this.estatura = estatura;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }
}
