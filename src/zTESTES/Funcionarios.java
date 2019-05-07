package zTESTES;

import MODEL.EnderecoModel;
import MODEL.PessoasModel;
import java.util.Objects;

public class Funcionarios extends PessoasModel {

    private String cpf;
    private String cargo;
    private String setor;
    private String matricula;
    private String ctps;
    private String pis;

    public Funcionarios() {
    }

    public Funcionarios(String cpf, String cargo, String setor, String matricula, String ctps, String pis, String nome, String email, String telefone, EnderecoModel endereco) {
        super(nome, email, telefone, endereco);
        this.cpf = cpf;
        this.cargo = cargo;
        this.setor = setor;
        this.matricula = matricula;
        this.ctps = ctps;
        this.pis = pis;
    }

    @Override
    public String toString() {
        return "Funcionarios{" +
                "CPF= " + cpf +
                ", cargo=" + cargo +
                ", setor=" + setor +
                ", matricula=" + matricula +
                ", ctps=" + ctps +
                ", pis=" + pis +
                "Id=" + super.getId() + 
                ", Nome= " + super.getNome() + 
                ", Email= " + super.getEmail() + 
                ", Telefone= " + super.getTelefone() + 
                ", Endereco: {" + super.getEndereco() + ".";
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.cpf);
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
        final Funcionarios other = (Funcionarios) obj;
        if (!Objects.equals(this.cpf, other.cpf)) {
            return false;
        }
        return true;
    }
    
    
    
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getCtps() {
        return ctps;
    }

    public void setCtps(String ctps) {
        this.ctps = ctps;
    }

    public String getPis() {
        return pis;
    }

    public void setPis(String pis) {
        this.pis = pis;
    }
    
}
