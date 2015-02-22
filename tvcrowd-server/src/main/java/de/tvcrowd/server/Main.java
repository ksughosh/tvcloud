package de.tvcrowd.server;

import de.tvcrowd.server.entity.Movie;
import de.tvcrowd.server.entity.TVCrowdUser;
import de.tvcrowd.server.entity.Tag;
import de.tvcrowd.server.entity.Watching;
import de.tvcrowd.server.entity.manager.BaseManager;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Random;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

/**
 * Main class.
 *
 */
public class Main {

    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://localhost:8080/tvcrowd/";
    public static final String USER_ROLE = "USER";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this
     * application.
     * <p>
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in de.tvcrowd.server package
        final ResourceConfig rc = new ResourceConfig().packages("de.tvcrowd.server.rest").register(JacksonFeature.class).register(RolesAllowedDynamicFeature.class);

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    /**
     * Main method.
     * <p>
     * @param args
     * <p>
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        generateMovies();
        generateUsers();
        generateTags();
        generateWatchings();

        final HttpServer server = startServer();
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
        System.in.read();
        server.stop();
    }

    private static String[] movieNames = new String[]{
        "Transformers: Age of Extinction",
        "Tammy",
        "22 Jump Street",
        "Deliver Us from Evil",
        "How to Train Your Dragon 2",
        "Earth to Echo",
        "Maleficent",
        "Jersey Boys",
        "Think Like a Man Too",
        "Edge of Tomorrow"
    };

    private static String[] names = new String[]{
        "user1",
        "user2",
        "Elias",
        "Alexander",
        "Daniel",
        "Luca",
        "David",
        "Michael",
        "Liam",
        "Jonas",
        "Noah",
        "Lukas", "Felix", "Marcel", "Sughosh", "Pavel", "Bharath"
    };

    private static String[] comments = new String[]{
        "I told my wife the truth. I told her I was seeing a psychiatrist. Then she told me the truth: that she was seeing a psychiatrist, two plumbers, and a bartender.",
        "Men are simple things. They can survive a whole weekend with only three things: beer, boxer shorts and batteries for the remote control.",
        "I'm always relieved when someone is delivering a eulogy and I realise I'm listening to it.",
        "I am ready to meet my Maker. Whether my Maker is prepared for the great ordeal of meeting me is another matter.",
        "Suicide would be my way of telling God that I quit.",
        "Girls have an unfair advantage over men: If they can't get what they want by being smart, they can get it by being dumb.",
        "I saw six men kicking and punching the mother-in-law. My neighbor said \"Are you going to help?\" I said, \"No, Six should be enough.\"",
        "A good lawyer knows the law; a clever one takes the judge to lunch.",
        "Thank you Facebook, I can now farm without going outside, cook without being in my kitchen, feed fish I don't have & waste an entire day without having a life.",
        "After one look at this planet any visitor from outer space would say \"I WANT TO SEE THE MANAGER.\"",
        "Some people come into our lives and leave footprints on our hearts, while others come into our lives and make us wanna leave footprints on their face.",
        "Don't you find it Funny that after Monday(M) and Tuesday(T), the rest of the week says WTF?",
        "Sorry, I can't hangout. My uncle's cousin's sister in law's best friend's insurance agent's roommate's pet goldfish died. Maybe next time.",
        "Why go to college? There's Google.",
        "Microsoft bought Skype for 8,5 billion!.. what a bunch of idiots! I downloaded it for free!",
        "The human body was designed by a civil engineer. Who else would run a toxic waste pipeline through a recreational area?",
        "I feel sorry for people who don't drink. When they wake up in the morning, that's as good as they're going to feel all day.",
        "Life is full of temporary situations, ultimately ending in a permanent solution.",
        "Don't steal, don't lie, don't cheat, don't sell drugs. The government hates competition!",
        "In my life there's been heartache and pain I don't know if I can face it again Can't stop now, I've traveled so far To change this lonely life.",
        "Oppan Gangnam Style Gangnam Style Op op op op oppan Gangnam Style Gangnam Style Op op op op oppan Gangnam Style.",
        "Don't want to close my eyes I don't want to fall asleep Cause I'd miss you babe And I don't want to miss a thing Cause even when I dream of you The sweetest dream will never do I'd still miss you babe And I don't want to miss a thing.",
        "I see trees of green........ red roses too I see em bloom..... for me and for you And I think to myself.... what a wonderful world.",
        "Please allow me to introduce myself I'm a man of wealth and taste I've been around for a long, long year Stole many a mans soul and faith And I was round when jesus christ Had his moment of doubt and pain.",
        "Loving you Isn't the right thing to do How can I Ever change things that I feel? If I could Maybe I'd give you my world How can I When you won't take it from me?",
        "It's gonna take a lot to take me away from you There's nothing that a hundred men or more could ever do I bless the rains down in Africa Gonna take some time to do the things we never have.",
        "Oh, a storm is threat'ning My very life today If I don't get some shelter Oh yeah, I'm gonna fade away.",
        "I never meant to cause you any sorrow. I never meant to cause you any pain. I only wanted to one time see you laughing. I only wanted to see you laughing in the purple rain.",
        "Now this is the story all about how My life got flipped, turned upside down And I'd like to take a minute just sit right there I'll tell you how I became the prince of a town called Bel-air.",
        "You can find me in the club, bottle full of bub Look mami I got the X if you into taking drugs I'm into having sex, I ain't into makin love So come give me a hug if you into getting rubbed.",
        "Buddy you're a young man hard man Shoutin' in the street gonna take on the world some day You got blood on yo' face You big disgrace Wavin' your banner all over the place.",
        "People always told me be careful of what you do And dont go around breaking young girls' hearts And mother always told me be careful of who you love And be careful of what you do cause the lie becomes the truth.",
        "And everything is going to the beat And everything is going to the beat And everything is going...",
        "If you really wanted to do that, then why wouldn't you do that? Instead you do this. It makes no sense.",
        "I can drive 10 miles, walk 50 feet. Turn around and before I know it, I'd be back home. Or would I? I'm not sure but that's just how it is.",
        "Sometimes I wonder if I really can. But then I think to myself, maybe I can't. But if I could, that would be good. Maybe it's all a lie?",
        "I see you have something to talk about. Well, I have something to shout about. Infact something to sing about. But I'll just keep quiet and let you carry on.",
        "If I could I would. Wether or not I should, I still would.",
        "Yo wa gwan blud you rudeboy bludclart.",
        "If I roll once and you roll twice. What does that mean?",
        "From this day on I shall be known as Bob. For Bob is a good name and I am good. But if you want you can just call me Sally.",
        "I like to wax my legs and stick the hair on my back. Why? Because it keeps my back warm. There's method in my madness.",
        "Look! In the sky. It's a bird, it's a plane. Or is it a hellicopter? No actually I think it is a bird. Or maybe I'm just seeing things. Who knows... After 10 shots of Whiskey things start to get a bit strange.",
        "I like to say things twice, say things twice. It can get annoying though, annoying though."
    };

    private static void generateMovies() {
        for (int i = 0; i < 10; i++) {
            Movie movie = new Movie();
            if (i == 1) {
                movie.setDuration(322);
            } else {
                movie.setDuration((int) (Math.random() * 6000) + 1200);
            }
            movie.setName(movieNames[i]);
            BaseManager.get().save(movie);
        }
    }

    private static void generateUsers() {
        for (int i = 0; i < names.length; i++) {
            TVCrowdUser user = new TVCrowdUser();
            user.setName(names[i]);
            user.setPassword("123456");
            user.setUsername(names[i]);
            BaseManager.get().save(user);
        }
    }

    private static void generateTags() {
        int commentIndex = 0;
        List<Movie> movies = BaseManager.get().listMovies();
        for (int i = 0; i < names.length; i++) {
            for (int j = 0; j < movies.size(); j++) {
                int duration = movies.get(j).getDuration();
                Tag tag = new Tag();
                tag.setSeconds(Math.min((int) (Math.random() * duration) + 1, duration));
                tag.setMovie(movies.get(j));
                tag.setUser(BaseManager.get().getReference(TVCrowdUser.class, names[i]));
                tag.setComment(comments[commentIndex % comments.length]);
                commentIndex++;

                BaseManager.get().save(tag);
            }
        }
        int duration = BaseManager.get().find(Movie.class, 2).getDuration();
        for (int j = 0; j < 300; j++) {
            Tag tag = new Tag();
            tag.setSeconds(Math.min((int) (Math.random() * duration) + 1, duration));
            tag.setMovie(BaseManager.get().getReference(Movie.class, 2));
            tag.setUser(BaseManager.get().getReference(TVCrowdUser.class, names[new Random().nextInt(names.length)]));
            tag.setComment(comments[new Random().nextInt(comments.length)]);

            BaseManager.get().save(tag);
        }
    }

    private static void generateWatchings() {
        List<Movie> movies = BaseManager.get().listMovies();
        for (int i = 0; i < names.length; i++) {
            TVCrowdUser user = BaseManager.get().find(TVCrowdUser.class, names[i]);
            Movie m = movies.get(new Random().nextInt(movies.size()));
            int duration = m.getDuration();
            Watching w = new Watching();
            w.setCurrentSecond(new Random().nextInt(duration) + 1);
            w.setMovie(m);
            w.setTvCrowdUser(user);

            user.setWatching(w);

            BaseManager.get().save(user);
        }
    }

}
