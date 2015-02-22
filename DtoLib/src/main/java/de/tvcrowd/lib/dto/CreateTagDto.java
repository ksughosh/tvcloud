package de.tvcrowd.lib.dto;

/**
 *
 * @author Marcel Carlé <mc@marcel-carle.de>
 */
public class CreateTagDto {

    private Integer movieId;
    private String comment;
    private Integer seconds;

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getSeconds() {
        return seconds;
    }

    public void setSeconds(Integer seconds) {
        this.seconds = seconds;
    }

}
