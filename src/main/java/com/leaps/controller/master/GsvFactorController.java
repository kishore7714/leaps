package com.leaps.controller.master;

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

import com.leaps.dto.master.GsvFactorDto;
import com.leaps.responses.master.GsvFactorResponse;
import com.leaps.serviceImpl.master.GsvFactorServiceImpl;
import com.leaps.utilities.AppConstants;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/gsvFactor")
public class GsvFactorController {

	private static final String DEFAULT_SORT_DIRECTION = AppConstants.DEFAULT_SORT_DIRECTION;
    private static final String DEFAULT_SORT_BY = AppConstants.DEFAULT_SORT_BY;
    private static final String DEFAULT_PAGE_SIZE = AppConstants.DEFAULT_PAGE_SIZE;
    private static final String DEFAULT_PAGE_NUMBER = AppConstants.DEFAULT_PAGE_NUMBER;

    @Autowired
    private GsvFactorServiceImpl gsvFactorService;

    // Get-all with Pagination
    @GetMapping("/getall-pagination")
    public ResponseEntity<GsvFactorResponse> getallWithPagination(
            @RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        HttpHeaders header = new HttpHeaders();
        GsvFactorResponse gsvFactor = gsvFactorService.getAllWithPagination(pageNo, pageSize, sortBy, sortDir);
        header.add("description", "GsvFactor-getall-pagination");
        return new ResponseEntity<>(gsvFactor, header, HttpStatus.OK);
        // return ResponseEntity.status(HttpStatus.OK).headers(header).body(gsvFactor);
    }

    // Get-all
    @GetMapping("/getall")
    public ResponseEntity<List<GsvFactorDto>> getall() {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "getall");
        List<GsvFactorDto> gsvFactor = gsvFactorService.getall();
        return new ResponseEntity<>(gsvFactor, header, HttpStatus.OK);
    }

    // Get By Id
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<GsvFactorDto> getbyid(@PathVariable Long id) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "get-by-id");
        header.add("type", "GsvFactor Object");
        GsvFactorDto gsvFactor = gsvFactorService.getbyid(id);
        return new ResponseEntity<>(gsvFactor, header, HttpStatus.OK);
    }

    // Add
    @PostMapping("/add")
    public ResponseEntity<String> add(@Valid @RequestBody GsvFactorDto gsvFactorDto) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "add-gsvFactor");
        String add = gsvFactorService.add(gsvFactorDto);
        return new ResponseEntity<>(add, header, HttpStatus.CREATED);
    }

    // Update
    @PatchMapping("/update/{id}")
    public ResponseEntity<String> update(@Valid @PathVariable Long id, @RequestBody GsvFactorDto gsvFactorDto) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "update-gsvFactor");
        String update = gsvFactorService.update(id, gsvFactorDto);
        return new ResponseEntity<>(update, header, HttpStatus.OK);
    }

    // Delete
    @PatchMapping("/soft-delete/{id}")
    public ResponseEntity<String> softDelete(@PathVariable Long id) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "soft-delete-gsvFactor");
        String softdelete = gsvFactorService.softdelete(id);
        return new ResponseEntity<>(softdelete, header, HttpStatus.OK);
    }

    // Delete
    @DeleteMapping("/hard-delete/{id}")
    public ResponseEntity<String> hardDelete(@PathVariable Long id) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "hard-delete-gsvFactor");
        String harddelete = gsvFactorService.harddelete(id);
        return new ResponseEntity<>(harddelete, header, HttpStatus.OK);
    }

    // Global Search
    @GetMapping("/global-search/{key}")
    public ResponseEntity<List<GsvFactorDto>> globalSearch(String key) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "global-search-gsvFactor");
        List<GsvFactorDto> globalSearch = gsvFactorService.globalSearch(key);
        return new ResponseEntity<>(globalSearch, header, HttpStatus.OK);
    }
}
