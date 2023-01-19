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

import com.leaps.dto.flc.FlcLeapDetailsDto;
import com.leaps.entity.flc.FlcLeapDetails;
import com.leaps.error.ErrorService;
import com.leaps.exceptions.ResourceNotFoundException;
import com.leaps.repository.flc.FlcLeapDetailsRepository;
import com.leaps.responses.flc.FlcLeapDetailsResponse;
import com.leaps.service.flc.FlcLeapDetailsService;

@Service
public class FlcLeapDetailsServiceImpl implements FlcLeapDetailsService {
	// Autowiring Repository to use jpa methods and custom queries
	@Autowired
	private FlcLeapDetailsRepository flcLeapDetailsRepository;

	// Autowiring Error Service to Display the Response Messages Stored in Error
	// Table
	@Autowired
	private ErrorService errorService;

	// Entity to Dto using Function

	public Function<FlcLeapDetails, FlcLeapDetailsDto> entityToDto = entity -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		mapper.addMappings(new PropertyMap<FlcLeapDetails, FlcLeapDetailsDto>() {

			@Override
			protected void configure() {
				map().setCompanyName(source.getCompany().getCompanyName());

			}

		});
		FlcLeapDetailsDto dto = mapper.map(entity, FlcLeapDetailsDto.class);
		return dto;
	};

	// Dto to Entity using Function

	public Function<FlcLeapDetailsDto, FlcLeapDetails> dtoToEntity = dto -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		FlcLeapDetails entity = mapper.map(dto, FlcLeapDetails.class);
		return entity;
	};

	// Get all where valid flag=1
	@Override
	public List<FlcLeapDetailsDto> getall() {
		List<FlcLeapDetails> listOfFlcLeapDetails = flcLeapDetailsRepository.getallActive();
		List<FlcLeapDetailsDto> listOfFlcLeapDetailsDto = listOfFlcLeapDetails.stream()
				.map(flcLeapDetails -> entityToDto.apply(flcLeapDetails)).collect(Collectors.toList());
		return listOfFlcLeapDetailsDto;
	}

	// Get All where valid flag=1 With Pagination
	@Override
	public FlcLeapDetailsResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir) {
		// sort Condition
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		// Create Pageable Instance
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

		Page<FlcLeapDetails> flcLeapDetailsPage = flcLeapDetailsRepository.getallActivePagination(pageable);

		// get content
		List<FlcLeapDetails> listOfFlcLeapDetails = flcLeapDetailsPage.getContent();

		List<FlcLeapDetailsDto> content = listOfFlcLeapDetails.stream()
				.map(flcLeapDetails -> entityToDto.apply(flcLeapDetails)).collect(Collectors.toList());

		// Setting the the values to Custom Response Created
		FlcLeapDetailsResponse flcLeapDetailsResponse = new FlcLeapDetailsResponse();
		flcLeapDetailsResponse.setContent(content);
		flcLeapDetailsResponse.setPageNo(flcLeapDetailsPage.getNumber());
		flcLeapDetailsResponse.setPageSize(flcLeapDetailsPage.getSize());
		flcLeapDetailsResponse.setTotalElements(flcLeapDetailsPage.getTotalElements());
		flcLeapDetailsResponse.setTotalPages(flcLeapDetailsPage.getTotalPages());
		flcLeapDetailsResponse.setLast(flcLeapDetailsPage.isLast());

		return flcLeapDetailsResponse;
	}

	// Get Active By id
	@Override
	public FlcLeapDetailsDto getbyid(Long id) {
		FlcLeapDetails flcLeapDetails = flcLeapDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FlcLeapDetails", "id", id));
		FlcLeapDetailsDto flcLeapDetailsDto = entityToDto.apply(flcLeapDetails);
		return flcLeapDetailsDto;
	}

	// Add
	@Override
	public String add(FlcLeapDetailsDto dto) {
		FlcLeapDetails flcLeapDetails = dtoToEntity.apply(dto);
		flcLeapDetails.setValidFlag(1);
		flcLeapDetailsRepository.save(flcLeapDetails);
		return errorService.getErrorById("ER001");
	}

	// Update
	@Override
	public String update(Long id, FlcLeapDetailsDto dto) {
		FlcLeapDetails entity = dtoToEntity.apply(dto);

		FlcLeapDetails flcLeapDetails = flcLeapDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FlcLeapDetails", "id", id));

		// Using Optional for Null check
		Optional.of(entity.getCompanyId())
				.ifPresent((DeathClaimCoverDetails) -> flcLeapDetails.setCompanyId(entity.getCompanyId()));

		Optional.ofNullable(entity.getPolicyNo())
				.ifPresent((FlcLeapDetails) -> flcLeapDetails.setPolicyNo(entity.getPolicyNo()));
		Optional.ofNullable(entity.getTranDate())
				.ifPresent((FlcLeapDetails) -> flcLeapDetails.setTranDate(entity.getTranDate()));
		Optional.ofNullable(entity.getTranNo())
				.ifPresent((FlcLeapDetails) -> flcLeapDetails.setTranNo(entity.getTranNo()));
		Optional.ofNullable(entity.getUinNumber())
				.ifPresent((FlcLeapDetails) -> flcLeapDetails.setUinNumber(entity.getUinNumber()));
		Optional.ofNullable(entity.getTotalPremium())
				.ifPresent((FlcLeapDetails) -> flcLeapDetails.setTotalPremium(entity.getTotalPremium()));
		Optional.ofNullable(entity.getAvalSuspense())
				.ifPresent((FlcLeapDetails) -> flcLeapDetails.setAvalSuspense(entity.getAvalSuspense()));
		Optional.ofNullable(entity.getPenalInterest())
				.ifPresent((FlcLeapDetails) -> flcLeapDetails.setPenalInterest(entity.getPenalInterest()));
		Optional.ofNullable(entity.getMedicalFee())
				.ifPresent((FlcLeapDetails) -> flcLeapDetails.setMedicalFee(entity.getMedicalFee()));
		Optional.ofNullable(entity.getStampDuty())
				.ifPresent((FlcLeapDetails) -> flcLeapDetails.setStampDuty(entity.getStampDuty()));
		Optional.ofNullable(entity.getMortCharge())
				.ifPresent((FlcLeapDetails) -> flcLeapDetails.setMortCharge(entity.getMortCharge()));
		Optional.ofNullable(entity.getGrossPayable())
				.ifPresent((FlcLeapDetails) -> flcLeapDetails.setGrossPayable(entity.getGrossPayable()));
		Optional.ofNullable(entity.getRecoveries())
				.ifPresent((FlcLeapDetails) -> flcLeapDetails.setRecoveries(entity.getRecoveries()));
		Optional.ofNullable(entity.getNetPayable())
				.ifPresent((FlcLeapDetails) -> flcLeapDetails.setNetPayable(entity.getNetPayable()));
		Optional.ofNullable(entity.getEffDate())
				.ifPresent((FlcLeapDetails) -> flcLeapDetails.setEffDate(entity.getEffDate()));
		Optional.ofNullable(entity.getFundValue())
				.ifPresent((FlcLeapDetails) -> flcLeapDetails.setFundValue(entity.getFundValue()));
		Optional.ofNullable(entity.getPfFlag())
				.ifPresent((FlcLeapDetails) -> flcLeapDetails.setPfFlag(entity.getPfFlag()));
		Optional.ofNullable(entity.getPfRemarks())
				.ifPresent((FlcLeapDetails) -> flcLeapDetails.setPfRemarks(entity.getPfRemarks()));
		Optional.ofNullable(entity.getApprovFlag())
				.ifPresent((FlcLeapDetails) -> flcLeapDetails.setApprovFlag(entity.getApprovFlag()));
		Optional.ofNullable(entity.getApprovRemarks())
				.ifPresent((FlcLeapDetails) -> flcLeapDetails.setApprovRemarks(entity.getApprovRemarks()));
		Optional.ofNullable(entity.getPfFlagUpdate())
				.ifPresent((FlcLeapDetails) -> flcLeapDetails.setPfFlagUpdate(entity.getPfFlagUpdate()));

		flcLeapDetailsRepository.save(flcLeapDetails);
		return errorService.getErrorById("ER002");

	}

	// Soft Delete
	@Override
	public String softdelete(Long id) {
		FlcLeapDetails flcLeapDetails = flcLeapDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FlcLeapDetails", "id", id));
		flcLeapDetails.setValidFlag(-1);
		flcLeapDetailsRepository.save(flcLeapDetails);
		return errorService.getErrorById("ER003");
	}

	// Hard Delete
	@Override
	public String harddelete(Long id) {
		FlcLeapDetails flcLeapDetails = flcLeapDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FlcLeapDetails", "id", id));
		flcLeapDetailsRepository.delete(flcLeapDetails);
		return errorService.getErrorById("ER003");
	}

	// Global Search
	@Override
	public List<FlcLeapDetailsDto> globalSearch(String key) {
		List<FlcLeapDetails> listOfFlcLeapDetails = flcLeapDetailsRepository.globalSearch(key);
		List<FlcLeapDetailsDto> listOfFlcLeapDetailsDto = listOfFlcLeapDetails.stream()
				.map(flcLeapDetails -> entityToDto.apply(flcLeapDetails)).collect(Collectors.toList());
		return listOfFlcLeapDetailsDto;
	}
}
