package com.mycompany.myapp.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.mycompany.myapp.domain.enumeration.NetworkEnum;

/**
 * A Opportunity.
 */
@Entity
@Table(name = "opportunity")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Opportunity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "place")
    private String place;

    @Enumerated(EnumType.STRING)
    @Column(name = "contact_from")
    private NetworkEnum contactFrom;

    @OneToMany(mappedBy = "opportunity")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Interview> interviews = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public Opportunity companyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPlace() {
        return place;
    }

    public Opportunity place(String place) {
        this.place = place;
        return this;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public NetworkEnum getContactFrom() {
        return contactFrom;
    }

    public Opportunity contactFrom(NetworkEnum contactFrom) {
        this.contactFrom = contactFrom;
        return this;
    }

    public void setContactFrom(NetworkEnum contactFrom) {
        this.contactFrom = contactFrom;
    }

    public Set<Interview> getInterviews() {
        return interviews;
    }

    public Opportunity interviews(Set<Interview> interviews) {
        this.interviews = interviews;
        return this;
    }

    public Opportunity addInterview(Interview interview) {
        this.interviews.add(interview);
        interview.setOpportunity(this);
        return this;
    }

    public Opportunity removeInterview(Interview interview) {
        this.interviews.remove(interview);
        interview.setOpportunity(null);
        return this;
    }

    public void setInterviews(Set<Interview> interviews) {
        this.interviews = interviews;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Opportunity)) {
            return false;
        }
        return id != null && id.equals(((Opportunity) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Opportunity{" +
            "id=" + getId() +
            ", companyName='" + getCompanyName() + "'" +
            ", place='" + getPlace() + "'" +
            ", contactFrom='" + getContactFrom() + "'" +
            "}";
    }
}
