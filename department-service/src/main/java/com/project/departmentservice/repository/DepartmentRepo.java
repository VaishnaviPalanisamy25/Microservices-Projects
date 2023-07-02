package com.project.departmentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.departmentservice.entity.Department;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepo extends JpaRepository<Department,Long>{
    Department findByDepartmentCode(String departmentCode);
}
