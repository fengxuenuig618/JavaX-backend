package com.fengxue.javax_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user_account")
public class UserAccount {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int uid;

    @Column(name = "user_name")
    private String uname;

    @Column(name = "user_password")
    private String upw;

    @Column(name = "user_email")
    private String uemail;
}
