package com.leaps.serviceImpl.flc;

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

import com.leaps.dto.flc.FlcFundDetailsPasDto;
import com.leaps.entity.flc.FlcFundDetailsPas;
import com.leaps.error.ErrorService;
import com.leaps.exceptions.ResourceNotFoundException;
import com.leaps.repository.flc.FlcFundDetailsPasRepository;
import com.leaps.responses.flc.FlcFundDetailsPasResponse;
import com.leaps.service.flc.FlcFundDetailsPasService;

@Service
public class FlcFundDetailsPasServiceImpl implements FlcFundDetailsPasService {

	// Autowiring Repository to use jpa methods and custom queries
	@Autowired
	private FlcFundDetailsPasRepository flcFundDetailsPasRepository;

	// Autowiring Error Service to Display the Response Messages Stored in Error
	// Table
	@Autowired
	private ErrorService errorService;

	// Entity to Dto using Function

	public Function<FlcFundDetailsPas, FlcFundDetailsPasDto> entityToDto = entity -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		mapper.addMappings(new PropertyMap<FlcFundDetailsPas, FlcFundDetailsPasDto>() {

			@Override
			protected void configure() {
				map().setCompanyName(source.getCompany().getCompanyName());

			}

		});
		FlcFundDetailsPasDto dto = mapper.map(entity, FlcFundDetailsPasDto.class);
		return dto;
	};

	// Dto to Entity using Function

	public Function<FlcFundDetailsPasDto, FlcFundDetailsPas> dtoToEntity = dto -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		FlcFundDetailsPas entity = mapper.map(dto, FlcFundDetailsPas.class);
		return entity;
	};

	// Get all where valid flag=1
	@Override
	public List<FlcFundDetailsPasDto> getall() {
		List<FlcFundDetailsPas> listOfFlcFundDetailsPas = flcFundDetailsPasRepository.getallActive();
		List<FlcFundDetailsPasDto> listOfFlcFundDetailsPasDto = listOfFlcFundDetailsPas.stream()
				.map(flcFundDetailsPas -> entityToDto.apply(flcFundDetailsPas)).collect(Collectors.toList());
		return listOfFlcFundDetailsPasDto;
	}

	// Get All where valid flag=1 With Pagination
	@Override
	public FlcFundDetailsPasResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir) {
		// sort Condition
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		// Create Pageable Instance
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

		Page<FlcFundDetailsPas> flcFundDetailsPasPage = flcFundDetailsPasRepository.getallActivePagination(pageable);

		// get content
		List<FlcFundDetailsPas> listOfFlcFundDetailsPas = flcFundDetailsPasPage.getContent();

		List<FlcFundDetailsPasDto> content = listOfFlcFundDetailsPas.stream()
				.map(flcFundDetailsPas -> entityToDto.apply(flcFundDetailsPas)).collect(Collectors.toList());

		// Setting the the values to Custom Response Created
		FlcFundDetailsPasResponse flcFundDetailsPasResponse = new FlcFundDetailsPasResponse();
		flcFundDetailsPasResponse.setContent(content);
		flcFundDetailsPasResponse.setPageNo(flcFundDetailsPasPage.getNumber());
		flcFundDetailsPasResponse.setPageSize(flcFundDetailsPasPage.getSize());
		flcFundDetailsPasResponse.setTotalElements(flcFundDetailsPasPage.getTotalElements());
		flcFundDetailsPasResponse.setTotalPages(flcFundDetailsPasPage.getTotalPages());
		flcFundDetailsPasResponse.setLast(flcFundDetailsPasPage.isLast());

		return flcFundDetailsPasResponse;
	}

	// Get Active By id
	@Override
	public FlcFundDetailsPasDto getbyid(Long id) {
		FlcFundDetailsPas flcFundDetailsPas = flcFundDetailsPasRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FlcFundDetailsPas", "id", id));
		FlcFundDetailsPasDto flcFundDetailsPasDto = entityToDto.apply(flcFundDetailsPas);
		return flcFundDetailsPasDto;
	}

	// Add
	@Override
	public String add(FlcFundDetailsPasDto dto) {
		FlcFundDetailsPas flcFundDetailsPas = dtoToEntity.apply(dto);
		flcFundDetailsPas.setValidFlag(1);
		flcFundDetailsPasRepository.save(flcFundDetailsPas);
		return errorService.getErrorById("ER001");
	}

	// Update
	@Override
	public String update(Long id, FlcFundDetailsPasDto dto) {
		FlcFundDetailsPas entity = dtoToEntity.apply(dto);

		FlcFundDetailsPas flcFundDetailsPas = flcFundDetailsPasRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FlcFundDetailsPas", "id", id));

		// Using Optional for Null check
		Optional.of(entity.getCompanyId())
				.ifPresent((DeathClaimCoverDetails) -> flcFundDetailsPas.setCompanyId(entity.getCompanyId()));

		Optional.ofNullable(entity.getPolicyNo())
				.ifPresent((FlcFundDetailsPas) -> flcFundDetailsPas.setPolicyNo(entity.getPolicyNo()));
		Optional.ofNullable(entity.getFundCode())
				.ifPresent((FlcFundDetailsPas) -> flcFundDetailsPas.setFundCode(entity.getFundCode()));
		Optional.ofNullable(entity.getFundName())
				.ifPresent((FlcFundDetailsPas) -> flcFundDetailsPas.setFundName(entity.getFundName()));
		Optional.ofNullable(entity.getRateApp())
				.ifPresent((FlcFundDetailsPas) -> flcFundDetailsPas.setRateApp(entity.getRateApp()));
		Optional.ofNullable(entity.getUnits())
				.ifPresent((FlcFundDetailsPas) -> flcFundDetailsPas.setUnits(entity.getUnits()));
		Optional.ofNullable(entity.getValue())
				.ifPresent((FlcFundDetailsPas) -> flcFundDetailsPas.setValue(entity.getValue()));
		Optional.ofNullable(entity.getNavDate())
				.ifPresent((FlcFundDetailsPas) -> flcFundDetailsPas.setNavDate(entity.getNavDate()));

		flcFundDetailsPasRepository.save(flcFundDetailsPas);
		return errorService.getErrorById("ER002");

	}

	// Soft Delete
	@Override
	public String softdelete(Long id) {
		FlcFundDetailsPas flcFundDetailsPas = flcFundDetailsPasRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FlcFundDetailsPas", "id", id));
		flcFundDetailsPas.setValidFlag(-1);
		flcFundDetailsPasRepository.save(flcFundDetailsPas);
		return errorService.getErrorById("ER003");
	}

	// Hard Delete
	@Override
	public String harddelete(Long id) {
		FlcFundDetailsPas flcFundDetailsPas = flcFundDetailsPasRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FlcFundDetailsPas", "id", id));
		flcFundDetailsPasRepository.delete(flcFundDetailsPas);
		return errorService.getErrorById("ER003");
	}

	// Global Search
	@Override
	public List<FlcFundDetailsPasDto> globalSearch(String key) {
		List<FlcFundDetailsPas> listOfFlcFundDetailsPas = flcFundDetailsPasRepository.globalSearch(key);
		List<FlcFundDetailsPasDto> listOfFlcFundDetailsPasDto = listOfFlcFundDetailsPas.stream()
				.map(flcFundDetailsPas -> entityToDto.apply(flcFundDetailsPas)).collect(Collectors.toList());
		return listOfFlcFundDetailsPasDto;
	}
}
