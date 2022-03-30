package com.fengxue.javax_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "course_chapter")
public class CourseChapter {
    @Id
    @Column(name = "chapter_id")
    private String chapterId;

    @Column(name = "module_belong")
    private String moduleBelong;

    @Column(name = "chapter_title")
    private String chapterTitle;

    @Column(name = "global_level")
    private int globalLevel;
}
