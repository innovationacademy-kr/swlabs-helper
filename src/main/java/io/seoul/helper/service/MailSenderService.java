package io.seoul.helper.service;

import io.seoul.helper.config.auth.dto.SessionUser;
import io.seoul.helper.controller.user.dto.UserResponseDto;
import io.seoul.helper.domain.member.Member;
import io.seoul.helper.domain.member.MemberRole;
import io.seoul.helper.domain.team.Team;
import io.seoul.helper.domain.user.User;
import io.seoul.helper.repository.team.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Objects;


@Service
@AllArgsConstructor
@EnableAsync
public class MailSenderService {
    private final JavaMailSender mailSender;
    private final TeamRepository teamRepo;
    private final UserService userService;

    @Async
    public void sendMail(String sendTo, String title, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(sendTo);
        message.setSubject(title);
        message.setText(content);

        mailSender.send(message);
    }

    @Async
    @Transactional(readOnly = true)
    public void sendMatchMail(Long teamId, String baseUrl) {
        Team team = teamRepo.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team Not Exist"));
        User user = team.getMembers().stream().filter(Member::getCreator).findFirst()
                .orElseThrow(EntityNotFoundException::new).getUser();
        sendMail(user.getEmail(), "Your Team Matched!",
                "Team '#" + team.getId() + " - " + team.getProject().getName() + "' is Matched!\n" +
                        createLinkFromBaseUrl(baseUrl) + "/#" + team.getId());
    }

    @Async
    @Transactional(readOnly = true)
    public void sendEndMail(Long teamId, String baseUrl) {
        Team team = teamRepo.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team Not Exist"));
        team.getMembers().stream()
                .map(Member::getUser)
                .filter(Objects::nonNull)
                .forEach(user -> sendMail(user.getEmail(), "Your Team Ended!",
                        "Team '#" + team.getId() + " - " + team.getProject().getName() + "' is Ended!\n" +
                                createLinkFromBaseUrl(baseUrl) + "/#" + team.getId()));
    }

    @Async
    @Transactional(readOnly = true)
    public void sendJoinMail(SessionUser userSession, Long teamId, String baseUrl) throws Exception {
        Team team = teamRepo.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team Not Exist"));
        UserResponseDto joinedUser = userService.findUserBySession(userSession);
        team.getMembers().stream()
                .filter(member -> member.getRole() == MemberRole.MENTOR)
                .map(Member::getUser)
                .forEach(mentor -> sendMail(mentor.getEmail(),
                        "One of mentees has joined your Team!",
                        "Mentee '" + joinedUser.getNickname() + "' has joined your Team #" + team.getId() + "'" +
                                "\nTeam : " + createLinkFromBaseUrl(baseUrl) + "/#" + team.getId() +
                                "\nMentee : " + createLinkFromBaseUrl("profile.intra.42.fr/users") + "/" + joinedUser.getNickname()));
    }

    @Async
    @Transactional(readOnly = true)
    public void sendOutMail(SessionUser userSession, Long teamId, String baseUrl) throws Exception {
        Team team = teamRepo.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team Not Exist"));
        UserResponseDto outUser = userService.findUserBySession(userSession);
        team.getMembers().stream()
                .filter(member -> member.getRole() == MemberRole.MENTOR)
                .map(Member::getUser)
                .forEach(mentor -> sendMail(mentor.getEmail(),
                        "One of mentees has left your Team!",
                        "Mentee '" + outUser.getNickname() + "' has left your Team #" + team.getId() + "'" +
                                "\nTeam : " + createLinkFromBaseUrl(baseUrl) + "/#" + team.getId() +
                                "\nMentee : " + createLinkFromBaseUrl("profile.intra.42.fr/users") + "/" + outUser.getNickname()));
    }

    private String createLinkFromBaseUrl(String baseUrl) {
        if (baseUrl.equals("localhost")) {
            return "http://localhost:8080";
        } else {
            return "https://" + baseUrl;
        }
    }
}
