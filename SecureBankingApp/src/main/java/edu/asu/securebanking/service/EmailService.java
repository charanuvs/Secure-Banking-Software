package edu.asu.securebanking.service;

import edu.asu.securebanking.constants.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

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
}
