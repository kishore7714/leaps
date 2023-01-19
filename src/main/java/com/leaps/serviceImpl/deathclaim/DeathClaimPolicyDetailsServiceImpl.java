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

import com.leaps.dto.deathclaim.DeathClaimPolicyDetailsDto;
import com.leaps.entity.deathclaim.DeathClaimPolicyDetails;
import com.leaps.error.ErrorService;
import com.leaps.exceptions.ResourceNotFoundException;
import com.leaps.repository.deathclaim.DeathClaimPolicyDetailsRepository;
import com.leaps.responses.deathclaim.DeathClaimPolicyDetailsResponse;
import com.leaps.service.deathclaim.DeathClaimPolicyDetailsService;

@Service
public class DeathClaimPolicyDetailsServiceImpl implements DeathClaimPolicyDetailsService{
	
	// Autowiring Repository to use jpa methods and custom queries
				@Autowired
				private DeathClaimPolicyDetailsRepository deathClaimPolicyDetailsRepository;

				// Autowiring Error Service to Display the Response Messages Stored in Error
				// Table
				@Autowired
				private ErrorService errorService;

				// Entity to Dto using Function

				public Function<DeathClaimPolicyDetails, DeathClaimPolicyDetailsDto> entityToDto = entity -> {

					// using Model Mapper to convert entity to dto
					ModelMapper mapper = new ModelMapper();
					mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

					mapper.addMappings(new PropertyMap<DeathClaimPolicyDetails, DeathClaimPolicyDetailsDto>() {

						@Override
						protected void configure() {
							map().setCompanyName(source.getCompany().getCompanyName());

						}

					});
					DeathClaimPolicyDetailsDto dto = mapper.map(entity, DeathClaimPolicyDetailsDto.class);
					return dto;
				};

				// Dto to Entity using Function

				public Function<DeathClaimPolicyDetailsDto, DeathClaimPolicyDetails> dtoToEntity = dto -> {

					// using Model Mapper to convert entity to dto
					ModelMapper mapper = new ModelMapper();
					mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
					DeathClaimPolicyDetails entity = mapper.map(dto, DeathClaimPolicyDetails.class);
					return entity;
				};

				// Get all where valid flag=1
				@Override
				public List<DeathClaimPolicyDetailsDto> getall() {
					List<DeathClaimPolicyDetails> listOfDeathClaimPolicyDetails = deathClaimPolicyDetailsRepository.getallActive();
					List<DeathClaimPolicyDetailsDto> listOfDeathClaimPolicyDetailsDto = listOfDeathClaimPolicyDetails.stream()
							.map(deathClaimPolicyDetails -> entityToDto.apply(deathClaimPolicyDetails))
							.collect(Collectors.toList());
					return listOfDeathClaimPolicyDetailsDto;
				}

				// Get All where valid flag=1 With Pagination
				@Override
				public DeathClaimPolicyDetailsResponse getAllWithPagination(int pageNo, int pageSize, String sortBy,
						String sortDir) {
					// sort Condition
					Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
							: Sort.by(sortBy).descending();

					// Create Pageable Instance
					Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

					Page<DeathClaimPolicyDetails> deathClaimPolicyDetailsPage = deathClaimPolicyDetailsRepository
							.getallActivePagination(pageable);

					// get content
					List<DeathClaimPolicyDetails> listOfDeathClaimPolicyDetails = deathClaimPolicyDetailsPage.getContent();

					List<DeathClaimPolicyDetailsDto> content = listOfDeathClaimPolicyDetails.stream()
							.map(deathClaimPolicyDetails -> entityToDto.apply(deathClaimPolicyDetails))
							.collect(Collectors.toList());

					// Setting the the values to Custom Response Created
					DeathClaimPolicyDetailsResponse deathClaimPolicyDetailsResponse = new DeathClaimPolicyDetailsResponse();
					deathClaimPolicyDetailsResponse.setContent(content);
					deathClaimPolicyDetailsResponse.setPageNo(deathClaimPolicyDetailsPage.getNumber());
					deathClaimPolicyDetailsResponse.setPageSize(deathClaimPolicyDetailsPage.getSize());
					deathClaimPolicyDetailsResponse.setTotalElements(deathClaimPolicyDetailsPage.getTotalElements());
					deathClaimPolicyDetailsResponse.setTotalPages(deathClaimPolicyDetailsPage.getTotalPages());
					deathClaimPolicyDetailsResponse.setLast(deathClaimPolicyDetailsPage.isLast());

					return deathClaimPolicyDetailsResponse;
				}

				// Get Active By id
				@Override
				public DeathClaimPolicyDetailsDto getbyid(Long id) {
					DeathClaimPolicyDetails deathClaimCoverTable = deathClaimPolicyDetailsRepository.getActiveById(id)
							.orElseThrow(() -> new ResourceNotFoundException("DeathClaimPolicyDetails", "id", id));
					DeathClaimPolicyDetailsDto deathClaimPolicyDetailsDto = entityToDto.apply(deathClaimCoverTable);
					return deathClaimPolicyDetailsDto;
				}

