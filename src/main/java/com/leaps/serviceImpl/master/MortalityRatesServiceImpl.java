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

import com.leaps.dto.master.MortalityRatesDto;
import com.leaps.entity.master.MortalityRates;
import com.leaps.error.ErrorService;
import com.leaps.exceptions.ResourceNotFoundException;
import com.leaps.repository.master.MortalityRatesRepository;
import com.leaps.responses.master.MortalityRatesResponse;
import com.leaps.service.master.MortalityRatesService;

@Service
public class MortalityRatesServiceImpl implements MortalityRatesService {

	// Autowiring Repository to use jpa methods and custom queries
	@Autowired
	private MortalityRatesRepository mortalityRatesRepository;

	// Autowiring Error Service to Display the Response Messages Stored in Error
	// Table
	@Autowired
	private ErrorService errorService;

	// Entity to Dto using Function

	public Function<MortalityRates, MortalityRatesDto> entityToDto = entity -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		mapper.addMappings(new PropertyMap<MortalityRates, MortalityRatesDto>() {

			@Override
			protected void configure() {
				map().setCompanyName(source.getCompany().getCompanyName());

			}

		});
		MortalityRatesDto dto = mapper.map(entity, MortalityRatesDto.class);
		return dto;
	};

	// Dto to Entity using Function

	public Function<MortalityRatesDto, MortalityRates> dtoToEntity = dto -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		MortalityRates entity = mapper.map(dto, MortalityRates.class);
		return entity;
	};

	// Get all where valid flag=1
	@Override
	public List<MortalityRatesDto> getall() {
		List<MortalityRates> listOfMortalityRates = mortalityRatesRepository.getallActive();
		List<MortalityRatesDto> listOfMortalityRatesDto = listOfMortalityRates.stream()
				.map(mortalityRates -> entityToDto.apply(mortalityRates)).collect(Collectors.toList());
		return listOfMortalityRatesDto;
	}

	// Get All where valid flag=1 With Pagination
	@Override
	public MortalityRatesResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir) {
		// sort Condition
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		// Create Pageable Instance
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

		Page<MortalityRates> mortalityRatesPage = mortalityRatesRepository.getallActivePagination(pageable);

		// get content
		List<MortalityRates> listOfMortalityRates = mortalityRatesPage.getContent();

		List<MortalityRatesDto> content = listOfMortalityRates.stream()
				.map(mortalityRates -> entityToDto.apply(mortalityRates)).collect(Collectors.toList());

		// Setting the the values to Custom Response Created
		MortalityRatesResponse mortalityRatesResponse = new MortalityRatesResponse();
		mortalityRatesResponse.setContent(content);
		mortalityRatesResponse.setPageNo(mortalityRatesPage.getNumber());
		mortalityRatesResponse.setPageSize(mortalityRatesPage.getSize());
		mortalityRatesResponse.setTotalElements(mortalityRatesPage.getTotalElements());
		mortalityRatesResponse.setTotalPages(mortalityRatesPage.getTotalPages());
		mortalityRatesResponse.setLast(mortalityRatesPage.isLast());

		return mortalityRatesResponse;
	}

	// Get Active By id
	@Override
	public MortalityRatesDto getbyid(Long id) {
		MortalityRates mortalityRates = mortalityRatesRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("MortalityRates", "id", id));
		MortalityRatesDto mortalityRatesDto = entityToDto.apply(mortalityRates);
		return mortalityRatesDto;
	}

	// Add
	@Override
	public String add(MortalityRatesDto dto) {
		MortalityRates mortalityRates = dtoToEntity.apply(dto);
		mortalityRates.setValidFlag(1);
		mortalityRatesRepository.save(mortalityRates);
		return errorService.getErrorById("ER001");
	}

	// Update
	@Override
	public String update(Long id, MortalityRatesDto dto) {
		MortalityRates entity = dtoToEntity.apply(dto);

		MortalityRates mortalityRates = mortalityRatesRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("MortalityRates", "id", id));

		// Using Optional for Null check
		Optional.of(entity.getCompanyId())
				.ifPresent((DeathClaimCoverDetails) -> mortalityRates.setCompanyId(entity.getCompanyId()));
		Optional.ofNullable(entity.getUinNumber())
				.ifPresent((MortalityRates) -> mortalityRates.setUinNumber(entity.getUinNumber()));
		Optional.ofNullable(entity.getPlanName())
				.ifPresent((MortalityRates) -> mortalityRates.setPlanName(entity.getPlanName()));
		Optional.ofNullable(entity.getPlan()).ifPresent((MortalityRates) -> mortalityRates.setPlan(entity.getPlan()));
		Optional.ofNullable(entity.getPremTerm())
				.ifPresent((MortalityRates) -> mortalityRates.setPremTerm(entity.getPremTerm()));
		Optional.ofNullable(entity.getAge()).ifPresent((MortalityRates) -> mortalityRates.setAge(entity.getAge()));
		Optional.ofNullable(entity.getRates())
				.ifPresent((MortalityRates) -> mortalityRates.setRates(entity.getRates()));

		Optional.ofNullable(entity.getSmoker())
				.ifPresent((MortalityRates) -> mortalityRates.setSmoker(entity.getSmoker()));

		Optional.ofNullable(entity.getStartDate())
				.ifPresent((MortalityRates) -> mortalityRates.setStartDate(entity.getStartDate()));
		Optional.ofNullable(entity.getEndDate())
				.ifPresent((MortalityRates) -> mortalityRates.setEndDate(entity.getEndDate()));

		mortalityRatesRepository.save(mortalityRates);
		return errorService.getErrorById("ER002");

	}

	// Soft Delete
	@Override
	public String softdelete(Long id) {
		MortalityRates mortalityRates = mortalityRatesRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("MortalityRates", "id", id));
		mortalityRates.setValidFlag(-1);
		mortalityRatesRepository.save(mortalityRates);
		return errorService.getErrorById("ER003");
	}

	// Hard Delete
	@Override
	public String harddelete(Long id) {
		MortalityRates mortalityRates = mortalityRatesRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("MortalityRates", "id", id));
		mortalityRatesRepository.delete(mortalityRates);
		return errorService.getErrorById("ER003");
	}

	// Global Search
	@Override
	public List<MortalityRatesDto> globalSearch(String key) {
		List<MortalityRates> listOfMortalityRates = mortalityRatesRepository.globalSearch(key);
		List<MortalityRatesDto> listOfMortalityRatesDto = listOfMortalityRates.stream()
				.map(mortalityRates -> entityToDto.apply(mortalityRates)).collect(Collectors.toList());
		return listOfMortalityRatesDto;
	}
}
