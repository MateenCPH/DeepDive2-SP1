package dat.daos;

import dat.config.HibernateConfig;
import dat.dtos.MovieDTO;
import dat.entities.Genre;
import dat.entities.Movie;
import dat.entities.Person;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MovieDAOTest {
    private static IDAO<Movie> dao;
    private static EntityManagerFactory emf;
    Movie m1, m2;
    Person p1, p2;

    @BeforeAll
    static void beforeAll() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        dao = new MovieDAO(emf);
    }

   @BeforeEach
   void setUp() {
       try (EntityManager em = emf.createEntityManager()) {
           em.getTransaction().begin();
           em.createQuery("DELETE FROM Movie").executeUpdate();
           em.createQuery("DELETE FROM Person").executeUpdate();
           em.createQuery("DELETE FROM Genre").executeUpdate();

           Genre genre = new Genre();
           genre.setGenreName("Action");
           em.persist(genre);

           m1 = Movie.builder()
                   .title("Free Fall")
                   .originalTitle("Frit Fald")
                   .adult(false)
                   .originalLanguage("da")
                   .popularity(1.393)
                   .releaseDate(LocalDate.of(2024, 8, 26))
                   .video(false)
                   .voteAverage(0.0)
                   .genres(List.of(genre))
                   .cast(null)
                   .build();

           m2 = Movie.builder()
                   .title("Amnesia")
                   .originalTitle("Amnesia")
                   .adult(false)
                   .originalLanguage("da")
                   .popularity(5.529)
                   .releaseDate(LocalDate.of(2024, 9, 13))
                   .video(false)
                   .voteAverage(9.0)
                   .genres(List.of(genre))
                   .cast(null)
                   .build();

           p1 = Person.builder()
                   .name("Lasse Jørgensen")
                   .role("Acting")
                   .gender(2)
                   .build();

           p2 = Person.builder()
                   .name("Sebastian Poulsen")
                   .role("Acting")
                   .gender(2)
                   .build();

           em.persist(m1);
           em.persist(m2);
           em.persist(p1);
           em.persist(p2);
           em.getTransaction().commit();
       }

   }

    @AfterAll
    static void tearDown(){

    }

    @Test
    @DisplayName("Test create Movie")
    void create() {
        // Create a Genre object
        Genre genre = new Genre();
        genre.setGenreName("Action");

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(genre);
            em.getTransaction().commit();
        }

        // Build a movie object without setting the ID manually (let Hibernate assign it)
        Movie m = Movie.builder()
                .title("Free Fall 2")
                .originalTitle("Frit Fald 2")
                .adult(false)
                .originalLanguage("da")
                .popularity(1.393)
                .releaseDate(LocalDate.of(2024, 8, 26))
                .video(false)
                .voteAverage(0.0)
                .genres(List.of(genre))
                .cast(null)
                .build();

        // Ensure dao.create returns a Movie object (no need for casting if dao is typed)
        Movie created = (Movie) dao.create(m);

        // Assert the movie was created successfully
        Assertions.assertNotNull(created, "The movie should be created successfully.");
        Assertions.assertNotNull(created.getId(), "The movie should have an ID after creation.");
    }

    @Test
    @DisplayName("Test get Movie by ID")
    void getById() {
        Long expected = m1.getId();
        Movie movie = dao.getById(m1.getId());

        Long actual = movie.getId();

        assertEquals(expected, actual);
    }


    @Test
    @DisplayName("Test delete Movie by ID")
    void delete() {
        int numberOf = dao.getAll().size();

        dao.delete(m2);

        int expected = numberOf - 1;
        int actual = dao.getAll().size();

        assertEquals(expected,actual);
    }


    @Test
    @DisplayName("Test Update Movie variables")
    void update() {
        Movie newMovie = Movie.builder()
                .title("Deep Dive")
                .build();
        m2.setTitle(newMovie.getTitle());
        dao.update(m2);
        String expected = "Deep Dive";
        String actual = dao.getById(m2.getId()).getTitle();
        assertEquals(expected, actual);
    }


    @Test
    @DisplayName("Test getting all Movies")
    void getAllEmployees() {
        int expected = 2;
        int actual = dao.getAll().size();

        assertEquals(expected, actual);
    }
}