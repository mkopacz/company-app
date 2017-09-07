package pl.kopacz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kopacz.domain.Spice;
import pl.kopacz.domain.Supply;

import java.util.List;

@SuppressWarnings("unused")
public interface SupplyRepository extends JpaRepository<Supply, Long> {

    List<Supply> findBySpiceAndAmountGreaterThanOrderByIdAsc(Spice spice, Double amount);

}
