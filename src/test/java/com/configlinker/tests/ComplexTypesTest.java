package com.configlinker.tests;


import com.configlinker.Deserializer;
import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;
import java.util.Map;
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
		TypeCompany_fromConstructorString typeCompany_fromConstructorString = getSingleConfigInstance(TypeCompany_fromConstructorString.class);
		Company company = typeCompany_fromConstructorString.getCompany();
		Assertions.assertEquals(companyInConfigFile, company);
		Assertions.assertEquals(BoundProperty.DeserializationMethod.CONSTRUCTOR_STRING, company.deserializationMethod);
	}
	
	@Test
	void test_getWithStringValueOf()
	{
		TypeCompany_fromValueOfString typeCompany_fromValueOfString = getSingleConfigInstance(TypeCompany_fromValueOfString.class);
		Company company = typeCompany_fromValueOfString.getCompany();
		Assertions.assertEquals(companyInConfigFile, company);
		Assertions.assertEquals(BoundProperty.DeserializationMethod.VALUEOF_STRING, company.deserializationMethod);
	}
	
	@Test
	void test_getWithStringDeserializer()
	{
		TypeCompany_fromDeserializerString typeCompany_fromDeserializer = getSingleConfigInstance(TypeCompany_fromDeserializerString.class);
		Company company = typeCompany_fromDeserializer.getCompany();
		Assertions.assertEquals(companyInConfigFile, company);
		Assertions.assertEquals(BoundProperty.DeserializationMethod.DESERIALIZER_STRING, company.deserializationMethod);
	}
	
	@Test
	void test_getWithStringMixedWaysSimultaneously()
	{
		TypeCompany_MixedString typeCompany_mixedString = getSingleConfigInstance(TypeCompany_MixedString.class);
		
		Company companyFromConstructor = typeCompany_mixedString.getCompany_fromConstructorString();
		Assertions.assertEquals(companyInConfigFile, companyFromConstructor);
		Assertions.assertEquals(BoundProperty.DeserializationMethod.CONSTRUCTOR_STRING, companyFromConstructor.deserializationMethod);
		
		Company companyFromValueOf = typeCompany_mixedString.getCompany_fromValueOfString();
		Assertions.assertEquals(companyInConfigFile, companyFromValueOf);
		Assertions.assertEquals(BoundProperty.DeserializationMethod.VALUEOF_STRING, companyFromValueOf.deserializationMethod);
		
		Company companyFromDeserizlizer = typeCompany_mixedString.getCompany_fromDeserializerString();
		Assertions.assertEquals(companyInConfigFile, companyFromDeserizlizer);
		Assertions.assertEquals(BoundProperty.DeserializationMethod.DESERIALIZER_STRING, companyFromDeserizlizer.deserializationMethod);
	}
	
	
}


@BoundObject(sourcePath = "configs/complex_types.properties")
interface TypeCompany_fromConstructorString
{
	@BoundProperty(name = "com.mycompany.info", deserializationMethod = BoundProperty.DeserializationMethod.CONSTRUCTOR_STRING)
	Company getCompany();
}

@BoundObject(sourcePath = "configs/complex_types.properties")
interface TypeCompany_fromValueOfString
{
	@BoundProperty(name = "com.mycompany.info", deserializationMethod = BoundProperty.DeserializationMethod.VALUEOF_STRING)
	Company getCompany();
}

@BoundObject(sourcePath = "configs/complex_types.properties")
interface TypeCompany_fromDeserializerString
{
	@BoundProperty(name = "com.mycompany.info", deserializationMethod = BoundProperty.DeserializationMethod.DESERIALIZER_STRING)
	Company getCompany();
}

@BoundObject(sourcePath = "configs/complex_types.properties")
interface TypeCompany_MixedString
{
	@BoundProperty(name = "com.mycompany.info", deserializationMethod = BoundProperty.DeserializationMethod.CONSTRUCTOR_STRING)
	Company getCompany_fromConstructorString();
	
	@BoundProperty(name = "com.mycompany.info", deserializationMethod = BoundProperty.DeserializationMethod.VALUEOF_STRING)
	Company getCompany_fromValueOfString();
	
	@BoundProperty(name = "com.mycompany.info", deserializationMethod = BoundProperty.DeserializationMethod.DESERIALIZER_STRING)
	Company getCompany_fromDeserializerString();
}

