package com.bookstudio.book.service;

import java.time.LocalDate;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.bookstudio.author.dao.AuthorDao;
import com.bookstudio.author.dao.AuthorDaoImpl;
import com.bookstudio.book.dao.BookDao;
import com.bookstudio.book.dao.BookDaoImpl;
import com.bookstudio.book.model.Book;
import com.bookstudio.course.dao.CourseDao;
import com.bookstudio.course.dao.CourseDaoImpl;
import com.bookstudio.publisher.dao.PublisherDao;
import com.bookstudio.publisher.dao.PublisherDaoImpl;
import com.bookstudio.shared.dao.GenreDao;
import com.bookstudio.shared.dao.GenreDaoImpl;
import com.bookstudio.shared.util.SelectOptions;

public class BookService {
	private BookDao bookDao = new BookDaoImpl();
	private AuthorDao authorDao = new AuthorDaoImpl();
	private PublisherDao publisherDao = new PublisherDaoImpl();
	private CourseDao courseDao = new CourseDaoImpl();
	private GenreDao genreDao = new GenreDaoImpl();

	public List<Book> listBooks() throws Exception {
		return bookDao.listAll();
	}

	public Book getBook(String bookId) throws Exception {
		return bookDao.getById(bookId);
	}

	public Book createBook(HttpServletRequest request) throws Exception {
		String title = request.getParameter("addBookTitle");
		int totalCopies = Integer.parseInt(request.getParameter("addBookTotalCopies"));
		String authorId = request.getParameter("addBookAuthor");
		String publisherId = request.getParameter("addBookPublisher");
		String courseId = request.getParameter("addBookCourse");
		LocalDate releaseDate = LocalDate.parse(request.getParameter("addReleaseDate"));
		String genreId = request.getParameter("addBookGenre");
		String status = request.getParameter("addBookStatus");

		Book book = new Book();
		book.setTitle(title);
		book.setTotalCopies(totalCopies);
		book.setAuthorId(authorId);
		book.setPublisherId(publisherId);
		book.setCourseId(courseId);
		book.setReleaseDate(releaseDate);
		book.setGenreId(genreId);
		book.setStatus(status);

		return bookDao.create(book);
	}

	public Book updateBook(String bookId, HttpServletRequest request) throws Exception {
		String title = request.getParameter("editBookTitle");
		int totalCopies = Integer.parseInt(request.getParameter("editBookTotalCopies"));
		String authorId = request.getParameter("editBookAuthor");
		String publisherId = request.getParameter("editBookPublisher");
		String courseId = request.getParameter("editBookCourse");
		LocalDate releaseDate = LocalDate.parse(request.getParameter("editReleaseDate"));
		String genreId = request.getParameter("editBookGenre");
		String status = request.getParameter("editBookStatus");

		Book book = new Book();
		book.setBookId(bookId);
		book.setTitle(title);
		book.setTotalCopies(totalCopies);
		book.setAuthorId(authorId);
		book.setPublisherId(publisherId);
		book.setCourseId(courseId);
		book.setReleaseDate(releaseDate);
		book.setGenreId(genreId);
		book.setStatus(status);

		return bookDao.update(book);
	}

	public SelectOptions populateSelects() throws Exception {
		SelectOptions selectOptions = new SelectOptions();

		selectOptions.setAuthors(authorDao.populateAuthorSelect());

		selectOptions.setPublishers(publisherDao.populatePublisherSelect());

		selectOptions.setCourses(courseDao.populateCourseSelect());

		selectOptions.setGenres(genreDao.populateGenreSelect());

		return selectOptions;
	}
}
