package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SocialCharges;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SocialCharges entity.
 */
@Repository
public interface SocialChargesRepository extends JpaRepository<SocialCharges, Long> {
    default Optional<SocialCharges> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<SocialCharges> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<SocialCharges> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select socialCharges from SocialCharges socialCharges left join fetch socialCharges.responsableDepense left join fetch socialCharges.enterprise",
        countQuery = "select count(socialCharges) from SocialCharges socialCharges"
    )
    Page<SocialCharges> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select socialCharges from SocialCharges socialCharges left join fetch socialCharges.responsableDepense left join fetch socialCharges.enterprise"
    )
    List<SocialCharges> findAllWithToOneRelationships();

    @Query(
        "select socialCharges from SocialCharges socialCharges left join fetch socialCharges.responsableDepense left join fetch socialCharges.enterprise where socialCharges.id =:id"
    )
    Optional<SocialCharges> findOneWithToOneRelationships(@Param("id") Long id);
}
