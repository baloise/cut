/*
 * Copyright 2019 Baloise Group
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
package ch.basler.cat.services;

import ch.basler.cat.model.CodeType;
import ch.basler.cat.model.InlineCodeValuesAndStyles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin
@RepositoryRestResource(collectionResourceRel = "codeType", path = "codeType", excerptProjection = InlineCodeValuesAndStyles.class)

public interface CodeTypeRepository extends PagingAndSortingRepository<CodeType, Long> {

    Page<List<CodeType>> findByName(@Param("name") String name, Pageable pageable);

    Page<List<CodeType>> findByResponsiblePrefix(@Param("prefix") String prefix, Pageable pageable);

}
