package de.tvcrowd.server.rest.dto;

import de.tvcrowd.server.entity.Tag;

/**
 *
 * @author Marcel Carlé <mc@marcel-carle.de>
 */
public class TagDto extends de.tvcrowd.lib.dto.TagDto{

    public TagDto(Tag tagEntity) {
        setId(tagEntity.getId());
        setMovieId(tagEntity.getMovie().getId());
        setUsername(tagEntity.getUser().getUsername());
        setComment(tagEntity.getComment());
        setSeconds(tagEntity.getSeconds());
        setVotes(tagEntity.getVotes().size());
    }

}