				// Add
				@Override
				public String add(DeathClaimPolicyDetailsDto dto) {
					DeathClaimPolicyDetails deathClaimPolicyDetails = dtoToEntity.apply(dto);
					deathClaimPolicyDetails.setValidFlag(1);
					deathClaimPolicyDetailsRepository.save(deathClaimPolicyDetails);
					return errorService.getErrorById("ER001");
				}

				// Update
				@Override
				public String update(Long id, DeathClaimPolicyDetailsDto dto) {
					DeathClaimPolicyDetails entity = dtoToEntity.apply(dto);

					DeathClaimPolicyDetails deathClaimCoverTable = deathClaimPolicyDetailsRepository.getActiveById(id)
							.orElseThrow(() -> new ResourceNotFoundException("DeathClaimPolicyDetails", "id", id));

					// Using Optional for Null check
					Optional.of(entity.getCompanyId())
							.ifPresent((DeathClaimCoverDetails) -> deathClaimCoverTable.setCompanyId(entity.getCompanyId()));
					Optional.of(entity.getBillFreq())
							.ifPresent((DeathClaimCoverDetails) -> deathClaimCoverTable.setBillFreq(entity.getBillFreq()));
					Optional.of(entity.getPolicyNo())
					.ifPresent((DeathClaimCoverDetails) -> deathClaimCoverTable.setPolicyNo(entity.getPolicyNo()));
					Optional.of(entity.getClntNum())
					.ifPresent((DeathClaimCoverDetails) -> deathClaimCoverTable.setClntNum(entity.getClntNum()));
					Optional.of(entity.getDocDate())
					.ifPresent((DeathClaimCoverDetails) -> deathClaimCoverTable.setDocDate(entity.getDocDate()));
					Optional.of(entity.getExtraPremium())
					.ifPresent((DeathClaimCoverDetails) -> deathClaimCoverTable.setExtraPremium(entity.getExtraPremium()));
					Optional.of(entity.getFup())
					.ifPresent((DeathClaimCoverDetails) -> deathClaimCoverTable.setFup(entity.getFup()));
					Optional.of(entity.getInstallmentPremium())
					.ifPresent((DeathClaimCoverDetails) -> deathClaimCoverTable.setInstallmentPremium(entity.getInstallmentPremium()));
					Optional.of(entity.getLaAge())
					.ifPresent((DeathClaimCoverDetails) -> deathClaimCoverTable.setLaAge(entity.getLaAge()));
					Optional.of(entity.getPhAge())
					.ifPresent((DeathClaimCoverDetails) -> deathClaimCoverTable.setPhAge(entity.getPhAge()));
					Optional.of(entity.getSmokerFlag())
					.ifPresent((DeathClaimCoverDetails) -> deathClaimCoverTable.setSmokerFlag(entity.getSmokerFlag()));
					Optional.of(entity.getStatusCode())
					.ifPresent((DeathClaimCoverDetails) -> deathClaimCoverTable.setStatusCode(entity.getStatusCode()));
					Optional.of(entity.getUinNumber())
					.ifPresent((DeathClaimCoverDetails) -> deathClaimCoverTable.setUinNumber(entity.getUinNumber()));
					
					return errorService.getErrorById("ER002");

				}

				// Soft Delete
				@Override
				public String softdelete(Long id) {
					DeathClaimPolicyDetails deathClaimPolicyDetails = deathClaimPolicyDetailsRepository.getActiveById(id)
							.orElseThrow(() -> new ResourceNotFoundException("DeathClaimPolicyDetails", "id", id));
					deathClaimPolicyDetails.setValidFlag(-1);
					deathClaimPolicyDetailsRepository.save(deathClaimPolicyDetails);
					return errorService.getErrorById("ER003");
				}

				// Hard Delete
				@Override
				public String harddelete(Long id) {
					DeathClaimPolicyDetails deathClaimPolicyDetails = deathClaimPolicyDetailsRepository.getActiveById(id)
							.orElseThrow(() -> new ResourceNotFoundException("DeathClaimPolicyDetails", "id", id));
					deathClaimPolicyDetailsRepository.delete(deathClaimPolicyDetails);
					return errorService.getErrorById("ER003");
				}

				// Global Search
				@Override
				public List<DeathClaimPolicyDetailsDto> globalSearch(String key) {
					List<DeathClaimPolicyDetails> listOfDeathClaimPolicyDetails = deathClaimPolicyDetailsRepository
							.globalSearch(key);
					List<DeathClaimPolicyDetailsDto> listOfDeathClaimPolicyDetailsDto = listOfDeathClaimPolicyDetails.stream()
							.map(deathClaimPolicyDetails -> entityToDto.apply(deathClaimPolicyDetails))
							.collect(Collectors.toList());
					return listOfDeathClaimPolicyDetailsDto;
				}
}
