package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Enterprise;
import com.mycompany.myapp.repository.EnterpriseRepository;
import com.mycompany.myapp.service.EnterpriseService;
import com.mycompany.myapp.service.dto.EnterpriseDTO;
import com.mycompany.myapp.service.mapper.EnterpriseMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Enterprise}.
 */
@Service
@Transactional
public class EnterpriseServiceImpl implements EnterpriseService {

    private final Logger log = LoggerFactory.getLogger(EnterpriseServiceImpl.class);

    private final EnterpriseRepository enterpriseRepository;

    private final EnterpriseMapper enterpriseMapper;

    public EnterpriseServiceImpl(EnterpriseRepository enterpriseRepository, EnterpriseMapper enterpriseMapper) {
        this.enterpriseRepository = enterpriseRepository;
        this.enterpriseMapper = enterpriseMapper;
    }

    @Override
    public EnterpriseDTO save(EnterpriseDTO enterpriseDTO) {
        log.debug("Request to save Enterprise : {}", enterpriseDTO);
        Enterprise enterprise = enterpriseMapper.toEntity(enterpriseDTO);
        enterprise = enterpriseRepository.save(enterprise);
        return enterpriseMapper.toDto(enterprise);
    }

    @Override
    public EnterpriseDTO update(EnterpriseDTO enterpriseDTO) {
        log.debug("Request to update Enterprise : {}", enterpriseDTO);
        Enterprise enterprise = enterpriseMapper.toEntity(enterpriseDTO);
        enterprise = enterpriseRepository.save(enterprise);
        return enterpriseMapper.toDto(enterprise);
    }

    @Override
    public Optional<EnterpriseDTO> partialUpdate(EnterpriseDTO enterpriseDTO) {
        log.debug("Request to partially update Enterprise : {}", enterpriseDTO);

        return enterpriseRepository
            .findById(enterpriseDTO.getId())
            .map(existingEnterprise -> {
                enterpriseMapper.partialUpdate(existingEnterprise, enterpriseDTO);

                return existingEnterprise;
            })
            .map(enterpriseRepository::save)
            .map(enterpriseMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EnterpriseDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Enterprises");
        return enterpriseRepository.findAll(pageable).map(enterpriseMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EnterpriseDTO> findOne(Long id) {
        log.debug("Request to get Enterprise : {}", id);
        return enterpriseRepository.findById(id).map(enterpriseMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Enterprise : {}", id);
        enterpriseRepository.deleteById(id);
    }
}
