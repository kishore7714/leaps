package com.leaps.serviceImpl.flc;

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

import com.leaps.dto.flc.FlcClientDetailsDto;
import com.leaps.entity.flc.FlcClientDetails;
import com.leaps.error.ErrorService;
import com.leaps.exceptions.ResourceNotFoundException;
import com.leaps.repository.flc.FlcClientDetailsRepository;
import com.leaps.responses.flc.FlcClientDetailsResponse;
import com.leaps.service.flc.FlcClientDetailsService;

@Service
public class FlcClientDetailsServiceImpl implements FlcClientDetailsService {

	// Autowiring Repository to use jpa methods and custom queries
	@Autowired
	private FlcClientDetailsRepository flcClientDetailsRepository;

	// Autowiring Error Service to Display the Response Messages Stored in Error
	// Table
	@Autowired
	private ErrorService errorService;

	// Entity to Dto using Function

	public Function<FlcClientDetails, FlcClientDetailsDto> entityToDto = entity -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		mapper.addMappings(new PropertyMap<FlcClientDetails, FlcClientDetailsDto>() {

			@Override
			protected void configure() {
				map().setCompanyName(source.getCompany().getCompanyName());

			}

		});
		FlcClientDetailsDto dto = mapper.map(entity, FlcClientDetailsDto.class);
		return dto;
	};

	// Dto to Entity using Function

	public Function<FlcClientDetailsDto, FlcClientDetails> dtoToEntity = dto -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		FlcClientDetails entity = mapper.map(dto, FlcClientDetails.class);
		return entity;
	};

	// Get all where valid flag=1
	@Override
	public List<FlcClientDetailsDto> getall() {
		List<FlcClientDetails> listOfFlcClientDetails = flcClientDetailsRepository.getallActive();
		List<FlcClientDetailsDto> listOfFlcClientDetailsDto = listOfFlcClientDetails.stream()
				.map(flcClientDetails -> entityToDto.apply(flcClientDetails)).collect(Collectors.toList());
		return listOfFlcClientDetailsDto;
	}

	// Get All where valid flag=1 With Pagination
	@Override
	public FlcClientDetailsResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir) {
		// sort Condition
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		// Create Pageable Instance
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

		Page<FlcClientDetails> flcClientDetailsPage = flcClientDetailsRepository.getallActivePagination(pageable);

		// get content
		List<FlcClientDetails> listOfFlcClientDetails = flcClientDetailsPage.getContent();

		List<FlcClientDetailsDto> content = listOfFlcClientDetails.stream()
				.map(flcClientDetails -> entityToDto.apply(flcClientDetails)).collect(Collectors.toList());

		// Setting the the values to Custom Response Created
		FlcClientDetailsResponse flcClientDetailsResponse = new FlcClientDetailsResponse();
		flcClientDetailsResponse.setContent(content);
		flcClientDetailsResponse.setPageNo(flcClientDetailsPage.getNumber());
		flcClientDetailsResponse.setPageSize(flcClientDetailsPage.getSize());
		flcClientDetailsResponse.setTotalElements(flcClientDetailsPage.getTotalElements());
		flcClientDetailsResponse.setTotalPages(flcClientDetailsPage.getTotalPages());
		flcClientDetailsResponse.setLast(flcClientDetailsPage.isLast());

		return flcClientDetailsResponse;
	}

	// Get Active By id
	@Override
	public FlcClientDetailsDto getbyid(Long id) {
		FlcClientDetails flcClientDetails = flcClientDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FlcClientDetails", "id", id));
		FlcClientDetailsDto flcClientDetailsDto = entityToDto.apply(flcClientDetails);
		return flcClientDetailsDto;
	}

	// Add
	@Override
	public String add(FlcClientDetailsDto dto) {
		FlcClientDetails flcClientDetails = dtoToEntity.apply(dto);
		flcClientDetails.setValidFlag(1);
		flcClientDetailsRepository.save(flcClientDetails);
		return errorService.getErrorById("ER001");
	}

	// Update
	@Override
	public String update(Long id, FlcClientDetailsDto dto) {
		FlcClientDetails entity = dtoToEntity.apply(dto);

		FlcClientDetails flcClientDetails = flcClientDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FlcClientDetails", "id", id));

		// Using Optional for Null check
		Optional.of(entity.getCompanyId())
				.ifPresent((DeathClaimCoverDetails) -> flcClientDetails.setCompanyId(entity.getCompanyId()));

		Optional.ofNullable(entity.getClntNum())
				.ifPresent((FlcClientDetails) -> flcClientDetails.setClntNum(entity.getClntNum()));
		Optional.ofNullable(entity.getContactNumber())
				.ifPresent((FlcClientDetails) -> flcClientDetails.setContactNumber(entity.getContactNumber()));
		Optional.ofNullable(entity.getEmailId())
				.ifPresent((FlcClientDetails) -> flcClientDetails.setEmailId(entity.getEmailId()));
		Optional.ofNullable(entity.getLaName())
				.ifPresent((FlcClientDetails) -> flcClientDetails.setLaName(entity.getLaName()));
		Optional.ofNullable(entity.getLaDob())
				.ifPresent((FlcClientDetails) -> flcClientDetails.setLaDob(entity.getLaDob()));
		Optional.ofNullable(entity.getNationality())
				.ifPresent((FlcClientDetails) -> flcClientDetails.setNationality(entity.getNationality()));
		Optional.ofNullable(entity.getResidentStatus())
				.ifPresent((FlcClientDetails) -> flcClientDetails.setResidentStatus(entity.getResidentStatus()));
		Optional.ofNullable(entity.getGender())
				.ifPresent((FlcClientDetails) -> flcClientDetails.setGender(entity.getGender()));

		flcClientDetailsRepository.save(flcClientDetails);
		return errorService.getErrorById("ER002");

	}

	// Soft Delete
	@Override
	public String softdelete(Long id) {
		FlcClientDetails flcClientDetails = flcClientDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FlcClientDetails", "id", id));
		flcClientDetails.setValidFlag(-1);
		flcClientDetailsRepository.save(flcClientDetails);
		return errorService.getErrorById("ER003");
	}

	// Hard Delete
	@Override
	public String harddelete(Long id) {
		FlcClientDetails flcClientDetails = flcClientDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FlcClientDetails", "id", id));
		flcClientDetailsRepository.delete(flcClientDetails);
		return errorService.getErrorById("ER003");
	}

	// Global Search
	@Override
	public List<FlcClientDetailsDto> globalSearch(String key) {
		List<FlcClientDetails> listOfFlcClientDetails = flcClientDetailsRepository.globalSearch(key);
		List<FlcClientDetailsDto> listOfFlcClientDetailsDto = listOfFlcClientDetails.stream()
				.map(flcClientDetails -> entityToDto.apply(flcClientDetails)).collect(Collectors.toList());
		return listOfFlcClientDetailsDto;
	}
}
