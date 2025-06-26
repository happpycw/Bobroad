package com.grepp.synapse4.app.model.user;

import com.grepp.synapse4.app.model.user.mail.request.MailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    // 밑에 태그는 현재 WebClientConfig 에
    // 3개의 빈이 등록되어 어떤 빈을
    // 호출할지 인식하지못해 명시적으로 선언해놨습니다.
    @Qualifier("mailWebClient")
    private final WebClient mailWebClient;

    // 메일서버로 요청
    public void sendToServerTempPasswordEmail(String toEmail, String tempPassword) {
        MailRequest request = new MailRequest(toEmail, tempPassword);

        mailWebClient.post()
                .uri("/mail/send")// 메일 서버 컨트로럴로 요청
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
