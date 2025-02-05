package site.soobin.myselectshop.domains.folder.domain;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Value;
import site.soobin.myselectshop.commons.exception.ApiBusinessException;
import site.soobin.myselectshop.domains.folder.application.exception.FolderErrorSpec;
import site.soobin.myselectshop.domains.folder.domain.entity.Folder;
import site.soobin.myselectshop.domains.user.domain.entity.User;

@Value
public class Folders {
  List<Folder> folders;

  public void validateDuplicateName(String newFolderName) {
    if (folders.stream().anyMatch(f -> f.getName().equals(newFolderName))) {
      throw new ApiBusinessException(FolderErrorSpec.DUPLICATE_FOLDER);
    }
  }

  public List<Folder> createNewFolders(List<String> folderNames, User user) {
    return folderNames.stream()
        .peek(this::validateDuplicateName)
        .map(name -> Folder.builder(name, user).build())
        .collect(Collectors.toList());
  }
}
