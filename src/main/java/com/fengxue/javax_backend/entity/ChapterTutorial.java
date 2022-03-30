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
@Entity(name = "course_tutorial")
public class ChapterTutorial {
    @Id
    @Column(name = "tutorial_id")
    private String tutorialId;

    @Column(name = "chapter_belong")
    private String chapterBelong;

    @Column(name = "title")
    private String tutorialTitle;

    @Column(name = "content")
    private String tutorialContent;

    @Column(name = "global_level")
    private int globalLevel;
}
