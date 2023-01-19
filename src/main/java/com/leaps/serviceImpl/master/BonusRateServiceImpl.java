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

import com.leaps.dto.master.BonusRateDto;
import com.leaps.entity.master.BonusRate;
import com.leaps.error.ErrorService;
import com.leaps.exceptions.ResourceNotFoundException;
import com.leaps.repository.master.BonusRateRepository;
import com.leaps.responses.master.BonusRateResponse;
import com.leaps.service.master.BonusRateService;

@Service
public class BonusRateServiceImpl implements BonusRateService {

	// Autowiring Repository to use jpa methods and custom queries
	@Autowired
	private BonusRateRepository bonusRateRepository;

	// Autowiring Error Service to Display the Response Messages Stored in Error
	// Table
	@Autowired
	private ErrorService errorService;

	// Entity to Dto using Function

	public Function<BonusRate, BonusRateDto> entityToDto = entity -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		mapper.addMappings(new PropertyMap<BonusRate, BonusRateDto>() {

			@Override
			protected void configure() {
				map().setCompanyName(source.getCompany().getCompanyName());

			}

		});
		BonusRateDto dto = mapper.map(entity, BonusRateDto.class);
		return dto;
	};

	// Dto to Entity using Function

	public Function<BonusRateDto, BonusRate> dtoToEntity = dto -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		BonusRate entity = mapper.map(dto, BonusRate.class);
		return entity;
	};

	// Get all where valid flag=1
	@Override
	public List<BonusRateDto> getall() {
		List<BonusRate> listOfBonusRate = bonusRateRepository.getallActive();
		List<BonusRateDto> listOfBonusRateDto = listOfBonusRate.stream().map(bonusRate -> entityToDto.apply(bonusRate))
				.collect(Collectors.toList());
		return listOfBonusRateDto;
	}

	// Get All where valid flag=1 With Pagination
	@Override
	public BonusRateResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir) {
		// sort Condition
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		// Create Pageable Instance
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

		Page<BonusRate> bonusRatePage = bonusRateRepository.getallActivePagination(pageable);

		// get content
		List<BonusRate> listOfBonusRate = bonusRatePage.getContent();

		List<BonusRateDto> content = listOfBonusRate.stream().map(bonusRate -> entityToDto.apply(bonusRate))
				.collect(Collectors.toList());

		// Setting the the values to Custom Response Created
		BonusRateResponse bonusRateResponse = new BonusRateResponse();
		bonusRateResponse.setContent(content);
		bonusRateResponse.setPageNo(bonusRatePage.getNumber());
		bonusRateResponse.setPageSize(bonusRatePage.getSize());
		bonusRateResponse.setTotalElements(bonusRatePage.getTotalElements());
		bonusRateResponse.setTotalPages(bonusRatePage.getTotalPages());
		bonusRateResponse.setLast(bonusRatePage.isLast());

		return bonusRateResponse;
	}

	// Get Active By id
	@Override
	public BonusRateDto getbyid(Long id) {
		BonusRate usergroup = bonusRateRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("BonusRate", "id", id));
		BonusRateDto bonusRateDto = entityToDto.apply(usergroup);
		return bonusRateDto;
	}

	// Add
	@Override
	public String add(BonusRateDto dto) {
		BonusRate bonusRate = dtoToEntity.apply(dto);
		bonusRate.setValidFlag(1);
		bonusRateRepository.save(bonusRate);
		return errorService.getErrorById("ER001");
	}

	// Update
	@Override
	public String update(Long id, BonusRateDto dto) {
		BonusRate entity = dtoToEntity.apply(dto);
		BonusRate bonusRate = bonusRateRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Bonus Rate", "id", id));

		// Using Optional for Null check
		Optional.ofNullable(entity.getCompanyId())
				.ifPresent((BonusRate) -> bonusRate.setCompanyId(entity.getCompanyId()));
		Optional.ofNullable(entity.getUinNumber())
				.ifPresent((BonusRate) -> bonusRate.setUinNumber(entity.getUinNumber()));
		Optional.ofNullable(entity.getPlanCode()).ifPresent((BonusRate) -> bonusRate.setPlanCode(entity.getPlanCode()));
		Optional.ofNullable(entity.getPlanName()).ifPresent((BonusRate) -> bonusRate.setPlanName(entity.getPlanName()));
		Optional.ofNullable(entity.getFinancialYear())
				.ifPresent((BonusRate) -> bonusRate.setFinancialYear(entity.getFinancialYear()));
		Optional.ofNullable(entity.getBonusRate())
				.ifPresent((BonusRate) -> bonusRate.setBonusRate(entity.getBonusRate()));
		Optional.ofNullable(entity.getBonusType())
				.ifPresent((BonusRate) -> bonusRate.setBonusType(entity.getBonusType()));
		Optional.ofNullable(entity.getStartDate())
				.ifPresent((BonusRate) -> bonusRate.setStartDate(entity.getStartDate()));
		Optional.ofNullable(entity.getEndDate()).ifPresent((BonusRate) -> bonusRate.setEndDate(entity.getEndDate()));

		bonusRateRepository.save(bonusRate);
		return errorService.getErrorById("ER002");

	}

	// Soft Delete
	@Override
	public String softdelete(Long id) {
		BonusRate bonusRate = bonusRateRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("BonusRate", "id", id));
		bonusRate.setValidFlag(-1);
		bonusRateRepository.save(bonusRate);
		return errorService.getErrorById("ER003");
	}

	// Hard Delete
	@Override
	public String harddelete(Long id) {
		BonusRate bonusRate = bonusRateRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("BonusRate", "id", id));
		bonusRateRepository.delete(bonusRate);
		return errorService.getErrorById("ER003");
	}

	// Global Search
	@Override
	public List<BonusRateDto> globalSearch(String key) {
		List<BonusRate> listOfBonusRate = bonusRateRepository.globalSearch(key);
		List<BonusRateDto> listOfBonusRateDto = listOfBonusRate.stream().map(bonusRate -> entityToDto.apply(bonusRate))
				.collect(Collectors.toList());
		return listOfBonusRateDto;
	}

}
