package MODEL;

import java.util.Objects;

public class ClientesPFModel extends PessoasModel {

    private String CPF;

    public ClientesPFModel(String CPF) {
        this.CPF = CPF;
    }

    public ClientesPFModel() {
    }

    public ClientesPFModel(String CPF, String nome, String email, String telefone, EnderecoModel endereco) {
        super(nome, email, telefone, endereco);
        this.CPF = CPF;
    }

    @Override
    public String toString() {
        return "CPF=" + CPF + ", "
                + "Id=" + super.getId()
                + ", Nome= " + super.getNome()
                + ", Email= " + super.getEmail()
                + ", Telefone= " + super.getTelefone()
                + ", Endereco: {" + super.getEndereco() + ".";
    }

    @Override
    public int hashCode() {
        return CPF.hashCode();
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
        final ClientesPFModel other = (ClientesPFModel) obj;
        if (!Objects.equals(this.CPF, other.CPF)) {
            return false;
        }
        return true;
    }

    
    
    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

}
