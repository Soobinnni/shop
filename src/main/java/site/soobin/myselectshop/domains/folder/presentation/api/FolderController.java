package site.soobin.myselectshop.domains.folder.presentation.api;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.soobin.myselectshop.commons.security.UserDetailsImpl;
import site.soobin.myselectshop.domains.folder.application.dto.FolderRequestDto;
import site.soobin.myselectshop.domains.folder.application.dto.FolderResponseDto;
import site.soobin.myselectshop.domains.folder.application.service.FolderService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/folders")
public class FolderController {

  private final FolderService folderService;

  @PostMapping
  public void addFolders(
      @AuthenticationPrincipal UserDetailsImpl principal,
      @RequestBody FolderRequestDto requestDto) {
    folderService.addFolders(requestDto, principal);
  }

  @GetMapping
  public List<FolderResponseDto> getFolders(@AuthenticationPrincipal UserDetailsImpl principal) {
    return folderService.getFolders(principal);
  }
}
