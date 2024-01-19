package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.PaySlipDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.PaySlip}.
 */
public interface PaySlipService {
    /**
     * Save a paySlip.
     *
     * @param paySlipDTO the entity to save.
     * @return the persisted entity.
     */
    PaySlipDTO save(PaySlipDTO paySlipDTO);

    /**
     * Updates a paySlip.
     *
     * @param paySlipDTO the entity to update.
     * @return the persisted entity.
     */
    PaySlipDTO update(PaySlipDTO paySlipDTO);

    /**
     * Partially updates a paySlip.
     *
     * @param paySlipDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PaySlipDTO> partialUpdate(PaySlipDTO paySlipDTO);

    /**
     * Get all the paySlips.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PaySlipDTO> findAll(Pageable pageable);

    /**
     * Get all the paySlips with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PaySlipDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" paySlip.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PaySlipDTO> findOne(Long id);

    /**
     * Delete the "id" paySlip.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
