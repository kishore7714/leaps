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

import com.leaps.dto.master.SsvFactorDto;
import com.leaps.responses.master.SsvFactorResponse;
import com.leaps.serviceImpl.master.SsvFactorServiceImpl;
import com.leaps.utilities.AppConstants;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/ssvFactor")
public class SsvFactorController {

	private static final String DEFAULT_SORT_DIRECTION = AppConstants.DEFAULT_SORT_DIRECTION;
	private static final String DEFAULT_SORT_BY = AppConstants.DEFAULT_SORT_BY;
	private static final String DEFAULT_PAGE_SIZE = AppConstants.DEFAULT_PAGE_SIZE;
	private static final String DEFAULT_PAGE_NUMBER = AppConstants.DEFAULT_PAGE_NUMBER;

	@Autowired
	private SsvFactorServiceImpl ssvFactorService;

	// Get-all with Pagination
	@GetMapping("/getall-pagination")
	public ResponseEntity<SsvFactorResponse> getallWithPagination(
			@RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
		HttpHeaders header = new HttpHeaders();
		SsvFactorResponse ssvFactor = ssvFactorService.getAllWithPagination(pageNo, pageSize, sortBy, sortDir);
		header.add("description", "SsvFactor-getall-pagination");
		return new ResponseEntity<>(ssvFactor, header, HttpStatus.OK);
		// return
		// ResponseEntity.status(HttpStatus.OK).headers(header).body(ssvFactor);
	}

	// Get-all
	@GetMapping("/getall")
	public ResponseEntity<List<SsvFactorDto>> getall() {
		HttpHeaders header = new HttpHeaders();
		header.add("description", "getall");
		List<SsvFactorDto> ssvFactor = ssvFactorService.getall();
		return new ResponseEntity<>(ssvFactor, header, HttpStatus.OK);
	}

	// Get By Id
	@GetMapping("/get-by-id/{id}")
	public ResponseEntity<SsvFactorDto> getbyid(@PathVariable Long id) {
		HttpHeaders header = new HttpHeaders();
		header.add("description", "get-by-id");
		header.add("type", "SsvFactor Object");
		SsvFactorDto ssvFactor = ssvFactorService.getbyid(id);
		return new ResponseEntity<>(ssvFactor, header, HttpStatus.OK);
	}

	// Add
	@PostMapping("/add")
	public ResponseEntity<String> add(@Valid @RequestBody SsvFactorDto ssvFactorDto) {
		HttpHeaders header = new HttpHeaders();
		header.add("description", "add-ssvFactor");
		String add = ssvFactorService.add(ssvFactorDto);
		return new ResponseEntity<>(add, header, HttpStatus.CREATED);
	}

	// Update
	@PatchMapping("/update/{id}")
	public ResponseEntity<String> update(@Valid @PathVariable Long id, @RequestBody SsvFactorDto ssvFactorDto) {
		HttpHeaders header = new HttpHeaders();
		header.add("description", "update-ssvFactor");
		String update = ssvFactorService.update(id, ssvFactorDto);
		return new ResponseEntity<>(update, header, HttpStatus.OK);
	}

	// Delete
	@PatchMapping("/soft-delete/{id}")
	public ResponseEntity<String> softDelete(@PathVariable Long id) {
		HttpHeaders header = new HttpHeaders();
		header.add("description", "soft-delete-ssvFactor");
		String softdelete = ssvFactorService.softdelete(id);
		return new ResponseEntity<>(softdelete, header, HttpStatus.OK);
	}

	// Delete
	@DeleteMapping("/hard-delete/{id}")
	public ResponseEntity<String> hardDelete(@PathVariable Long id) {
		HttpHeaders header = new HttpHeaders();
		header.add("description", "hard-delete-ssvFactor");
		String harddelete = ssvFactorService.harddelete(id);
		return new ResponseEntity<>(harddelete, header, HttpStatus.OK);
	}

	// Global Search
	@GetMapping("/global-search/{key}")
	public ResponseEntity<List<SsvFactorDto>> globalSearch(String key) {
		HttpHeaders header = new HttpHeaders();
		header.add("description", "global-search-ssvFactor");
		List<SsvFactorDto> globalSearch = ssvFactorService.globalSearch(key);
		return new ResponseEntity<>(globalSearch, header, HttpStatus.OK);
	}
}
