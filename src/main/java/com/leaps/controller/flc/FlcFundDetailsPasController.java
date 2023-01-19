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

import com.leaps.dto.flc.FlcFundDetailsPasDto;
import com.leaps.responses.flc.FlcFundDetailsPasResponse;
import com.leaps.serviceImpl.flc.FlcFundDetailsPasServiceImpl;
import com.leaps.utilities.AppConstants;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/flcFundDetailsPas")
public class FlcFundDetailsPasController {
	
	private static final String DEFAULT_SORT_DIRECTION = AppConstants.DEFAULT_SORT_DIRECTION;
    private static final String DEFAULT_SORT_BY = AppConstants.DEFAULT_SORT_BY;
    private static final String DEFAULT_PAGE_SIZE = AppConstants.DEFAULT_PAGE_SIZE;
    private static final String DEFAULT_PAGE_NUMBER = AppConstants.DEFAULT_PAGE_NUMBER;

    @Autowired
    private FlcFundDetailsPasServiceImpl flcFundDetailsPasService;

    // Get-all with Pagination
    @GetMapping("/getall-pagination")
    public ResponseEntity<FlcFundDetailsPasResponse> getallWithPagination(
            @RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        HttpHeaders header = new HttpHeaders();
        FlcFundDetailsPasResponse flcFundDetailsPas = flcFundDetailsPasService.getAllWithPagination(pageNo, pageSize, sortBy, sortDir);
        header.add("description", "FlcFundDetailsPas-getall-pagination");
        return new ResponseEntity<>(flcFundDetailsPas, header, HttpStatus.OK);
        // return ResponseEntity.status(HttpStatus.OK).headers(header).body(flcFundDetailsPas);
    }

    // Get-all
    @GetMapping("/getall")
    public ResponseEntity<List<FlcFundDetailsPasDto>> getall() {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "getall");
        List<FlcFundDetailsPasDto> flcFundDetailsPas = flcFundDetailsPasService.getall();
        return new ResponseEntity<>(flcFundDetailsPas, header, HttpStatus.OK);
    }

    // Get By Id
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<FlcFundDetailsPasDto> getbyid(@PathVariable Long id) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "get-by-id");
        header.add("type", "FlcFundDetailsPas Object");
        FlcFundDetailsPasDto flcFundDetailsPas = flcFundDetailsPasService.getbyid(id);
        return new ResponseEntity<>(flcFundDetailsPas, header, HttpStatus.OK);
    }

    // Add
    @PostMapping("/add")
    public ResponseEntity<String> add(@Valid @RequestBody FlcFundDetailsPasDto flcFundDetailsPasDto) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "add-flcFundDetailsPas");
        String add = flcFundDetailsPasService.add(flcFundDetailsPasDto);
        return new ResponseEntity<>(add, header, HttpStatus.CREATED);
    }

    // Update
    @PatchMapping("/update/{id}")
    public ResponseEntity<String> update(@Valid @PathVariable Long id, @RequestBody FlcFundDetailsPasDto flcFundDetailsPasDto) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "update-flcFundDetailsPas");
        String update = flcFundDetailsPasService.update(id, flcFundDetailsPasDto);
        return new ResponseEntity<>(update, header, HttpStatus.OK);
    }

    // Delete
    @PatchMapping("/soft-delete/{id}")
    public ResponseEntity<String> softDelete(@PathVariable Long id) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "soft-delete-flcFundDetailsPas");
        String softdelete = flcFundDetailsPasService.softdelete(id);
        return new ResponseEntity<>(softdelete, header, HttpStatus.OK);
    }

    // Delete
    @DeleteMapping("/hard-delete/{id}")
    public ResponseEntity<String> hardDelete(@PathVariable Long id) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "hard-delete-flcFundDetailsPas");
        String harddelete = flcFundDetailsPasService.harddelete(id);
        return new ResponseEntity<>(harddelete, header, HttpStatus.OK);
    }

    // Global Search
    @GetMapping("/global-search/{key}")
    public ResponseEntity<List<FlcFundDetailsPasDto>> globalSearch(String key) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "global-search-flcFundDetailsPas");
        List<FlcFundDetailsPasDto> globalSearch = flcFundDetailsPasService.globalSearch(key);
        return new ResponseEntity<>(globalSearch, header, HttpStatus.OK);
    }
}
