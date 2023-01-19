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

import com.leaps.dto.deathclaim.DeathClaimLeapParameterDto;
import com.leaps.entity.deathclaim.DeathClaimLeapParameter;
import com.leaps.error.ErrorService;
import com.leaps.exceptions.ResourceNotFoundException;
import com.leaps.repository.deathclaim.DeathClaimLeapParameterRepository;
import com.leaps.responses.deathclaim.DeathClaimLeapParameterResponse;
import com.leaps.service.deathclaim.DeathClaimLeapParameterService;

@Service
public class DeathClaimLeapParameterServiceImpl implements DeathClaimLeapParameterService{
	
	// Autowiring Repository to use jpa methods and custom queries
			@Autowired
			private DeathClaimLeapParameterRepository deathClaimLeapParameterRepository;

			// Autowiring Error Service to Display the Response Messages Stored in Error
			// Table
			@Autowired
			private ErrorService errorService;

			// Entity to Dto using Function

			public Function<DeathClaimLeapParameter, DeathClaimLeapParameterDto> entityToDto = entity -> {

				// using Model Mapper to convert entity to dto
				ModelMapper mapper = new ModelMapper();
				mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

				mapper.addMappings(new PropertyMap<DeathClaimLeapParameter, DeathClaimLeapParameterDto>() {

					@Override
					protected void configure() {
						map().setCompanyName(source.getCompany().getCompanyName());

					}

				});
				DeathClaimLeapParameterDto dto = mapper.map(entity, DeathClaimLeapParameterDto.class);
				return dto;
			};

			// Dto to Entity using Function

			public Function<DeathClaimLeapParameterDto, DeathClaimLeapParameter> dtoToEntity = dto -> {

				// using Model Mapper to convert entity to dto
				ModelMapper mapper = new ModelMapper();
				mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
				DeathClaimLeapParameter entity = mapper.map(dto, DeathClaimLeapParameter.class);
				return entity;
			};

			// Get all where valid flag=1
			@Override
			public List<DeathClaimLeapParameterDto> getall() {
				List<DeathClaimLeapParameter> listOfDeathClaimLeapParameter = deathClaimLeapParameterRepository.getallActive();
				List<DeathClaimLeapParameterDto> listOfDeathClaimLeapParameterDto = listOfDeathClaimLeapParameter.stream()
						.map(deathClaimLeapParameter -> entityToDto.apply(deathClaimLeapParameter))
						.collect(Collectors.toList());
				return listOfDeathClaimLeapParameterDto;
			}

			// Get All where valid flag=1 With Pagination
			@Override
			public DeathClaimLeapParameterResponse getAllWithPagination(int pageNo, int pageSize, String sortBy,
					String sortDir) {
				// sort Condition
				Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
						: Sort.by(sortBy).descending();

				// Create Pageable Instance
				Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

				Page<DeathClaimLeapParameter> deathClaimLeapParameterPage = deathClaimLeapParameterRepository
						.getallActivePagination(pageable);

				// get content
				List<DeathClaimLeapParameter> listOfDeathClaimLeapParameter = deathClaimLeapParameterPage.getContent();

				List<DeathClaimLeapParameterDto> content = listOfDeathClaimLeapParameter.stream()
						.map(deathClaimLeapParameter -> entityToDto.apply(deathClaimLeapParameter))
						.collect(Collectors.toList());

				// Setting the the values to Custom Response Created
				DeathClaimLeapParameterResponse deathClaimLeapParameterResponse = new DeathClaimLeapParameterResponse();
				deathClaimLeapParameterResponse.setContent(content);
				deathClaimLeapParameterResponse.setPageNo(deathClaimLeapParameterPage.getNumber());
				deathClaimLeapParameterResponse.setPageSize(deathClaimLeapParameterPage.getSize());
				deathClaimLeapParameterResponse.setTotalElements(deathClaimLeapParameterPage.getTotalElements());
				deathClaimLeapParameterResponse.setTotalPages(deathClaimLeapParameterPage.getTotalPages());
				deathClaimLeapParameterResponse.setLast(deathClaimLeapParameterPage.isLast());

				return deathClaimLeapParameterResponse;
			}

			// Get Active By id
			@Override
			public DeathClaimLeapParameterDto getbyid(Long id) {
				DeathClaimLeapParameter deathClaimCoverTable = deathClaimLeapParameterRepository.getActiveById(id)
						.orElseThrow(() -> new ResourceNotFoundException("DeathClaimLeapParameter", "id", id));
				DeathClaimLeapParameterDto deathClaimLeapParameterDto = entityToDto.apply(deathClaimCoverTable);
				return deathClaimLeapParameterDto;
			}

			// Add
			@Override
			public String add(DeathClaimLeapParameterDto dto) {
				DeathClaimLeapParameter deathClaimLeapParameter = dtoToEntity.apply(dto);
				deathClaimLeapParameter.setValidFlag(1);
				deathClaimLeapParameterRepository.save(deathClaimLeapParameter);
				return errorService.getErrorById("ER001");
			}

