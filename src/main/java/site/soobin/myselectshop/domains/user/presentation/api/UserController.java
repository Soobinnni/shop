package site.soobin.myselectshop.domains.user.presentation.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import site.soobin.myselectshop.commons.security.UserDetailsImpl;
import site.soobin.myselectshop.commons.security.jwt.JwtUtil;
import site.soobin.myselectshop.domains.user.application.dto.SignupRequestDto;
import site.soobin.myselectshop.domains.user.application.dto.UserInfoDto;
import site.soobin.myselectshop.domains.user.application.service.KakaoUserService;
import site.soobin.myselectshop.domains.user.application.service.UserService;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

  private final UserService userService;
  private final KakaoUserService kakaoUserService;

  @GetMapping("/user/login-page")
  public String loginPage() {
    return "login";
  }

  @GetMapping("/user/signup")
  public String signupPage() {
    return "signup";
  }

  @PostMapping("/user/signup")
  public String signup(@Valid SignupRequestDto requestDto) {
    log.info(requestDto.toString());
    userService.signup(requestDto);

    return "redirect:/api/user/login-page";
  }

  // 회원 관련 정보 받기
  @GetMapping("/user-info")
  @ResponseBody
  public UserInfoDto getUserInfo(@AuthenticationPrincipal UserDetailsImpl principal) {
    return userService.getUserInfo(principal);
  }

  @GetMapping("/user/kakao/callback")
  public String kakaoLogin(@RequestParam("code") String code, HttpServletResponse response)
      throws JsonProcessingException {
    String token = kakaoUserService.kakaoLogin(code);
    Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, token.substring(7));
    cookie.setPath("/");
    response.addCookie(cookie);

    return "redirect:/";
  }
}
