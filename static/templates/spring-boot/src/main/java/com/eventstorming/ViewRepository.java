forEach: View
fileName: {{namePascalCase}}Repository.java
path: {{boundedContext}}/{{{options.packagePath}}}
---
package {{options.package}};

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface {{aggregate.namePascalCase}}Repository extends CrudRepository<{{aggregate.namePascalCase}}, Long> {

    List<{{aggregate.namePascalCase}}> findByName(@Param("name") String name);
}