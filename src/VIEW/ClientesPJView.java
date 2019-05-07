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

// Import das Exceções
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

// Import necessário para tabalhar com ArrayList
import java.util.List;

// Import de componentes da interface gráfica
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableColumn;

/**
 *
 * @author Rodrigo Fereira Rodrigues
 */
public class ClientesPJView extends javax.swing.JInternalFrame {

    public static final String BARRA = System.getProperty("file.separator");
    public static final String PASTA_USUARIO = System.getProperty("user.home");
    public static String BASE_RELATORIOS = PASTA_USUARIO + BARRA + "labprog" + BARRA + "rfassistencia" + BARRA + "relatorio.pdf";

    /**
     * Creates new form ClientesPFView
     */
    public ClientesPJView() {
        initComponents();
        jTableClientesPf.setModel(tableModel);
        preencheTabela();

        //ajustaTabela();
        habilitaCampos(false);
        habilitaBotoes(false);
    }

    //Instancia minha jTable com base no modelo de Tabela definido no pacote MODEL
    ClientesPFTableModel tableModel = new ClientesPFTableModel();

    public void ajustaTabela() {

        TableColumn id = jTableClientesPf.getTableHeader().getColumnModel().getColumn(0);
        TableColumn cpf = jTableClientesPf.getTableHeader().getColumnModel().getColumn(1);
        TableColumn nome = jTableClientesPf.getTableHeader().getColumnModel().getColumn(2);
        TableColumn email = jTableClientesPf.getTableHeader().getColumnModel().getColumn(3);
        TableColumn telefone = jTableClientesPf.getTableHeader().getColumnModel().getColumn(4);
        TableColumn cep = jTableClientesPf.getTableHeader().getColumnModel().getColumn(5);
        TableColumn rua = jTableClientesPf.getTableHeader().getColumnModel().getColumn(6);
        TableColumn numero = jTableClientesPf.getTableHeader().getColumnModel().getColumn(7);
        TableColumn complemento = jTableClientesPf.getTableHeader().getColumnModel().getColumn(8);
        TableColumn bairro = jTableClientesPf.getTableHeader().getColumnModel().getColumn(9);
        TableColumn cidade = jTableClientesPf.getTableHeader().getColumnModel().getColumn(10);
        TableColumn estado = jTableClientesPf.getTableHeader().getColumnModel().getColumn(11);

        int tamid = 35;
        int tamcpf = 105;
        int tamnome = 260;
        int tamemail = 260;
        int tamtelefone = 110;
        int tamcep = 72;
        int tamrua = 230;
        int tamnumero = 30;
        int tamcomplemento = 60;
        int tambairro = 100;
        int tamcidade = 150;
        int tamestado = 35;

        id.setMinWidth(20);
        id.setPreferredWidth(tamid);
        id.setMaxWidth(tamid);

        cpf.setMinWidth(80);
        cpf.setPreferredWidth(tamcpf);
        cpf.setMaxWidth(tamcpf);

        nome.setMinWidth(50);
        nome.setPreferredWidth(tamnome);
        nome.setMaxWidth(tamnome);

        email.setMinWidth(50);
        email.setPreferredWidth(tamemail);
        email.setMaxWidth(tamemail);

        telefone.setMinWidth(50);
        telefone.setPreferredWidth(tamtelefone);
        telefone.setMaxWidth(tamtelefone);

        cep.setMinWidth(50);
        cep.setPreferredWidth(tamcep);
        cep.setMaxWidth(tamcep);

        rua.setMinWidth(50);
        rua.setPreferredWidth(tamrua);
        rua.setMaxWidth(tamrua);

        numero.setMinWidth(50);
        numero.setPreferredWidth(tamnumero);
        numero.setMaxWidth(tamnumero);

        complemento.setMinWidth(50);
        complemento.setPreferredWidth(tamcomplemento);
        complemento.setMaxWidth(tamcomplemento);

        bairro.setMinWidth(20);
        bairro.setPreferredWidth(tambairro);
        bairro.setMaxWidth(tambairro);

        cidade.setMinWidth(20);
        cidade.setPreferredWidth(tamcidade);
        cidade.setMaxWidth(tamcidade);

        estado.setMinWidth(20);
        estado.setPreferredWidth(tamestado);
        estado.setMaxWidth(tamestado);

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
    public void geraPdf() {

        ClientesPFController cliPfCtrl = new ClientesPFController();
        Document doc = new Document();
        try {
            doc.setPageSize(PageSize.A4.rotate());
            List<ClientesPFModel> minhaLista = cliPfCtrl.leClientesPf();
            PdfWriter.getInstance(doc, new FileOutputStream(BASE_RELATORIOS));
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

            doc.close();
            Process pro = Runtime.getRuntime().exec("cmd.exe /c  " + BASE_RELATORIOS);
            pro.waitFor();
        } catch (InterruptedException | IOException | DocumentException ex) {
            Logger.getLogger(ClientesPJView.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ClientesPJView.class
                    .getName()).log(Level.SEVERE, null, ex);
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
        jLabelRazaoSocial = new javax.swing.JLabel();
        jLabelId = new javax.swing.JLabel();
        jLabelEmail = new javax.swing.JLabel();
        jLabelTelefone = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldNomeFantasia = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldIE = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldIM = new javax.swing.JTextField();
        jPanelTabela = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableClientesPf = new javax.swing.JTable();
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

        setMaximizable(true);
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

        try {
            jFormattedTextTelefone.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##) ##### - ####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabelCPF.setText("CNPJ:");

        jLabelRazaoSocial.setText("Razão Social:");

        jLabelId.setText("ID:");

        jLabelEmail.setText("Email:");

        jLabelTelefone.setText("Telefone");

        jLabel1.setText("Nome Fantasia:");

        jLabel2.setText("IE:");

        jLabel3.setText("IM:");

        javax.swing.GroupLayout jPanelDadosPessoaisLayout = new javax.swing.GroupLayout(jPanelDadosPessoais);
        jPanelDadosPessoais.setLayout(jPanelDadosPessoaisLayout);
        jPanelDadosPessoaisLayout.setHorizontalGroup(
            jPanelDadosPessoaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDadosPessoaisLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelDadosPessoaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelDadosPessoaisLayout.createSequentialGroup()
                        .addGroup(jPanelDadosPessoaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelDadosPessoaisLayout.createSequentialGroup()
                                .addComponent(jLabelEmail)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelDadosPessoaisLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldNomeFantasia, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel2)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelDadosPessoaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelDadosPessoaisLayout.createSequentialGroup()
                                .addComponent(jLabelTelefone)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jFormattedTextTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelDadosPessoaisLayout.createSequentialGroup()
                                .addComponent(jTextFieldIE, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldIM, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanelDadosPessoaisLayout.createSequentialGroup()
                        .addComponent(jLabelId)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldId, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelCPF)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldCPF, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelRazaoSocial)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldNome, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
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
                    .addComponent(jLabelRazaoSocial)
                    .addComponent(jTextFieldNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelDadosPessoaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextFieldNomeFantasia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jTextFieldIE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jTextFieldIM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanelDadosPessoaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelEmail)
                    .addComponent(jLabelTelefone)
                    .addComponent(jFormattedTextTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

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

        jButtonConsulta.setText("Consultar");
        jButtonConsulta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConsultaActionPerformed(evt);
            }
        });

        jButtonExcluir.setText("Excluir");
        jButtonExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExcluirActionPerformed(evt);
            }
        });

        jButtonAlterar.setText("Alterar");
        jButtonAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAlterarActionPerformed(evt);
            }
        });

        jButtonLimpar.setText("Limpar Campos");
        jButtonLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLimparActionPerformed(evt);
            }
        });

        jButtonSalvar.setText("Salvar");
        jButtonSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSalvarActionPerformed(evt);
            }
        });

        jButtonVerificaPendencias.setText("Verifica Pendências noFormulário");
        jButtonVerificaPendencias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVerificaPendenciasActionPerformed(evt);
            }
        });

        jButtonNovo.setText("Novo");
        jButtonNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNovoActionPerformed(evt);
            }
        });

        jButtonCancelar.setText("Cancelar");
        jButtonCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelarActionPerformed(evt);
            }
        });

        jButtonGeraPdf.setText("Gerar PDF");
        jButtonGeraPdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGeraPdfActionPerformed(evt);
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
                        .addComponent(jButtonConsulta)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonExcluir)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonAlterar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonLimpar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonSalvar))
                    .addGroup(jPanelBotoesLayout.createSequentialGroup()
                        .addComponent(jButtonNovo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonCancelar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonVerificaPendencias)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonGeraPdf)))
                .addContainerGap(60, Short.MAX_VALUE))
        );
        jPanelBotoesLayout.setVerticalGroup(
            jPanelBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBotoesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanelBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonConsulta)
                    .addComponent(jButtonExcluir)
                    .addComponent(jButtonAlterar)
                    .addComponent(jButtonLimpar)
                    .addComponent(jButtonSalvar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonVerificaPendencias)
                    .addComponent(jButtonNovo)
                    .addComponent(jButtonCancelar)
                    .addComponent(jButtonGeraPdf))
                .addGap(36, 36, 36))
        );

        jPanelEndereco.setBorder(javax.swing.BorderFactory.createTitledBorder("Endereço"));

        jLabel5.setText("CEP");

        try {
            jFormattedTextFieldCep.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#####-###")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jButtonBuscaCep.setText("Buscar");
        jButtonBuscaCep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBuscaCepActionPerformed(evt);
            }
        });

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
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jFormattedTextFieldCep, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonBuscaCep)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldLogradouro)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(68, 68, 68))
            .addGroup(jPanelEnderecoLayout.createSequentialGroup()
                .addGroup(jPanelEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelEnderecoLayout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldComplemento, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldBairro, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelEnderecoLayout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldCidade, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButtonNaoSeiCep, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(281, Short.MAX_VALUE))
        );
        jPanelEnderecoLayout.setVerticalGroup(
            jPanelEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEnderecoLayout.createSequentialGroup()
                .addGroup(jPanelEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jFormattedTextFieldCep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonBuscaCep)
                        .addComponent(jTextFieldLogradouro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6)
                        .addComponent(jTextFieldNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jTextFieldComplemento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldBairro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jTextFieldCidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonNaoSeiCep)
                .addContainerGap(40, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanelBotoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanelDadosPessoais, javax.swing.GroupLayout.PREFERRED_SIZE, 571, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jPanelEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanelTabela, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanelEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanelDadosPessoais, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanelBotoes, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
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
            if (cliPfCtrl.gravaClientesPf(listaPF)) {
                JOptionPane.showMessageDialog(this, "Salvo com sucesso");
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
        geraPdf();
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
            Logger.getLogger(ClientesPJView.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButtonNaoSeiCepActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAlterar;
    private javax.swing.JButton jButtonBuscaCep;
    private javax.swing.JButton jButtonCancelar;
    private javax.swing.JButton jButtonConsulta;
    private javax.swing.JButton jButtonExcluir;
    private javax.swing.JButton jButtonGeraPdf;
    private javax.swing.JButton jButtonLimpar;
    private javax.swing.JButton jButtonNaoSeiCep;
    private javax.swing.JButton jButtonNovo;
    private javax.swing.JButton jButtonSalvar;
    private javax.swing.JButton jButtonVerificaPendencias;
    private javax.swing.JFormattedTextField jFormattedTextFieldCep;
    private javax.swing.JFormattedTextField jFormattedTextTelefone;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelCPF;
    private javax.swing.JLabel jLabelEmail;
    private javax.swing.JLabel jLabelId;
    private javax.swing.JLabel jLabelRazaoSocial;
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
    private javax.swing.JTextField jTextFieldIE;
    private javax.swing.JTextField jTextFieldIM;
    private javax.swing.JTextField jTextFieldId;
    private javax.swing.JTextField jTextFieldLogradouro;
    private javax.swing.JTextField jTextFieldNome;
    private javax.swing.JTextField jTextFieldNomeFantasia;
    private javax.swing.JTextField jTextFieldNumero;
    // End of variables declaration//GEN-END:variables
}
