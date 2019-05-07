/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VIEW;

// Import do Controlador
import CONTROLLER.ClientesPFController;
import CONTROLLER.NotebookController;
import MODEL.ClientesPFModel;
import MODEL.EnderecoModel;

// Import dos modelos
import MODEL.NotebookModel;
import MODEL.NotebooksTableModel;
import MODEL.TableColumnAdjuster;

// Import das classes para trabalhar com o preenchimento do endereço a partir do CEP
import com.itextpdf.text.BaseColor;

// Import das bibliotecas da API iText para gerar PDF'z
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
public class NotebooksView extends javax.swing.JInternalFrame {

    /**
     * Creates new form ClientesPFView
     */
    public NotebooksView() {
        initComponents();
        jTableNotebook.setModel(tableModel);
        preencheTabela();
        habilitaBotoes(false);
        habilitaCampos(false);

    }

    //Instancia minha jTable com base no modelo de Tabela definido no pacote MODEL
    NotebooksTableModel tableModel = new NotebooksTableModel();
    
    private void habilitaCampos(boolean estado) {

        jTextFieldDono.setEnabled(estado);
        jTextFieldMarca.setEnabled(estado);
        jTextFieldModelo.setEnabled(estado);
        jTextFieldSerial.setEnabled(estado);
        jTextFieldTela.setEnabled(estado);
        jTextFieldMemoria.setEnabled(estado);
        jTextFieldProc.setEnabled(estado);
        jTextFieldArmazenamento.setEnabled(estado);
        

    }

    private void habilitaBotoes(boolean estado) {
        jButtonAlterar.setEnabled(estado);
        jButtonCancelar.setEnabled(estado);
        jButtonExcluir.setEnabled(estado);
        jButtonLimpar.setEnabled(estado);
        jButtonSalvar.setEnabled(estado);
        jButtonVerificaPendencias.setEnabled(estado);
        jButtonGeraPdf.setEnabled(estado);
        jButtonConfirmaCliente.setEnabled(estado);
    }

    public JTextField[] limparCampos() {
        JTextField[] campos = {jTextFieldDono, jTextFieldMarca, jTextFieldModelo, jTextFieldSerial,
            jTextFieldTela, jTextFieldProc, jTextFieldMemoria, jTextFieldArmazenamento};
        for (int i = 0; i < campos.length; i++) {
            campos[i].setText("");
        }
        return campos;
    }

