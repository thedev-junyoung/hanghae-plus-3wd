package kr.hhplus.be.server.interfaces.product;

import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.application.product.*;
import kr.hhplus.be.server.common.dto.CustomApiResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
@Tag(name = "Product", description = "상품 API")
public class ProductController implements ProductAPI {

    private final ProductUseCase productUseCase;

    @Override
    public ResponseEntity<CustomApiResponse<ProductResponse.ProductListResponse>> getProducts(int page, int size, String sort) {
        ProductListResult result = productUseCase.getProductList(new GetProductListCommand(page, size, sort));
        return ResponseEntity.ok(CustomApiResponse.success(ProductResponse.ProductListResponse.from(result)));
    }

    @Override
    public ResponseEntity<CustomApiResponse<ProductResponse.ProductDetailResponse>> getProduct(Long productId) {
        ProductDetailResult result = productUseCase.getProductDetail(new GetProductDetailCommand(productId));
        return ResponseEntity.ok(CustomApiResponse.success(ProductResponse.ProductDetailResponse.from(result)));
    }

    @Override
    public ResponseEntity<CustomApiResponse<List<ProductResponse.PopularProductResponse>>> getPopularProducts() {
        List<PopularProductResult> result = productUseCase.getPopularProducts();
        return ResponseEntity.ok(CustomApiResponse.success(
                result.stream().map(ProductResponse.PopularProductResponse::from).toList()
        ));
    }


}
