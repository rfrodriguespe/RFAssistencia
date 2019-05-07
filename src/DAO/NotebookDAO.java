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
import MODEL.NotebookModel;
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
public class NotebookDAO implements INTERFACES.NotebooksInterface {

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
    private static final String BASE_NOTEBOOKS = PASTA_USUARIO + BARRA + "labprog" + BARRA + "rfassistencia" + BARRA + "baseNotebooks.dat";

    private static List<NotebookModel> listaNote = new ArrayList<>();

    @Override
    public boolean gravaNotebook(List<NotebookModel> listaNotebook) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BASE_NOTEBOOKS))) {
            oos.writeObject(listaNote);
            oos.close();
            return true;
        } catch (IOException e) {
        }
        return false;
    }

    @Override
    public List<NotebookModel> leNotebook() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(BASE_NOTEBOOKS))) {
            return NotebookDAO.listaNote = (List<NotebookModel>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
        }
        return null;
    }

    @Override
    public boolean alteraNotebook(NotebookModel noteModel) {
        listaNote = leNotebook();
        if (listaNote.contains(noteModel)) {
            listaNote.set(listaNote.indexOf(noteModel), noteModel);
            gravaNotebook(listaNote);
            return gravaNotebook(listaNote);
        } else {
            return false;
        }
    }
    
    @Override
    public boolean removeNotebook(NotebookModel notebook) {
        listaNote = leNotebook();
        if (listaNote.contains(notebook)) {
            listaNote.remove(notebook);
            gravaNotebook(listaNote);
            return gravaNotebook(listaNote);
        } else {
            return false;
        }
    }

}
