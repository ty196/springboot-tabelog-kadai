package com.example.nagoyameshi.entity;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Table(name = "restaurants")
@Data
public class Restaurant {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "image")
	private String image;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "lowest_price")
	private Integer lowestPrice;
	
	@Column(name = "highest_price")
	private Integer highestPrice;
	
	@Column(name = "postal_code")
	private String postalCode;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "opening_time")
	private LocalTime openingTime;
	
	@Column(name = "closing_time")
	private LocalTime closingTime;
	
	@Column(name = "seating_capacity")
	private Integer seatingCapacity;
	
	@Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;
	
	@Column(name = "updated_at", insertable = false, updatable = false)
    private Timestamp updatedAt; 
	
	@OneToMany(mappedBy = "restaurant")
    private List<CategoryRestaurant> categoryRestaurants;
	
	@Transient // このアノテーションにより、Hibernateがこのフィールドをデータベースカラムとして扱わないようになる  
    public List<Category> getCategories() {
        if (!categoryRestaurants.isEmpty()) {
            return categoryRestaurants.stream()
                                      .map(CategoryRestaurant::getCategory)
                                      .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }    
}
