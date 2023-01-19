package com.leaps.controller.admin;

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

import com.leaps.dto.admin.ServiceTableDto;
import com.leaps.responses.admin.ServiceTableResponse;
import com.leaps.serviceImpl.admin.ServiceTableServiceImpl;
import com.leaps.utilities.AppConstants;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/service")
public class ServiceTableController {
	private static final String DEFAULT_SORT_DIRECTION = AppConstants.DEFAULT_SORT_DIRECTION;
    private static final String DEFAULT_SORT_BY = AppConstants.DEFAULT_SORT_BY;
    private static final String DEFAULT_PAGE_SIZE = AppConstants.DEFAULT_PAGE_SIZE;
    private static final String DEFAULT_PAGE_NUMBER = AppConstants.DEFAULT_PAGE_NUMBER;
    @Autowired
    private ServiceTableServiceImpl usersService;

    // Get-all with Pagination
    @GetMapping("/getall-pagination")
    public ResponseEntity<ServiceTableResponse> getallWithPagination(
            @RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        HttpHeaders header = new HttpHeaders();
        ServiceTableResponse users = usersService.getAllWithPagination(pageNo, pageSize, sortBy, sortDir);
        header.add("description", "ServiceTable-getall-pagination");
        return new ResponseEntity<>(users, header, HttpStatus.OK);
        // return ResponseEntity.status(HttpStatus.OK).headers(header).body(serviceTable);
    }

    // Get-all
    @GetMapping("/getall")
    public ResponseEntity<List<ServiceTableDto>> getall() {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "getall");
        List<ServiceTableDto> serviceTable = usersService.getall();
        return new ResponseEntity<>(serviceTable, header, HttpStatus.OK);
    }

    // Get By Id
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<ServiceTableDto> getbyid(@PathVariable Long id) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "get-by-id");
        header.add("type", "ServiceTable Object");
        ServiceTableDto serviceTable = usersService.getbyid(id);
        return new ResponseEntity<>(serviceTable, header, HttpStatus.OK);
    }

    // Add
    @PostMapping("/add")
    public ResponseEntity<String> add(@Valid @RequestBody ServiceTableDto usersDto) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "add-serviceTable");
        String add = usersService.add(usersDto);
        return new ResponseEntity<>(add, header, HttpStatus.CREATED);
    }

    // Update
    @PatchMapping("/update/{id}")
    public ResponseEntity<String> update(@Valid @PathVariable Long id, @RequestBody ServiceTableDto usersDto) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "update-serviceTable");
        String update = usersService.update(id, usersDto);
        return new ResponseEntity<>(update, header, HttpStatus.OK);
    }

    

    // Delete
    @DeleteMapping("/hard-delete/{id}")
    public ResponseEntity<String> hardDelete(@PathVariable Long id) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "hard-delete-serviceTable");
        String harddelete = usersService.harddelete(id);
        return new ResponseEntity<>(harddelete, header, HttpStatus.OK);
    }

}
