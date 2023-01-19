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

import com.leaps.dto.master.GsvCashValueDto;
import com.leaps.entity.master.GsvCashValue;
import com.leaps.error.ErrorService;
import com.leaps.exceptions.ResourceNotFoundException;
import com.leaps.repository.master.GsvCashValueRepository;
import com.leaps.responses.master.GsvCashValueResponse;
import com.leaps.service.master.GsvCashValueService;

@Service
public class GsvCashValueServiceImpl implements GsvCashValueService {
	// Autowiring Repository to use jpa methods and custom queries
	@Autowired
	private GsvCashValueRepository gsvCashValueRepository;

	// Autowiring Error Service to Display the Response Messages Stored in Error
	// Table
	@Autowired
	private ErrorService errorService;

	// Entity to Dto using Function

	public Function<GsvCashValue, GsvCashValueDto> entityToDto = entity -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		mapper.addMappings(new PropertyMap<GsvCashValue, GsvCashValueDto>() {

			@Override
			protected void configure() {
				map().setCompanyName(source.getCompany().getCompanyName());

			}

		});
		GsvCashValueDto dto = mapper.map(entity, GsvCashValueDto.class);
		return dto;
	};

	// Dto to Entity using Function

	public Function<GsvCashValueDto, GsvCashValue> dtoToEntity = dto -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		GsvCashValue entity = mapper.map(dto, GsvCashValue.class);
		return entity;
	};

	// Get all where valid flag=1
	@Override
	public List<GsvCashValueDto> getall() {
		List<GsvCashValue> listOfGsvCashValue = gsvCashValueRepository.getallActive();
		List<GsvCashValueDto> listOfGsvCashValueDto = listOfGsvCashValue.stream()
				.map(gsvCashValue -> entityToDto.apply(gsvCashValue)).collect(Collectors.toList());
		return listOfGsvCashValueDto;
	}

	// Get All where valid flag=1 With Pagination
	@Override
	public GsvCashValueResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir) {
		// sort Condition
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		// Create Pageable Instance
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

		Page<GsvCashValue> gsvCashValuePage = gsvCashValueRepository.getallActivePagination(pageable);

		// get content
		List<GsvCashValue> listOfGsvCashValue = gsvCashValuePage.getContent();

		List<GsvCashValueDto> content = listOfGsvCashValue.stream().map(gsvCashValue -> entityToDto.apply(gsvCashValue))
				.collect(Collectors.toList());

		// Setting the the values to Custom Response Created
		GsvCashValueResponse gsvCashValueResponse = new GsvCashValueResponse();
		gsvCashValueResponse.setContent(content);
		gsvCashValueResponse.setPageNo(gsvCashValuePage.getNumber());
		gsvCashValueResponse.setPageSize(gsvCashValuePage.getSize());
		gsvCashValueResponse.setTotalElements(gsvCashValuePage.getTotalElements());
		gsvCashValueResponse.setTotalPages(gsvCashValuePage.getTotalPages());
		gsvCashValueResponse.setLast(gsvCashValuePage.isLast());

		return gsvCashValueResponse;
	}

	// Get Active By id
	@Override
	public GsvCashValueDto getbyid(Long id) {
		GsvCashValue gsvCashValue = gsvCashValueRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("GsvCashValue", "id", id));
		GsvCashValueDto gsvCashValueDto = entityToDto.apply(gsvCashValue);
		return gsvCashValueDto;
	}

	// Add
	@Override
	public String add(GsvCashValueDto dto) {
		GsvCashValue gsvCashValue = dtoToEntity.apply(dto);
		gsvCashValue.setValidFlag(1);
		gsvCashValueRepository.save(gsvCashValue);
		return errorService.getErrorById("ER001");
	}

	// Update
	@Override
	public String update(Long id, GsvCashValueDto dto) {
		GsvCashValue entity = dtoToEntity.apply(dto);

		GsvCashValue gsvCashValue = gsvCashValueRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("GsvCashValue", "id", id));

		// Using Optional for Null check
		Optional.of(entity.getCompanyId())
				.ifPresent((DeathClaimCoverDetails) -> gsvCashValue.setCompanyId(entity.getCompanyId()));
		Optional.ofNullable(entity.getUinNumber())
				.ifPresent((GsvCashValue) -> gsvCashValue.setUinNumber(entity.getUinNumber()));
		Optional.ofNullable(entity.getPlanCode())
				.ifPresent((GsvCashValue) -> gsvCashValue.setPlanCode(entity.getPlanCode()));
		Optional.ofNullable(entity.getPlanName())
				.ifPresent((GsvCashValue) -> gsvCashValue.setPlanName(entity.getPlanName()));
		Optional.ofNullable(entity.getYearsToMaturity())
				.ifPresent((GsvCashValue) -> gsvCashValue.setYearsToMaturity(entity.getYearsToMaturity()));
		Optional.ofNullable(entity.getCvbRate())
				.ifPresent((GsvCashValue) -> gsvCashValue.setCvbRate(entity.getCvbRate()));
		Optional.ofNullable(entity.getStartDate())
				.ifPresent((GsvCashValue) -> gsvCashValue.setStartDate(entity.getStartDate()));
		Optional.ofNullable(entity.getEndDate())
				.ifPresent((GsvCashValue) -> gsvCashValue.setEndDate(entity.getEndDate()));

		gsvCashValueRepository.save(gsvCashValue);
		return errorService.getErrorById("ER002");

	}

	// Soft Delete
	@Override
	public String softdelete(Long id) {
		GsvCashValue gsvCashValue = gsvCashValueRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("GsvCashValue", "id", id));
		gsvCashValue.setValidFlag(-1);
		gsvCashValueRepository.save(gsvCashValue);
		return errorService.getErrorById("ER003");
	}

	// Hard Delete
	@Override
	public String harddelete(Long id) {
		GsvCashValue gsvCashValue = gsvCashValueRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("GsvCashValue", "id", id));
		gsvCashValueRepository.delete(gsvCashValue);
		return errorService.getErrorById("ER003");
	}

	// Global Search
	@Override
	public List<GsvCashValueDto> globalSearch(String key) {
		List<GsvCashValue> listOfGsvCashValue = gsvCashValueRepository.globalSearch(key);
		List<GsvCashValueDto> listOfGsvCashValueDto = listOfGsvCashValue.stream()
				.map(gsvCashValue -> entityToDto.apply(gsvCashValue)).collect(Collectors.toList());
		return listOfGsvCashValueDto;
	}
}
