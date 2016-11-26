package pl.kopacz.service;

import pl.kopacz.domain.Ingredient;
import pl.kopacz.repository.IngredientRepository;
import pl.kopacz.service.dto.IngredientDTO;
import pl.kopacz.service.mapper.IngredientMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Ingredient.
 */
@Service
@Transactional
public class IngredientService {

    private final Logger log = LoggerFactory.getLogger(IngredientService.class);
    
    @Inject
    private IngredientRepository ingredientRepository;

    @Inject
    private IngredientMapper ingredientMapper;

    /**
     * Save a ingredient.
     *
     * @param ingredientDTO the entity to save
     * @return the persisted entity
     */
    public IngredientDTO save(IngredientDTO ingredientDTO) {
        log.debug("Request to save Ingredient : {}", ingredientDTO);
        Ingredient ingredient = ingredientMapper.ingredientDTOToIngredient(ingredientDTO);
        ingredient = ingredientRepository.save(ingredient);
        IngredientDTO result = ingredientMapper.ingredientToIngredientDTO(ingredient);
        return result;
    }

    /**
     *  Get all the ingredients.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<IngredientDTO> findAll() {
        log.debug("Request to get all Ingredients");
        List<IngredientDTO> result = ingredientRepository.findAll().stream()
            .map(ingredientMapper::ingredientToIngredientDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one ingredient by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public IngredientDTO findOne(Long id) {
        log.debug("Request to get Ingredient : {}", id);
        Ingredient ingredient = ingredientRepository.findOne(id);
        IngredientDTO ingredientDTO = ingredientMapper.ingredientToIngredientDTO(ingredient);
        return ingredientDTO;
    }

    /**
     *  Delete the  ingredient by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Ingredient : {}", id);
        ingredientRepository.delete(id);
    }
}
