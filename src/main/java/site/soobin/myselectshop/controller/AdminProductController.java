package site.soobin.myselectshop.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.soobin.myselectshop.dto.ProductResponseDto;
import site.soobin.myselectshop.entity.UserRoleEnum.Authority;
import site.soobin.myselectshop.service.ProductService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/products")
public class AdminProductController {
  private final ProductService service;

  @GetMapping
  @Secured(Authority.ADMIN)
  public List<ProductResponseDto> getProducts() {
    return service.getAllProducts();
  }
}
