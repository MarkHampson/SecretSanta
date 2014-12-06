import java.lang.IllegalArgumentException;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class SecretMail {

    private static Transport transport;
    private static Properties props;
    private static Session session;

    public static void main(String[] args) {

        if(args.length != 1) {
            throw new IllegalArgumentException("One argument required");
        }

        In in = new In(args[0]);
        String user = in.readLine();
        String pass = in.readLine();
        
        RandomizedQueue<Santa> giveQ = new RandomizedQueue();
        RandomizedQueue<Santa> rxQ = new RandomizedQueue();

        //collect the list of Santa Clauses
        while(in.hasNextLine()) {
            String santaFile = in.readLine();
            Santa santa = new Santa(santaFile);
            giveQ.enqueue(santa);
        }

        //assign someone to every santa claus, excluding self
        boolean noConflict = false;
        int count = 0;

        while(!noConflict) {
            
            //Initialize the RX queue
            for(Santa s: giveQ) {
                s.setGiveTo(null);
                s.setReceiveFrom(null);
                rxQ.enqueue(s);
            }

            //make the assignments
            for(Santa s: giveQ) {
                Santa rx = rxQ.dequeue();
                while(rx == s && !rxQ.isEmpty()) {
                   rxQ.enqueue(s); //if s was dequeued, 
                                   //put it back on
                   rx = rxQ.dequeue(); //and try again
                }
                s.setGiveTo(rx);
                rx.setReceiveFrom(s);
            }

            //Test the Queue for conflicts
            noConflict = true;
            for(Santa s: giveQ) {
                if(s.isConflicted()) noConflict = false;
            }
            count++;
        }
        System.out.println("Attempts: " + count);
        
//        for(Santa s: giveQ) {
//            System.out.println(s);
//        }

        //Send the emails 
        logIn(user, pass);
        String[] to = new String[1];
        for(Santa s: giveQ) {
            to[0] = s.getEmail();
            sendGmail(user, to, s.getMessage());
        //    showMessage(s);
        }
        logOut();
    }
    
    private static void showMessage(Santa s) {
        String to = s.getEmail();
        String body = s.getMessage();
        System.out.println(to);
        System.out.println(body);
    }

    private static void logIn(String from, String pass) {
        try {
        props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        
        session = Session.getDefaultInstance(props);
        transport = session.getTransport("smtp");
        transport.connect(host, from, pass); 
        }
        catch (AddressException ae) {
            ae.printStackTrace();
        }
        catch (MessagingException me) {
            me.printStackTrace();
        }
    }

    private static void logOut() {
        try {
        transport.close();
        }
        catch (MessagingException me) {
            me.printStackTrace();
        }
    }

    private static void sendGmail(String from, String[] to, String body){
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(from));
            InternetAddress[] toAddress = new InternetAddress[to.length];

            for(int i = 0; i < to.length; i++) {
                toAddress[i] = new InternetAddress(to[i]);
            }

            for(int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }

            message.setSubject("Secret Santa");
            message.setText(body);
            transport.sendMessage(message, message.getAllRecipients());
        }
        catch (AddressException ae) {
            ae.printStackTrace();
        }
        catch (MessagingException me) {
            me.printStackTrace();
        }
    }
}
        
