package com.leaps.error;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ErrorRepository extends JpaRepository<Error, Long> {

	@Query(value = "select error_description from error where error_id = :errorid", nativeQuery = true)
	String getErrorByErrorId(String errorid);

}
