package UTIL;

import java.util.Properties;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EnviaEmail {

    private static String remetente;
    private static String assunto;
    private static String corpo;

    public static void enviaEmailCadastro(String tipo, String destinatario, String chamado, String dadosEmail) {

        if (tipo.equals("cadastro")) {
            remetente = "cadastro.rfassistencia@gmail.com";
            assunto = "Cadastro efetuado na RF Assistência";
            corpo = "Obrigado por se cadastrar na RF Assistência Técnica" + "\n\n"
                    + "Confira abaixo se os seus dados estão corretos" + "\n\n"
                    + dadosEmail + "\n"
                    + "\n\nENVIO AUTOMÁTIO, NÃO RESPONDER";
        } else if (tipo.equals("cadastronote")) {
            remetente = "cadastro.rfassistencia@gmail.com";
            assunto = "Cadastro de equipamento na RF Assistência";
            corpo = "O seguinte equipamento foi cadastrado no nosso banco de dados" + "\n\n"
                    + "Confira abaixo se os dados do equipamento estão corretos" + "\n\n"
                    + dadosEmail + "\n"
                    + "\n\nENVIO AUTOMÁTIO, NÃO RESPONDER";
        } else if (tipo.equals("abertura")) {
            remetente = "chamados.rfassistencia@gmail.com";
            assunto = "Chamado "+ chamado +" abertona RF Assistência";
            corpo = "Uma ocorrência de chamado foi aberta para seu equipamento" + "\n\n"
                    + "Confira abaixo se os dados do chamado estão corretos" + "\n\n"
                    + dadosEmail + "\n"
                    + "\n\nENVIO AUTOMÁTIO, NÃO RESPONDER";
        } else if (tipo.equals("fechamento")) {
            remetente = "chamados.rfassistencia@gmail.com";
            assunto = "Chamado "+ chamado +" fechado na RF Assistência";
            corpo = "Uma ocrrência de chamado do seu equipamento foi encerrada" + "\n\n"
                    + "Confira abaixo se a solução está correta" + "\n\n"
                    + dadosEmail + "\n"
                    + "\n\nENVIO AUTOMÁTIO, NÃO RESPONDER";
        } else if (tipo.equals("orcamento")) {
            remetente = "chamados.rfassistencia@gmail.com";
            assunto = "RF Assistência - Envio de orçamento para aprovação";
            corpo = "Após análise em nosso laboratório, faz-se necessário a substituição da(s) seguinte(s) peça(s)" + "\n\n"
                    + "Confira abaixo e se estiver de acordo responda esse email com a mensagem 'DE ACORDO' " + "\n"
                    + "Para que a equipe técnica dê prosseguimento no reparo do seu equipamento " + "\n"
                    + "Do contrário, entre em contato ou compareça na nossa assistência " + "\n\n"
                    + dadosEmail + "\n"
                    + "\n\nENVIO AUTOMÁTIO";
        }

        Properties props = new Properties();
        /**
         * Parâmetros de conexão com servidor Gmail
         */
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remetente, "rfr017450");
            }
        });

        /**
         * Ativa Debug para sessão
         */
        session.setDebug(true);

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remetente)); //Remetente

            Address[] toUser = InternetAddress //Destinatário(s)
                    .parse(destinatario);

            message.setRecipients(Message.RecipientType.TO, toUser);
            message.setSubject(assunto);//Assunto
            message.setText(corpo);

            /**
             * Método para enviar a mensagem criada
             */
            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
