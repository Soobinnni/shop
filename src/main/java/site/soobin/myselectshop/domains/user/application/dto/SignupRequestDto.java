package site.soobin.myselectshop.domains.user.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignupRequestDto(
    @NotBlank String username,
    @NotBlank String password,
    @Email @NotBlank String email,
    Boolean isAdmin,
    String adminToken) {}
