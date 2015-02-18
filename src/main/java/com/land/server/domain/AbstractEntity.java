package com.land.server.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    public AbstractEntity() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean equals(LandObject obj) {
        if (obj == null) return false;
        if (obj.getClass().getCanonicalName() != this.getClass().getCanonicalName()) return false;
        if (this.getId() == null || ((AbstractEntity) obj).getId() == null) return false;
        return ((AbstractEntity) obj).getId().equals(this.getId());
    }
}
