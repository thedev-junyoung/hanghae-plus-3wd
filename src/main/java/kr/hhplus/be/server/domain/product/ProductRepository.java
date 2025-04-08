package kr.hhplus.be.server.domain.product;


import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProductRepository{
    Collection<Product> findTopSellingProducts();
    List<Product> findAll();
    void save(Product domain);
    Optional<Product> findById(Long aLong);

}
