package site.soobin.myselectshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.soobin.myselectshop.dto.FolderRequestDto;
import site.soobin.myselectshop.security.UserDetailsImpl;
import site.soobin.myselectshop.service.FolderService;

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
}
