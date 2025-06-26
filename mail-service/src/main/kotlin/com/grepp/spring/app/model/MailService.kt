package com.grepp.spring.app.model

import com.grepp.spring.app.controller.payload.SmtpRequest
import com.grepp.spring.app.model.code.MailTemplatePath
import com.grepp.spring.infra.mail.MailTemplate
import com.grepp.spring.infra.mail.SmtpDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import org.slf4j.LoggerFactory
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class MailService(
    private val mailSender: JavaMailSender
) {
    fun send(request: SmtpRequest){
        val subject = "[밥로드] 임시 비밀번호 안내";
        val content = """요청하신 임시 비밀번호는 아래와 같습니다.
            
            임시 비밀번호: ${request.tempPassword}
            
            로그인 후 반드시 비밀번호를 변경해주세요.
        """.trimIndent()

        val message = SimpleMailMessage().apply {
            setTo(request.toEmail)
            setSubject(subject)
            setText(content)
        }
        mailSender.send(message)
    }
}