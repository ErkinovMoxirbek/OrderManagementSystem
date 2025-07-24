package com.example.service.email;

import com.example.util.JwtUtil;
import com.example.util.RandomUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
    @Value("${spring.mail.username}")
    private String fromAccount;
    @Value("${server.url}")
    private String serverUrl;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private EmailHistoryService emailHistoryService;


    public void sendRegistrationStyledEmail(String toAccount) {
        Integer smsCode = RandomUtil.fiveDigit();
        String body = """
                <!DOCTYPE html>
                <html lang="uz">
                <head>
                    <meta charset="UTF-8">
                    <title>Email Verification</title>
                </head>
                <body style="font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px;">
                
                    <div style="max-width: 600px; margin: auto; background: #fff; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); padding: 30px; text-align: center;">
                
                        <h2 style="color: #333;">📩 Emailingizni tasdiqlang</h2>
                
                        <p style="font-size: 16px; color: #555; line-height: 1.5;">
                            Assalomu alaykum,<br><br>
                            Xush kelibsiz! Ro‘yxatdan o‘tishni yakunlash uchun quyidagi tugmani bosing.
                        </p>
                
                        <a href="%s/api/auth/registration/email/verification/%s"
                           style="display: inline-block; margin-top: 20px; background-color: #007bff; color: white; text-decoration: none; padding: 12px 25px; border-radius: 4px; font-size: 16px;">
                            ✅ Ro‘yxatdan o‘tishni yakunlash
                        </a>
                
                        <p style="font-size: 14px; color: #999; margin-top: 30px;">
                            Agar siz bu so‘rovni yubormagan bo‘lsangiz, bu xabarni e’tiborsiz qoldiring.
                        </p>
                    </div>
                
                </body>
                </html>
                """;
        String jwtToken = JwtUtil.encodeForRegistration(toAccount, smsCode.toString());
        body = String.format(body, serverUrl, jwtToken);
        // send
        sendMimeMessage("Registration complete", body, toAccount);
        // save to db
        emailHistoryService.create(body, smsCode.toString(), toAccount);
    }

    private String sendSimpleMessage(String subject, String body, String toAccount) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(fromAccount);
        msg.setTo(toAccount);
        msg.setSubject(subject);
        msg.setText(body);
        mailSender.send(msg);

        return "Mail was send";
    }

    private String sendMimeMessage(String subject, String body, String toAccount) {
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            msg.setFrom(fromAccount);

            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            helper.setTo(toAccount);
            helper.setSubject(subject);
            helper.setText(body, true);
            mailSender.send(msg);

        } catch (RuntimeException | MessagingException e) {
            throw new RuntimeException(e);
        }
        return "Mail was send";
    }

    public void sendPasswordResetEmail(String toAccount) {
        String body = """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <title>Title</title>
                </head>
                <body>
                <div style="max-width: 600px; margin: auto; background: #fff; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); padding: 30px; text-align: center;">
                
                    <h2 style="color: #333;">🔒 Yangi Parolni Kiritish</h2>
                
                    <p style="font-size: 16px; color: #555; line-height: 1.5;">
                        Assalomu alaykum,<br><br>
                        Parolingizni o'zgartirish uchun quyidagi tugmani bosing va ochilgan formada yangi parolni kiriting.
                    </p>
                
                
                    <a href="%s/change-password.html?token=%s"
                       style="display: inline-block; margin-top: 20px; background-color: #007bff; color: white; text-decoration: none; padding: 12px 25px; border-radius: 4px; font-size: 16px;">
                        ✅ Parolni o'zgartirish
                    </a>
                
                    <p style="font-size: 14px; color: #999; margin-top: 30px;">
                        Agar siz bu so‘rovni yubormagan bo‘lsangiz, bu xabarni e’tiborsiz qoldiring.
                    </p>
                </div>
                </body>
                </html>
                """;
        String jwtToken = JwtUtil.encodeForRegistration(toAccount,null);
        body = String.format(body, serverUrl, jwtToken);
        // send
        sendMimeMessage("Change", body, toAccount);
        // databasega saqlash
        emailHistoryService.create(body, null , toAccount);
    }

}
