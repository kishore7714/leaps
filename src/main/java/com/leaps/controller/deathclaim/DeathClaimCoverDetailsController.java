package com.leaps.controller.deathclaim;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.leaps.dto.deathclaim.DeathClaimCoverDetailsDto;
import com.leaps.responses.deathclaim.DeathClaimCoverDetailsResponse;
import com.leaps.serviceImpl.deathclaim.DeathClaimCoverDetailsServiceImpl;
import com.leaps.utilities.AppConstants;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/deathClaimCoverDetails")
public class DeathClaimCoverDetailsController {
	private static final String DEFAULT_SORT_DIRECTION = AppConstants.DEFAULT_SORT_DIRECTION;
    private static final String DEFAULT_SORT_BY = AppConstants.DEFAULT_SORT_BY;
    private static final String DEFAULT_PAGE_SIZE = AppConstants.DEFAULT_PAGE_SIZE;
    private static final String DEFAULT_PAGE_NUMBER = AppConstants.DEFAULT_PAGE_NUMBER;

    @Autowired
    private DeathClaimCoverDetailsServiceImpl deathClaimCoverDetailsService;

    // Get-all with Pagination
    @GetMapping("/getall-pagination")
    public ResponseEntity<DeathClaimCoverDetailsResponse> getallWithPagination(
            @RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        HttpHeaders header = new HttpHeaders();
        DeathClaimCoverDetailsResponse deathClaimCoverDetails = deathClaimCoverDetailsService.getAllWithPagination(pageNo, pageSize, sortBy, sortDir);
        header.add("description", "DeathClaimCoverDetails-getall-pagination");
        return new ResponseEntity<>(deathClaimCoverDetails, header, HttpStatus.OK);
        // return ResponseEntity.status(HttpStatus.OK).headers(header).body(deathClaimCoverDetails);
    }

    // Get-all
    @GetMapping("/getall")
    public ResponseEntity<List<DeathClaimCoverDetailsDto>> getall() {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "getall");
        List<DeathClaimCoverDetailsDto> deathClaimCoverDetails = deathClaimCoverDetailsService.getall();
        return new ResponseEntity<>(deathClaimCoverDetails, header, HttpStatus.OK);
    }

    // Get By Id
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<DeathClaimCoverDetailsDto> getbyid(@PathVariable Long id) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "get-by-id");
        header.add("type", "DeathClaimCoverDetails Object");
        DeathClaimCoverDetailsDto deathClaimCoverDetails = deathClaimCoverDetailsService.getbyid(id);
        return new ResponseEntity<>(deathClaimCoverDetails, header, HttpStatus.OK);
    }

    // Add
    @PostMapping("/add")
    public ResponseEntity<String> add(@Valid @RequestBody DeathClaimCoverDetailsDto deathClaimCoverDetailsDto) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "add-deathClaimCoverDetails");
        String add = deathClaimCoverDetailsService.add(deathClaimCoverDetailsDto);
        return new ResponseEntity<>(add, header, HttpStatus.CREATED);
    }

    // Update
    @PatchMapping("/update/{id}")
    public ResponseEntity<String> update(@Valid @PathVariable Long id, @RequestBody DeathClaimCoverDetailsDto deathClaimCoverDetailsDto) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "update-deathClaimCoverDetails");
        String update = deathClaimCoverDetailsService.update(id, deathClaimCoverDetailsDto);
        return new ResponseEntity<>(update, header, HttpStatus.OK);
    }

    // Delete
    @PatchMapping("/soft-delete/{id}")
    public ResponseEntity<String> softDelete(@PathVariable Long id) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "soft-delete-deathClaimCoverDetails");
        String softdelete = deathClaimCoverDetailsService.softdelete(id);
        return new ResponseEntity<>(softdelete, header, HttpStatus.OK);
    }

    // Delete
    @DeleteMapping("/hard-delete/{id}")
    public ResponseEntity<String> hardDelete(@PathVariable Long id) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "hard-delete-deathClaimCoverDetails");
        String harddelete = deathClaimCoverDetailsService.harddelete(id);
        return new ResponseEntity<>(harddelete, header, HttpStatus.OK);
    }

    // Global Search
    @GetMapping("/global-search/{key}")
    public ResponseEntity<List<DeathClaimCoverDetailsDto>> globalSearch(String key) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "global-search-deathClaimCoverDetails");
        List<DeathClaimCoverDetailsDto> globalSearch = deathClaimCoverDetailsService.globalSearch(key);
        return new ResponseEntity<>(globalSearch, header, HttpStatus.OK);
    }
}
