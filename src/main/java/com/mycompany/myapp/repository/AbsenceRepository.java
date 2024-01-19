package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Absence;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Absence entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AbsenceRepository extends JpaRepository<Absence, Long> {}
