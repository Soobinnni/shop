package site.soobin.myselectshop.domains.folder.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.soobin.myselectshop.commons.security.UserDetailsImpl;
import site.soobin.myselectshop.domains.folder.application.dto.FolderRequestDto;
import site.soobin.myselectshop.domains.folder.application.dto.FolderResponseDto;
import site.soobin.myselectshop.domains.folder.domain.Folders;
import site.soobin.myselectshop.domains.folder.domain.entity.Folder;
import site.soobin.myselectshop.domains.folder.domain.repository.FolderRepository;
import site.soobin.myselectshop.domains.user.domain.entity.User;

@Service
@RequiredArgsConstructor
public class FolderService {
  private final FolderRepository repository;
  private final FolderDomainService domainService;

  public void addFolders(FolderRequestDto requestDto, UserDetailsImpl principal) {
    User user = principal.getUser();
    List<String> folderNames = requestDto.getFolderNames();
    List<Folder> existingFolders = repository.findByUserAndNameIn(user, folderNames);

    Folders newFolders = domainService.createFolders(user, folderNames, existingFolders);
    repository.saveAll(newFolders.getFolders());
  }

  public List<FolderResponseDto> getFolders(UserDetailsImpl principal) {
    return repository.findByUser(principal.getUser()).stream().map(FolderResponseDto::new).toList();
  }
}
