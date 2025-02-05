package site.soobin.myselectshop.domains.user.domain.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import site.soobin.myselectshop.domains.user.domain.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUsername(String username);

  Optional<User> findByEmail(String email);
}
