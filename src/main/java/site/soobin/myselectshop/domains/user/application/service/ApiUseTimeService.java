package site.soobin.myselectshop.domains.user.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.soobin.myselectshop.domains.user.domain.entity.ApiUseTime;
import site.soobin.myselectshop.domains.user.domain.entity.User;
import site.soobin.myselectshop.domains.user.domain.repository.ApiUseTimeRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiUseTimeService {
  private final ApiUseTimeRepository apiUseTimeRepository;

  @Transactional
  public void saveApiUseTime(User loginUser, long runTime) {
    ApiUseTime apiUseTime = getOrCreateApiUseTime(loginUser, runTime);
    logApiUseTime(loginUser, apiUseTime);
    apiUseTimeRepository.save(apiUseTime);
  }

  private ApiUseTime getOrCreateApiUseTime(User loginUser, long runTime) {
    return apiUseTimeRepository
        .findByUser(loginUser)
        .map(
            existingTime -> {
              existingTime.addUseTime(runTime);
              return existingTime;
            })
        .orElse(new ApiUseTime(loginUser, runTime));
  }

  private void logApiUseTime(User loginUser, ApiUseTime apiUseTime) {
    log.info(
        "[API Use Time] Username: {}, Total Time: {} ms",
        loginUser.getUsername(),
        apiUseTime.getTotalTime());
  }
}
