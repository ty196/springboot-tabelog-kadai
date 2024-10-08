package com.example.nagoyameshi.form;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserEditForm {
	@NotNull
	private Integer id;
	
	@NotBlank(message = "氏名を入力してください。")
	private String name;
	
	@NotBlank(message = "フリガナを入力してください。")
	private String furigana;
	
	@NotBlank(message = "郵便番号を入力してください。")
	private String postalCode;
	
	@NotBlank(message = "住所を入力してください。")
	private String address;
	
	@NotBlank(message = "電話番号を入力してください。")
	private String phoneNumber;
	
	@Pattern(regexp = "^$|^[0-9]{8}$", message = "誕生日は8桁の半角数字で入力してください。")
    private String birthday;
            
    @Pattern(regexp = "^$|.*[^\\s　]+.*", message = "スペースのみの入力は禁止されています。")
    private String occupation;     
    
    @NotBlank(message = "メールアドレスを入力してください。")
    @Email(message = "メールアドレスは正しい形式で入力してください。")    
    private String email;
    
    @Length(min = 8, message = "パスワードは8文字以上で入力してください。")
    private String password;
    
    private String passwordConfirmation;

}
