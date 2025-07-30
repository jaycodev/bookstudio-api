package com.bookstudio.reader.service;

import com.bookstudio.reader.dto.CreateReaderDto;
import com.bookstudio.reader.dto.ReaderResponseDto;
import com.bookstudio.reader.dto.UpdateReaderDto;
import com.bookstudio.reader.model.Reader;
import com.bookstudio.reader.projection.ReaderInfoProjection;
import com.bookstudio.reader.projection.ReaderListProjection;
import com.bookstudio.reader.projection.ReaderSelectProjection;
import com.bookstudio.reader.repository.ReaderRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReaderService {

    private final ReaderRepository readerRepository;

    public List<ReaderListProjection> getList() {
        return readerRepository.findList();
    }

    public Optional<Reader> findById(Long readerId) {
        return readerRepository.findById(readerId);
    }

    public Optional<ReaderInfoProjection> getInfoById(Long readerId) {
        return readerRepository.findInfoById(readerId);
    }

    @Transactional
    public ReaderResponseDto create(CreateReaderDto dto) {
        if (readerRepository.findByDni(dto.getDni()).isPresent()) {
            throw new RuntimeException("El DNI ingresado ya ha sido registrado.");
        }

        if (readerRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("El correo electrónico ingresado ya ha sido registrado.");
        }

        Reader reader = new Reader();
        reader.setDni(dto.getDni());
        reader.setFirstName(dto.getFirstName());
        reader.setLastName(dto.getLastName());
        reader.setAddress(dto.getAddress());
        reader.setPhone(dto.getPhone());
        reader.setEmail(dto.getEmail());
        reader.setBirthDate(dto.getBirthDate());
        reader.setGender(dto.getGender());
        reader.setStatus(dto.getStatus());

        Reader saved = readerRepository.save(reader);

        return new ReaderResponseDto(
                saved.getReaderId(),
                saved.getCode(),
                saved.getDni(),
                saved.getFirstName(),
                saved.getLastName(),
                saved.getPhone(),
                saved.getEmail(),
                saved.getStatus().name());
    }

    @Transactional
    public ReaderResponseDto update(UpdateReaderDto dto) {
        Reader reader = readerRepository.findById(dto.getReaderId())
                .orElseThrow(() -> new RuntimeException("Lector no encontrado con ID: " + dto.getReaderId()));

        if (readerRepository.findByEmailAndIdNot(dto.getEmail(), dto.getReaderId()).isPresent()) {
            throw new RuntimeException("El correo electrónico ingresado ya ha sido registrado.");
        }

        reader.setFirstName(dto.getFirstName());
        reader.setLastName(dto.getLastName());
        reader.setAddress(dto.getAddress());
        reader.setPhone(dto.getPhone());
        reader.setEmail(dto.getEmail());
        reader.setBirthDate(dto.getBirthDate());
        reader.setGender(dto.getGender());
        reader.setStatus(dto.getStatus());

        Reader saved = readerRepository.save(reader);

        return new ReaderResponseDto(
                saved.getReaderId(),
                saved.getCode(),
                saved.getDni(),
                saved.getFirstName(),
                saved.getLastName(),
                saved.getPhone(),
                saved.getEmail(),
                saved.getStatus().name());
    }

    public List<ReaderSelectProjection> getForSelect() {
        return readerRepository.findForSelect();
    }
}
