package com.leaps.repository.deathclaim;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.leaps.entity.deathclaim.DeathClaimCoverTablePas;

public interface DeathClaimCoverTablePasRepository extends JpaRepository<DeathClaimCoverTablePas, Long>{
	@Query(value = "select * from death_claim_cover_table_pas where valid_flag=1", nativeQuery = true)
    List<DeathClaimCoverTablePas> getallActive();

    @Query(value = "select * from death_claim_cover_table_pas where id=:id and valid_flag=1", nativeQuery = true)
    Optional<DeathClaimCoverTablePas> getActiveById(Long id);

    @Query(value = "select * from death_claim_cover_table_pas where id like %:key% and valid_flag = 1 or uin_number like %:key% and valid_flag = 1", nativeQuery = true)
    List<DeathClaimCoverTablePas> globalSearch(String key);

    @Query(value = "select * from death_claim_cover_table_pas where valid_flag=1", nativeQuery = true)
    Page<DeathClaimCoverTablePas> getallActivePagination(Pageable pageable);
}
