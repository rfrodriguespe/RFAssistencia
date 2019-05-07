/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VIEW;

// Import do Controlador
import CONTROLLER.ChamadosController;
import CONTROLLER.ClientesPFController;
import CONTROLLER.NotebookController;
import MODEL.ChamadosModel;
import MODEL.ChamadosTableModel;

// Import dos modelos
import MODEL.ClientesPFModel;
import MODEL.NotebookModel;
import MODEL.TableColumnAdjuster;

// Import das bibliotecas da API iText para gerar PDF'z
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.File;

// Import das Exceções
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

// Import necessário para tabalhar com ArrayList
import java.util.List;
import javax.swing.JFileChooser;

// Import de componentes da interface gráfica
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Rodrigo Fereira Rodrigues
 */
public class ChamadosView extends javax.swing.JInternalFrame {

    /**
     * Creates new form ClientesPFView
     */
    public ChamadosView() {
        initComponents();
        jTableChamados.setModel(tableModel);
        preencheTabela();
        habilitaEncerramentoChamado(false);

    }

    //Instancia minha jTable com base no modelo de Tabela definido no pacote MODEL
    ChamadosTableModel tableModel = new ChamadosTableModel();

    private void habilitaEncerramentoChamado(boolean estado) {
        jTextAreaDefeitoConstatado.setVisible(estado);
        jTextAreaSolucao.setVisible(estado);
        jButtonEncerraChamado.setEnabled(estado);
        jButtonCancelaEncerramento.setEnabled(estado);
    }

    public JTextField[] limparCampos() {
        JTextField[] campos = {jTextFieldDono, jTextFieldNumeroChamado};
        for (int i = 0; i < campos.length; i++) {
            campos[i].setText("");
        }
        jTextAreaDefeitoRelatado.setText("");
        return campos;
    }

