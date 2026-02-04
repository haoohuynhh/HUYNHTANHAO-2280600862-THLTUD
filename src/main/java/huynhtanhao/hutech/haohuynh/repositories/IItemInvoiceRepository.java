package huynhtanhao.hutech.haohuynh.repositories;

import huynhtanhao.hutech.haohuynh.entities.ItemInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IItemInvoiceRepository extends JpaRepository<ItemInvoice, Long> {
}
