package de.tvcrowd.server.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Marcel Carlé <mc@marcel-carle.de>
 */
@Entity
public class Movie implements Serializable {

    private Integer id;      // id of the movie
    private String name;  // name of the movie
    private Integer duration; // in seconds
    private List<Tag> tags = new ArrayList<>(); // tags

    @Id
    @GeneratedValue
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "movie")
    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

}
