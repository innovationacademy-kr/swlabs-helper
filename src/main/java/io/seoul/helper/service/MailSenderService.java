package io.seoul.helper.service;

import io.seoul.helper.domain.team.Team;
import io.seoul.helper.domain.user.User;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@EnableAsync
public class MailSenderService {
    private JavaMailSender mailSender;

    @Async
    public void sendMail(User user, String title, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject(title);
        message.setText(content);

        mailSender.send(message);
    }

    @Async
    public void sendMatchMail(User user, Team team) {
        sendMail(user, "Your Team Matched!", "Team\n" +
                "\'#" + team.getId() + " : " + team.getProject().getName() + "\' is Matched!");
    }

    @Async
    public void sendEndMail(User user, Team team) {
        sendMail(user, "Your Team Ended!", "Team\n" +
                "\'#" + team.getId() + " : " + team.getProject().getName() + "\' is Ended!");
    }
}
