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

import com.leaps.dto.flc.FlcPolicyDetailsDto;
import com.leaps.entity.flc.FlcPolicyDetails;
import com.leaps.error.ErrorService;
import com.leaps.exceptions.ResourceNotFoundException;
import com.leaps.repository.flc.FlcPolicyDetailsRepository;
import com.leaps.responses.flc.FlcPolicyDetailsResponse;
import com.leaps.service.flc.FlcPolicyDetailsService;

@Service
public class FlcPolicyDetailsServiceImpl implements FlcPolicyDetailsService {

	// Autowiring Repository to use jpa methods and custom queries
	@Autowired
	private FlcPolicyDetailsRepository flcPolicyDetailsRepository;

	// Autowiring Error Service to Display the Response Messages Stored in Error
	// Table
	@Autowired
	private ErrorService errorService;

	// Entity to Dto using Function

	public Function<FlcPolicyDetails, FlcPolicyDetailsDto> entityToDto = entity -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		mapper.addMappings(new PropertyMap<FlcPolicyDetails, FlcPolicyDetailsDto>() {

			@Override
			protected void configure() {
				map().setCompanyName(source.getCompany().getCompanyName());

			}

		});
		FlcPolicyDetailsDto dto = mapper.map(entity, FlcPolicyDetailsDto.class);
		return dto;
	};

	// Dto to Entity using Function

	public Function<FlcPolicyDetailsDto, FlcPolicyDetails> dtoToEntity = dto -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		FlcPolicyDetails entity = mapper.map(dto, FlcPolicyDetails.class);
		return entity;
	};

	// Get all where valid flag=1
	@Override
	public List<FlcPolicyDetailsDto> getall() {
		List<FlcPolicyDetails> listOfFlcPolicyDetails = flcPolicyDetailsRepository.getallActive();
		List<FlcPolicyDetailsDto> listOfFlcPolicyDetailsDto = listOfFlcPolicyDetails.stream()
				.map(flcPolicyDetails -> entityToDto.apply(flcPolicyDetails)).collect(Collectors.toList());
		return listOfFlcPolicyDetailsDto;
	}

	// Get All where valid flag=1 With Pagination
	@Override
	public FlcPolicyDetailsResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir) {
		// sort Condition
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		// Create Pageable Instance
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

		Page<FlcPolicyDetails> flcPolicyDetailsPage = flcPolicyDetailsRepository.getallActivePagination(pageable);

		// get content
		List<FlcPolicyDetails> listOfFlcPolicyDetails = flcPolicyDetailsPage.getContent();

		List<FlcPolicyDetailsDto> content = listOfFlcPolicyDetails.stream()
				.map(flcPolicyDetails -> entityToDto.apply(flcPolicyDetails)).collect(Collectors.toList());

		// Setting the the values to Custom Response Created
		FlcPolicyDetailsResponse flcPolicyDetailsResponse = new FlcPolicyDetailsResponse();
		flcPolicyDetailsResponse.setContent(content);
		flcPolicyDetailsResponse.setPageNo(flcPolicyDetailsPage.getNumber());
		flcPolicyDetailsResponse.setPageSize(flcPolicyDetailsPage.getSize());
		flcPolicyDetailsResponse.setTotalElements(flcPolicyDetailsPage.getTotalElements());
		flcPolicyDetailsResponse.setTotalPages(flcPolicyDetailsPage.getTotalPages());
		flcPolicyDetailsResponse.setLast(flcPolicyDetailsPage.isLast());

		return flcPolicyDetailsResponse;
	}

	// Get Active By id
	@Override
	public FlcPolicyDetailsDto getbyid(Long id) {
		FlcPolicyDetails flcPolicyDetails = flcPolicyDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FlcPolicyDetails", "id", id));
		FlcPolicyDetailsDto flcPolicyDetailsDto = entityToDto.apply(flcPolicyDetails);
		return flcPolicyDetailsDto;
	}

	// Add
	@Override
	public String add(FlcPolicyDetailsDto dto) {
		FlcPolicyDetails flcPolicyDetails = dtoToEntity.apply(dto);
		flcPolicyDetails.setValidFlag(1);
		flcPolicyDetailsRepository.save(flcPolicyDetails);
		return errorService.getErrorById("ER001");
	}

	// Update
	@Override
	public String update(Long id, FlcPolicyDetailsDto dto) {
		FlcPolicyDetails entity = dtoToEntity.apply(dto);

		FlcPolicyDetails flcPolicyDetails = flcPolicyDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FlcPolicyDetails", "id", id));

		// Using Optional for Null check
		Optional.of(entity.getCompanyId())
				.ifPresent((DeathClaimCoverDetails) -> flcPolicyDetails.setCompanyId(entity.getCompanyId()));

		Optional.ofNullable(entity.getPolicyNo())
				.ifPresent((FlcPolicyDetails) -> flcPolicyDetails.setPolicyNo(entity.getPolicyNo()));
		Optional.ofNullable(entity.getClntNum())
				.ifPresent((FlcPolicyDetails) -> flcPolicyDetails.setClntNum(entity.getClntNum()));
		Optional.ofNullable(entity.getUinNumber())
				.ifPresent((FlcPolicyDetails) -> flcPolicyDetails.setUinNumber(entity.getUinNumber()));
		Optional.ofNullable(entity.getBillFreq())
				.ifPresent((FlcPolicyDetails) -> flcPolicyDetails.setBillFreq(entity.getBillFreq()));
		Optional.ofNullable(entity.getInstallmentPremium()).ifPresent(
				(FlcPolicyDetails) -> flcPolicyDetails.setInstallmentPremium(entity.getInstallmentPremium()));
		Optional.ofNullable(entity.getPremToDate())
				.ifPresent((FlcPolicyDetails) -> flcPolicyDetails.setPremToDate(entity.getPremToDate()));
		Optional.ofNullable(entity.getDocDate())
				.ifPresent((FlcPolicyDetails) -> flcPolicyDetails.setDocDate(entity.getDocDate()));
		Optional.ofNullable(entity.getLaAge())
				.ifPresent((FlcPolicyDetails) -> flcPolicyDetails.setLaAge(entity.getLaAge()));
		Optional.ofNullable(entity.getPhAge())
				.ifPresent((FlcPolicyDetails) -> flcPolicyDetails.setPhAge(entity.getPhAge()));
		Optional.ofNullable(entity.getStatCode())
				.ifPresent((FlcPolicyDetails) -> flcPolicyDetails.setStatCode(entity.getStatCode()));
		Optional.ofNullable(entity.getMedicalFlag())
				.ifPresent((FlcPolicyDetails) -> flcPolicyDetails.setMedicalFlag(entity.getMedicalFlag()));
		Optional.ofNullable(entity.getSmokerFlag())
				.ifPresent((FlcPolicyDetails) -> flcPolicyDetails.setSmokerFlag(entity.getSmokerFlag()));

		flcPolicyDetailsRepository.save(flcPolicyDetails);
		return errorService.getErrorById("ER002");

	}

	// Soft Delete
	@Override
	public String softdelete(Long id) {
		FlcPolicyDetails flcPolicyDetails = flcPolicyDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FlcPolicyDetails", "id", id));
		flcPolicyDetails.setValidFlag(-1);
		flcPolicyDetailsRepository.save(flcPolicyDetails);
		return errorService.getErrorById("ER003");
	}

	// Hard Delete
	@Override
	public String harddelete(Long id) {
		FlcPolicyDetails flcPolicyDetails = flcPolicyDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FlcPolicyDetails", "id", id));
		flcPolicyDetailsRepository.delete(flcPolicyDetails);
		return errorService.getErrorById("ER003");
	}

	// Global Search
	@Override
	public List<FlcPolicyDetailsDto> globalSearch(String key) {
		List<FlcPolicyDetails> listOfFlcPolicyDetails = flcPolicyDetailsRepository.globalSearch(key);
		List<FlcPolicyDetailsDto> listOfFlcPolicyDetailsDto = listOfFlcPolicyDetails.stream()
				.map(flcPolicyDetails -> entityToDto.apply(flcPolicyDetails)).collect(Collectors.toList());
		return listOfFlcPolicyDetailsDto;
	}

}
