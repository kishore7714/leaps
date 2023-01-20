package com.leaps.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.leaps.dto.admin.ParameterDto;
import com.leaps.responses.admin.ParameterResponse;
import com.leaps.serviceImpl.admin.ParameterServiceImpl;
import com.leaps.utilities.AppConstants;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/parameter")
public class ParameterController {
    private static final String DEFAULT_SORT_DIRECTION = AppConstants.DEFAULT_SORT_DIRECTION;
    private static final String DEFAULT_SORT_BY = AppConstants.DEFAULT_SORT_BY;
    private static final String DEFAULT_PAGE_SIZE = AppConstants.DEFAULT_PAGE_SIZE;
    private static final String DEFAULT_PAGE_NUMBER = AppConstants.DEFAULT_PAGE_NUMBER;

    @Autowired
    private ParameterServiceImpl parameterService;

    // Get-all with Pagination
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getall-pagination")
    public ResponseEntity<ParameterResponse> getallWithPagination(
            @RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        HttpHeaders header = new HttpHeaders();
        ParameterResponse parameter = parameterService.getAllWithPagination(pageNo, pageSize, sortBy, sortDir);
        header.add("description", "Parameter-getall-pagination");
        return new ResponseEntity<>(parameter, header, HttpStatus.OK);
        // return ResponseEntity.status(HttpStatus.OK).headers(header).body(parameter);
    }

    // Get-all
    
    @GetMapping("/getall")
    public ResponseEntity<List<ParameterDto>> getall() {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "getall");
        List<ParameterDto> parameter = parameterService.getall();
        return new ResponseEntity<>(parameter, header, HttpStatus.OK);
    }

    // Get By Id
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<ParameterDto> getbyid(@PathVariable Long id) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "get-by-id");
        header.add("type", "Parameter Object");
        ParameterDto parameter = parameterService.getbyid(id);
        return new ResponseEntity<>(parameter, header, HttpStatus.OK);
    }

    // Add
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<String> add(@Valid @RequestBody ParameterDto parameter) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "add-parameter");
        String add = parameterService.add(parameter);
        return new ResponseEntity<>(add, header, HttpStatus.CREATED);
    }

    // Update
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/update/{id}")
    public ResponseEntity<String> update(@Valid @PathVariable Long id, @RequestBody ParameterDto parameter) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "update-parameter");
        String update = parameterService.update(id, parameter);
        return new ResponseEntity<>(update, header, HttpStatus.OK);
    }

    // Delete
    @PatchMapping("/soft-delete/{id}")
    public ResponseEntity<String> softDelete(@PathVariable Long id) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "soft-delete-parameter");
        String softdelete = parameterService.softdelete(id);
        return new ResponseEntity<>(softdelete, header, HttpStatus.OK);
    }

    // Delete
    @DeleteMapping("/hard-delete/{id}")
    public ResponseEntity<String> hardDelete(@PathVariable Long id) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "hard-delete-parameter");
        String hardDelete = parameterService.harddelete(id);
        return new ResponseEntity<>(hardDelete, header, HttpStatus.OK);
    }

    // Global Search
    @GetMapping("/global-search/{key}")
    public ResponseEntity<List<ParameterDto>> globalSearch(String key) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "global-search-parameter");
        List<ParameterDto> globalSearch = parameterService.globalSearch(key);
        return new ResponseEntity<>(globalSearch, header, HttpStatus.OK);
    }
}
