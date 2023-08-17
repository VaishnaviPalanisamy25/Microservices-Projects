package com.project.employeeservice.service.impl;

import com.project.employeeservice.dto.APIResponseDto;
import com.project.employeeservice.dto.DepartmentDto;
import com.project.employeeservice.dto.EmployeeDto;
import com.project.employeeservice.dto.OrganizationDto;
import com.project.employeeservice.entity.Employee;
import com.project.employeeservice.service.APIClient;
import com.project.employeeservice.service.EmployeeService;
import com.project.employeeservice.mapper.EmployeeMapper;
import com.project.employeeservice.repository.EmployeeRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;

//    @Autowired
//    RestTemplate restTemplate;
    @Autowired
    WebClient webClient;
    @Autowired
    APIClient apiClient;
    @Override
    public EmployeeDto saveEmployee(EmployeeDto employeeDto) {

        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);

        Employee saveDEmployee = employeeRepository.save(employee);

        EmployeeDto savedEmployeeDto = EmployeeMapper.mapToEmployeeDto(saveDEmployee);

        return savedEmployeeDto;
    }

    @CircuitBreaker(name="${spring.application.name}",fallbackMethod = "getDefaultDepartment")
    @Override
    public APIResponseDto getEmployeeById(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).get();

//        ResponseEntity<DepartmentDto> responseEntity = restTemplate.getForEntity(
//                "http://localhost:8080/api/departments/"+ employee.getDepartmentCode(),
//                DepartmentDto.class);
//        DepartmentDto departmentDto = responseEntity.getBody();

//       DepartmentDto departmentDto = webClient.get()
//                .uri("http://localhost:8080/api/departments/" + employee.getDepartmentCode())
//                .retrieve()
//                .bodyToMono(DepartmentDto.class)
//                .block();
        DepartmentDto departmentDto = apiClient.getDepartment(employee.getDepartmentCode());
        OrganizationDto organizationDto = webClient.get()
                .uri("http://localhost:8083/api/organizations/" + employee.getOrganizationCode())
                .retrieve()
                .bodyToMono(OrganizationDto.class)
                .block();
        APIResponseDto apiResponseDto = new APIResponseDto(EmployeeMapper.mapToEmployeeDto(employee),departmentDto,organizationDto);
        return apiResponseDto;
    }

    public APIResponseDto getDefaultDepartment(Long employeeId,Exception exception) {
        Employee employee = employeeRepository.findById(employeeId).get();
        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setDepartmentName("R&D Department");
        departmentDto.setDepartmentCode("R001");
        departmentDto.setDepartmentDescription("Research and development");
        OrganizationDto organizationDto =new OrganizationDto();
        organizationDto.setOrganizationCode("B001");
        organizationDto.setOrganizationName("XXXX");
        organizationDto.setOrganizationDescription("YYYXXYY");
        APIResponseDto apiResponseDto = new APIResponseDto(EmployeeMapper.mapToEmployeeDto(employee),departmentDto,organizationDto);
        return apiResponseDto;
    }
}
