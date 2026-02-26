

public class CiudadanoMexicano extends Persona{
    String lugarDeResidencia;
    String curp;
    String rfc;

    public CiudadanoMexicano() {
    }

    public CiudadanoMexicano(String lugarDeResidencia, String curp, String rfc) {
        this.lugarDeResidencia = lugarDeResidencia;
        this.curp = curp;
        this.rfc = rfc;
    }

    public CiudadanoMexicano(String nombre, int edad, double estatura, double peso) {
        super(nombre, edad, estatura, peso);
    }

    public String getLugarDeResidencia() {
        return lugarDeResidencia;
    }

    public void setLugarDeResidencia(String lugarDeResidencia) {
        this.lugarDeResidencia = lugarDeResidencia;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String clasificacion(){
        if (super.getEdad()<0){
            throw new IllegalArgumentException("La Edad No Puede Ser Negativa.");
        }

        if (super.getEdad() >= 0 && super.getEdad() <= 17) {
            return "Menor de edad";
        } else if (super.getEdad() >= 18 && super.getEdad() <= 64) {
            return "Adulto";
        } else {
            return "Adulto mayor";
        }
    }
}
