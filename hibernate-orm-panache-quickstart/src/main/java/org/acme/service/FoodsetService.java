package org.acme.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.acme.entity.*;
import org.acme.resource.DishInSetPOJO;
import org.acme.resource.DishPOJO;
import org.acme.resource.FoodsetPOJO;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@ApplicationScoped
@AllArgsConstructor
@Slf4j
public class FoodsetService {

    @Inject
    EntityManager entityManager;

    @Inject
    private FoodsetRepository foodsetRepository;
    @Inject
    private DishRepository dishRepository;
    @Inject
    private DishInSetRepository dishInSetRepository;
    @Inject
    private MealtypeRepository mealtypeRepository;
    @Inject
    private FoodsetMapper foodsetMapper;

    @Inject
    private DishInSetMapper dishInSetMapper;

    @Inject
    Validator validator;

    public FoodsetService.Result validateDishInSet(@Valid DishInSetPOJO dis) {
        DishInSet entity = dishInSetMapper.toEntity(dis);
        Set<ConstraintViolation<DishInSet>> violations = validator.validate(entity);
        if (violations.isEmpty()) {
            return new FoodsetService.Result("Dish In Set is valid! It was validated by service validation.");
        } else {
            return new FoodsetService.Result(violations);
        }
    }

    public FoodsetService.Result validateFoodset(@Valid FoodsetPOJO dis) {
        Foodset entity = foodsetMapper.toEntity(dis);
        Set<ConstraintViolation<Foodset>> violations = validator.validate(entity);
        if (violations.isEmpty()) {
            return new FoodsetService.Result("Foodset is valid! It was validated by service validation.");
        } else {
            return new FoodsetService.Result(violations);
        }
    }

    public List<FoodsetPOJO> findAll() {
        return this.foodsetMapper.toDomainList(foodsetRepository.findAndSort());
    }

    public Optional<FoodsetPOJO> findById(@NonNull Long foodsetId) {
        return foodsetRepository.findByIdOptional(foodsetId)
                .map(foodsetMapper::toDomain);
    }

    @Transactional
    public void save(@Valid FoodsetPOJO foodset) {
        log.debug("Saving Foodset: {}", foodset);
        FoodsetService.Result res = validateFoodset(foodset);
        if(res.success){
            log.info(res.message);
            Foodset entity = foodsetMapper.toEntity(foodset);
            foodsetRepository.persist(entity);
            foodsetMapper.updateDomainFromEntity(entity, foodset);
        }
        else {
            log.error("Foodset validation error: {}", res.message);
            throw new ServiceException(res.message);
        }
    }

    @Transactional
    public void savexrandom(int x) {
        List<Dish> dishes = dishRepository.listAll();
        List<Long> ids = new ArrayList();
        for (Dish d : dishes){
            ids.add(d.id);
        }
        for (int i = 0; i < x; i++) {
            if (i > 0 && i % 50 == 0) {
                entityManager.flush();
                entityManager.clear();
            }
            Foodset fs = new Foodset();
            fs.name = ("Обед №"+ i);
            fs.mealtype = mealtypeRepository.findById(2L);
            fs.total=0.0;
            log.debug("Saving Foodset: {}", fs);
            foodsetRepository.persist(fs);
            FoodsetPOJO entity = foodsetMapper.toDomain(fs);
            foodsetMapper.updateDomainFromEntity(fs, entity);
            Dish d1 = Dish.findById(ids.get(ThreadLocalRandom.current().nextInt(0, ids.size())));
            Dish d2 = Dish.findById(ids.get(ThreadLocalRandom.current().nextInt(0, ids.size())));
            Dish d3 = Dish.findById(ids.get(ThreadLocalRandom.current().nextInt(0, ids.size())));
            DishInSet dis = new DishInSet();
            dis.foodset = fs;
            dis.dish = d1;
            dis.note = "-";
            fs.addDishInSet(dis);
            log.debug("Saving DishInSet: {}", dis);
            dishInSetRepository.persist(dis);
            entity = foodsetMapper.toDomain(fs);
            foodsetMapper.updateDomainFromEntity(fs, entity);
            dis = new DishInSet();
            dis.foodset = fs;
            dis.dish = d2;
            dis.note = "-";
            fs.addDishInSet(dis);
            log.debug("Saving DishInSet: {}", dis);
            dishInSetRepository.persist(dis);
            entity = foodsetMapper.toDomain(fs);
            foodsetMapper.updateDomainFromEntity(fs, entity);
            dis = new DishInSet();
            dis.foodset = fs;
            dis.dish = d3;
            dis.note = "-";
            log.debug("Saving DishInSet: {}", dis);
            fs.addDishInSet(dis);
            dishInSetRepository.persist(dis);
        }
    }

