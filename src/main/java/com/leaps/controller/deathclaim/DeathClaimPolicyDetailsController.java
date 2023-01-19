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

import com.leaps.dto.deathclaim.DeathClaimPolicyDetailsDto;
import com.leaps.responses.deathclaim.DeathClaimPolicyDetailsResponse;
import com.leaps.serviceImpl.deathclaim.DeathClaimPolicyDetailsServiceImpl;
import com.leaps.utilities.AppConstants;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/deathClaimPolicyDetails")
public class DeathClaimPolicyDetailsController {
	private static final String DEFAULT_SORT_DIRECTION = AppConstants.DEFAULT_SORT_DIRECTION;
    private static final String DEFAULT_SORT_BY = AppConstants.DEFAULT_SORT_BY;
    private static final String DEFAULT_PAGE_SIZE = AppConstants.DEFAULT_PAGE_SIZE;
    private static final String DEFAULT_PAGE_NUMBER = AppConstants.DEFAULT_PAGE_NUMBER;

    @Autowired
    private DeathClaimPolicyDetailsServiceImpl deathClaimPolicyDetailsService;

    // Get-all with Pagination
    @GetMapping("/getall-pagination")
    public ResponseEntity<DeathClaimPolicyDetailsResponse> getallWithPagination(
            @RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        HttpHeaders header = new HttpHeaders();
        DeathClaimPolicyDetailsResponse deathClaimPolicyDetails = deathClaimPolicyDetailsService.getAllWithPagination(pageNo, pageSize, sortBy, sortDir);
        header.add("description", "DeathClaimPolicyDetails-getall-pagination");
        return new ResponseEntity<>(deathClaimPolicyDetails, header, HttpStatus.OK);
        // return ResponseEntity.status(HttpStatus.OK).headers(header).body(deathClaimPolicyDetails);
    }

    // Get-all
    @GetMapping("/getall")
    public ResponseEntity<List<DeathClaimPolicyDetailsDto>> getall() {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "getall");
        List<DeathClaimPolicyDetailsDto> deathClaimPolicyDetails = deathClaimPolicyDetailsService.getall();
        return new ResponseEntity<>(deathClaimPolicyDetails, header, HttpStatus.OK);
    }

    // Get By Id
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<DeathClaimPolicyDetailsDto> getbyid(@PathVariable Long id) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "get-by-id");
        header.add("type", "DeathClaimPolicyDetails Object");
        DeathClaimPolicyDetailsDto deathClaimPolicyDetails = deathClaimPolicyDetailsService.getbyid(id);
        return new ResponseEntity<>(deathClaimPolicyDetails, header, HttpStatus.OK);
    }

    // Add
    @PostMapping("/add")
    public ResponseEntity<String> add(@Valid @RequestBody DeathClaimPolicyDetailsDto deathClaimPolicyDetailsDto) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "add-deathClaimPolicyDetails");
        String add = deathClaimPolicyDetailsService.add(deathClaimPolicyDetailsDto);
        return new ResponseEntity<>(add, header, HttpStatus.CREATED);
    }

    // Update
    @PatchMapping("/update/{id}")
    public ResponseEntity<String> update(@Valid @PathVariable Long id, @RequestBody DeathClaimPolicyDetailsDto deathClaimPolicyDetailsDto) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "update-deathClaimPolicyDetails");
        String update = deathClaimPolicyDetailsService.update(id, deathClaimPolicyDetailsDto);
        return new ResponseEntity<>(update, header, HttpStatus.OK);
    }

    // Delete
    @PatchMapping("/soft-delete/{id}")
    public ResponseEntity<String> softDelete(@PathVariable Long id) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "soft-delete-deathClaimPolicyDetails");
        String softdelete = deathClaimPolicyDetailsService.softdelete(id);
        return new ResponseEntity<>(softdelete, header, HttpStatus.OK);
    }

    // Delete
    @DeleteMapping("/hard-delete/{id}")
    public ResponseEntity<String> hardDelete(@PathVariable Long id) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "hard-delete-deathClaimPolicyDetails");
        String harddelete = deathClaimPolicyDetailsService.harddelete(id);
        return new ResponseEntity<>(harddelete, header, HttpStatus.OK);
    }

    // Global Search
    @GetMapping("/global-search/{key}")
    public ResponseEntity<List<DeathClaimPolicyDetailsDto>> globalSearch(String key) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "global-search-deathClaimPolicyDetails");
        List<DeathClaimPolicyDetailsDto> globalSearch = deathClaimPolicyDetailsService.globalSearch(key);
        return new ResponseEntity<>(globalSearch, header, HttpStatus.OK);
    }
}
