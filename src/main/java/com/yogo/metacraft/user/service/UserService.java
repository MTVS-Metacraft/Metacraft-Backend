package com.yogo.metacraft.user.service;

import com.yogo.metacraft.common.CustomApiResponse;
import com.yogo.metacraft.user.dto.LoginDto;
import com.yogo.metacraft.user.dto.SignUpDto;
import com.yogo.metacraft.user.entity.User;
import com.yogo.metacraft.user.repository.UserRepository;
import com.yogo.metacraft.utils.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public CustomApiResponse<String> signup(SignUpDto signUpDto) {
        if (userRepository.findById(signUpDto.getId()).isPresent()) {
            return new CustomApiResponse<>(false, "이미 존재하는 사용자 ID입니다.");
        }
        // 비밀번호 암호화
        String encodedPassword = PasswordUtil.hashPassword(signUpDto.getPassword());
        userRepository.save(new User(signUpDto.getId(), encodedPassword, signUpDto.getName()));
        return new CustomApiResponse<>(true, "회원가입이 완료되었습니다.");
    }

    public CustomApiResponse<String> login(LoginDto loginDto) {
        Optional<User> user = userRepository.findById(loginDto.getId());
        if (user.isPresent() && PasswordUtil.checkPassword(loginDto.getPassword(), user.get().getPassword())) {
            return new CustomApiResponse<>(true, "로그인 성공");
        }
        return new CustomApiResponse<>(false, "ID 또는 비밀번호가 잘못되었습니다.");
    }

    public CustomApiResponse<String> logout() {
        return new CustomApiResponse<>(true, "로그아웃 성공");
    }

    public CustomApiResponse<String> deleteUser(String id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
            return new CustomApiResponse<>(true, "회원탈퇴가 완료되었습니다.");
        }
        return new CustomApiResponse<>(false, "존재하지 않는 사용자입니다.");
    }
}