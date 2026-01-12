package ma.abla.book_service.services;

import jakarta.transaction.Transactional;
import ma.abla.book_service.domain.Book;
import ma.abla.book_service.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository repository;
    private final PricingClient pricingClient;

    public BookService(BookRepository repository, PricingClient pricingClient) {
        this.repository = repository;
        this.pricingClient = pricingClient;
    }

    public List<Book> getAllBooks() {
        return repository.findAll();
    }

    public Book addBook(Book newBook) {
        String bookTitle = newBook.getTitle();
        Optional<Book> existingBookOptional = repository.findByTitle(bookTitle);
        
        if (existingBookOptional.isPresent()) {
            throw new IllegalArgumentException("Titre déjà existant");
        }
        
        return repository.save(newBook);
    }

    @Transactional
    public BorrowResult processBorrow(long bookId) {
        Optional<Book> bookOptional = repository.findByIdForUpdate(bookId);
        Book selectedBook = bookOptional.orElseThrow(() -> 
            new IllegalArgumentException("Livre introuvable")
        );

        selectedBook.decrementStock();
        double bookPrice = pricingClient.getPrice(bookId);
        Long bookIdValue = selectedBook.getId();
        String bookTitle = selectedBook.getTitle();
        int remainingStock = selectedBook.getStock();

        return new BorrowResult(bookIdValue, bookTitle, remainingStock, bookPrice);
    }

    public record BorrowResult(Long id, String title, int stockLeft, double price) {
    }
}
