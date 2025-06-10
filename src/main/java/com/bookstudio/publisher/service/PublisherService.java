package com.bookstudio.publisher.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import com.bookstudio.publisher.dao.PublisherDao;
import com.bookstudio.publisher.dao.PublisherDaoImpl;
import com.bookstudio.publisher.model.Publisher;
import com.bookstudio.shared.dao.LiteraryGenreDao;
import com.bookstudio.shared.dao.LiteraryGenreDaoImpl;
import com.bookstudio.shared.dao.NationalityDao;
import com.bookstudio.shared.dao.NationalityDaoImpl;
import com.bookstudio.shared.util.SelectOptions;

public class PublisherService {
	private PublisherDao publisherDao = new PublisherDaoImpl();
	private NationalityDao nationalityDao = new NationalityDaoImpl();
	private LiteraryGenreDao literaryGenreDao = new LiteraryGenreDaoImpl();

	public List<Publisher> listPublishers() throws Exception {
		List<Publisher> publisherData = publisherDao.listAll();
		
		for (Publisher publisher : publisherData) {
			encodePublisherPhoto(publisher);
		}
		
		return publisherData;
	}

	public Publisher getPublisher(String publisherId) throws Exception {
		Publisher publisher = publisherDao.getById(publisherId);
		encodePublisherPhoto(publisher);
		
		return publisher;
	}

	public Publisher createPublisher(HttpServletRequest request) throws Exception {
		String name = getUtf8Parameter(request, "addPublisherName");
		String nationalityId = getUtf8Parameter(request, "addPublisherNationality");
		String literaryGenreId = getUtf8Parameter(request, "addLiteraryGenre");
		int foundationYear = Integer.parseInt(getUtf8Parameter(request, "addFoundationYear"));
		String website = getUtf8Parameter(request, "addPublisherWebsite");
		String address = getUtf8Parameter(request, "addPublisherAddress");
		String status = getUtf8Parameter(request, "addPublisherStatus");

		byte[] photo = readPhoto(request, "addPublisherPhoto");

		Publisher publisher = new Publisher();
		publisher.setName(name);
		publisher.setNationalityId(nationalityId);
		publisher.setLiteraryGenreId(literaryGenreId);
		publisher.setFoundationYear(foundationYear);
		publisher.setWebsite(website);
		publisher.setAddress(address);
		publisher.setStatus(status);
		publisher.setPhoto(photo);

		Publisher createdPublisher = publisherDao.create(publisher);
		encodePublisherPhoto(createdPublisher);
		
		return createdPublisher;
	}

	public Publisher updatePublisher(String publisherId, HttpServletRequest request) throws Exception {
		String name = getUtf8Parameter(request, "editPublisherName");
		String nationalityId = getUtf8Parameter(request, "editPublisherNationality");
		String literaryGenreId = getUtf8Parameter(request, "editLiteraryGenre");
		int foundationYear = Integer.parseInt(getUtf8Parameter(request, "editFoundationYear"));
		String website = getUtf8Parameter(request, "editPublisherWebsite");
		String address = getUtf8Parameter(request, "editPublisherAddress");
		String status = getUtf8Parameter(request, "editPublisherStatus");
		String deletePhoto = request.getParameter("deletePhoto");

		byte[] photo = null;
		if ("true".equals(deletePhoto)) {
			photo = null;
		} else {
			photo = readPhoto(request, "editPublisherPhoto");
			if (photo == null) {
				Publisher currentPublisher = publisherDao.getById(publisherId);
				photo = currentPublisher.getPhoto();
			}
		}

		Publisher publisher = new Publisher();
		publisher.setPublisherId(publisherId);
		publisher.setName(name);
		publisher.setNationalityId(nationalityId);
		publisher.setLiteraryGenreId(literaryGenreId);
		publisher.setFoundationYear(foundationYear);
		publisher.setWebsite(website);
		publisher.setAddress(address);
		publisher.setStatus(status);
		publisher.setPhoto(photo);

		return publisherDao.update(publisher);
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

	private void encodePublisherPhoto(Publisher publisher) {
		byte[] photo = publisher.getPhoto();
		if (photo != null) {
			String photoBase64 = Base64.getEncoder().encodeToString(photo);
			publisher.setPhotoBase64("data:image/jpeg;base64," + photoBase64);
		}
	}
}
