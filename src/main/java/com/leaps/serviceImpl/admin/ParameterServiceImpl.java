package com.leaps.serviceImpl.admin;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.leaps.dto.admin.ParameterDto;
import com.leaps.entity.admin.Parameter;
import com.leaps.error.ErrorService;
import com.leaps.exceptions.ResourceNotFoundException;
import com.leaps.repository.admin.ParameterRepository;
import com.leaps.responses.admin.ParameterResponse;
import com.leaps.service.admin.ParameterService;

@Service
public class ParameterServiceImpl implements ParameterService {

        @Autowired
        private ModelMapper mapper;

        // Autowiring Repository to use jpa methods and custom queries
        @Autowired
        private ParameterRepository parameterRepository;

        // Autowiring Error Service to Display the Response Messages Stored in Error
        // Table
        @Autowired
        private ErrorService errorService;

        // Entity to Dto using Function

        public Function<Parameter, ParameterDto> entityToDto = entity -> mapper.map(entity, ParameterDto.class);

        // Dto to Entity using Function

        public Function<ParameterDto, Parameter> dtoToEntity = dto -> mapper.map(dto, Parameter.class);

        // Get all where valid flag=1
        @Override
        public List<ParameterDto> getall() {
                List<Parameter> listOfParameter = parameterRepository.findAll();
                List<ParameterDto> listOfParameterDto = listOfParameter.stream()
                                .map(userGroup -> entityToDto.apply(userGroup))
                                .collect(Collectors.toList());
                return listOfParameterDto;
        }

        // Get All where valid flag=1 With Pagination
        @Override
        public ParameterResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir) {
                // sort Condition
                Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                                : Sort.by(sortBy).descending();

                // Create Pageable Instance
                Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

                Page<Parameter> paramterPage = parameterRepository.findAll(pageable);

                // get content
                List<Parameter> listOfParameter = paramterPage.getContent();

                List<ParameterDto> content = listOfParameter.stream().map(userGroup -> entityToDto.apply(userGroup))
                                .collect(Collectors.toList());

                // Setting the the values to Custom Response Created
                ParameterResponse parameterResponse = new ParameterResponse();
                parameterResponse.setContent(content);
                parameterResponse.setPageNo(paramterPage.getNumber());
                parameterResponse.setPageSize(paramterPage.getSize());
                parameterResponse.setTotalElements(paramterPage.getTotalElements());
                parameterResponse.setTotalPages(paramterPage.getTotalPages());
                parameterResponse.setLast(paramterPage.isLast());

                return parameterResponse;
        }

        // Get Active By id
        @Override
        public ParameterDto getbyid(Long id) {
                Parameter usergroup = parameterRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Parameter", "id", id));
                ParameterDto userGroupDto = entityToDto.apply(usergroup);
                return userGroupDto;
        }

        // Add
        @Override
        public String add(ParameterDto dto) {
                Parameter parameter = dtoToEntity.apply(dto);
                parameterRepository.save(parameter);
                return errorService.getErrorById("ER001");
        }

        // Update
        @Override
        public String update(Long id, ParameterDto dto) {
                Parameter entity = dtoToEntity.apply(dto);

                Parameter parameter = parameterRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Parameter", "id", id));

                // Using Optional for Null check
                Optional.ofNullable(entity.getParameter())
                                .ifPresent((Parameter) -> parameter.setParameter(entity.getParameter()));
                Optional.ofNullable(entity.getRule()).ifPresent((Parameter) -> parameter.setRule(entity.getRule()));
                Optional.ofNullable(entity.getShortDescription())
                                .ifPresent((Parameter) -> parameter.setShortDescription(entity.getShortDescription()));
                Optional.ofNullable(entity.getLongDescription())
                                .ifPresent((Parameter) -> parameter.getLongDescription());

                parameterRepository.save(parameter);
                return errorService.getErrorById("ER002");

        }

        // Soft Delete
        @Override
        public String softdelete(Long id) {
                Parameter parameter = parameterRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Parameter", "id", id));

                parameterRepository.save(parameter);
                return errorService.getErrorById("ER003");
        }

        // Hard Delete
        @Override
        public String harddelete(Long id) {
                Parameter parameter = parameterRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Parameter", "id", id));
                parameterRepository.delete(parameter);
                return errorService.getErrorById("ER003");
        }

        // Global Search
        @Override
        public List<ParameterDto> globalSearch(String key) {
                List<Parameter> listOfParameter = parameterRepository.globalSearch(key);
                List<ParameterDto> listOfParameterDto = listOfParameter.stream()
                                .map(userGroup -> entityToDto.apply(userGroup))
                                .collect(Collectors.toList());
                return listOfParameterDto;
        }

}
