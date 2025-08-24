package com.bookstudio.reader.service;

import com.bookstudio.reader.dto.*;
import com.bookstudio.reader.model.Reader;
import com.bookstudio.reader.repository.ReaderRepository;

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
public class ReaderService {

    @PersistenceContext
    private EntityManager entityManager;

    private final ReaderRepository readerRepository;

    public List<ReaderListDto> getList() {
        return readerRepository.findList();
    }

    public Optional<Reader> findById(Long readerId) {
        return readerRepository.findById(readerId);
    }

    public ReaderDetailDto getInfoById(Long readerId) {
        Reader reader = readerRepository.findById(readerId)
                .orElseThrow(() -> new EntityNotFoundException("Reader not found with ID: " + readerId));

        return ReaderDetailDto.builder()
                .id(reader.getReaderId())
                .code(reader.getCode())
                .dni(reader.getDni())
                .firstName(reader.getFirstName())
                .lastName(reader.getLastName())
                .address(reader.getAddress())
                .phone(reader.getPhone())
                .email(reader.getEmail())
                .birthDate(reader.getBirthDate())
                .gender(reader.getGender().name())
                .type(reader.getType().name())
                .status(reader.getStatus().name())
                .build();
    }

    @Transactional
    public ReaderListDto create(CreateReaderDto dto) {
        if (readerRepository.findByDni(dto.getDni()).isPresent()) {
            throw new IllegalArgumentException("The provided DNI is already registered.");
        }

        if (readerRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("The provided email address is already registered.");
        }

        Reader reader = Reader.builder()
                .dni(dto.getDni())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .address(dto.getAddress())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .birthDate(dto.getBirthDate())
                .gender(dto.getGender())
                .type(dto.getType())
                .status(dto.getStatus())
                .build();

        Reader saved = readerRepository.save(reader);
        entityManager.refresh(saved);

        return toListDto(saved);
    }

    @Transactional
    public ReaderListDto update(Long readerId, UpdateReaderDto dto) {
        Reader reader = readerRepository.findById(readerId)
                .orElseThrow(() -> new EntityNotFoundException("Reader not found with ID: " + readerId));

        if (readerRepository.findByEmailAndReaderIdNot(dto.getEmail(), readerId).isPresent()) {
            throw new IllegalArgumentException("The provided email address is already registered.");
        }

        reader.setFirstName(dto.getFirstName());
        reader.setLastName(dto.getLastName());
        reader.setAddress(dto.getAddress());
        reader.setPhone(dto.getPhone());
        reader.setEmail(dto.getEmail());
        reader.setBirthDate(dto.getBirthDate());
        reader.setGender(dto.getGender());
        reader.setType(dto.getType());
        reader.setStatus(dto.getStatus());

        Reader saved = readerRepository.save(reader);
        return toListDto(saved);
    }

    public List<ReaderSelectDto> getForSelect() {
        return readerRepository.findForSelect();
    }

    public ReaderSummaryDto toSummaryDto(Reader reader) {
        return ReaderSummaryDto.builder()
                .id(reader.getReaderId())
                .code(reader.getCode())
                .firstName(reader.getFirstName())
                .lastName(reader.getLastName())
                .build();
    }

    private ReaderListDto toListDto(Reader reader) {
        return new ReaderListDto(
                reader.getCode(),
                reader.getFullName(),
                reader.getDni(),
                reader.getPhone(),
                reader.getEmail(),
                reader.getType(),
                reader.getStatus(),
                reader.getReaderId());
    }
}
