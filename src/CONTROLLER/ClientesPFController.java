package CONTROLLER;

import MODEL.ClientesPFModel;
import DAO.ClientesPFDAO;
import java.util.List;

/**
 *
 * @author Rodrigo
 */
public class ClientesPFController {

    private final ClientesPFModel cliPFModel;
    private final ClientesPFDAO cliPfDao;
    private List<ClientesPFModel> minhaListaPf;

    public ClientesPFController() {
        cliPFModel = new ClientesPFModel();
        cliPfDao = new ClientesPFDAO();
    }

    public boolean gravaClientesPf(List<ClientesPFModel> listaCliPf) {
        return cliPfDao.gravaClientesPF(listaCliPf);
    }

    public List<ClientesPFModel> leClientesPf() {
        return minhaListaPf = cliPfDao.leClientesPF();
    }

    public boolean alteraClientesPF(ClientesPFModel CliPfModel) {
        return cliPfDao.alteraClientesPF(CliPfModel);
    }

    public boolean removeClientesPF(ClientesPFModel clientePf) {
        return cliPfDao.removeClientesPF(clientePf);
    }

}
