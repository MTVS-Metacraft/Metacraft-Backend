package com.yogo.metacraft.user.controller;

import com.yogo.metacraft.common.CustomApiResponse;
import com.yogo.metacraft.user.dto.LoginDto;
import com.yogo.metacraft.user.dto.SignUpDto;
import com.yogo.metacraft.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "유저 인증 관리 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입", description = "사용자가 회원가입합니다.")
    @PostMapping("/signup")
    public ResponseEntity<CustomApiResponse<String>> signup(@Valid @RequestBody SignUpDto signUpDto) {
        CustomApiResponse<String> response = userService.signup(signUpDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "로그인", description = "사용자가 로그인합니다.")
    @PostMapping("/login")
    public ResponseEntity<CustomApiResponse<String>> login(@Valid @RequestBody LoginDto loginDto) {
        CustomApiResponse<String> response = userService.login(loginDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "로그아웃", description = "사용자가 로그아웃합니다.")
    @PostMapping("/logout")
    public ResponseEntity<CustomApiResponse<String>> logout() {
        CustomApiResponse<String> response = userService.logout();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "회원탈퇴", description = "사용자를 삭제합니다.")
    @DeleteMapping("/signout")
    public ResponseEntity<CustomApiResponse<String>> signout(@RequestParam String id) {
        CustomApiResponse<String> response = userService.deleteUser(id);
        return ResponseEntity.ok(response);
    }
}