			// Update
			@Override
			public String update(Long id, DeathClaimLeapParameterDto dto) {
				DeathClaimLeapParameter entity = dtoToEntity.apply(dto);

				DeathClaimLeapParameter deathClaimCoverTable = deathClaimLeapParameterRepository.getActiveById(id)
						.orElseThrow(() -> new ResourceNotFoundException("DeathClaimLeapParameter", "id", id));

				// Using Optional for Null check
				Optional.of(entity.getCompanyId())
						.ifPresent((DeathClaimCoverDetails) -> deathClaimCoverTable.setCompanyId(entity.getCompanyId()));
				Optional.of(entity.getBasicSa())
						.ifPresent((DeathClaimCoverDetails) -> deathClaimCoverTable.setBasicSa(entity.getBasicSa()));
				Optional.of(entity.getIncreaseSaYears())
				.ifPresent((DeathClaimCoverDetails) -> deathClaimCoverTable.setIncreaseSaYears(entity.getIncreaseSaYears()));
				Optional.of(entity.getPercentageSaIncrease())
				.ifPresent((DeathClaimCoverDetails) -> deathClaimCoverTable.setPercentageSaIncrease(entity.getPercentageSaIncrease()));
				Optional.of(entity.getReversionaryBonus())
				.ifPresent((DeathClaimCoverDetails) -> deathClaimCoverTable.setReversionaryBonus(entity.getReversionaryBonus()));
				Optional.of(entity.getLoyaltyBonus())
				.ifPresent((DeathClaimCoverDetails) -> deathClaimCoverTable.setLoyaltyBonus(entity.getLoyaltyBonus()));
				Optional.of(entity.getGuaranteedBonus())
				.ifPresent((DeathClaimCoverDetails) -> deathClaimCoverTable.setGuaranteedBonus(entity.getGuaranteedBonus()));
				Optional.of(entity.getTerminalBonus())
				.ifPresent((DeathClaimCoverDetails) -> deathClaimCoverTable.setTerminalBonus(entity.getTerminalBonus()));
				Optional.of(entity.getSuicideClause())
				.ifPresent((DeathClaimCoverDetails) -> deathClaimCoverTable.setSuicideClause(entity.getSuicideClause()));
				Optional.of(entity.getWaitingPeriod())
				.ifPresent((DeathClaimCoverDetails) -> deathClaimCoverTable.setWaitingPeriod(entity.getWaitingPeriod()));
				Optional.of(entity.getRefundOfAdminFee())
				.ifPresent((DeathClaimCoverDetails) -> deathClaimCoverTable.setRefundOfAdminFee(entity.getRefundOfAdminFee()));
				Optional.of(entity.getRefundOfMc())
				.ifPresent((DeathClaimCoverDetails) -> deathClaimCoverTable.setRefundOfMc(entity.getRefundOfMc()));
				Optional.of(entity.getRefundOfGuaranteedCharges())
				.ifPresent((DeathClaimCoverDetails) -> deathClaimCoverTable.setRefundOfGuaranteedCharges(entity.getRefundOfGuaranteedCharges()));
				Optional.of(entity.getReturnOfPremiums())
				.ifPresent((DeathClaimCoverDetails) -> deathClaimCoverTable.setReturnOfPremiums(entity.getReturnOfPremiums()));
				Optional.of(entity.getFundvalueSaPayable())
				.ifPresent((DeathClaimCoverDetails) -> deathClaimCoverTable.setFundvalueSaPayable(entity.getFundvalueSaPayable()));
				deathClaimLeapParameterRepository.save(deathClaimCoverTable);
				Optional.of(entity.getClaimConcession())
				.ifPresent((DeathClaimCoverDetails) -> deathClaimCoverTable.setClaimConcession(entity.getClaimConcession()));
				Optional.of(entity.getTdsRate())
				.ifPresent((DeathClaimCoverDetails) -> deathClaimCoverTable.setTdsRate(entity.getTdsRate()));
				Optional.of(entity.getTdsType())
				.ifPresent((DeathClaimCoverDetails) -> deathClaimCoverTable.setTdsType(entity.getTdsType()));
				return errorService.getErrorById("ER002");

			}

			// Soft Delete
			@Override
			public String softdelete(Long id) {
				DeathClaimLeapParameter deathClaimLeapParameter = deathClaimLeapParameterRepository.getActiveById(id)
						.orElseThrow(() -> new ResourceNotFoundException("DeathClaimLeapParameter", "id", id));
				deathClaimLeapParameter.setValidFlag(-1);
				deathClaimLeapParameterRepository.save(deathClaimLeapParameter);
				return errorService.getErrorById("ER003");
			}

			// Hard Delete
			@Override
			public String harddelete(Long id) {
				DeathClaimLeapParameter deathClaimLeapParameter = deathClaimLeapParameterRepository.getActiveById(id)
						.orElseThrow(() -> new ResourceNotFoundException("DeathClaimLeapParameter", "id", id));
				deathClaimLeapParameterRepository.delete(deathClaimLeapParameter);
				return errorService.getErrorById("ER003");
			}

			// Global Search
			@Override
			public List<DeathClaimLeapParameterDto> globalSearch(String key) {
				List<DeathClaimLeapParameter> listOfDeathClaimLeapParameter = deathClaimLeapParameterRepository
						.globalSearch(key);
				List<DeathClaimLeapParameterDto> listOfDeathClaimLeapParameterDto = listOfDeathClaimLeapParameter.stream()
						.map(deathClaimLeapParameter -> entityToDto.apply(deathClaimLeapParameter))
						.collect(Collectors.toList());
				return listOfDeathClaimLeapParameterDto;
			}
}
