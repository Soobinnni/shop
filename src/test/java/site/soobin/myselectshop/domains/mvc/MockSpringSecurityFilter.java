package site.soobin.myselectshop.domains.mvc;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

// Spring Security와 관련된 필터를 구현, 테스트 환경에서 사용되는 모의 필터
public class MockSpringSecurityFilter implements Filter {
  @Override
  public void init(FilterConfig filterConfig) {}

  // doFilter 메서드에서 SecurityContextHolder의 현재 보안 컨텍스트에 인증 정보를 설정
  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    // req 객체를 HttpServletRequest로 캐스팅
    // -> getUserPrincipal() 메서드를 호출하여 현재 사용자의 인증 정보를 가져옴
    HttpServletRequest servletRequest = (HttpServletRequest) req;
    Authentication authentication = (Authentication) servletRequest.getUserPrincipal();

    // 이 인증 정보는 Spring Security의 보안 컨텍스트에 저장되어, 이후의 요청 처리에서 사용
    SecurityContextHolder.getContext().setAuthentication(authentication);

    // 다음 필터 또는 요청 처리기로 요청을 전달.
    // 해당 필터가 끝난 후에도 요청 처리가 계속 진행될 수 있도록 함.
    chain.doFilter(req, res);
  }

  @Override
  public void destroy() {
    // 필터가 제거될 때 호출,  보안 컨텍스트를 정리
    SecurityContextHolder.clearContext();
  }
}
