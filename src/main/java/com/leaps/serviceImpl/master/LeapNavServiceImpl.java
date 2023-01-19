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

import com.leaps.dto.master.LeapNavDto;
import com.leaps.entity.master.LeapNav;
import com.leaps.error.ErrorService;
import com.leaps.exceptions.ResourceNotFoundException;
import com.leaps.repository.master.LeapNavRepository;
import com.leaps.responses.master.LeapNavResponse;
import com.leaps.service.master.LeapNavService;

@Service
public class LeapNavServiceImpl implements LeapNavService {

	// Autowiring Repository to use jpa methods and custom queries
	@Autowired
	private LeapNavRepository leapNavRepository;

	// Autowiring Error Service to Display the Response Messages Stored in Error
	// Table
	@Autowired
	private ErrorService errorService;

	// Entity to Dto using Function

	public Function<LeapNav, LeapNavDto> entityToDto = entity -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		mapper.addMappings(new PropertyMap<LeapNav, LeapNavDto>() {

			@Override
			protected void configure() {
				map().setCompanyName(source.getCompany().getCompanyName());

			}

		});
		LeapNavDto dto = mapper.map(entity, LeapNavDto.class);
		return dto;
	};

	// Dto to Entity using Function

	public Function<LeapNavDto, LeapNav> dtoToEntity = dto -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		LeapNav entity = mapper.map(dto, LeapNav.class);
		return entity;
	};

	// Get all where valid flag=1
	@Override
	public List<LeapNavDto> getall() {
		List<LeapNav> listOfLeapNav = leapNavRepository.getallActive();
		List<LeapNavDto> listOfLeapNavDto = listOfLeapNav.stream().map(leapNav -> entityToDto.apply(leapNav))
				.collect(Collectors.toList());
		return listOfLeapNavDto;
	}

	// Get All where valid flag=1 With Pagination
	@Override
	public LeapNavResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir) {
		// sort Condition
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		// Create Pageable Instance
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

		Page<LeapNav> leapNavPage = leapNavRepository.getallActivePagination(pageable);

		// get content
		List<LeapNav> listOfLeapNav = leapNavPage.getContent();

		List<LeapNavDto> content = listOfLeapNav.stream().map(leapNav -> entityToDto.apply(leapNav))
				.collect(Collectors.toList());

		// Setting the the values to Custom Response Created
		LeapNavResponse leapNavResponse = new LeapNavResponse();
		leapNavResponse.setContent(content);
		leapNavResponse.setPageNo(leapNavPage.getNumber());
		leapNavResponse.setPageSize(leapNavPage.getSize());
		leapNavResponse.setTotalElements(leapNavPage.getTotalElements());
		leapNavResponse.setTotalPages(leapNavPage.getTotalPages());
		leapNavResponse.setLast(leapNavPage.isLast());

		return leapNavResponse;
	}

	// Get Active By id
	@Override
	public LeapNavDto getbyid(Long id) {
		LeapNav leapNav = leapNavRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("LeapNav", "id", id));
		LeapNavDto leapNavDto = entityToDto.apply(leapNav);
		return leapNavDto;
	}

	// Add
	@Override
	public String add(LeapNavDto dto) {
		LeapNav leapNav = dtoToEntity.apply(dto);
		leapNav.setValidFlag(1);
		leapNavRepository.save(leapNav);
		return errorService.getErrorById("ER001");
	}

	// Update
	@Override
	public String update(Long id, LeapNavDto dto) {
		LeapNav entity = dtoToEntity.apply(dto);

		LeapNav leapNav = leapNavRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("LeapNav", "id", id));

		// Using Optional for Null check
		Optional.of(entity.getCompanyId())
				.ifPresent((DeathClaimCoverDetails) -> leapNav.setCompanyId(entity.getCompanyId()));
		Optional.ofNullable(entity.getFundCode()).ifPresent((LeapNav) -> leapNav.setFundCode(entity.getFundCode()));
		Optional.ofNullable(entity.getFundName()).ifPresent((LeapNav) -> leapNav.setFundName(entity.getFundName()));
		Optional.ofNullable(entity.getNavDate()).ifPresent((LeapNav) -> leapNav.setNavDate(entity.getNavDate()));
		Optional.ofNullable(entity.getRate()).ifPresent((LeapNav) -> leapNav.setRate(entity.getRate()));
		leapNavRepository.save(leapNav);
		return errorService.getErrorById("ER002");

	}

	// Soft Delete
	@Override
	public String softdelete(Long id) {
		LeapNav leapNav = leapNavRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("LeapNav", "id", id));
		leapNav.setValidFlag(-1);
		leapNavRepository.save(leapNav);
		return errorService.getErrorById("ER003");
	}

	// Hard Delete
	@Override
	public String harddelete(Long id) {
		LeapNav leapNav = leapNavRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("LeapNav", "id", id));
		leapNavRepository.delete(leapNav);
		return errorService.getErrorById("ER003");
	}

	// Global Search
	@Override
	public List<LeapNavDto> globalSearch(String key) {
		List<LeapNav> listOfLeapNav = leapNavRepository.globalSearch(key);
		List<LeapNavDto> listOfLeapNavDto = listOfLeapNav.stream().map(leapNav -> entityToDto.apply(leapNav))
				.collect(Collectors.toList());
		return listOfLeapNavDto;
	}
}
