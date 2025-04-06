package kr.hhplus.be.server.application.product;

import java.util.List;

public interface ProductUseCase {

    /**
     * 상품 목록을 조회합니다.
     */
    ProductListResult getProductList(GetProductListCommand command);

    /**
     * 단일 상품 상세 정보를 조회합니다.
     */
    ProductDetailResult getProductDetail(GetProductDetailCommand command);

    /**
     * 최근 3일간 가장 많이 팔린 상품 조회
     */
    List<PopularProductResult> getPopularProducts();

    /**
     * 상품 재고 차감
     */
    boolean decreaseStock(DecreaseStockCommand command);
}
