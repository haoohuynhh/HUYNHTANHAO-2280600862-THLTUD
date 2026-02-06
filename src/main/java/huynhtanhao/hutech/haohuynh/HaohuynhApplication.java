package huynhtanhao.hutech.haohuynh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HaohuynhApplication {

	public static void main(String[] args) {
		SpringApplication.run(HaohuynhApplication.class, args);
	}

	@org.springframework.context.annotation.Bean
	public org.springframework.boot.CommandLineRunner dataLoader(
			huynhtanhao.hutech.haohuynh.repositories.IRoleRepository roleRepository) {
		return args -> {
			if (roleRepository.count() == 0) {
				roleRepository.save(huynhtanhao.hutech.haohuynh.entities.Role.builder().id(1L).name("ADMIN").build());
				roleRepository.save(huynhtanhao.hutech.haohuynh.entities.Role.builder().id(2L).name("USER").build());
			}
		};
	}
}
