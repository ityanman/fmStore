package com.fmjava.core.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PageResult implements Serializable {
   private List rows;
   private Long total;

    public PageResult(List rows, Long total) {
        this.rows = rows;
        this.total = total;
    }
}
