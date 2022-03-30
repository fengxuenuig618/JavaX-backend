package com.fengxue.javax_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileSettingTransfer {
    private int uid;

    private int hasSet;

    private int educationLevel;

    private int major;

    private int javaExperience;

    private int otherExperience;

    private int globalLevel;

    private Map<String, Boolean> selectModules = new HashMap<String, Boolean>();
}
