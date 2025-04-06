package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.infrastructure.product.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    @Query("""
    SELECT p FROM ProductEntity p
    ORDER BY p.salesCount DESC
    LIMIT 5
    """)
    Collection<ProductEntity> findTopSellingProducts();
}
