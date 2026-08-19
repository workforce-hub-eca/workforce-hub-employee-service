package com.workforcehub.employeeservice.repository;

import com.workforcehub.employeeservice.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByDepartmentId(Long departmentId);
    boolean existsByEmail(String email);
    Optional<Employee> findByEmail(String email);
}
