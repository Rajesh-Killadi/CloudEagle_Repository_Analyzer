package com.cloudeagle.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@ToString
public class RepoDto {
    private Long id;
    private String name;
    private String fullName;
  
}
