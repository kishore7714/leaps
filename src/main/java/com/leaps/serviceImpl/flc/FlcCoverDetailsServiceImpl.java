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

import com.leaps.dto.flc.FlcCoverDetailsDto;
import com.leaps.entity.flc.FlcCoverDetails;
import com.leaps.error.ErrorService;
import com.leaps.exceptions.ResourceNotFoundException;
import com.leaps.repository.flc.FlcCoverDetailsRepository;
import com.leaps.responses.flc.FlcCoverDetailsResponse;
import com.leaps.service.flc.FlcCoverDetailsService;

@Service
public class FlcCoverDetailsServiceImpl implements FlcCoverDetailsService {

	// Autowiring Repository to use jpa methods and custom queries
	@Autowired
	private FlcCoverDetailsRepository flcCoverDetailsRepository;

	// Autowiring Error Service to Display the Response Messages Stored in Error
	// Table
	@Autowired
	private ErrorService errorService;

	// Entity to Dto using Function

	public Function<FlcCoverDetails, FlcCoverDetailsDto> entityToDto = entity -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		mapper.addMappings(new PropertyMap<FlcCoverDetails, FlcCoverDetailsDto>() {

			@Override
			protected void configure() {
				map().setCompanyName(source.getCompany().getCompanyName());

			}

		});
		FlcCoverDetailsDto dto = mapper.map(entity, FlcCoverDetailsDto.class);
		return dto;
	};

	// Dto to Entity using Function

	public Function<FlcCoverDetailsDto, FlcCoverDetails> dtoToEntity = dto -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		FlcCoverDetails entity = mapper.map(dto, FlcCoverDetails.class);
		return entity;
	};

	// Get all where valid flag=1
	@Override
	public List<FlcCoverDetailsDto> getall() {
		List<FlcCoverDetails> listOfFlcCoverDetails = flcCoverDetailsRepository.getallActive();
		List<FlcCoverDetailsDto> listOfFlcCoverDetailsDto = listOfFlcCoverDetails.stream()
				.map(flcCoverDetails -> entityToDto.apply(flcCoverDetails)).collect(Collectors.toList());
		return listOfFlcCoverDetailsDto;
	}

	// Get All where valid flag=1 With Pagination
	@Override
	public FlcCoverDetailsResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir) {
		// sort Condition
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		// Create Pageable Instance
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

		Page<FlcCoverDetails> flcCoverDetailsPage = flcCoverDetailsRepository.getallActivePagination(pageable);

		// get content
		List<FlcCoverDetails> listOfFlcCoverDetails = flcCoverDetailsPage.getContent();

		List<FlcCoverDetailsDto> content = listOfFlcCoverDetails.stream()
				.map(flcCoverDetails -> entityToDto.apply(flcCoverDetails)).collect(Collectors.toList());

		// Setting the the values to Custom Response Created
		FlcCoverDetailsResponse flcCoverDetailsResponse = new FlcCoverDetailsResponse();
		flcCoverDetailsResponse.setContent(content);
		flcCoverDetailsResponse.setPageNo(flcCoverDetailsPage.getNumber());
		flcCoverDetailsResponse.setPageSize(flcCoverDetailsPage.getSize());
		flcCoverDetailsResponse.setTotalElements(flcCoverDetailsPage.getTotalElements());
		flcCoverDetailsResponse.setTotalPages(flcCoverDetailsPage.getTotalPages());
		flcCoverDetailsResponse.setLast(flcCoverDetailsPage.isLast());

		return flcCoverDetailsResponse;
	}

	// Get Active By id
	@Override
	public FlcCoverDetailsDto getbyid(Long id) {
		FlcCoverDetails flcCoverDetails = flcCoverDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FlcCoverDetails", "id", id));
		FlcCoverDetailsDto flcCoverDetailsDto = entityToDto.apply(flcCoverDetails);
		return flcCoverDetailsDto;
	}

	// Add
	@Override
	public String add(FlcCoverDetailsDto dto) {
		FlcCoverDetails flcCoverDetails = dtoToEntity.apply(dto);
		flcCoverDetails.setValidFlag(1);
		flcCoverDetailsRepository.save(flcCoverDetails);
		return errorService.getErrorById("ER001");
	}

	// Update
	@Override
	public String update(Long id, FlcCoverDetailsDto dto) {
		FlcCoverDetails entity = dtoToEntity.apply(dto);

		FlcCoverDetails flcCoverDetails = flcCoverDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FlcCoverDetails", "id", id));

		// Using Optional for Null check
		Optional.of(entity.getCompanyId())
				.ifPresent((DeathClaimCoverDetails) -> flcCoverDetails.setCompanyId(entity.getCompanyId()));

		Optional.ofNullable(entity.getClntNum())
				.ifPresent((FlcCoverDetails) -> flcCoverDetails.setClntNum(entity.getClntNum()));
		Optional.ofNullable(entity.getPolicyNo())
				.ifPresent((FlcCoverDetails) -> flcCoverDetails.setPolicyNo(entity.getPolicyNo()));
		Optional.ofNullable(entity.getPlanName())
				.ifPresent((FlcCoverDetails) -> flcCoverDetails.setPlanName(entity.getPlanName()));
		Optional.ofNullable(entity.getPlanCode())
				.ifPresent((FlcCoverDetails) -> flcCoverDetails.setPlanCode(entity.getPlanCode()));
		Optional.ofNullable(entity.getUinNumber())
				.ifPresent((FlcCoverDetails) -> flcCoverDetails.setUinNumber(entity.getUinNumber()));
		Optional.ofNullable(entity.getRiskComDate())
				.ifPresent((FlcCoverDetails) -> flcCoverDetails.setRiskComDate(entity.getRiskComDate()));
		Optional.ofNullable(entity.getPremCessTerm())
				.ifPresent((FlcCoverDetails) -> flcCoverDetails.setPremCessTerm(entity.getPremCessTerm()));
		Optional.ofNullable(entity.getCoverPremium())
				.ifPresent((FlcCoverDetails) -> flcCoverDetails.setCoverPremium(entity.getCoverPremium()));
		Optional.ofNullable(entity.getCoverStatus())
				.ifPresent((FlcCoverDetails) -> flcCoverDetails.setCoverStatus(entity.getCoverStatus()));

		flcCoverDetailsRepository.save(flcCoverDetails);
		return errorService.getErrorById("ER002");

	}

	// Soft Delete
	@Override
	public String softdelete(Long id) {
		FlcCoverDetails flcCoverDetails = flcCoverDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FlcCoverDetails", "id", id));
		flcCoverDetails.setValidFlag(-1);
		flcCoverDetailsRepository.save(flcCoverDetails);
		return errorService.getErrorById("ER003");
	}

	// Hard Delete
	@Override
	public String harddelete(Long id) {
		FlcCoverDetails flcCoverDetails = flcCoverDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FlcCoverDetails", "id", id));
		flcCoverDetailsRepository.delete(flcCoverDetails);
		return errorService.getErrorById("ER003");
	}

	// Global Search
	@Override
	public List<FlcCoverDetailsDto> globalSearch(String key) {
		List<FlcCoverDetails> listOfFlcCoverDetails = flcCoverDetailsRepository.globalSearch(key);
		List<FlcCoverDetailsDto> listOfFlcCoverDetailsDto = listOfFlcCoverDetails.stream()
				.map(flcCoverDetails -> entityToDto.apply(flcCoverDetails)).collect(Collectors.toList());
		return listOfFlcCoverDetailsDto;
	}
}
