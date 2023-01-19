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

import com.leaps.dto.master.SsvFactorDto;
import com.leaps.entity.master.SsvFactor;
import com.leaps.error.ErrorService;
import com.leaps.exceptions.ResourceNotFoundException;
import com.leaps.repository.master.SsvFactorRepository;
import com.leaps.responses.master.SsvFactorResponse;
import com.leaps.service.master.SsvFactorService;

@Service
public class SsvFactorServiceImpl implements SsvFactorService {
	// Autowiring Repository to use jpa methods and custom queries
	@Autowired
	private SsvFactorRepository ssvFactorRepository;

	// Autowiring Error Service to Display the Response Messages Stored in Error
	// Table
	@Autowired
	private ErrorService errorService;

	// Entity to Dto using Function

	public Function<SsvFactor, SsvFactorDto> entityToDto = entity -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		mapper.addMappings(new PropertyMap<SsvFactor, SsvFactorDto>() {

			@Override
			protected void configure() {
				map().setCompanyName(source.getCompany().getCompanyName());

			}

		});
		SsvFactorDto dto = mapper.map(entity, SsvFactorDto.class);
		return dto;
	};

	// Dto to Entity using Function

	public Function<SsvFactorDto, SsvFactor> dtoToEntity = dto -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		SsvFactor entity = mapper.map(dto, SsvFactor.class);
		return entity;
	};

	// Get all where valid flag=1
	@Override
	public List<SsvFactorDto> getall() {
		List<SsvFactor> listOfSsvFactor = ssvFactorRepository.getallActive();
		List<SsvFactorDto> listOfSsvFactorDto = listOfSsvFactor.stream().map(ssvFactor -> entityToDto.apply(ssvFactor))
				.collect(Collectors.toList());
		return listOfSsvFactorDto;
	}

	// Get All where valid flag=1 With Pagination
	@Override
	public SsvFactorResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir) {
		// sort Condition
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		// Create Pageable Instance
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

		Page<SsvFactor> ssvFactorPage = ssvFactorRepository.getallActivePagination(pageable);

		// get content
		List<SsvFactor> listOfSsvFactor = ssvFactorPage.getContent();

		List<SsvFactorDto> content = listOfSsvFactor.stream().map(ssvFactor -> entityToDto.apply(ssvFactor))
				.collect(Collectors.toList());

		// Setting the the values to Custom Response Created
		SsvFactorResponse ssvFactorResponse = new SsvFactorResponse();
		ssvFactorResponse.setContent(content);
		ssvFactorResponse.setPageNo(ssvFactorPage.getNumber());
		ssvFactorResponse.setPageSize(ssvFactorPage.getSize());
		ssvFactorResponse.setTotalElements(ssvFactorPage.getTotalElements());
		ssvFactorResponse.setTotalPages(ssvFactorPage.getTotalPages());
		ssvFactorResponse.setLast(ssvFactorPage.isLast());

		return ssvFactorResponse;
	}

	// Get Active By id
	@Override
	public SsvFactorDto getbyid(Long id) {
		SsvFactor ssvFactor = ssvFactorRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SsvFactor", "id", id));
		SsvFactorDto ssvFactorDto = entityToDto.apply(ssvFactor);
		return ssvFactorDto;
	}

	// Add
	@Override
	public String add(SsvFactorDto dto) {
		SsvFactor ssvFactor = dtoToEntity.apply(dto);
		ssvFactor.setValidFlag(1);
		ssvFactorRepository.save(ssvFactor);
		return errorService.getErrorById("ER001");
	}

	// Update
	@Override
	public String update(Long id, SsvFactorDto dto) {
		SsvFactor entity = dtoToEntity.apply(dto);

		SsvFactor ssvFactor = ssvFactorRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SsvFactor", "id", id));

		// Using Optional for Null check
		Optional.of(entity.getCompanyId())
				.ifPresent((DeathClaimCoverDetails) -> ssvFactor.setCompanyId(entity.getCompanyId()));
		Optional.ofNullable(entity.getUinNumber())
				.ifPresent((SsvFactor) -> ssvFactor.setUinNumber(entity.getUinNumber()));
		Optional.ofNullable(entity.getPlanName()).ifPresent((SsvFactor) -> ssvFactor.setPlanName(entity.getPlanName()));
		Optional.ofNullable(entity.getPlanCode()).ifPresent((SsvFactor) -> ssvFactor.setPlanCode(entity.getPlanCode()));
		Optional.ofNullable(entity.getPolicyTerm())
				.ifPresent((SsvFactor) -> ssvFactor.setPolicyTerm(entity.getPolicyTerm()));
		Optional.ofNullable(entity.getPremiumTerm())
				.ifPresent((SsvFactor) -> ssvFactor.setPremiumTerm(entity.getPremiumTerm()));
		Optional.ofNullable(entity.getPremiumType())
				.ifPresent((SsvFactor) -> ssvFactor.setPremiumType(entity.getPremiumType()));

		Optional.ofNullable(entity.getPolicyDuration())
				.ifPresent((SsvFactor) -> ssvFactor.setPolicyDuration(entity.getPolicyDuration()));

		Optional.ofNullable(entity.getRate()).ifPresent((SsvFactor) -> ssvFactor.setRate(entity.getRate()));

		Optional.ofNullable(entity.getStartDate())
				.ifPresent((SsvFactor) -> ssvFactor.setStartDate(entity.getStartDate()));
		Optional.ofNullable(entity.getEndDate()).ifPresent((SsvFactor) -> ssvFactor.setEndDate(entity.getEndDate()));

		ssvFactorRepository.save(ssvFactor);
		return errorService.getErrorById("ER002");

	}

	// Soft Delete
	@Override
	public String softdelete(Long id) {
		SsvFactor ssvFactor = ssvFactorRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SsvFactor", "id", id));
		ssvFactor.setValidFlag(-1);
		ssvFactorRepository.save(ssvFactor);
		return errorService.getErrorById("ER003");
	}

	// Hard Delete
	@Override
	public String harddelete(Long id) {
		SsvFactor ssvFactor = ssvFactorRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SsvFactor", "id", id));
		ssvFactorRepository.delete(ssvFactor);
		return errorService.getErrorById("ER003");
	}

	// Global Search
	@Override
	public List<SsvFactorDto> globalSearch(String key) {
		List<SsvFactor> listOfSsvFactor = ssvFactorRepository.globalSearch(key);
		List<SsvFactorDto> listOfSsvFactorDto = listOfSsvFactor.stream().map(ssvFactor -> entityToDto.apply(ssvFactor))
				.collect(Collectors.toList());
		return listOfSsvFactorDto;
	}
}
