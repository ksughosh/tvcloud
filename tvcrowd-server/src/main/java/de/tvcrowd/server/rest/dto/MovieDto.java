package de.tvcrowd.server.rest.dto;

import de.tvcrowd.server.entity.Movie;

/**
 *
 * @author Marcel Carlé <mc@marcel-carle.de>
 */
public class MovieDto extends de.tvcrowd.lib.dto.MovieDto {

    public MovieDto(Movie movie) {
        setId(movie.getId());
        setName(movie.getName());
        setDuration(movie.getDuration());
        setTagCount(movie.getTags().size());
    }

}
