forEach: View
fileName: {{namePascalCase}}Repository.java
path: {{boundedContext.name}}/{{{options.packagePath}}}
---
package {{options.package}};

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface {{namePascalCase}}Repository extends CrudRepository<{{namePascalCase}}, Long> {

    List<{{namePascalCase}}> findByName(@Param("name") String name);
}