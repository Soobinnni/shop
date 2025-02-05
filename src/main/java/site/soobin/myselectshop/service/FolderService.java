package site.soobin.myselectshop.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.soobin.myselectshop.dto.FolderRequestDto;
import site.soobin.myselectshop.dto.FolderResponseDto;
import site.soobin.myselectshop.entity.Folder;
import site.soobin.myselectshop.entity.User;
import site.soobin.myselectshop.repository.FolderRepository;
import site.soobin.myselectshop.security.UserDetailsImpl;

@Service
@RequiredArgsConstructor
public class FolderService {
  private final FolderRepository repository;

  public void addFolders(FolderRequestDto requestDto, UserDetailsImpl principal) {
    User user = getUserFromPrincipal(principal);
    List<String> folderNames = getFolderNameFromRequestDto(requestDto);
    List<Folder> existFolderList = repository.findByUserAndNameIn(user, folderNames);

    List<Folder> addFolderList = new ArrayList<>();

    for (String folderName : folderNames) {
      if (!isExistFolderName(folderName, existFolderList)) {
        Folder folder = new Folder(folderName, user);
        addFolderList.add(folder);
      } else {
        throw new IllegalArgumentException("폴더명이 중복되었습니다.");
      }
    }

    repository.saveAll(addFolderList);
  }

  public List<FolderResponseDto> getFolders(UserDetailsImpl principal) {
    User user = getUserFromPrincipal(principal);
    List<Folder> existFolderList = repository.findByUser(user);

    return existFolderList.stream().map(FolderResponseDto::new).toList();
  }

  private User getUserFromPrincipal(UserDetailsImpl principal) {
    return principal.getUser();
  }

  private List<String> getFolderNameFromRequestDto(FolderRequestDto requestDto) {
    return requestDto.getFolderNames();
  }

  private boolean isExistFolderName(String folderName, List<Folder> existFolderList) {
    for (Folder existFolder : existFolderList) {
      if (folderName.equals(existFolder.getName())) {
        return true;
      }
    }
    return false;
  }
}
