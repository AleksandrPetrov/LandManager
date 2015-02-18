package com.land.shared.dto;

@SuppressWarnings("serial")
public abstract class EntityDto implements IDto {
    protected Long id;

    public EntityDto() {
        super();
    }

    public EntityDto(Long id) {
        super();
        this.id = id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        EntityDto other = (EntityDto) obj;
        if (id == null) {
            if (other.getId() != null) return false;
        }
        else if (!id.equals(other.getId())) return false;
        return true;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}
