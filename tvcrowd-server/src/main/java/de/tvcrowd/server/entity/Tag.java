package de.tvcrowd.server.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

/**
 *
 * @author Marcel Carlé <mc@marcel-carle.de>
 */
@Entity
public class Tag implements Serializable {

    private Integer id;
    private Movie movie;
    private TVCrowdUser user;
    private String comment;
    private Integer seconds;
    private List<TVCrowdUser> votes = new ArrayList<>();

    @Id
    @GeneratedValue
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(optional = false)
    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    @ManyToOne(optional = false)
    public TVCrowdUser getUser() {
        return user;
    }

    public void setUser(TVCrowdUser user) {
        this.user = user;
    }

    @Basic(optional = false)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Basic(optional = false)
    public Integer getSeconds() {
        return seconds;
    }

    public void setSeconds(Integer seconds) {
        this.seconds = seconds;
    }

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "votes")
    public List<TVCrowdUser> getVotes() {
        return votes;
    }

    public void setVotes(List<TVCrowdUser> votes) {
        this.votes = votes;
    }

}
