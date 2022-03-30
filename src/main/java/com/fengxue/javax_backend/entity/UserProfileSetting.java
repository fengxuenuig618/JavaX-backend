package com.fengxue.javax_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user_profile_setting")
public class UserProfileSetting {
    @Id
    @Column(name = "user_id")
    private int uid;

    @Column(name = "has_set")
    private int hasSet;

    @Column(name = "education_level")
    private int educationLevel;

    @Column(name = "major")
    private int major;

    @Column(name = "java_experience")
    private int javaExperience;

    @Column(name = "other_experience")
    private int otherExperience;

    @Column(name = "global_level")
    private int globalLevel;

    @Column(name = "select_modules")
    private String selectModules;

    @Transient
    private Map<String, Boolean> modulesMap = new HashMap<String, Boolean>();;

}
