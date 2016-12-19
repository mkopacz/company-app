package pl.kopacz.repository;

import pl.kopacz.domain.ProductionItem;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ProductionItem entity.
 */
@SuppressWarnings("unused")
public interface ProductionItemRepository extends JpaRepository<ProductionItem,Long> {

}
