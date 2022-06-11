package org.promise.user.service.util;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author moqi
 * @date 2022/5/18 13:37
 */
@Component
public class EmailUtil {

    @Resource
    MailSender mailSender;

    private static final String SUBJECT="COLLECT众测平台注册验证码";

    public void sendVerificationCode(String receiver,String verificationCode){
        send(receiver,SUBJECT,getText(verificationCode));
    }

    public void send(String receiver,String subject,String text){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(receiver);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(text);
        simpleMailMessage.setSentDate(new Date());
        mailSender.send(simpleMailMessage);
    }

    private String getText(String code){
        return "尊敬的用户，欢迎您使用COLLECT众测平台。您的验证码为：" +
                code +
                "（10分钟内有效）。为了保证您的账户安全，请勿向任何人提供此验证码！";
    }

}
