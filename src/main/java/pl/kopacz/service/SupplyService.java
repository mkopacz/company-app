package pl.kopacz.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kopacz.domain.*;
import pl.kopacz.exception.InsufficientSpiceException;
import pl.kopacz.repository.SupplyRepository;
import pl.kopacz.repository.SupplyUsageRepository;
import pl.kopacz.service.dto.SupplyDTO;
import pl.kopacz.service.mapper.SupplyMapper;
import pl.kopacz.service.util.RoundUtil;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class SupplyService {

    private final Logger log = LoggerFactory.getLogger(SupplyService.class);

    @Inject
    private SupplyRepository supplyRepository;

    @Inject
    private SupplyUsageRepository supplyUsageRepository;

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

    public Set<SupplyUsage> useSupplies(Production production) throws InsufficientSpiceException {
        log.debug("Request to use Supplies : {}", production);
        Set<SupplyUsage> totalSupplyUsages = new HashSet<>();

        for (ProductionItem productionItem : production.getProductionItems()) {
            Product product = productionItem.getProduct();
            Double productAmount = productionItem.getAmount();

            for (Ingredient ingredient : product.getIngredients()) {
                Double spiceAmount = ingredient.getAmount() * productAmount / 100;
                Double roundedSpiceAmount = RoundUtil.roundToNearest005(spiceAmount);
                Set<SupplyUsage> supplyUsages = useSupply(product, ingredient.getSpice(), roundedSpiceAmount);
                totalSupplyUsages.addAll(supplyUsages);
            }
        }

        return totalSupplyUsages;
    }

    public void returnSupplies(Long productionId) {
        log.debug("Request to return Supplies : {}", productionId);
        List<SupplyUsage> supplyUsages = supplyUsageRepository.findByProductionId(productionId);
        supplyUsages.forEach(supplyUsage -> {
            Supply supply = supplyUsage.getSupply();
            supply.increaseAmount(supplyUsage.getAmount());
        });
        supplyUsageRepository.delete(supplyUsages);
    }

    private Set<SupplyUsage> useSupply(Product product, Spice spice, double amount) throws InsufficientSpiceException {
        Set<SupplyUsage> supplyUsages = new HashSet<>();
        List<Supply> supplies = supplyRepository.findBySpiceAndAmountGreaterThanOrderByIdAsc(spice, 0d);

        try {
            while (amount > 0) {
                Supply supply = supplies.remove(0);
                double amountToUse = Math.min(supply.getAmount(), amount);

                SupplyUsage supplyUsage = new SupplyUsage();
                supplyUsage.amount(amountToUse).supply(supply).product(product);
                supplyUsages.add(supplyUsage);

                supply.decreaseAmount(amountToUse);
                amount -= amountToUse;
            }
        } catch (IndexOutOfBoundsException e) {
            throw new InsufficientSpiceException();
        }

        return supplyUsages;
    }

}
