package edu.asu.securebanking.service;

import edu.asu.securebanking.constants.AppConstants;
import edu.asu.securebanking.exceptions.AppBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Created by Vikranth on 10/18/2015.
 */
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Sends an email
     *
     * @param to
     * @param subject
     * @param body
     */
    public void sendEmail(String to,
                          String subject,
                          String body) {
        SimpleMailMessage msg = new SimpleMailMessage();

        msg.setFrom(AppConstants.BANK_FROM_ADDR);
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(body);

        mailSender.send(msg);
    }

    /**
     * @param to
     * @param subject
     * @param body
     * @param attachment
     */
    public void sendEmail(String to,
                          String subject,
                          String body,
                          String attachment) throws AppBusinessException {
        MimeMessage message = mailSender.createMimeMessage();
        SimpleMailMessage msg = new SimpleMailMessage();

        msg.setFrom(AppConstants.BANK_FROM_ADDR);
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(body);
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(msg.getFrom());
            helper.setTo(msg.getTo());
            helper.setSubject(msg.getSubject());
            helper.setText(String.format(
                    msg.getText()));

            FileSystemResource file = new FileSystemResource(attachment);
            helper.addAttachment(file.getFilename(), file);

        } catch (MessagingException e) {
            throw new AppBusinessException("Unable to send an email. Please try again.");
        }
        mailSender.send(message);
    }

}
