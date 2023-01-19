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

import com.leaps.dto.flc.FlcCoverDetailsDto;
import com.leaps.responses.flc.FlcCoverDetailsResponse;
import com.leaps.serviceImpl.flc.FlcCoverDetailsServiceImpl;
import com.leaps.utilities.AppConstants;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/flcCoverDetails")
public class FlcCoverDetailsController {
	
	private static final String DEFAULT_SORT_DIRECTION = AppConstants.DEFAULT_SORT_DIRECTION;
    private static final String DEFAULT_SORT_BY = AppConstants.DEFAULT_SORT_BY;
    private static final String DEFAULT_PAGE_SIZE = AppConstants.DEFAULT_PAGE_SIZE;
    private static final String DEFAULT_PAGE_NUMBER = AppConstants.DEFAULT_PAGE_NUMBER;

    @Autowired
    private FlcCoverDetailsServiceImpl flcCoverDetailsService;

    // Get-all with Pagination
    @GetMapping("/getall-pagination")
    public ResponseEntity<FlcCoverDetailsResponse> getallWithPagination(
            @RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        HttpHeaders header = new HttpHeaders();
        FlcCoverDetailsResponse flcCoverDetails = flcCoverDetailsService.getAllWithPagination(pageNo, pageSize, sortBy, sortDir);
        header.add("description", "FlcCoverDetails-getall-pagination");
        return new ResponseEntity<>(flcCoverDetails, header, HttpStatus.OK);
        // return ResponseEntity.status(HttpStatus.OK).headers(header).body(flcCoverDetails);
    }

    // Get-all
    @GetMapping("/getall")
    public ResponseEntity<List<FlcCoverDetailsDto>> getall() {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "getall");
        List<FlcCoverDetailsDto> flcCoverDetails = flcCoverDetailsService.getall();
        return new ResponseEntity<>(flcCoverDetails, header, HttpStatus.OK);
    }

    // Get By Id
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<FlcCoverDetailsDto> getbyid(@PathVariable Long id) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "get-by-id");
        header.add("type", "FlcCoverDetails Object");
        FlcCoverDetailsDto flcCoverDetails = flcCoverDetailsService.getbyid(id);
        return new ResponseEntity<>(flcCoverDetails, header, HttpStatus.OK);
    }

    // Add
    @PostMapping("/add")
    public ResponseEntity<String> add(@Valid @RequestBody FlcCoverDetailsDto flcCoverDetailsDto) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "add-flcCoverDetails");
        String add = flcCoverDetailsService.add(flcCoverDetailsDto);
        return new ResponseEntity<>(add, header, HttpStatus.CREATED);
    }

    // Update
    @PatchMapping("/update/{id}")
    public ResponseEntity<String> update(@Valid @PathVariable Long id, @RequestBody FlcCoverDetailsDto flcCoverDetailsDto) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "update-flcCoverDetails");
        String update = flcCoverDetailsService.update(id, flcCoverDetailsDto);
        return new ResponseEntity<>(update, header, HttpStatus.OK);
    }

    // Delete
    @PatchMapping("/soft-delete/{id}")
    public ResponseEntity<String> softDelete(@PathVariable Long id) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "soft-delete-flcCoverDetails");
        String softdelete = flcCoverDetailsService.softdelete(id);
        return new ResponseEntity<>(softdelete, header, HttpStatus.OK);
    }

    // Delete
    @DeleteMapping("/hard-delete/{id}")
    public ResponseEntity<String> hardDelete(@PathVariable Long id) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "hard-delete-flcCoverDetails");
        String harddelete = flcCoverDetailsService.harddelete(id);
        return new ResponseEntity<>(harddelete, header, HttpStatus.OK);
    }

    // Global Search
    @GetMapping("/global-search/{key}")
    public ResponseEntity<List<FlcCoverDetailsDto>> globalSearch(String key) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "global-search-flcCoverDetails");
        List<FlcCoverDetailsDto> globalSearch = flcCoverDetailsService.globalSearch(key);
        return new ResponseEntity<>(globalSearch, header, HttpStatus.OK);
    }
}
