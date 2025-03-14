package com.bookstudio.services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import com.bookstudio.dao.LiteraryGenreDao;
import com.bookstudio.dao.NationalityDao;
import com.bookstudio.dao.PublisherDao;
import com.bookstudio.dao.impl.LiteraryGenreDaoImpl;
import com.bookstudio.dao.impl.NationalityDaoImpl;
import com.bookstudio.dao.impl.PublisherDaoImpl;
import com.bookstudio.models.Publisher;
import com.bookstudio.utils.SelectOptions;

public class PublisherService {
	private PublisherDao publisherDao = new PublisherDaoImpl();
	private NationalityDao nationalityDao = new NationalityDaoImpl();
	private LiteraryGenreDao literaryGenreDao = new LiteraryGenreDaoImpl();

	public List<Publisher> listPublishers() throws Exception {
		List<Publisher> publisherData = publisherDao.listPublishers();
		
		for (Publisher publisher : publisherData) {
			encodePublisherPhoto(publisher);
		}
		
		return publisherData;
	}

	public Publisher getPublisher(String publisherId) throws Exception {
		Publisher publisher = publisherDao.getPublisher(publisherId);
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

		Publisher createdPublisher = publisherDao.createPublisher(publisher);
		encodePublisherPhoto(createdPublisher);
		
		return createdPublisher;
	}

	public Publisher updatePublisher(HttpServletRequest request) throws Exception {
		String publisherId = getUtf8Parameter(request, "publisherId");
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
				Publisher currentPublisher = publisherDao.getPublisher(publisherId);
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

		return publisherDao.updatePublisher(publisher);
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
