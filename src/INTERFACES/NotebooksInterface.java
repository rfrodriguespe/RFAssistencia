package INTERFACES;

import MODEL.NotebookModel;
import java.util.List;

/**
 *
 * @author Rodrigo
 */
public interface NotebooksInterface {
    
    public boolean gravaNotebook (List<NotebookModel> listaNote);
    public List<NotebookModel> leNotebook ();
    public boolean alteraNotebook (NotebookModel noteModel);
    public boolean removeNotebook (NotebookModel noteModel);
    
}
