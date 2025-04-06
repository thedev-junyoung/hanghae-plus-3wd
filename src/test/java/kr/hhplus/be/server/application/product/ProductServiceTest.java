package kr.hhplus.be.server.application.product;

import kr.hhplus.be.server.common.vo.Money;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.product.exception.InsufficientStockException;
import kr.hhplus.be.server.domain.product.exception.ProductNotFoundException;
import kr.hhplus.be.server.infrastructure.product.ProductEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductService productService;

    @Test
    @DisplayName("상품 목록을 조회할 수 있다")
    void getProductList_success() {
        // given
        Product product = Product.create(1L, "Jordan 1", "Nike", Money.wons(200_000), 10,
                LocalDate.of(2024, 1, 1), "image.jpg", "best seller");
        when(productRepository.findAll()).thenReturn(List.of(ProductEntity.from(product)));

        // when
        ProductListResult result = productService.getProductList(new GetProductListCommand(0, 10, null));

        // then
        assertThat(result.products()).hasSize(1);
        assertThat(result.products().get(0).name()).isEqualTo("Jordan 1");
    }

    @Test
    @DisplayName("상품 상세 조회 성공")
    void getProductDetail_success() {
        // given
        Product product = Product.create(1L, "Jordan 1", "Nike", Money.wons(200_000), 10,
                LocalDate.of(2024, 1, 1), "image.jpg", "best seller");
        when(productRepository.findById(1L)).thenReturn(Optional.of(ProductEntity.from(product)));

        // when
        ProductDetailResult result = productService.getProductDetail(new GetProductDetailCommand(1L));

        // then
        assertThat(result.product().name()).isEqualTo("Jordan 1");
    }

    @Test
    @DisplayName("상품 상세 조회 실패 - 존재하지 않는 상품")
    void getProductDetail_fail() {
        // given
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> productService.getProductDetail(new GetProductDetailCommand(99L)))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    @DisplayName("상품 재고 차감 성공")
    void decreaseStock_success() {
        // given
        Product product = Product.create(1L, "Jordan 1", "Nike", Money.wons(200_000), 10,
                LocalDate.of(2024, 1, 1), "image.jpg", "best seller");
        when(productRepository.findById(1L)).thenReturn(Optional.of(ProductEntity.from(product)));

        // when
        boolean result = productService.decreaseStock(new DecreaseStockCommand(1L, 3));

        // then
        assertThat(result).isTrue();
        verify(productRepository).save(any());
    }

    @Test
    @DisplayName("상품 재고 차감 실패 - 재고 부족")
    void decreaseStock_insufficient() {
        // given
        Product product = Product.create(1L, "Jordan 1", "Nike", Money.wons(200_000), 1,
                LocalDate.of(2024, 1, 1), "image.jpg", "best seller");
        when(productRepository.findById(1L)).thenReturn(Optional.of(ProductEntity.from(product)));

        // when & then
        assertThatThrownBy(() -> productService.decreaseStock(new DecreaseStockCommand(1L, 5)))
                .isInstanceOf(InsufficientStockException.class);
    }
}
