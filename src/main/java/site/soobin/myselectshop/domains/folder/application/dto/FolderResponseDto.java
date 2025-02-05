package site.soobin.myselectshop.domains.folder.application.dto;

import site.soobin.myselectshop.domains.folder.domain.entity.Folder;

public record FolderResponseDto(Long id, String name) {

  private FolderResponseDto(Folder folder) {
    this(folder.getId(), folder.getName());
  }

  public static FolderResponseDto from(Folder folder) {
    return new FolderResponseDto(folder);
  }
}
