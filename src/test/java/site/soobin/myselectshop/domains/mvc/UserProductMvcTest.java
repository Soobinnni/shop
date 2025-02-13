package site.soobin.myselectshop.domains.mvc;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.security.Principal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import site.soobin.myselectshop.commons.config.WebSecurityConfig;
import site.soobin.myselectshop.commons.security.UserDetailsImpl;
import site.soobin.myselectshop.domains.folder.application.service.FolderService;
import site.soobin.myselectshop.domains.product.application.dto.ProductRequestDto;
import site.soobin.myselectshop.domains.product.application.service.ProductService;
import site.soobin.myselectshop.domains.product.presentation.api.ProductController;
import site.soobin.myselectshop.domains.user.application.service.KakaoUserService;
import site.soobin.myselectshop.domains.user.application.service.UserService;
import site.soobin.myselectshop.domains.user.domain.entity.User;
import site.soobin.myselectshop.domains.user.domain.entity.UserRoleEnum;
import site.soobin.myselectshop.domains.user.presentation.api.UserController;

@WebMvcTest(
    controllers = {UserController.class, ProductController.class},
    excludeFilters = {
      @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfig.class)
    })
public class UserProductMvcTest {
  @MockitoBean UserService userService;
  @MockitoBean KakaoUserService kakaoService;
  @MockitoBean ProductService productService;
  @MockitoBean FolderService folderService;
  private MockMvc mvc;
  private Principal mockPrincipal;
  @Autowired private WebApplicationContext context;
  @Autowired private ObjectMapper objectMapper;

  @BeforeEach
  public void setup() {
    mvc =
        // MockMvcBuilders는 Spring MVC 테스트를 위한 유틸리티 클래스
        MockMvcBuilders.webAppContextSetup(
                context) //  주어진 ApplicationContext를 사용하여 MockMvc 인스턴스를 설정
            .apply(springSecurity(new MockSpringSecurityFilter())) // Spring Security를 모의하는 설정을 추가
            .build();
  }

  private void mockUserSetup() {
    // Mock 테스트 유져 생성
    String username = "sollertia4351";
    String password = "robbie1234";
    String email = "sollertia@sparta.com";
    UserRoleEnum role = UserRoleEnum.USER;
    User testUser = User.builder(username, password, email, role).build();
    UserDetailsImpl testUserDetails = new UserDetailsImpl(testUser);
    mockPrincipal =
        new UsernamePasswordAuthenticationToken(
            testUserDetails, "", testUserDetails.getAuthorities());
  }

  @Test
  @DisplayName("로그인 Page")
  void test1() throws Exception {
    // when - then
    mvc.perform(get("/api/user/login-page"))
        .andExpect(status().isOk())
        .andExpect(view().name("login"))
        .andDo(print());
  }

  @Test
  @DisplayName("회원 가입 요청 처리")
  void test2() throws Exception {
    // given
    MultiValueMap<String, String> signupRequestForm = new LinkedMultiValueMap<>();
    signupRequestForm.add("username", "sollertia4351");
    signupRequestForm.add("password", "robbie1234");
    signupRequestForm.add("email", "sollertia@sparta.com");
    signupRequestForm.add("admin", "false");

    // when - then
    mvc.perform(post("/api/user/signup").params(signupRequestForm))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/api/user/login-page"))
        .andDo(print());
  }

  @Test
  @DisplayName("신규 관심상품 등록")
  void test3() throws Exception {
    // given
    this.mockUserSetup();
    String title = "Apple <b>아이폰</b> 14 프로 256GB [자급제]";
    String imageUrl =
        "https://shopping-phinf.pstatic.net/main_3456175/34561756621.20220929142551.jpg";
    String linkUrl = "https://search.shopping.naver.com/gate.nhn?id=34561756621";
    int lPrice = 959000;
    ProductRequestDto requestDto = new ProductRequestDto(title, imageUrl, linkUrl, lPrice);

    String postInfo = objectMapper.writeValueAsString(requestDto);

    // when - then
    mvc.perform(
            post("/api/products")
                .content(postInfo)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal))
        .andExpect(status().isOk())
        .andDo(print());
  }
}
