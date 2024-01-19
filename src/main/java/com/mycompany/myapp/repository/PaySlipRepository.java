package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PaySlip;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PaySlip entity.
 */
@Repository
public interface PaySlipRepository extends JpaRepository<PaySlip, Long> {
    default Optional<PaySlip> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PaySlip> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PaySlip> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select paySlip from PaySlip paySlip left join fetch paySlip.employee",
        countQuery = "select count(paySlip) from PaySlip paySlip"
    )
    Page<PaySlip> findAllWithToOneRelationships(Pageable pageable);

    @Query("select paySlip from PaySlip paySlip left join fetch paySlip.employee")
    List<PaySlip> findAllWithToOneRelationships();

    @Query("select paySlip from PaySlip paySlip left join fetch paySlip.employee where paySlip.id =:id")
    Optional<PaySlip> findOneWithToOneRelationships(@Param("id") Long id);
}
