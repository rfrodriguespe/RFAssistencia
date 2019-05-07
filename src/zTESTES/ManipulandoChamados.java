/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zTESTES;

import DAO.ChamadosDAO;
import MODEL.ChamadosModel;
import java.util.List;

/**
 *
 * @author Rodrigo
 */
public class ManipulandoChamados {
    
    public static void main(String[] args) {
        
        ChamadosDAO chamDao = new ChamadosDAO();
        List<ChamadosModel> meusChamados = chamDao.listaChamado();
        
        meusChamados.get(0).setStatus("ABERTO");
        meusChamados.get(0).setDefeitoConstatado("");
        meusChamados.get(0).setSolucao("");
        
        meusChamados.get(1).setStatus("ABERTO");
        meusChamados.get(1).setDefeitoConstatado("");
        meusChamados.get(1).setSolucao("");
        
        meusChamados.get(2).setStatus("ABERTO");
        meusChamados.get(2).setDefeitoConstatado("");
        meusChamados.get(2).setSolucao("");
        
        chamDao.abreChamado(meusChamados);
    }
    
}
