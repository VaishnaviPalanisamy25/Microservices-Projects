package com.project.employeeservice.service.impl;

import com.project.employeeservice.dto.APIResponseDto;
import com.project.employeeservice.dto.DepartmentDto;
import com.project.employeeservice.dto.EmployeeDto;
import com.project.employeeservice.entity.Employee;
import com.project.employeeservice.service.APIClient;
import com.project.employeeservice.service.EmployeeService;
import com.project.employeeservice.mapper.EmployeeMapper;
import com.project.employeeservice.repository.EmployeeRepository;
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
//    @Autowired
//    WebClient webClient;
    @Autowired
    APIClient apiClient;
    @Override
    public EmployeeDto saveEmployee(EmployeeDto employeeDto) {

        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);

        Employee saveDEmployee = employeeRepository.save(employee);

        EmployeeDto savedEmployeeDto = EmployeeMapper.mapToEmployeeDto(saveDEmployee);

        return savedEmployeeDto;
    }

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
        APIResponseDto apiResponseDto = new APIResponseDto(EmployeeMapper.mapToEmployeeDto(employee),departmentDto);
        return apiResponseDto;
    }
}
