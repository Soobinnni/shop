package site.soobin.myselectshop.domains.product.application.dto;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import site.soobin.myselectshop.domains.folder.application.dto.FolderResponseDto;
import site.soobin.myselectshop.domains.product.domain.entity.Product;
import site.soobin.myselectshop.domains.product.domain.entity.ProductFolder;

public record ProductResponseDto(
    Long id,
    String title,
    String link,
    String image,
    int lprice,
    int myprice,
    List<FolderResponseDto> productFolderList) {

  private ProductResponseDto(Product product, List<FolderResponseDto> folderResponseDtos) {
    this(
        product.getId(),
        product.getTitle(),
        product.getLink(),
        product.getImage(),
        product.getLprice(),
        product.getMyprice(),
        folderResponseDtos);
  }

  public static ProductResponseDto from(Product product) {
    return new ProductResponseDto(product, getProductFolderListFromProduct(product));
  }

  private static List<FolderResponseDto> getProductFolderListFromProduct(Product product) {
    return Optional.ofNullable(product.getProductFolderList()).stream()
        .flatMap(Collection::stream)
        .map(ProductFolder::getFolder)
        .map(FolderResponseDto::from)
        .toList();
  }
}
