package sample.foody;

import com.sun.mail.smtp.SMTPMessage;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.Properties;

public class bill_mail {

    public static void sendMail(String to) {



        String from = "MAIL_ID";

        String host = "smtp.gmail.com";

        Properties properties = System.getProperties();

        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication("MAIL_ID", "PASS");

            }

        });

        session.setDebug(true);

        try {



            MimeMessage m = new SMTPMessage(session);
            MimeMultipart content = new MimeMultipart();

            m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            MimeBodyPart mainPart = new MimeBodyPart();
            mainPart.setText("Hello FOODY\n\t We have got the bill for your order attached with this mail. Visit again ;)");
            content.addBodyPart(mainPart);


            MimeBodyPart imagePart = new MimeBodyPart();

            imagePart.attachFile("bill.png");
            content.addBodyPart(imagePart);

            m.setContent(content);
            m.setSubject("Bill for FOODY!!");


            System.out.println("sending...");
            Transport.send(m);
            System.out.println("Sent message successfully....");
        } catch (MessagingException | IOException mex) {
            mex.printStackTrace();
        }

    }
}

