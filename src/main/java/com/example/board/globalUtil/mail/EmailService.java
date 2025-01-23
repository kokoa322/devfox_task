package com.example.board.globalUtil.mail;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.sun.mail.util.MailConnectException;

import lombok.RequiredArgsConstructor;

import org.springframework.mail.MailException;
import org.springframework.mail.MailParseException;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;


@Component
@RequiredArgsConstructor
public class EmailService {
	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
	
    private final JavaMailSender mailSender;

    @Value("#{emailProperties['email.from']}")
    private String from;

    @Value("#{emailProperties['email.to']}")
    private String to;
    
    public void sendEmail(String subject, String message) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false);  // true는 멀티파트 지원
            
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(message);

            // HTML 메일을 보낼 때는 setText에서 두 번째 인자를 true로 설정
            // helper.setText("<h1>HTML content</h1>", true);

            // 이메일 보내기
            mailSender.send(mimeMessage);
            System.out.println("이메일 전송 완료!");
        } catch (SendFailedException e) {
        	
        	logger.error(EmailErrorMessage.SEND_FAILED.getMessage()+"{}", e.getMessage(), e);
        	throw new EmailConnectionException(EmailErrorMessage.SEND_FAILED.getMessage(), e);
        } catch (MailParseException e) {
        	
        	logger.error(EmailErrorMessage.MAIL_PARSE_FAILED.getMessage()+"{}", e.getMessage(), e);
        	throw new EmailConnectionException(EmailErrorMessage.MAIL_PARSE_FAILED.getMessage(), e);
        } catch (AuthenticationFailedException e) {
        	
        	logger.error(EmailErrorMessage.AUTHENTICATION_FAILED.getMessage()+"{}", e.getMessage(), e);
        	throw new EmailConnectionException(EmailErrorMessage.AUTHENTICATION_FAILED.getMessage(), e);
        } catch (MailConnectException e) {
        	
        	logger.error(EmailErrorMessage.MAIL_SERVER_CONNECTION_FAILED.getMessage()+"{}", e.getMessage(), e);
        	throw new EmailConnectionException(EmailErrorMessage.MAIL_SERVER_CONNECTION_FAILED.getMessage(), e);
        } catch (AddressException e) {
        	
        	logger.error(EmailErrorMessage.INVALID_EMAIL_ADDRESS.getMessage()+"{}", e.getMessage(), e);
        	throw new EmailConnectionException(EmailErrorMessage.INVALID_EMAIL_ADDRESS.getMessage(), e);
        } catch (MessagingException e) {
			
        	logger.error(EmailErrorMessage.MESSAGING_FAILED.getMessage()+"{}", e.getMessage(), e);
        	throw new EmailConnectionException(EmailErrorMessage.MESSAGING_FAILED.getMessage(), e);
		} catch (MailException e) {
        	
			logger.error(EmailErrorMessage.MESSAGING_FAILED.getMessage()+"{}", e.getMessage(), e);
        	throw new EmailConnectionException(EmailErrorMessage.GENERAL_MAIL_ERROR.getMessage(), e);
        } 
    }
}
