package com.leaps.serviceImpl.master;

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

import com.leaps.dto.master.MedicalDetailsDto;
import com.leaps.entity.master.MedicalDetails;
import com.leaps.error.ErrorService;
import com.leaps.exceptions.ResourceNotFoundException;
import com.leaps.repository.master.MedicalDetailsRepository;
import com.leaps.responses.master.MedicalDetailsResponse;
import com.leaps.service.master.MedicalDetailsService;

@Service
public class MedicalDetailsServiceImpl implements MedicalDetailsService {

	// Autowiring Repository to use jpa methods and custom queries
	@Autowired
	private MedicalDetailsRepository medicalDetailsRepository;

	// Autowiring Error Service to Display the Response Messages Stored in Error
	// Table
	@Autowired
	private ErrorService errorService;

	// Entity to Dto using Function

	public Function<MedicalDetails, MedicalDetailsDto> entityToDto = entity -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		mapper.addMappings(new PropertyMap<MedicalDetails, MedicalDetailsDto>() {

			@Override
			protected void configure() {
				map().setCompanyName(source.getCompany().getCompanyName());

			}

		});
		MedicalDetailsDto dto = mapper.map(entity, MedicalDetailsDto.class);
		return dto;
	};

	// Dto to Entity using Function

	public Function<MedicalDetailsDto, MedicalDetails> dtoToEntity = dto -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		MedicalDetails entity = mapper.map(dto, MedicalDetails.class);
		return entity;
	};

	// Get all where valid flag=1
	@Override
	public List<MedicalDetailsDto> getall() {
		List<MedicalDetails> listOfMedicalDetails = medicalDetailsRepository.getallActive();
		List<MedicalDetailsDto> listOfMedicalDetailsDto = listOfMedicalDetails.stream()
				.map(medicalDetails -> entityToDto.apply(medicalDetails)).collect(Collectors.toList());
		return listOfMedicalDetailsDto;
	}

	// Get All where valid flag=1 With Pagination
	@Override
	public MedicalDetailsResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir) {
		// sort Condition
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		// Create Pageable Instance
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

		Page<MedicalDetails> medicalDetailsPage = medicalDetailsRepository.getallActivePagination(pageable);

		// get content
		List<MedicalDetails> listOfMedicalDetails = medicalDetailsPage.getContent();

		List<MedicalDetailsDto> content = listOfMedicalDetails.stream()
				.map(medicalDetails -> entityToDto.apply(medicalDetails)).collect(Collectors.toList());

		// Setting the the values to Custom Response Created
		MedicalDetailsResponse medicalDetailsResponse = new MedicalDetailsResponse();
		medicalDetailsResponse.setContent(content);
		medicalDetailsResponse.setPageNo(medicalDetailsPage.getNumber());
		medicalDetailsResponse.setPageSize(medicalDetailsPage.getSize());
		medicalDetailsResponse.setTotalElements(medicalDetailsPage.getTotalElements());
		medicalDetailsResponse.setTotalPages(medicalDetailsPage.getTotalPages());
		medicalDetailsResponse.setLast(medicalDetailsPage.isLast());

		return medicalDetailsResponse;
	}

	// Get Active By id
	@Override
	public MedicalDetailsDto getbyid(Long id) {
		MedicalDetails medicalDetails = medicalDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("MedicalDetails", "id", id));
		MedicalDetailsDto medicalDetailsDto = entityToDto.apply(medicalDetails);
		return medicalDetailsDto;
	}

	// Add
	@Override
	public String add(MedicalDetailsDto dto) {
		MedicalDetails medicalDetails = dtoToEntity.apply(dto);
		medicalDetails.setValidFlag(1);
		medicalDetailsRepository.save(medicalDetails);
		return errorService.getErrorById("ER001");
	}

	// Update
	@Override
	public String update(Long id, MedicalDetailsDto dto) {
		MedicalDetails entity = dtoToEntity.apply(dto);

		MedicalDetails medicalDetails = medicalDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("MedicalDetails", "id", id));

		// Using Optional for Null check
		Optional.of(entity.getCompanyId())
				.ifPresent((DeathClaimCoverDetails) -> medicalDetails.setCompanyId(entity.getCompanyId()));
		Optional.ofNullable(entity.getCompanyName())
				.ifPresent((MedicalDetails) -> medicalDetails.setCompanyName(entity.getCompanyName()));
		Optional.ofNullable(entity.getTpaCode())
				.ifPresent((MedicalDetails) -> medicalDetails.setTpaCode(entity.getTpaCode()));
		Optional.ofNullable(entity.getProdName())
				.ifPresent((MedicalDetails) -> medicalDetails.setProdName(entity.getProdName()));
		Optional.ofNullable(entity.getMedicalCategory())
				.ifPresent((MedicalDetails) -> medicalDetails.setMedicalCategory(entity.getMedicalCategory()));
		Optional.ofNullable(entity.getMedicalCenter())
				.ifPresent((MedicalDetails) -> medicalDetails.setMedicalCenter(entity.getMedicalCenter()));
		Optional.ofNullable(entity.getMfRate())
				.ifPresent((MedicalDetails) -> medicalDetails.setMfRate(entity.getMfRate()));
		Optional.ofNullable(entity.getStartDate())
				.ifPresent((MedicalDetails) -> medicalDetails.setStartDate(entity.getStartDate()));
		Optional.ofNullable(entity.getEndDate())
				.ifPresent((MedicalDetails) -> medicalDetails.setEndDate(entity.getEndDate()));

		medicalDetailsRepository.save(medicalDetails);
		return errorService.getErrorById("ER002");

	}

	// Soft Delete
	@Override
	public String softdelete(Long id) {
		MedicalDetails medicalDetails = medicalDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("MedicalDetails", "id", id));
		medicalDetails.setValidFlag(-1);
		medicalDetailsRepository.save(medicalDetails);
		return errorService.getErrorById("ER003");
	}

	// Hard Delete
	@Override
	public String harddelete(Long id) {
		MedicalDetails medicalDetails = medicalDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("MedicalDetails", "id", id));
		medicalDetailsRepository.delete(medicalDetails);
		return errorService.getErrorById("ER003");
	}

	// Global Search
	@Override
	public List<MedicalDetailsDto> globalSearch(String key) {
		List<MedicalDetails> listOfMedicalDetails = medicalDetailsRepository.globalSearch(key);
		List<MedicalDetailsDto> listOfMedicalDetailsDto = listOfMedicalDetails.stream()
				.map(medicalDetails -> entityToDto.apply(medicalDetails)).collect(Collectors.toList());
		return listOfMedicalDetailsDto;
	}

}
