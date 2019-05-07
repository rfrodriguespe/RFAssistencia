
package MODEL;

import MODEL.EnderecoModel;
import MODEL.PessoasModel;

public class ClientesPJModel extends PessoasModel {

    private String razaoSocial;
    private String CNPJ;
    private String IE;
    private String IM;

    public ClientesPJModel() {
    }

    public ClientesPJModel(String razaoSocial, String CNPJ, String IE, String IM, String nome, String email, String telefone, EnderecoModel endereco) {
        super(nome, email, telefone, endereco);
        this.razaoSocial = razaoSocial;
        this.CNPJ = CNPJ;
        this.IE = IE;
        this.IM = IM;
    }
    
    @Override
    public String toString() {
        return "ClientesPJ{" +
                "razaoSocial=" + razaoSocial +
                ", CNPJ=" + CNPJ +
                ", IE=" + IE +
                ", IM=" + IM + '}' +
                ", Id=" + super.getId() +
                ", nome=" + super.getNome() +
                ", email=" + super.getEmail() +
                ", telefone=" + super.getTelefone()+ '}'+
                "Endereco{" + super.getEndereco() + '}';
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getCNPJ() {
        return CNPJ;
    }

    public void setCNPJ(String CNPJ) {
        this.CNPJ = CNPJ;
    }

    public String getIE() {
        return IE;
    }

    public void setIE(String IE) {
        this.IE = IE;
    }

    public String getIM() {
        return IM;
    }

    public void setIM(String IM) {
        this.IM = IM;
    }
    
}
