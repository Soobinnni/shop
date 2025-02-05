package site.soobin.myselectshop.domains.product.application.service;

import org.springframework.stereotype.Service;
import site.soobin.myselectshop.domains.folder.domain.entity.Folder;
import site.soobin.myselectshop.domains.product.domain.entity.Product;
import site.soobin.myselectshop.domains.user.domain.entity.User;

@Service
public class ProductDomainService {

  public void validateAndAddToFolder(Product product, Folder folder, User user) {
    product.validateOwnership(user);
    folder.validateOwnership(user);
  }
}
