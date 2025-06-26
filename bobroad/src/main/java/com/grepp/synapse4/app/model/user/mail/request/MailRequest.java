package com.grepp.synapse4.app.model.user.mail.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class MailRequest {
    private final String toEmail;
    private final String tempPassword;
}
