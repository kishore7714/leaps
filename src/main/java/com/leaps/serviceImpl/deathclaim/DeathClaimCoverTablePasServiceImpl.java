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

import com.leaps.dto.deathclaim.DeathClaimCoverTablePasDto;
import com.leaps.entity.deathclaim.DeathClaimCoverTablePas;
import com.leaps.error.ErrorService;
import com.leaps.exceptions.ResourceNotFoundException;
import com.leaps.repository.deathclaim.DeathClaimCoverTablePasRepository;
import com.leaps.responses.deathclaim.DeathClaimCoverTablePasResponse;
import com.leaps.service.deathclaim.DeathClaimCoverTablePasService;

@Service
public class DeathClaimCoverTablePasServiceImpl implements DeathClaimCoverTablePasService {

	// Autowiring Repository to use jpa methods and custom queries
	@Autowired
	private DeathClaimCoverTablePasRepository deathClaimCoverTablePasRepository;

	// Autowiring Error Service to Display the Response Messages Stored in Error
	// Table
	@Autowired
	private ErrorService errorService;

	// Entity to Dto using Function

	public Function<DeathClaimCoverTablePas, DeathClaimCoverTablePasDto> entityToDto = entity -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		mapper.addMappings(new PropertyMap<DeathClaimCoverTablePas, DeathClaimCoverTablePasDto>() {

			@Override
			protected void configure() {
				map().setCompanyName(source.getCompany().getCompanyName());

			}

		});
		DeathClaimCoverTablePasDto dto = mapper.map(entity, DeathClaimCoverTablePasDto.class);
		return dto;
	};

	// Dto to Entity using Function

	public Function<DeathClaimCoverTablePasDto, DeathClaimCoverTablePas> dtoToEntity = dto -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		DeathClaimCoverTablePas entity = mapper.map(dto, DeathClaimCoverTablePas.class);
		return entity;
	};

	// Get all where valid flag=1
	@Override
	public List<DeathClaimCoverTablePasDto> getall() {
		List<DeathClaimCoverTablePas> listOfDeathClaimCoverTablePas = deathClaimCoverTablePasRepository.getallActive();
		List<DeathClaimCoverTablePasDto> listOfDeathClaimCoverTablePasDto = listOfDeathClaimCoverTablePas.stream()
				.map(deathClaimCoverTablePas -> entityToDto.apply(deathClaimCoverTablePas))
				.collect(Collectors.toList());
		return listOfDeathClaimCoverTablePasDto;
	}

	// Get All where valid flag=1 With Pagination
	@Override
	public DeathClaimCoverTablePasResponse getAllWithPagination(int pageNo, int pageSize, String sortBy,
			String sortDir) {
		// sort Condition
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		// Create Pageable Instance
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

		Page<DeathClaimCoverTablePas> deathClaimCoverTablePasPage = deathClaimCoverTablePasRepository
				.getallActivePagination(pageable);

		// get content
		List<DeathClaimCoverTablePas> listOfDeathClaimCoverTablePas = deathClaimCoverTablePasPage.getContent();

		List<DeathClaimCoverTablePasDto> content = listOfDeathClaimCoverTablePas.stream()
				.map(deathClaimCoverTablePas -> entityToDto.apply(deathClaimCoverTablePas))
				.collect(Collectors.toList());

		// Setting the the values to Custom Response Created
		DeathClaimCoverTablePasResponse deathClaimCoverTablePasResponse = new DeathClaimCoverTablePasResponse();
		deathClaimCoverTablePasResponse.setContent(content);
		deathClaimCoverTablePasResponse.setPageNo(deathClaimCoverTablePasPage.getNumber());
		deathClaimCoverTablePasResponse.setPageSize(deathClaimCoverTablePasPage.getSize());
		deathClaimCoverTablePasResponse.setTotalElements(deathClaimCoverTablePasPage.getTotalElements());
		deathClaimCoverTablePasResponse.setTotalPages(deathClaimCoverTablePasPage.getTotalPages());
		deathClaimCoverTablePasResponse.setLast(deathClaimCoverTablePasPage.isLast());

		return deathClaimCoverTablePasResponse;
	}

	// Get Active By id
	@Override
	public DeathClaimCoverTablePasDto getbyid(Long id) {
		DeathClaimCoverTablePas deathClaimCoverTable = deathClaimCoverTablePasRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("DeathClaimCoverTablePas", "id", id));
		DeathClaimCoverTablePasDto deathClaimCoverTablePasDto = entityToDto.apply(deathClaimCoverTable);
		return deathClaimCoverTablePasDto;
	}

	// Add
	@Override
	public String add(DeathClaimCoverTablePasDto dto) {
		DeathClaimCoverTablePas deathClaimCoverTablePas = dtoToEntity.apply(dto);
		deathClaimCoverTablePas.setValidFlag(1);
		deathClaimCoverTablePasRepository.save(deathClaimCoverTablePas);
		return errorService.getErrorById("ER001");
	}

	// Update
	@Override
	public String update(Long id, DeathClaimCoverTablePasDto dto) {
		DeathClaimCoverTablePas entity = dtoToEntity.apply(dto);

		DeathClaimCoverTablePas deathClaimCoverTable = deathClaimCoverTablePasRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("DeathClaimCoverTablePas", "id", id));

		// Using Optional for Null check
		Optional.of(entity.getCompanyId())
				.ifPresent((DeathClaimCoverDetails) -> deathClaimCoverTable.setCompanyId(entity.getCompanyId()));
		Optional.of(entity.getUinNumber())
				.ifPresent((DeathClaimCoverDetails) -> deathClaimCoverTable.setUinNumber(entity.getUinNumber()));
		Optional.of(entity.getDoc())
		.ifPresent((DeathClaimCoverDetails) -> deathClaimCoverTable.setDoc(entity.getDoc()));
		Optional.of(entity.getRiskComDate())
		.ifPresent((DeathClaimCoverDetails) -> deathClaimCoverTable.setRiskComDate(entity.getRiskComDate()));
		Optional.of(entity.getOriginalSumAssured())
		.ifPresent((DeathClaimCoverDetails) -> deathClaimCoverTable.setOriginalSumAssured(entity.getOriginalSumAssured()));
		Optional.of(entity.getPasSumAssured())
		.ifPresent((DeathClaimCoverDetails) -> deathClaimCoverTable.setPasSumAssured(entity.getPasSumAssured()));
		Optional.of(entity.getFlag())
		.ifPresent((DeathClaimCoverDetails) -> deathClaimCoverTable.setFlag(entity.getFlag()));
		Optional.of(entity.getRemark())
		.ifPresent((DeathClaimCoverDetails) -> deathClaimCoverTable.setRemark(entity.getRemark()));
		

		deathClaimCoverTablePasRepository.save(deathClaimCoverTable);
		return errorService.getErrorById("ER002");

	}

	// Soft Delete
	@Override
	public String softdelete(Long id) {
		DeathClaimCoverTablePas deathClaimCoverTablePas = deathClaimCoverTablePasRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("DeathClaimCoverTablePas", "id", id));
		deathClaimCoverTablePas.setValidFlag(-1);
		deathClaimCoverTablePasRepository.save(deathClaimCoverTablePas);
		return errorService.getErrorById("ER003");
	}

	// Hard Delete
	@Override
	public String harddelete(Long id) {
		DeathClaimCoverTablePas deathClaimCoverTablePas = deathClaimCoverTablePasRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("DeathClaimCoverTablePas", "id", id));
		deathClaimCoverTablePasRepository.delete(deathClaimCoverTablePas);
		return errorService.getErrorById("ER003");
	}

	// Global Search
	@Override
	public List<DeathClaimCoverTablePasDto> globalSearch(String key) {
		List<DeathClaimCoverTablePas> listOfDeathClaimCoverTablePas = deathClaimCoverTablePasRepository
				.globalSearch(key);
		List<DeathClaimCoverTablePasDto> listOfDeathClaimCoverTablePasDto = listOfDeathClaimCoverTablePas.stream()
				.map(deathClaimCoverTablePas -> entityToDto.apply(deathClaimCoverTablePas))
				.collect(Collectors.toList());
		return listOfDeathClaimCoverTablePasDto;
	}
}
