package CONTROLLER;

import MODEL.NotebookModel;
import DAO.NotebookDAO;
import java.util.List;

public class NotebookController {

    private final NotebookModel notModel;
    private final NotebookDAO noteDao;
    private List<NotebookModel> minhaListaNote;

    public NotebookController() {
        
        notModel = new NotebookModel();
        noteDao = new NotebookDAO();
    }

    public boolean gravaNotebook(List<NotebookModel> listaNote) {
        return noteDao.gravaNotebook(listaNote);
    }

    public List<NotebookModel> leNotebook() {
        return minhaListaNote = noteDao.leNotebook();
    }

    public boolean alteranotebook(NotebookModel notebook) {
        return noteDao.alteraNotebook(notebook);
    }

    public boolean removeNotebook(NotebookModel notebook) {
        return noteDao.removeNotebook(notebook);
    }

}