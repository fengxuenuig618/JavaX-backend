package com.fengxue.javax_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "course_module")
public class CourseModule {
    @Id
    @Column(name = "module_id")
    private String moduleId;

    @Column(name = "module_title")
    private String moduleTitle;

    @Column(name = "card_introduce")
    private String cardIntroduce;
}
