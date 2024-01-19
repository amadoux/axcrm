package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Contract;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Contract entity.
 */
@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    default Optional<Contract> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Contract> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Contract> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select contract from Contract contract left join fetch contract.employee",
        countQuery = "select count(contract) from Contract contract"
    )
    Page<Contract> findAllWithToOneRelationships(Pageable pageable);

    @Query("select contract from Contract contract left join fetch contract.employee")
    List<Contract> findAllWithToOneRelationships();

    @Query("select contract from Contract contract left join fetch contract.employee where contract.id =:id")
    Optional<Contract> findOneWithToOneRelationships(@Param("id") Long id);
}
