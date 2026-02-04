package huynhtanhao.hutech.haohuynh.repositories;

import huynhtanhao.hutech.haohuynh.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {
}
