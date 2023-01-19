package com.leaps.serviceImpl.admin;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.leaps.dto.admin.CompanyDto;
import com.leaps.entity.admin.Company;
import com.leaps.error.ErrorService;
import com.leaps.exceptions.ResourceNotFoundException;
import com.leaps.repository.admin.CompanyRepository;
import com.leaps.responses.admin.CompanyResponse;
import com.leaps.service.admin.CompanyService;

@Service
public class CompanyServiceImpl implements CompanyService {
        // Autowiring ModelMapper to Convert Entity to Dto and Vice Versa
        @Autowired
        private ModelMapper mapper;

        // Autowiring Repository to use jpa methods and custom queries
        @Autowired
        private CompanyRepository companyRepository;

        // Autowiring Error Service to Display the Response Messages Stored in Error
        // Table
        @Autowired
        private ErrorService errorService;

        // Entity to Dto using Function

        public Function<Company, CompanyDto> entityToDto = entity -> {
                mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
                return mapper.map(entity, CompanyDto.class);
        };

        // Dto to Entity using Function

        public Function<CompanyDto, Company> dtoToEntity = dto -> {
                mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
                return mapper.map(dto, Company.class);
        };

        // Get all where valid flag=1
        @Override
        public List<CompanyDto> getall() {
                List<Company> listOfCompany = companyRepository.getallActive();
                List<CompanyDto> listofCompanyDto = listOfCompany.stream().map(company -> entityToDto.apply(company))
                                .collect(Collectors.toList());
                return listofCompanyDto;
        }

        // Get All where valid flag=1 With Pagination
        @Override
        public CompanyResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir) {
                // sort Condition
                Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                                : Sort.by(sortBy).descending();

                // Create Pageable Instance
                Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

                Page<Company> companyPage = companyRepository.getallActivePagination(pageable);

                // get content
                List<Company> listOfCompany = companyPage.getContent();

                List<CompanyDto> content = listOfCompany.stream().map(company -> entityToDto.apply(company))
                                .collect(Collectors.toList());

                // Setting the the values to Custom Response Created
                CompanyResponse companyResponse = new CompanyResponse();
                companyResponse.setContent(content);
                companyResponse.setPageNo(companyPage.getNumber());
                companyResponse.setPageSize(companyPage.getSize());
                companyResponse.setTotalElements(companyPage.getTotalElements());
                companyResponse.setTotalPages(companyPage.getTotalPages());
                companyResponse.setLast(companyPage.isLast());

                return companyResponse;
        }

        // Get Active By id
        @Override
        public CompanyDto getbyid(Long id) {
                Company company = companyRepository.getActiveById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Company", "id", id));
                CompanyDto CompanyDto = entityToDto.apply(company);
                return CompanyDto;
        }

        // Add
        @Override
        public String add(CompanyDto dto) {
                Company company = dtoToEntity.apply(dto);
                company.setValidFlag(1);
                companyRepository.save(company);
                return errorService.getErrorById("ER001");
        }

        // Update
        @Override
        public String update(Long id, CompanyDto dto) {
                Company entity = dtoToEntity.apply(dto);

                Company company = companyRepository.getActiveById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Company", "id", id));

                // Using Optional for Null check
                Optional.ofNullable(entity.getAddressId())
                                .ifPresent((Company) -> company.setAddressId(entity.getAddressId()));
                Optional.ofNullable(entity.getCompanyCode())
                                .ifPresent((Company) -> company.setCompanyCode(entity.getCompanyCode()));
                Optional.ofNullable(entity.getCompanyName())
                                .ifPresent((Company) -> company.setCompanyName(entity.getCompanyName()));
                Optional.ofNullable(entity.getCompanyShortName())
                                .ifPresent((Company) -> company.setCompanyShortName(entity.getCompanyShortName()));
                Optional.ofNullable(entity.getCompanyLongName())
                                .ifPresent((Company) -> company.setCompanyLongName(entity.getCompanyLongName()));
                Optional.ofNullable(entity.getGst())
                                .ifPresent((Company) -> company.setGst(entity.getGst()));
                Optional.ofNullable(entity.getCin())
                                .ifPresent((Company) -> company.setCin(entity.getCin()));
                Optional.ofNullable(entity.getCinDate())
                                .ifPresent((Company) -> company.setCinDate(entity.getCinDate()));
                Optional.ofNullable(entity.getTin())
                                .ifPresent((Company) -> company.setTin(entity.getTin()));
                Optional.ofNullable(entity.getPan())
                                .ifPresent((Company) -> company.setPan(entity.getPan()));
                Optional.ofNullable(entity.getCompanyLogo())
                                .ifPresent((Company) -> company.setCompanyLogo(entity.getCompanyLogo()));
                companyRepository.save(company);
                return errorService.getErrorById("ER002");

        }

        // Soft Delete
        @Override
        public String softdelete(Long id) {
                Company company = companyRepository.getActiveById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Company", "id", id));
                company.setValidFlag(-1);
                companyRepository.save(company);
                return errorService.getErrorById("ER003");
        }

        // Hard Delete
        @Override
        public String harddelete(Long id) {
                Company company = companyRepository.getActiveById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Company", "id", id));
                companyRepository.delete(company);
                return errorService.getErrorById("ER003");
        }

        // Global Search
        @Override
        public List<CompanyDto> globalSearch(String key) {
                List<Company> listOfCompany = companyRepository.globalSearch(key);
                List<CompanyDto> listofCompanyDto = listOfCompany.stream().map(company -> entityToDto.apply(company))
                                .collect(Collectors.toList());
                return listofCompanyDto;
        }
}
