package com.project.socialnetwork.dto.filter;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageFilterDto<T> {

    private Integer pageNumber;

    private Integer pageSize;

    private T filter;

    private String common;

    private String sortProperty;

    private Boolean asc = true;

}
