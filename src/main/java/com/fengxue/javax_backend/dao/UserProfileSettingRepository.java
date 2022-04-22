package com.fengxue.javax_backend.dao;
import com.fengxue.javax_backend.entity.UserProfileSetting;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserProfileSettingRepository extends JpaRepository<UserProfileSetting,Integer>{
    UserProfileSetting findByUid(int userID);
}
