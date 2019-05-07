package zTESTES;

import java.util.List;
import MODEL.ClientesPFModel;
import MODEL.EnderecoModel;

import CONTROLLER.ClientesPFController;

/**
 *
 * @author Rodrigo
 */
public class ClientesPFView {

    public static void main(String args[]) {

        EnderecoModel end = new EnderecoModel("12345", "rua tal", "1300", "apto 301", "bairro tal", "cidade tal", "estado tal");
        ClientesPFModel cliPf1 = new ClientesPFModel("013.231.974-86", "Rodrigo", "rodrigo2208@gmail.com", "81988557802", end);

        // Acessando direto pelo Controller
        ClientesPFController cliPfCtrl = new ClientesPFController();
        List<ClientesPFModel> minhaLista = cliPfCtrl.leClientesPf();
        // Acessando direto pelo Controller

        System.out.println("Lista de Clientes Pessoa Física");
        for (int i = 0; i < minhaLista.size(); i++) {
            System.out.println(minhaLista.get(i));
        }

//        //Gravando uma base limpa no arquivo ClientesPF.dat
//          DAO.ClientesPFDAO.gravaListaBaseClientesPF();
//        //
//          //Adicionando um usuário do tipo ClientePF manualmente
//          ClientesPFDAO dao = new ClientesPFDAO();
//          List<ClientesPFModel> minhaLista = dao.leClientesPf();
//          minhaLista.add(cliPf1);
//          dao.gravaClientesPF(minhaLista);
//          //
//        //Verifica base e se necessário cria
//        if (DAO.ClientesPFDAO.verificaBaseClientesPF()) {
//            System.out.println("Base existe");
//        } else {
//            System.out.println("Base não enxontrada, criando nova");
//            DAO.ClientesPFDAO.criaArquivoBaseClientesPF();
//            if (DAO.ClientesPFDAO.verificaBaseClientesPF()) {
//                System.out.println("Base Criada com sucesso");
//                ClientesPFDAO.gravaListaBaseClientesPF();
//            }else {
//                System.out.println("Não foi possível criar");
//            }
//        }
//        // Acessando direto pelo DAO
//        DAO.ClientesPFDAO daoclipf = new DAO.ClientesPFDAO();
//        List<ClientesPFModel> minhaLista = daoclipf.leClientesPf();
//        // Acessando direto pelo DAO
//        List<Funcionarios> minhaLista = new ArrayList<>();
//        List<ClientesPJ> minhaLista = new ArrayList<>();
//        minhaLista.add(cliPj);
//        minhaLista.add(cliPf1);
//        minhaLista.add(cliPf2);
//        minhaLista.add(cliPf3);
//        minhaLista.add(cliPf4);
//        
//        Controle.gravaObjeto(minhaLista);
//        System.out.println("Lista ??");
//        for (Pessoas teste : minhaLista) {
//            System.out.println(teste);
//        }
//        listaCliPf.add(cliPf2);
//        listaCliPf.add(cliPf3);
//        listaCliPj.add(cliPj);
//        Controle.gravaObjeto(listaCliPf);
//        Controle.gravaObjeto(listaCliPj);
//        Controle.gravaObjeto(listaFunc);
//        System.out.println("Lista de Clientes Pessoa Física");
//        for (int i = 0; i < listaCliPf.size(); i++) {
//            System.out.println(listaCliPf.get(i));
//        }
//        System.out.println("Lista de Clientes Pessoa Jurídica");
//        for (int i = 0; i < listaCliPj.size(); i++) {
//            System.out.println(listaCliPj.get(i));
//        }
//
//        System.out.println("Lista de Funcionários");
//        for (int i = 0; i < listaFunc.size(); i++) {
//            System.out.println(listaFunc.get(i));
//        }
    }

}
