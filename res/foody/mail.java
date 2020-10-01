package sample.foody;


import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;



public class mail {
    static Session sesh;
    static Properties prop = new Properties();
    static String UN, PW, host, port, rec , mhead, msub, cTEXT;

    public static void sendMail(String recepient,String otp){
        UN = "MAIL_ID";
        PW = "PASSWORD";
        host ="smtp.gmail.com";
        port = "487";
        auth();
        rec = recepient;
        mhead = "OTP";
        msub ="OTP";
        cTEXT = ("dear "+recepient+":\n\tYour OTP for your request is "+otp+".\n\nPlease return to the verification page to use our services :)");
        Mail(rec, msub, cTEXT);
    }

    public static void auth() {
        boolean auth = chk(UN, PW);
        if(!auth) {
            System.out.print("Not auth");
        } else if (auth) {
            System.out.print("Auth");
        } else {
            System.out.print("Not auth");
        }}

    public static boolean chk(String UN, String PW) /*throws AuthenticationFailedException /*AddressException*/ {

        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");

        prop.put("mail.smtp.host", host/*[0]*/);
        prop.put("mail.smtp.port", port);

        boolean check = true;
        //
        try {
            InternetAddress e = new InternetAddress();
            e.validate();
        } catch (AddressException e) {
            e.getStackTrace();
            check = true;
        }

        if(check) {
            sesh = Session.getInstance(prop,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(UN, PW);
                        }
                    });
        }

        return true;
    }
    public static void Mail(String to, String sub, String cont) /*throws IOException*/ {

        try {

            Message m = new MimeMessage(sesh);
            m.setFrom(new InternetAddress("MAIL_ID"));
            m.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            m.setSubject(sub);
            m.setSentDate(new Date());
            m.setContent(cont, "text/plain");
            m.setHeader("EMAIL HEAD", mhead);
            System.out.println("\n \n \n \t >> ??????? " + m.getContentType());
            System.out.println("\n \n \n \t >> ??????? " + m.getDataHandler());
            System.out.println("\n \n \n \t >> ??????? " + m.getSubject());


            System.out.println(" otp");

            Transport.send(m);

            System.out.println("end otp");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } // end try/catch //

        // });
        System.out.println("end mail");
    } // end Mail() //


}


