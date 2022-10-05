package org.acme.service;


import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.acme.entity.DishMapper;
import org.acme.entity.Dish;
import org.acme.resource.DishPOJO;
import org.acme.resource.DishPOJO;

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
public class DishService {

    @Inject
    private DishRepository DishRepository;

    @Inject
    private DishMapper DishMapper;

    @Inject
    Validator validator;

    public DishService.Result validateDish(@Valid DishPOJO dish) {
        Dish entity = DishMapper.toEntity(dish);
        Set<ConstraintViolation<Dish>> violations = validator.validate(entity);
        if (violations.isEmpty()) {
            return new DishService.Result("Dish is valid! It was validated by service validation.");
        } else {
            return new DishService.Result(violations);
        }
    }

    public List<DishPOJO> findAll() {
        return this.DishMapper.toDomainList(DishRepository.findAll().list());
    }

    public Optional<DishPOJO> findById(@NonNull Long DishId) {
        return DishRepository.findByIdOptional(DishId)
                .map(DishMapper::toDomain);
    }

    @Transactional
    public void save(@Valid DishPOJO dish) {
        log.debug("Saving Dish: {}", dish);
        DishService.Result res = validateDish(dish);
        if(res.success){
            log.info(res.message);
            Dish entity = DishMapper.toEntity(dish);
            DishRepository.persist(entity);
            DishMapper.updateDomainFromEntity(entity, dish);
        }
        else {
            log.error("Dish validation error: {}", res.message);
            throw new ServiceException(res.message);
        }
    }

    @Transactional
    public void update(@Valid DishPOJO dish) {
        log.debug("Updating Dish: {}", dish);
        if (Objects.isNull(dish.getId())) {
            throw new ServiceException("Dish does not have a DishId");
        }
        DishService.Result res = validateDish(dish);
        if(res.success){
            log.info(res.message);
            org.acme.entity.Dish entity = DishRepository.findByIdOptional(dish.getId())
                    .orElseThrow(() -> new ServiceException("No Dish found for DishId[%s]", dish.getId()));
            DishMapper.updateEntityFromDomain(dish, entity);
            DishRepository.persist(entity);
            DishMapper.updateDomainFromEntity(entity, dish);
        }
        else {
            log.error("Dish validation error: {}", res.message);
            throw new ServiceException(res.message);
        }
    }

    @Transactional
    public void delete(@Valid Long id) {
        log.debug("Deleting Dish with id: {}", id);
        if (Objects.isNull(DishRepository.findById(id))) {
            throw new ServiceException("Dish does not exist");
        }
        DishRepository.deleteById(id);
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
