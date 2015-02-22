package de.tvcrowd.server.rest;

import de.tvcrowd.lib.dto.CreateTagDto;
import de.tvcrowd.server.Main;
import de.tvcrowd.server.entity.Movie;
import de.tvcrowd.server.entity.TVCrowdUser;
import de.tvcrowd.server.entity.Tag;
import de.tvcrowd.server.entity.manager.BaseManager;
import de.tvcrowd.server.rest.dto.MovieDto;
import de.tvcrowd.server.rest.dto.TagDto;
import de.tvcrowd.server.rest.dto.WatchingDto;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

@Path("app")
public class AppResource {

    @Path("list/tags")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(Main.USER_ROLE)
    public TagDto[] getCurrentMovieTags(@QueryParam("secondsBefore") Integer secondsBefore, @Context SecurityContext context) {

        TVCrowdUser user = BaseManager.get().find(TVCrowdUser.class, context.getUserPrincipal().getName());
        if (user.getWatching() != null) {
            return getMovieTags(user.getWatching().getMovie().getId(), user.getWatching().getCurrentSecond(), secondsBefore);
        } else {
            return null;
        }
    }

    @Path("list/tags/{movieid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public TagDto[] getMovieTags(@PathParam("movieid") int movieId, @QueryParam("seconds") Integer seconds, @QueryParam("secondsBefore") Integer secondsBefore) {
        if (seconds != null) {
            int secondsStart = seconds;
            if (secondsBefore != null) {
                // if secondsBefore is set, look x seconds before actual seconds for tags
                secondsStart -= secondsBefore;
            }
            return BaseManager.get().listTagsByMovie(movieId, secondsStart, seconds).stream().map((tag) -> new TagDto(tag)).toArray((size) -> new TagDto[size]);
        } else {
            return BaseManager.get().listTagsByMovie(movieId).stream().map((tag) -> new TagDto(tag)).toArray((size) -> new TagDto[size]);
        }
    }

    @Path("get/watching")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(Main.USER_ROLE)
    public WatchingDto getCurrentWatching(@Context SecurityContext context) {

        TVCrowdUser user = BaseManager.get().find(TVCrowdUser.class, context.getUserPrincipal().getName());
        if (user.getWatching() == null) {
            return null;
        } else {
            return new WatchingDto(user.getWatching());
        }
    }

    @Path("list/movies")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public MovieDto[] getMovies() {

        List<Movie> movies = BaseManager.get().listMovies();
        return movies.stream().map((movie) -> new MovieDto(movie)).toArray((size) -> new MovieDto[size]);
    }

    @Path("get/movie/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public MovieDto getMovie(@PathParam("id") int movieId) {
        return new MovieDto(BaseManager.get().find(Movie.class, movieId));
    }

    @Path("list/user/tags/{username}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public TagDto[] getUserTags(@PathParam("username") String username, @QueryParam("movieid") Integer movieId) {

        List<Tag> tags = BaseManager.get().listTagsByUser(username);

        if (movieId != null) {
            return tags.stream().filter((tag) -> tag.getMovie().getId().equals(movieId)).map((tag) -> new TagDto(tag)).toArray((size) -> new TagDto[size]);
        } else {
            return tags.stream().map((tag) -> new TagDto(tag)).toArray((size) -> new TagDto[size]);
        }
    }

    @Path("create/tag")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(Main.USER_ROLE)
    public TagDto createTag(CreateTagDto createTagDto, @Context SecurityContext context) {

        Tag newTag = new Tag();
        newTag.setComment(createTagDto.getComment());
        newTag.setMovie(BaseManager.get().getReference(Movie.class, createTagDto.getMovieId()));
        newTag.setSeconds(createTagDto.getSeconds());
        newTag.setUser(BaseManager.get().getReference(TVCrowdUser.class, context.getUserPrincipal().getName()));

        return new TagDto(BaseManager.get().save(newTag));
    }

    @Path("create/vote/{tagid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(Main.USER_ROLE)
    public TagDto createVote(@PathParam("tagid") Integer tagId, @Context SecurityContext context) {

        TVCrowdUser user = BaseManager.get().find(TVCrowdUser.class, context.getUserPrincipal().getName());
        if (!user.getVotes().stream().anyMatch((tag) -> tag.getId().equals(tagId))) {
            user.getVotes().add(BaseManager.get().getReference(Tag.class, tagId));
        }

        BaseManager.get().save(user);

        return new TagDto(BaseManager.get().find(Tag.class, tagId));
    }

}
