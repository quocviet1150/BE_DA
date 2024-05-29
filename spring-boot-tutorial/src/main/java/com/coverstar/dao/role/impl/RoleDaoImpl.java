package com.coverstar.dao.role.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.coverstar.dao.role.RoleDao;
import com.coverstar.model.Role;
import com.coverstar.repository.RoleRepository;

@Repository
public class RoleDaoImpl implements RoleDao{

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public Optional<Role> findById(long id) {
		return roleRepository.findById(id);
	}

	@Override
	public List<Role> findAll() {
		return roleRepository.findAll();
	}

	@Override
	public Role create(Role entity) {
		return roleRepository.save(entity);
	}

	@Override
	public Role update(Role entity) {
		return roleRepository.save(entity);
	}

	@Override
	public void delete(Role entity) {
		roleRepository.delete(entity);
	}

	@Override
	public void deleteById(long entityId) {
		roleRepository.deleteById(entityId);
	}
	
	

}
