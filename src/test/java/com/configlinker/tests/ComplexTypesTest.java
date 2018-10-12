package com.configlinker.tests;


import com.configlinker.Deserializer;
import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;
import java.util.Objects;


@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class ComplexTypesTest extends AbstractBaseTest
{
	private Company companyInConfigFile;
	
	@BeforeAll
	void initHardcodedValues()
	{
		companyInConfigFile = new Company();
		companyInConfigFile.name = "Horns and hooves";
		companyInConfigFile.emails = new String[] {"horns.hooves@great.org", "director@great.org"};
		companyInConfigFile.phoneNumbers = new String[] {"456-876-876", "764-143-078"};
		companyInConfigFile.ceo = "Peter Jackson";
		companyInConfigFile.dateFoundation = 1993;
		companyInConfigFile.authorizedCapital = 140500.82;
	}
	
	@Test
	void test_getWithStringConstructor()
	{
		TypeCompany_fromConstructor typeCompany_fromConstructor = getSingleConfigInstance(TypeCompany_fromConstructor.class);
		Company company = typeCompany_fromConstructor.getCompany();
		Assertions.assertEquals(companyInConfigFile, company);
		Assertions.assertEquals(BoundProperty.DeserializationMethod.CONSTRUCTOR_STRING, company.deserializationMethod);
	}
	
	@Test
	void test_getWithStringValueOf()
	{
		TypeCompany_fromValueOf typeCompany_fromValueOf = getSingleConfigInstance(TypeCompany_fromValueOf.class);
		Company company = typeCompany_fromValueOf.getCompany();
		Assertions.assertEquals(companyInConfigFile, company);
		Assertions.assertEquals(BoundProperty.DeserializationMethod.VALUEOF_STRING, company.deserializationMethod);
	}
	
	@Test
	void test_getWithStringDeserializer()
	{
		TypeCompany_fromDeserializer typeCompany_fromDeserializer = getSingleConfigInstance(TypeCompany_fromDeserializer.class);
		Company company = typeCompany_fromDeserializer.getCompany();
		Assertions.assertEquals(companyInConfigFile, company);
		Assertions.assertEquals(BoundProperty.DeserializationMethod.DESERIALIZER_STRING, company.deserializationMethod);
	}
	
	@Test
	void test_getWithStringMixedWaysSimultaneously()
	{
		TypeCompany_Mixed typeCompany_mixed = getSingleConfigInstance(TypeCompany_Mixed.class);
		
		Company companyFromConstructor = typeCompany_mixed.getCompany_fromConstructor();
		Assertions.assertEquals(companyInConfigFile, companyFromConstructor);
		Assertions.assertEquals(BoundProperty.DeserializationMethod.CONSTRUCTOR_STRING, companyFromConstructor.deserializationMethod);
		
		Company companyFromValueOf = typeCompany_mixed.getCompany_fromValueOf();
		Assertions.assertEquals(companyInConfigFile, companyFromValueOf);
		Assertions.assertEquals(BoundProperty.DeserializationMethod.VALUEOF_STRING, companyFromValueOf.deserializationMethod);
		
		Company companyFromDeserizlizer = typeCompany_mixed.getCompany_fromDeserializer();
		Assertions.assertEquals(companyInConfigFile, companyFromDeserizlizer);
		Assertions.assertEquals(BoundProperty.DeserializationMethod.DESERIALIZER_STRING, companyFromDeserizlizer.deserializationMethod);
	}
}


@BoundObject(sourcePath = "configs/complex_types.properties")
interface TypeCompany_fromConstructor
{
	@BoundProperty(name = "com.mycompany.info", deserializationMethod = BoundProperty.DeserializationMethod.CONSTRUCTOR_STRING)
	Company getCompany();
}

@BoundObject(sourcePath = "configs/complex_types.properties")
interface TypeCompany_fromValueOf
{
	@BoundProperty(name = "com.mycompany.info", deserializationMethod = BoundProperty.DeserializationMethod.VALUEOF_STRING)
	Company getCompany();
}

@BoundObject(sourcePath = "configs/complex_types.properties")
interface TypeCompany_fromDeserializer
{
	@BoundProperty(name = "com.mycompany.info", deserializationMethod = BoundProperty.DeserializationMethod.DESERIALIZER_STRING)
	Company getCompany();
}

@BoundObject(sourcePath = "configs/complex_types.properties")
interface TypeCompany_Mixed
{
	@BoundProperty(name = "com.mycompany.info", deserializationMethod = BoundProperty.DeserializationMethod.CONSTRUCTOR_STRING)
	Company getCompany_fromConstructor();
	
	@BoundProperty(name = "com.mycompany.info", deserializationMethod = BoundProperty.DeserializationMethod.VALUEOF_STRING)
	Company getCompany_fromValueOf();
	
	@BoundProperty(name = "com.mycompany.info", deserializationMethod = BoundProperty.DeserializationMethod.DESERIALIZER_STRING)
	Company getCompany_fromDeserializer();
}

class Company implements Deserializer<Company>
{
	String name;
	String[] emails;
	String[] phoneNumbers;
	String ceo;
	short dateFoundation;
	double authorizedCapital;
	BoundProperty.DeserializationMethod deserializationMethod;
	
	public Company()
	{
	}
	
	public static Company valueOf(String rawData)
	{
		Company newCompany = new Company();
		parseRawData(newCompany, rawData);
		newCompany.deserializationMethod = BoundProperty.DeserializationMethod.VALUEOF_STRING;
		return newCompany;
	}
	
	private static void parseRawData(Company company, String rawData)
	{
		String[] parts = rawData.split("//");
		company.name = parts[0].trim();
		company.emails = Arrays.stream(parts[1].split(",")).map(String::trim).toArray(String[]::new);
		company.phoneNumbers = Arrays.stream(parts[2].split(",")).map(String::trim).toArray(String[]::new);
		company.ceo = parts[3].trim();
		company.dateFoundation = Short.parseShort(parts[4].trim());
		company.authorizedCapital = Double.parseDouble(parts[5].trim());
	}
	
	public Company(String rawData)
	{
		parseRawData(this, rawData);
		this.deserializationMethod = BoundProperty.DeserializationMethod.CONSTRUCTOR_STRING;
	}
	
	@Override
	public Company deserialize(String rawValue)
	{
		Company newCompany = new Company();
		parseRawData(newCompany, rawValue);
		newCompany.deserializationMethod = BoundProperty.DeserializationMethod.DESERIALIZER_STRING;
		return newCompany;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Company company = (Company) o;
		return dateFoundation == company.dateFoundation &&
		  Double.compare(company.authorizedCapital, authorizedCapital) == 0 &&
		  Objects.equals(name, company.name) &&
		  Arrays.equals(emails, company.emails) &&
		  Arrays.equals(phoneNumbers, company.phoneNumbers) &&
		  Objects.equals(ceo, company.ceo);
	}
	
	@Override
	public int hashCode()
	{
		int result = Objects.hash(name, ceo, dateFoundation, authorizedCapital);
		result = 31 * result + Arrays.hashCode(emails);
		result = 31 * result + Arrays.hashCode(phoneNumbers);
		return result;
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
		  ", authorizedСapital=" + authorizedCapital +
		  '}';
	}
}
