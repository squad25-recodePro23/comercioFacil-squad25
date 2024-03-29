package squad25.comercioFacil.services;

import java.util.List;

import squad25.comercioFacil.enums.Category;
import squad25.comercioFacil.models.Product;

public interface ProductService {
	Product save(Product product);
	
	Product getById(Long id);
	
	List<Product> getAllByIdMarketAndCategory(Long idMarket, Category category);
	
	List<Product> getAllByIdEnterprise(Long idEnterprise);
	
	Product update(Long id, Product updatedProduct);
	
	void deleteById(Long id);
}