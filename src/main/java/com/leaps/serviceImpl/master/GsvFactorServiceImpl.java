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

import com.leaps.dto.master.GsvFactorDto;
import com.leaps.entity.master.GsvFactor;
import com.leaps.error.ErrorService;
import com.leaps.exceptions.ResourceNotFoundException;
import com.leaps.repository.master.GsvFactorRepository;
import com.leaps.responses.master.GsvFactorResponse;
import com.leaps.service.master.GsvFactorService;

@Service
public class GsvFactorServiceImpl implements GsvFactorService {

	// Autowiring Repository to use jpa methods and custom queries
	@Autowired
	private GsvFactorRepository gsvFactorRepository;

	// Autowiring Error Service to Display the Response Messages Stored in Error
	// Table
	@Autowired
	private ErrorService errorService;

	// Entity to Dto using Function

	public Function<GsvFactor, GsvFactorDto> entityToDto = entity -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		mapper.addMappings(new PropertyMap<GsvFactor, GsvFactorDto>() {

			@Override
			protected void configure() {
				map().setCompanyName(source.getCompany().getCompanyName());

			}

		});
		GsvFactorDto dto = mapper.map(entity, GsvFactorDto.class);
		return dto;
	};

	// Dto to Entity using Function

	public Function<GsvFactorDto, GsvFactor> dtoToEntity = dto -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		GsvFactor entity = mapper.map(dto, GsvFactor.class);
		return entity;
	};

	// Get all where valid flag=1
	@Override
	public List<GsvFactorDto> getall() {
		List<GsvFactor> listOfGsvFactor = gsvFactorRepository.getallActive();
		List<GsvFactorDto> listOfGsvFactorDto = listOfGsvFactor.stream().map(gsvFactor -> entityToDto.apply(gsvFactor))
				.collect(Collectors.toList());
		return listOfGsvFactorDto;
	}

	// Get All where valid flag=1 With Pagination
	@Override
	public GsvFactorResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir) {
		// sort Condition
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		// Create Pageable Instance
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

		Page<GsvFactor> gsvFactorPage = gsvFactorRepository.getallActivePagination(pageable);

		// get content
		List<GsvFactor> listOfGsvFactor = gsvFactorPage.getContent();

		List<GsvFactorDto> content = listOfGsvFactor.stream().map(gsvFactor -> entityToDto.apply(gsvFactor))
				.collect(Collectors.toList());

		// Setting the the values to Custom Response Created
		GsvFactorResponse gsvFactorResponse = new GsvFactorResponse();
		gsvFactorResponse.setContent(content);
		gsvFactorResponse.setPageNo(gsvFactorPage.getNumber());
		gsvFactorResponse.setPageSize(gsvFactorPage.getSize());
		gsvFactorResponse.setTotalElements(gsvFactorPage.getTotalElements());
		gsvFactorResponse.setTotalPages(gsvFactorPage.getTotalPages());
		gsvFactorResponse.setLast(gsvFactorPage.isLast());

		return gsvFactorResponse;
	}

	// Get Active By id
	@Override
	public GsvFactorDto getbyid(Long id) {
		GsvFactor gsvFactor = gsvFactorRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("GsvFactor", "id", id));
		GsvFactorDto gsvFactorDto = entityToDto.apply(gsvFactor);
		return gsvFactorDto;
	}

	// Add
	@Override
	public String add(GsvFactorDto dto) {
		GsvFactor gsvFactor = dtoToEntity.apply(dto);
		gsvFactor.setValidFlag(1);
		gsvFactorRepository.save(gsvFactor);
		return errorService.getErrorById("ER001");
	}

	// Update
	@Override
	public String update(Long id, GsvFactorDto dto) {
		GsvFactor entity = dtoToEntity.apply(dto);

		GsvFactor gsvFactor = gsvFactorRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("GsvFactor", "id", id));

		// Using Optional for Null check
		Optional.of(entity.getCompanyId())
				.ifPresent((DeathClaimCoverDetails) -> gsvFactor.setCompanyId(entity.getCompanyId()));
		Optional.ofNullable(entity.getUinNumber())
				.ifPresent((GsvFactor) -> gsvFactor.setUinNumber(entity.getUinNumber()));
		Optional.ofNullable(entity.getPlanCode()).ifPresent((GsvFactor) -> gsvFactor.setPlanCode(entity.getPlanCode()));
		Optional.ofNullable(entity.getPlanName()).ifPresent((GsvFactor) -> gsvFactor.setPlanName(entity.getPlanName()));
		Optional.ofNullable(entity.getPolicyTerm())
				.ifPresent((GsvFactor) -> gsvFactor.setPolicyTerm(entity.getPolicyTerm()));
		Optional.ofNullable(entity.getPremiumTerm())
				.ifPresent((GsvFactor) -> gsvFactor.setPremiumTerm(entity.getPremiumTerm()));
		Optional.ofNullable(entity.getPremiumType())
				.ifPresent((GsvFactor) -> gsvFactor.setPremiumType(entity.getPremiumType()));
		Optional.ofNullable(entity.getPolicyDuration())
				.ifPresent((GsvFactor) -> gsvFactor.setPolicyDuration(entity.getPolicyDuration()));
		Optional.ofNullable(entity.getRate()).ifPresent((GsvFactor) -> gsvFactor.setRate(entity.getRate()));
		Optional.ofNullable(entity.getStartDate())
				.ifPresent((GsvFactor) -> gsvFactor.setStartDate(entity.getStartDate()));
		Optional.ofNullable(entity.getEndDate()).ifPresent((GsvFactor) -> gsvFactor.setEndDate(entity.getEndDate()));

		gsvFactorRepository.save(gsvFactor);
		return errorService.getErrorById("ER002");

	}

	// Soft Delete
	@Override
	public String softdelete(Long id) {
		GsvFactor gsvFactor = gsvFactorRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("GsvFactor", "id", id));
		gsvFactor.setValidFlag(-1);
		gsvFactorRepository.save(gsvFactor);
		return errorService.getErrorById("ER003");
	}

	// Hard Delete
	@Override
	public String harddelete(Long id) {
		GsvFactor gsvFactor = gsvFactorRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("GsvFactor", "id", id));
		gsvFactorRepository.delete(gsvFactor);
		return errorService.getErrorById("ER003");
	}

	// Global Search
	@Override
	public List<GsvFactorDto> globalSearch(String key) {
		List<GsvFactor> listOfGsvFactor = gsvFactorRepository.globalSearch(key);
		List<GsvFactorDto> listOfGsvFactorDto = listOfGsvFactor.stream().map(gsvFactor -> entityToDto.apply(gsvFactor))
				.collect(Collectors.toList());
		return listOfGsvFactorDto;
	}
}
