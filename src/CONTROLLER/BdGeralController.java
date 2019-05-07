/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLLER;

import DAO.BdGeralDAO;
import DAO.ChamadosDAO;
import DAO.ClientesPFDAO;
import DAO.NotebookDAO;
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
public class BdGeralController {

    private static final String BARRA = System.getProperty("file.separator");
    private static final String PASTA_USUARIO = System.getProperty("user.home");
    private static final String BASE_DADOS = PASTA_USUARIO + BARRA + "labprog" + BARRA + "rfassistencia";
    private static final String BASE_CHAMADOS = PASTA_USUARIO + BARRA + "labprog" + BARRA + "rfassistencia" + BARRA + "baseChamados.dat";
    private static final String BASE_CLIENTESPF = PASTA_USUARIO + BARRA + "labprog" + BARRA + "rfassistencia" + BARRA + "baseCClientesPF.dat";
    private static final String BASE_NOTEBOOKS = PASTA_USUARIO + BARRA + "labprog" + BARRA + "rfassistencia" + BARRA + "baseNotebooks.dat";

    private static final ClientesPFDAO cliPfDao = new ClientesPFDAO();
    private static final NotebookDAO noteDao = new NotebookDAO();
    private static final ChamadosDAO chamfDao = new ChamadosDAO();
    private static final BdGeralDAO bdGeralDao = new BdGeralDAO();
    
    private static final List<ClientesPFModel> listaClientesPF = new ArrayList<>();
    private static final List<NotebookModel> listaNote = new ArrayList<>();
    private static final List<ChamadosModel> listaChamModel = new ArrayList<>();

    //Essa classe faz algumas verificações nas base de dados para previnir crash do programa
    
    
    public boolean verificaCaminhoGeral() {
        return bdGeralDao.verificaCaminhoGeral();
    }
    
    /**
     *
     * @return Esse método retorna TRUE quando o programa acha os arquivos
     * baseClientesPF.dat no sistema de arquivos.
     */
    public boolean verificaBaseClientesPF() {
        return bdGeralDao.verificaBaseClientesPF();
    }

    /**
     *
     * @return Esse método retorna TRUE quando o programa acha os arquivos
     * baseChamados.dat no sistema de arquivos.
     */
    public boolean verificaBaseNotebook() {
        return bdGeralDao.verificaBaseNotebook();
    }

    /**
     *
     * @return Esse método retorna TRUE quando o programa acha os arquivos
     * baseNotebooks.dat no sistema de arquivos.
     */
    public boolean verificaBaseChamados() {
        return bdGeralDao.verificaBaseChamados();
    }

    /**
     * Os 3 Métodos que "zera o banco de dados", gravando no arquivo uma lista
     * vazia
     */
    public void gravaListaBaseClientesPF() {
        bdGeralDao.gravaListaBaseClientesPF();
    }

    public void gravaListaBaseNotebook() {
        bdGeralDao.gravaListaBaseNotebook();
    }

    public void gravaListaBaseChamados() {
        bdGeralDao.gravaListaBaseChamados();
    }

    /*
    * Os 4 métodos a seguir criam o arquivo quando o mesmo não é encontrado no sistema de arquivos
     */
    
    public void criaCaminhoBaseGeral() {
        bdGeralDao.criaCaminhoBaseGeral();
    }
    
    public void criaArquivoBaseClientesPF() {
        bdGeralDao.criaArquivoBaseClientesPF();
    }

    public void criaArquivoBaseNotebook() {
        bdGeralDao.criaArquivoBaseNotebook();
    }

    public void criaArquivoBaseChamados() {
        bdGeralDao.criaArquivoBaseChamados();
    }

}
