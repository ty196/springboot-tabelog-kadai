package com.example.nagoyameshi.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.form.RestaurantEditForm;
import com.example.nagoyameshi.form.RestaurantRegisterForm;
import com.example.nagoyameshi.repository.RestaurantRepository;

@Service
public class RestaurantService {
	private final RestaurantRepository restaurantRepository;
	private final RegularHolidayRestaurantService regularHolidayRestaurantService;
	private final CategoryRestaurantService categoryRestaurantService;
	
	public RestaurantService(RestaurantRepository restaurantRepository,
			                 RegularHolidayRestaurantService regularHolidayRestaurantService,
			                 CategoryRestaurantService categoryRestaurantService) {
		this.restaurantRepository = restaurantRepository;
		this.regularHolidayRestaurantService = regularHolidayRestaurantService;
		this.categoryRestaurantService = categoryRestaurantService;
	}
	
	@Transactional
	public void create(RestaurantRegisterForm restaurantRegisterForm) {
		Restaurant restaurant = new Restaurant();
		List<Integer> regularHolidayIds = restaurantRegisterForm.getRegularHolidayIds();
		List<Integer> categoryIds = restaurantRegisterForm.getCategoryIds();
		MultipartFile image = restaurantRegisterForm.getImage();
		
		if (!image.isEmpty()) {
			String imageName = image.getOriginalFilename();
			String hashedImageName = generateNewFileName(imageName);
			Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);
			copyImageFile(image, filePath);
			restaurant.setImage(hashedImageName);
		}
		
		restaurant.setName(restaurantRegisterForm.getName());
		restaurant.setDescription(restaurantRegisterForm.getDescription());
		restaurant.setLowestPrice(restaurantRegisterForm.getLowestPrice());
		restaurant.setHighestPrice(restaurantRegisterForm.getHighestPrice());
		restaurant.setPostalCode(restaurantRegisterForm.getPostalCode());
		restaurant.setAddress(restaurantRegisterForm.getAddress());
		restaurant.setOpeningTime(restaurantRegisterForm.getOpeningTime());
		restaurant.setClosingTime(restaurantRegisterForm.getClosingTime());
		restaurant.setSeatingCapacity(restaurantRegisterForm.getSeatingCapacity());
		
		restaurantRepository.save(restaurant);
		
		if (regularHolidayIds != null) {
			regularHolidayRestaurantService.create(regularHolidayIds, restaurant);
		}
		
		if (categoryIds != null) {
			categoryRestaurantService.create(categoryIds, restaurant);
		}
	}
		
		@Transactional
		public void update(RestaurantEditForm restaurantEditForm) {
			Restaurant restaurant = restaurantRepository.getReferenceById(restaurantEditForm.getId());
			List<Integer> regularHolidayIds = restaurantEditForm.getRegularHolidayIds();
			List<Integer> categoryIds = restaurantEditForm.getCategoryIds();
			MultipartFile image = restaurantEditForm.getImage();
			
			if (!image.isEmpty()) {
				String imageName = image.getOriginalFilename();
				String hashedImageName = generateNewFileName(imageName);
				Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);
				copyImageFile(image, filePath);
				restaurant.setImage(hashedImageName);
			}
			
			restaurant.setName(restaurantEditForm.getName());
			restaurant.setDescription(restaurantEditForm.getDescription());
			restaurant.setLowestPrice(restaurantEditForm.getLowestPrice());
			restaurant.setHighestPrice(restaurantEditForm.getHighestPrice());
			restaurant.setPostalCode(restaurantEditForm.getPostalCode());
			restaurant.setAddress(restaurantEditForm.getAddress());
			restaurant.setOpeningTime(restaurantEditForm.getOpeningTime());
			restaurant.setClosingTime(restaurantEditForm.getClosingTime());
			restaurant.setSeatingCapacity(restaurantEditForm.getSeatingCapacity());
			
			restaurantRepository.save(restaurant);
			
			regularHolidayRestaurantService.update(regularHolidayIds, restaurant);
	        categoryRestaurantService.update(categoryIds, restaurant);
		
	}
		
		@Transactional
	    public void delete(Restaurant restaurant) {
	        regularHolidayRestaurantService.deleteByRestaurant(restaurant);
	        categoryRestaurantService.deleteByRestaurant(restaurant);
	        restaurantRepository.delete(restaurant);
	    }
		 // UUIDを使って生成したファイル名を返す
	     public String generateNewFileName(String fileName) {
	         String[] fileNames = fileName.split("\\.");                
	         for (int i = 0; i < fileNames.length - 1; i++) {
	             fileNames[i] = UUID.randomUUID().toString();            
	         }
	         String hashedFileName = String.join(".", fileNames);
	         return hashedFileName;
	     }     
	     
	     // 画像ファイルを指定したファイルにコピーする
	     public void copyImageFile(MultipartFile imageFile, Path filePath) {           
	         try {
	             Files.copy(imageFile.getInputStream(), filePath);
	         } catch (IOException e) {
	             e.printStackTrace();
	         }          
	}
	     
	  // 価格が正しく設定されているかどうかをチェックする
	     public boolean isValidPrices(Integer lowestPrice, Integer highestPrice) {
	         return highestPrice >= lowestPrice;
	     }
	     
	     // 営業時間が正しく設定されているかどうかをチェックする
	     public boolean isValidBusinessHours(LocalTime openingTime, LocalTime closingTime) {
	         return closingTime.isAfter(openingTime);
	     }

}
