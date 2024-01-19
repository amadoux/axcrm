package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.EnterpriseDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Enterprise}.
 */
public interface EnterpriseService {
    /**
     * Save a enterprise.
     *
     * @param enterpriseDTO the entity to save.
     * @return the persisted entity.
     */
    EnterpriseDTO save(EnterpriseDTO enterpriseDTO);

    /**
     * Updates a enterprise.
     *
     * @param enterpriseDTO the entity to update.
     * @return the persisted entity.
     */
    EnterpriseDTO update(EnterpriseDTO enterpriseDTO);

    /**
     * Partially updates a enterprise.
     *
     * @param enterpriseDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EnterpriseDTO> partialUpdate(EnterpriseDTO enterpriseDTO);

    /**
     * Get all the enterprises.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EnterpriseDTO> findAll(Pageable pageable);

    /**
     * Get the "id" enterprise.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EnterpriseDTO> findOne(Long id);

    /**
     * Delete the "id" enterprise.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
