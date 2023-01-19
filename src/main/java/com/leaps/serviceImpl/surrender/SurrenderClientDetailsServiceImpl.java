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

import com.leaps.dto.surrender.SurrenderClientDetailsDto;
import com.leaps.entity.surrender.SurrenderClientDetails;
import com.leaps.error.ErrorService;
import com.leaps.exceptions.ResourceNotFoundException;
import com.leaps.repository.surrender.SurrenderClientDetailsRepository;
import com.leaps.responses.surrender.SurrenderClientDetailsResponse;
import com.leaps.service.surrender.SurrenderClientDetailsService;

@Service
public class SurrenderClientDetailsServiceImpl implements SurrenderClientDetailsService {

	// Autowiring Repository to use jpa methods and custom queries
	@Autowired
	private SurrenderClientDetailsRepository surrenderClientDetailsRepository;

	// Autowiring Error Service to Display the Response Messages Stored in Error
	// Table
	@Autowired
	private ErrorService errorService;

	// Entity to Dto using Function

	public Function<SurrenderClientDetails, SurrenderClientDetailsDto> entityToDto = entity -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		mapper.addMappings(new PropertyMap<SurrenderClientDetails, SurrenderClientDetailsDto>() {

			@Override
			protected void configure() {
				map().setCompanyName(source.getCompany().getCompanyName());
			}
		});
		SurrenderClientDetailsDto dto = mapper.map(entity, SurrenderClientDetailsDto.class);
		return dto;
	};

	// Dto to Entity using Function

	public Function<SurrenderClientDetailsDto, SurrenderClientDetails> dtoToEntity = dto -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		SurrenderClientDetails entity = mapper.map(dto, SurrenderClientDetails.class);
		return entity;
	};

	// Get all where valid flag=1
	@Override
	public List<SurrenderClientDetailsDto> getall() {
		List<SurrenderClientDetails> listOfSurrenderClientDetails = surrenderClientDetailsRepository.getallActive();
		List<SurrenderClientDetailsDto> listOfSurrenderClientDetailsDto = listOfSurrenderClientDetails.stream()
				.map(surrenderClientDetails -> entityToDto.apply(surrenderClientDetails)).collect(Collectors.toList());
		return listOfSurrenderClientDetailsDto;
	}

	// Get All where valid flag=1 With Pagination
	@Override
	public SurrenderClientDetailsResponse getAllWithPagination(int pageNo, int pageSize, String sortBy,
			String sortDir) {
		// sort Condition
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		// Create Pageable Instance
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

		Page<SurrenderClientDetails> surrenderClientDetailsPage = surrenderClientDetailsRepository
				.getallActivePagination(pageable);

		// get content
		List<SurrenderClientDetails> listOfSurrenderClientDetails = surrenderClientDetailsPage.getContent();

		List<SurrenderClientDetailsDto> content = listOfSurrenderClientDetails.stream()
				.map(surrenderClientDetails -> entityToDto.apply(surrenderClientDetails)).collect(Collectors.toList());

		// Setting the the values to Custom Response Created
		SurrenderClientDetailsResponse surrenderClientDetailsResponse = new SurrenderClientDetailsResponse();
		surrenderClientDetailsResponse.setContent(content);
		surrenderClientDetailsResponse.setPageNo(surrenderClientDetailsPage.getNumber());
		surrenderClientDetailsResponse.setPageSize(surrenderClientDetailsPage.getSize());
		surrenderClientDetailsResponse.setTotalElements(surrenderClientDetailsPage.getTotalElements());
		surrenderClientDetailsResponse.setTotalPages(surrenderClientDetailsPage.getTotalPages());
		surrenderClientDetailsResponse.setLast(surrenderClientDetailsPage.isLast());

		return surrenderClientDetailsResponse;
	}

	// Get Active By id
	@Override
	public SurrenderClientDetailsDto getbyid(Long id) {
		SurrenderClientDetails surrenderClientDetails = surrenderClientDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SurrenderClientDetails", "id", id));
		SurrenderClientDetailsDto surrenderClientDetailsDto = entityToDto.apply(surrenderClientDetails);
		return surrenderClientDetailsDto;
	}

	// Add
	@Override
	public String add(SurrenderClientDetailsDto dto) {
		SurrenderClientDetails surrenderClientDetails = dtoToEntity.apply(dto);
		surrenderClientDetails.setValidFlag(1);
		surrenderClientDetailsRepository.save(surrenderClientDetails);
		return errorService.getErrorById("ER001");
	}

	// Update
	@Override
	public String update(Long id, SurrenderClientDetailsDto dto) {
		SurrenderClientDetails entity = dtoToEntity.apply(dto);

		SurrenderClientDetails surrenderClientDetails = surrenderClientDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SurrenderClientDetails", "id", id));

		// Using Optional for Null check
		Optional.of(entity.getCompanyId())
				.ifPresent((DeathClaimCoverDetails) -> surrenderClientDetails.setCompanyId(entity.getCompanyId()));

		Optional.ofNullable(entity.getClntNum())
				.ifPresent((SurrenderClientDetails) -> surrenderClientDetails.setClntNum(entity.getClntNum()));
		Optional.ofNullable(entity.getLaName())
				.ifPresent((SurrenderClientDetails) -> surrenderClientDetails.setLaName(entity.getLaName()));
		Optional.ofNullable(entity.getLaDob())
				.ifPresent((SurrenderClientDetails) -> surrenderClientDetails.setLaDob(entity.getLaDob()));
		Optional.ofNullable(entity.getNationality())
				.ifPresent((SurrenderClientDetails) -> surrenderClientDetails.setNationality(entity.getNationality()));
		Optional.ofNullable(entity.getResidentStatus()).ifPresent(
				(SurrenderClientDetails) -> surrenderClientDetails.setResidentStatus(entity.getResidentStatus()));
		Optional.ofNullable(entity.getGender())
				.ifPresent((SurrenderClientDetails) -> surrenderClientDetails.setGender(entity.getGender()));
		Optional.ofNullable(entity.getContactNumber()).ifPresent(
				(SurrenderClientDetails) -> surrenderClientDetails.setContactNumber(entity.getContactNumber()));
		Optional.ofNullable(entity.getEmailId())
				.ifPresent((SurrenderClientDetails) -> surrenderClientDetails.setEmailId(entity.getEmailId()));
		surrenderClientDetailsRepository.save(surrenderClientDetails);
		return errorService.getErrorById("ER002");

	}

	// Soft Delete
	@Override
	public String softdelete(Long id) {
		SurrenderClientDetails surrenderClientDetails = surrenderClientDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SurrenderClientDetails", "id", id));
		surrenderClientDetails.setValidFlag(-1);
		surrenderClientDetailsRepository.save(surrenderClientDetails);
		return errorService.getErrorById("ER003");
	}

	// Hard Delete
	@Override
	public String harddelete(Long id) {
		SurrenderClientDetails surrenderClientDetails = surrenderClientDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SurrenderClientDetails", "id", id));
		surrenderClientDetailsRepository.delete(surrenderClientDetails);
		return errorService.getErrorById("ER003");
	}

	// Global Search
	@Override
	public List<SurrenderClientDetailsDto> globalSearch(String key) {
		List<SurrenderClientDetails> listOfSurrenderClientDetails = surrenderClientDetailsRepository.globalSearch(key);
		List<SurrenderClientDetailsDto> listOfSurrenderClientDetailsDto = listOfSurrenderClientDetails.stream()
				.map(surrenderClientDetails -> entityToDto.apply(surrenderClientDetails)).collect(Collectors.toList());
		return listOfSurrenderClientDetailsDto;
	}
}
