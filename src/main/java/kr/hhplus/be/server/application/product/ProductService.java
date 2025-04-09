package kr.hhplus.be.server.application.product;

import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.product.ProductStock;
import kr.hhplus.be.server.domain.product.ProductStockRepository;
import kr.hhplus.be.server.domain.product.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService implements ProductUseCase {

    private final ProductRepository productRepository;
    private final ProductStockRepository productStockRepository;

    @Override
    public ProductListResult getProductList(GetProductListCommand command) {
        List<Product> products = productRepository.findAll(); // TODO: 페이징 정렬 필요 시 확장
        List<Product> domainProducts = products .stream()
                .toList();

        return ProductListResult.from(domainProducts);
    }

    @Override
    public ProductDetailResult getProductDetail(GetProductDetailCommand command) {
        Product product = productRepository.findById(command.productId())
                .orElseThrow(() -> new ProductNotFoundException(command.productId()));
        return ProductDetailResult.from(product);
    }

    @Override
    public List<PopularProductResult> getPopularProducts() {
        return productRepository.findTopSellingProducts().stream()
                .map(PopularProductResult::from)
                .toList();
    }

    @Override
    public boolean decreaseStock(DecreaseStockCommand command) {
        Product product = productRepository.findById(command.productId())
                .orElseThrow(() -> new ProductNotFoundException(command.productId()));

        ProductStock stock = productStockRepository.findByProductIdAndSize(command.productId(), command.size())
                .orElseThrow(() -> new IllegalStateException("해당 상품의 사이즈 재고가 없습니다."));

        stock.decreaseStock(command.quantity());

        productStockRepository.save(stock);
        return true;
    }
}
