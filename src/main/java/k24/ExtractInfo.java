package k24;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class ExtractInfo {

	public static void main(String[] args) {
		Properties properties = new Properties();
		
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		Session session = Session.getDefaultInstance(properties,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication("mailOfSender","passOfSender");
					}
				});
		
		try {
			File inputF = new File("absolutePathForFile.html");
			Document doc = Jsoup.parse(inputF, "UTF-8", "http://example.com/");
		
		
			Elements rows = doc.select(".waffle tbody tr");
			
			for (int i=0; i<rows.size(); i++) {
				if (i==173 || i==0 || i==1)
					continue;
		//		Elements sdi = rows.get(i).select(".s2");
				
				Elements td = rows.get(i).select("td");
				
				if (!td.get(0).childNodes().isEmpty() && !td.get(3).childNodes().isEmpty()) {
				
					if (td.get(0).childNode(0).toString().equals(" XXX ")) continue;
					System.out.println(td.get(0).childNode(0).toString().concat("@di.uoa.gr") + "  --  " + td.get(3).childNode(0));
				
					try {
						  Message msg = new MimeMessage(session);
						  msg.setFrom(new InternetAddress("sender email address here"));
						  msg.addRecipient(Message.RecipientType.TO, new InternetAddress(td.get(0).childNode(0).toString().concat("@di.uoa.gr")));
						  msg.setSubject("Subject");
						  msg.setText("Text ..  " + td.get(3).childNode(0).toString() );
						  
						  
						  Transport transport = session.getTransport("smtp");
						  transport.connect("smtp.gmail.com", "sender email address here");
						  transport.sendMessage(msg, msg.getAllRecipients());
						  transport.close();
						  
					} catch (AddressException e) {
						  System.out.println(e.getMessage());
					} catch (MessagingException e) {
						  System.out.println(e.getMessage());
					}
					
				}
		
			}
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
