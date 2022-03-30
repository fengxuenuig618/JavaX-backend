package com.fengxue.javax_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "course_mcq")
public class CourseMcq {
    @Id
    @Column(name = "mcq_id")
    private String mcqId;

    @Column(name = "tutorial_belong")
    private String paragraphBelong;

    @Column(name = "chapter_belong")
    private String chapterBelong;

    @Column(name = "module_belong")
    private String moduleBelong;

    @Column(name = "question")
    private String question;

    @Transient
    List<String> options = new ArrayList<>();

    @Column(name = "options")
    private String strOptions;

    @Column(name = "answer")
    private String answer;

    @Column(name = "global_level")
    private int globalLevel;

    @Column(name = "module_title")
    private String moduleTitle;

    @Column(name = "chapter_title")
    private String chapterTitle;

    @Column(name = "tutorial_title")
    private String tutorialTitle;

}
