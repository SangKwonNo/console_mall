package com.console.mall.form;


import lombok.Getter;
import lombok.Setter;


import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UpdateForm {
//        private Long id;
        private String name;
        private String city;
        private String street;
        private String zipcode;
        @Pattern(regexp="/^([a-z0-9_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})$/",message = "올바른 이메일주소를 입력해주세요.")
        private String email;

        private String phone;

        private String pw;
}
