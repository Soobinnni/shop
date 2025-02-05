package site.soobin.myselectshop.domains.folder.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import site.soobin.myselectshop.commons.exception.ApiBusinessException;
import site.soobin.myselectshop.domains.folder.application.exception.FolderErrorSpec;
import site.soobin.myselectshop.domains.user.domain.entity.User;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "folder")
public class Folder {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  public Folder(String name, User user) {
    this.name = name;
    this.user = user;
  }

  public void validateOwnership(User user) {
    if (!this.user.getId().equals(user.getId())) {
      throw new ApiBusinessException(FolderErrorSpec.NOT_OWNER);
    }
  }
}
