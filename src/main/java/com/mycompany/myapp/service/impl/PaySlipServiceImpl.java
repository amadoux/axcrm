package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.PaySlip;
import com.mycompany.myapp.repository.PaySlipRepository;
import com.mycompany.myapp.service.PaySlipService;
import com.mycompany.myapp.service.dto.PaySlipDTO;
import com.mycompany.myapp.service.mapper.PaySlipMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.PaySlip}.
 */
@Service
@Transactional
public class PaySlipServiceImpl implements PaySlipService {

    private final Logger log = LoggerFactory.getLogger(PaySlipServiceImpl.class);

    private final PaySlipRepository paySlipRepository;

    private final PaySlipMapper paySlipMapper;

    public PaySlipServiceImpl(PaySlipRepository paySlipRepository, PaySlipMapper paySlipMapper) {
        this.paySlipRepository = paySlipRepository;
        this.paySlipMapper = paySlipMapper;
    }

    @Override
    public PaySlipDTO save(PaySlipDTO paySlipDTO) {
        log.debug("Request to save PaySlip : {}", paySlipDTO);
        PaySlip paySlip = paySlipMapper.toEntity(paySlipDTO);
        paySlip = paySlipRepository.save(paySlip);
        return paySlipMapper.toDto(paySlip);
    }

    @Override
    public PaySlipDTO update(PaySlipDTO paySlipDTO) {
        log.debug("Request to update PaySlip : {}", paySlipDTO);
        PaySlip paySlip = paySlipMapper.toEntity(paySlipDTO);
        paySlip = paySlipRepository.save(paySlip);
        return paySlipMapper.toDto(paySlip);
    }

    @Override
    public Optional<PaySlipDTO> partialUpdate(PaySlipDTO paySlipDTO) {
        log.debug("Request to partially update PaySlip : {}", paySlipDTO);

        return paySlipRepository
            .findById(paySlipDTO.getId())
            .map(existingPaySlip -> {
                paySlipMapper.partialUpdate(existingPaySlip, paySlipDTO);

                return existingPaySlip;
            })
            .map(paySlipRepository::save)
            .map(paySlipMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaySlipDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PaySlips");
        return paySlipRepository.findAll(pageable).map(paySlipMapper::toDto);
    }

    public Page<PaySlipDTO> findAllWithEagerRelationships(Pageable pageable) {
        return paySlipRepository.findAllWithEagerRelationships(pageable).map(paySlipMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaySlipDTO> findOne(Long id) {
        log.debug("Request to get PaySlip : {}", id);
        return paySlipRepository.findOneWithEagerRelationships(id).map(paySlipMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PaySlip : {}", id);
        paySlipRepository.deleteById(id);
    }
}
