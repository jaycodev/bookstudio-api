package com.bookstudio.services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import com.bookstudio.dao.AuthorDao;
import com.bookstudio.dao.NationalityDao;
import com.bookstudio.dao.LiteraryGenreDao;
import com.bookstudio.dao.impl.AuthorDaoImpl;
import com.bookstudio.dao.impl.NationalityDaoImpl;
import com.bookstudio.dao.impl.LiteraryGenreDaoImpl;
import com.bookstudio.models.Author;
import com.bookstudio.utils.SelectOptions;

public class AuthorService {
	private AuthorDao authorDao = new AuthorDaoImpl();
	private NationalityDao nationalityDao = new NationalityDaoImpl();
	private LiteraryGenreDao literaryGenreDao = new LiteraryGenreDaoImpl();

	public List<Author> listAuthors() throws Exception {
		List<Author> authorData = authorDao.listAll();

		for (Author author : authorData) {
			encodeAuthorPhoto(author);
		}

		return authorData;
	}

	public Author getAuthor(String authorId) throws Exception {
		Author author = authorDao.getById(authorId);
		encodeAuthorPhoto(author);
		
		return author;
	}

	public Author createAuthor(HttpServletRequest request) throws Exception {
		String name = getUtf8Parameter(request, "addAuthorName");
		String nationalityId = getUtf8Parameter(request, "addAuthorNationality");
		String literaryGenreId = getUtf8Parameter(request, "addLiteraryGenre");
		LocalDate birthDate = LocalDate.parse(request.getParameter("addAuthorBirthDate"));
		String biography = getUtf8Parameter(request, "addAuthorBiography");
		String status = getUtf8Parameter(request, "addAuthorStatus");

		byte[] photo = readPhoto(request, "addAuthorPhoto");

		Author author = new Author();
		author.setName(name);
		author.setNationalityId(nationalityId);
		author.setLiteraryGenreId(literaryGenreId);
		author.setBirthDate(birthDate);
		author.setBiography(biography);
		author.setStatus(status);
		author.setPhoto(photo);

		Author createdAuthor = authorDao.create(author);
		encodeAuthorPhoto(createdAuthor);

		return createdAuthor;
	}

	public Author updateAuthor(HttpServletRequest request) throws Exception {
		String authorId = request.getParameter("authorId");
		String name = getUtf8Parameter(request, "editAuthorName");
		String nationalityId = getUtf8Parameter(request, "editAuthorNationality");
		String literaryGenreId = getUtf8Parameter(request, "editLiteraryGenre");
		LocalDate birthDate = LocalDate.parse(request.getParameter("editAuthorBirthDate"));
		String biography = getUtf8Parameter(request, "editAuthorBiography");
		String status = getUtf8Parameter(request, "editAuthorStatus");
		String deletePhoto = request.getParameter("deletePhoto");

		byte[] photo = null;
		if ("true".equals(deletePhoto)) {
			photo = null;
		} else {
			photo = readPhoto(request, "editAuthorPhoto");
			if (photo == null) {
				Author currentAuthor = authorDao.getById(authorId);
				photo = currentAuthor.getPhoto();
			}
		}

		Author author = new Author();
		author.setAuthorId(authorId);
		author.setName(name);
		author.setNationalityId(nationalityId);
		author.setLiteraryGenreId(literaryGenreId);
		author.setBirthDate(birthDate);
		author.setBiography(biography);
		author.setStatus(status);
		author.setPhoto(photo);

		return authorDao.update(author);
	}

	public SelectOptions populateSelects() throws Exception {
		SelectOptions selectOptions = new SelectOptions();

		selectOptions.setNationalities(nationalityDao.populateNationalitySelect());

		selectOptions.setLiteraryGenres(literaryGenreDao.populateLiteraryGenreSelect());

		return selectOptions;
	}

	private String getUtf8Parameter(HttpServletRequest request, String fieldName) throws IOException, ServletException {
		Part part = request.getPart(fieldName);
		if (part != null) {
			try (InputStream is = part.getInputStream()) {
				return new String(is.readAllBytes(), StandardCharsets.UTF_8);
			}
		}
		
		return "";
	}

	private byte[] readPhoto(HttpServletRequest request, String partName) {
		try {
			Part part = request.getPart(partName);
			if (part != null) {
				InputStream inputStream = part.getInputStream();
				if (inputStream.available() > 0) {
					return inputStream.readAllBytes();
				}
			}
		} catch (Exception e) {
			System.err.println("Error reading photo from part '" + partName + "': " + e.getMessage());
		}
		
		return null;
	}

	private void encodeAuthorPhoto(Author author) {
		byte[] photo = author.getPhoto();
		if (photo != null) {
			String photoBase64 = Base64.getEncoder().encodeToString(photo);
			author.setPhotoBase64("data:image/jpeg;base64," + photoBase64);
		}
	}
}
