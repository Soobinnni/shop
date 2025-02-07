package site.soobin.myselectshop.commons.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import site.soobin.myselectshop.commons.aop.recorder.ApiUseTimeRecorder;
import site.soobin.myselectshop.commons.security.UserDetailsImpl;
import site.soobin.myselectshop.domains.user.domain.entity.User;

@Slf4j(topic = "UseTimeAop")
@Aspect
@Component
@RequiredArgsConstructor
public class UseTimeAop {
  private final ApiUseTimeRecorder apiUseTimeRecorder;

  @Pointcut(
      "execution(* site.soobin.myselectshop.domains.product.presentation.api.ProductController.*(..))")
  private void product() {}

  @Pointcut(
      "execution(* site.soobin.myselectshop.domains.folder.presentation.api.FolderController.*(..))")
  private void folder() {}

  @Pointcut(
      "execution(* site.soobin.myselectshop.domains.product.infrastructure.external.naver.controller.NaverApiController.*(..))")
  private void naver() {}

  @Around("product() || folder() || naver()")
  public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
    long startTime = System.currentTimeMillis();

    try {
      return joinPoint.proceed();
    } finally {
      long runTime = calculateRunTime(startTime);
      processApiUseTime(runTime);
    }
  }

  private long calculateRunTime(long startTime) {
    long endTime = System.currentTimeMillis();
    return endTime - startTime;
  }

  private void processApiUseTime(long runTime) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (!isValidAuthentication(auth)) {
      return;
    }

    User loginUser = getLoginUser(auth);
    apiUseTimeRecorder.recordApiUseTime(loginUser, runTime);
  }

  private boolean isValidAuthentication(Authentication auth) {
    return auth != null && auth.getPrincipal().getClass() == UserDetailsImpl.class;
  }

  private User getLoginUser(Authentication auth) {
    UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
    return userDetails.getUser();
  }
}
