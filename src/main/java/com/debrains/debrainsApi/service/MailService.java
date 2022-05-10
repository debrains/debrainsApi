package com.debrains.debrainsApi.service;

import com.debrains.debrainsApi.common.MailHandler;
import com.debrains.debrainsApi.dto.MailDTO;
import com.debrains.debrainsApi.entity.Mail;
import com.debrains.debrainsApi.entity.User;
import com.debrains.debrainsApi.repository.MailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;
    private final MailRepository mailRepository;
    private final ModelMapper modelMapper;

    public void sendMail(MailDTO mail, MultipartFile[] file) {
        try {
            MailHandler mailHandler = new MailHandler(mailSender);
            mailHandler.setTo(mail.getToAddr());
            mailHandler.setSubject(mail.getTitle());
            String htmlContent = "<p>" + mail.getContent() + "</p>";
            mailHandler.setText(htmlContent, true);
            if (file != null) {
                for (int i = 0; i < file.length; i++) {
                    mailHandler.setAttach(file[i].getOriginalFilename(), file[i]);
                }
            }
            mailHandler.send();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Mail entity = modelMapper.map(mail, Mail.class);
        mailRepository.save(entity);
    }

    public void sendWelcomeMail(User user) {
        String title = "Welcome To Debrains";
        String content = "<p>" + "환영합니다. 데브레인에 가입이 완료되었습니다.<br>" +
                "<a href='https://debrain.co.kr'>데브레인으로 이동</a>" + "</p>";
        try {
            MailHandler mailHandler = new MailHandler(mailSender);
            mailHandler.setTo(user.getEmail());
            mailHandler.setSubject(title);
            mailHandler.setText(content, true);
            mailHandler.send();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Mail entity = Mail.builder()
                .toAddr(user.getEmail())
                .title(title)
                .content(content)
                .user(user)
                .build();
        mailRepository.save(entity);
    }

}
