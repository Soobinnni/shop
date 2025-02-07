package site.soobin.myselectshop.commons.aop.recorder;

import site.soobin.myselectshop.domains.user.domain.entity.User;

public interface ApiUseTimeRecorder {
  void recordApiUseTime(User user, long runTime);
}
