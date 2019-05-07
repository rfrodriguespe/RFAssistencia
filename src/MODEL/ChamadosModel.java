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
public class ChamadosModel implements Serializable {
    
    private static int autoIncrement = 0;
    private int numero;
    private String cliente;
    private String equipamento;
    private String defeitoRelatado;
    private String defeitoConstatado;
    private String solucao;
    private String status;

    public ChamadosModel() {
    }

    public ChamadosModel(String cliente, String equipamento, String defeitoRelatado, String defeitoConstatado, String solucao, String status) {
        this.numero = ++autoIncrement;
        this.cliente = cliente;
        this.equipamento = equipamento;
        this.defeitoRelatado = defeitoRelatado;
        this.defeitoConstatado = defeitoConstatado;
        this.solucao = solucao;
        this.status = status;
    }

    @Override
    public String toString() {
        return "ChamadosModel{" + "numero=" + numero + ", cliente=" + cliente + ", equipamento=" + equipamento + ", defeitoRelatado=" + defeitoRelatado + ", defeitoConstatado=" + defeitoConstatado + ", solucao=" + solucao + ", status=" + status + '}';
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + this.numero;
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
        final ChamadosModel other = (ChamadosModel) obj;
        if (this.numero != other.numero) {
            return false;
        }
        return true;
    }
    
    

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getEquipamento() {
        return equipamento;
    }

    public void setEquipamento(String equipamento) {
        this.equipamento = equipamento;
    }

    public String getDefeitoRelatado() {
        return defeitoRelatado;
    }

    public void setDefeitoRelatado(String defeitoRelatado) {
        this.defeitoRelatado = defeitoRelatado;
    }

    public String getDefeitoConstatado() {
        return defeitoConstatado;
    }

    public void setDefeitoConstatado(String defeitoConstatado) {
        this.defeitoConstatado = defeitoConstatado;
    }

    public String getSolucao() {
        return solucao;
    }

    public void setSolucao(String solucao) {
        this.solucao = solucao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
    
    
    
}
