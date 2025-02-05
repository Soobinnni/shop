package site.soobin.myselectshop.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import site.soobin.myselectshop.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUsername(String username);

  Optional<User> findByEmail(String email);
}
