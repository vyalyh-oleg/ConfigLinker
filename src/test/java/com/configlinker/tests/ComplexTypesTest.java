package com.configlinker.tests;


import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;
import com.configlinker.exceptions.PropertyMapException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;


@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class ComplexTypesTest extends AbstractBaseTest
{
	@Test
	void test_ComplexTypeDeserializer()
	{
		TypeCompany typeCompany = getSingleConfigInstance(TypeCompany.class);
		Company company = typeCompany.getCompany();
		System.out.println(company);
	}
	
}

@BoundObject(sourcePath = "configs/complex_types.properties")
interface TypeCompany
{
	@BoundProperty(name = "com.mycompany.info", deserializationMethod = BoundProperty.DeserializationMethod.CONSTRUCTOR_STRING)
	Company getCompany();
}

class Company
{
	private String name;
	private String[] emails;
	private String[] phoneNumbers;
	private String ceo;
	private short dateFoundation;
	private double authorizedСapital;
	
	public Company()
	{
	}
	
	public Company(String rawData)
	{
		String[] parts = rawData.split("//");
		this.name = parts[0].trim();
		this.emails = Arrays.stream(parts[1].split(",")).map(String::trim).toArray(String[]::new);
		this.phoneNumbers = Arrays.stream(parts[2].split(",")).map(String::trim).toArray(String[]::new);
		this.ceo = parts[3].trim();
		this.dateFoundation = Short.parseShort(parts[4].trim());
		this.authorizedСapital = Double.parseDouble(parts[5].trim());
	}
	
	@Override
	public String toString()
	{
		return "Company{" +
		  "name='" + name + '\'' +
		  ", emails=" + Arrays.toString(emails) +
		  ", phoneNumbers=" + Arrays.toString(phoneNumbers) +
		  ", ceo='" + ceo + '\'' +
		  ", dateFoundation=" + dateFoundation +
		  ", authorizedСapital=" + authorizedСapital +
		  '}';
	}
}
