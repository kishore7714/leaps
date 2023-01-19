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

import com.leaps.dto.master.MortalityFlagMasterDto;
import com.leaps.entity.master.MortalityFlagMaster;
import com.leaps.error.ErrorService;
import com.leaps.exceptions.ResourceNotFoundException;
import com.leaps.repository.master.MortalityFlagMasterRepository;
import com.leaps.responses.master.MortalityFlagMasterResponse;
import com.leaps.service.master.MortalityFlagMasterService;

@Service
public class MortalityFlagMasterServiceImpl implements MortalityFlagMasterService {

	// Autowiring Repository to use jpa methods and custom queries
	@Autowired
	private MortalityFlagMasterRepository mortalityFlagMasterRepository;

	// Autowiring Error Service to Display the Response Messages Stored in Error
	// Table
	@Autowired
	private ErrorService errorService;

	// Entity to Dto using Function

	public Function<MortalityFlagMaster, MortalityFlagMasterDto> entityToDto = entity -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		mapper.addMappings(new PropertyMap<MortalityFlagMaster, MortalityFlagMasterDto>() {

			@Override
			protected void configure() {
				map().setCompanyName(source.getCompany().getCompanyName());

			}

		});
		MortalityFlagMasterDto dto = mapper.map(entity, MortalityFlagMasterDto.class);
		return dto;
	};

	// Dto to Entity using Function

	public Function<MortalityFlagMasterDto, MortalityFlagMaster> dtoToEntity = dto -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		MortalityFlagMaster entity = mapper.map(dto, MortalityFlagMaster.class);
		return entity;
	};

	// Get all where valid flag=1
	@Override
	public List<MortalityFlagMasterDto> getall() {
		List<MortalityFlagMaster> listOfMortalityFlagMaster = mortalityFlagMasterRepository.getallActive();
		List<MortalityFlagMasterDto> listOfMortalityFlagMasterDto = listOfMortalityFlagMaster.stream()
				.map(mortalityFlagMaster -> entityToDto.apply(mortalityFlagMaster)).collect(Collectors.toList());
		return listOfMortalityFlagMasterDto;
	}

	// Get All where valid flag=1 With Pagination
	@Override
	public MortalityFlagMasterResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir) {
		// sort Condition
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		// Create Pageable Instance
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

		Page<MortalityFlagMaster> mortalityFlagMasterPage = mortalityFlagMasterRepository
				.getallActivePagination(pageable);

		// get content
		List<MortalityFlagMaster> listOfMortalityFlagMaster = mortalityFlagMasterPage.getContent();

		List<MortalityFlagMasterDto> content = listOfMortalityFlagMaster.stream()
				.map(mortalityFlagMaster -> entityToDto.apply(mortalityFlagMaster)).collect(Collectors.toList());

		// Setting the the values to Custom Response Created
		MortalityFlagMasterResponse mortalityFlagMasterResponse = new MortalityFlagMasterResponse();
		mortalityFlagMasterResponse.setContent(content);
		mortalityFlagMasterResponse.setPageNo(mortalityFlagMasterPage.getNumber());
		mortalityFlagMasterResponse.setPageSize(mortalityFlagMasterPage.getSize());
		mortalityFlagMasterResponse.setTotalElements(mortalityFlagMasterPage.getTotalElements());
		mortalityFlagMasterResponse.setTotalPages(mortalityFlagMasterPage.getTotalPages());
		mortalityFlagMasterResponse.setLast(mortalityFlagMasterPage.isLast());

		return mortalityFlagMasterResponse;
	}

	// Get Active By id
	@Override
	public MortalityFlagMasterDto getbyid(Long id) {
		MortalityFlagMaster mortalityFlagMaster = mortalityFlagMasterRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("MortalityFlagMaster", "id", id));
		MortalityFlagMasterDto mortalityFlagMasterDto = entityToDto.apply(mortalityFlagMaster);
		return mortalityFlagMasterDto;
	}

	// Add
	@Override
	public String add(MortalityFlagMasterDto dto) {
		MortalityFlagMaster mortalityFlagMaster = dtoToEntity.apply(dto);
		mortalityFlagMaster.setValidFlag(1);
		mortalityFlagMasterRepository.save(mortalityFlagMaster);
		return errorService.getErrorById("ER001");
	}

	// Update
	@Override
	public String update(Long id, MortalityFlagMasterDto dto) {
		MortalityFlagMaster entity = dtoToEntity.apply(dto);

		MortalityFlagMaster mortalityFlagMaster = mortalityFlagMasterRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("MortalityFlagMaster", "id", id));

		// Using Optional for Null check
		Optional.of(entity.getCompanyId())
				.ifPresent((DeathClaimCoverDetails) -> mortalityFlagMaster.setCompanyId(entity.getCompanyId()));
		Optional.ofNullable(entity.getUinNumber())
				.ifPresent((MortalityFlagMaster) -> mortalityFlagMaster.setUinNumber(entity.getUinNumber()));
		Optional.ofNullable(entity.getCoverName())
				.ifPresent((MortalityFlagMaster) -> mortalityFlagMaster.setCoverName(entity.getCoverName()));
		Optional.ofNullable(entity.getGstRate())
				.ifPresent((MortalityFlagMaster) -> mortalityFlagMaster.setGstRate(entity.getGstRate()));
		Optional.ofNullable(entity.getMortFlag())
				.ifPresent((MortalityFlagMaster) -> mortalityFlagMaster.setMortFlag(entity.getMortFlag()));
		Optional.ofNullable(entity.getStartDate())
				.ifPresent((MortalityFlagMaster) -> mortalityFlagMaster.setStartDate(entity.getStartDate()));
		Optional.ofNullable(entity.getEndDate())
				.ifPresent((MortalityFlagMaster) -> mortalityFlagMaster.setEndDate(entity.getEndDate()));

		mortalityFlagMasterRepository.save(mortalityFlagMaster);
		return errorService.getErrorById("ER002");

	}

	// Soft Delete
	@Override
	public String softdelete(Long id) {
		MortalityFlagMaster mortalityFlagMaster = mortalityFlagMasterRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("MortalityFlagMaster", "id", id));
		mortalityFlagMaster.setValidFlag(-1);
		mortalityFlagMasterRepository.save(mortalityFlagMaster);
		return errorService.getErrorById("ER003");
	}

	// Hard Delete
	@Override
	public String harddelete(Long id) {
		MortalityFlagMaster mortalityFlagMaster = mortalityFlagMasterRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("MortalityFlagMaster", "id", id));
		mortalityFlagMasterRepository.delete(mortalityFlagMaster);
		return errorService.getErrorById("ER003");
	}

	// Global Search
	@Override
	public List<MortalityFlagMasterDto> globalSearch(String key) {
		List<MortalityFlagMaster> listOfMortalityFlagMaster = mortalityFlagMasterRepository.globalSearch(key);
		List<MortalityFlagMasterDto> listOfMortalityFlagMasterDto = listOfMortalityFlagMaster.stream()
				.map(mortalityFlagMaster -> entityToDto.apply(mortalityFlagMaster)).collect(Collectors.toList());
		return listOfMortalityFlagMasterDto;
	}
}
