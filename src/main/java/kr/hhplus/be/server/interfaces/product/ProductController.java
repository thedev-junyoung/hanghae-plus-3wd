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
    public ResponseEntity<CustomApiResponse<Response.ProductListResponse>> getProducts(int page, int size, String sort) {
        ProductListResult result = productUseCase.getProductList(new GetProductListCommand(page, size, sort));
        return ResponseEntity.ok(CustomApiResponse.success(Response.ProductListResponse.from(result)));
    }

    @Override
    public ResponseEntity<CustomApiResponse<Response.ProductDetailResponse>> getProduct(Long productId) {
        ProductDetailResult result = productUseCase.getProductDetail(new GetProductDetailCommand(productId));
        return ResponseEntity.ok(CustomApiResponse.success(Response.ProductDetailResponse.from(result)));
    }

    @Override
    public ResponseEntity<CustomApiResponse<List<Response.PopularProductResponse>>> getPopularProducts() {
        List<PopularProductResult> result = productUseCase.getPopularProducts();
        return ResponseEntity.ok(CustomApiResponse.success(
                result.stream().map(Response.PopularProductResponse::from).toList()
        ));
    }

    // ------------------- Nested Response DTOs -------------------

    public static class Response {

        @Getter
        public static class ProductListResponse {
            private final List<ProductResponse> products;

            public ProductListResponse(List<ProductResponse> products) {
                this.products = products;
            }

            public static ProductListResponse from(ProductListResult result) {
                return new ProductListResponse(
                        result.products().stream().map(ProductResponse::from).toList()
                );
            }
        }

        @Getter
        public static class ProductDetailResponse {
            private final ProductResponse product;

            public ProductDetailResponse(ProductResponse product) {
                this.product = product;
            }

            public static ProductDetailResponse from(ProductDetailResult result) {
                return new ProductDetailResponse(ProductResponse.from(result.product()));
            }
        }

        @Getter
        public static class ProductResponse {
            private final Long id;
            private final String name;
            private final BigDecimal price;
            private final int stock;
            private final LocalDate releaseDate;
            private final String imageUrl;
            private final LocalDateTime createdAt;
            private final LocalDateTime updatedAt;

            public ProductResponse(Long id, String name, BigDecimal price, int stock,
                                   LocalDate releaseDate, String imageUrl, LocalDateTime createdAt, LocalDateTime updatedAt) {
                this.id = id;
                this.name = name;
                this.price = price;
                this.stock = stock;
                this.releaseDate = releaseDate;
                this.imageUrl = imageUrl;
                this.createdAt = createdAt;
                this.updatedAt = updatedAt;
            }

            public static ProductResponse from(ProductResult result) {
                return new ProductResponse(
                        result.id(), result.name(), result.price(), result.stock(),
                        result.releaseDate(), result.imageUrl(), result.createdAt(), result.updatedAt()
                );
            }
        }

        @Getter
        public static class PopularProductResponse {
            private final Long id;
            private final String name;
            private final BigDecimal price;
            private final int salesCount;

            public PopularProductResponse(Long id, String name, BigDecimal price, int salesCount) {
                this.id = id;
                this.name = name;
                this.price = price;
                this.salesCount = salesCount;
            }

            public static PopularProductResponse from(PopularProductResult result) {
                return new PopularProductResponse(
                        result.id(), result.name(), result.price(), result.salesCount()
                );
            }
        }
    }
}
