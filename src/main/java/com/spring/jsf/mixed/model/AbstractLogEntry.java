package com.spring.jsf.mixed.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
public abstract class AbstractLogEntry<PK extends Serializable> extends AbstractEntity<PK> {
    /* Log field */

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Created_At")
    private Date createdAt;

    @JoinColumn(name = "Created_By")
    @OneToOne(targetEntity = User.class)
    private User createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Modified_At")
    private Date modifiedAt;

    @JoinColumn(name = "Modified_By")
    @OneToOne(targetEntity = User.class)
    private User modifiedBy;

    /*getter and setters */

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public User getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
}
