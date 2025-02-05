package site.soobin.myselectshop.domains.folder.application.dto;

import lombok.Getter;
import site.soobin.myselectshop.domains.folder.domain.entity.Folder;

@Getter
public class FolderResponseDto {
  private final Long id;
  private final String name;

  public FolderResponseDto(Folder folder) {
    this.id = folder.getId();
    this.name = folder.getName();
  }
}
