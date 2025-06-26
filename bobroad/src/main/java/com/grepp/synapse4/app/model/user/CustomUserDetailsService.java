package com.grepp.synapse4.app.model.user;

import com.grepp.synapse4.app.model.user.dto.AdminUserSearchDto;
import com.grepp.synapse4.app.model.user.dto.CustomUserDetails;
import com.grepp.synapse4.app.model.user.entity.User;
import com.grepp.synapse4.app.model.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // no usages 메서드
    @Override
    public UserDetails loadUserByUsername(String userAccount) throws UsernameNotFoundException {
        User user = userRepository.findByUserAccount(userAccount)
            .orElseThrow(() -> new UsernameNotFoundException(
                "사용자 계정이 존재하지 않습니다: " + userAccount
            ));

        return new CustomUserDetails(user);
    }

    @Cacheable("user-authorities")
    public List<SimpleGrantedAuthority> findAuthorities(String userAccount) {
        User user = userRepository.findByUserAccount(userAccount)
            .orElseThrow(() -> new UsernameNotFoundException(userAccount));
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().name()));

        return authorities;
    }

    public Long loadUserIdByAccount() throws UsernameNotFoundException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUserAccount(authentication.getName())
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        return user.getId();
    }

    public Long loadUserIdByAccount(String account) throws UsernameNotFoundException{
        User user = userRepository.findByUserAccount(account)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        return user.getId();
    }

    public Boolean findUserByAccount(String account) throws UsernameNotFoundException{
        return userRepository.existsByUserAccount(account);
    }

    // 관리자 유저 검색 기능
    @Transactional(readOnly = true)
    public List<AdminUserSearchDto> findByUserAccountContaining(String userAccount) {
        return userRepository.findByUserAccountContaining(userAccount);
    }
}
