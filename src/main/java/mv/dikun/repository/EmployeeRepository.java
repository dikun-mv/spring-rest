package mv.dikun.repository;

import mv.dikun.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@SuppressWarnings("JpaQlInspection")
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("select E from Employee E where E.department.id = :id")
    List<Employee> findByDepartmentId(@Param("id") Long id);
}
