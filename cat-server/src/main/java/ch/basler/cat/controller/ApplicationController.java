package ch.basler.cat.controller;

import ch.basler.cat.model.Application;
import ch.basler.cat.services.ApplicationRepository;
import org.apache.commons.collections4.IterableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ApplicationController {

    private static final Logger log = LoggerFactory.getLogger(ApplicationController.class);
    private final ApplicationRepository repository;
    private ApplicationResourceAssembler applicationResourceAssembler;

    @Autowired
    public ApplicationController(
            ApplicationRepository repository,
            ApplicationResourceAssembler applicationResourceAssembler) {

        this.repository = repository;
        this.applicationResourceAssembler = applicationResourceAssembler;
    }

//    @RequestMapping(method = RequestMethod.GET)
//    public NestedContentResource<ApplicationResource> all() {
//        return new NestedContentResource<ApplicationResource>(applicationResourceAssembler.toCollectionModel(this.applicationRepository.findAll()));
//    }

    // Aggregate root
    @GetMapping("/applications")
    public List<Application> all() {
        return IterableUtils.toList(this.repository.findAll());
    }

    @PostMapping("/applications")
    public Application create(@RequestBody Application application) {
        return repository.save(application);
    }

    // Single item
    @GetMapping("/applications/{id}")
    public Application one(@PathVariable("id") Long id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new ApplicationNotFoundException(id));
    }

    @PutMapping("/applications/{id}")
    public Application update(@RequestBody Application newApplication, @PathVariable Long id) {
        return repository.findById(id)
                .map((application -> {
                    application.setName(newApplication.getName());
                    return repository.save(application);
                })).orElseGet(() -> {
                    newApplication.setId(id);
                    return repository.save(newApplication);
                });
    }

    @DeleteMapping("/applications/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}