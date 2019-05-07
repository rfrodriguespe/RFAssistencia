/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zTESTES;

/**
 *
 * @author Jefferson Bruno de Assis Lima, José Cláudio de Figueiredo Filh e
 * Rodrigo Ferreira Rodrigues.
 *
 * Essa classe é usada para gerar o modelo do objeto USUÁRIO que tem seus
 * atributos e seus métodos de atribuir "set" e pegar o valor, "get".
 *
 */
public class Pecas {

    private int id;
    private String identificador;
    private String nome;
    private byte[] email;
    private String senha;
    private byte[] telefone;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public byte[] getEmail() {
        return email;
    }

    public void setEmail(byte[] email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public byte[] getTelefone() {
        return telefone;
    }

    public void setTelefone(byte[] telefone) {
        this.telefone = telefone;
    }
}
