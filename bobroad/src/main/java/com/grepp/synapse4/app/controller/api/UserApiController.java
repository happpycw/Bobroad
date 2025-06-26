package com.grepp.synapse4.app.controller.api;

import com.grepp.synapse4.app.model.user.UserService;
import com.grepp.synapse4.app.model.user.dto.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @PatchMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id,
        @AuthenticationPrincipal CustomUserDetails userDetails,
        HttpServletRequest request,
        HttpServletResponse response) {

        if (!userDetails.getUser().getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        userService.softDelete(userDetails.getUser());

        SecurityContextHolder.clearContext();
        new SecurityContextLogoutHandler().logout(request, response, null);

        return ResponseEntity.ok().build();
    }

}
