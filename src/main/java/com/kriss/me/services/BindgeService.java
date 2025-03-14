package com.kriss.me.services;

import com.kriss.me.data.Bindge;
import com.kriss.me.data.BindgeRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class BindgeService {

    private final BindgeRepository repository;

    public BindgeService(BindgeRepository repository) {
        this.repository = repository;
    }

    public Optional<Bindge> get(Long id) {
        return repository.findById(id);
    }

    public Bindge save(Bindge entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Bindge> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Bindge> list(Pageable pageable, Specification<Bindge> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
