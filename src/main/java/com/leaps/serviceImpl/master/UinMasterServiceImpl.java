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

import com.leaps.dto.master.UinMasterDto;
import com.leaps.entity.master.UinMaster;
import com.leaps.error.ErrorService;
import com.leaps.exceptions.ResourceNotFoundException;
import com.leaps.repository.master.UinMasterRepository;
import com.leaps.responses.master.UinMasterResponse;
import com.leaps.service.master.UinMasterService;

@Service
public class UinMasterServiceImpl implements UinMasterService {
	// Autowiring Repository to use jpa methods and custom queries
	@Autowired
	private UinMasterRepository uinMasterRepository;

	// Autowiring Error Service to Display the Response Messages Stored in Error
	// Table
	@Autowired
	private ErrorService errorService;

	// Entity to Dto using Function

	public Function<UinMaster, UinMasterDto> entityToDto = entity -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		mapper.addMappings(new PropertyMap<UinMaster, UinMasterDto>() {

			@Override
			protected void configure() {
				map().setCompanyName(source.getCompany().getCompanyName());

			}

		});
		UinMasterDto dto = mapper.map(entity, UinMasterDto.class);
		return dto;
	};

	// Dto to Entity using Function

	public Function<UinMasterDto, UinMaster> dtoToEntity = dto -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UinMaster entity = mapper.map(dto, UinMaster.class);
		return entity;
	};

	// Get all where valid flag=1
	@Override
	public List<UinMasterDto> getall() {
		List<UinMaster> listOfUinMaster = uinMasterRepository.getallActive();
		List<UinMasterDto> listOfUinMasterDto = listOfUinMaster.stream().map(uinMaster -> entityToDto.apply(uinMaster))
				.collect(Collectors.toList());
		return listOfUinMasterDto;
	}

	// Get All where valid flag=1 With Pagination
	@Override
	public UinMasterResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir) {
		// sort Condition
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		// Create Pageable Instance
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

		Page<UinMaster> uinMasterPage = uinMasterRepository.getallActivePagination(pageable);

		// get content
		List<UinMaster> listOfUinMaster = uinMasterPage.getContent();

		List<UinMasterDto> content = listOfUinMaster.stream().map(uinMaster -> entityToDto.apply(uinMaster))
				.collect(Collectors.toList());

		// Setting the the values to Custom Response Created
		UinMasterResponse uinMasterResponse = new UinMasterResponse();
		uinMasterResponse.setContent(content);
		uinMasterResponse.setPageNo(uinMasterPage.getNumber());
		uinMasterResponse.setPageSize(uinMasterPage.getSize());
		uinMasterResponse.setTotalElements(uinMasterPage.getTotalElements());
		uinMasterResponse.setTotalPages(uinMasterPage.getTotalPages());
		uinMasterResponse.setLast(uinMasterPage.isLast());

		return uinMasterResponse;
	}

	// Get Active By id
	@Override
	public UinMasterDto getbyid(Long id) {
		UinMaster uinMaster = uinMasterRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("UinMaster", "id", id));
		UinMasterDto uinMasterDto = entityToDto.apply(uinMaster);
		return uinMasterDto;
	}

	// Add
	@Override
	public String add(UinMasterDto dto) {
		UinMaster uinMaster = dtoToEntity.apply(dto);
		uinMaster.setValidFlag(1);
		uinMasterRepository.save(uinMaster);
		return errorService.getErrorById("ER001");
	}

	// Update
	@Override
	public String update(Long id, UinMasterDto dto) {
		UinMaster entity = dtoToEntity.apply(dto);

		UinMaster uinMaster = uinMasterRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("UinMaster", "id", id));

		// Using Optional for Null check
		Optional.of(entity.getCompanyId())
				.ifPresent((DeathClaimCoverDetails) -> uinMaster.setCompanyId(entity.getCompanyId()));
		Optional.ofNullable(entity.getUinNumber())
				.ifPresent((UinMaster) -> uinMaster.setUinNumber(entity.getUinNumber()));
		Optional.ofNullable(entity.getPlanName()).ifPresent((UinMaster) -> uinMaster.setPlanName(entity.getPlanName()));
		Optional.ofNullable(entity.getPlanCode()).ifPresent((UinMaster) -> uinMaster.setPlanCode(entity.getPlanCode()));
		Optional.ofNullable(entity.getGsvFactor())
				.ifPresent((UinMaster) -> uinMaster.setGsvFactor(entity.getGsvFactor()));
		Optional.ofNullable(entity.getGsvCashValue())
				.ifPresent((UinMaster) -> uinMaster.setGsvCashValue(entity.getGsvCashValue()));
		Optional.ofNullable(entity.getSsvFactor())
				.ifPresent((UinMaster) -> uinMaster.setSsvFactor(entity.getSsvFactor()));
		Optional.ofNullable(entity.getProductType())
				.ifPresent((UinMaster) -> uinMaster.setProductType(entity.getProductType()));
		Optional.ofNullable(entity.getFlcEligibility())
		.ifPresent((UinMaster) -> uinMaster.setFlcEligibility(entity.getFlcEligibility()));
		Optional.ofNullable(entity.getSurrenderChargeRate())
				.ifPresent((UinMaster) -> uinMaster.setSurrenderChargeRate(entity.getSurrenderChargeRate()));
		Optional.ofNullable(entity.getStartDate())
				.ifPresent((UinMaster) -> uinMaster.setStartDate(entity.getStartDate()));
		Optional.ofNullable(entity.getEndDate()).ifPresent((UinMaster) -> uinMaster.setEndDate(entity.getEndDate()));

		uinMasterRepository.save(uinMaster);
		return errorService.getErrorById("ER002");

	}

	// Soft Delete
	@Override
	public String softdelete(Long id) {
		UinMaster uinMaster = uinMasterRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("UinMaster", "id", id));
		uinMaster.setValidFlag(-1);
		uinMasterRepository.save(uinMaster);
		return errorService.getErrorById("ER003");
	}

	// Hard Delete
	@Override
	public String harddelete(Long id) {
		UinMaster uinMaster = uinMasterRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("UinMaster", "id", id));
		uinMasterRepository.delete(uinMaster);
		return errorService.getErrorById("ER003");
	}

	// Global Search
	@Override
	public List<UinMasterDto> globalSearch(String key) {
		List<UinMaster> listOfUinMaster = uinMasterRepository.globalSearch(key);
		List<UinMasterDto> listOfUinMasterDto = listOfUinMaster.stream().map(uinMaster -> entityToDto.apply(uinMaster))
				.collect(Collectors.toList());
		return listOfUinMasterDto;
	}
}
