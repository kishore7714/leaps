package com.leaps.serviceImpl.surrender;

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

import com.leaps.dto.surrender.SurrenderPolicyDetailsDto;
import com.leaps.entity.surrender.SurrenderPolicyDetails;
import com.leaps.error.ErrorService;
import com.leaps.exceptions.ResourceNotFoundException;
import com.leaps.repository.surrender.SurrenderPolicyDetailsRepository;
import com.leaps.responses.surrender.SurrenderPolicyDetailsResponse;
import com.leaps.service.surrender.SurrenderPolicyDetailsService;

@Service
public class SurrenderPolicyDetailsServiceImpl implements SurrenderPolicyDetailsService {
	// Autowiring Repository to use jpa methods and custom queries
	@Autowired
	private SurrenderPolicyDetailsRepository surrenderPolicyDetailsRepository;

	// Autowiring Error Service to Display the Response Messages Stored in Error
	// Table
	@Autowired
	private ErrorService errorService;

	// Entity to Dto using Function

	public Function<SurrenderPolicyDetails, SurrenderPolicyDetailsDto> entityToDto = entity -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		mapper.addMappings(new PropertyMap<SurrenderPolicyDetails, SurrenderPolicyDetailsDto>() {

			@Override
			protected void configure() {
				map().setCompanyName(source.getCompany().getCompanyName());
			}
		});
		SurrenderPolicyDetailsDto dto = mapper.map(entity, SurrenderPolicyDetailsDto.class);
		return dto;
	};

	// Dto to Entity using Function

	public Function<SurrenderPolicyDetailsDto, SurrenderPolicyDetails> dtoToEntity = dto -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		SurrenderPolicyDetails entity = mapper.map(dto, SurrenderPolicyDetails.class);
		return entity;
	};

	// Get all where valid flag=1
	@Override
	public List<SurrenderPolicyDetailsDto> getall() {
		List<SurrenderPolicyDetails> listOfSurrenderPolicyDetails = surrenderPolicyDetailsRepository.getallActive();
		List<SurrenderPolicyDetailsDto> listOfSurrenderPolicyDetailsDto = listOfSurrenderPolicyDetails.stream()
				.map(surrenderPolicyDetails -> entityToDto.apply(surrenderPolicyDetails)).collect(Collectors.toList());
		return listOfSurrenderPolicyDetailsDto;
	}

	// Get All where valid flag=1 With Pagination
	@Override
	public SurrenderPolicyDetailsResponse getAllWithPagination(int pageNo, int pageSize, String sortBy,
			String sortDir) {
		// sort Condition
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		// Create Pageable Instance
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

		Page<SurrenderPolicyDetails> surrenderPolicyDetailsPage = surrenderPolicyDetailsRepository
				.getallActivePagination(pageable);

		// get content
		List<SurrenderPolicyDetails> listOfSurrenderPolicyDetails = surrenderPolicyDetailsPage.getContent();

		List<SurrenderPolicyDetailsDto> content = listOfSurrenderPolicyDetails.stream()
				.map(surrenderPolicyDetails -> entityToDto.apply(surrenderPolicyDetails)).collect(Collectors.toList());

		// Setting the the values to Custom Response Created
		SurrenderPolicyDetailsResponse surrenderPolicyDetailsResponse = new SurrenderPolicyDetailsResponse();
		surrenderPolicyDetailsResponse.setContent(content);
		surrenderPolicyDetailsResponse.setPageNo(surrenderPolicyDetailsPage.getNumber());
		surrenderPolicyDetailsResponse.setPageSize(surrenderPolicyDetailsPage.getSize());
		surrenderPolicyDetailsResponse.setTotalElements(surrenderPolicyDetailsPage.getTotalElements());
		surrenderPolicyDetailsResponse.setTotalPages(surrenderPolicyDetailsPage.getTotalPages());
		surrenderPolicyDetailsResponse.setLast(surrenderPolicyDetailsPage.isLast());

		return surrenderPolicyDetailsResponse;
	}

	// Get Active By id
	@Override
	public SurrenderPolicyDetailsDto getbyid(Long id) {
		SurrenderPolicyDetails surrenderPolicyDetails = surrenderPolicyDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SurrenderPolicyDetails", "id", id));
		SurrenderPolicyDetailsDto surrenderPolicyDetailsDto = entityToDto.apply(surrenderPolicyDetails);
		return surrenderPolicyDetailsDto;
	}

	// Add
	@Override
	public String add(SurrenderPolicyDetailsDto dto) {
		SurrenderPolicyDetails surrenderPolicyDetails = dtoToEntity.apply(dto);
		surrenderPolicyDetails.setValidFlag(1);
		surrenderPolicyDetailsRepository.save(surrenderPolicyDetails);
		return errorService.getErrorById("ER001");
	}

	// Update
	@Override
	public String update(Long id, SurrenderPolicyDetailsDto dto) {
		SurrenderPolicyDetails entity = dtoToEntity.apply(dto);

		SurrenderPolicyDetails surrenderPolicyDetails = surrenderPolicyDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SurrenderPolicyDetails", "id", id));

		// Using Optional for Null check
		Optional.of(entity.getCompanyId())
				.ifPresent((DeathClaimCoverDetails) -> surrenderPolicyDetails.setCompanyId(entity.getCompanyId()));

		Optional.ofNullable(entity.getPolicyNo())
				.ifPresent((SurrenderPolicyDetails) -> surrenderPolicyDetails.setPolicyNo(entity.getPolicyNo()));
		Optional.ofNullable(entity.getClntNum())
				.ifPresent((SurrenderPolicyDetails) -> surrenderPolicyDetails.setClntNum(entity.getClntNum()));
		Optional.ofNullable(entity.getPolicyNo())
				.ifPresent((SurrenderPolicyDetails) -> surrenderPolicyDetails.setPolicyNo(entity.getPolicyNo()));
		Optional.ofNullable(entity.getUinNumber())
				.ifPresent((SurrenderPolicyDetails) -> surrenderPolicyDetails.setUinNumber(entity.getUinNumber()));
		Optional.ofNullable(entity.getBillFreq())
				.ifPresent((SurrenderPolicyDetails) -> surrenderPolicyDetails.setBillFreq(entity.getBillFreq()));
		Optional.ofNullable(entity.getInstallmentPremium()).ifPresent((SurrenderPolicyDetails) -> surrenderPolicyDetails
				.setInstallmentPremium(entity.getInstallmentPremium()));
		Optional.ofNullable(entity.getExtraPremium()).ifPresent(
				(SurrenderPolicyDetails) -> surrenderPolicyDetails.setExtraPremium(entity.getExtraPremium()));
		Optional.ofNullable(entity.getFup())
				.ifPresent((SurrenderPolicyDetails) -> surrenderPolicyDetails.setFup(entity.getFup()));
		Optional.ofNullable(entity.getDocDate())
				.ifPresent((SurrenderPolicyDetails) -> surrenderPolicyDetails.setDocDate(entity.getDocDate()));
		Optional.ofNullable(entity.getLaAge())
				.ifPresent((SurrenderPolicyDetails) -> surrenderPolicyDetails.setLaAge(entity.getLaAge()));
		Optional.ofNullable(entity.getPhAge())
				.ifPresent((SurrenderPolicyDetails) -> surrenderPolicyDetails.setPhAge(entity.getPhAge()));
		Optional.ofNullable(entity.getStatusCode())
				.ifPresent((SurrenderPolicyDetails) -> surrenderPolicyDetails.setStatusCode(entity.getStatusCode()));
		Optional.ofNullable(entity.getSmokerFlag())
				.ifPresent((SurrenderPolicyDetails) -> surrenderPolicyDetails.setSmokerFlag(entity.getSmokerFlag()));

		surrenderPolicyDetailsRepository.save(surrenderPolicyDetails);
		return errorService.getErrorById("ER002");

	}

	// Soft Delete
	@Override
	public String softdelete(Long id) {
		SurrenderPolicyDetails surrenderPolicyDetails = surrenderPolicyDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SurrenderPolicyDetails", "id", id));
		surrenderPolicyDetails.setValidFlag(-1);
		surrenderPolicyDetailsRepository.save(surrenderPolicyDetails);
		return errorService.getErrorById("ER003");
	}

	// Hard Delete
	@Override
	public String harddelete(Long id) {
		SurrenderPolicyDetails surrenderPolicyDetails = surrenderPolicyDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SurrenderPolicyDetails", "id", id));
		surrenderPolicyDetailsRepository.delete(surrenderPolicyDetails);
		return errorService.getErrorById("ER003");
	}

	// Global Search
	@Override
	public List<SurrenderPolicyDetailsDto> globalSearch(String key) {
		List<SurrenderPolicyDetails> listOfSurrenderPolicyDetails = surrenderPolicyDetailsRepository.globalSearch(key);
		List<SurrenderPolicyDetailsDto> listOfSurrenderPolicyDetailsDto = listOfSurrenderPolicyDetails.stream()
				.map(surrenderPolicyDetails -> entityToDto.apply(surrenderPolicyDetails)).collect(Collectors.toList());
		return listOfSurrenderPolicyDetailsDto;
	}
}
