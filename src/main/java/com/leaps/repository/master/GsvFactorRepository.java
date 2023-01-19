package com.leaps.repository.master;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.leaps.entity.master.GsvFactor;

public interface GsvFactorRepository extends JpaRepository<GsvFactor, Long>{

	@Query(value = "select * from gsv_factor where valid_flag=1", nativeQuery = true)
    List<GsvFactor> getallActive();

    @Query(value = "select * from gsv_factor where id=:id and valid_flag=1", nativeQuery = true)
    Optional<GsvFactor> getActiveById(Long id);

    @Query(value = "select * from gsv_factor where id like %:key% and valid_flag = 1 or uin_number like %:key% and valid_flag = 1", nativeQuery = true)
    List<GsvFactor> globalSearch(String key);

    @Query(value = "select * from gsv_factor where valid_flag=1", nativeQuery = true)
    Page<GsvFactor> getallActivePagination(Pageable pageable);
}
