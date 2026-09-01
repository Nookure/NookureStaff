package com.nookure.staff.api.model;

import static com.nookure.staff.api.database.AbstractPluginConnection.DATABASE_NAME;

import io.ebean.Model;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import java.time.Instant;

@MappedSuperclass
public class BaseDomain extends Model {
    @Id
    Long id;

    @Version
    Long version;

    @WhenCreated
    Instant whenCreated;

    @WhenModified
    Instant whenModified;

    public BaseDomain() {
        super(DATABASE_NAME);
    }

    public Long getId() {
        return id;
    }

    public BaseDomain setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    public BaseDomain setVersion(Long version) {
        this.version = version;
        return this;
    }

    public Instant getWhenCreated() {
        return whenCreated;
    }

    public BaseDomain setWhenCreated(Instant whenCreated) {
        this.whenCreated = whenCreated;
        return this;
    }

    public Instant getWhenModified() {
        return whenModified;
    }

    public BaseDomain setWhenModified(Instant whenModified) {
        this.whenModified = whenModified;
        return this;
    }
}
