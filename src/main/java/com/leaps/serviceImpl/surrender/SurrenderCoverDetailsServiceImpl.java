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

import com.leaps.dto.surrender.SurrenderCoverDetailsDto;
import com.leaps.entity.surrender.SurrenderCoverDetails;
import com.leaps.error.ErrorService;
import com.leaps.exceptions.ResourceNotFoundException;
import com.leaps.repository.surrender.SurrenderCoverDetailsRepository;
import com.leaps.responses.surrender.SurrenderCoverDetailsResponse;
import com.leaps.service.surrender.SurrenderCoverDetailsService;

@Service
public class SurrenderCoverDetailsServiceImpl implements SurrenderCoverDetailsService {

	// Autowiring Repository to use jpa methods and custom queries
	@Autowired
	private SurrenderCoverDetailsRepository surrenderCoverDetailsRepository;

	// Autowiring Error Service to Display the Response Messages Stored in Error
	// Table
	@Autowired
	private ErrorService errorService;

	// Entity to Dto using Function

	public Function<SurrenderCoverDetails, SurrenderCoverDetailsDto> entityToDto = entity -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		mapper.addMappings(new PropertyMap<SurrenderCoverDetails, SurrenderCoverDetailsDto>() {

			@Override
			protected void configure() {
				map().setCompanyName(source.getCompany().getCompanyName());
			}
		});
		SurrenderCoverDetailsDto dto = mapper.map(entity, SurrenderCoverDetailsDto.class);
		return dto;
	};

	// Dto to Entity using Function

	public Function<SurrenderCoverDetailsDto, SurrenderCoverDetails> dtoToEntity = dto -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		SurrenderCoverDetails entity = mapper.map(dto, SurrenderCoverDetails.class);
		return entity;
	};

	// Get all where valid flag=1
	@Override
	public List<SurrenderCoverDetailsDto> getall() {
		List<SurrenderCoverDetails> listOfSurrenderCoverDetails = surrenderCoverDetailsRepository.getallActive();
		List<SurrenderCoverDetailsDto> listOfSurrenderCoverDetailsDto = listOfSurrenderCoverDetails.stream()
				.map(surrenderCoverDetails -> entityToDto.apply(surrenderCoverDetails)).collect(Collectors.toList());
		return listOfSurrenderCoverDetailsDto;
	}

	// Get All where valid flag=1 With Pagination
	@Override
	public SurrenderCoverDetailsResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir) {
		// sort Condition
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		// Create Pageable Instance
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

		Page<SurrenderCoverDetails> surrenderCoverDetailsPage = surrenderCoverDetailsRepository
				.getallActivePagination(pageable);

		// get content
		List<SurrenderCoverDetails> listOfSurrenderCoverDetails = surrenderCoverDetailsPage.getContent();

		List<SurrenderCoverDetailsDto> content = listOfSurrenderCoverDetails.stream()
				.map(surrenderCoverDetails -> entityToDto.apply(surrenderCoverDetails)).collect(Collectors.toList());

		// Setting the the values to Custom Response Created
		SurrenderCoverDetailsResponse surrenderCoverDetailsResponse = new SurrenderCoverDetailsResponse();
		surrenderCoverDetailsResponse.setContent(content);
		surrenderCoverDetailsResponse.setPageNo(surrenderCoverDetailsPage.getNumber());
		surrenderCoverDetailsResponse.setPageSize(surrenderCoverDetailsPage.getSize());
		surrenderCoverDetailsResponse.setTotalElements(surrenderCoverDetailsPage.getTotalElements());
		surrenderCoverDetailsResponse.setTotalPages(surrenderCoverDetailsPage.getTotalPages());
		surrenderCoverDetailsResponse.setLast(surrenderCoverDetailsPage.isLast());

		return surrenderCoverDetailsResponse;
	}

	// Get Active By id
	@Override
	public SurrenderCoverDetailsDto getbyid(Long id) {
		SurrenderCoverDetails surrenderCoverDetails = surrenderCoverDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SurrenderCoverDetails", "id", id));
		SurrenderCoverDetailsDto surrenderCoverDetailsDto = entityToDto.apply(surrenderCoverDetails);
		return surrenderCoverDetailsDto;
	}

	// Add
	@Override
	public String add(SurrenderCoverDetailsDto dto) {
		SurrenderCoverDetails surrenderCoverDetails = dtoToEntity.apply(dto);
		surrenderCoverDetails.setValidFlag(1);
		surrenderCoverDetailsRepository.save(surrenderCoverDetails);
		return errorService.getErrorById("ER001");
	}

	// Update
	@Override
	public String update(Long id, SurrenderCoverDetailsDto dto) {
		SurrenderCoverDetails entity = dtoToEntity.apply(dto);

		SurrenderCoverDetails surrenderCoverDetails = surrenderCoverDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SurrenderCoverDetails", "id", id));

		// Using Optional for Null check
		Optional.of(entity.getCompanyId())
				.ifPresent((DeathClaimCoverDetails) -> surrenderCoverDetails.setCompanyId(entity.getCompanyId()));

		Optional.ofNullable(entity.getClntNum())
				.ifPresent((SurrenderCoverDetails) -> surrenderCoverDetails.setClntNum(entity.getClntNum()));
		Optional.ofNullable(entity.getPolicyNo())
				.ifPresent((SurrenderCoverDetails) -> surrenderCoverDetails.setPolicyNo(entity.getPolicyNo()));
		Optional.ofNullable(entity.getUinNumber())
				.ifPresent((SurrenderCoverDetails) -> surrenderCoverDetails.setUinNumber(entity.getUinNumber()));
		Optional.ofNullable(entity.getPlanName())
				.ifPresent((SurrenderCoverDetails) -> surrenderCoverDetails.setPlanName(entity.getPlanName()));
		Optional.ofNullable(entity.getPlanCode())
				.ifPresent((SurrenderCoverDetails) -> surrenderCoverDetails.setPlanCode(entity.getPlanCode()));
		Optional.ofNullable(entity.getRiskComDate())
				.ifPresent((SurrenderCoverDetails) -> surrenderCoverDetails.setRiskComDate(entity.getRiskComDate()));
		Optional.ofNullable(entity.getDocDate())
				.ifPresent((SurrenderCoverDetails) -> surrenderCoverDetails.setDocDate(entity.getDocDate()));
		Optional.ofNullable(entity.getSumAssured())
				.ifPresent((SurrenderCoverDetails) -> surrenderCoverDetails.setSumAssured(entity.getSumAssured()));
		Optional.ofNullable(entity.getPremiumTerm())
				.ifPresent((SurrenderCoverDetails) -> surrenderCoverDetails.setPremiumTerm(entity.getPremiumTerm()));
		Optional.ofNullable(entity.getPolicyTerm())
				.ifPresent((SurrenderCoverDetails) -> surrenderCoverDetails.setPolicyTerm(entity.getPolicyTerm()));
		Optional.ofNullable(entity.getCoverPremium())
				.ifPresent((SurrenderCoverDetails) -> surrenderCoverDetails.setCoverPremium(entity.getCoverPremium()));
		Optional.ofNullable(entity.getCoverStatus())
				.ifPresent((SurrenderCoverDetails) -> surrenderCoverDetails.setCoverStatus(entity.getCoverStatus()));
		surrenderCoverDetailsRepository.save(surrenderCoverDetails);
		return errorService.getErrorById("ER002");

	}

	// Soft Delete
	@Override
	public String softdelete(Long id) {
		SurrenderCoverDetails surrenderCoverDetails = surrenderCoverDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SurrenderCoverDetails", "id", id));
		surrenderCoverDetails.setValidFlag(-1);
		surrenderCoverDetailsRepository.save(surrenderCoverDetails);
		return errorService.getErrorById("ER003");
	}

	// Hard Delete
	@Override
	public String harddelete(Long id) {
		SurrenderCoverDetails surrenderCoverDetails = surrenderCoverDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SurrenderCoverDetails", "id", id));
		surrenderCoverDetailsRepository.delete(surrenderCoverDetails);
		return errorService.getErrorById("ER003");
	}

	// Global Search
	@Override
	public List<SurrenderCoverDetailsDto> globalSearch(String key) {
		List<SurrenderCoverDetails> listOfSurrenderCoverDetails = surrenderCoverDetailsRepository.globalSearch(key);
		List<SurrenderCoverDetailsDto> listOfSurrenderCoverDetailsDto = listOfSurrenderCoverDetails.stream()
				.map(surrenderCoverDetails -> entityToDto.apply(surrenderCoverDetails)).collect(Collectors.toList());
		return listOfSurrenderCoverDetailsDto;
	}
}
