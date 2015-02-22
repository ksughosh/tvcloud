package de.tvcrowd.server.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author Marcel Carlé <mc@marcel-carle.de>
 */
@Entity
public class TVCrowdUser implements Serializable {

    private String username;    // username of the user
    private String password;    // password of the user
    private String name;    // name of the user
    private List<Tag> votes = new ArrayList<>();
    private Watching watching;

    @Id
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public List<Tag> getVotes() {
        return votes;
    }

    public void setVotes(List<Tag> votes) {
        this.votes = votes;
    }

    @OneToOne(cascade = CascadeType.ALL)
    public Watching getWatching() {
        return watching;
    }

    public void setWatching(Watching watching) {
        this.watching = watching;
    }

}
