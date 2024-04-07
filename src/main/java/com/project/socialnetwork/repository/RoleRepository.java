package com.project.socialnetwork.repository;

import com.project.socialnetwork.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {

    Optional<Role> getRoleById(Long id);

    @Query("""
    select r from Role r 
    where r.roleName=:name
""")
    Optional<Role> getRoleByName(@Param("name") String name);
}
