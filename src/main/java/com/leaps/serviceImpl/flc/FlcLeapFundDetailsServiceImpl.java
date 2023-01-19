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

import com.leaps.dto.flc.FlcLeapFundDetailsDto;
import com.leaps.entity.flc.FlcLeapFundDetails;
import com.leaps.error.ErrorService;
import com.leaps.exceptions.ResourceNotFoundException;
import com.leaps.repository.flc.FlcLeapFundDetailsRepository;
import com.leaps.responses.flc.FlcLeapFundDetailsResponse;
import com.leaps.service.flc.FlcLeapFundDetailsService;

@Service
public class FlcLeapFundDetailsServiceImpl implements FlcLeapFundDetailsService {

	// Autowiring Repository to use jpa methods and custom queries
	@Autowired
	private FlcLeapFundDetailsRepository flcLeapFundDetailsRepository;

	// Autowiring Error Service to Display the Response Messages Stored in Error
	// Table
	@Autowired
	private ErrorService errorService;

	// Entity to Dto using Function

	public Function<FlcLeapFundDetails, FlcLeapFundDetailsDto> entityToDto = entity -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		mapper.addMappings(new PropertyMap<FlcLeapFundDetails, FlcLeapFundDetailsDto>() {

			@Override
			protected void configure() {
				map().setCompanyName(source.getCompany().getCompanyName());

			}

		});
		FlcLeapFundDetailsDto dto = mapper.map(entity, FlcLeapFundDetailsDto.class);
		return dto;
	};

	// Dto to Entity using Function

	public Function<FlcLeapFundDetailsDto, FlcLeapFundDetails> dtoToEntity = dto -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		FlcLeapFundDetails entity = mapper.map(dto, FlcLeapFundDetails.class);
		return entity;
	};

	// Get all where valid flag=1
	@Override
	public List<FlcLeapFundDetailsDto> getall() {
		List<FlcLeapFundDetails> listOfFlcLeapFundDetails = flcLeapFundDetailsRepository.getallActive();
		List<FlcLeapFundDetailsDto> listOfFlcLeapFundDetailsDto = listOfFlcLeapFundDetails.stream()
				.map(flcLeapFundDetails -> entityToDto.apply(flcLeapFundDetails)).collect(Collectors.toList());
		return listOfFlcLeapFundDetailsDto;
	}

	// Get All where valid flag=1 With Pagination
	@Override
	public FlcLeapFundDetailsResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir) {
		// sort Condition
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		// Create Pageable Instance
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

		Page<FlcLeapFundDetails> flcLeapFundDetailsPage = flcLeapFundDetailsRepository.getallActivePagination(pageable);

		// get content
		List<FlcLeapFundDetails> listOfFlcLeapFundDetails = flcLeapFundDetailsPage.getContent();

		List<FlcLeapFundDetailsDto> content = listOfFlcLeapFundDetails.stream()
				.map(flcLeapFundDetails -> entityToDto.apply(flcLeapFundDetails)).collect(Collectors.toList());

		// Setting the the values to Custom Response Created
		FlcLeapFundDetailsResponse flcLeapFundDetailsResponse = new FlcLeapFundDetailsResponse();
		flcLeapFundDetailsResponse.setContent(content);
		flcLeapFundDetailsResponse.setPageNo(flcLeapFundDetailsPage.getNumber());
		flcLeapFundDetailsResponse.setPageSize(flcLeapFundDetailsPage.getSize());
		flcLeapFundDetailsResponse.setTotalElements(flcLeapFundDetailsPage.getTotalElements());
		flcLeapFundDetailsResponse.setTotalPages(flcLeapFundDetailsPage.getTotalPages());
		flcLeapFundDetailsResponse.setLast(flcLeapFundDetailsPage.isLast());

		return flcLeapFundDetailsResponse;
	}

	// Get Active By id
	@Override
	public FlcLeapFundDetailsDto getbyid(Long id) {
		FlcLeapFundDetails flcLeapFundDetails = flcLeapFundDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FlcLeapFundDetails", "id", id));
		FlcLeapFundDetailsDto flcLeapFundDetailsDto = entityToDto.apply(flcLeapFundDetails);
		return flcLeapFundDetailsDto;
	}

	// Add
	@Override
	public String add(FlcLeapFundDetailsDto dto) {
		FlcLeapFundDetails flcLeapFundDetails = dtoToEntity.apply(dto);
		flcLeapFundDetails.setValidFlag(1);
		flcLeapFundDetailsRepository.save(flcLeapFundDetails);
		return errorService.getErrorById("ER001");
	}

	// Update
	@Override
	public String update(Long id, FlcLeapFundDetailsDto dto) {
		FlcLeapFundDetails entity = dtoToEntity.apply(dto);

		FlcLeapFundDetails flcLeapFundDetails = flcLeapFundDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FlcLeapFundDetails", "id", id));

		// Using Optional for Null check
		Optional.of(entity.getCompanyId())
				.ifPresent((DeathClaimCoverDetails) -> flcLeapFundDetails.setCompanyId(entity.getCompanyId()));

		Optional.ofNullable(entity.getPolicyNo())
				.ifPresent((FlcLeapFundDetails) -> flcLeapFundDetails.setPolicyNo(entity.getPolicyNo()));
		Optional.ofNullable(entity.getLeapFundCode())
				.ifPresent((FlcLeapFundDetails) -> flcLeapFundDetails.setLeapFundCode(entity.getLeapFundCode()));
		Optional.ofNullable(entity.getLeapFundName())
				.ifPresent((FlcLeapFundDetails) -> flcLeapFundDetails.setLeapFundName(entity.getLeapFundName()));
		Optional.ofNullable(entity.getLeapNavDate())
				.ifPresent((FlcLeapFundDetails) -> flcLeapFundDetails.setLeapNavDate(entity.getLeapNavDate()));
		Optional.ofNullable(entity.getLeapRateApp())
				.ifPresent((FlcLeapFundDetails) -> flcLeapFundDetails.setLeapRateApp(entity.getLeapRateApp()));
		Optional.ofNullable(entity.getLeapUnits())
				.ifPresent((FlcLeapFundDetails) -> flcLeapFundDetails.setLeapUnits(entity.getLeapUnits()));
		Optional.ofNullable(entity.getLeapFundValue())
				.ifPresent((FlcLeapFundDetails) -> flcLeapFundDetails.setLeapFundValue(entity.getLeapFundValue()));
		Optional.ofNullable(entity.getRemark())
				.ifPresent((FlcLeapFundDetails) -> flcLeapFundDetails.setRemark(entity.getRemark()));

		flcLeapFundDetailsRepository.save(flcLeapFundDetails);
		return errorService.getErrorById("ER002");

	}

	// Soft Delete
	@Override
	public String softdelete(Long id) {
		FlcLeapFundDetails flcLeapFundDetails = flcLeapFundDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FlcLeapFundDetails", "id", id));
		flcLeapFundDetails.setValidFlag(-1);
		flcLeapFundDetailsRepository.save(flcLeapFundDetails);
		return errorService.getErrorById("ER003");
	}

	// Hard Delete
	@Override
	public String harddelete(Long id) {
		FlcLeapFundDetails flcLeapFundDetails = flcLeapFundDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FlcLeapFundDetails", "id", id));
		flcLeapFundDetailsRepository.delete(flcLeapFundDetails);
		return errorService.getErrorById("ER003");
	}

	// Global Search
	@Override
	public List<FlcLeapFundDetailsDto> globalSearch(String key) {
		List<FlcLeapFundDetails> listOfFlcLeapFundDetails = flcLeapFundDetailsRepository.globalSearch(key);
		List<FlcLeapFundDetailsDto> listOfFlcLeapFundDetailsDto = listOfFlcLeapFundDetails.stream()
				.map(flcLeapFundDetails -> entityToDto.apply(flcLeapFundDetails)).collect(Collectors.toList());
		return listOfFlcLeapFundDetailsDto;
	}
}
