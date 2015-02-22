package de.tvcrowd.server.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 *
 * @author Marcel Carlé <mc@marcel-carle.de>
 */
@Entity
public class Watching implements Serializable {

    private Integer id;
    private Date lastModified;
    private TVCrowdUser tvCrowdUser;
    private Movie movie;
    private Integer currentSecond;

    @Id
    @GeneratedValue
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Version
    @Temporal(TemporalType.TIMESTAMP)
    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    @OneToOne(optional = false, mappedBy = "watching")
    public TVCrowdUser getTvCrowdUser() {
        return tvCrowdUser;
    }

    public void setTvCrowdUser(TVCrowdUser tvCrowdUser) {
        this.tvCrowdUser = tvCrowdUser;
    }

    @ManyToOne(optional = false)
    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    @Basic(optional = false)
    public Integer getCurrentSecond() {
        return currentSecond;
    }

    public void setCurrentSecond(Integer currentSecond) {
        this.currentSecond = currentSecond;
    }

}
