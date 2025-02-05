package site.soobin.myselectshop.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import site.soobin.myselectshop.entity.Folder;
import site.soobin.myselectshop.entity.User;

public interface FolderRepository extends JpaRepository<Folder, Long> {

  List<Folder> findByUserAndNameIn(User user, List<String> folderNames);

  List<Folder> findByUser(User user);
}
