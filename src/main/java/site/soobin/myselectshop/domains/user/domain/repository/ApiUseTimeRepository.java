package site.soobin.myselectshop.domains.user.domain.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import site.soobin.myselectshop.domains.user.domain.entity.ApiUseTime;
import site.soobin.myselectshop.domains.user.domain.entity.User;

public interface ApiUseTimeRepository extends JpaRepository<ApiUseTime, Long> {
  Optional<ApiUseTime> findByUser(User useruser);
}