    public boolean verificaCamposEmBranco() {
        int erros = 0;
        JTextField[] campos = {jTextFieldDono, jTextFieldMarca, jTextFieldModelo, jTextFieldSerial,
            jTextFieldTela, jTextFieldProc, jTextFieldMemoria, jTextFieldArmazenamento};
        String[] camposNome = {"Dono", "Marca", "Modelo", "Serial", "Tela", "Processador", "Memória", "Armazenamento"};
        for (int i = 0; i < campos.length; i++) {
            if (campos[i].getText().equals("")) {
                JOptionPane.showMessageDialog(this, "Campo " + camposNome[i] + " em branco, verifique");
                erros++;
            }
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
            NotebookController noteCtrl = new NotebookController();
            List<NotebookModel> minhaLista = noteCtrl.leNotebook();
            //PdfWriter.getInstance(doc, new FileOutputStream(BASE_RELATORIOS));
            PdfWriter.getInstance(doc, new FileOutputStream(caminho));
            doc.open();
            doc.addAuthor("RF Assistência");
            doc.addLanguage("PT-BR");
            doc.addTitle("Relatório de Clientes - Pessoa Física");
            //
            PdfPTable table = new PdfPTable(new float[]{0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f});
            table.setWidthPercentage(100.0f);
            table.setHorizontalAlignment(Element.ALIGN_JUSTIFIED_ALL);
            doc.add(new Paragraph("RF Assistência Técnica - Relatórios\n"));
            doc.add(new Paragraph("\n"));
//            doc.add(new Paragraph("RF Assistência Técnica - Relatórios\n",FontFactory.getFont(FontFactory.COURIER, 5)));
//            doc.add(new Paragraph("\n"));
            PdfPCell header = new PdfPCell(new Paragraph("Lista dos Notebooks registrados"));
            PdfPCell cellDono = new PdfPCell();
            PdfPCell cellMarca = new PdfPCell();
            PdfPCell cellModelo = new PdfPCell();
            PdfPCell cellSerial = new PdfPCell();
            PdfPCell cellTela = new PdfPCell();
            PdfPCell cellProcessador = new PdfPCell();
            PdfPCell cellRam = new PdfPCell();
            PdfPCell cellArmazenamento = new PdfPCell();
            header.setColspan(8);
            header.setBackgroundColor(BaseColor.GRAY);
            String[] cabecalho = {"Dono", "Marca", "Modelo", "Serial", "Tela", "Processador", "Memória", "Armazenamento"};
            table.addCell(header);
            for (String nomes : cabecalho) {
                table.addCell(nomes);
            }
            // Fazer um for e preeencher a tabela
            for (int i = 0; i < minhaLista.size(); i++) {

                table.addCell(minhaLista.get(i).getDono());
                table.addCell(minhaLista.get(i).getMarca());
                table.addCell(minhaLista.get(i).getModelo());
                table.addCell(minhaLista.get(i).getSerial());
                table.addCell(minhaLista.get(i).getTamTela());
                table.addCell(minhaLista.get(i).getProc());
                table.addCell(minhaLista.get(i).getRam());
                table.addCell(minhaLista.get(i).getHd());

            }
            //
            doc.add(table);
            //
            
            // Adiciona rodapé com a data e hora atuais
            Date dia = GregorianCalendar.getInstance().getTime();
            SimpleDateFormat formatadoData = new SimpleDateFormat("EEEEEE ',' dd ' de 'MMMM ' de ' yyyy. HH:mm:ss");
            doc.add(new Paragraph("\n\nRecife, "+formatadoData.format(dia)));
            //

            doc.close();
            Desktop.getDesktop().open(new File(caminho));
        } catch (IOException | DocumentException ex) {
            Logger.getLogger(NotebooksView.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void preencheTabela() {
        NotebookController noteCtrl = new NotebookController();
        tableModel.limpaTabela();
        for (NotebookModel note : noteCtrl.leNotebook()) {
            tableModel.addRow(note);
        }
        jTableNotebook.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumnAdjuster tca = new TableColumnAdjuster(jTableNotebook);
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

        jPanelDadosPessoais = new javax.swing.JPanel();
        jTextFieldDono = new javax.swing.JTextField();
        jTextFieldMarca = new javax.swing.JTextField();
        jTextFieldModelo = new javax.swing.JTextField();
        jLabelCPF = new javax.swing.JLabel();
        jLabelNome = new javax.swing.JLabel();
        jLabelEmail = new javax.swing.JLabel();
        jLabelEmail1 = new javax.swing.JLabel();
        jTextFieldSerial = new javax.swing.JTextField();
        jLabelEmail2 = new javax.swing.JLabel();
        jTextFieldTela = new javax.swing.JTextField();
        jLabelEmail3 = new javax.swing.JLabel();
        jTextFieldProc = new javax.swing.JTextField();
        jLabelEmail4 = new javax.swing.JLabel();
        jTextFieldMemoria = new javax.swing.JTextField();
        jLabelEmail5 = new javax.swing.JLabel();
        jTextFieldArmazenamento = new javax.swing.JTextField();
        jButtonConfirmaCliente = new javax.swing.JButton();
        jPanelTabela = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableNotebook = new javax.swing.JTable();
        jPanelBotoes = new javax.swing.JPanel();
        jButtonConsulta = new javax.swing.JButton();
        jButtonExcluir = new javax.swing.JButton();
        jButtonAlterar = new javax.swing.JButton();
        jButtonLimpar = new javax.swing.JButton();
        jButtonSalvar = new javax.swing.JButton();
        jButtonVerificaPendencias = new javax.swing.JButton();
        jButtonNovo = new javax.swing.JButton();
        jButtonCancelar = new javax.swing.JButton();
        jButtonGeraPdf = new javax.swing.JButton();
        jButtonFecharJanela = new javax.swing.JButton();

        setMaximizable(true);
        setPreferredSize(new java.awt.Dimension(787, 412));
        try {
            setSelected(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }

        jPanelDadosPessoais.setBorder(javax.swing.BorderFactory.createTitledBorder("Cadastro de Notebooks"));
        jPanelDadosPessoais.setPreferredSize(new java.awt.Dimension(787, 412));

        jTextFieldDono.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldDonoFocusLost(evt);
            }
        });

        jTextFieldMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldMarcaActionPerformed(evt);
            }
        });

        jLabelCPF.setText("CPF DONO:");

        jLabelNome.setText("Marca:");

        jLabelEmail.setText("Modelo:");

        jLabelEmail1.setText("Serial:");

        jLabelEmail2.setText("Tela");

        jLabelEmail3.setText("Processador");

        jLabelEmail4.setText("Memória");

        jLabelEmail5.setText("Armazenamento");

        jButtonConfirmaCliente.setText("Confirma Cliente");
        jButtonConfirmaCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConfirmaClienteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelDadosPessoaisLayout = new javax.swing.GroupLayout(jPanelDadosPessoais);
        jPanelDadosPessoais.setLayout(jPanelDadosPessoaisLayout);
        jPanelDadosPessoaisLayout.setHorizontalGroup(
            jPanelDadosPessoaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDadosPessoaisLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelDadosPessoaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelDadosPessoaisLayout.createSequentialGroup()
                        .addGroup(jPanelDadosPessoaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanelDadosPessoaisLayout.createSequentialGroup()
                                .addComponent(jLabelNome)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelDadosPessoaisLayout.createSequentialGroup()
                                .addComponent(jLabelCPF)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldDono)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelDadosPessoaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelDadosPessoaisLayout.createSequentialGroup()
                                .addComponent(jLabelEmail)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldModelo, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jButtonConfirmaCliente)))
                    .addGroup(jPanelDadosPessoaisLayout.createSequentialGroup()
                        .addComponent(jLabelEmail1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldSerial, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelEmail2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldTela, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelEmail4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldMemoria, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelDadosPessoaisLayout.createSequentialGroup()
                        .addComponent(jLabelEmail3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextFieldProc, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelEmail5, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldArmazenamento, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 27, Short.MAX_VALUE))
        );
        jPanelDadosPessoaisLayout.setVerticalGroup(
            jPanelDadosPessoaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDadosPessoaisLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelDadosPessoaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCPF)
                    .addComponent(jTextFieldDono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonConfirmaCliente))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelDadosPessoaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelNome)
                    .addComponent(jTextFieldMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldModelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelEmail))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelDadosPessoaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelEmail1)
                    .addComponent(jTextFieldSerial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldTela, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelEmail2)
                    .addComponent(jTextFieldMemoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelEmail4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelDadosPessoaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelEmail3)
                    .addComponent(jTextFieldProc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelEmail5)
                    .addComponent(jTextFieldArmazenamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(44, Short.MAX_VALUE))
        );

        jTableNotebook.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"01323197486", "Dell", "Vostro 5470", "abc45d", "14''", "Intel Core i7", "8 Gb", "500 Gb"}
            },
            new String [] {
                "Dono", "Marca", "Modelo", "Serial", "Tela", "Processador", "Memória", "Armazenamento"
            }
        ));
        jTableNotebook.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableNotebookMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTableNotebookMouseReleased(evt);
            }
        });
        jTableNotebook.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTableNotebookKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(jTableNotebook);

        javax.swing.GroupLayout jPanelTabelaLayout = new javax.swing.GroupLayout(jPanelTabela);
        jPanelTabela.setLayout(jPanelTabelaLayout);
        jPanelTabelaLayout.setHorizontalGroup(
            jPanelTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1430, Short.MAX_VALUE)
            .addGroup(jPanelTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelTabelaLayout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1410, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );
        jPanelTabelaLayout.setVerticalGroup(
            jPanelTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 435, Short.MAX_VALUE)
            .addGroup(jPanelTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelTabelaLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 413, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jButtonConsulta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/zoom.png"))); // NOI18N
        jButtonConsulta.setText("Consultar");
        jButtonConsulta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConsultaActionPerformed(evt);
            }
        });

        jButtonExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/remove.png"))); // NOI18N
        jButtonExcluir.setText("Excluir");
        jButtonExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExcluirActionPerformed(evt);
            }
        });

        jButtonAlterar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/edit.png"))); // NOI18N
        jButtonAlterar.setText("Alterar");
        jButtonAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAlterarActionPerformed(evt);
            }
        });

        jButtonLimpar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/erase.png"))); // NOI18N
        jButtonLimpar.setText("Limpar Campos");
        jButtonLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLimparActionPerformed(evt);
            }
        });

        jButtonSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/salvar.png"))); // NOI18N
        jButtonSalvar.setText("Salvar");
        jButtonSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSalvarActionPerformed(evt);
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
        jButtonNovo.setText("Novo");
        jButtonNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNovoActionPerformed(evt);
            }
        });

        jButtonCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/erase.png"))); // NOI18N
        jButtonCancelar.setText("Cancelar");
        jButtonCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelarActionPerformed(evt);
            }
        });

        jButtonGeraPdf.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/pdfIcon24px.png"))); // NOI18N
        jButtonGeraPdf.setText("Gerar PDF");
        jButtonGeraPdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGeraPdfActionPerformed(evt);
            }
        });

        jButtonFecharJanela.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Fechar.png"))); // NOI18N
        jButtonFecharJanela.setText("Fechar Janela");
        jButtonFecharJanela.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFecharJanelaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelBotoesLayout = new javax.swing.GroupLayout(jPanelBotoes);
        jPanelBotoes.setLayout(jPanelBotoesLayout);
        jPanelBotoesLayout.setHorizontalGroup(
            jPanelBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBotoesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonNovo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonAlterar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonExcluir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonVerificaPendencias, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonGeraPdf, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonLimpar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonConsulta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonSalvar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonFecharJanela, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(211, Short.MAX_VALUE))
        );
        jPanelBotoesLayout.setVerticalGroup(
            jPanelBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBotoesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonNovo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonConsulta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanelBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonCancelar)
                    .addComponent(jButtonAlterar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonExcluir)
                    .addComponent(jButtonLimpar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonVerificaPendencias)
                    .addComponent(jButtonSalvar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonGeraPdf)
                    .addComponent(jButtonFecharJanela))
                .addGap(7, 7, 7))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanelDadosPessoais, javax.swing.GroupLayout.PREFERRED_SIZE, 498, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanelBotoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanelTabela, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanelDadosPessoais, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanelBotoes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(29, 29, 29)
                .addComponent(jPanelTabela, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanelTabela.getAccessibleContext().setAccessibleName("Dados Pessoais");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExcluirActionPerformed
        if (jTableNotebook.getSelectedRow() != -1) {
            NotebookModel noteModel = new NotebookModel();
            noteModel.setDono(jTextFieldDono.getText());
            noteModel.setMarca(jTextFieldMarca.getText());
            noteModel.setModelo(jTextFieldModelo.getText());
            noteModel.setSerial(jTextFieldSerial.getText());
            noteModel.setTamTela(jTextFieldTela.getText());
            noteModel.setProc(jTextFieldProc.getText());
            noteModel.setRam(jTextFieldMemoria.getText());
            noteModel.setHd(jTextFieldArmazenamento.getText());
           
            NotebookController noteCtrl = new NotebookController();
            if (noteCtrl.removeNotebook(noteModel)) {
                JOptionPane.showMessageDialog(this, "Excluído Excluído com sucesso");
                preencheTabela();
                limparCampos();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Equipamento para excluir");
        }

    }//GEN-LAST:event_jButtonExcluirActionPerformed

    private void jButtonConsultaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConsultaActionPerformed
        // Acessando direto pelo Controller
        preencheTabela();
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Banco de dados vazio");
        }
        
    }//GEN-LAST:event_jButtonConsultaActionPerformed

    private void jTextFieldMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldMarcaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldMarcaActionPerformed

    private void jButtonLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLimparActionPerformed
        // Limpa os campos
        limparCampos();
    }//GEN-LAST:event_jButtonLimparActionPerformed

    private void jButtonSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalvarActionPerformed
        // Método para acionar o CONTROLLER que jogará o objeto pro DAO salvar
        NotebookController noteCtrl = new NotebookController();
        NotebookModel noteModel = new NotebookModel();
        ClientesPFController cliPfCtrl = new ClientesPFController();
        String email = "";
        if (verificaCamposEmBranco()) {
            noteModel = new NotebookModel(jTextFieldTela.getText(), jTextFieldProc.getText(), jTextFieldMemoria.getText(),
                    jTextFieldArmazenamento.getText(),jTextFieldDono.getText(), jTextFieldMarca.getText(),
                    jTextFieldModelo.getText(), jTextFieldSerial.getText());
            List<NotebookModel> listaNote = noteCtrl.leNotebook();
            listaNote.add(noteModel);

            String dadosEmail = "NOTEBOOK CADASTRADO: " + "\n"
                    + "CPF DONO: " + jTextFieldDono.getText() + "\n"
                    + "Marca: " + jTextFieldMarca.getText() + "\n"
                    + "Modelo: " + jTextFieldModelo.getText() + "\n"
                    + "Serial: " + jTextFieldSerial.getText() + "\n"
                    + "Tela: " + jTextFieldTela.getText() + "\n"
                    + "Processador: " + jTextFieldProc.getText() + "\n"
                    + "Memória: " + jTextFieldMemoria.getText() + "\n"
                    + "Armazenamento: " + jTextFieldArmazenamento.getText();

            if (noteCtrl.gravaNotebook(listaNote)) {

                List<ClientesPFModel> listaClientesPF = cliPfCtrl.leClientesPf();
                for (ClientesPFModel cliPf : listaClientesPF) {
                    if (cliPf.getCPF().equals(jTextFieldDono.getText())) {
                        email = cliPf.getEmail();
                    }
                }

                JOptionPane.showMessageDialog(this, "Salvo com sucesso");
                JOptionPane.showMessageDialog(this, "Email enviado para " + email);
                UTIL.EnviaEmail.enviaEmailCadastro("cadastronote", email, "",dadosEmail);
                limparCampos();
                preencheTabela();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao salvar");
            }
        }
    }//GEN-LAST:event_jButtonSalvarActionPerformed

    private void jButtonAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAlterarActionPerformed
        // TODO add your handling code here:
        NotebookController noteCtrl = new NotebookController();
        NotebookModel noteModel = new NotebookModel();
        if (jTableNotebook.getSelectedRow() != -1) {
            noteModel.setDono(jTextFieldDono.getText());
            noteModel.setMarca(jTextFieldMarca.getText());
            noteModel.setModelo(jTextFieldModelo.getText());
            noteModel.setSerial(jTextFieldSerial.getText());
            if (noteCtrl.alteranotebook(noteModel)) {
                JOptionPane.showMessageDialog(this, "Cliente Alterado com sucesso");
                preencheTabela();
                limparCampos();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Cliente para alterar");
        }

    }//GEN-LAST:event_jButtonAlterarActionPerformed

    private void jButtonNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNovoActionPerformed
        habilitaCampos(true);
        habilitaBotoes(true);

    }//GEN-LAST:event_jButtonNovoActionPerformed

    private void jButtonCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelarActionPerformed
        habilitaCampos(false);
        habilitaBotoes(false);

    }//GEN-LAST:event_jButtonCancelarActionPerformed

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

    private void jTableNotebookMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableNotebookMouseClicked
        // TODO add your handling code here:
        if (jTableNotebook.getSelectedRow() != -1) {
            jTextFieldDono.setText(jTableNotebook.getValueAt(jTableNotebook.getSelectedRow(), 0).toString());
            jTextFieldMarca.setText(jTableNotebook.getValueAt(jTableNotebook.getSelectedRow(), 1).toString());
            jTextFieldModelo.setText(jTableNotebook.getValueAt(jTableNotebook.getSelectedRow(), 2).toString());
            jTextFieldSerial.setText(jTableNotebook.getValueAt(jTableNotebook.getSelectedRow(), 3).toString());
            jTextFieldTela.setText(jTableNotebook.getValueAt(jTableNotebook.getSelectedRow(), 4).toString());
            jTextFieldProc.setText(jTableNotebook.getValueAt(jTableNotebook.getSelectedRow(), 5).toString());
            jTextFieldMemoria.setText(jTableNotebook.getValueAt(jTableNotebook.getSelectedRow(), 6).toString());
            jTextFieldArmazenamento.setText(jTableNotebook.getValueAt(jTableNotebook.getSelectedRow(), 7).toString());
        }
    }//GEN-LAST:event_jTableNotebookMouseClicked

    private void jTableNotebookMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableNotebookMouseReleased
        // TODO add your handling code here:
        if (jTableNotebook.getSelectedRow() != -1) {
            jTextFieldDono.setText(jTableNotebook.getValueAt(jTableNotebook.getSelectedRow(), 0).toString());
            jTextFieldMarca.setText(jTableNotebook.getValueAt(jTableNotebook.getSelectedRow(), 1).toString());
            jTextFieldModelo.setText(jTableNotebook.getValueAt(jTableNotebook.getSelectedRow(), 2).toString());
            jTextFieldSerial.setText(jTableNotebook.getValueAt(jTableNotebook.getSelectedRow(), 3).toString());
            jTextFieldTela.setText(jTableNotebook.getValueAt(jTableNotebook.getSelectedRow(), 4).toString());
            jTextFieldProc.setText(jTableNotebook.getValueAt(jTableNotebook.getSelectedRow(), 5).toString());
            jTextFieldMemoria.setText(jTableNotebook.getValueAt(jTableNotebook.getSelectedRow(), 6).toString());
            jTextFieldArmazenamento.setText(jTableNotebook.getValueAt(jTableNotebook.getSelectedRow(), 7).toString());
        }
    }//GEN-LAST:event_jTableNotebookMouseReleased

    private void jTableNotebookKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableNotebookKeyReleased
        // TODO add your handling code here:
        if (jTableNotebook.getSelectedRow() != -1) {
            jTextFieldDono.setText(jTableNotebook.getValueAt(jTableNotebook.getSelectedRow(), 0).toString());
            jTextFieldMarca.setText(jTableNotebook.getValueAt(jTableNotebook.getSelectedRow(), 1).toString());
            jTextFieldModelo.setText(jTableNotebook.getValueAt(jTableNotebook.getSelectedRow(), 2).toString());
            jTextFieldSerial.setText(jTableNotebook.getValueAt(jTableNotebook.getSelectedRow(), 3).toString());
            jTextFieldTela.setText(jTableNotebook.getValueAt(jTableNotebook.getSelectedRow(), 4).toString());
            jTextFieldProc.setText(jTableNotebook.getValueAt(jTableNotebook.getSelectedRow(), 5).toString());
            jTextFieldMemoria.setText(jTableNotebook.getValueAt(jTableNotebook.getSelectedRow(), 6).toString());
            jTextFieldArmazenamento.setText(jTableNotebook.getValueAt(jTableNotebook.getSelectedRow(), 7).toString());
        }
    }//GEN-LAST:event_jTableNotebookKeyReleased

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
        if (!cpfValido()) {
            jTextFieldDono.setText("");
        }
        if (cpfValido() && !verificaCpfExistente(jTextFieldDono.getText())) {
            JOptionPane.showMessageDialog(this, "CPF '" + jTextFieldDono.getText() + "' não existe no cadastro");
            jTextFieldDono.setText("");
        } else {
            ClientesPFController cliPfCtrl = new ClientesPFController();
            List<ClientesPFModel> listaClientesPF = cliPfCtrl.leClientesPf();
            for (ClientesPFModel cliPf : listaClientesPF) {
                if (cliPf.getCPF().equals(jTextFieldDono.getText())) {
                    int selecionaOpcao = JOptionPane.showConfirmDialog(null,
                            "O nome do cliente é: " + cliPf.getNome() + " ?", null,
                            JOptionPane.OK_CANCEL_OPTION);
                    if (selecionaOpcao == JOptionPane.CANCEL_OPTION) {
                        jTextFieldDono.setText("");
                    } else {
                        jTextFieldMarca.requestFocus();
                    }
                }
            }
        }
    }//GEN-LAST:event_jButtonConfirmaClienteActionPerformed

    private void jButtonFecharJanelaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFecharJanelaActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButtonFecharJanelaActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAlterar;
    private javax.swing.JButton jButtonCancelar;
    private javax.swing.JButton jButtonConfirmaCliente;
    private javax.swing.JButton jButtonConsulta;
    private javax.swing.JButton jButtonExcluir;
    private javax.swing.JButton jButtonFecharJanela;
    private javax.swing.JButton jButtonGeraPdf;
    private javax.swing.JButton jButtonLimpar;
    private javax.swing.JButton jButtonNovo;
    private javax.swing.JButton jButtonSalvar;
    private javax.swing.JButton jButtonVerificaPendencias;
    private javax.swing.JLabel jLabelCPF;
    private javax.swing.JLabel jLabelEmail;
    private javax.swing.JLabel jLabelEmail1;
    private javax.swing.JLabel jLabelEmail2;
    private javax.swing.JLabel jLabelEmail3;
    private javax.swing.JLabel jLabelEmail4;
    private javax.swing.JLabel jLabelEmail5;
    private javax.swing.JLabel jLabelNome;
    private javax.swing.JPanel jPanelBotoes;
    private javax.swing.JPanel jPanelDadosPessoais;
    private javax.swing.JPanel jPanelTabela;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTableNotebook;
    private javax.swing.JTextField jTextFieldArmazenamento;
    private javax.swing.JTextField jTextFieldDono;
    private javax.swing.JTextField jTextFieldMarca;
    private javax.swing.JTextField jTextFieldMemoria;
    private javax.swing.JTextField jTextFieldModelo;
    private javax.swing.JTextField jTextFieldProc;
    private javax.swing.JTextField jTextFieldSerial;
    private javax.swing.JTextField jTextFieldTela;
    // End of variables declaration//GEN-END:variables
}
