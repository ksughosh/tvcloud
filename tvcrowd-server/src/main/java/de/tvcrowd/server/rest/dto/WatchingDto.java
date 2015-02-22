package de.tvcrowd.server.rest.dto;

import de.tvcrowd.server.entity.Watching;

/**
 *
 * @author Marcel Carlé <mc@marcel-carle.de>
 */
public class WatchingDto extends de.tvcrowd.lib.dto.WatchingDto {

    public WatchingDto(Watching watching) {
        setMovieId(watching.getMovie().getId());
        setSecond(watching.getCurrentSecond());
    }
}
