package bookshop;

import bookshop.models.Author;
import bookshop.models.Book;
import bookshop.repositories.AuthorRepository;
import bookshop.repositories.BookRepository;
import bookshop.services.SeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Component
public class ConsoleRunner implements CommandLineRunner {


    private final SeedService seedService;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public ConsoleRunner(
            SeedService seedService,
            BookRepository bookRepository,
            AuthorRepository authorRepository) {
        this.seedService = seedService;
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    private void _01_booksAfter2000() {
        LocalDate year2000 = LocalDate.of(2000, 12, 31);

        List<Book> books = this.bookRepository.findByReleaseDateAfter(year2000);

        books.forEach(b -> System.out.println(b.getTitle()));

        int count = this.bookRepository.countByReleaseDateAfter(year2000);

        System.out.println("Total count: " + count);
    }

    private void _02_allAuthorsWithBookBefore1990() {
        LocalDate year1990 = LocalDate.of(1990, 1, 1);

        List<Author> authors = this.authorRepository.findDistinctByBooksReleaseDateBefore(year1990);

        authors.forEach(a -> System.out.println(a.getFirstName() + " " + a.getLastName()));
    }

    private void _03_allAuthorsOrderedByBookCount() {
        List<Author> authors = this.authorRepository.findAll();

        authors
                .stream()
                .sorted((l, r) -> r.getBooks().size() - l.getBooks().size())
                .forEach(author ->
                        System.out.printf("%s %s -> %d%n",
                                author.getFirstName(),
                                author.getLastName(),
                                author.getBooks().size()));
    }

    private void _03_allBooksByAuthorGeorgePowell() {
        List<Book> books = this.bookRepository.findByAuthorLastName(
                "Powell");
        books.stream()
                .sorted(Comparator.comparing(Book::getReleaseDate).reversed().thenComparing(Book::getTitle))
                .forEach(e -> System.out.println(e.getTitle() + " " + e.getReleaseDate()));
    }

    @Override
    @Transactional
    public void run(String... strings) throws Exception {
        this._03_allBooksByAuthorGeorgePowell();
    }
}
