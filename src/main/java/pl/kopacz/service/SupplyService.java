package pl.kopacz.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kopacz.domain.Production;
import pl.kopacz.domain.Spice;
import pl.kopacz.domain.Supply;
import pl.kopacz.repository.SupplyRepository;
import pl.kopacz.service.dto.SupplyDTO;
import pl.kopacz.service.mapper.SupplyMapper;

import javax.inject.Inject;
import java.util.List;

@Service
@Transactional
public class SupplyService {

    private final Logger log = LoggerFactory.getLogger(SupplyService.class);

    @Inject
    private SupplyRepository supplyRepository;

    @Inject
    private SupplyMapper supplyMapper;

    public SupplyDTO save(SupplyDTO supplyDTO) {
        log.debug("Request to save Supply : {}", supplyDTO);
        Supply supply = supplyMapper.supplyDTOToSupply(supplyDTO);
        supply = supplyRepository.save(supply);
        SupplyDTO result = supplyMapper.supplyToSupplyDTO(supply);
        return result;
    }

    @Transactional(readOnly = true)
    public Page<SupplyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Supplies");
        Page<Supply> result = supplyRepository.findAll(pageable);
        return result.map(supply -> supplyMapper.supplyToSupplyDTO(supply));
    }

    @Transactional(readOnly = true)
    public SupplyDTO findOne(Long id) {
        log.debug("Request to get Supply : {}", id);
        Supply supply = supplyRepository.findOne(id);
        SupplyDTO supplyDTO = supplyMapper.supplyToSupplyDTO(supply);
        return supplyDTO;
    }

    public void delete(Long id) {
        log.debug("Request to delete Supply : {}", id);
        supplyRepository.delete(id);
    }

    public void useSupplies(Production production) {
        log.debug("Request to use Supplies : {}", production);
        production.getProductionItems().forEach(productionItem -> {
            Double productAmount = productionItem.getAmount();
            productionItem.getIngredients().forEach(ingredient -> {
                Double spiceAmount = ingredient.getAmount() * productAmount / 100;
                useSupply(ingredient.getSpice(), spiceAmount);
            });
        });
    }

    private void useSupply(Spice spice, double amount) {
        List<Supply> supplies = supplyRepository.findBySpiceOrderByIdAsc(spice);
        while (amount > 0) {
            Supply supply = supplies.remove(0);
            double amountToUse = Math.min(supply.getAmount(), amount);
            supply.lowerAmount(amountToUse);
            amount -= amountToUse;
        }
    }

}
