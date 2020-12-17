package com.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

//有参无参构造
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Book {
    private Integer id;
    private String name;
    private String author;
    private Integer count;
    private String desc;


}
