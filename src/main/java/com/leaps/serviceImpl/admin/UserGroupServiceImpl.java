package com.leaps.serviceImpl.admin;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.leaps.dto.admin.UserGroupDto;
import com.leaps.entity.admin.UserGroup;
import com.leaps.error.ErrorService;
import com.leaps.exceptions.ResourceNotFoundException;
import com.leaps.repository.admin.UserGroupRepository;
import com.leaps.responses.admin.UserGroupResponse;
import com.leaps.service.admin.UserGroupService;

@Service
public class UserGroupServiceImpl implements UserGroupService {


    // Autowiring Repository to use jpa methods and custom queries
    @Autowired
    private UserGroupRepository userGroupRepository;

    // Autowiring Error Service to Display the Response Messages Stored in Error
    // Table
    @Autowired
    private ErrorService errorService;

    // Entity to Dto using Function

    public Function<UserGroup, UserGroupDto> entityToDto = entity -> {
    	
    	//using Model Mapper to convert entity to dto
    	ModelMapper mapper = new ModelMapper();
    	mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    	

    	UserGroupDto dto = mapper.map(entity, UserGroupDto.class);
        return dto;
    };

    // Dto to Entity using Function

    public Function<UserGroupDto, UserGroup> dtoToEntity = dto -> {
    	
    	//using Model Mapper to convert entity to dto
    	ModelMapper mapper = new ModelMapper();
    	mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    	UserGroup entity = mapper.map(dto, UserGroup.class);
        return entity;
    };

    // Get all where valid flag=1
    @Override
    public List<UserGroupDto> getall() {
        List<UserGroup> listOfUserGroup = userGroupRepository.getallActive();
        List<UserGroupDto> listOfUserGroupDto = listOfUserGroup.stream()
                .map(userGroup -> entityToDto.apply(userGroup))
                .collect(Collectors.toList());
        return listOfUserGroupDto;
    }

    // Get All where valid flag=1 With Pagination
    @Override
    public UserGroupResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir) {
        // sort Condition
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Create Pageable Instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<UserGroup> userGroupPage = userGroupRepository.getallActivePagination(pageable);

        // get content
        List<UserGroup> listOfUserGroup = userGroupPage.getContent();

        List<UserGroupDto> content = listOfUserGroup.stream().map(userGroup -> entityToDto.apply(userGroup))
                .collect(Collectors.toList());

        // Setting the the values to Custom Response Created
        UserGroupResponse userGroupResponse = new UserGroupResponse();
        userGroupResponse.setContent(content);
        userGroupResponse.setPageNo(userGroupPage.getNumber());
        userGroupResponse.setPageSize(userGroupPage.getSize());
        userGroupResponse.setTotalElements(userGroupPage.getTotalElements());
        userGroupResponse.setTotalPages(userGroupPage.getTotalPages());
        userGroupResponse.setLast(userGroupPage.isLast());

        return userGroupResponse;
    }

    // Get Active By id
    @Override
    public UserGroupDto getbyid(Long id) {
        UserGroup usergroup = userGroupRepository.getActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserGroup", "id", id));
        UserGroupDto userGroupDto = entityToDto.apply(usergroup);
        return userGroupDto;
    }

    // Add
    @Override
    public String add(UserGroupDto dto) {
        UserGroup userGroup = dtoToEntity.apply(dto);
        userGroup.setValidFlag(1);
        userGroupRepository.save(userGroup);
        return errorService.getErrorById("ER001");
    }

    // Update
    @Override
    public String update(Long id, UserGroupDto dto) {
        UserGroup entity = dtoToEntity.apply(dto);

        UserGroup usergroup = userGroupRepository.getActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserGroup", "id", id));

        // Using Optional for Null check
//        Optional.of(entity.getCompanyId()).ifPresent((DeathClaimCoverDetails) -> usergroup.setCompanyId(entity.getCompanyId()));

        Optional.ofNullable(entity.getUserGroupName())
                .ifPresent((UserGroup) -> usergroup.setUserGroupName(entity.getUserGroupName()));

        userGroupRepository.save(usergroup);
        return errorService.getErrorById("ER002");

    }

    // Soft Delete
    @Override
    public String softdelete(Long id) {
        UserGroup userGroup = userGroupRepository.getActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserGroup", "id", id));
        userGroup.setValidFlag(-1);
        userGroupRepository.save(userGroup);
        return errorService.getErrorById("ER003");
    }

    // Hard Delete
    @Override
    public String harddelete(Long id) {
        UserGroup userGroup = userGroupRepository.getActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserGroup", "id", id));
        userGroupRepository.delete(userGroup);
        return errorService.getErrorById("ER003");
    }

    // Global Search
    @Override
    public List<UserGroupDto> globalSearch(String key) {
        List<UserGroup> listOfUserGroup = userGroupRepository.globalSearch(key);
        List<UserGroupDto> listOfUserGroupDto = listOfUserGroup.stream().map(userGroup -> entityToDto.apply(userGroup))
                .collect(Collectors.toList());
        return listOfUserGroupDto;
    }

}