@BoundObject(sourcePath = "configs/complex_types.properties")
interface TypeCompany_fromConstructorMap
{
	@BoundProperty(name = "com.mycompany.info", deserializationMethod = BoundProperty.DeserializationMethod.CONSTRUCTOR_MAP)
	Company getCompany();
}

@BoundObject(sourcePath = "configs/complex_types.properties")
interface TypeCompany_fromValueOfMap
{
	@BoundProperty(name = "com.mycompany.info", deserializationMethod = BoundProperty.DeserializationMethod.VALUEOF_MAP)
	Company getCompany();
}

@BoundObject(sourcePath = "configs/complex_types.properties")
interface TypeCompany_fromDeserializerMap
{
	@BoundProperty(name = "com.mycompany.info", deserializationMethod = BoundProperty.DeserializationMethod.DESERIALIZER_MAP)
	Company getCompany();
}

@BoundObject(sourcePath = "configs/complex_types.properties")
interface TypeCompany_MixedMap
{
	@BoundProperty(name = "com.mycompany.info", deserializationMethod = BoundProperty.DeserializationMethod.CONSTRUCTOR_MAP)
	Company getCompany_fromConstructorString();
	
	@BoundProperty(name = "com.mycompany.info", deserializationMethod = BoundProperty.DeserializationMethod.VALUEOF_MAP)
	Company getCompany_fromValueOfString();
	
	@BoundProperty(name = "com.mycompany.info", deserializationMethod = BoundProperty.DeserializationMethod.DESERIALIZER_MAP)
	Company getCompany_fromDeserializerString();
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
	
	public static Company valueOf(String rawStringData)
	{
		Company newCompany = new Company();
		parseFromString(newCompany, rawStringData);
		newCompany.deserializationMethod = BoundProperty.DeserializationMethod.VALUEOF_STRING;
		return newCompany;
	}
	
	public static Company valueOf(Map<String, String> rawStringMap)
	{
		Company newCompany = new Company();
		parseFromMap(newCompany, rawStringMap);
		newCompany.deserializationMethod = BoundProperty.DeserializationMethod.VALUEOF_MAP;
		return newCompany;
	}
	
	private static void parseFromString(Company company, String rawStringData)
	{
		String[] parts = rawStringData.split("//");
		company.name = parts[0].trim();
		company.emails = Arrays.stream(parts[1].split(",")).map(String::trim).toArray(String[]::new);
		company.phoneNumbers = Arrays.stream(parts[2].split(",")).map(String::trim).toArray(String[]::new);
		company.ceo = parts[3].trim();
		company.dateFoundation = Short.parseShort(parts[4].trim());
		company.authorizedCapital = Double.parseDouble(parts[5].trim());
	}
	
	private static void parseFromMap(Company company, Map<String, String> rawKeyValues)
	{
		company.name = rawKeyValues.get("name");
		company.emails = Arrays.stream(rawKeyValues.get("emails").split(",")).map(String::trim).toArray(String[]::new);
		company.phoneNumbers = Arrays.stream(rawKeyValues.get("phoneNumbers").split(",")).map(String::trim).toArray(String[]::new);
		company.ceo = rawKeyValues.get("ceo");
		company.dateFoundation = Short.parseShort(rawKeyValues.get("dateFoundation"));
		company.authorizedCapital = Double.parseDouble(rawKeyValues.get("authorizedCapital"));
	}
	
	public Company(String rawData)
	{
		parseFromString(this, rawData);
		this.deserializationMethod = BoundProperty.DeserializationMethod.CONSTRUCTOR_STRING;
	}
	
	public Company(Map<String, String> rawStringMap)
	{
		parseFromMap(this, rawStringMap);
		this.deserializationMethod = BoundProperty.DeserializationMethod.CONSTRUCTOR_MAP;
	}
	
	@Override
	public Company deserialize(String rawValue)
	{
		Company newCompany = new Company();
		parseFromString(newCompany, rawValue);
		newCompany.deserializationMethod = BoundProperty.DeserializationMethod.DESERIALIZER_STRING;
		return newCompany;
	}
	
	@Override
	public Company deserialize(Map<String, String> rawValue)
	{
		Company newCompany = new Company();
		parseFromMap(newCompany, rawValue);
		newCompany.deserializationMethod = BoundProperty.DeserializationMethod.DESERIALIZER_MAP;
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
