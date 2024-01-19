package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SocialCharges;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SocialCharges entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SocialChargesRepository extends JpaRepository<SocialCharges, Long> {}
