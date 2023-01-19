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

import com.leaps.dto.deathclaim.DeathClaimCoverTablePasDto;
import com.leaps.responses.deathclaim.DeathClaimCoverTablePasResponse;
import com.leaps.serviceImpl.deathclaim.DeathClaimCoverTablePasServiceImpl;
import com.leaps.utilities.AppConstants;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/deathClaimCoverTablePas")
public class DeathClaimCoverTableController {
	
	private static final String DEFAULT_SORT_DIRECTION = AppConstants.DEFAULT_SORT_DIRECTION;
    private static final String DEFAULT_SORT_BY = AppConstants.DEFAULT_SORT_BY;
    private static final String DEFAULT_PAGE_SIZE = AppConstants.DEFAULT_PAGE_SIZE;
    private static final String DEFAULT_PAGE_NUMBER = AppConstants.DEFAULT_PAGE_NUMBER;

    @Autowired
    private DeathClaimCoverTablePasServiceImpl deathClaimCoverTablePasService;

    // Get-all with Pagination
    @GetMapping("/getall-pagination")
    public ResponseEntity<DeathClaimCoverTablePasResponse> getallWithPagination(
            @RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        HttpHeaders header = new HttpHeaders();
        DeathClaimCoverTablePasResponse deathClaimCoverTablePas = deathClaimCoverTablePasService.getAllWithPagination(pageNo, pageSize, sortBy, sortDir);
        header.add("description", "DeathClaimCoverTablePas-getall-pagination");
        return new ResponseEntity<>(deathClaimCoverTablePas, header, HttpStatus.OK);
        // return ResponseEntity.status(HttpStatus.OK).headers(header).body(deathClaimCoverTablePas);
    }

    // Get-all
    @GetMapping("/getall")
    public ResponseEntity<List<DeathClaimCoverTablePasDto>> getall() {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "getall");
        List<DeathClaimCoverTablePasDto> deathClaimCoverTablePas = deathClaimCoverTablePasService.getall();
        return new ResponseEntity<>(deathClaimCoverTablePas, header, HttpStatus.OK);
    }

    // Get By Id
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<DeathClaimCoverTablePasDto> getbyid(@PathVariable Long id) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "get-by-id");
        header.add("type", "DeathClaimCoverTablePas Object");
        DeathClaimCoverTablePasDto deathClaimCoverTablePas = deathClaimCoverTablePasService.getbyid(id);
        return new ResponseEntity<>(deathClaimCoverTablePas, header, HttpStatus.OK);
    }

    // Add
    @PostMapping("/add")
    public ResponseEntity<String> add(@Valid @RequestBody DeathClaimCoverTablePasDto deathClaimCoverTablePasDto) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "add-deathClaimCoverTablePas");
        String add = deathClaimCoverTablePasService.add(deathClaimCoverTablePasDto);
        return new ResponseEntity<>(add, header, HttpStatus.CREATED);
    }

    // Update
    @PatchMapping("/update/{id}")
    public ResponseEntity<String> update(@Valid @PathVariable Long id, @RequestBody DeathClaimCoverTablePasDto deathClaimCoverTablePasDto) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "update-deathClaimCoverTablePas");
        String update = deathClaimCoverTablePasService.update(id, deathClaimCoverTablePasDto);
        return new ResponseEntity<>(update, header, HttpStatus.OK);
    }

    // Delete
    @PatchMapping("/soft-delete/{id}")
    public ResponseEntity<String> softDelete(@PathVariable Long id) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "soft-delete-deathClaimCoverTablePas");
        String softdelete = deathClaimCoverTablePasService.softdelete(id);
        return new ResponseEntity<>(softdelete, header, HttpStatus.OK);
    }

    // Delete
    @DeleteMapping("/hard-delete/{id}")
    public ResponseEntity<String> hardDelete(@PathVariable Long id) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "hard-delete-deathClaimCoverTablePas");
        String harddelete = deathClaimCoverTablePasService.harddelete(id);
        return new ResponseEntity<>(harddelete, header, HttpStatus.OK);
    }

    // Global Search
    @GetMapping("/global-search/{key}")
    public ResponseEntity<List<DeathClaimCoverTablePasDto>> globalSearch(String key) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "global-search-deathClaimCoverTablePas");
        List<DeathClaimCoverTablePasDto> globalSearch = deathClaimCoverTablePasService.globalSearch(key);
        return new ResponseEntity<>(globalSearch, header, HttpStatus.OK);
    }

}
