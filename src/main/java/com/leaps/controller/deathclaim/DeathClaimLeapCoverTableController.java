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

import com.leaps.dto.deathclaim.DeathClaimLeapCoverTableDto;
import com.leaps.responses.deathclaim.DeathClaimLeapCoverTableResponse;
import com.leaps.serviceImpl.deathclaim.DeathClaimLeapCoverTableServiceImpl;
import com.leaps.utilities.AppConstants;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/deathClaimLeapCoverTable")
public class DeathClaimLeapCoverTableController {
	
	private static final String DEFAULT_SORT_DIRECTION = AppConstants.DEFAULT_SORT_DIRECTION;
    private static final String DEFAULT_SORT_BY = AppConstants.DEFAULT_SORT_BY;
    private static final String DEFAULT_PAGE_SIZE = AppConstants.DEFAULT_PAGE_SIZE;
    private static final String DEFAULT_PAGE_NUMBER = AppConstants.DEFAULT_PAGE_NUMBER;

    @Autowired
    private DeathClaimLeapCoverTableServiceImpl deathClaimLeapCoverTableService;

    // Get-all with Pagination
    @GetMapping("/getall-pagination")
    public ResponseEntity<DeathClaimLeapCoverTableResponse> getallWithPagination(
            @RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        HttpHeaders header = new HttpHeaders();
        DeathClaimLeapCoverTableResponse deathClaimLeapCoverTable = deathClaimLeapCoverTableService.getAllWithPagination(pageNo, pageSize, sortBy, sortDir);
        header.add("description", "DeathClaimLeapCoverTable-getall-pagination");
        return new ResponseEntity<>(deathClaimLeapCoverTable, header, HttpStatus.OK);
        // return ResponseEntity.status(HttpStatus.OK).headers(header).body(deathClaimLeapCoverTable);
    }

    // Get-all
    @GetMapping("/getall")
    public ResponseEntity<List<DeathClaimLeapCoverTableDto>> getall() {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "getall");
        List<DeathClaimLeapCoverTableDto> deathClaimLeapCoverTable = deathClaimLeapCoverTableService.getall();
        return new ResponseEntity<>(deathClaimLeapCoverTable, header, HttpStatus.OK);
    }

    // Get By Id
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<DeathClaimLeapCoverTableDto> getbyid(@PathVariable Long id) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "get-by-id");
        header.add("type", "DeathClaimLeapCoverTable Object");
        DeathClaimLeapCoverTableDto deathClaimLeapCoverTable = deathClaimLeapCoverTableService.getbyid(id);
        return new ResponseEntity<>(deathClaimLeapCoverTable, header, HttpStatus.OK);
    }

    // Add
    @PostMapping("/add")
    public ResponseEntity<String> add(@Valid @RequestBody DeathClaimLeapCoverTableDto deathClaimLeapCoverTableDto) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "add-deathClaimLeapCoverTable");
        String add = deathClaimLeapCoverTableService.add(deathClaimLeapCoverTableDto);
        return new ResponseEntity<>(add, header, HttpStatus.CREATED);
    }

    // Update
    @PatchMapping("/update/{id}")
    public ResponseEntity<String> update(@Valid @PathVariable Long id, @RequestBody DeathClaimLeapCoverTableDto deathClaimLeapCoverTableDto) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "update-deathClaimLeapCoverTable");
        String update = deathClaimLeapCoverTableService.update(id, deathClaimLeapCoverTableDto);
        return new ResponseEntity<>(update, header, HttpStatus.OK);
    }

    // Delete
    @PatchMapping("/soft-delete/{id}")
    public ResponseEntity<String> softDelete(@PathVariable Long id) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "soft-delete-deathClaimLeapCoverTable");
        String softdelete = deathClaimLeapCoverTableService.softdelete(id);
        return new ResponseEntity<>(softdelete, header, HttpStatus.OK);
    }

    // Delete
    @DeleteMapping("/hard-delete/{id}")
    public ResponseEntity<String> hardDelete(@PathVariable Long id) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "hard-delete-deathClaimLeapCoverTable");
        String harddelete = deathClaimLeapCoverTableService.harddelete(id);
        return new ResponseEntity<>(harddelete, header, HttpStatus.OK);
    }

    // Global Search
    @GetMapping("/global-search/{key}")
    public ResponseEntity<List<DeathClaimLeapCoverTableDto>> globalSearch(String key) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "global-search-deathClaimLeapCoverTable");
        List<DeathClaimLeapCoverTableDto> globalSearch = deathClaimLeapCoverTableService.globalSearch(key);
        return new ResponseEntity<>(globalSearch, header, HttpStatus.OK);
    }
}
