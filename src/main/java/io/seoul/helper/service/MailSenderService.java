package io.seoul.helper.service;

import io.seoul.helper.config.auth.dto.SessionUser;
import io.seoul.helper.domain.member.Member;
import io.seoul.helper.domain.member.MemberRole;
import io.seoul.helper.domain.team.Team;
import io.seoul.helper.domain.user.User;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
@EnableAsync
public class MailSenderService {
    private JavaMailSender mailSender;

    private final TeamService teamService;
    private final UserService userService;

    @Async
    public void sendMail(User user, String title, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject(title);
        message.setText(content);

        mailSender.send(message);
    }

    @Async
    @Transactional
    public void sendMatchMail(Long teamId, String baseUrl) {
        Team team = teamService.findTeam(teamId);
        User user = null;
        for (Member member : team.getMembers()) {
            if (member.getRole() == MemberRole.MENTEE) {
                user = member.getUser();
            }
        }
        if (user == null)
            return;
        sendMail(user, "Your Team Matched!",
                "Team \'#" + team.getId() + " - " + team.getProject().getName() + "\' is Matched!\n" +
                        createLinkFromBaseUrl(baseUrl) + "/#" + team.getId());
    }

    @Async
    @Transactional
    public void sendEndMail(Long teamId, String baseUrl) {
        Team team = teamService.findTeam(teamId);
        for (Member member : team.getMembers()) {
            User user = member.getUser();
            sendMail(user, "Your Team Ended!",
                    "Team \'#" + team.getId() + " - " + team.getProject().getName() + "\' is Ended!\n" +
                            createLinkFromBaseUrl(baseUrl) + "/#" + team.getId());
        }
    }

    @Async
    @Transactional
    public void sendJoinMail(SessionUser userSession, Long teamId, String baseUrl) throws Exception {
        Team team = teamService.findTeam(teamId);
        User joinedUser = userService.findUserBySession(userSession);
        for (Member member : team.getMembers()) {
            if (member.getRole() == MemberRole.MENTOR) {
                User mentor = member.getUser();
                sendMail(mentor,
                        "One of mentees has joined your Team!",
                        "Mentee \'" + joinedUser.getNickname() + "\' has joined your Team #" + team.getId() + "\'" +
                                "\nTeam : " + createLinkFromBaseUrl(baseUrl) + "/#" + team.getId() +
                                "\nMentee : " + createLinkFromBaseUrl("profile.intra.42.fr/users") + "/" + joinedUser.getNickname());
                break;
            }
        }
    }

    @Async
    @Transactional
    public void sendOutMail(SessionUser userSession, Long teamId, String baseUrl) throws Exception {
        Team team = teamService.findTeam(teamId);
        User outUser = userService.findUserBySession(userSession);
        for (Member member : team.getMembers()) {
            if (member.getRole() == MemberRole.MENTOR) {
                User mentor = member.getUser();
                sendMail(mentor,
                        "One of mentees has left your Team!",
                        "Mentee \'" + outUser.getNickname() + "\' has left your Team #" + team.getId() + "\'" +
                                "\nTeam : " + createLinkFromBaseUrl(baseUrl) + "/#" + team.getId() +
                                "\nMentee : " + createLinkFromBaseUrl("profile.intra.42.fr/users") + "/" + outUser.getNickname());
                break;
            }
        }
    }

    private String createLinkFromBaseUrl(String baseUrl) {
        if (baseUrl.equals("localhost")) {
            return "http://localhost:8080";
        } else {
            return "https://" + baseUrl;
        }
    }
}
