package com.leaps.repository.surrender;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.leaps.entity.surrender.SurrenderCoverDetails;

public interface SurrenderCoverDetailsRepository extends JpaRepository<SurrenderCoverDetails, Long>{
	@Query(value = "select * from surrender_cover_details where valid_flag=1", nativeQuery = true)
    List<SurrenderCoverDetails> getallActive();

    @Query(value = "select * from surrender_cover_details where id=:id and valid_flag=1", nativeQuery = true)
    Optional<SurrenderCoverDetails> getActiveById(Long id);

    @Query(value = "select * from surrender_cover_details where id like %:key% and valid_flag = 1 or policy_no like %:key% and valid_flag = 1", nativeQuery = true)
    List<SurrenderCoverDetails> globalSearch(String key);

	@Query(value = "select * from surrender_cover_details where valid_flag=1", nativeQuery = true)
    Page<SurrenderCoverDetails> getallActivePagination(Pageable pageable);
}
