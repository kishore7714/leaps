package com.leaps.serviceImpl.deathclaim;

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

import com.leaps.dto.deathclaim.DeathClaimClientDetailsDto;
import com.leaps.entity.deathclaim.DeathClaimClientDetails;
import com.leaps.error.ErrorService;
import com.leaps.exceptions.ResourceNotFoundException;
import com.leaps.repository.deathclaim.DeathClaimClientDetailsRepository;
import com.leaps.responses.deathclaim.DeathClaimClientDetailsResponse;
import com.leaps.service.deathclaim.DeathClaimClientDetailsService;

@Service
public class DeathClaimClientDetailsServiceImpl implements DeathClaimClientDetailsService {

	// Autowiring Repository to use jpa methods and custom queries
	@Autowired
	private DeathClaimClientDetailsRepository deathClaimClientDetailsRepository;

	// Autowiring Error Service to Display the Response Messages Stored in Error
	// Table
	@Autowired
	private ErrorService errorService;

	// Entity to Dto using Function

	public Function<DeathClaimClientDetails, DeathClaimClientDetailsDto> entityToDto = entity -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		mapper.addMappings(new PropertyMap<DeathClaimClientDetails, DeathClaimClientDetailsDto>() {

			@Override
			protected void configure() {
				map().setCompanyName(source.getCompany().getCompanyName());

			}

		});
		DeathClaimClientDetailsDto dto = mapper.map(entity, DeathClaimClientDetailsDto.class);
		return dto;
	};

	// Dto to Entity using Function

	public Function<DeathClaimClientDetailsDto, DeathClaimClientDetails> dtoToEntity = dto -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		DeathClaimClientDetails entity = mapper.map(dto, DeathClaimClientDetails.class);
		return entity;
	};

	// Get all where valid flag=1
	@Override
	public List<DeathClaimClientDetailsDto> getall() {
		List<DeathClaimClientDetails> listOfDeathClaimClientDetails = deathClaimClientDetailsRepository.getallActive();
		List<DeathClaimClientDetailsDto> listOfDeathClaimClientDetailsDto = listOfDeathClaimClientDetails.stream()
				.map(deathClaimClientDetails -> entityToDto.apply(deathClaimClientDetails))
				.collect(Collectors.toList());
		return listOfDeathClaimClientDetailsDto;
	}

	// Get All where valid flag=1 With Pagination
	@Override
	public DeathClaimClientDetailsResponse getAllWithPagination(int pageNo, int pageSize, String sortBy,
			String sortDir) {
		// sort Condition
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		// Create Pageable Instance
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

		Page<DeathClaimClientDetails> deathClaimClientDetailsPage = deathClaimClientDetailsRepository
				.getallActivePagination(pageable);

		// get content
		List<DeathClaimClientDetails> listOfDeathClaimClientDetails = deathClaimClientDetailsPage.getContent();

		List<DeathClaimClientDetailsDto> content = listOfDeathClaimClientDetails.stream()
				.map(deathClaimClientDetails -> entityToDto.apply(deathClaimClientDetails))
				.collect(Collectors.toList());

		// Setting the the values to Custom Response Created
		DeathClaimClientDetailsResponse deathClaimClientDetailsResponse = new DeathClaimClientDetailsResponse();
		deathClaimClientDetailsResponse.setContent(content);
		deathClaimClientDetailsResponse.setPageNo(deathClaimClientDetailsPage.getNumber());
		deathClaimClientDetailsResponse.setPageSize(deathClaimClientDetailsPage.getSize());
		deathClaimClientDetailsResponse.setTotalElements(deathClaimClientDetailsPage.getTotalElements());
		deathClaimClientDetailsResponse.setTotalPages(deathClaimClientDetailsPage.getTotalPages());
		deathClaimClientDetailsResponse.setLast(deathClaimClientDetailsPage.isLast());

		return deathClaimClientDetailsResponse;
	}

	// Get Active By id
	@Override
	public DeathClaimClientDetailsDto getbyid(Long id) {
		DeathClaimClientDetails deathClaimDetails = deathClaimClientDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("DeathClaimClientDetails", "id", id));
		DeathClaimClientDetailsDto deathClaimClientDetailsDto = entityToDto.apply(deathClaimDetails);
		return deathClaimClientDetailsDto;
	}

	// Add
	@Override
	public String add(DeathClaimClientDetailsDto dto) {
		DeathClaimClientDetails deathClaimClientDetails = dtoToEntity.apply(dto);
		deathClaimClientDetails.setValidFlag(1);
		deathClaimClientDetailsRepository.save(deathClaimClientDetails);
		return errorService.getErrorById("ER001");
	}

	// Update
	@Override
	public String update(Long id, DeathClaimClientDetailsDto dto) {
		DeathClaimClientDetails entity = dtoToEntity.apply(dto);

		DeathClaimClientDetails deathClaimDetails = deathClaimClientDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("DeathClaimClientDetails", "id", id));

		// Using Optional for Null check
		Optional.of(entity.getCompanyId())
				.ifPresent((DeathClaimCoverDetails) -> deathClaimDetails.setCompanyId(entity.getCompanyId()));

		Optional.ofNullable(entity.getClntNum())
				.ifPresent((DeathClaimClientDetails) -> deathClaimDetails.setClntNum(entity.getClntNum()));
		Optional.ofNullable(entity.getContactNumber())
				.ifPresent((DeathClaimClientDetails) -> deathClaimDetails.setContactNumber(entity.getContactNumber()));
		Optional.ofNullable(entity.getEmailId())
				.ifPresent((DeathClaimClientDetails) -> deathClaimDetails.setEmailId(entity.getEmailId()));
		Optional.ofNullable(entity.getLaName())
				.ifPresent((DeathClaimClientDetails) -> deathClaimDetails.setLaName(entity.getLaName()));
		Optional.ofNullable(entity.getLaDob())
				.ifPresent((DeathClaimClientDetails) -> deathClaimDetails.setLaDob(entity.getLaDob()));
		Optional.ofNullable(entity.getNationality())
				.ifPresent((DeathClaimClientDetails) -> deathClaimDetails.setNationality(entity.getNationality()));
		Optional.ofNullable(entity.getResidentStatus()).ifPresent(
				(DeathClaimClientDetails) -> deathClaimDetails.setResidentStatus(entity.getResidentStatus()));
		Optional.ofNullable(entity.getGender())
				.ifPresent((DeathClaimClientDetails) -> deathClaimDetails.setGender(entity.getGender()));

		deathClaimClientDetailsRepository.save(deathClaimDetails);
		return errorService.getErrorById("ER002");

	}

	// Soft Delete
	@Override
	public String softdelete(Long id) {
		DeathClaimClientDetails deathClaimClientDetails = deathClaimClientDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("DeathClaimClientDetails", "id", id));
		deathClaimClientDetails.setValidFlag(-1);
		deathClaimClientDetailsRepository.save(deathClaimClientDetails);
		return errorService.getErrorById("ER003");
	}

	// Hard Delete
	@Override
	public String harddelete(Long id) {
		DeathClaimClientDetails deathClaimClientDetails = deathClaimClientDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("DeathClaimClientDetails", "id", id));
		deathClaimClientDetailsRepository.delete(deathClaimClientDetails);
		return errorService.getErrorById("ER003");
	}

	// Global Search
	@Override
	public List<DeathClaimClientDetailsDto> globalSearch(String key) {
		List<DeathClaimClientDetails> listOfDeathClaimClientDetails = deathClaimClientDetailsRepository
				.globalSearch(key);
		List<DeathClaimClientDetailsDto> listOfDeathClaimClientDetailsDto = listOfDeathClaimClientDetails.stream()
				.map(deathClaimClientDetails -> entityToDto.apply(deathClaimClientDetails))
				.collect(Collectors.toList());
		return listOfDeathClaimClientDetailsDto;
	}
}
