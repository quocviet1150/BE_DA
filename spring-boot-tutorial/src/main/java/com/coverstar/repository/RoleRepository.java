package com.coverstar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coverstar.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{}