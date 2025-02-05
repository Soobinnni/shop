package site.soobin.myselectshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.soobin.myselectshop.entity.Folder;

public interface FolderRepository extends JpaRepository<Folder, Long> {}
