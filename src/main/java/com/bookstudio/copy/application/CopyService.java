package com.bookstudio.copy.application;

import com.bookstudio.book.application.BookService;
import com.bookstudio.copy.infrastructure.repository.CopyRepository;
import com.bookstudio.location.application.ShelfService;
import com.bookstudio.copy.application.dto.request.CreateCopyRequest;
import com.bookstudio.copy.application.dto.request.UpdateCopyRequest;
import com.bookstudio.copy.application.dto.response.CopyDetailResponse;
import com.bookstudio.copy.application.dto.response.CopyListResponse;
import com.bookstudio.copy.domain.model.Copy;
import com.bookstudio.shared.application.dto.response.OptionResponse;
import com.bookstudio.shared.util.SelectOptions;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CopyService {
    @PersistenceContext
    private EntityManager entityManager;

    private final CopyRepository copyRepository;

    private final BookService bookService;
    private final ShelfService shelfService;

    public List<CopyListResponse> getList() {
        return copyRepository.findList();
    }

    public List<OptionResponse> getOptions() {
        return copyRepository.findForOptions();
    }

    public SelectOptions getSelectOptions() {
        return SelectOptions.builder()
                .books(bookService.getOptions())
                .shelves(shelfService.getOptions())
                .build();
    }

    public Optional<Copy> findById(Long id) {
        return copyRepository.findById(id);
    }

    public CopyDetailResponse getDetailById(Long id) {
        return copyRepository.findDetailById(id)
                .orElseThrow(() -> new RuntimeException("Copy not found with ID: " + id));
    }

    @Transactional
    public CopyListResponse create(CreateCopyRequest request) {
        Copy copy = new Copy();
        copy.setBook(bookService.findById(request.bookId())
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ID: " + request.bookId())));
        copy.setShelf(shelfService.findById(request.shelfId())
                .orElseThrow(() -> new EntityNotFoundException("Shelf not found with ID: " + request.shelfId())));

        copy.setBarcode(request.barcode());
        copy.setStatus(request.status());
        copy.setCondition(request.condition());

        Copy saved = copyRepository.save(copy);
        entityManager.refresh(saved);

        return toListResponse(saved);
    }

    @Transactional
    public CopyListResponse update(Long id, UpdateCopyRequest request) {
        Copy copy = copyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Copy not found with ID: " + id));

        copy.setShelf(shelfService.findById(request.shelfId())
                .orElseThrow(() -> new EntityNotFoundException("Shelf not found with ID: " + request.shelfId())));

        copy.setBarcode(request.barcode());
        copy.setStatus(request.status());
        copy.setCondition(request.condition());

        Copy updated = copyRepository.save(copy);

        return toListResponse(updated);
    }

    private CopyListResponse toListResponse(Copy copy) {
        return new CopyListResponse(
                copy.getId(),
                copy.getCode(),

                copy.getBook().getId(),
                copy.getBook().getCoverUrl(),
                copy.getBook().getTitle(),

                copy.getShelf().getCode(),
                copy.getShelf().getFloor(),

                copy.getShelf().getLocation().getName(),

                copy.getStatus(),
                copy.getCondition());
    }
}
