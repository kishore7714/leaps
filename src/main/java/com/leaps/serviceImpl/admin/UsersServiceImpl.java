package com.leaps.serviceImpl.admin;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.leaps.dto.admin.UsersDto;
import com.leaps.entity.admin.Users;
import com.leaps.error.ErrorService;
import com.leaps.exceptions.ResourceNotFoundException;
import com.leaps.repository.admin.UsersRepository;
import com.leaps.responses.admin.UsersResponse;
import com.leaps.service.admin.UsersService;

@Service
public class UsersServiceImpl implements UsersService {
    // Autowiring Repository to use jpa methods and custom queries
    @Autowired
    private UsersRepository usersRepository;

    // Autowiring Error Service to Display the Response Messages Stored in Error
    // Table
    @Autowired
    private ErrorService errorService;

    // Entity to Dto using Function

    public Function<Users, UsersDto> entityToDto = entity -> {
        
    	ModelMapper mapper = new ModelMapper();
    	mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    	mapper.addMappings(new PropertyMap<Users, UsersDto>() {

			@Override
			protected void configure() {
				map().setCompanyName(source.getCompany().getCompanyName());
//				map().setUserGroupName(source.getUserGroup().getUserGroupName());
				
				
			}
    		
		});
        
        UsersDto dto = mapper.map(entity, UsersDto.class);
        return dto;
    };

    // Dto to Entity using Function

    public Function<UsersDto, Users> dtoToEntity = dto -> {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Users entity = mapper.map(dto,Users.class);
        return entity;
    };

    // Get all where valid flag=1
    @Override
    public List<UsersDto> getall() {
        List<Users> listOfUsers = usersRepository.getallActive();
        List<UsersDto> listOfUsersDto = listOfUsers.stream()
                .map(users -> entityToDto.apply(users))
                .collect(Collectors.toList());
        return listOfUsersDto;
    }

    // Get All where valid flag=1 With Pagination
    @Override
    public UsersResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir) {
        // sort Condition
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Create Pageable Instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Users> usersPage = usersRepository.getallActivePagination(pageable);

        // get content
        List<Users> listOfUsers = usersPage.getContent();

        List<UsersDto> content = listOfUsers.stream().map(users -> entityToDto.apply(users))
                .collect(Collectors.toList());

        // Setting the the values to Custom Response Created
        UsersResponse usersResponse = new UsersResponse();
        usersResponse.setContent(content);
        usersResponse.setPageNo(usersPage.getNumber());
        usersResponse.setPageSize(usersPage.getSize());
        usersResponse.setTotalElements(usersPage.getTotalElements());
        usersResponse.setTotalPages(usersPage.getTotalPages());
        usersResponse.setLast(usersPage.isLast());

        return usersResponse;
    }

    // Get Active By id
    @Override
    public UsersDto getbyid(Long id) {
        Users users = usersRepository.getActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Users", "id", id));
        UsersDto usersDto = entityToDto.apply(users);
        return usersDto;
    }

    // Add
    @Override
    public String add(UsersDto dto) {
        Users users = dtoToEntity.apply(dto);
        users.setValidFlag(1);
        usersRepository.save(users);
        return errorService.getErrorById("ER001");
    }

    // Update
    @Override
    public String update(Long id, UsersDto dto) {
        Users entity = dtoToEntity.apply(dto);

        Users users = usersRepository.getActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Users", "id", id));

        // Using Optional for Null check
        Optional.ofNullable(entity.getCompanyId()).ifPresent((Users) -> users.setCompanyId(entity.getCompanyId()));
        Optional.ofNullable(entity.getUsername()).ifPresent((Users) -> users.setUsername(entity.getUsername()));
        Optional.ofNullable(entity.getEmail()).ifPresent((Users) -> users.setEmail(entity.getEmail()));
        Optional.ofNullable(entity.getPassword()).ifPresent((Users) -> users.setPassword(entity.getPassword()));
//        Optional.ofNullable(entity.getUserGroupId())
//                .ifPresent((Users) -> users.setUserGroupId(entity.getUserGroupId()));
        Optional.ofNullable(entity.getProfile()).ifPresent((Users) -> users.setProfile(entity.getProfile()));
        Optional.ofNullable(entity.getVerificationCode())
                .ifPresent((Users) -> users.setVerificationCode(entity.getVerificationCode()));

        usersRepository.save(users);
        return errorService.getErrorById("ER002");

    }

    // Soft Delete
    @Override
    public String softdelete(Long id) {
        Users users = usersRepository.getActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Users", "id", id));
        users.setValidFlag(-1);
        usersRepository.save(users);
        return errorService.getErrorById("ER003");
    }

    // Hard Delete
    @Override
    public String harddelete(Long id) {
        Users users = usersRepository.getActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Users", "id", id));
        usersRepository.delete(users);
        return errorService.getErrorById("ER003");
    }

    // Global Search
    @Override
    public List<UsersDto> globalSearch(String key) {
        List<Users> listOfUsers = usersRepository.globalSearch(key);
        List<UsersDto> listOfUsersDto = listOfUsers.stream().map(users -> entityToDto.apply(users))
                .collect(Collectors.toList());
        return listOfUsersDto;
    }
}
