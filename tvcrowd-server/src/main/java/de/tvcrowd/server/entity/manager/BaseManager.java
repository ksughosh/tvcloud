package de.tvcrowd.server.entity.manager;

import de.tvcrowd.server.entity.Movie;
import de.tvcrowd.server.entity.Tag;
import de.tvcrowd.server.rest.dto.TagStormDto;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.LockModeType;
import javax.persistence.Persistence;

/**
 * Basis-Klasse aller Manager.
 *
 * @author Marcel Carlé
 */
public class BaseManager {

    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("de.tvcrowd_PU");

    public final static BaseManager instance = new BaseManager();

    private BaseManager() {
    }

    public static BaseManager get() {
        return instance;
    }

    public List<Tag> listTagsByMovie(int movieId) {
        EntityManager em = null;
        try {
            em = entityManagerFactory.createEntityManager();

            return em.createQuery("Select t from Tag t where t.movie.id = :movieId", Tag.class).setParameter("movieId", movieId).getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tag> listTagsByMovie(int movieId, int secondsStart, int secondsEnd) {
        EntityManager em = null;
        try {
            em = entityManagerFactory.createEntityManager();

            return em.createQuery("Select t from Tag t where t.movie.id = :movieId and t.seconds >= :secondsStart and t.seconds <= :secondsEnd order by t.seconds", Tag.class).setParameter("movieId", movieId).setParameter("secondsStart", secondsStart).setParameter("secondsEnd", secondsEnd).getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TagStormDto> listTagStroms(int movieId, int period) {
        EntityManager em = null;
        try {
            em = entityManagerFactory.createEntityManager();

            return em.createQuery("select new de.tvcrowd.server.rest.dto.TagStormDto(t.seconds / :period) from Tag t where t.movie.id = :movieId order by t.seconds", TagStormDto.class).setParameter("movieId", movieId).setParameter("period", period).getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tag> listTagsByUser(String username) {
        EntityManager em = null;
        try {
            em = entityManagerFactory.createEntityManager();

            return em.createQuery("Select t from Tag t where t.user.username = :username", Tag.class).setParameter("username", username).getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Movie> listMovies() {
        EntityManager em = null;
        try {
            em = entityManagerFactory.createEntityManager();

            return em.createQuery("Select m from Movie m", Movie.class).getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public <T> T save(T t) {
        EntityManager em = null;
        try {
            em = entityManagerFactory.createEntityManager();

            EntityTransaction tx = null;
            try {
                tx = em.getTransaction();
                tx.begin();

                return em.merge(t);
            } finally {
                if (tx != null && tx.isActive()) {
                    tx.commit();
                }
            }

        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public <T> T getReference(Class<T> entityClass, Object id) {
        EntityManager em = null;
        try {
            em = entityManagerFactory.createEntityManager();

            return em.getReference(entityClass, id);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public <T> T find(Class<T> entityClass, Object id) {
        EntityManager em = null;
        try {
            em = entityManagerFactory.createEntityManager();

            return em.find(entityClass, id);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

}
