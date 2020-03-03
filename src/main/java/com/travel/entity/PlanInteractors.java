package com.travel.entity;

import org.springframework.data.annotation.Id;

import javax.persistence.*;

public class PlanInteractors {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "status")
    private Integer status;
    @JoinColumn(name = "plan_id", referencedColumnName = "id")
    @ManyToOne
    private Plan planId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private User userId;
}
