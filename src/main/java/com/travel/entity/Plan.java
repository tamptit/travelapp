package com.travel.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.travel.dto.PlanDto;
import com.travel.model.AuditModel;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Table(name = "plan")

public class Plan extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Name is required")
    @Size(min = 4, max = 200, message = "Please use 4 to 200 letters")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "Content is required")
    @Column(name = "content")
    private String content;

    @Column(name = "start_day")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date startDay;

    @Column(name = "end_day")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date endDay;

    @Column(name = "status")
    private int status;

    @Column(name = "created_day")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date createdDay;

    @Column(name = "image")
    private String imageCover;

    @Column(name = "num_people")
    private int numPeople;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "plan_id", nullable = false)
    private List<Schedule> schedules;

    @OneToMany(mappedBy = "plan")
    private List<PlanInteractor> planInteractors;

    public Plan() {
    }

    public Plan(long id) {
        this.id = id;
    }

    public PlanDto convertToDto() {
        return new PlanDto(this,this.getUser(),this.getPlanInteractors());
    }

    public Plan(@NotBlank(message = "Name is required") @Size(min = 4, max = 200, message = "Please use 4 to 200 letters") String name,
                @NotBlank(message = "Content is required") String content, Date startDay, Date endDay,
                @NotBlank(message = "Status is required") int status,
                Date createdDay, String imageCover,
                User user) {
        this.name = name;
        this.content = content;
        this.startDay = startDay;
        this.endDay = endDay;
        this.status = status;
        this.createdDay = createdDay;
        this.imageCover = imageCover;
        this.user = user;
    }

    public List<PlanInteractor> getPlanInteractors() {
        return planInteractors;
    }

    public void setPlanInteractors(List<PlanInteractor> planInteractors) {
        this.planInteractors = planInteractors;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreatedDay() {
        return createdDay;
    }

    public void setCreatedDay(Date createdDay) {
        this.createdDay = createdDay;
    }

    public Date getStartDay() {
        return startDay;
    }

    public void setStartDay(Date startDay) {
        this.startDay = startDay;
    }

    public Date getEndDay() {
        return endDay;
    }

    public void setEndDay(Date endDay) {
        this.endDay = endDay;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getImageCover() {
        return imageCover;
    }

    public void setImageCover(String imageCover) {
        this.imageCover = imageCover;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    public int getNumPeople() {
        return numPeople;
    }

    public void setNumPeople(int numPeople) {
        this.numPeople = numPeople;
    }

    @Override
    public String toString() {
        return "Plan{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", startDay=" + startDay +
                ", endDay=" + endDay +
                ", status='" + status + '\'' +
                ", createdDay=" + createdDay +
                ", image='" + imageCover + '\'' +
                ", user=" + user +
                '}';
    }
}
