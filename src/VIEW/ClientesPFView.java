/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VIEW;

// Import do Controlador
import CONTROLLER.ClientesPFController;

// Import dos modelos
import MODEL.ClientesPFModel;
import MODEL.ClientesPFTableModel;
import MODEL.EnderecoModel;
import MODEL.TableColumnAdjuster;

// Import das classes para trabalhar com o preenchimento do endereço a partir do CEP
import UTIL.ViaCEP;
import UTIL.ViaCEPException;
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
import java.net.URISyntaxException;
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
public class ClientesPFView extends javax.swing.JInternalFrame {

    /**
     * Creates new form ClientesPFView
     */
    public ClientesPFView() {
        initComponents();
        jTableClientesPf.setModel(tableModel);
        preencheTabela();

        //ajustaTabela();
        habilitaCampos(false);
        habilitaBotoes(false);
    }

    //Instancia minha jTable com base no modelo de Tabela definido no pacote MODEL
    ClientesPFTableModel tableModel = new ClientesPFTableModel();

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

        ClientesPFController cliPfCtrl = new ClientesPFController();
        Document doc = new Document();
        try {
            doc.setPageSize(PageSize.A4.rotate());
            List<ClientesPFModel> minhaLista = cliPfCtrl.leClientesPf();
            //PdfWriter.getInstance(doc, new FileOutputStream(BASE_RELATORIOS));
            PdfWriter.getInstance(doc, new FileOutputStream(caminho));
            doc.open();
            doc.addAuthor("RF Assistência");
            doc.addLanguage("PT-BR");
            doc.addTitle("Relatório de Clientes - Pessoa Física");
            //
            PdfPTable table = new PdfPTable(new float[]{0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f});
            table.setWidthPercentage(100.0f);
            table.setHorizontalAlignment(Element.ALIGN_JUSTIFIED_ALL);
            doc.add(new Paragraph("RF Assistência Técnica - Relatórios\n"));
            doc.add(new Paragraph("\n"));
//            doc.add(new Paragraph("RF Assistência Técnica - Relatórios\n",FontFactory.getFont(FontFactory.COURIER, 5)));
//            doc.add(new Paragraph("\n"));
            PdfPCell header = new PdfPCell(new Paragraph("Lista dos Clientes Pessoa Física"));
            PdfPCell cellId = new PdfPCell();
            PdfPCell cellCpf = new PdfPCell();
            PdfPCell cellNome = new PdfPCell();
            PdfPCell cellEmail = new PdfPCell();
            PdfPCell cellTelefone = new PdfPCell();
            PdfPCell cellCep = new PdfPCell();
            PdfPCell cellLogradouro = new PdfPCell();
            PdfPCell cellNumero = new PdfPCell();
            PdfPCell cellComplemento = new PdfPCell();
            PdfPCell cellBairro = new PdfPCell();
            PdfPCell cellCidade = new PdfPCell();
            PdfPCell cellUf = new PdfPCell();
            header.setColspan(12);
            header.setBackgroundColor(BaseColor.GRAY);
            String[] cabecalho = {"Id", "CPF", "Nome", "Email", "Telefone", "CEP", "Logradouro", "Número", "Complemento", "Bairro", "Cidade", "UF"};
            table.addCell(header);
            for (String nomes : cabecalho) {
                table.addCell(nomes);
            }
            // Fazer um for e preeencher a tabela
            for (int i = 0; i < minhaLista.size(); i++) {

                table.addCell(String.valueOf(minhaLista.get(i).getId()));
                table.addCell(minhaLista.get(i).getCPF());
                table.addCell(minhaLista.get(i).getNome());
                table.addCell(minhaLista.get(i).getEmail());
                table.addCell(minhaLista.get(i).getTelefone());
                table.addCell(minhaLista.get(i).getEndereco().getCEP());
                table.addCell(minhaLista.get(i).getEndereco().getLogradouro());
                table.addCell(minhaLista.get(i).getEndereco().getNumero());
                table.addCell(minhaLista.get(i).getEndereco().getComplemento());
                table.addCell(minhaLista.get(i).getEndereco().getBairro());
                table.addCell(minhaLista.get(i).getEndereco().getCidade());
                table.addCell(minhaLista.get(i).getEndereco().getUf());

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
            Logger.getLogger(ClientesPFView.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void preencheTabela() {

        tableModel.limpaTabela();
        ClientesPFModel cliPfModel = new ClientesPFModel();
        ClientesPFController cliPfCtrl = new ClientesPFController();
        for (ClientesPFModel cli : cliPfCtrl.leClientesPf()) {
            tableModel.addRow(cli);
        }
        jTableClientesPf.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumnAdjuster tca = new TableColumnAdjuster(jTableClientesPf);
        tca.adjustColumns();

    }

    public boolean cpfValido() {
        boolean cpfValido = UTIL.ValidaCampos.validaCpf(jTextFieldCPF.getText());
        if (!cpfValido) {
            JOptionPane.showMessageDialog(this, "CPF digitado não é válido");
            return false;
        }
        return true;
    }

    public void buscaCep() {
        ViaCEP endereco;
        try {
            endereco = new ViaCEP(jFormattedTextFieldCep.getText().replaceAll("-", ""));
            jTextFieldLogradouro.setText(endereco.getLogradouro());
            jTextFieldComplemento.setText(endereco.getComplemento());
            jTextFieldBairro.setText(endereco.getBairro());
            jTextFieldCidade.setText(endereco.getLocalidade());
            jTextFieldEstado.setText(endereco.getUf());

        } catch (ViaCEPException ex) {
            if (ex.toString().substring(22).equals("viacep.com.br")) {
                JOptionPane.showMessageDialog(this, "Verifique a sua conexão");
            } else if (ex.toString().substring(22).equals("Não foi possível encontrar o CEP")) {
                JOptionPane.showMessageDialog(this, "Não foi possível localizar o CEP");
            } else if (ex.toString().substring(22).equals("Server returned HTTP response code: 400 for URL: http://viacep.com.br/ws/5441039991/json/")) {
                JOptionPane.showMessageDialog(this, "CEP INVÁLIDO");
            }
        }
    }

    public JTextField[] limparCampos() {
        JTextField[] campos = {jTextFieldCPF, jTextFieldNome, jTextFieldId, jTextFieldEmail, jTextFieldLogradouro, jTextFieldNumero, jTextFieldComplemento, jTextFieldBairro, jTextFieldCidade, jTextFieldEstado};
        for (int i = 0; i < campos.length; i++) {
            campos[i].setText("");
        }
        jFormattedTextTelefone.setText("");
        jFormattedTextFieldCep.setText("");
        return campos;
    }

    public boolean verificaCamposEmBranco() {
        int erros = 0;
        String cepvazio = "     -   ";
        String telvazio = "(  )       -     ";
        JTextField[] campos = {jTextFieldCPF, jTextFieldNome, jTextFieldEmail, jTextFieldLogradouro, jTextFieldNumero, jTextFieldBairro, jTextFieldCidade, jTextFieldEstado};
        String[] camposNome = {"CPF", "Nome", "Email", "Logradouro", "Numero", "Bairro", "Cidade", "Estado"};
        for (int i = 0; i < campos.length; i++) {
            if (campos[i].getText().equals("")) {
                JOptionPane.showMessageDialog(this, "Campo " + camposNome[i] + " em branco, verifique");
                erros++;
            }
        }
        if (jFormattedTextTelefone.getText().equals(telvazio)) {
            JOptionPane.showMessageDialog(this, "Campo Telefone em branco, verifique");
            erros++;
        }
        if (jFormattedTextFieldCep.getText().equals(cepvazio)) {
            JOptionPane.showMessageDialog(this, "Campo CEP em branco, verifique");
            erros++;
        }
        if (erros == 0) {
            return true;
        } else {
            return false;
        }
    }

    private void habilitaCampos(boolean estado) {

        jTextFieldCPF.setEnabled(estado);
        jFormattedTextFieldCep.setEnabled(estado);
        jFormattedTextTelefone.setEnabled(estado);
        jTextFieldNome.setEnabled(estado);
        jTextFieldComplemento.setEnabled(estado);
        jTextFieldEmail.setEnabled(estado);
        jTextFieldLogradouro.setEnabled(estado);
        jTextFieldNumero.setEnabled(estado);
        jTextFieldBairro.setEnabled(estado);
        jTextFieldCidade.setEnabled(estado);
        jTextFieldEstado.setEnabled(estado);

    }

    private void habilitaBotoes(boolean estado) {
        jButtonAlterar.setEnabled(estado);
        jButtonCancelar.setEnabled(estado);
        jButtonBuscaCep.setEnabled(estado);
        jButtonExcluir.setEnabled(estado);
        jButtonLimpar.setEnabled(estado);
        jButtonNaoSeiCep.setEnabled(estado);
        jButtonSalvar.setEnabled(estado);
        jButtonVerificaPendencias.setEnabled(estado);
        jButtonGeraPdf.setEnabled(estado);
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
        jTextFieldCPF = new javax.swing.JTextField();
        jTextFieldNome = new javax.swing.JTextField();
        jTextFieldId = new javax.swing.JTextField();
        jTextFieldEmail = new javax.swing.JTextField();
        jFormattedTextTelefone = new javax.swing.JFormattedTextField();
        jLabelCPF = new javax.swing.JLabel();
        jLabelNome = new javax.swing.JLabel();
        jLabelId = new javax.swing.JLabel();
        jLabelEmail = new javax.swing.JLabel();
        jLabelTelefone = new javax.swing.JLabel();
        jPanelTabela = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableClientesPf = new javax.swing.JTable();
        jPanelEndereco = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jFormattedTextFieldCep = new javax.swing.JFormattedTextField();
        jButtonBuscaCep = new javax.swing.JButton();
        jButtonNaoSeiCep = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldLogradouro = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextFieldNumero = new javax.swing.JTextField();
        jTextFieldComplemento = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextFieldBairro = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jTextFieldCidade = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jTextFieldEstado = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
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

        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        setPreferredSize(new java.awt.Dimension(787, 412));
        try {
            setSelected(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }

        jPanelDadosPessoais.setBorder(javax.swing.BorderFactory.createTitledBorder("Dados Pessoais"));
        jPanelDadosPessoais.setPreferredSize(new java.awt.Dimension(787, 412));

        jTextFieldCPF.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldCPFFocusLost(evt);
            }
        });

        jTextFieldNome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldNomeActionPerformed(evt);
            }
        });

        jTextFieldId.setEditable(false);

        jTextFieldEmail.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldEmailFocusLost(evt);
            }
        });

        try {
            jFormattedTextTelefone.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##) ##### - ####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabelCPF.setText("CPF:");

        jLabelNome.setText("Nome:");

        jLabelId.setText("ID:");

        jLabelEmail.setText("Email:");

        jLabelTelefone.setText("Telefone");

        javax.swing.GroupLayout jPanelDadosPessoaisLayout = new javax.swing.GroupLayout(jPanelDadosPessoais);
        jPanelDadosPessoais.setLayout(jPanelDadosPessoaisLayout);
        jPanelDadosPessoaisLayout.setHorizontalGroup(
            jPanelDadosPessoaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDadosPessoaisLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelDadosPessoaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanelDadosPessoaisLayout.createSequentialGroup()
                        .addComponent(jLabelId)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldId, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelCPF)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldCPF, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelNome)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldNome, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelDadosPessoaisLayout.createSequentialGroup()
                        .addComponent(jLabelEmail)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldEmail)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelTelefone)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jFormattedTextTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 42, Short.MAX_VALUE))
        );
        jPanelDadosPessoaisLayout.setVerticalGroup(
            jPanelDadosPessoaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDadosPessoaisLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelDadosPessoaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelId)
                    .addComponent(jTextFieldId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelCPF)
                    .addComponent(jTextFieldCPF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelNome)
                    .addComponent(jTextFieldNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelDadosPessoaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelEmail)
                    .addComponent(jLabelTelefone)
                    .addComponent(jFormattedTextTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelTabela.setBorder(javax.swing.BorderFactory.createTitledBorder("Consulta de Clientes Pessoa Física"));

        jTableClientesPf.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"000", "Roberta Raquel Ferreira Rodrigues Van Druem", "(88) 88888 - 8888", "00000 - 000", "Rua Rossini Roosevelt de Albuquerque", "9999 casa A", "Edf Itatiaia Apto 301", "Santa Terezinha", "Jaboatão dos Guararapes", "PE"}
            },
            new String [] {
                "Id", "Nome", "Telefone", "CEP", "Logradouro", "Número", "Complemento", "Bairro", "Cidade", "Estado"
            }
        ));
        jTableClientesPf.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableClientesPfMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTableClientesPfMouseReleased(evt);
            }
        });
        jTableClientesPf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTableClientesPfKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(jTableClientesPf);

        javax.swing.GroupLayout jPanelTabelaLayout = new javax.swing.GroupLayout(jPanelTabela);
        jPanelTabela.setLayout(jPanelTabelaLayout);
        jPanelTabelaLayout.setHorizontalGroup(
            jPanelTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTabelaLayout.createSequentialGroup()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        jPanelTabelaLayout.setVerticalGroup(
            jPanelTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTabelaLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanelEndereco.setBorder(javax.swing.BorderFactory.createTitledBorder("Endereço"));

        jLabel5.setText("CEP");

        try {
            jFormattedTextFieldCep.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#####-###")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jButtonBuscaCep.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/zoom.png"))); // NOI18N
        jButtonBuscaCep.setText("Buscar");
        jButtonBuscaCep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBuscaCepActionPerformed(evt);
            }
        });

        jButtonNaoSeiCep.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/question1.png"))); // NOI18N
        jButtonNaoSeiCep.setText("Não sabe o CEP?");
        jButtonNaoSeiCep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNaoSeiCepActionPerformed(evt);
            }
        });

        jLabel6.setText("Logradouro");

        jLabel7.setText("Número");

        jTextFieldComplemento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldComplementoActionPerformed(evt);
            }
        });

        jLabel8.setText("Complemento");

        jLabel12.setText("Cidade");

        jLabel13.setText("Estado");

        jTextFieldEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldEstadoActionPerformed(evt);
            }
        });

        jLabel9.setText("Bairro");

        javax.swing.GroupLayout jPanelEnderecoLayout = new javax.swing.GroupLayout(jPanelEndereco);
        jPanelEndereco.setLayout(jPanelEnderecoLayout);
        jPanelEnderecoLayout.setHorizontalGroup(
            jPanelEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEnderecoLayout.createSequentialGroup()
                .addGroup(jPanelEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelEnderecoLayout.createSequentialGroup()
                        .addGroup(jPanelEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelEnderecoLayout.createSequentialGroup()
                                .addComponent(jTextFieldCidade, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jTextFieldLogradouro, javax.swing.GroupLayout.PREFERRED_SIZE, 520, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanelEnderecoLayout.createSequentialGroup()
                                .addComponent(jTextFieldNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextFieldComplemento, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextFieldBairro, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelEnderecoLayout.createSequentialGroup()
                        .addGroup(jPanelEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelEnderecoLayout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jFormattedTextFieldCep, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonBuscaCep)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonNaoSeiCep, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(50, 50, 50))
        );
        jPanelEnderecoLayout.setVerticalGroup(
            jPanelEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEnderecoLayout.createSequentialGroup()
                .addGroup(jPanelEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonBuscaCep, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonNaoSeiCep)
                    .addComponent(jFormattedTextFieldCep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(4, 4, 4)
                .addGroup(jPanelEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextFieldLogradouro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jTextFieldComplemento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldBairro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel7)
                    .addComponent(jTextFieldNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jTextFieldCidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addContainerGap(42, Short.MAX_VALUE))
        );

        jButtonConsulta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/zoom.png"))); // NOI18N
        jButtonConsulta.setText("Consultar");
        jButtonConsulta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConsultaActionPerformed(evt);
            }
        });

        jButtonExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/TrashGreen24px.png"))); // NOI18N
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

        jButtonCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Fechar.png"))); // NOI18N
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
        jButtonFecharJanela.setText("Fechar");
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
                .addComponent(jButtonConsulta)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonNovo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonCancelar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonAlterar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonExcluir)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonSalvar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonVerificaPendencias)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonLimpar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonGeraPdf)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonFecharJanela)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelBotoesLayout.setVerticalGroup(
            jPanelBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBotoesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonAlterar)
                        .addComponent(jButtonLimpar)
                        .addComponent(jButtonSalvar)
                        .addComponent(jButtonFecharJanela)
                        .addComponent(jButtonExcluir)
                        .addComponent(jButtonGeraPdf)
                        .addComponent(jButtonVerificaPendencias))
                    .addGroup(jPanelBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonNovo)
                        .addComponent(jButtonCancelar))
                    .addComponent(jButtonConsulta, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelTabela, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanelDadosPessoais, javax.swing.GroupLayout.PREFERRED_SIZE, 528, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanelEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanelBotoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanelEndereco, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelDadosPessoais, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelBotoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelTabela, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanelTabela.getAccessibleContext().setAccessibleName("Dados Pessoais");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExcluirActionPerformed
        if (jTableClientesPf.getSelectedRow() != -1) {
            ClientesPFModel clientePf = new ClientesPFModel();
            clientePf.setId(Integer.parseInt(jTextFieldId.getText()));
            clientePf.setCPF(jTextFieldCPF.getText());
            clientePf.setNome(jTextFieldNome.getText());
            clientePf.setEmail(jTextFieldEmail.getText());
            clientePf.setTelefone(jFormattedTextTelefone.getText());
            EnderecoModel end = new EnderecoModel(jFormattedTextFieldCep.getText().replaceAll("-", ""), jTextFieldLogradouro.getText(),
                    jTextFieldNumero.getText(), jTextFieldComplemento.getText(), jTextFieldBairro.getText(),
                    jTextFieldCidade.getText(), jTextFieldEstado.getText());
            clientePf.setEndereco(end);
            ClientesPFController cliPfCtrl = new ClientesPFController();
            if (cliPfCtrl.removeClientesPF(clientePf)) {
                JOptionPane.showMessageDialog(this, "Cliente Excluído com sucesso");
                preencheTabela();
                limparCampos();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Cliente para excluir");
        }


    }//GEN-LAST:event_jButtonExcluirActionPerformed

    private void jButtonConsultaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConsultaActionPerformed
        // Acessando direto pelo Controller
        preencheTabela();
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Banco de dados vazio");
        }
    }//GEN-LAST:event_jButtonConsultaActionPerformed

    private void jTextFieldNomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldNomeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldNomeActionPerformed

    private void jButtonBuscaCepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBuscaCepActionPerformed
        // Busca o CEP e preenche os campos
        buscaCep();
    }//GEN-LAST:event_jButtonBuscaCepActionPerformed

    private void jTextFieldComplementoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldComplementoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldComplementoActionPerformed

    private void jTextFieldEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldEstadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldEstadoActionPerformed

    private void jButtonLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLimparActionPerformed
        // Limpa os campos
        limparCampos();
    }//GEN-LAST:event_jButtonLimparActionPerformed

    private void jButtonSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalvarActionPerformed
        // Método para acionar o CONTROLLER que jogará o objeto pro DAO salvar
        if (verificaCamposEmBranco()) {
            ClientesPFController cliPfCtrl = new ClientesPFController();
            List<ClientesPFModel> listaPF = cliPfCtrl.leClientesPf();
            EnderecoModel end = new EnderecoModel(jFormattedTextFieldCep.getText(), jTextFieldLogradouro.getText(), jTextFieldNumero.getText(),
                    jTextFieldComplemento.getText(), jTextFieldBairro.getText(), jTextFieldCidade.getText(), jTextFieldEstado.getText());
            ClientesPFModel cliPf = new ClientesPFModel(jTextFieldCPF.getText(), jTextFieldNome.getText(),
                    jTextFieldEmail.getText(), jFormattedTextTelefone.getText(), end);

            if (listaPF.size() != 0) {
                int ultimaId = listaPF.get(listaPF.size() - 1).getId();
                cliPf.setId(++ultimaId);
            }

            listaPF.add(cliPf);

            String dadosEmail = "DADOS PESSOAIS: " + "\n"
                    + "CPF: " + jTextFieldCPF.getText() + "\n"
                    + "Nome: " + jTextFieldNome.getText() + "\n"
                    + "Email: " + jTextFieldEmail.getText() + "\n"
                    + "Telefone: " + jFormattedTextTelefone.getText() + "\n"
                    + "ENDEREÇO: " + "\n"
                    + "CEP: " + jFormattedTextFieldCep.getText() + "\n"
                    + "Logradouro: " + jTextFieldLogradouro.getText() + "\n"
                    + "Número: " + jTextFieldNumero.getText() + "\n"
                    + "Complemento: " + jTextFieldComplemento.getText() + "\n"
                    + "Bairro: " + jTextFieldBairro.getText() + "\n"
                    + "Cidade: " + jTextFieldCidade.getText() + "\n"
                    + "UF: " + jTextFieldEstado.getText() + "\n";

            if (cliPfCtrl.gravaClientesPf(listaPF)) {
                JOptionPane.showMessageDialog(this, "Salvo com sucesso");
                JOptionPane.showMessageDialog(this, "Email enviado para " + jTextFieldEmail.getText());
                UTIL.EnviaEmail.enviaEmailCadastro("cadastro", jTextFieldEmail.getText(), "", dadosEmail);
                limparCampos();
                preencheTabela();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao salvar");
            }
        }
    }//GEN-LAST:event_jButtonSalvarActionPerformed

    private void jButtonAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAlterarActionPerformed
        // TODO add your handling code here:
        if (jTableClientesPf.getSelectedRow() != -1) {
            ClientesPFModel clientePf = new ClientesPFModel();
            clientePf.setId(Integer.parseInt(jTextFieldId.getText()));
            clientePf.setCPF(jTextFieldCPF.getText());
            clientePf.setNome(jTextFieldNome.getText());
            clientePf.setEmail(jTextFieldEmail.getText());
            clientePf.setTelefone(jFormattedTextTelefone.getText());
            EnderecoModel end = new EnderecoModel(jFormattedTextFieldCep.getText().replaceAll("-", ""), jTextFieldLogradouro.getText(),
                    jTextFieldNumero.getText(), jTextFieldComplemento.getText(), jTextFieldBairro.getText(),
                    jTextFieldCidade.getText(), jTextFieldEstado.getText());
            clientePf.setEndereco(end);
            ClientesPFController cliPfCtrl = new ClientesPFController();
            if (cliPfCtrl.alteraClientesPF(clientePf)) {
                JOptionPane.showMessageDialog(this, "Cliente Alterado com sucesso");
                preencheTabela();
                limparCampos();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Cliente para alterar");
        }

    }//GEN-LAST:event_jButtonAlterarActionPerformed

    private void jButtonNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNovoActionPerformed
        // TODO add your handling code here:
        habilitaCampos(true);
        habilitaBotoes(true);
    }//GEN-LAST:event_jButtonNovoActionPerformed

    private void jButtonCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelarActionPerformed
        // TODO add your handling code here:
        habilitaBotoes(false);
        habilitaCampos(false);
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

    private void jTableClientesPfMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableClientesPfMouseClicked
        // TODO add your handling code here:
        if (jTableClientesPf.getSelectedRow() != -1) {
            jTextFieldId.setText(jTableClientesPf.getValueAt(jTableClientesPf.getSelectedRow(), 0).toString());
            jTextFieldCPF.setText(jTableClientesPf.getValueAt(jTableClientesPf.getSelectedRow(), 1).toString());
            jTextFieldNome.setText(jTableClientesPf.getValueAt(jTableClientesPf.getSelectedRow(), 2).toString());
            jTextFieldEmail.setText(jTableClientesPf.getValueAt(jTableClientesPf.getSelectedRow(), 3).toString());
            jFormattedTextTelefone.setText(jTableClientesPf.getValueAt(jTableClientesPf.getSelectedRow(), 4).toString());
            jFormattedTextFieldCep.setText(jTableClientesPf.getValueAt(jTableClientesPf.getSelectedRow(), 5).toString());
            jTextFieldLogradouro.setText(jTableClientesPf.getValueAt(jTableClientesPf.getSelectedRow(), 6).toString());
            jTextFieldNumero.setText(jTableClientesPf.getValueAt(jTableClientesPf.getSelectedRow(), 7).toString());
            jTextFieldComplemento.setText(jTableClientesPf.getValueAt(jTableClientesPf.getSelectedRow(), 8).toString());
            jTextFieldBairro.setText(jTableClientesPf.getValueAt(jTableClientesPf.getSelectedRow(), 9).toString());
            jTextFieldCidade.setText(jTableClientesPf.getValueAt(jTableClientesPf.getSelectedRow(), 10).toString());
            jTextFieldEstado.setText(jTableClientesPf.getValueAt(jTableClientesPf.getSelectedRow(), 11).toString());
        }
    }//GEN-LAST:event_jTableClientesPfMouseClicked

    private void jTableClientesPfMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableClientesPfMouseReleased
        // TODO add your handling code here:
        if (jTableClientesPf.getSelectedRow() != -1) {
            jTextFieldId.setText(jTableClientesPf.getValueAt(jTableClientesPf.getSelectedRow(), 0).toString());
            jTextFieldCPF.setText(jTableClientesPf.getValueAt(jTableClientesPf.getSelectedRow(), 1).toString());
            jTextFieldNome.setText(jTableClientesPf.getValueAt(jTableClientesPf.getSelectedRow(), 2).toString());
            jTextFieldEmail.setText(jTableClientesPf.getValueAt(jTableClientesPf.getSelectedRow(), 3).toString());
            jFormattedTextTelefone.setText(jTableClientesPf.getValueAt(jTableClientesPf.getSelectedRow(), 4).toString());
            jFormattedTextFieldCep.setText(jTableClientesPf.getValueAt(jTableClientesPf.getSelectedRow(), 5).toString());
            jTextFieldLogradouro.setText(jTableClientesPf.getValueAt(jTableClientesPf.getSelectedRow(), 6).toString());
            jTextFieldNumero.setText(jTableClientesPf.getValueAt(jTableClientesPf.getSelectedRow(), 7).toString());
            jTextFieldComplemento.setText(jTableClientesPf.getValueAt(jTableClientesPf.getSelectedRow(), 8).toString());
            jTextFieldBairro.setText(jTableClientesPf.getValueAt(jTableClientesPf.getSelectedRow(), 9).toString());
            jTextFieldCidade.setText(jTableClientesPf.getValueAt(jTableClientesPf.getSelectedRow(), 10).toString());
            jTextFieldEstado.setText(jTableClientesPf.getValueAt(jTableClientesPf.getSelectedRow(), 11).toString());
        }
    }//GEN-LAST:event_jTableClientesPfMouseReleased

    private void jTableClientesPfKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableClientesPfKeyReleased
        // TODO add your handling code here:
        if (jTableClientesPf.getSelectedRow() != -1) {
            jTextFieldId.setText(jTableClientesPf.getValueAt(jTableClientesPf.getSelectedRow(), 0).toString());
            jTextFieldCPF.setText(jTableClientesPf.getValueAt(jTableClientesPf.getSelectedRow(), 1).toString());
            jTextFieldNome.setText(jTableClientesPf.getValueAt(jTableClientesPf.getSelectedRow(), 2).toString());
            jTextFieldEmail.setText(jTableClientesPf.getValueAt(jTableClientesPf.getSelectedRow(), 3).toString());
            jFormattedTextTelefone.setText(jTableClientesPf.getValueAt(jTableClientesPf.getSelectedRow(), 4).toString());
            jFormattedTextFieldCep.setText(jTableClientesPf.getValueAt(jTableClientesPf.getSelectedRow(), 5).toString());
            jTextFieldLogradouro.setText(jTableClientesPf.getValueAt(jTableClientesPf.getSelectedRow(), 6).toString());
            jTextFieldNumero.setText(jTableClientesPf.getValueAt(jTableClientesPf.getSelectedRow(), 7).toString());
            jTextFieldComplemento.setText(jTableClientesPf.getValueAt(jTableClientesPf.getSelectedRow(), 8).toString());
            jTextFieldBairro.setText(jTableClientesPf.getValueAt(jTableClientesPf.getSelectedRow(), 9).toString());
            jTextFieldCidade.setText(jTableClientesPf.getValueAt(jTableClientesPf.getSelectedRow(), 10).toString());
            jTextFieldEstado.setText(jTableClientesPf.getValueAt(jTableClientesPf.getSelectedRow(), 11).toString());
        }
    }//GEN-LAST:event_jTableClientesPfKeyReleased

    private void jTextFieldCPFFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldCPFFocusLost
        // TODO add your handling code here:
        if (!cpfValido()) {
            jTextFieldCPF.setText("");
        }
        if (verificaCpfExistente(jTextFieldCPF.getText())) {
            JOptionPane.showMessageDialog(this, "CPF '" + jTextFieldCPF.getText() + "' já existe no cadastro");
            jTextFieldCPF.setText("");
        }


    }//GEN-LAST:event_jTextFieldCPFFocusLost

    private void jButtonVerificaPendenciasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVerificaPendenciasActionPerformed
        // TODO add your handling code here:
        if (verificaCamposEmBranco()) {
            JOptionPane.showMessageDialog(this, "Nenhuma Pendência encontrada");
        }
    }//GEN-LAST:event_jButtonVerificaPendenciasActionPerformed

    private void jButtonNaoSeiCepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNaoSeiCepActionPerformed
        // TODO add your handling code here:
        try {
            java.awt.Desktop.getDesktop().browse(new java.net.URI("http://www.buscacep.correios.com.br/sistemas/buscacep/buscaCepEndereco.cfm"));
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(ClientesPFView.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButtonNaoSeiCepActionPerformed

    private void jButtonFecharJanelaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFecharJanelaActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButtonFecharJanelaActionPerformed

    private void jTextFieldEmailFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldEmailFocusLost
        if (!UTIL.ValidaCampos.validaEmail(jTextFieldEmail.getText())){
            JOptionPane.showMessageDialog(this, "Email Inválido");
            jTextFieldEmail.setText("");
        }
    }//GEN-LAST:event_jTextFieldEmailFocusLost

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAlterar;
    private javax.swing.JButton jButtonBuscaCep;
    private javax.swing.JButton jButtonCancelar;
    private javax.swing.JButton jButtonConsulta;
    private javax.swing.JButton jButtonExcluir;
    private javax.swing.JButton jButtonFecharJanela;
    private javax.swing.JButton jButtonGeraPdf;
    private javax.swing.JButton jButtonLimpar;
    private javax.swing.JButton jButtonNaoSeiCep;
    private javax.swing.JButton jButtonNovo;
    private javax.swing.JButton jButtonSalvar;
    private javax.swing.JButton jButtonVerificaPendencias;
    private javax.swing.JFormattedTextField jFormattedTextFieldCep;
    private javax.swing.JFormattedTextField jFormattedTextTelefone;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelCPF;
    private javax.swing.JLabel jLabelEmail;
    private javax.swing.JLabel jLabelId;
    private javax.swing.JLabel jLabelNome;
    private javax.swing.JLabel jLabelTelefone;
    private javax.swing.JPanel jPanelBotoes;
    private javax.swing.JPanel jPanelDadosPessoais;
    private javax.swing.JPanel jPanelEndereco;
    private javax.swing.JPanel jPanelTabela;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTableClientesPf;
    private javax.swing.JTextField jTextFieldBairro;
    private javax.swing.JTextField jTextFieldCPF;
    private javax.swing.JTextField jTextFieldCidade;
    private javax.swing.JTextField jTextFieldComplemento;
    private javax.swing.JTextField jTextFieldEmail;
    private javax.swing.JTextField jTextFieldEstado;
    private javax.swing.JTextField jTextFieldId;
    private javax.swing.JTextField jTextFieldLogradouro;
    private javax.swing.JTextField jTextFieldNome;
    private javax.swing.JTextField jTextFieldNumero;
    // End of variables declaration//GEN-END:variables
}
