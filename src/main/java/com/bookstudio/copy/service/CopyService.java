package com.bookstudio.copy.service;

import com.bookstudio.copy.dto.CopyResponseDto;
import com.bookstudio.copy.dto.CreateCopyDto;
import com.bookstudio.copy.dto.UpdateCopyDto;
import com.bookstudio.copy.model.Copy;
import com.bookstudio.copy.projection.CopyInfoProjection;
import com.bookstudio.copy.projection.CopyListProjection;
import com.bookstudio.copy.projection.CopySelectProjection;
import com.bookstudio.book.service.BookService;
import com.bookstudio.copy.repository.CopyRepository;
import com.bookstudio.shared.service.ShelfService;
import com.bookstudio.shared.util.SelectOptions;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CopyService {

    private final CopyRepository copyRepository;

    private final BookService bookService;
    private final ShelfService shelfService;

    public List<CopyListProjection> getList() {
        return copyRepository.findList();
    }

    public Optional<Copy> findById(Long copyId) {
        return copyRepository.findById(copyId);
    }

    public Optional<CopyInfoProjection> getInfoById(Long copyId) {
        return copyRepository.findInfoById(copyId);
    }

    @Transactional
    public CopyResponseDto create(CreateCopyDto dto) {
        Copy copy = new Copy();
        copy.setBarcode(dto.getBarcode());
        copy.setIsAvailable(dto.getIsAvailable());
        copy.setCondition(dto.getCondition());

        copy.setBook(bookService.findById(dto.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found")));
        copy.setShelf(shelfService.findById(dto.getShelfId())
                .orElseThrow(() -> new RuntimeException("Shelf not found")));

        Copy saved = copyRepository.save(copy);

        return new CopyResponseDto(
                saved.getCopyId(),
                saved.getCode(),
                saved.getBook().getTitle(),
                saved.getShelf().getLocation().getName(),
                saved.getIsAvailable(),
                saved.getCondition().name());
    }

    @Transactional
    public CopyResponseDto update(UpdateCopyDto dto) {
        Copy copy = copyRepository.findById(dto.getCopyId())
                .orElseThrow(() -> new RuntimeException("Ejemplar no encontrado con ID: " + dto.getCopyId()));

        copy.setBarcode(dto.getBarcode());
        copy.setIsAvailable(dto.getIsAvailable());
        copy.setCondition(dto.getCondition());

        copy.setShelf(shelfService.findById(dto.getShelfId())
                .orElseThrow(() -> new RuntimeException("Shelf not found")));

        Copy saved = copyRepository.save(copy);

        return new CopyResponseDto(
                saved.getCopyId(),
                saved.getCode(),
                saved.getBook().getTitle(),
                saved.getShelf().getLocation().getName(),
                saved.getIsAvailable(),
                saved.getCondition().name());
    }

    public List<CopySelectProjection> getForSelect() {
        return copyRepository.findForSelect();
    }

    public SelectOptions getSelectOptions() {
        return SelectOptions.builder()
                .books(bookService.getForSelect())
                .shelves(shelfService.getForSelect())
                .build();
    }
}
