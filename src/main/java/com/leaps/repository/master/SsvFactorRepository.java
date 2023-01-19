package com.leaps.repository.master;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.leaps.entity.master.SsvFactor;

public interface SsvFactorRepository extends JpaRepository<SsvFactor, Long>{
	@Query(value = "select * from ssv_factor where valid_flag=1", nativeQuery = true)
    List<SsvFactor> getallActive();

    @Query(value = "select * from ssv_factor where id=:id and valid_flag=1", nativeQuery = true)
    Optional<SsvFactor> getActiveById(Long id);

    @Query(value = "select * from ssv_factor where id like %:key% and valid_flag = 1 or uin_number like %:key% and valid_flag = 1", nativeQuery = true)
    List<SsvFactor> globalSearch(String key);

    @Query(value = "select * from ssv_factor where valid_flag=1", nativeQuery = true)
    Page<SsvFactor> getallActivePagination(Pageable pageable);
}
