package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.infrastructure.product.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository{
    Collection<Product> findTopSellingProducts();
    List<Product> findAll();
    void save(Product domain);
    Optional<Product> findById(Long aLong);

}
