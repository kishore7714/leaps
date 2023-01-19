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

import com.leaps.dto.master.MedicalDetailsDto;
import com.leaps.responses.master.MedicalDetailsResponse;
import com.leaps.serviceImpl.master.MedicalDetailsServiceImpl;
import com.leaps.utilities.AppConstants;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/medicalDetails")
public class MedicalDetailsController {

	private static final String DEFAULT_SORT_DIRECTION = AppConstants.DEFAULT_SORT_DIRECTION;
	private static final String DEFAULT_SORT_BY = AppConstants.DEFAULT_SORT_BY;
	private static final String DEFAULT_PAGE_SIZE = AppConstants.DEFAULT_PAGE_SIZE;
	private static final String DEFAULT_PAGE_NUMBER = AppConstants.DEFAULT_PAGE_NUMBER;

	@Autowired
	private MedicalDetailsServiceImpl medicalDetailsService;

	// Get-all with Pagination
	@GetMapping("/getall-pagination")
	public ResponseEntity<MedicalDetailsResponse> getallWithPagination(
			@RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
		HttpHeaders header = new HttpHeaders();
		MedicalDetailsResponse medicalDetails = medicalDetailsService.getAllWithPagination(pageNo, pageSize, sortBy,
				sortDir);
		header.add("description", "MedicalDetails-getall-pagination");
		return new ResponseEntity<>(medicalDetails, header, HttpStatus.OK);
		// return
		// ResponseEntity.status(HttpStatus.OK).headers(header).body(medicalDetails);
	}

	// Get-all
	@GetMapping("/getall")
	public ResponseEntity<List<MedicalDetailsDto>> getall() {
		HttpHeaders header = new HttpHeaders();
		header.add("description", "getall");
		List<MedicalDetailsDto> medicalDetails = medicalDetailsService.getall();
		return new ResponseEntity<>(medicalDetails, header, HttpStatus.OK);
	}

	// Get By Id
	@GetMapping("/get-by-id/{id}")
	public ResponseEntity<MedicalDetailsDto> getbyid(@PathVariable Long id) {
		HttpHeaders header = new HttpHeaders();
		header.add("description", "get-by-id");
		header.add("type", "MedicalDetails Object");
		MedicalDetailsDto medicalDetails = medicalDetailsService.getbyid(id);
		return new ResponseEntity<>(medicalDetails, header, HttpStatus.OK);
	}

	// Add
	@PostMapping("/add")
	public ResponseEntity<String> add(@Valid @RequestBody MedicalDetailsDto medicalDetailsDto) {
		HttpHeaders header = new HttpHeaders();
		header.add("description", "add-medicalDetails");
		String add = medicalDetailsService.add(medicalDetailsDto);
		return new ResponseEntity<>(add, header, HttpStatus.CREATED);
	}

	// Update
	@PatchMapping("/update/{id}")
	public ResponseEntity<String> update(@Valid @PathVariable Long id,
			@RequestBody MedicalDetailsDto medicalDetailsDto) {
		HttpHeaders header = new HttpHeaders();
		header.add("description", "update-medicalDetails");
		String update = medicalDetailsService.update(id, medicalDetailsDto);
		return new ResponseEntity<>(update, header, HttpStatus.OK);
	}

	// Delete
	@PatchMapping("/soft-delete/{id}")
	public ResponseEntity<String> softDelete(@PathVariable Long id) {
		HttpHeaders header = new HttpHeaders();
		header.add("description", "soft-delete-medicalDetails");
		String softdelete = medicalDetailsService.softdelete(id);
		return new ResponseEntity<>(softdelete, header, HttpStatus.OK);
	}

	// Delete
	@DeleteMapping("/hard-delete/{id}")
	public ResponseEntity<String> hardDelete(@PathVariable Long id) {
		HttpHeaders header = new HttpHeaders();
		header.add("description", "hard-delete-medicalDetails");
		String harddelete = medicalDetailsService.harddelete(id);
		return new ResponseEntity<>(harddelete, header, HttpStatus.OK);
	}

	// Global Search
	@GetMapping("/global-search/{key}")
	public ResponseEntity<List<MedicalDetailsDto>> globalSearch(String key) {
		HttpHeaders header = new HttpHeaders();
		header.add("description", "global-search-medicalDetails");
		List<MedicalDetailsDto> globalSearch = medicalDetailsService.globalSearch(key);
		return new ResponseEntity<>(globalSearch, header, HttpStatus.OK);
	}
}
