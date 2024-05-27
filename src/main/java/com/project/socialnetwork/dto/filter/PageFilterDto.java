package com.project.socialnetwork.dto.filter;

import lombok.*;
import org.jooq.OrderField;
import org.jooq.impl.DSL;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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

    public OrderField getSort() {
        if (this.sortProperty == null) {
            return null;
        }
        OrderField<?> orderField;

        if (this.asc==true) {
            orderField = DSL.field(this.sortProperty).asc();
        } else {
            orderField = DSL.field(this.sortProperty).desc();
        }
        return orderField;
    }
    public Pageable getPageable() {
        if (this.pageSize == 0) return Pageable.unpaged();
        return PageRequest.of(this.getPageNumber(), this.getPageSize());
    }

    private String camelToSnake(String camelCase) {
        // Use a regular expression to find the positions where we need to insert underscores
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1_$2";

        // Replace the matches with the underscore and convert to lowercase
        String snakeCase = camelCase.replaceAll(regex, replacement).toLowerCase();

        return snakeCase;
    }

}
