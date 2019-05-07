/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import MODEL.ChamadosModel;
import MODEL.ClientesPFModel;
import MODEL.NotebookModel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rodrigo
 */
public class BdGeralDAO {

    private static final String BARRA = System.getProperty("file.separator");
    private static final String PASTA_USUARIO = System.getProperty("user.home");
    private static final String BASE_DADOS = PASTA_USUARIO + BARRA + "labprog" + BARRA + "rfassistencia";
    private static final String BASE_CHAMADOS = PASTA_USUARIO + BARRA + "labprog" + BARRA + "rfassistencia" + BARRA + "baseChamados.dat";
    private static final String BASE_CLIENTESPF = PASTA_USUARIO + BARRA + "labprog" + BARRA + "rfassistencia" + BARRA + "baseClientesPF.dat";
    private static final String BASE_NOTEBOOKS = PASTA_USUARIO + BARRA + "labprog" + BARRA + "rfassistencia" + BARRA + "baseNotebooks.dat";

    private static final List<ClientesPFModel> listaClientesPF = new ArrayList<>();
    private static final List<NotebookModel> listaNote = new ArrayList<>();
    private static final List<ChamadosModel> listaChamModel = new ArrayList<>();

    //Essa classe faz algumas verificações nas base de dados para previnir crash do programa
    

    public boolean verificaCaminhoGeral() {
        File bdGeral = new File(BASE_DADOS);
        if (!bdGeral.exists()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     *
     * @return Esse método retorna TRUE quando o programa acha os arquivos
     * baseClientesPF.dat no sistema de arquivos.
     */
    public boolean verificaBaseClientesPF() {
        File bdCliPf = new File(BASE_CLIENTESPF);
        if (!bdCliPf.exists()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     *
     * @return Esse método retorna TRUE quando o programa acha os arquivos
     * baseChamados.dat no sistema de arquivos.
     */
    public boolean verificaBaseNotebook() {
        File bdNotebook = new File(BASE_NOTEBOOKS);
        if (!bdNotebook.exists()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     *
     * @return Esse método retorna TRUE quando o programa acha os arquivos
     * baseNotebooks.dat no sistema de arquivos.
     */
    public boolean verificaBaseChamados() {
        File bdChamados = new File(BASE_CHAMADOS);
        if (!bdChamados.exists()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Os 3 Métodos que "zera o banco de dados", gravando no arquivo uma lista
     * vazia
     */
    public void gravaListaBaseClientesPF() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BASE_CLIENTESPF))) {
            oos.writeObject(listaClientesPF);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void gravaListaBaseNotebook() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BASE_NOTEBOOKS))) {
            oos.writeObject(listaNote);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void gravaListaBaseChamados() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BASE_CHAMADOS))) {
            oos.writeObject(listaChamModel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    * Os 3 métodos a seguir criam o arquivo quando o mesmo não é encontrado no sistema de arquivos
     */
    public void criaCaminhoBaseGeral() {
        File bdGeral = new File(BASE_DADOS);
        if (!bdGeral.exists()) {
            bdGeral.mkdirs();
        }
    }

    public void criaArquivoBaseClientesPF() {
        File bdCliPf = new File(BASE_CLIENTESPF);
        if (!bdCliPf.exists()) {
            try {
                bdCliPf.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(ClientesPFDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void criaArquivoBaseNotebook() {
        File bdNotebook = new File(BASE_NOTEBOOKS);
        if (!bdNotebook.exists()) {
            try {
                bdNotebook.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(NotebookDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void criaArquivoBaseChamados() {
        File bdchamados = new File(BASE_CHAMADOS);
        if (!bdchamados.exists()) {
            try {
                bdchamados.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(ChamadosDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
