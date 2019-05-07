package MODEL;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Rodrigo
 */
public class NotebooksTableModel extends AbstractTableModel {

    private List<NotebookModel> dados = new ArrayList<>();
    private String[] colunas = {"Dono", "Marca", "Modelo", "Serial", "Tela", "Processador", "Memória", "Armazenamento"};
    
    @Override
    public String getColumnName(int col) {
        return colunas[col];
    }

    @Override
    public int getRowCount() {
        return dados.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public Object getValueAt(int linha, int coluna) {
        switch (coluna) {
            case 0:
                return dados.get(linha).getDono();
            case 1:
                return dados.get(linha).getMarca();
            case 2:
                return dados.get(linha).getModelo();
            case 3:
                return dados.get(linha).getSerial();
            case 4:
                return dados.get(linha).getTamTela();
            case 5:
                return dados.get(linha).getProc();
            case 6:
                return dados.get(linha).getRam();
            case 7:
                return dados.get(linha).getHd();
            default:
                return null;
        }
    }

    @Override
    public void setValueAt(Object valor, int linha, int coluna) {
        switch (coluna) {
            case 0:
                dados.get(linha).setDono((String) valor);
                break;
            case 1:
                dados.get(linha).setMarca((String) valor);
                break;
            case 2:
                dados.get(linha).setModelo((String) valor);
                break;
            case 3:
                dados.get(linha).setSerial((String) valor);
                break;
            case 4:
                dados.get(linha).setTamTela((String) valor);
                break;
            case 5:
                dados.get(linha).setProc((String) valor);
                break;
            case 6:
                dados.get(linha).setRam((String) valor);
                break;
            case 7:
                dados.get(linha).setHd((String) valor);
                break;
        }
        this.fireTableRowsUpdated(linha, linha);
    }

    public void addRow(NotebookModel noteModel) {
        this.dados.add(noteModel);
        this.fireTableDataChanged();
    }

    public void removeRow(int linha) {
        this.dados.remove(linha);
        this.fireTableRowsDeleted(linha, linha);
    }

    public void limpaTabela() {
        dados.clear();
        fireTableDataChanged();
    }

}
