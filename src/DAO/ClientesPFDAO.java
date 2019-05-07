package DAO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

// A classe DAO enxerga a Classe Model
import MODEL.ClientesPFModel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rodrigo Ferreira Rodrigues
 *
 * "DAO" responsável por prover ao sistema os métodos de acesso ao banco para as
 * funções de "CRUD", assim o sistema "não se preocupa" em saber como é feito
 *
 */
public class ClientesPFDAO implements INTERFACES.ClientesPFInterface {

    /**
     * Variável para introduzir a barra correta, indepentende do S.O
     */
    private static final String BARRA = System.getProperty("file.separator");

    /**
     * Variável que indica a pasta do próprio usuário, afim de não haver
     * problemas par a gravação de arquivos.
     */
    private static final String PASTA_USUARIO = System.getProperty("user.home");

    /**
     * Arquivo baseClientesPF.dat será o destino das informações ref a Clientes
     * Pessoa Física.
     */
    private static final String BASE_CLIENTESPF = PASTA_USUARIO + BARRA + "labprog" + BARRA + "rfassistencia" + BARRA + "baseClientesPF.dat";

    private static List<ClientesPFModel> listaClientesPF = new ArrayList<>();

    @Override
    public boolean gravaClientesPF(List<ClientesPFModel> listaClientesPF) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BASE_CLIENTESPF))) {
            oos.writeObject(listaClientesPF);
            oos.close();
            return true;
        } catch (IOException e) {
        }
        return false;
    }

    @Override
    public List<ClientesPFModel> leClientesPF() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(BASE_CLIENTESPF))) {
            return ClientesPFDAO.listaClientesPF = (List<ClientesPFModel>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
        }
        return null;
    }

    @Override
    public boolean alteraClientesPF(ClientesPFModel clientePf) {
        listaClientesPF = leClientesPF();
        if (listaClientesPF.contains(clientePf)) {
            listaClientesPF.set(listaClientesPF.indexOf(clientePf), clientePf);
            gravaClientesPF(listaClientesPF);
            return gravaClientesPF(listaClientesPF);
        } else {
            return false;
        }
    }

    @Override
    public boolean removeClientesPF(ClientesPFModel clientePf) {
        listaClientesPF = leClientesPF();
        if (listaClientesPF.contains(clientePf)) {
            listaClientesPF.remove(clientePf);
            gravaClientesPF(listaClientesPF);
            return gravaClientesPF(listaClientesPF);
        } else {
            return false;
        }
    }    
    
}
