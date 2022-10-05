package org.acme.service;


import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.acme.entity.DishInSetMapper;
import org.acme.resource.DishInSetPOJO;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@ApplicationScoped
@AllArgsConstructor
@Slf4j
public class DishInSetService {

    @Inject
    private DishInSetRepository DishInSetRepository;

    @Inject
    private DishInSetMapper DishInSetMapper;

    public List<DishInSetPOJO> findAll() {
        return this.DishInSetMapper.toDomainList(DishInSetRepository.findAll().list());
    }

    public Optional<DishInSetPOJO> findById(@NonNull Long DishInSetId) {
        return DishInSetRepository.findByIdOptional(DishInSetId)
                .map(DishInSetMapper::toDomain);
    }
    
}