    @Transactional
    public void update(@Valid FoodsetPOJO foodset) {
        log.debug("Updating Foodset: {}", foodset);
        if (Objects.isNull(foodset.getId())) {
            throw new ServiceException("Foodset does not have a foodsetId");
        }
        FoodsetService.Result res = validateFoodset(foodset);
        if(res.success){
            log.info(res.message);
            Foodset entity = foodsetRepository.findByIdOptional(foodset.getId())
                    .orElseThrow(() -> new ServiceException("No Foodset found for foodsetId[%s]", foodset.getId()));
            foodsetMapper.updateEntityFromDomain(foodset, entity);
            foodsetRepository.persist(entity);
            foodsetMapper.updateDomainFromEntity(entity, foodset);
        }
        else {
            log.error("Foodset validation error: {}", res.message);
            throw new ServiceException(res.message);
        }
    }

    @Transactional
    public void delete(@Valid Long id) {
        log.debug("Deleting Foodset with id: {}", id);
        if (Objects.isNull(foodsetRepository.findById(id))) {
            throw new ServiceException("Foodset does not exist");
        }
        foodsetRepository.deleteById(id);
    }

    @Transactional
    public void commentEdit(Long id, Long dish, String comment) {
        log.debug("Editing a Dishinset inside a Foodset No: {}", id);
        if (Objects.isNull(foodsetRepository.findById(id))) {
            throw new ServiceException("Foodset does not exist");
        }
        Foodset entity = foodsetRepository.findById(id);
        if (Objects.isNull(dishInSetRepository.findById(dish))) {
            throw new ServiceException("Dish does not exist");
        }
        entity.editComment(dishInSetRepository.findById(dish), comment);
    }

    @Transactional
    public void addFoodToSet(Long id, Long dish, String comment) {
        log.debug("Adding a Dish to a Foodset No: {}", id);
        if (Objects.isNull(foodsetRepository.findById(id))) {
            throw new ServiceException("Foodset does not exist");
        }
        Foodset entity = foodsetRepository.findById(id);
        if (Objects.isNull(dishRepository.findById(dish))) {
            throw new ServiceException("Dish does not exist");
        }
        Dish d = dishRepository.findById(dish);
        DishInSet dis = new DishInSet();
        dis.foodset = entity;
        dis.dish = d;
        dis.note = comment;
        entity.addDishInSet(dis);
        dishInSetRepository.persist(dis);
        DishInSetPOJO dispojo = dishInSetMapper.toDomain(dis);
        dishInSetMapper.updateDomainFromEntity(dis, dispojo);
    }



    @Transactional
    public Response removeFoodFromSet(Long id, Long disid) {
        log.debug("Removing a Dish from a Foodset No: {}", id);
        if (Objects.isNull(foodsetRepository.findById(id))) {
            throw new ServiceException("Foodset does not exist");
        }
        if (Objects.isNull(dishInSetRepository.findById(disid))) {
            throw new ServiceException("Such connection between dish and foodset does not exist");
        }
        foodsetRepository.findById(id).removeDishInSet(dishInSetRepository.findById(disid));
        dishInSetRepository.deleteById(disid);
        return Response.status(204).build();
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
