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

import com.leaps.dto.master.StamDutyMasterDto;
import com.leaps.entity.master.StamDutyMaster;
import com.leaps.error.ErrorService;
import com.leaps.exceptions.ResourceNotFoundException;
import com.leaps.repository.master.StamDutyMasterRepository;
import com.leaps.responses.master.StamDutyMasterResponse;
import com.leaps.service.master.StamDutyMasterService;

@Service
public class StamDutyMasterServiceImpl implements StamDutyMasterService {

	// Autowiring Repository to use jpa methods and custom queries
	@Autowired
	private StamDutyMasterRepository stamDutyMasterRepository;

	// Autowiring Error Service to Display the Response Messages Stored in Error
	// Table
	@Autowired
	private ErrorService errorService;

	// Entity to Dto using Function

	public Function<StamDutyMaster, StamDutyMasterDto> entityToDto = entity -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		mapper.addMappings(new PropertyMap<StamDutyMaster, StamDutyMasterDto>() {

			@Override
			protected void configure() {
				map().setCompanyName(source.getCompany().getCompanyName());

			}

		});
		StamDutyMasterDto dto = mapper.map(entity, StamDutyMasterDto.class);
		return dto;
	};

	// Dto to Entity using Function

	public Function<StamDutyMasterDto, StamDutyMaster> dtoToEntity = dto -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		StamDutyMaster entity = mapper.map(dto, StamDutyMaster.class);
		return entity;
	};

	// Get all where valid flag=1
	@Override
	public List<StamDutyMasterDto> getall() {
		List<StamDutyMaster> listOfStamDutyMaster = stamDutyMasterRepository.getallActive();
		List<StamDutyMasterDto> listOfStamDutyMasterDto = listOfStamDutyMaster.stream()
				.map(stamDutyMaster -> entityToDto.apply(stamDutyMaster)).collect(Collectors.toList());
		return listOfStamDutyMasterDto;
	}

	// Get All where valid flag=1 With Pagination
	@Override
	public StamDutyMasterResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir) {
		// sort Condition
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		// Create Pageable Instance
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

		Page<StamDutyMaster> stamDutyMasterPage = stamDutyMasterRepository.getallActivePagination(pageable);

		// get content
		List<StamDutyMaster> listOfStamDutyMaster = stamDutyMasterPage.getContent();

		List<StamDutyMasterDto> content = listOfStamDutyMaster.stream()
				.map(stamDutyMaster -> entityToDto.apply(stamDutyMaster)).collect(Collectors.toList());

		// Setting the the values to Custom Response Created
		StamDutyMasterResponse stamDutyMasterResponse = new StamDutyMasterResponse();
		stamDutyMasterResponse.setContent(content);
		stamDutyMasterResponse.setPageNo(stamDutyMasterPage.getNumber());
		stamDutyMasterResponse.setPageSize(stamDutyMasterPage.getSize());
		stamDutyMasterResponse.setTotalElements(stamDutyMasterPage.getTotalElements());
		stamDutyMasterResponse.setTotalPages(stamDutyMasterPage.getTotalPages());
		stamDutyMasterResponse.setLast(stamDutyMasterPage.isLast());

		return stamDutyMasterResponse;
	}

	// Get Active By id
	@Override
	public StamDutyMasterDto getbyid(Long id) {
		StamDutyMaster stamDutyMaster = stamDutyMasterRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("StamDutyMaster", "id", id));
		StamDutyMasterDto stamDutyMasterDto = entityToDto.apply(stamDutyMaster);
		return stamDutyMasterDto;
	}

	// Add
	@Override
	public String add(StamDutyMasterDto dto) {
		StamDutyMaster stamDutyMaster = dtoToEntity.apply(dto);
		stamDutyMaster.setValidFlag(1);
		stamDutyMasterRepository.save(stamDutyMaster);
		return errorService.getErrorById("ER001");
	}

	// Update
	@Override
	public String update(Long id, StamDutyMasterDto dto) {
		StamDutyMaster entity = dtoToEntity.apply(dto);

		StamDutyMaster stamDutyMaster = stamDutyMasterRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("StamDutyMaster", "id", id));

		// Using Optional for Null check
		Optional.of(entity.getCompanyId())
				.ifPresent((DeathClaimCoverDetails) -> stamDutyMaster.setCompanyId(entity.getCompanyId()));
		Optional.ofNullable(entity.getUinNumber())
				.ifPresent((StamDutyMaster) -> stamDutyMaster.setUinNumber(entity.getUinNumber()));
		Optional.ofNullable(entity.getCoverName())
				.ifPresent((StamDutyMaster) -> stamDutyMaster.setCoverName(entity.getCoverName()));
		Optional.ofNullable(entity.getSdRate())
				.ifPresent((StamDutyMaster) -> stamDutyMaster.setSdRate(entity.getSdRate()));
		Optional.ofNullable(entity.getGstRate())
				.ifPresent((StamDutyMaster) -> stamDutyMaster.setGstRate(entity.getGstRate()));

		Optional.ofNullable(entity.getStartDate())
				.ifPresent((StamDutyMaster) -> stamDutyMaster.setStartDate(entity.getStartDate()));
		Optional.ofNullable(entity.getEndDate())
				.ifPresent((StamDutyMaster) -> stamDutyMaster.setEndDate(entity.getEndDate()));

		stamDutyMasterRepository.save(stamDutyMaster);
		return errorService.getErrorById("ER002");

	}

	// Soft Delete
	@Override
	public String softdelete(Long id) {
		StamDutyMaster stamDutyMaster = stamDutyMasterRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("StamDutyMaster", "id", id));
		stamDutyMaster.setValidFlag(-1);
		stamDutyMasterRepository.save(stamDutyMaster);
		return errorService.getErrorById("ER003");
	}

	// Hard Delete
	@Override
	public String harddelete(Long id) {
		StamDutyMaster stamDutyMaster = stamDutyMasterRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("StamDutyMaster", "id", id));
		stamDutyMasterRepository.delete(stamDutyMaster);
		return errorService.getErrorById("ER003");
	}

	// Global Search
	@Override
	public List<StamDutyMasterDto> globalSearch(String key) {
		List<StamDutyMaster> listOfStamDutyMaster = stamDutyMasterRepository.globalSearch(key);
		List<StamDutyMasterDto> listOfStamDutyMasterDto = listOfStamDutyMaster.stream()
				.map(stamDutyMaster -> entityToDto.apply(stamDutyMaster)).collect(Collectors.toList());
		return listOfStamDutyMasterDto;
	}
}
