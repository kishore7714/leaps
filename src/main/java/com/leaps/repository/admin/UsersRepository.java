package com.leaps.repository.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.leaps.entity.admin.Users;

public interface UsersRepository extends JpaRepository<Users, Long> {
    @Query(value = "select * from users where valid_flag=1", nativeQuery = true)
    List<Users> getallActive();

    @Query(value = "select * from users where id=:id and valid_flag=1", nativeQuery = true)
    Optional<Users> getActiveById(Long id);

    @Query(value = "select * from users where id like %:key% and valid_flag = 1 or username like %:key% and valid_flag = 1 or email like %:key% and valid_flag = 1", nativeQuery = true)
    List<Users> globalSearch(String key);

    @Query(value = "select * from users where valid_flag=1", nativeQuery = true)
    Page<Users> getallActivePagination(Pageable pageable);
    
    Optional<Users> findByEmail(String email);
    
    Optional<Users> findByUsernameOrEmail(String username,String email);
    
    Optional<Users> findByUsername(String username);
    
    Boolean existsByUsername(String username);
    
    Boolean existsByEmail(String email);
}
