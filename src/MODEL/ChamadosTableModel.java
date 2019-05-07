package MODEL;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Rodrigo
 */
public class ChamadosTableModel extends AbstractTableModel {

    private List<ChamadosModel> dados = new ArrayList<>();
    private String[] colunas = {"Número", "Cliente", "Eqipamento", "Defeito Relatado", "Defeito Constatado", "Solução", "Status"};
    
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
                return dados.get(linha).getNumero();
            case 1:
                return dados.get(linha).getCliente();
            case 2:
                return dados.get(linha).getEquipamento();
            case 3:
                return dados.get(linha).getDefeitoRelatado();
            case 4:
                return dados.get(linha).getDefeitoConstatado();
            case 5:
                return dados.get(linha).getSolucao();
            case 6:
                return dados.get(linha).getStatus();
            default:
                return null;
        }
    }

    @Override
    public void setValueAt(Object valor, int linha, int coluna) {
        switch (coluna) {
            case 0:
                dados.get(linha).setNumero((int) valor);
                break;
            case 1:
                dados.get(linha).setCliente((String) valor);
                break;
            case 2:
                dados.get(linha).setEquipamento((String) valor);
                break;
            case 3:
                dados.get(linha).setDefeitoRelatado((String) valor);
                break;
            case 4:
                dados.get(linha).setDefeitoConstatado((String) valor);
                break;
            case 5:
                dados.get(linha).setSolucao((String) valor);
                break;
            case 6:
                dados.get(linha).setStatus((String) valor);
                break;
        }
        this.fireTableRowsUpdated(linha, linha);
    }

    public void addRow(ChamadosModel chamadosModel) {
        this.dados.add(chamadosModel);
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
