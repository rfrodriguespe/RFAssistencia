
package MODEL;

import java.io.Serializable;
import java.util.Objects;


public class EquipamentosModel implements Serializable {
    
    private String dono;
    private String marca;
    private String modelo;
    private String serial;

    public EquipamentosModel() {
    }

    public EquipamentosModel(String dono, String marca, String modelo, String serial) {
        this.dono = dono;
        this.marca = marca;
        this.modelo = modelo;
        this.serial = serial;
    }

    @Override
    public String toString() {
        return "Equipamentos{" + "dono=" + dono
                + ", marca=" + marca
                + ", modelo=" + modelo
                + ", serial=" + serial
                + '}';
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(this.serial);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EquipamentosModel other = (EquipamentosModel) obj;
        if (!Objects.equals(this.serial, other.serial)) {
            return false;
        }
        return true;
    }
    
    
    
     public String getDono() {
        return dono;
    }

    public void setDono(String dono) {
        this.dono = dono;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }
    
}