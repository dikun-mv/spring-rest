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
public class DepartmentController {
    private DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentController(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @ApiOperation(value = "readAll", response = Department.class)
    @RequestMapping(value = "/departments", method = RequestMethod.GET)
    public ResponseEntity readAll() {
        List<Department> departments = departmentRepository.findAll();

        return (departments != null) ?
                ResponseEntity.ok(departments) :
                ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "readOne", response = Department.class)
    @RequestMapping(value = "/departments/{deptId}", method = RequestMethod.GET)
    public ResponseEntity readOne(@PathVariable Long deptId) {
        Department department = departmentRepository.findOne(deptId);

        return (department != null) ?
                ResponseEntity.ok(department) :
                ResponseEntity.notFound().build();
    }

    @RequestMapping(value = "/departments", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody Department request) {
        Department department = departmentRepository.save(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/" + department.getId())
                .build().toUri();

        return ResponseEntity.created(location).build();
    }

    @RequestMapping(value = "/departments/{deptId}", method = RequestMethod.PUT)
    public ResponseEntity update(@RequestBody Department request, @PathVariable Long deptId) {
        Department department = departmentRepository.findOne(deptId);

        if(department != null) {
            if (request.getName() != null) department.setName(request.getName());
            departmentRepository.save(department);

            return ResponseEntity.ok().build();
        }
        else return ResponseEntity.notFound().build();
    }

    @RequestMapping(value = "/departments/{deptId}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable Long deptId) {
        boolean isExist = departmentRepository.exists(deptId);

        if (isExist) {
            departmentRepository.delete(deptId);
            return ResponseEntity.ok().build();
        }
        else return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity exceptionHandler() {
        return ResponseEntity.badRequest().build();
    }

    @RestController
    @RequestMapping(value = "/api/departments/{deptId}/", produces = MediaType.APPLICATION_JSON_VALUE)
    public class NestedEmployeeController {
        private EmployeeRepository employeeRepository;

        @Autowired
        public NestedEmployeeController(EmployeeRepository employeeRepository) {
            this.employeeRepository = employeeRepository;
        }

        @ApiOperation(value = "readAll", response = Employee.class)
        @RequestMapping(value = "/employees", method = RequestMethod.GET)
        public ResponseEntity readAll(@PathVariable Long deptId) {
            List<Employee> employees = employeeRepository.findByDepartmentId(deptId);

            return (employees != null) ?
                    ResponseEntity.ok(employees) :
                    ResponseEntity.noContent().build();
        }

        @RequestMapping(value = "/employees", method = RequestMethod.POST)
        public ResponseEntity create(@RequestBody Employee request, @PathVariable Long deptId) {
            Department department = departmentRepository.findOne(deptId);

            request.setDepartment(department);
            Employee employee = employeeRepository.save(request);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/" + employee.getId())
                    .build().toUri();

            return ResponseEntity.created(location).build();
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity exceptionHandler() {
            return ResponseEntity.badRequest().build();
        }
    }
}
