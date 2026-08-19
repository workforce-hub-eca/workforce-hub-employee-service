package com.workforcehub.employeeservice.service.impl;

import com.workforcehub.employeeservice.dto.EmployeeDTO;
import com.workforcehub.employeeservice.entity.Employee;
import com.workforcehub.employeeservice.exception.ResourceNotFoundException;
import com.workforcehub.employeeservice.exception.EmailAlreadyExistsException;
import com.workforcehub.employeeservice.repository.EmployeeRepository;
import com.workforcehub.employeeservice.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final RestTemplate restTemplate;

    @Override
    public EmployeeDTO saveEmployee(EmployeeDTO employeeDTO) {
        if (employeeRepository.existsByEmail(employeeDTO.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists: " + employeeDTO.getEmail());
        }
        
        validateDepartment(employeeDTO.getDepartmentId());
        Employee employee = mapToEntity(employeeDTO);
        Employee savedEmployee = employeeRepository.save(employee);
        return mapToDTO(savedEmployee);
    }

    @Override
    public EmployeeDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        return mapToDTO(employee);
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDTO> getEmployeesByDepartmentId(Long departmentId) {
        return employeeRepository.findByDepartmentId(departmentId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        Optional<Employee> existingEmployee = employeeRepository.findByEmail(employeeDTO.getEmail());
        if (existingEmployee.isPresent() && !existingEmployee.get().getId().equals(id)) {
            throw new EmailAlreadyExistsException("Email already exists: " + employeeDTO.getEmail());
        }

        if (!employee.getDepartmentId().equals(employeeDTO.getDepartmentId())) {
            validateDepartment(employeeDTO.getDepartmentId());
        }

        employee.setName(employeeDTO.getName());
        employee.setEmail(employeeDTO.getEmail());
        employee.setPhone(employeeDTO.getPhone());
        employee.setDepartmentId(employeeDTO.getDepartmentId());

        Employee updatedEmployee = employeeRepository.save(employee);
        return mapToDTO(updatedEmployee);
    }

    @Override
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        employeeRepository.delete(employee);
    }

    private void validateDepartment(Long departmentId) {
        try {
            restTemplate.getForObject("http://department-service/api/v1/departments/" + departmentId, Object.class);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new ResourceNotFoundException("Department not found with id: " + departmentId);
        } catch (HttpClientErrorException ex) {
            throw new RuntimeException("Error communicating with department-service: " + ex.getMessage());
        }
    }

    private EmployeeDTO mapToDTO(Employee employee) {
        return new EmployeeDTO(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getPhone(),
                employee.getDepartmentId()
        );
    }

    private Employee mapToEntity(EmployeeDTO employeeDTO) {
        return new Employee(
                employeeDTO.getId(),
                employeeDTO.getName(),
                employeeDTO.getEmail(),
                employeeDTO.getPhone(),
                employeeDTO.getDepartmentId()
        );
    }
}
