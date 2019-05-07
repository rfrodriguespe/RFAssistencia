package INTERFACES;

import MODEL.ClientesPFModel;
import java.util.List;

/**
 *
 * @author Rodrigo
 */
public interface ClientesPFInterface {
    
    public boolean gravaClientesPF (List<ClientesPFModel> listaClientesPF);
    public List<ClientesPFModel> leClientesPF ();
    public boolean alteraClientesPF (ClientesPFModel CliPfModel);
    public boolean removeClientesPF (ClientesPFModel clientePf);
    
}
