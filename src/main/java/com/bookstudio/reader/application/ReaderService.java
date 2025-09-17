package com.bookstudio.reader.application;

import com.bookstudio.reader.domain.dto.request.CreateReaderRequest;
import com.bookstudio.reader.domain.dto.request.UpdateReaderRequest;
import com.bookstudio.reader.domain.dto.response.ReaderDetailResponse;
import com.bookstudio.reader.domain.dto.response.ReaderListResponse;
import com.bookstudio.reader.domain.model.Reader;
import com.bookstudio.reader.infrastructure.repository.ReaderRepository;
import com.bookstudio.shared.domain.dto.response.OptionResponse;

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
public class ReaderService {

    @PersistenceContext
    private EntityManager entityManager;

    private final ReaderRepository readerRepository;

    public List<ReaderListResponse> getList() {
        return readerRepository.findList();
    }

    public List<OptionResponse> getOptions() {
        return readerRepository.findForOptions();
    }

    public Optional<Reader> findById(Long id) {
        return readerRepository.findById(id);
    }

    public ReaderDetailResponse getDetailById(Long id) {
        return readerRepository.findDetailById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reader not found with ID: " + id));
    }

    @Transactional
    public ReaderListResponse create(CreateReaderRequest request) {
        if (readerRepository.findByDni(request.getDni()).isPresent()) {
            throw new IllegalArgumentException("The provided DNI is already registered.");
        }

        if (readerRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("The provided email address is already registered.");
        }

        Reader reader = Reader.builder()
                .dni(request.getDni())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .address(request.getAddress())
                .phone(request.getPhone())
                .email(request.getEmail())
                .birthDate(request.getBirthDate())
                .gender(request.getGender())
                .type(request.getType())
                .status(request.getStatus())
                .build();

        Reader saved = readerRepository.save(reader);
        entityManager.refresh(saved);

        return toListResponse(saved);
    }

    @Transactional
    public ReaderListResponse update(Long id, UpdateReaderRequest request) {
        Reader reader = readerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reader not found with ID: " + id));

        if (readerRepository.findByEmailAndIdNot(request.getEmail(), id).isPresent()) {
            throw new IllegalArgumentException("The provided email address is already registered.");
        }

        reader.setFirstName(request.getFirstName());
        reader.setLastName(request.getLastName());
        reader.setAddress(request.getAddress());
        reader.setPhone(request.getPhone());
        reader.setEmail(request.getEmail());
        reader.setBirthDate(request.getBirthDate());
        reader.setGender(request.getGender());
        reader.setType(request.getType());
        reader.setStatus(request.getStatus());

        Reader updated = readerRepository.save(reader);
        return toListResponse(updated);
    }

    private ReaderListResponse toListResponse(Reader reader) {
        return new ReaderListResponse(
                reader.getId(),
                reader.getCode(),
                reader.getFullName(),
                reader.getPhone(),
                reader.getEmail(),
                reader.getType(),
                reader.getStatus());
    }
}
