package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Absence;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Absence entity.
 */
@Repository
public interface AbsenceRepository extends JpaRepository<Absence, Long> {
    default Optional<Absence> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Absence> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Absence> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select absence from Absence absence left join fetch absence.employee",
        countQuery = "select count(absence) from Absence absence"
    )
    Page<Absence> findAllWithToOneRelationships(Pageable pageable);

    @Query("select absence from Absence absence left join fetch absence.employee")
    List<Absence> findAllWithToOneRelationships();

    @Query("select absence from Absence absence left join fetch absence.employee where absence.id =:id")
    Optional<Absence> findOneWithToOneRelationships(@Param("id") Long id);
}
