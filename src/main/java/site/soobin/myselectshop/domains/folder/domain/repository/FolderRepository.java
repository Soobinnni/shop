package site.soobin.myselectshop.domains.folder.domain.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import site.soobin.myselectshop.domains.folder.domain.entity.Folder;
import site.soobin.myselectshop.domains.user.domain.entity.User;

public interface FolderRepository extends JpaRepository<Folder, Long> {

  List<Folder> findByUserAndNameIn(User user, List<String> folderNames);

  List<Folder> findByUser(User user);
}
