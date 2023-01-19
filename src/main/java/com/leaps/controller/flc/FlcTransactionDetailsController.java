package com.leaps.controller.flc;

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

import com.leaps.dto.flc.FlcTransactionDetailsDto;
import com.leaps.responses.flc.FlcTransactionDetailsResponse;
import com.leaps.serviceImpl.flc.FlcTransactionDetailsServiceImpl;
import com.leaps.utilities.AppConstants;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/flcTransactionDetails")
public class FlcTransactionDetailsController {

	private static final String DEFAULT_SORT_DIRECTION = AppConstants.DEFAULT_SORT_DIRECTION;
    private static final String DEFAULT_SORT_BY = AppConstants.DEFAULT_SORT_BY;
    private static final String DEFAULT_PAGE_SIZE = AppConstants.DEFAULT_PAGE_SIZE;
    private static final String DEFAULT_PAGE_NUMBER = AppConstants.DEFAULT_PAGE_NUMBER;

    @Autowired
    private FlcTransactionDetailsServiceImpl flcTransactionDetailsService;

    // Get-all with Pagination
    @GetMapping("/getall-pagination")
    public ResponseEntity<FlcTransactionDetailsResponse> getallWithPagination(
            @RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        HttpHeaders header = new HttpHeaders();
        FlcTransactionDetailsResponse flcTransactionDetails = flcTransactionDetailsService.getAllWithPagination(pageNo, pageSize, sortBy, sortDir);
        header.add("description", "FlcTransactionDetails-getall-pagination");
        return new ResponseEntity<>(flcTransactionDetails, header, HttpStatus.OK);
        // return ResponseEntity.status(HttpStatus.OK).headers(header).body(flcTransactionDetails);
    }

    // Get-all
    @GetMapping("/getall")
    public ResponseEntity<List<FlcTransactionDetailsDto>> getall() {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "getall");
        List<FlcTransactionDetailsDto> flcTransactionDetails = flcTransactionDetailsService.getall();
        return new ResponseEntity<>(flcTransactionDetails, header, HttpStatus.OK);
    }

    // Get By Id
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<FlcTransactionDetailsDto> getbyid(@PathVariable Long id) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "get-by-id");
        header.add("type", "FlcTransactionDetails Object");
        FlcTransactionDetailsDto flcTransactionDetails = flcTransactionDetailsService.getbyid(id);
        return new ResponseEntity<>(flcTransactionDetails, header, HttpStatus.OK);
    }

    // Add
    @PostMapping("/add")
    public ResponseEntity<String> add(@Valid @RequestBody FlcTransactionDetailsDto flcTransactionDetailsDto) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "add-flcTransactionDetails");
        String add = flcTransactionDetailsService.add(flcTransactionDetailsDto);
        return new ResponseEntity<>(add, header, HttpStatus.CREATED);
    }

    // Update
    @PatchMapping("/update/{id}")
    public ResponseEntity<String> update(@Valid @PathVariable Long id, @RequestBody FlcTransactionDetailsDto flcTransactionDetailsDto) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "update-flcTransactionDetails");
        String update = flcTransactionDetailsService.update(id, flcTransactionDetailsDto);
        return new ResponseEntity<>(update, header, HttpStatus.OK);
    }

    // Delete
    @PatchMapping("/soft-delete/{id}")
    public ResponseEntity<String> softDelete(@PathVariable Long id) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "soft-delete-flcTransactionDetails");
        String softdelete = flcTransactionDetailsService.softdelete(id);
        return new ResponseEntity<>(softdelete, header, HttpStatus.OK);
    }

    // Delete
    @DeleteMapping("/hard-delete/{id}")
    public ResponseEntity<String> hardDelete(@PathVariable Long id) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "hard-delete-flcTransactionDetails");
        String harddelete = flcTransactionDetailsService.harddelete(id);
        return new ResponseEntity<>(harddelete, header, HttpStatus.OK);
    }

    // Global Search
    @GetMapping("/global-search/{key}")
    public ResponseEntity<List<FlcTransactionDetailsDto>> globalSearch(String key) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "global-search-flcTransactionDetails");
        List<FlcTransactionDetailsDto> globalSearch = flcTransactionDetailsService.globalSearch(key);
        return new ResponseEntity<>(globalSearch, header, HttpStatus.OK);
    }
	
}
