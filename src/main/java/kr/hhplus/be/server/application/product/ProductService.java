package kr.hhplus.be.server.application.product;

import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.product.exception.ProductNotFoundException;
import kr.hhplus.be.server.infrastructure.product.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService implements ProductUseCase {

    private final ProductRepository productRepository;

    @Override
    public ProductListResult getProductList(GetProductListCommand command) {
        List<ProductEntity> entities = productRepository.findAll(); // TODO: 페이징 정렬 필요 시 확장
        List<Product> domainProducts = entities.stream()
                .map(ProductEntity::toDomain)
                .toList();

        return ProductListResult.from(domainProducts);
    }

    @Override
    public ProductDetailResult getProductDetail(GetProductDetailCommand command) {
        ProductEntity entity = productRepository.findById(command.productId())
                .orElseThrow(() -> new ProductNotFoundException(command.productId()));
        return ProductDetailResult.from(entity.toDomain());
    }

    @Override
    public List<PopularProductResult> getPopularProducts() {
        return productRepository.findTopSellingProducts().stream()
                .map(ProductEntity::toDomain)
                .map(PopularProductResult::from)
                .toList();
    }

    @Override
    public boolean decreaseStock(DecreaseStockCommand command) {
        ProductEntity entity = productRepository.findById(command.productId())
                .orElseThrow(() -> new ProductNotFoundException(command.productId()));

        Product domain = entity.toDomain();
        domain.decreaseStock(command.quantity());

        productRepository.save(ProductEntity.from(domain));
        return true;
    }
}
