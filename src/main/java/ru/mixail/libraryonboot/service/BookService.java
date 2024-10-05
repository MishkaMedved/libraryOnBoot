package ru.mixail.libraryonboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mixail.libraryonboot.models.Book;
import ru.mixail.libraryonboot.models.Person;
import ru.mixail.libraryonboot.repositories.BookRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // Аналог метода index() — получение всех книг
    public List<Book> findAll(boolean sortByeYear) {
        if (sortByeYear){
            return bookRepository.findAll(Sort.by("year"));
        } else {
            return bookRepository.findAll();
        }
    }

    public List<Book> findWithPagination(Integer page, Integer booksPerPage ,boolean sortByeYear) {
        if (sortByeYear){
            return bookRepository.findAll(PageRequest.of(page,booksPerPage,Sort.by("year"))).getContent();
        } else {
            return bookRepository.findAll(PageRequest.of(page,booksPerPage)).getContent();
        }

    }

    public Book findOne(int id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.orElse(null);
    }

    public List<Book> searchByTitle(String query) {
        return bookRepository.findByTitleStartingWith(query);
    }

    // Аналог метода save() — сохранение книги
    @Transactional
    public void save(Book book) {
        bookRepository.save(book);
    }

    // Аналог метода update(int id, Book updatedBook) — обновление книги
    @Transactional
    public void update(int id, Book updatedBook) {
        Book bookToBeUpdated = bookRepository.findById(id).get();

        updatedBook.setId(id);
        updatedBook.setOwner(bookToBeUpdated.getOwner());
        bookRepository.save(updatedBook);
    }

    @Transactional
    public void delete(int id) {
        bookRepository.deleteById(id);
    }

    public Person getBookOwner(int id) {
        return bookRepository.findById(id).map(Book::getOwner).orElse(null);
    }

    @Transactional
    public void release(int bookId) {
        bookRepository.findById(bookId).ifPresent(
                book -> {
                    book.setOwner(null);
                    book.setTakeAt(null);
                });
    }

    @Transactional
    public void assign(int bookId, Person selectedPerson) {
        bookRepository.findById(bookId).ifPresent(
                book -> {
            book.setOwner(selectedPerson);
            book.setTakeAt(new Date());
        });
    }
}
