package com.example.convenientparking.Config;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String errorMessage = "";
        if (exception instanceof LockedException) {
            errorMessage = "Tài khoản của bạn đã bị khoá. Vui lòng liên hệ quản trị viên để biết thêm chi tiết.";
        } else if (exception instanceof BadCredentialsException) {
            errorMessage = "Mật khẩu và tên người dùng không đúng.";
        }
        String encodedErrorMessage = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8.toString());
        response.sendRedirect("/login?error=" + encodedErrorMessage);
    }
}
