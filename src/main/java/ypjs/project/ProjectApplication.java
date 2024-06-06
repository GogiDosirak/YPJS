package ypjs.project;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ypjs.project.controller.CategoryController;
import ypjs.project.domain.Category;
import ypjs.project.dto.request.CategoryRequestDto;
import ypjs.project.repository.CategoryRepository;
import ypjs.project.service.CategoryService;

@SpringBootApplication
public class ProjectApplication {

	public static void main(String[] args) {

		SpringApplication.run(ProjectApplication.class, args);
		System.out.println("hi");






	}

}
