package INTERFACES;

import MODEL.ChamadosModel;
import java.util.List;

/**
 *
 * @author Rodrigo
 */
public interface ChamadosInterface {
    
    public boolean abreChamado (List<ChamadosModel> listaChamados);
    public List<ChamadosModel> listaChamado ();
    public boolean fechaChamado (ChamadosModel noteModel);
    public boolean removeChamado (ChamadosModel noteModel);
    
}
