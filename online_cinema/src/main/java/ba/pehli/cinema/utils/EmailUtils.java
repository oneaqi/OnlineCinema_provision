package ba.pehli.cinema.utils;

import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.velocity.VelocityEngineUtils;

import ba.pehli.cinema.domain.User;

/**
 * Utility class that is responsible for sending emails from system. All email messages are
 * defined in coresponding velocity templates. Class is instanced in mvc-config.xml
 * @author almir
 *
 */
public class EmailUtils {
	private JavaMailSender mailSender;
	private VelocityEngine velocityEngine;
	
	/**
	 * Send email to user.
	 * @param user User to which we are sending emails
	 * @param subject Subject of email
	 * @param velocityTemplateFile Location and file name of velocity temlate, for example email/templateRegistration.vm
	 * @param velocityTemplateParams Map of all params that are configured in velocity template file
	 * @throws MessagingException
	 */
	public void sendEmail(User user, String subject,
			String velocityTemplateFile,
			Map<String, Object> velocityTemplateParams)
			throws MessagingException {

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
		helper.setFrom("Administrator");
		helper.setTo(user.getUsername());
		helper.setSubject(subject);

		Map<String, Object> params = new HashMap<String, Object>();
		for (String key : velocityTemplateParams.keySet()) {
			params.put(key, velocityTemplateParams.get(key));
		}
		String text = VelocityEngineUtils.mergeTemplateIntoString(
				velocityEngine, velocityTemplateFile, "UTF-8", params);
		helper.setText(text, true);

		mailSender.send(message);
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}
	
}
