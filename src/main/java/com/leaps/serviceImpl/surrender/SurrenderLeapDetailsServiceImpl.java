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

import com.leaps.dto.surrender.SurrenderLeapDetailsDto;
import com.leaps.entity.surrender.SurrenderLeapDetails;
import com.leaps.error.ErrorService;
import com.leaps.exceptions.ResourceNotFoundException;
import com.leaps.repository.surrender.SurrenderLeapDetailsRepository;
import com.leaps.responses.surrender.SurrenderLeapDetailsResponse;
import com.leaps.service.surrender.SurrenderLeapDetailsService;

@Service
public class SurrenderLeapDetailsServiceImpl implements SurrenderLeapDetailsService {
	// Autowiring Repository to use jpa methods and custom queries
	@Autowired
	private SurrenderLeapDetailsRepository surrenderLeapDetailsRepository;

	// Autowiring Error Service to Display the Response Messages Stored in Error
	// Table
	@Autowired
	private ErrorService errorService;

	// Entity to Dto using Function

	public Function<SurrenderLeapDetails, SurrenderLeapDetailsDto> entityToDto = entity -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		mapper.addMappings(new PropertyMap<SurrenderLeapDetails, SurrenderLeapDetailsDto>() {

			@Override
			protected void configure() {
				map().setCompanyName(source.getCompany().getCompanyName());
			}
		});
		SurrenderLeapDetailsDto dto = mapper.map(entity, SurrenderLeapDetailsDto.class);
		return dto;
	};

	// Dto to Entity using Function

	public Function<SurrenderLeapDetailsDto, SurrenderLeapDetails> dtoToEntity = dto -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		SurrenderLeapDetails entity = mapper.map(dto, SurrenderLeapDetails.class);
		return entity;
	};

	// Get all where valid flag=1
	@Override
	public List<SurrenderLeapDetailsDto> getall() {
		List<SurrenderLeapDetails> listOfSurrenderLeapDetails = surrenderLeapDetailsRepository.getallActive();
		List<SurrenderLeapDetailsDto> listOfSurrenderLeapDetailsDto = listOfSurrenderLeapDetails.stream()
				.map(surrenderLeapDetails -> entityToDto.apply(surrenderLeapDetails)).collect(Collectors.toList());
		return listOfSurrenderLeapDetailsDto;
	}

	// Get All where valid flag=1 With Pagination
	@Override
	public SurrenderLeapDetailsResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir) {
		// sort Condition
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		// Create Pageable Instance
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

		Page<SurrenderLeapDetails> surrenderLeapDetailsPage = surrenderLeapDetailsRepository
				.getallActivePagination(pageable);

		// get content
		List<SurrenderLeapDetails> listOfSurrenderLeapDetails = surrenderLeapDetailsPage.getContent();

		List<SurrenderLeapDetailsDto> content = listOfSurrenderLeapDetails.stream()
				.map(surrenderLeapDetails -> entityToDto.apply(surrenderLeapDetails)).collect(Collectors.toList());

		// Setting the the values to Custom Response Created
		SurrenderLeapDetailsResponse surrenderLeapDetailsResponse = new SurrenderLeapDetailsResponse();
		surrenderLeapDetailsResponse.setContent(content);
		surrenderLeapDetailsResponse.setPageNo(surrenderLeapDetailsPage.getNumber());
		surrenderLeapDetailsResponse.setPageSize(surrenderLeapDetailsPage.getSize());
		surrenderLeapDetailsResponse.setTotalElements(surrenderLeapDetailsPage.getTotalElements());
		surrenderLeapDetailsResponse.setTotalPages(surrenderLeapDetailsPage.getTotalPages());
		surrenderLeapDetailsResponse.setLast(surrenderLeapDetailsPage.isLast());

		return surrenderLeapDetailsResponse;
	}

	// Get Active By id
	@Override
	public SurrenderLeapDetailsDto getbyid(Long id) {
		SurrenderLeapDetails surrenderLeapDetails = surrenderLeapDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SurrenderLeapDetails", "id", id));
		SurrenderLeapDetailsDto surrenderLeapDetailsDto = entityToDto.apply(surrenderLeapDetails);
		return surrenderLeapDetailsDto;
	}

	// Add
	@Override
	public String add(SurrenderLeapDetailsDto dto) {
		SurrenderLeapDetails surrenderLeapDetails = dtoToEntity.apply(dto);
		surrenderLeapDetails.setValidFlag(1);
		surrenderLeapDetailsRepository.save(surrenderLeapDetails);
		return errorService.getErrorById("ER001");
	}

	// Update
	@Override
	public String update(Long id, SurrenderLeapDetailsDto dto) {
		SurrenderLeapDetails entity = dtoToEntity.apply(dto);

		SurrenderLeapDetails surrenderLeapDetails = surrenderLeapDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SurrenderLeapDetails", "id", id));

		// Using Optional for Null check
		Optional.of(entity.getCompanyId())
				.ifPresent((DeathClaimCoverDetails) -> surrenderLeapDetails.setCompanyId(entity.getCompanyId()));

		Optional.ofNullable(entity.getPolicyNo())
				.ifPresent((SurrenderLeapDetails) -> surrenderLeapDetails.setPolicyNo(entity.getPolicyNo()));
		Optional.ofNullable(entity.getTransNo())
				.ifPresent((SurrenderLeapDetails) -> surrenderLeapDetails.setTransNo(entity.getTransNo()));
		Optional.ofNullable(entity.getUinNumber())
				.ifPresent((SurrenderLeapDetails) -> surrenderLeapDetails.setUinNumber(entity.getUinNumber()));
		Optional.ofNullable(entity.getReqDate())
				.ifPresent((SurrenderLeapDetails) -> surrenderLeapDetails.setReqDate(entity.getReqDate()));
		Optional.ofNullable(entity.getLogDate())
				.ifPresent((SurrenderLeapDetails) -> surrenderLeapDetails.setLogDate(entity.getLogDate()));
		Optional.ofNullable(entity.getNoOfDues())
				.ifPresent((SurrenderLeapDetails) -> surrenderLeapDetails.setNoOfDues(entity.getNoOfDues()));
		Optional.ofNullable(entity.getTotalPremium())
				.ifPresent((SurrenderLeapDetails) -> surrenderLeapDetails.setTotalPremium(entity.getTotalPremium()));
		Optional.ofNullable(entity.getValueOfBonus())
				.ifPresent((SurrenderLeapDetails) -> surrenderLeapDetails.setValueOfBonus(entity.getValueOfBonus()));
		Optional.ofNullable(entity.getCvbFactor())
				.ifPresent((SurrenderLeapDetails) -> surrenderLeapDetails.setCvbFactor(entity.getCvbFactor()));
		Optional.ofNullable(entity.getGsvFactor())
				.ifPresent((SurrenderLeapDetails) -> surrenderLeapDetails.setGsvFactor(entity.getGsvFactor()));
		Optional.ofNullable(entity.getGsvGross())
				.ifPresent((SurrenderLeapDetails) -> surrenderLeapDetails.setGsvGross(entity.getGsvGross()));
		Optional.ofNullable(entity.getSbPaid())
				.ifPresent((SurrenderLeapDetails) -> surrenderLeapDetails.setSbPaid(entity.getSbPaid()));
		Optional.ofNullable(entity.getGsvNet())
				.ifPresent((SurrenderLeapDetails) -> surrenderLeapDetails.setGsvNet(entity.getGsvNet()));
		Optional.ofNullable(entity.getPaidUpValue())
				.ifPresent((SurrenderLeapDetails) -> surrenderLeapDetails.setPaidUpValue(entity.getPaidUpValue()));
		Optional.ofNullable(entity.getReversionaryBonus()).ifPresent(
				(SurrenderLeapDetails) -> surrenderLeapDetails.setReversionaryBonus(entity.getReversionaryBonus()));
		Optional.ofNullable(entity.getInterimBonus())
				.ifPresent((SurrenderLeapDetails) -> surrenderLeapDetails.setInterimBonus(entity.getInterimBonus()));
		Optional.ofNullable(entity.getGuaranteedBonus()).ifPresent(
				(SurrenderLeapDetails) -> surrenderLeapDetails.setGuaranteedBonus(entity.getGuaranteedBonus()));
		Optional.ofNullable(entity.getTerminalBonus())
				.ifPresent((SurrenderLeapDetails) -> surrenderLeapDetails.setTerminalBonus(entity.getTerminalBonus()));
		Optional.ofNullable(entity.getSsvGrossAmount()).ifPresent(
				(SurrenderLeapDetails) -> surrenderLeapDetails.setSsvGrossAmount(entity.getSsvGrossAmount()));
		Optional.ofNullable(entity.getSsvFactor())
				.ifPresent((SurrenderLeapDetails) -> surrenderLeapDetails.setSsvFactor(entity.getSsvFactor()));
		Optional.ofNullable(entity.getSsvNet())
				.ifPresent((SurrenderLeapDetails) -> surrenderLeapDetails.setSsvNet(entity.getSsvNet()));
		Optional.ofNullable(entity.getSsvOrGsv())
				.ifPresent((SurrenderLeapDetails) -> surrenderLeapDetails.setSsvOrGsv(entity.getSsvOrGsv()));
		Optional.ofNullable(entity.getFundValue())
				.ifPresent((SurrenderLeapDetails) -> surrenderLeapDetails.setFundValue(entity.getFundValue()));
		Optional.ofNullable(entity.getEffDate())
				.ifPresent((SurrenderLeapDetails) -> surrenderLeapDetails.setEffDate(entity.getEffDate()));
		Optional.ofNullable(entity.getPolicyDeposit())
				.ifPresent((SurrenderLeapDetails) -> surrenderLeapDetails.setPolicyDeposit(entity.getPolicyDeposit()));
		Optional.ofNullable(entity.getPenalInterest())
				.ifPresent((SurrenderLeapDetails) -> surrenderLeapDetails.setPenalInterest(entity.getPenalInterest()));
		Optional.ofNullable(entity.getGrossPay())
				.ifPresent((SurrenderLeapDetails) -> surrenderLeapDetails.setGrossPay(entity.getGrossPay()));
		Optional.ofNullable(entity.getCdaCharge())
				.ifPresent((SurrenderLeapDetails) -> surrenderLeapDetails.setCdaCharge(entity.getCdaCharge()));
		Optional.ofNullable(entity.getUlipSurrenderCharge()).ifPresent(
				(SurrenderLeapDetails) -> surrenderLeapDetails.setUlipSurrenderCharge(entity.getUlipSurrenderCharge()));
		Optional.ofNullable(entity.getTotalRecovery())
				.ifPresent((SurrenderLeapDetails) -> surrenderLeapDetails.setTotalRecovery(entity.getTotalRecovery()));
		Optional.ofNullable(entity.getTds())
				.ifPresent((SurrenderLeapDetails) -> surrenderLeapDetails.setTds(entity.getTds()));
		Optional.ofNullable(entity.getNetPayable())
				.ifPresent((SurrenderLeapDetails) -> surrenderLeapDetails.setNetPayable(entity.getNetPayable()));
		Optional.ofNullable(entity.getPolicyLoan())
				.ifPresent((SurrenderLeapDetails) -> surrenderLeapDetails.setPolicyLoan(entity.getPolicyLoan()));
		Optional.ofNullable(entity.getLoanInterest())
				.ifPresent((SurrenderLeapDetails) -> surrenderLeapDetails.setLoanInterest(entity.getLoanInterest()));
		Optional.ofNullable(entity.getCashValueBonus()).ifPresent(
				(SurrenderLeapDetails) -> surrenderLeapDetails.setCashValueBonus(entity.getCashValueBonus()));
		Optional.ofNullable(entity.getMakerFlag())
				.ifPresent((SurrenderLeapDetails) -> surrenderLeapDetails.setMakerFlag(entity.getMakerFlag()));
		Optional.ofNullable(entity.getCheckerFlag())
				.ifPresent((SurrenderLeapDetails) -> surrenderLeapDetails.setCheckerFlag(entity.getCheckerFlag()));
		Optional.ofNullable(entity.getPfFlag())
				.ifPresent((SurrenderLeapDetails) -> surrenderLeapDetails.setPfFlag(entity.getPfFlag()));
		Optional.ofNullable(entity.getPfRemarks())
				.ifPresent((SurrenderLeapDetails) -> surrenderLeapDetails.setPfRemarks(entity.getPfRemarks()));
		Optional.ofNullable(entity.getLeapApprovalFlag()).ifPresent(
				(SurrenderLeapDetails) -> surrenderLeapDetails.setLeapApprovalFlag(entity.getLeapApprovalFlag()));
		Optional.ofNullable(entity.getLeapApprovalRemarks()).ifPresent(
				(SurrenderLeapDetails) -> surrenderLeapDetails.setLeapApprovalRemarks(entity.getLeapApprovalRemarks()));
		Optional.ofNullable(entity.getLeapApprovalDate()).ifPresent(
				(SurrenderLeapDetails) -> surrenderLeapDetails.setLeapApprovalDate(entity.getLeapApprovalDate()));
		Optional.ofNullable(entity.getApprovalQcUserId()).ifPresent(
				(SurrenderLeapDetails) -> surrenderLeapDetails.setApprovalQcUserId(entity.getApprovalQcUserId()));

		surrenderLeapDetailsRepository.save(surrenderLeapDetails);
		return errorService.getErrorById("ER002");

	}

	// Soft Delete
	@Override
	public String softdelete(Long id) {
		SurrenderLeapDetails surrenderLeapDetails = surrenderLeapDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SurrenderLeapDetails", "id", id));
		surrenderLeapDetails.setValidFlag(-1);
		surrenderLeapDetailsRepository.save(surrenderLeapDetails);
		return errorService.getErrorById("ER003");
	}

	// Hard Delete
	@Override
	public String harddelete(Long id) {
		SurrenderLeapDetails surrenderLeapDetails = surrenderLeapDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SurrenderLeapDetails", "id", id));
		surrenderLeapDetailsRepository.delete(surrenderLeapDetails);
		return errorService.getErrorById("ER003");
	}

	// Global Search
	@Override
	public List<SurrenderLeapDetailsDto> globalSearch(String key) {
		List<SurrenderLeapDetails> listOfSurrenderLeapDetails = surrenderLeapDetailsRepository.globalSearch(key);
		List<SurrenderLeapDetailsDto> listOfSurrenderLeapDetailsDto = listOfSurrenderLeapDetails.stream()
				.map(surrenderLeapDetails -> entityToDto.apply(surrenderLeapDetails)).collect(Collectors.toList());
		return listOfSurrenderLeapDetailsDto;
	}
}
