package site.soobin.myselectshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.soobin.myselectshop.dto.ProductResponseDto;
import site.soobin.myselectshop.security.UserDetailsImpl;
import site.soobin.myselectshop.service.ProductService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/folders")
public class ProductFolderController {
  private final ProductService productService;

  @GetMapping("/{folderId}/products")
  public Page<ProductResponseDto> getProductsInFolder(
      @AuthenticationPrincipal UserDetailsImpl principal,
      @PathVariable("folderId") Long folderId,
      @RequestParam("page") int page,
      @RequestParam("size") int size,
      @RequestParam("sortBy") String sortBy,
      @RequestParam("isAsc") boolean isAsc) {
    return productService.getProductsInFolder(principal, folderId, page - 1, size, sortBy, isAsc);
  }
}
