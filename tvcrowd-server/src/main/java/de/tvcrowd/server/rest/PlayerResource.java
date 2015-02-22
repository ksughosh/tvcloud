package de.tvcrowd.server.rest;

import de.tvcrowd.server.Main;
import de.tvcrowd.server.entity.Movie;
import de.tvcrowd.server.entity.TVCrowdUser;
import de.tvcrowd.server.entity.Watching;
import de.tvcrowd.server.entity.manager.BaseManager;
import de.tvcrowd.server.rest.dto.TagDto;
import de.tvcrowd.server.rest.dto.TagStormDto;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;
import javax.annotation.security.RolesAllowed;
import javax.persistence.LockModeType;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

@Path("player")
public class PlayerResource {

    @POST
    @Path("update/{movieid}/{seconds}")
    @RolesAllowed(Main.USER_ROLE)
    public void update(@PathParam("movieid") int movieId, @PathParam("seconds") int seconds, @Context SecurityContext context) {

        TVCrowdUser user = BaseManager.get().find(TVCrowdUser.class, context.getUserPrincipal().getName());
        Watching watching = user.getWatching();
        if (watching != null) {
            boolean changed = false;
            if (watching.getMovie().getId() != movieId) {
                watching.setMovie(BaseManager.get().getReference(Movie.class, movieId));
                changed = true;
            }
            if (watching.getCurrentSecond() != seconds) {
                watching.setCurrentSecond(seconds);
                changed = true;
            }
            if (changed) {
                try {
                    BaseManager.get().save(watching);
                } catch (Exception e) {
                }
            }
        } else {
            watching = new Watching();
            watching.setCurrentSecond(seconds);
            watching.setMovie(BaseManager.get().getReference(Movie.class, movieId));
            watching.setTvCrowdUser(user);
            user.setWatching(watching);
            BaseManager.get().save(user);
        }
    }

//    @GET
//    @Path("list/tags/{movieId}")
//    public Response listTags(@PathParam("movieId") int movieId) throws URISyntaxException {
//        Response.ResponseBuilder builder = Response.status(Response.Status.MOVED_PERMANENTLY).location(new URI("app/list/tags/"+movieId));
//        return builder.build();
//    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("list/tags/{movieId}")
    public TagDto[] listTags(@PathParam("movieId") int movieId) {
        return new AppResource().getMovieTags(movieId, null, null);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("list/tagstorms/{movieId}/{period}")
    public TagStormDto[] listTagStorms(@PathParam("movieId") int movieId, @PathParam("period") int period) {
        List<TagStormDto> resultList = new ArrayList<>();
        BaseManager.get().listTagStroms(movieId, period).stream().map((tagStorm) -> {
            tagStorm.setTagCount(1);
            return tagStorm;
        }).forEach((tagStorm) -> {
            if (resultList.isEmpty()) {
                resultList.add(tagStorm);
            } else if (resultList.get(resultList.size() - 1).getPeriod() == tagStorm.getPeriod()) {
                resultList.get(resultList.size() - 1).setTagCount(resultList.get(resultList.size() - 1).getTagCount() + 1);
            } else {
                resultList.add(tagStorm);
            }
        });
        return resultList.toArray(new TagStormDto[resultList.size()]);
    }

}
