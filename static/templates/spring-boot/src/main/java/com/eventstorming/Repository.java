forEach: Aggregate
fileName: {{namePascalCase}}Repository.java
path: {{boundedContext.name}}/{{{options.packagePath}}}
---
package {{options.package}};

import org.springframework.data.repository.PagingAndSortingRepository;

public interface {{namePascalCase}}Repository extends PagingAndSortingRepository<{{namePascalCase}}, {{aggregateRoot.keyFieldDescriptor.className}}>{


}