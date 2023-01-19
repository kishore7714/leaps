package com.leaps.dto.admin;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParameterDto {

    private Long id;

    private Long parameter;

    private String rule;

    @Size(max = 20, message = "Short Description Cannot be More than 20 characters")
    private String shortDescription;

    private String longDescription;

}
