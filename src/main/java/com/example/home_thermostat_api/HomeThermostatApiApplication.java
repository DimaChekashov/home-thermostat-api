package com.example.home_thermostat_api;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.home_thermostat_api.model.Permission;
import com.example.home_thermostat_api.model.Role;
import com.example.home_thermostat_api.repository.PermissionRepository;
import com.example.home_thermostat_api.repository.RoleRepository;

@SpringBootApplication
public class HomeThermostatApiApplication implements ApplicationRunner {
	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PermissionRepository permissionRepository;

	public static void main(String[] args) {
		SpringApplication.run(HomeThermostatApiApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		createPermissionsIfNotExists();
		createRolesIfNotExists();
	}

	private void createRolesIfNotExists() {
		if (roleRepository.count() == 0) {
			Role userRole = Role.builder().name("USER").build();
			Role moderatorRole = Role.builder().name("MODERATOR").build();
			Role managerRole = Role.builder().name("MANAGER").build();
			Role adminRole = Role.builder().name("ADMIN").build();

			roleRepository.save(userRole);
			roleRepository.save(managerRole);
			roleRepository.save(moderatorRole);
			roleRepository.save(adminRole);

			Permission productRead = permissionRepository.findByResourceAndOperation("product", "read").orElseThrow();
			Permission productWrite = permissionRepository.findByResourceAndOperation("product", "write").orElseThrow();
			Permission productDelete = permissionRepository.findByResourceAndOperation("product", "delete")
					.orElseThrow();

			userRole.setPermissions(new HashSet<>(Set.of(productRead)));

			managerRole.setPermissions(new HashSet<>(Set.of(productRead, productWrite)));

			adminRole.setPermissions(new HashSet<>(Set.of(productRead, productWrite, productDelete)));

			roleRepository.save(userRole);
			roleRepository.save(managerRole);
			roleRepository.save(adminRole);
		}
	}

	private void createPermissionsIfNotExists() {
		if (permissionRepository.count() == 0) {
			Iterable<Permission> permissions = List.of(
					new Permission("product", "read"),
					new Permission("product", "write"),
					new Permission("product", "delete"));

			permissionRepository.saveAll(permissions);
		}
	}
}
