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

import com.leaps.dto.surrender.SurrenderLeapFundDetailsDto;
import com.leaps.entity.surrender.SurrenderLeapFundDetails;
import com.leaps.error.ErrorService;
import com.leaps.exceptions.ResourceNotFoundException;
import com.leaps.repository.surrender.SurrenderLeapFundDetailsRepository;
import com.leaps.responses.surrender.SurrenderLeapFundDetailsResponse;
import com.leaps.service.surrender.SurrenderLeapFundDetailsService;

@Service
public class SurrenderLeapFundDetailsServiceImpl implements SurrenderLeapFundDetailsService {
	// Autowiring Repository to use jpa methods and custom queries
	@Autowired
	private SurrenderLeapFundDetailsRepository surrenderLeapFundDetailsRepository;

	// Autowiring Error Service to Display the Response Messages Stored in Error
	// Table
	@Autowired
	private ErrorService errorService;

	// Entity to Dto using Function

	public Function<SurrenderLeapFundDetails, SurrenderLeapFundDetailsDto> entityToDto = entity -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		mapper.addMappings(new PropertyMap<SurrenderLeapFundDetails, SurrenderLeapFundDetailsDto>() {

			@Override
			protected void configure() {
				map().setCompanyName(source.getCompany().getCompanyName());
			}
		});
		SurrenderLeapFundDetailsDto dto = mapper.map(entity, SurrenderLeapFundDetailsDto.class);
		return dto;
	};

	// Dto to Entity using Function

	public Function<SurrenderLeapFundDetailsDto, SurrenderLeapFundDetails> dtoToEntity = dto -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		SurrenderLeapFundDetails entity = mapper.map(dto, SurrenderLeapFundDetails.class);
		return entity;
	};

	// Get all where valid flag=1
	@Override
	public List<SurrenderLeapFundDetailsDto> getall() {
		List<SurrenderLeapFundDetails> listOfSurrenderLeapFundDetails = surrenderLeapFundDetailsRepository
				.getallActive();
		List<SurrenderLeapFundDetailsDto> listOfSurrenderLeapFundDetailsDto = listOfSurrenderLeapFundDetails.stream()
				.map(surrenderLeapFundDetails -> entityToDto.apply(surrenderLeapFundDetails))
				.collect(Collectors.toList());
		return listOfSurrenderLeapFundDetailsDto;
	}

	// Get All where valid flag=1 With Pagination
	@Override
	public SurrenderLeapFundDetailsResponse getAllWithPagination(int pageNo, int pageSize, String sortBy,
			String sortDir) {
		// sort Condition
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		// Create Pageable Instance
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

		Page<SurrenderLeapFundDetails> surrenderLeapFundDetailsPage = surrenderLeapFundDetailsRepository
				.getallActivePagination(pageable);

		// get content
		List<SurrenderLeapFundDetails> listOfSurrenderLeapFundDetails = surrenderLeapFundDetailsPage.getContent();

		List<SurrenderLeapFundDetailsDto> content = listOfSurrenderLeapFundDetails.stream()
				.map(surrenderLeapFundDetails -> entityToDto.apply(surrenderLeapFundDetails))
				.collect(Collectors.toList());

		// Setting the the values to Custom Response Created
		SurrenderLeapFundDetailsResponse surrenderLeapFundDetailsResponse = new SurrenderLeapFundDetailsResponse();
		surrenderLeapFundDetailsResponse.setContent(content);
		surrenderLeapFundDetailsResponse.setPageNo(surrenderLeapFundDetailsPage.getNumber());
		surrenderLeapFundDetailsResponse.setPageSize(surrenderLeapFundDetailsPage.getSize());
		surrenderLeapFundDetailsResponse.setTotalElements(surrenderLeapFundDetailsPage.getTotalElements());
		surrenderLeapFundDetailsResponse.setTotalPages(surrenderLeapFundDetailsPage.getTotalPages());
		surrenderLeapFundDetailsResponse.setLast(surrenderLeapFundDetailsPage.isLast());

		return surrenderLeapFundDetailsResponse;
	}

	// Get Active By id
	@Override
	public SurrenderLeapFundDetailsDto getbyid(Long id) {
		SurrenderLeapFundDetails surrenderLeapFundDetails = surrenderLeapFundDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SurrenderLeapFundDetails", "id", id));
		SurrenderLeapFundDetailsDto surrenderLeapFundDetailsDto = entityToDto.apply(surrenderLeapFundDetails);
		return surrenderLeapFundDetailsDto;
	}

	// Add
	@Override
	public String add(SurrenderLeapFundDetailsDto dto) {
		SurrenderLeapFundDetails surrenderLeapFundDetails = dtoToEntity.apply(dto);
		surrenderLeapFundDetails.setValidFlag(1);
		surrenderLeapFundDetailsRepository.save(surrenderLeapFundDetails);
		return errorService.getErrorById("ER001");
	}

	// Update
	@Override
	public String update(Long id, SurrenderLeapFundDetailsDto dto) {
		SurrenderLeapFundDetails entity = dtoToEntity.apply(dto);

		SurrenderLeapFundDetails surrenderLeapFundDetails = surrenderLeapFundDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SurrenderLeapFundDetails", "id", id));

		// Using Optional for Null check
		Optional.of(entity.getCompanyId())
				.ifPresent((DeathClaimCoverDetails) -> surrenderLeapFundDetails.setCompanyId(entity.getCompanyId()));

		Optional.ofNullable(entity.getPolicyNo())
				.ifPresent((SurrenderLeapFundDetails) -> surrenderLeapFundDetails.setPolicyNo(entity.getPolicyNo()));
		Optional.ofNullable(entity.getLeapFundCode())
				.ifPresent((SurrenderLeapFundDetails) -> surrenderLeapFundDetails.setLeapFundCode(entity.getLeapFundCode()));
		Optional.ofNullable(entity.getLeapFundName())
				.ifPresent((SurrenderLeapFundDetails) -> surrenderLeapFundDetails.setLeapFundName(entity.getLeapFundName()));
		Optional.ofNullable(entity.getLeapNavDate())
				.ifPresent((SurrenderLeapFundDetails) -> surrenderLeapFundDetails.setLeapNavDate(entity.getLeapNavDate()));
		Optional.ofNullable(entity.getLeapUnits())
				.ifPresent((SurrenderLeapFundDetails) -> surrenderLeapFundDetails.setLeapUnits(entity.getLeapUnits()));
		Optional.ofNullable(entity.getLeapRateApp())
				.ifPresent((SurrenderLeapFundDetails) -> surrenderLeapFundDetails.setLeapRateApp(entity.getLeapRateApp()));
		Optional.ofNullable(entity.getLeapFundValue()).ifPresent(
				(SurrenderLeapFundDetails) -> surrenderLeapFundDetails.setLeapFundValue(entity.getLeapFundValue()));
		Optional.ofNullable(entity.getStatus()).ifPresent(
				(SurrenderLeapFundDetails) -> surrenderLeapFundDetails.setStatus(entity.getStatus()));
		Optional.ofNullable(entity.getRemark())
				.ifPresent((SurrenderLeapFundDetails) -> surrenderLeapFundDetails.setRemark(entity.getRemark()));
		

		surrenderLeapFundDetailsRepository.save(surrenderLeapFundDetails);
		return errorService.getErrorById("ER002");

	}

	// Soft Delete
	@Override
	public String softdelete(Long id) {
		SurrenderLeapFundDetails surrenderLeapFundDetails = surrenderLeapFundDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SurrenderLeapFundDetails", "id", id));
		surrenderLeapFundDetails.setValidFlag(-1);
		surrenderLeapFundDetailsRepository.save(surrenderLeapFundDetails);
		return errorService.getErrorById("ER003");
	}

	// Hard Delete
	@Override
	public String harddelete(Long id) {
		SurrenderLeapFundDetails surrenderLeapFundDetails = surrenderLeapFundDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SurrenderLeapFundDetails", "id", id));
		surrenderLeapFundDetailsRepository.delete(surrenderLeapFundDetails);
		return errorService.getErrorById("ER003");
	}

	// Global Search
	@Override
	public List<SurrenderLeapFundDetailsDto> globalSearch(String key) {
		List<SurrenderLeapFundDetails> listOfSurrenderLeapFundDetails = surrenderLeapFundDetailsRepository
				.globalSearch(key);
		List<SurrenderLeapFundDetailsDto> listOfSurrenderLeapFundDetailsDto = listOfSurrenderLeapFundDetails.stream()
				.map(surrenderLeapFundDetails -> entityToDto.apply(surrenderLeapFundDetails))
				.collect(Collectors.toList());
		return listOfSurrenderLeapFundDetailsDto;
	}
}
