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

import com.leaps.dto.surrender.SurrenderFundDetailsPasDto;
import com.leaps.entity.surrender.SurrenderFundDetailsPas;
import com.leaps.error.ErrorService;
import com.leaps.exceptions.ResourceNotFoundException;
import com.leaps.repository.surrender.SurrenderFundDetailsPasRepository;
import com.leaps.responses.surrender.SurrenderFundDetailsPasResponse;
import com.leaps.service.surrender.SurrenderFundDetailsPasService;

@Service
public class SurrenderFundDetailsPasServiceImpl implements SurrenderFundDetailsPasService {

	// Autowiring Repository to use jpa methods and custom queries
	@Autowired
	private SurrenderFundDetailsPasRepository surrenderFundDetailsPasRepository;

	// Autowiring Error Service to Display the Response Messages Stored in Error
	// Table
	@Autowired
	private ErrorService errorService;

	// Entity to Dto using Function

	public Function<SurrenderFundDetailsPas, SurrenderFundDetailsPasDto> entityToDto = entity -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		mapper.addMappings(new PropertyMap<SurrenderFundDetailsPas, SurrenderFundDetailsPasDto>() {

			@Override
			protected void configure() {
				map().setCompanyName(source.getCompany().getCompanyName());
			}
		});
		SurrenderFundDetailsPasDto dto = mapper.map(entity, SurrenderFundDetailsPasDto.class);
		return dto;
	};

	// Dto to Entity using Function

	public Function<SurrenderFundDetailsPasDto, SurrenderFundDetailsPas> dtoToEntity = dto -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		SurrenderFundDetailsPas entity = mapper.map(dto, SurrenderFundDetailsPas.class);
		return entity;
	};

	// Get all where valid flag=1
	@Override
	public List<SurrenderFundDetailsPasDto> getall() {
		List<SurrenderFundDetailsPas> listOfSurrenderFundDetailsPas = surrenderFundDetailsPasRepository.getallActive();
		List<SurrenderFundDetailsPasDto> listOfSurrenderFundDetailsPasDto = listOfSurrenderFundDetailsPas.stream()
				.map(surrenderFundDetailsPas -> entityToDto.apply(surrenderFundDetailsPas))
				.collect(Collectors.toList());
		return listOfSurrenderFundDetailsPasDto;
	}

	// Get All where valid flag=1 With Pagination
	@Override
	public SurrenderFundDetailsPasResponse getAllWithPagination(int pageNo, int pageSize, String sortBy,
			String sortDir) {
		// sort Condition
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		// Create Pageable Instance
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

		Page<SurrenderFundDetailsPas> surrenderFundDetailsPasPage = surrenderFundDetailsPasRepository
				.getallActivePagination(pageable);

		// get content
		List<SurrenderFundDetailsPas> listOfSurrenderFundDetailsPas = surrenderFundDetailsPasPage.getContent();

		List<SurrenderFundDetailsPasDto> content = listOfSurrenderFundDetailsPas.stream()
				.map(surrenderFundDetailsPas -> entityToDto.apply(surrenderFundDetailsPas))
				.collect(Collectors.toList());

		// Setting the the values to Custom Response Created
		SurrenderFundDetailsPasResponse surrenderFundDetailsPasResponse = new SurrenderFundDetailsPasResponse();
		surrenderFundDetailsPasResponse.setContent(content);
		surrenderFundDetailsPasResponse.setPageNo(surrenderFundDetailsPasPage.getNumber());
		surrenderFundDetailsPasResponse.setPageSize(surrenderFundDetailsPasPage.getSize());
		surrenderFundDetailsPasResponse.setTotalElements(surrenderFundDetailsPasPage.getTotalElements());
		surrenderFundDetailsPasResponse.setTotalPages(surrenderFundDetailsPasPage.getTotalPages());
		surrenderFundDetailsPasResponse.setLast(surrenderFundDetailsPasPage.isLast());

		return surrenderFundDetailsPasResponse;
	}

	// Get Active By id
	@Override
	public SurrenderFundDetailsPasDto getbyid(Long id) {
		SurrenderFundDetailsPas surrenderFundDetailsPas = surrenderFundDetailsPasRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SurrenderFundDetailsPas", "id", id));
		SurrenderFundDetailsPasDto surrenderFundDetailsPasDto = entityToDto.apply(surrenderFundDetailsPas);
		return surrenderFundDetailsPasDto;
	}

	// Add
	@Override
	public String add(SurrenderFundDetailsPasDto dto) {
		SurrenderFundDetailsPas surrenderFundDetailsPas = dtoToEntity.apply(dto);
		surrenderFundDetailsPas.setValidFlag(1);
		surrenderFundDetailsPasRepository.save(surrenderFundDetailsPas);
		return errorService.getErrorById("ER001");
	}

	// Update
	@Override
	public String update(Long id, SurrenderFundDetailsPasDto dto) {
		SurrenderFundDetailsPas entity = dtoToEntity.apply(dto);

		SurrenderFundDetailsPas surrenderFundDetailsPas = surrenderFundDetailsPasRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SurrenderFundDetailsPas", "id", id));

		// Using Optional for Null check
		Optional.of(entity.getCompanyId())
				.ifPresent((DeathClaimCoverDetails) -> surrenderFundDetailsPas.setCompanyId(entity.getCompanyId()));

		Optional.ofNullable(entity.getPolicyNo())
				.ifPresent((SurrenderFundDetailsPas) -> surrenderFundDetailsPas.setPolicyNo(entity.getPolicyNo()));
		Optional.ofNullable(entity.getFundCode())
				.ifPresent((SurrenderFundDetailsPas) -> surrenderFundDetailsPas.setFundCode(entity.getFundCode()));
		Optional.ofNullable(entity.getFundName())
				.ifPresent((SurrenderFundDetailsPas) -> surrenderFundDetailsPas.setFundName(entity.getFundName()));
		Optional.ofNullable(entity.getNavDate())
				.ifPresent((SurrenderFundDetailsPas) -> surrenderFundDetailsPas.setNavDate(entity.getNavDate()));
		Optional.ofNullable(entity.getUnits()).ifPresent(
				(SurrenderFundDetailsPas) -> surrenderFundDetailsPas.setUnits(entity.getUnits()));
		Optional.ofNullable(entity.getRateApp())
				.ifPresent((SurrenderFundDetailsPas) -> surrenderFundDetailsPas.setRateApp(entity.getRateApp()));
		Optional.ofNullable(entity.getValue())
				.ifPresent((SurrenderFundDetailsPas) -> surrenderFundDetailsPas.setValue(entity.getValue()));
		surrenderFundDetailsPasRepository.save(surrenderFundDetailsPas);
		return errorService.getErrorById("ER002");

	}

	// Soft Delete
	@Override
	public String softdelete(Long id) {
		SurrenderFundDetailsPas surrenderFundDetailsPas = surrenderFundDetailsPasRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SurrenderFundDetailsPas", "id", id));
		surrenderFundDetailsPas.setValidFlag(-1);
		surrenderFundDetailsPasRepository.save(surrenderFundDetailsPas);
		return errorService.getErrorById("ER003");
	}

	// Hard Delete
	@Override
	public String harddelete(Long id) {
		SurrenderFundDetailsPas surrenderFundDetailsPas = surrenderFundDetailsPasRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SurrenderFundDetailsPas", "id", id));
		surrenderFundDetailsPasRepository.delete(surrenderFundDetailsPas);
		return errorService.getErrorById("ER003");
	}

	// Global Search
	@Override
	public List<SurrenderFundDetailsPasDto> globalSearch(String key) {
		List<SurrenderFundDetailsPas> listOfSurrenderFundDetailsPas = surrenderFundDetailsPasRepository
				.globalSearch(key);
		List<SurrenderFundDetailsPasDto> listOfSurrenderFundDetailsPasDto = listOfSurrenderFundDetailsPas.stream()
				.map(surrenderFundDetailsPas -> entityToDto.apply(surrenderFundDetailsPas))
				.collect(Collectors.toList());
		return listOfSurrenderFundDetailsPasDto;
	}
}
