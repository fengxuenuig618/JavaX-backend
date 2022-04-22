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
@Entity(name = "user_feedback")
public class UserFeedback {
    @Id
    @Column(name = "user_id")
    private int uid;

    @Column(name = "user_rating")
    private String userRating;

    @Column(name = "user_review")
    private String userReview;
}
