package com.mycompany.myapp.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A SellingProduct.
 */
@Entity
@Table(name = "selling_product")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SellingProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "v_2_product_name")
    private String v2ProductName;

    @Column(name = "v_2_product_category")
    private String v2ProductCategory;

    @Column(name = "units_sold")
    private Integer unitsSold;

    @Column(name = "revenue")
    private Float revenue;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getv2ProductName() {
        return v2ProductName;
    }

    public SellingProduct v2ProductName(String v2ProductName) {
        this.v2ProductName = v2ProductName;
        return this;
    }

    public void setv2ProductName(String v2ProductName) {
        this.v2ProductName = v2ProductName;
    }

    public String getv2ProductCategory() {
        return v2ProductCategory;
    }

    public SellingProduct v2ProductCategory(String v2ProductCategory) {
        this.v2ProductCategory = v2ProductCategory;
        return this;
    }

    public void setv2ProductCategory(String v2ProductCategory) {
        this.v2ProductCategory = v2ProductCategory;
    }

    public Integer getUnitsSold() {
        return unitsSold;
    }

    public SellingProduct unitsSold(Integer unitsSold) {
        this.unitsSold = unitsSold;
        return this;
    }

    public void setUnitsSold(Integer unitsSold) {
        this.unitsSold = unitsSold;
    }

    public Float getRevenue() {
        return revenue;
    }

    public SellingProduct revenue(Float revenue) {
        this.revenue = revenue;
        return this;
    }

    public void setRevenue(Float revenue) {
        this.revenue = revenue;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SellingProduct)) {
            return false;
        }
        return id != null && id.equals(((SellingProduct) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "SellingProduct{" +
            "id=" + getId() +
            ", v2ProductName='" + getv2ProductName() + "'" +
            ", v2ProductCategory='" + getv2ProductCategory() + "'" +
            ", unitsSold=" + getUnitsSold() +
            ", revenue=" + getRevenue() +
            "}";
    }
}
