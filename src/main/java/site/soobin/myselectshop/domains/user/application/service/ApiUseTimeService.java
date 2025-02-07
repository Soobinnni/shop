package site.soobin.myselectshop.domains.user.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.soobin.myselectshop.commons.aop.recorder.ApiUseTimeRecorder;
import site.soobin.myselectshop.domains.user.domain.entity.ApiUseTime;
import site.soobin.myselectshop.domains.user.domain.entity.User;
import site.soobin.myselectshop.domains.user.domain.repository.ApiUseTimeRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiUseTimeService implements ApiUseTimeRecorder {
  private final ApiUseTimeRepository apiUseTimeRepository;

  @Override
  @Transactional
  public void recordApiUseTime(User loginUser, long runTime) {
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
