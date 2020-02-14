package com.mycompany.myapp.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

import com.mycompany.myapp.domain.enumeration.InterviewTypeEnum;

/**
 * A Interview.
 */
@Entity
@Table(name = "interview")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Interview implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "occured_date")
    private LocalDate occuredDate;

    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "description")
    private String description;

    @Column(name = "salary")
    private Float salary;

    @Column(name = "contact")
    private String contact;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private InterviewTypeEnum type;

    @ManyToOne
    @JsonIgnoreProperties("interviews")
    private Opportunity opportunity;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getOccuredDate() {
        return occuredDate;
    }

    public Interview occuredDate(LocalDate occuredDate) {
        this.occuredDate = occuredDate;
        return this;
    }

    public void setOccuredDate(LocalDate occuredDate) {
        this.occuredDate = occuredDate;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public Interview jobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
        return this;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getDescription() {
        return description;
    }

    public Interview description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getSalary() {
        return salary;
    }

    public Interview salary(Float salary) {
        this.salary = salary;
        return this;
    }

    public void setSalary(Float salary) {
        this.salary = salary;
    }

    public String getContact() {
        return contact;
    }

    public Interview contact(String contact) {
        this.contact = contact;
        return this;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public InterviewTypeEnum getType() {
        return type;
    }

    public Interview type(InterviewTypeEnum type) {
        this.type = type;
        return this;
    }

    public void setType(InterviewTypeEnum type) {
        this.type = type;
    }

    public Opportunity getOpportunity() {
        return opportunity;
    }

    public Interview opportunity(Opportunity opportunity) {
        this.opportunity = opportunity;
        return this;
    }

    public void setOpportunity(Opportunity opportunity) {
        this.opportunity = opportunity;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Interview)) {
            return false;
        }
        return id != null && id.equals(((Interview) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Interview{" +
            "id=" + getId() +
            ", occuredDate='" + getOccuredDate() + "'" +
            ", jobTitle='" + getJobTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", salary=" + getSalary() +
            ", contact='" + getContact() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
