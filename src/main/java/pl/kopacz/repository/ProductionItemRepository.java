package pl.kopacz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kopacz.domain.ProductionItem;

import java.time.LocalDate;
import java.util.List;

@SuppressWarnings("unused")
public interface ProductionItemRepository extends JpaRepository<ProductionItem, Long> {

    List<ProductionItem> findByProductIdAndProduction_DateBetween(Long productId, LocalDate from, LocalDate to);

}
