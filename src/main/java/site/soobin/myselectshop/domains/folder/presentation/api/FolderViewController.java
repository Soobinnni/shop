package site.soobin.myselectshop.domains.folder.presentation.api;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import site.soobin.myselectshop.commons.security.UserDetailsImpl;
import site.soobin.myselectshop.domains.folder.application.service.FolderService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class FolderViewController {
  private final FolderService folderService;

  @GetMapping("/user-folder")
  public String getUserInfo(Model model, @AuthenticationPrincipal UserDetailsImpl principal) {
    model.addAttribute("folders", folderService.getFolders(principal));

    return "index :: #fragment";
  }
}
