package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.SocialCharges;
import com.mycompany.myapp.repository.SocialChargesRepository;
import com.mycompany.myapp.service.SocialChargesService;
import com.mycompany.myapp.service.dto.SocialChargesDTO;
import com.mycompany.myapp.service.mapper.SocialChargesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.SocialCharges}.
 */
@Service
@Transactional
public class SocialChargesServiceImpl implements SocialChargesService {

    private final Logger log = LoggerFactory.getLogger(SocialChargesServiceImpl.class);

    private final SocialChargesRepository socialChargesRepository;

    private final SocialChargesMapper socialChargesMapper;

    public SocialChargesServiceImpl(SocialChargesRepository socialChargesRepository, SocialChargesMapper socialChargesMapper) {
        this.socialChargesRepository = socialChargesRepository;
        this.socialChargesMapper = socialChargesMapper;
    }

    @Override
    public SocialChargesDTO save(SocialChargesDTO socialChargesDTO) {
        log.debug("Request to save SocialCharges : {}", socialChargesDTO);
        SocialCharges socialCharges = socialChargesMapper.toEntity(socialChargesDTO);
        socialCharges = socialChargesRepository.save(socialCharges);
        return socialChargesMapper.toDto(socialCharges);
    }

    @Override
    public SocialChargesDTO update(SocialChargesDTO socialChargesDTO) {
        log.debug("Request to update SocialCharges : {}", socialChargesDTO);
        SocialCharges socialCharges = socialChargesMapper.toEntity(socialChargesDTO);
        socialCharges = socialChargesRepository.save(socialCharges);
        return socialChargesMapper.toDto(socialCharges);
    }

    @Override
    public Optional<SocialChargesDTO> partialUpdate(SocialChargesDTO socialChargesDTO) {
        log.debug("Request to partially update SocialCharges : {}", socialChargesDTO);

        return socialChargesRepository
            .findById(socialChargesDTO.getId())
            .map(existingSocialCharges -> {
                socialChargesMapper.partialUpdate(existingSocialCharges, socialChargesDTO);

                return existingSocialCharges;
            })
            .map(socialChargesRepository::save)
            .map(socialChargesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SocialChargesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SocialCharges");
        return socialChargesRepository.findAll(pageable).map(socialChargesMapper::toDto);
    }

    public Page<SocialChargesDTO> findAllWithEagerRelationships(Pageable pageable) {
        return socialChargesRepository.findAllWithEagerRelationships(pageable).map(socialChargesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SocialChargesDTO> findOne(Long id) {
        log.debug("Request to get SocialCharges : {}", id);
        return socialChargesRepository.findOneWithEagerRelationships(id).map(socialChargesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SocialCharges : {}", id);
        socialChargesRepository.deleteById(id);
    }
}
