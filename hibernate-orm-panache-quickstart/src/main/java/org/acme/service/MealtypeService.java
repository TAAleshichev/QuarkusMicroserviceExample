package org.acme.service;


import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.acme.entity.Mealtype;
import org.acme.entity.MealtypeMapper;
import org.acme.resource.MealtypePOJO;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
@AllArgsConstructor
@Slf4j
public class MealtypeService {


    @Inject
    private MealtypeRepository mealtypeRepository;

    @Inject
    Validator validator;

    public Result validateMealtype(@Valid MealtypePOJO mealtype) {
        Mealtype entity = mealtypeMapper.toEntity(mealtype);
        Set<ConstraintViolation<Mealtype>> violations = validator.validate(entity);
        if (violations.isEmpty()) {
            return new Result("Mealtype is valid! It was validated by service validation.");
        } else {
            return new Result(violations);
        }
    }

    @Inject
    private MealtypeMapper mealtypeMapper;

    public List<MealtypePOJO> findAll() {
        return this.mealtypeMapper.toDomainList(mealtypeRepository.findAll().list());
    }

    public Optional<MealtypePOJO> findById(@NonNull Long mealtypeId) {
        return mealtypeRepository.findByIdOptional(mealtypeId)
                .map(mealtypeMapper::toDomain);
    }

    @Transactional
    public void save(@Valid MealtypePOJO mealtype) {
        log.debug("Saving Mealtype: {}", mealtype);
        Result res = validateMealtype(mealtype);
        if(res.success){
            log.info(res.message);
            Mealtype entity = mealtypeMapper.toEntity(mealtype);
            mealtypeRepository.persist(entity);
            mealtypeMapper.updateDomainFromEntity(entity, mealtype);
        }
        else {
            log.error("Mealtype validation error: {}", res.message);
            throw new ServiceException(res.message);
        }
    }

    @Transactional
    public void update(@Valid MealtypePOJO mealtype) {
        log.debug("Updating Mealtype: {}", mealtype);
        if (Objects.isNull(mealtype.getId())) {
            throw new ServiceException("Mealtype does not have a mealtypeId");
        }
        Result res = validateMealtype(mealtype);
        if(res.success){
            log.info(res.message);
            Mealtype entity = mealtypeRepository.findByIdOptional(mealtype.getId())
                .orElseThrow(() -> new ServiceException("No Mealtype found for mealtypeId[%s]", mealtype.getId()));
            mealtypeMapper.updateEntityFromDomain(mealtype, entity);
            mealtypeRepository.persist(entity);
            mealtypeMapper.updateDomainFromEntity(entity, mealtype);
        }
        else {
            log.error("Mealtype validation error: {}", res.message);
            throw new ServiceException(res.message);
        }
    }

    @Transactional
    public void delete(@Valid Long id) {
        log.debug("Deleting Mealtype with id: {}", id);
        if (Objects.isNull(mealtypeRepository.findById(id))) {
            throw new ServiceException("Mealtype does not exist");
        }
        mealtypeRepository.deleteById(id);
    }

    public static class Result {

        Result(String message) {
            this.success = true;
            this.message = message;
        }

        Result(Set<? extends ConstraintViolation<?>> violations) {
            this.success = false;
            this.message = violations.stream()
                    .map(cv -> cv.getMessage())
                    .collect(Collectors.joining(", "));
        }

        private String message;
        private boolean success;

        public String getMessage() {
            return message;
        }

        public boolean isSuccess() {
            return success;
        }

    }
    
}
