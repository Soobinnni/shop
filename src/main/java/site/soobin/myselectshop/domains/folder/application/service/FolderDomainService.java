package site.soobin.myselectshop.domains.folder.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.soobin.myselectshop.domains.folder.domain.Folders;
import site.soobin.myselectshop.domains.folder.domain.entity.Folder;
import site.soobin.myselectshop.domains.user.domain.entity.User;

@Service
@RequiredArgsConstructor
public class FolderDomainService {
  public Folders createFolders(User user, List<String> folderNames, List<Folder> existingFolders) {
    Folders folders = new Folders(existingFolders);
    return new Folders(folders.createNewFolders(folderNames, user));
  }
}
