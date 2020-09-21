/*
 * Copyright 2020 Baloise Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.basler.cat.controller;

import ch.basler.cat.api.ApplicationDto;
import ch.basler.cat.model.Application;
import ch.basler.cat.services.ApplicationRepository;
import org.apache.commons.collections4.IterableUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ApplicationController {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);
    private final ApplicationRepository repository;

    private ModelMapper modelMapper;

    @Autowired
    public ApplicationController(
            ApplicationRepository repository,
            ModelMapper modelMapper) {

        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    // Aggregate root
    @GetMapping("/applications")
    public List<ApplicationDto> all() {
        return IterableUtils.toList(this.repository.findAll()).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/applications")
    public ApplicationDto create(@RequestBody ApplicationDto applicationDto) {
        Application application = convertToEntity(applicationDto);
        Application applicationCreated = repository.save(application);
        return convertToDto(applicationCreated);
    }

    // Single item
    @GetMapping("/applications/{id}")
    public ApplicationDto one(@PathVariable("id") Long id) {
        Application application = this.repository.findById(id)
                .orElseThrow(() -> new EntityFoundException("application", id));

        return convertToDto(application);
    }

    @PutMapping("/applications/{id}")
    public ApplicationDto update(@RequestBody ApplicationDto newApplicationDto, @PathVariable Long id) {
        Application newApplication = convertToEntity(newApplicationDto);
        return repository.findById(id)
                .map((application -> {
                    modelMapper.map(newApplication, application);
                    return convertToDto(repository.save(application));
                })).orElseGet(() -> {
                    newApplication.setId(id);
                    return convertToDto(repository.save(newApplication));
                });
    }

    @DeleteMapping("/applications/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }

    ApplicationDto convertToDto(Application application) {
        return modelMapper.map(application, ApplicationDto.class);
    }

    Application convertToEntity(ApplicationDto applicationDto) {
        return modelMapper.map(applicationDto, Application.class);
    }
}
