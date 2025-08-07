package com.bookstudio.copy.service;

import com.bookstudio.book.service.BookService;
import com.bookstudio.copy.dto.CopyDetailDto;
import com.bookstudio.copy.dto.CopyListDto;
import com.bookstudio.copy.dto.CopySelectDto;
import com.bookstudio.copy.dto.CopySummaryDto;
import com.bookstudio.copy.dto.CreateCopyDto;
import com.bookstudio.copy.dto.UpdateCopyDto;
import com.bookstudio.copy.model.Copy;
import com.bookstudio.copy.repository.CopyRepository;
import com.bookstudio.location.service.ShelfService;
import com.bookstudio.shared.util.SelectOptions;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CopyService {

    @PersistenceContext
    private EntityManager entityManager;

    private final CopyRepository copyRepository;

    private final BookService bookService;
    private final ShelfService shelfService;

    public List<CopyListDto> getList() {
        return copyRepository.findList();
    }

    public Optional<Copy> findById(Long copyId) {
        return copyRepository.findById(copyId);
    }

    public CopyDetailDto getInfoById(Long copyId) {
        Copy copy = copyRepository.findById(copyId)
                .orElseThrow(() -> new EntityNotFoundException("Copy not found with ID: " + copyId));

        return CopyDetailDto.builder()
                .id(copy.getCopyId())
                .code(copy.getCode())
                .book(bookService.toSummaryDto(copy.getBook()))
                .shelf(shelfService.toSummaryDto(copy.getShelf()))
                .barcode(copy.getBarcode())
                .isAvailable(copy.getIsAvailable())
                .condition(copy.getCondition().name())
                .build();
    }

    @Transactional
    public CopyListDto create(CreateCopyDto dto) {
        Copy copy = new Copy();
        copy.setBarcode(dto.getBarcode());
        copy.setIsAvailable(dto.getIsAvailable());
        copy.setCondition(dto.getCondition());

        copy.setBook(bookService.findById(dto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Book not found")));
        copy.setShelf(shelfService.findById(dto.getShelfId())
                .orElseThrow(() -> new EntityNotFoundException("Shelf not found")));

        Copy saved = copyRepository.save(copy);
        entityManager.refresh(saved);

        return toListDto(saved);
    }

    @Transactional
    public CopyListDto update(UpdateCopyDto dto) {
        Copy copy = copyRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Copy not found with ID: " + dto.getId()));

        copy.setBarcode(dto.getBarcode());
        copy.setIsAvailable(dto.getIsAvailable());
        copy.setCondition(dto.getCondition());

        copy.setShelf(shelfService.findById(dto.getShelfId())
                .orElseThrow(() -> new EntityNotFoundException("Shelf not found")));

        Copy saved = copyRepository.save(copy);
        return toListDto(saved);
    }

    public List<CopySelectDto> getForSelect() {
        return copyRepository.findForSelect();
    }

    public SelectOptions getSelectOptions() {
        return SelectOptions.builder()
                .books(bookService.getForSelect())
                .shelves(shelfService.getForSelect())
                .build();
    }

    public CopySummaryDto toSummaryDto(Copy copy) {
        return CopySummaryDto.builder()
                .id(copy.getCopyId())
                .code(copy.getCode())
                .barcode(copy.getBarcode())
                .isAvailable(copy.getIsAvailable())
                .build();
    }

    private CopyListDto toListDto(Copy copy) {
        return new CopyListDto(
                copy.getCode(),
                copy.getBook().getTitle(),
                copy.getShelf().getCode(),
                copy.getShelf().getFloor(),
                copy.getShelf().getLocation().getName(),
                copy.getIsAvailable(),
                copy.getCondition(),
                copy.getCopyId());
    }
}
