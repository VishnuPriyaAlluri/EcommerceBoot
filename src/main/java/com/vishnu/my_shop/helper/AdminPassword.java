package com.vishnu.my_shop.helper;

public class AdminPassword {
    public static void main(String[] args) {
		System.out.println(AES.encrypt("ADMIN","123"));
	}
}
