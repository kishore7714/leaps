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

import com.leaps.dto.deathclaim.DeathClaimLeapCoverTableDto;
import com.leaps.entity.deathclaim.DeathClaimLeapCoverTable;
import com.leaps.error.ErrorService;
import com.leaps.exceptions.ResourceNotFoundException;
import com.leaps.repository.deathclaim.DeathClaimLeapCoverTableRepository;
import com.leaps.responses.deathclaim.DeathClaimLeapCoverTableResponse;
import com.leaps.service.deathclaim.DeathClaimLeapCoverTableService;

@Service
public class DeathClaimLeapCoverTableServiceImpl implements DeathClaimLeapCoverTableService {

	// Autowiring Repository to use jpa methods and custom queries
	@Autowired
	private DeathClaimLeapCoverTableRepository deathClaimLeapCoverTableRepository;

	// Autowiring Error Service to Display the Response Messages Stored in Error
	// Table
	@Autowired
	private ErrorService errorService;

	// Entity to Dto using Function

	public Function<DeathClaimLeapCoverTable, DeathClaimLeapCoverTableDto> entityToDto = entity -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		mapper.addMappings(new PropertyMap<DeathClaimLeapCoverTable, DeathClaimLeapCoverTableDto>() {

			@Override
			protected void configure() {
				map().setCompanyName(source.getCompany().getCompanyName());

			}

		});
		DeathClaimLeapCoverTableDto dto = mapper.map(entity, DeathClaimLeapCoverTableDto.class);
		return dto;
	};

	// Dto to Entity using Function

	public Function<DeathClaimLeapCoverTableDto, DeathClaimLeapCoverTable> dtoToEntity = dto -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		DeathClaimLeapCoverTable entity = mapper.map(dto, DeathClaimLeapCoverTable.class);
		return entity;
	};

	// Get all where valid flag=1
	@Override
	public List<DeathClaimLeapCoverTableDto> getall() {
		List<DeathClaimLeapCoverTable> listOfDeathClaimLeapCoverTable = deathClaimLeapCoverTableRepository
				.getallActive();
		List<DeathClaimLeapCoverTableDto> listOfDeathClaimLeapCoverTableDto = listOfDeathClaimLeapCoverTable.stream()
				.map(deathClaimLeapCoverTable -> entityToDto.apply(deathClaimLeapCoverTable))
				.collect(Collectors.toList());
		return listOfDeathClaimLeapCoverTableDto;
	}

	// Get All where valid flag=1 With Pagination
	@Override
	public DeathClaimLeapCoverTableResponse getAllWithPagination(int pageNo, int pageSize, String sortBy,
			String sortDir) {
		// sort Condition
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		// Create Pageable Instance
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

		Page<DeathClaimLeapCoverTable> deathClaimLeapCoverTablePage = deathClaimLeapCoverTableRepository
				.getallActivePagination(pageable);

		// get content
		List<DeathClaimLeapCoverTable> listOfDeathClaimLeapCoverTable = deathClaimLeapCoverTablePage.getContent();

		List<DeathClaimLeapCoverTableDto> content = listOfDeathClaimLeapCoverTable.stream()
				.map(deathClaimLeapCoverTable -> entityToDto.apply(deathClaimLeapCoverTable))
				.collect(Collectors.toList());

		// Setting the the values to Custom Response Created
		DeathClaimLeapCoverTableResponse deathClaimLeapCoverTableResponse = new DeathClaimLeapCoverTableResponse();
		deathClaimLeapCoverTableResponse.setContent(content);
		deathClaimLeapCoverTableResponse.setPageNo(deathClaimLeapCoverTablePage.getNumber());
		deathClaimLeapCoverTableResponse.setPageSize(deathClaimLeapCoverTablePage.getSize());
		deathClaimLeapCoverTableResponse.setTotalElements(deathClaimLeapCoverTablePage.getTotalElements());
		deathClaimLeapCoverTableResponse.setTotalPages(deathClaimLeapCoverTablePage.getTotalPages());
		deathClaimLeapCoverTableResponse.setLast(deathClaimLeapCoverTablePage.isLast());

		return deathClaimLeapCoverTableResponse;
	}

	// Get Active By id
	@Override
	public DeathClaimLeapCoverTableDto getbyid(Long id) {
		DeathClaimLeapCoverTable deathClaimCoverTable = deathClaimLeapCoverTableRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("DeathClaimLeapCoverTable", "id", id));
		DeathClaimLeapCoverTableDto deathClaimLeapCoverTableDto = entityToDto.apply(deathClaimCoverTable);
		return deathClaimLeapCoverTableDto;
	}

	// Add
	@Override
	public String add(DeathClaimLeapCoverTableDto dto) {
		DeathClaimLeapCoverTable deathClaimLeapCoverTable = dtoToEntity.apply(dto);
		deathClaimLeapCoverTable.setValidFlag(1);
		deathClaimLeapCoverTableRepository.save(deathClaimLeapCoverTable);
		return errorService.getErrorById("ER001");
	}

	// Update
	@Override
	public String update(Long id, DeathClaimLeapCoverTableDto dto) {
		DeathClaimLeapCoverTable entity = dtoToEntity.apply(dto);

		DeathClaimLeapCoverTable deathClaimCoverTable = deathClaimLeapCoverTableRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("DeathClaimLeapCoverTable", "id", id));

		// Using Optional for Null check
		Optional.of(entity.getCompanyId())
				.ifPresent((DeathClaimCoverDetails) -> deathClaimCoverTable.setCompanyId(entity.getCompanyId()));
		Optional.of(entity.getUinNumber())
				.ifPresent((DeathClaimCoverDetails) -> deathClaimCoverTable.setUinNumber(entity.getUinNumber()));
		Optional.of(entity.getDoc())
				.ifPresent((DeathClaimCoverDetails) -> deathClaimCoverTable.setDoc(entity.getDoc()));
		Optional.of(entity.getRiskComDate())
				.ifPresent((DeathClaimCoverDetails) -> deathClaimCoverTable.setRiskComDate(entity.getRiskComDate()));
		Optional.of(entity.getOriginalSumAssured()).ifPresent(
				(DeathClaimCoverDetails) -> deathClaimCoverTable.setOriginalSumAssured(entity.getOriginalSumAssured()));
		Optional.of(entity.getPasSumAssured()).ifPresent(
				(DeathClaimCoverDetails) -> deathClaimCoverTable.setPasSumAssured(entity.getPasSumAssured()));
		Optional.of(entity.getLeapSumAssured()).ifPresent(
				(DeathClaimCoverDetails) -> deathClaimCoverTable.setLeapSumAssured(entity.getLeapSumAssured()));
		Optional.of(entity.getLeapFlag())
				.ifPresent((DeathClaimCoverDetails) -> deathClaimCoverTable.setLeapFlag(entity.getLeapFlag()));
		Optional.of(entity.getLeapRemark())
				.ifPresent((DeathClaimCoverDetails) -> deathClaimCoverTable.setLeapRemark(entity.getLeapRemark()));
		Optional.of(entity.getQcRemark())
				.ifPresent((DeathClaimCoverDetails) -> deathClaimCoverTable.setQcRemark(entity.getQcRemark()));

		deathClaimLeapCoverTableRepository.save(deathClaimCoverTable);
		return errorService.getErrorById("ER002");

	}

	// Soft Delete
	@Override
	public String softdelete(Long id) {
		DeathClaimLeapCoverTable deathClaimLeapCoverTable = deathClaimLeapCoverTableRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("DeathClaimLeapCoverTable", "id", id));
		deathClaimLeapCoverTable.setValidFlag(-1);
		deathClaimLeapCoverTableRepository.save(deathClaimLeapCoverTable);
		return errorService.getErrorById("ER003");
	}

	// Hard Delete
	@Override
	public String harddelete(Long id) {
		DeathClaimLeapCoverTable deathClaimLeapCoverTable = deathClaimLeapCoverTableRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("DeathClaimLeapCoverTable", "id", id));
		deathClaimLeapCoverTableRepository.delete(deathClaimLeapCoverTable);
		return errorService.getErrorById("ER003");
	}

	// Global Search
	@Override
	public List<DeathClaimLeapCoverTableDto> globalSearch(String key) {
		List<DeathClaimLeapCoverTable> listOfDeathClaimLeapCoverTable = deathClaimLeapCoverTableRepository
				.globalSearch(key);
		List<DeathClaimLeapCoverTableDto> listOfDeathClaimLeapCoverTableDto = listOfDeathClaimLeapCoverTable.stream()
				.map(deathClaimLeapCoverTable -> entityToDto.apply(deathClaimLeapCoverTable))
				.collect(Collectors.toList());
		return listOfDeathClaimLeapCoverTableDto;
	}
}
