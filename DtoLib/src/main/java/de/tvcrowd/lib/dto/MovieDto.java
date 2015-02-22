package de.tvcrowd.lib.dto;

/**
 *
 * @author Marcel Carlé <mc@marcel-carle.de>
 */
public class MovieDto {

    private Integer id;
    private String name;
    private Integer duration;
    private Integer tagCount;

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

    public Integer getTagCount() {
        return tagCount;
    }

    public void setTagCount(Integer tagCount) {
        this.tagCount = tagCount;
    }

}
