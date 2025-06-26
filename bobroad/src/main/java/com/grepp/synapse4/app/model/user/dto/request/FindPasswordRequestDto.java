package com.grepp.synapse4.app.model.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FindPasswordRequestDto {
    private String userAccount;
    private String name;
    private String email;
}
