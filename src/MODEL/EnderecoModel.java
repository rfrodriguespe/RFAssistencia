package MODEL;

import java.io.Serializable;

public class EnderecoModel implements Serializable {

    private String CEP;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String uf;

    public EnderecoModel() {
    }

    public EnderecoModel(String CEP, String logradouro, String numero, String complemento, String bairro, String cidade, String uf) {
        this.CEP = CEP;
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.uf = uf;
    }

    @Override
    public String toString() {
        return "CEP=" + CEP +
                ", Logradouro= " + logradouro +
                ", Numero= " + numero +
                ", Complemento= " + complemento +
                ", Bairro= " + bairro +
                ", Cidade= " + cidade +
                ", UF= " + uf + '}';
    }

    public String getCEP() {
        return CEP;
    }

    public void setCEP(String CEP) {
        this.CEP = CEP;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

}
