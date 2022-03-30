package com.fengxue.javax_backend.dao;
import com.fengxue.javax_backend.entity.UserLogin;
import com.fengxue.javax_backend.entity.UserProfileSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;


public interface UserProfileSettingRepository extends JpaRepository<UserProfileSetting,Integer>{
    UserProfileSetting findByUid(int userID);
}
