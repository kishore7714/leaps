package com.leaps.repository.master;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.leaps.entity.master.LeapNav;

public interface LeapNavRepository extends JpaRepository<LeapNav, Long>{
	@Query(value = "select * from leap_nav where valid_flag=1", nativeQuery = true)
    List<LeapNav> getallActive();

    @Query(value = "select * from leap_nav where id=:id and valid_flag=1", nativeQuery = true)
    Optional<LeapNav> getActiveById(Long id);

    @Query(value = "select * from leap_nav where id like %:key% and valid_flag = 1 or fund_code like %:key% and valid_flag = 1", nativeQuery = true)
    List<LeapNav> globalSearch(String key);

    @Query(value = "select * from leap_nav where valid_flag=1", nativeQuery = true)
    Page<LeapNav> getallActivePagination(Pageable pageable);
    
	@Query(value = "select * from leap_nav where fund_code=:fundCode and nav_date=:navDate and valid_flag=1", nativeQuery = true)
	LeapNav getByFundCode(String fundCode, String navDate);
}