    public boolean verificaCamposEmBranco() {
        int erros = 0;
        if (jTextFieldDono.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Campo CPF DONO em branco, verifique");
            erros++;
        }
        if (jComboBoxEquipamentos.getSelectedItem().equals("Selecione um Equipamento")) {
            JOptionPane.showMessageDialog(this, "Selecione um euipamento");
            erros++;
        }
        if (jTextAreaDefeitoRelatado.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Campo Defeito Relatado em branco, verifique");
            erros++;
        }
        if (erros == 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean verificaCpfExistente(String cpf) {
        boolean jaTemCpf = false;
        ClientesPFController cliPfCtrl = new ClientesPFController();
        List<ClientesPFModel> listaClientesPF = cliPfCtrl.leClientesPf();
        for (ClientesPFModel cliPf : listaClientesPF) {
            if (cliPf.getCPF().equals(cpf)) {
                jaTemCpf = true;
                break;
            }
        }
        return jaTemCpf;
    }

    /**
     * Função que carrega a consulta dos clientes em um arquivo PDF
     */
    public void geraPdf(String caminho) {
        Document doc = new Document();
        try {
            doc.setPageSize(PageSize.A4.rotate());
            ChamadosController chamCtrl = new ChamadosController();
            List<ChamadosModel> listaChamados = chamCtrl.listaChamado();
            //PdfWriter.getInstance(doc, new FileOutputStream(BASE_RELATORIOS));
            PdfWriter.getInstance(doc, new FileOutputStream(caminho));
            doc.open();
            doc.addAuthor("RF Assistência");
            doc.addLanguage("PT-BR");
            doc.addTitle("Relatório de Chamados");
            //
            PdfPTable table = new PdfPTable(new float[]{0.1f, 0.1f, 0.1f, 0.2f, 0.2f, 0.2f, 0.1f});
            table.setWidthPercentage(100.0f);
            table.setHorizontalAlignment(Element.ALIGN_JUSTIFIED_ALL);
            doc.add(new Paragraph("RF Assistência Técnica - Relatórios\n"));
            doc.add(new Paragraph("\n"));
//            doc.add(new Paragraph("RF Assistência Técnica - Relatórios\n",FontFactory.getFont(FontFactory.COURIER, 5)));
//            doc.add(new Paragraph("\n"));
            String complemento = "";
            if (jRadioButtonChamadosAbertos.isSelected()) {
                complemento = "Abertos";
            } else if (jRadioButtonChamadosFechados.isSelected()) {
                complemento = "Fechados";
            }
            PdfPCell header = new PdfPCell(new Paragraph("Listagem de Chamados " + complemento));
            PdfPCell cellNumeroChamado = new PdfPCell();
            PdfPCell cellCliente = new PdfPCell();
            PdfPCell cellEquipamento = new PdfPCell();
            PdfPCell cellDefeitoRelatado = new PdfPCell();
            PdfPCell cellDefeitoConstatado = new PdfPCell();
            PdfPCell cellSolucao = new PdfPCell();
            PdfPCell cellStatus = new PdfPCell();
            header.setColspan(8);
            header.setBackgroundColor(BaseColor.GRAY);
            String[] cabecalho = {"Número do chamado", "Cliente", "Equipamento", "Defeito Relatado",
                "Defeito Constatado", "Solução", "Status do Chamado"};
            table.addCell(header);
            for (String nomes : cabecalho) {
                table.addCell(nomes);
            }
            // Fazer um for e preeencher a tabela
            for (int i = 0; i < listaChamados.size(); i++) {

                if (listaChamados.get(i).getStatus().equals("ABERTO") && jRadioButtonChamadosAbertos.isSelected()) {
                    table.addCell(String.valueOf(listaChamados.get(i).getNumero()));
                    table.addCell(listaChamados.get(i).getCliente());
                    table.addCell(listaChamados.get(i).getEquipamento());
                    table.addCell(listaChamados.get(i).getDefeitoRelatado());
                    table.addCell(listaChamados.get(i).getDefeitoConstatado());
                    table.addCell(listaChamados.get(i).getSolucao());
                    table.addCell(listaChamados.get(i).getStatus());
                } else if (listaChamados.get(i).getStatus().equals("FECHADO") && jRadioButtonChamadosFechados.isSelected()) {
                    table.addCell(String.valueOf(listaChamados.get(i).getNumero()));
                    table.addCell(listaChamados.get(i).getCliente());
                    table.addCell(listaChamados.get(i).getEquipamento());
                    table.addCell(listaChamados.get(i).getDefeitoRelatado());
                    table.addCell(listaChamados.get(i).getDefeitoConstatado());
                    table.addCell(listaChamados.get(i).getSolucao());
                    table.addCell(listaChamados.get(i).getStatus());
                } else if (jRadioButtonTodosChamados.isSelected()) {
                    table.addCell(String.valueOf(listaChamados.get(i).getNumero()));
                    table.addCell(listaChamados.get(i).getCliente());
                    table.addCell(listaChamados.get(i).getEquipamento());
                    table.addCell(listaChamados.get(i).getDefeitoRelatado());
                    table.addCell(listaChamados.get(i).getDefeitoConstatado());
                    table.addCell(listaChamados.get(i).getSolucao());
                    table.addCell(listaChamados.get(i).getStatus());
                }

            }
            //
            doc.add(table);
            //

            // Adiciona rodapé com a data e hora atuais
            Date dia = GregorianCalendar.getInstance().getTime();
            SimpleDateFormat formatadoData = new SimpleDateFormat("EEEEEE ',' dd ' de 'MMMM ' de ' yyyy. HH:mm:ss");
            doc.add(new Paragraph("\n\nRecife, " + formatadoData.format(dia)));
            //

            doc.close();
            Desktop.getDesktop().open(new File(caminho));
        } catch (IOException | DocumentException ex) {
            Logger.getLogger(ChamadosView.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void preencheTabela() {
        ChamadosController chamCtrl = new ChamadosController();
        tableModel.limpaTabela();
        for (ChamadosModel chamados : chamCtrl.listaChamado()) {
            if (jRadioButtonChamadosAbertos.isSelected() && chamados.getStatus().equals("ABERTO")) {
                tableModel.addRow(chamados);
            } else if (jRadioButtonChamadosFechados.isSelected() && chamados.getStatus().equals("FECHADO")) {
                tableModel.addRow(chamados);
            } else if (jRadioButtonTodosChamados.isSelected()) {
                tableModel.addRow(chamados);
            }
        }
        jTableChamados.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumnAdjuster tca = new TableColumnAdjuster(jTableChamados);
        tca.adjustColumns();

    }

    public boolean cpfValido() {
        boolean cpfValido = UTIL.ValidaCampos.validaCpf(jTextFieldDono.getText());
        if (!cpfValido) {
            JOptionPane.showMessageDialog(this, "CPF digitado não é válido");
            return false;
        }
        return true;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroupStatusChamados = new javax.swing.ButtonGroup();
        jPanelDadosPessoais = new javax.swing.JPanel();
        jTextFieldDono = new javax.swing.JTextField();
        jLabelCPF = new javax.swing.JLabel();
        jLabelNome = new javax.swing.JLabel();
        jButtonConfirmaCliente = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jComboBoxEquipamentos = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldNumeroChamado = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaDefeitoRelatado = new javax.swing.JTextArea();
        jPanelTabela = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableChamados = new javax.swing.JTable();
        jPanelBotoes = new javax.swing.JPanel();
        jButtonConsultaChamados = new javax.swing.JButton();
        jButtonCancelaChamado = new javax.swing.JButton();
        jButtonFecharChamados = new javax.swing.JButton();
        jButtonLimpar = new javax.swing.JButton();
        jButtonAbreChamado = new javax.swing.JButton();
        jButtonVerificaPendencias = new javax.swing.JButton();
        jButtonNovo = new javax.swing.JButton();
        jButtonGeraPdf = new javax.swing.JButton();
        jRadioButtonChamadosAbertos = new javax.swing.JRadioButton();
        jRadioButtonChamadosFechados = new javax.swing.JRadioButton();
        jRadioButtonTodosChamados = new javax.swing.JRadioButton();
        jButtonCancela = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextAreaSolucao = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextAreaDefeitoConstatado = new javax.swing.JTextArea();
        jButtonEncerraChamado = new javax.swing.JButton();
        jButtonCancelaEncerramento = new javax.swing.JButton();

        setMaximizable(true);
        setPreferredSize(new java.awt.Dimension(787, 412));
        try {
            setSelected(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }

        jPanelDadosPessoais.setBorder(javax.swing.BorderFactory.createTitledBorder("Abertura de Chamados"));
        jPanelDadosPessoais.setPreferredSize(new java.awt.Dimension(787, 412));

        jTextFieldDono.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldDonoFocusLost(evt);
            }
        });

        jLabelCPF.setText("CPF DONO:");

        jLabelNome.setText("Defeito Relatado:");

        jButtonConfirmaCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/ticado24px.png"))); // NOI18N
        jButtonConfirmaCliente.setText("Confirma Cliente");
        jButtonConfirmaCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConfirmaClienteActionPerformed(evt);
            }
        });

        jLabel1.setText("Equipamentos Disponíveis");

        jComboBoxEquipamentos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione um Equipamento" }));

        jLabel2.setText("Número do chamado:");

        jTextFieldNumeroChamado.setEditable(false);

        jTextAreaDefeitoRelatado.setColumns(20);
        jTextAreaDefeitoRelatado.setLineWrap(true);
        jTextAreaDefeitoRelatado.setRows(5);
        jScrollPane1.setViewportView(jTextAreaDefeitoRelatado);

        javax.swing.GroupLayout jPanelDadosPessoaisLayout = new javax.swing.GroupLayout(jPanelDadosPessoais);
        jPanelDadosPessoais.setLayout(jPanelDadosPessoaisLayout);
        jPanelDadosPessoaisLayout.setHorizontalGroup(
            jPanelDadosPessoaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDadosPessoaisLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelDadosPessoaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelDadosPessoaisLayout.createSequentialGroup()
                        .addComponent(jLabelCPF)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldDono, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonConfirmaCliente))
                    .addGroup(jPanelDadosPessoaisLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxEquipamentos, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelDadosPessoaisLayout.createSequentialGroup()
                        .addComponent(jLabelNome)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelDadosPessoaisLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldNumeroChamado, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 27, Short.MAX_VALUE))
        );
        jPanelDadosPessoaisLayout.setVerticalGroup(
            jPanelDadosPessoaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelDadosPessoaisLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelDadosPessoaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextFieldNumeroChamado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelDadosPessoaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCPF)
                    .addComponent(jTextFieldDono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonConfirmaCliente))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelDadosPessoaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jComboBoxEquipamentos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanelDadosPessoaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelDadosPessoaisLayout.createSequentialGroup()
                        .addComponent(jLabelNome)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );

        jTableChamados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"01323197486", "Dell", "Vostro 5470", "abc45d", "14''", "Intel Core i7", "8 Gb"}
            },
            new String [] {
                "Número", "Cliente", "Equipamento", "Defeito Relatado", "Defeito Constatado", "Solução", "Status"
            }
        ));
        jTableChamados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableChamadosMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTableChamadosMouseReleased(evt);
            }
        });
        jTableChamados.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTableChamadosKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(jTableChamados);

        javax.swing.GroupLayout jPanelTabelaLayout = new javax.swing.GroupLayout(jPanelTabela);
        jPanelTabela.setLayout(jPanelTabelaLayout);
        jPanelTabelaLayout.setHorizontalGroup(
            jPanelTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTabelaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1410, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelTabelaLayout.setVerticalGroup(
            jPanelTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTabelaLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 26, Short.MAX_VALUE))
        );

        jButtonConsultaChamados.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/zoom.png"))); // NOI18N
        jButtonConsultaChamados.setText("Consulta Chamados");
        jButtonConsultaChamados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConsultaChamadosActionPerformed(evt);
            }
        });

        jButtonCancelaChamado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/remove.png"))); // NOI18N
        jButtonCancelaChamado.setLabel("Cancelar Chamado");
        jButtonCancelaChamado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelaChamadoActionPerformed(evt);
            }
        });

        jButtonFecharChamados.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/edit.png"))); // NOI18N
        jButtonFecharChamados.setText("Fechar Chamados");
        jButtonFecharChamados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFecharChamadosActionPerformed(evt);
            }
        });

        jButtonLimpar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/erase.png"))); // NOI18N
        jButtonLimpar.setText("Limpar Campos");
        jButtonLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLimparActionPerformed(evt);
            }
        });

        jButtonAbreChamado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/salvar.png"))); // NOI18N
        jButtonAbreChamado.setText("Abre Chamado");
        jButtonAbreChamado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAbreChamadoActionPerformed(evt);
            }
        });

        jButtonVerificaPendencias.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/ticado24px.png"))); // NOI18N
        jButtonVerificaPendencias.setText("Verifica Pendências");
        jButtonVerificaPendencias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVerificaPendenciasActionPerformed(evt);
            }
        });

        jButtonNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/novo.png"))); // NOI18N
        jButtonNovo.setText("Novo Chamado");
        jButtonNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNovoActionPerformed(evt);
            }
        });

        jButtonGeraPdf.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/pdfIcon24px.png"))); // NOI18N
        jButtonGeraPdf.setText("Gerar PDF");
        jButtonGeraPdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGeraPdfActionPerformed(evt);
            }
        });

        buttonGroupStatusChamados.add(jRadioButtonChamadosAbertos);
        jRadioButtonChamadosAbertos.setText("Abertos");
        jRadioButtonChamadosAbertos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonChamadosAbertosActionPerformed(evt);
            }
        });

        buttonGroupStatusChamados.add(jRadioButtonChamadosFechados);
        jRadioButtonChamadosFechados.setText("Fechados");

        buttonGroupStatusChamados.add(jRadioButtonTodosChamados);
        jRadioButtonTodosChamados.setSelected(true);
        jRadioButtonTodosChamados.setText("Todos");

        jButtonCancela.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/erase.png"))); // NOI18N
        jButtonCancela.setText("Cancelar");

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Fechar.png"))); // NOI18N
        jButton1.setText("Fecha janela");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelBotoesLayout = new javax.swing.GroupLayout(jPanelBotoes);
        jPanelBotoes.setLayout(jPanelBotoesLayout);
        jPanelBotoesLayout.setHorizontalGroup(
            jPanelBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBotoesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelBotoesLayout.createSequentialGroup()
                        .addComponent(jButtonConsultaChamados, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonGeraPdf, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanelBotoesLayout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(jRadioButtonTodosChamados)
                        .addGap(18, 18, 18)
                        .addComponent(jRadioButtonChamadosAbertos)
                        .addGap(18, 18, 18)
                        .addComponent(jRadioButtonChamadosFechados))
                    .addGroup(jPanelBotoesLayout.createSequentialGroup()
                        .addGroup(jPanelBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButtonNovo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonFecharChamados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonAbreChamado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonCancela, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanelBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jButtonLimpar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButtonCancelaChamado, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jButtonVerificaPendencias, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(0, 20, Short.MAX_VALUE))
        );
        jPanelBotoesLayout.setVerticalGroup(
            jPanelBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBotoesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelBotoesLayout.createSequentialGroup()
                        .addComponent(jButtonLimpar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonCancelaChamado)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonVerificaPendencias))
                    .addGroup(jPanelBotoesLayout.createSequentialGroup()
                        .addComponent(jButtonNovo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonAbreChamado)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonFecharChamados)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonCancela)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanelBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonConsultaChamados, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonGeraPdf))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButtonTodosChamados)
                    .addComponent(jRadioButtonChamadosAbertos)
                    .addComponent(jRadioButtonChamadosFechados))
                .addContainerGap())
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Encerramento de Chamados"));

        jLabel3.setText("Defeito Constatado:");

        jTextAreaSolucao.setColumns(20);
        jTextAreaSolucao.setLineWrap(true);
        jTextAreaSolucao.setRows(5);
        jScrollPane3.setViewportView(jTextAreaSolucao);

        jLabel4.setText("Solução:");

        jTextAreaDefeitoConstatado.setColumns(20);
        jTextAreaDefeitoConstatado.setLineWrap(true);
        jTextAreaDefeitoConstatado.setRows(5);
        jScrollPane4.setViewportView(jTextAreaDefeitoConstatado);

        jButtonEncerraChamado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/ticado24px.png"))); // NOI18N
        jButtonEncerraChamado.setText("Encerrar Chamado");
        jButtonEncerraChamado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEncerraChamadoActionPerformed(evt);
            }
        });

        jButtonCancelaEncerramento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/erase.png"))); // NOI18N
        jButtonCancelaEncerramento.setText("Cancelar");
        jButtonCancelaEncerramento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelaEncerramentoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButtonEncerraChamado)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonCancelaEncerramento)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane3)
                    .addComponent(jScrollPane4))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonEncerraChamado)
                    .addComponent(jButtonCancelaEncerramento))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanelTabela, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanelDadosPessoais, javax.swing.GroupLayout.PREFERRED_SIZE, 498, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelBotoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(171, 171, 171))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanelBotoes, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanelDadosPessoais, javax.swing.GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jPanelTabela, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanelTabela.getAccessibleContext().setAccessibleName("Dados Pessoais");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCancelaChamadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelaChamadoActionPerformed
        if (jTableChamados.getSelectedRow() != -1) {
            ChamadosModel chamModel = new ChamadosModel();
            chamModel.setNumero(Integer.parseInt(jTextFieldNumeroChamado.getText()));
            ChamadosController chamCtrl = new ChamadosController();

            int resposta = JOptionPane.showConfirmDialog(null, "Deseja realmente excluir o chamado?", "Excluir Chamado", JOptionPane.YES_NO_OPTION);

            if (resposta == JOptionPane.YES_OPTION) {
                if (chamCtrl.removeChamado(chamModel)) {
                JOptionPane.showMessageDialog(this, "Chamado Cancelado com sucesso");
                preencheTabela();
                limparCampos();
            }
            } else if (resposta == JOptionPane.NO_OPTION) {
                JOptionPane.showMessageDialog(this, "Ação Cancelada");
            }
            
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Chamado para excluir");
        }

    }//GEN-LAST:event_jButtonCancelaChamadoActionPerformed

    private void jButtonConsultaChamadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConsultaChamadosActionPerformed
        preencheTabela();
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Banco de dados vazio");
        }
    }//GEN-LAST:event_jButtonConsultaChamadosActionPerformed

    private void jButtonLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLimparActionPerformed
        // Limpa os campos
        limparCampos();
    }//GEN-LAST:event_jButtonLimparActionPerformed

    private void jButtonAbreChamadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAbreChamadoActionPerformed
        // Método para acionar o CONTROLLER que jogará o objeto pro DAO salvar
        NotebookController noteCtrl = new NotebookController();
        NotebookModel noteModel = new NotebookModel();
        ClientesPFController cliPfCtrl = new ClientesPFController();
        ClientesPFModel cliPfModel = new ClientesPFModel();

        ChamadosController chamCtrl = new ChamadosController();
        String email = "";
        if (verificaCamposEmBranco()) {

            List<ChamadosModel> listaChamados = chamCtrl.listaChamado();
            ChamadosModel chamModel = new ChamadosModel(jTextFieldDono.getText(), jComboBoxEquipamentos.getSelectedItem().toString(),
                    jTextAreaDefeitoRelatado.getText(), "", "", "ABERTO");

            if (listaChamados.size() != 0) {
                int ultimaId = listaChamados.get(listaChamados.size() - 1).getNumero();
                chamModel.setNumero(++ultimaId);
            }

            listaChamados.add(chamModel);

            chamCtrl.abreChamado(listaChamados);

            String dadosEmail = "DADOS DO CHAMADO: " + "\n"
                    + "CPF DONO: " + jTextFieldDono.getText() + "\n"
                    + "Número do Chamado: " + chamModel.getNumero() + "\n"
                    + "Equipamento: " + jComboBoxEquipamentos.getSelectedItem() + "\n"
                    + "Defeito Relatado: " + jTextAreaDefeitoRelatado.getText() + "\n";

            if (chamCtrl.abreChamado(listaChamados)) {

                List<ClientesPFModel> listaClientesPF = cliPfCtrl.leClientesPf();
                for (ClientesPFModel cliPf : listaClientesPF) {
                    if (cliPf.getCPF().equals(jTextFieldDono.getText())) {
                        email = cliPf.getEmail();
                    }
                }

                JOptionPane.showMessageDialog(this, "Chamado Aberto com Sucesso");
                JOptionPane.showMessageDialog(this, "Email enviado para " + email);
                UTIL.EnviaEmail.enviaEmailCadastro("abertura", email, String.valueOf(chamModel.getNumero()), dadosEmail);
                limparCampos();
                preencheTabela();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao Abrir o chamado");
            }
        }
    }//GEN-LAST:event_jButtonAbreChamadoActionPerformed

    private void jButtonFecharChamadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFecharChamadosActionPerformed
        if (jTableChamados.getSelectedRow() != -1) {
            if (jTableChamados.getValueAt(jTableChamados.getSelectedRow(), 6).toString().equals("ABERTO")) {
                habilitaEncerramentoChamado(true);
            } else {
                JOptionPane.showMessageDialog(this, "Chamado já encerrado!");
            }

        } else {
            JOptionPane.showMessageDialog(this, "Selecione um chamado para fechar!");
        }


    }//GEN-LAST:event_jButtonFecharChamadosActionPerformed

    private void jButtonNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNovoActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jButtonNovoActionPerformed

    private void jButtonGeraPdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGeraPdfActionPerformed
        // TODO add your handling code here:
        JFileChooser relatorio = new JFileChooser();
        relatorio.setDialogTitle("Salvar Relatório");
        relatorio.setFileSelectionMode(JFileChooser.FILES_ONLY);
        relatorio.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Arquivo PDF", ".pdf");
        relatorio.setFileFilter(filter);
        int resultado = relatorio.showSaveDialog(this);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            File file = relatorio.getSelectedFile();
            if (file.exists()) {
                int selecionaOpcao = JOptionPane.showConfirmDialog(null,
                        " O arquivo já existe, deseja sobrescreve-lo? ", null,
                        JOptionPane.OK_CANCEL_OPTION);
                if (selecionaOpcao == JOptionPane.OK_OPTION) {
                    geraPdf(file.getPath() + ".pdf");
                } else if (selecionaOpcao == JOptionPane.CANCEL_OPTION) {
                    JOptionPane.showMessageDialog(this, "Ação Cancelada");
                }
            } else {
                geraPdf(file.getPath() + ".pdf");
            }
        } else if (resultado == JFileChooser.CANCEL_OPTION) {
            JOptionPane.showMessageDialog(this, "Ação Cancelada");
        }

    }//GEN-LAST:event_jButtonGeraPdfActionPerformed

    private void jTableChamadosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableChamadosMouseClicked
        // TODO add your handling code here:
        if (jTableChamados.getSelectedRow() != -1) {
            jComboBoxEquipamentos.removeAllItems();
            jTextFieldNumeroChamado.setText(jTableChamados.getValueAt(jTableChamados.getSelectedRow(), 0).toString());
            jTextFieldDono.setText(jTableChamados.getValueAt(jTableChamados.getSelectedRow(), 1).toString());
            jComboBoxEquipamentos.addItem(jTableChamados.getValueAt(jTableChamados.getSelectedRow(), 2).toString());
            jTextAreaDefeitoRelatado.setText(jTableChamados.getValueAt(jTableChamados.getSelectedRow(), 3).toString());
        }
    }//GEN-LAST:event_jTableChamadosMouseClicked

    private void jTableChamadosMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableChamadosMouseReleased
        // TODO add your handling code here:
        if (jTableChamados.getSelectedRow() != -1) {
            jComboBoxEquipamentos.removeAllItems();
            jTextFieldNumeroChamado.setText(jTableChamados.getValueAt(jTableChamados.getSelectedRow(), 0).toString());
            jTextFieldDono.setText(jTableChamados.getValueAt(jTableChamados.getSelectedRow(), 1).toString());
            jComboBoxEquipamentos.addItem(jTableChamados.getValueAt(jTableChamados.getSelectedRow(), 2).toString());
            jTextAreaDefeitoRelatado.setText(jTableChamados.getValueAt(jTableChamados.getSelectedRow(), 3).toString());
        }
    }//GEN-LAST:event_jTableChamadosMouseReleased

    private void jTableChamadosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableChamadosKeyReleased
        // TODO add your handling code here:
        if (jTableChamados.getSelectedRow() != -1) {
            jComboBoxEquipamentos.removeAllItems();
            jTextFieldNumeroChamado.setText(jTableChamados.getValueAt(jTableChamados.getSelectedRow(), 0).toString());
            jTextFieldDono.setText(jTableChamados.getValueAt(jTableChamados.getSelectedRow(), 1).toString());
            jComboBoxEquipamentos.addItem(jTableChamados.getValueAt(jTableChamados.getSelectedRow(), 2).toString());
            jTextAreaDefeitoRelatado.setText(jTableChamados.getValueAt(jTableChamados.getSelectedRow(), 3).toString());
        }
    }//GEN-LAST:event_jTableChamadosKeyReleased

    private void jTextFieldDonoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldDonoFocusLost
        // TODO add your handling code here:
        if (!cpfValido()) {
            jTextFieldDono.setText("");
        }
        if (!verificaCpfExistente(jTextFieldDono.getText())) {
            JOptionPane.showMessageDialog(this, "CPF '" + jTextFieldDono.getText() + "' não existe no cadastro");
            jTextFieldDono.setText("");
        }

    }//GEN-LAST:event_jTextFieldDonoFocusLost

    private void jButtonVerificaPendenciasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVerificaPendenciasActionPerformed
        // TODO add your handling code here:
        if (verificaCamposEmBranco()) {
            JOptionPane.showMessageDialog(this, "Nenhuma Pendência encontrada");
        }
    }//GEN-LAST:event_jButtonVerificaPendenciasActionPerformed

    private void jButtonConfirmaClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConfirmaClienteActionPerformed
        // TODO add your handling code here:
        ClientesPFController cliPfCtrl = new ClientesPFController();
        List<ClientesPFModel> listaClientesPF = cliPfCtrl.leClientesPf();
        ClientesPFModel cliSelecionado = null;
        if (!cpfValido()) {
            jTextFieldDono.setText("");
        }
        if (cpfValido() && !verificaCpfExistente(jTextFieldDono.getText())) {
            JOptionPane.showMessageDialog(this, "CPF '" + jTextFieldDono.getText() + "' não existe no cadastro");
            jTextFieldDono.setText("");
        } else {

            for (ClientesPFModel cliPf : listaClientesPF) {
                if (cliPf.getCPF().equals(jTextFieldDono.getText())) {
                    int selecionaOpcao = JOptionPane.showConfirmDialog(null,
                            "O nome do cliente é: " + cliPf.getNome() + " ?", null,
                            JOptionPane.OK_CANCEL_OPTION);
                    cliSelecionado = cliPf;
                    if (selecionaOpcao == JOptionPane.CANCEL_OPTION) {
                        jTextFieldDono.setText("");
                    } else {
                        jTextAreaDefeitoRelatado.requestFocus();
                    }
                }
            }
        }

        //Lista os equipamentos na ComboBox
        NotebookController noteCtrl = new NotebookController();
        jComboBoxEquipamentos.removeAllItems();
        jComboBoxEquipamentos.addItem("Selecione um Equipamento");
        for (NotebookModel noteModel : noteCtrl.leNotebook()) {
            if (noteModel.getDono().equals(jTextFieldDono.getText())) {
                jComboBoxEquipamentos.addItem(noteModel.getSerial());
            }
        }
        jTextAreaDefeitoRelatado.setVisible(true);
        if (jComboBoxEquipamentos.getItemCount() == 1) {
            JOptionPane.showMessageDialog(this, "O CLiente " + cliSelecionado.getNome() + " não tem nenhum equipamento cadastrado!");
            jTextAreaDefeitoRelatado.setVisible(false);
        }
        //Fim da Listagem
    }//GEN-LAST:event_jButtonConfirmaClienteActionPerformed

    private void jRadioButtonChamadosAbertosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonChamadosAbertosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButtonChamadosAbertosActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButtonEncerraChamadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEncerraChamadoActionPerformed
        if (jTableChamados.getSelectedRow() != -1) {
            ChamadosController chamCtrl = new ChamadosController();
            ClientesPFController cliPfCtrl = new ClientesPFController();
            ChamadosModel chamModel = new ChamadosModel();
            chamModel.setNumero(Integer.parseInt(jTextFieldNumeroChamado.getText()));
            chamModel.setCliente(jTextFieldDono.getText());
            chamModel.setEquipamento(jComboBoxEquipamentos.getSelectedItem().toString());
            chamModel.setDefeitoRelatado(jTextAreaDefeitoRelatado.getText());
            chamModel.setDefeitoConstatado(jTextAreaDefeitoConstatado.getText());
            chamModel.setSolucao(jTextAreaSolucao.getText());
            chamModel.setStatus("FECHADO");
            //
            String email = "";
            List<ClientesPFModel> listaClientesPF = cliPfCtrl.leClientesPf();
            for (ClientesPFModel cliPf : listaClientesPF) {
                if (cliPf.getCPF().equals(jTextFieldDono.getText())) {
                    email = cliPf.getEmail();
                }
            }
            String dadosEmail = "DADOS DO CHAMADO: " + "\n"
                    + "CPF DONO: " + jTextFieldDono.getText() + "\n"
                    + "Número do Chamado: " + jTextFieldNumeroChamado.getText() + "\n"
                    + "Equipamento: " + jComboBoxEquipamentos.getSelectedItem() + "\n"
                    + "Defeito Relatado: " + jTextAreaDefeitoRelatado.getText() + "\n"
                    + "Defeito Constatado: " + jTextAreaDefeitoConstatado.getText() + "\n"
                    + "Defeito Solução: " + jTextAreaSolucao.getText() + "\n";

            //
            if (chamCtrl.fechaChamado(chamModel)) {
                JOptionPane.showMessageDialog(this, "Chamado Fechado com Sucesso");
                JOptionPane.showMessageDialog(this, "Email enviado para " + email);
                UTIL.EnviaEmail.enviaEmailCadastro("fechamento", email, jTextFieldNumeroChamado.getText(), dadosEmail);
                preencheTabela();
                limparCampos();
                habilitaEncerramentoChamado(false);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Chamado para fechar");
        }
    }//GEN-LAST:event_jButtonEncerraChamadoActionPerformed

    private void jButtonCancelaEncerramentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelaEncerramentoActionPerformed
        habilitaEncerramentoChamado(false);
    }//GEN-LAST:event_jButtonCancelaEncerramentoActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupStatusChamados;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonAbreChamado;
    private javax.swing.JButton jButtonCancela;
    private javax.swing.JButton jButtonCancelaChamado;
    private javax.swing.JButton jButtonCancelaEncerramento;
    private javax.swing.JButton jButtonConfirmaCliente;
    private javax.swing.JButton jButtonConsultaChamados;
    private javax.swing.JButton jButtonEncerraChamado;
    private javax.swing.JButton jButtonFecharChamados;
    private javax.swing.JButton jButtonGeraPdf;
    private javax.swing.JButton jButtonLimpar;
    private javax.swing.JButton jButtonNovo;
    private javax.swing.JButton jButtonVerificaPendencias;
    private javax.swing.JComboBox<Object> jComboBoxEquipamentos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabelCPF;
    private javax.swing.JLabel jLabelNome;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelBotoes;
    private javax.swing.JPanel jPanelDadosPessoais;
    private javax.swing.JPanel jPanelTabela;
    private javax.swing.JRadioButton jRadioButtonChamadosAbertos;
    private javax.swing.JRadioButton jRadioButtonChamadosFechados;
    private javax.swing.JRadioButton jRadioButtonTodosChamados;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTableChamados;
    private javax.swing.JTextArea jTextAreaDefeitoConstatado;
    private javax.swing.JTextArea jTextAreaDefeitoRelatado;
    private javax.swing.JTextArea jTextAreaSolucao;
    private javax.swing.JTextField jTextFieldDono;
    private javax.swing.JTextField jTextFieldNumeroChamado;
    // End of variables declaration//GEN-END:variables

}
