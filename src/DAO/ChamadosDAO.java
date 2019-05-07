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
import MODEL.ChamadosModel;
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
public class ChamadosDAO implements INTERFACES.ChamadosInterface {

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
    private static final String BASE_CHAMADOS = PASTA_USUARIO + BARRA + "labprog" + BARRA + "rfassistencia" + BARRA + "baseChamados.dat";

    private static List<ChamadosModel> listaChamModel = new ArrayList<>();

    @Override
    public boolean abreChamado(List<ChamadosModel> listaChamados) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BASE_CHAMADOS))) {
            oos.writeObject(listaChamados);
            oos.close();
            return true;
        } catch (IOException e) {
        }
        return false;
    }

    @Override
    public List<ChamadosModel> listaChamado() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(BASE_CHAMADOS))) {
            return ChamadosDAO.listaChamModel = (List<ChamadosModel>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
        }
        return null;
    }

    @Override
    public boolean fechaChamado(ChamadosModel chamado) {
        listaChamModel = listaChamado();
        if (listaChamModel.contains(chamado)) {
            listaChamModel.set(listaChamModel.indexOf(chamado), chamado);
            abreChamado(listaChamModel);
            return abreChamado(listaChamModel);
        } else {
            return false;
        }
    }
    
    @Override
    public boolean removeChamado(ChamadosModel chamado) {
        listaChamModel = listaChamado();
        if (listaChamModel.contains(chamado)) {
            listaChamModel.remove(chamado);
            abreChamado(listaChamModel);
            return abreChamado(listaChamModel);
        } else {
            return false;
        }
    } 
    
}
