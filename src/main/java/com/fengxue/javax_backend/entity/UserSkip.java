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
@Entity(name = "user_skip")
public class UserSkip {
    @Id
    @Column(name = "user_id")
    private int uid;

    @Column(name = "skip_tutorial")
    private String skipTutorial;
}
