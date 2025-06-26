package com.grepp.synapse4.app.controller.web.user;

import com.grepp.synapse4.app.model.user.UserService;
import com.grepp.synapse4.app.model.user.dto.request.FindIdRequestDto;
import com.grepp.synapse4.app.model.user.dto.request.FindPasswordRequestDto;
import com.grepp.synapse4.app.model.user.dto.response.FindIdResponseDto;
import com.grepp.synapse4.app.model.user.dto.request.UserSignUpRequest;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/signin")
    public String signInForm() {
        return "user/signin";
    }

    @GetMapping("/signup")
    public String signUpForm(Model model) {
        model.addAttribute("userSignUpRequest", new UserSignUpRequest());
        return "user/signup";
    }

    @PostMapping("/signup")
    public String processSignUp(@Valid @ModelAttribute("userSignUpRequest") UserSignUpRequest request,
        BindingResult bindingResult) {

        // 1차: @Valid 유효성 검사 실패 시
        if (bindingResult.hasErrors()) {
            return "user/signup";
        }

        // 2차: 중복 필드 검사
        userService.validateDuplicateUser(request, bindingResult);

        if (bindingResult.hasErrors()) {
            return "user/signup";
        }

        userService.signup(request);
        return "redirect:/user/signin";
    }

    @GetMapping("/find-id")
    public String showFindIdForm(Model model) {
        model.addAttribute("findIdRequestDto", new FindIdRequestDto());
        return "user/find-id";
    }

    @PostMapping("/find-id")
    public String processFindId(@ModelAttribute FindIdRequestDto requestDto, Model model) {

        Optional<FindIdResponseDto> responseDto = userService.findUserAccount(
            requestDto.getName(), requestDto.getEmail());

        if (responseDto.isPresent()) {
            model.addAttribute("result", responseDto.get());
            return "user/find-id-result";
        }
        else {
            model.addAttribute("error", "입력하신 정보와 일치하는 사용자가 없습니다.");
            return "user/find-id";
        }
    }

    @GetMapping("/find-password")
    public String showFindPasswordForm(Model model) {
        model.addAttribute("findPasswordRequestDto", new FindPasswordRequestDto());
        return "user/find-password";
    }

    @PostMapping("/find-password")
    public String processFindPassword(@ModelAttribute FindPasswordRequestDto requestDto, Model model) {
        try {
            userService.sendTemporaryPassword(
                requestDto.getUserAccount(), requestDto.getName(), requestDto.getEmail());

            model.addAttribute("message", "입력하신 이메일로 임시 비밀번호를 전송했습니다.");
            return "user/find-password-result";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "user/find-password";
        }
    }
}
