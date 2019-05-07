package CONTROLLER;

import MODEL.ChamadosModel;
import DAO.ChamadosDAO;
import java.util.List;

public class ChamadosController {

    private final ChamadosModel chamModel;
    private final ChamadosDAO chamDao;
    private List<ChamadosModel> minhaListaChamados;

    public ChamadosController() {
        
        chamModel = new ChamadosModel();
        chamDao = new ChamadosDAO();
    }

    public boolean abreChamado(List<ChamadosModel> listaChamados) {
        return chamDao.abreChamado(listaChamados);
    }

    public List<ChamadosModel> listaChamado() {
        return minhaListaChamados = chamDao.listaChamado();
    }

    public boolean fechaChamado(ChamadosModel chammado) {
        return chamDao.fechaChamado(chammado);
    }

    public boolean removeChamado(ChamadosModel chamado) {
        return chamDao.removeChamado(chamado);
    }

}