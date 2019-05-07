/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODEL;

import java.io.Serializable;

/**
 *
 * @author Rodrigo
 */
public class NotebookModel extends EquipamentosModel implements Serializable {
    
    private String TamTela;
    private String proc;
    private String ram;
    private String hd;

    public NotebookModel() {
    }

    public NotebookModel(String TamTela, String proc, String ram, String hd, String dono, String marca, String modelo, String serial) {
        super(dono, marca, modelo, serial);
        this.TamTela = TamTela;
        this.proc = proc;
        this.ram = ram;
        this.hd = hd;
    }

    @Override
    public String toString() {
        return "NotebookModel{"
                + "Dono: " + super.getDono()
                + "Marca: " + super.getMarca()
                + "Modelo: " + super.getModelo()
                + "Número de série: " + super.getSerial()
                + "TamTela=" + TamTela
                + ", proc=" + proc
                + ", ram=" + ram
                + ", hd=" + hd
                + '}';
    }
    
    public String getTamTela() {
        return TamTela;
    }

    public void setTamTela(String TamTela) {
        this.TamTela = TamTela;
    }

    public String getProc() {
        return proc;
    }

    public void setProc(String proc) {
        this.proc = proc;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getHd() {
        return hd;
    }

    public void setHd(String hd) {
        this.hd = hd;
    }
    
    
    
}
