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
@Entity(name = "user_verification")
public class UserVerification {
    @Id
    @Column(name = "user_email")
    private String email;

    @Column(name = "verification_code")
    private String verificationCode;

    @Column(name = "time_stamp")
    private long timeStamp;
}
