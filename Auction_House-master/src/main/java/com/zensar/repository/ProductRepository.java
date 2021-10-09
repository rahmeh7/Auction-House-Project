package com.zensar.repository;

import java.sql.Timestamp;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.zensar.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	
	@Query("FROM Product P WHERE P.status = 'On'")
	Iterable<Product> getLiveProducts();

	@Query("FROM Product P WHERE P.bidAmount >= :priceLowerLimit and P.bidAmount <= :priceUpperLimit")
	Iterable<Product> getProductsByRange(@Param("priceLowerLimit") int priceLowerLimit,
			@Param("priceUpperLimit") int priceUpperLimit);

	@Query("FROM Product P WHERE P.productCategory.productCategoryId = :productCategoryId")
	Iterable<Product> getProductsByCategory(@Param("productCategoryId") int categoryId);

	@Query("FROM Product P WHERE P.status = 'On' ORDER BY P.productId DESC")
	Iterable<Product> getLatestProducts();

	@Transactional
	@Modifying
	@Query("UPDATE Product P SET P.productName= :productName ,P.productDetails = :productDetails ,P.bidAmount = :bidAmount ,P.closingDate= :closingDate,P.status= :status ,product_category_id = :productCategoryId WHERE P.productId = :productId")
	public void updateProduct(@Param("productId") int productId, @Param("productName") String productName,
			@Param("productDetails") String productDetails, @Param("bidAmount") int bidAmount,
			@Param("closingDate") Timestamp closingDate, @Param("status") String status,
			@Param("productCategoryId") int productCategory);

	@Query(value = "CREATE EVENT IF NOT EXISTS auto_set_status" + "   ON SCHEDULE EVERY 5 SECOND DO"
			+ "   UPDATE auctiondemo.product SET status = 'Off' "
			+ "	WHERE SYSDATE() >= closing_date AND status ='On';", nativeQuery = true)
	public void createEvent();

}
