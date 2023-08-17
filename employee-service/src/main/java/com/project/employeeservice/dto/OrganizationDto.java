package com.project.employeeservice.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrganizationDto {
        private Long id;
        private String organizationName;
        private String organizationDescription;
        private String organizationCode;
        private LocalDateTime createDate;
}
