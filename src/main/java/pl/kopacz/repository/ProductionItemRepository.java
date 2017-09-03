package pl.kopacz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kopacz.domain.ProductionItem;

import java.util.List;

@SuppressWarnings("unused")
public interface ProductionItemRepository extends JpaRepository<ProductionItem, Long> {

    List<ProductionItem> findByProductId(Long productId);

}
