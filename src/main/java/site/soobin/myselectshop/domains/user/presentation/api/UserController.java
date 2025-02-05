package site.soobin.myselectshop.domains.user.presentation.api;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import site.soobin.myselectshop.commons.security.UserDetailsImpl;
import site.soobin.myselectshop.domains.folder.application.service.FolderService;
import site.soobin.myselectshop.domains.user.application.dto.SignupRequestDto;
import site.soobin.myselectshop.domains.user.application.dto.UserInfoDto;
import site.soobin.myselectshop.domains.user.application.service.UserService;
import site.soobin.myselectshop.domains.user.domain.entity.UserRoleEnum;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

  private final UserService userService;
  private final FolderService folderService;

  @GetMapping("/user/login-page")
  public String loginPage() {
    return "login";
  }

  @GetMapping("/user/signup")
  public String signupPage() {
    return "signup";
  }

  @PostMapping("/user/signup")
  public String signup(@Valid SignupRequestDto requestDto, BindingResult bindingResult) {
    // Validation 예외처리
    List<FieldError> fieldErrors = bindingResult.getFieldErrors();
    if (fieldErrors.size() > 0) {
      for (FieldError fieldError : bindingResult.getFieldErrors()) {
        log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
      }
      return "redirect:/api/user/signup";
    }

    userService.signup(requestDto);

    return "redirect:/api/user/login-page";
  }

  // 회원 관련 정보 받기
  @GetMapping("/user-info")
  @ResponseBody
  public UserInfoDto getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    String username = userDetails.getUser().getUsername();
    UserRoleEnum role = userDetails.getUser().getRole();
    boolean isAdmin = (role == UserRoleEnum.ADMIN);

    return new UserInfoDto(username, isAdmin);
  }

  @GetMapping("/user-folder")
  public String getUserInfo(Model model, @AuthenticationPrincipal UserDetailsImpl principal) {
    model.addAttribute("folders", folderService.getFolders(principal));

    return "index :: #fragment";
  }
}
