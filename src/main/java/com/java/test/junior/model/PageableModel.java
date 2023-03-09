package com.java.test.junior.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author artiom.spac
 * @version java-test-junior
 * @apiNote 08.03.2023
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PageableModel<T> {
    @JsonProperty("total_pages")
    private Integer totalPages;
    @JsonProperty("total_objects")
    private Integer totalObject;
    private List<T> objects;
}
