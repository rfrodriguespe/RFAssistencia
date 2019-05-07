package MODEL;

import java.io.Serializable;

public abstract class PessoasModel implements Serializable {

    private static int autoIncrement = 0;
    private int id;
    private String nome;
    private String email;
    private String telefone;
    private EnderecoModel endereco;
    
    public PessoasModel() {
    }

    public PessoasModel(String nome, String email, String telefone, EnderecoModel endereco) {
        this.id = ++autoIncrement;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.endereco = endereco;
    }
    
    @Override
    public String toString() {
        return "Pessoas{" + "id=" + id +
                ", nome=" + nome +
                ", email=" + email +
                ", telefone=" + telefone + '}'+
                "Endereco{" + getEndereco()+ '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    
    public EnderecoModel getEndereco() {
        return endereco;
    }

    public void setEndereco(EnderecoModel endereco) {
        this.endereco = endereco;
    }

}
