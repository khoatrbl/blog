package com.khoatrbl.blog.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateTagRequest {
    @NotBlank(message = "Tag name is required")
    @Size(min = 2, max = 50, message = "Tag name must be between 2 and 50 characters")
    @Pattern(regexp = "^[\\w\\s-]+$", message = "Tag name can only contains letters, numbers, spaces and hyphens")
    private Set<String> tagNames;
}
