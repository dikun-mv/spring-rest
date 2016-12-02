package mv.dikun.controller;

import io.swagger.annotations.ApiOperation;
import mv.dikun.model.Department;
import mv.dikun.model.Employee;
import mv.dikun.repository.DepartmentRepository;
import mv.dikun.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmployeeController {
    private EmployeeRepository employeeRepository;
    private DepartmentRepository departmentRepository;

    @Autowired
    public EmployeeController(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    @ApiOperation(value = "getAll", response = Employee.class)
    @RequestMapping(value = "/employees", method = RequestMethod.GET)
    public ResponseEntity getAll() {
        List<Employee> employees = employeeRepository.findAll();

        return (employees != null) ?
                ResponseEntity.ok(employees) :
                ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "getOne", response = Employee.class)
    @RequestMapping(value = "/employees/{empId}", method = RequestMethod.GET)
    public ResponseEntity getOne(@PathVariable Long empId) {
        Employee employee = employeeRepository.findOne(empId);

        return (employee != null) ?
                ResponseEntity.ok(employee) :
                ResponseEntity.notFound().build();
    }

    @RequestMapping(value = "/employees", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody Employee request) {
        Department department = departmentRepository.findOne(request.getDepartmentId());

        if(department != null) {
            request.setDepartment(department);
            Employee employee = employeeRepository.save(request);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/" + employee.getId())
                    .build().toUri();

            return ResponseEntity.created(location).build();
        }
        else return ResponseEntity.badRequest().build();
    }

    @RequestMapping(value = "/employees/{empId}", method = RequestMethod.PUT)
    public ResponseEntity update(@RequestBody Employee request, @PathVariable Long empId) {
        Employee employee = employeeRepository.findOne(empId);

        if(employee != null) {
            if(request.getFirstName() != null) employee.setFirstName(request.getFirstName());
            if(request.getSecondName() != null) employee.setSecondName(request.getSecondName());
            if(request.getLastName() != null) employee.setLastName(request.getLastName());
            if(request.getDepartmentId() != null) {
                Department department = departmentRepository.findOne(request.getDepartmentId());
                if(department != null) employee.setDepartment(department);
                else return ResponseEntity.badRequest().build();
            }
            employeeRepository.save(employee);

            return ResponseEntity.ok().build();
        }
        else return ResponseEntity.notFound().build();
    }

    @RequestMapping(value = "/employees/{empId}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable Long empId) {
        boolean isExist = employeeRepository.exists(empId);

        if (isExist) {
            employeeRepository.delete(empId);
            return ResponseEntity.ok().build();
        }
        else return ResponseEntity.notFound().build();
    }
}
