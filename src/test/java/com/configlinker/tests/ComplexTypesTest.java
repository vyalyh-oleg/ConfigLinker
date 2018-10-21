package com.configlinker.tests;


import com.configlinker.Deserializer;
import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class ComplexTypesTest extends AbstractBaseTest
{
	private Company companyInConfigFile;
	
	// affiliates.info.list = Horns and hooves // horns.hooves@great.org, director@great.org // 456-876-876, 764-143-078 // Peter Jackson // 1993 // 140500.82 ,\
	//                       Moon Light // admin@moonlight.org, review@moonlight.org // (122) 544-56-78, 7648 // Peter Jackson // 1985 // 1000.00 ,\
	//                       Simple and Affordable // director@simpaff.org, feedback@simpaff.org // (556) 44-55-987, 98-55 // Peter Jackson // 2008 // 12500. ,\
	//                       Moon Light // admin@moonlight.org, review@moonlight.org // (122) 544-56-78, 7648 // Peter Jackson // 1985 // 1000.00
	
	private List<Company> companiesInConfigFile;
	
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
		
		companiesInConfigFile = new ArrayList<>();
		
		
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
		TypeCompany_fromDeserializerString typeCompany_fromDeserializerString = getSingleConfigInstance(TypeCompany_fromDeserializerString.class);
		Company company = typeCompany_fromDeserializerString.getCompany();
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
	
	@Test
	void test_getWithMapConstructor()
	{
		TypeCompany_fromConstructorMap typeCompany_fromConstructorMap = getSingleConfigInstance(TypeCompany_fromConstructorMap.class);
		Company company = typeCompany_fromConstructorMap.getCompany();
		Assertions.assertEquals(companyInConfigFile, company);
		Assertions.assertEquals(BoundProperty.DeserializationMethod.CONSTRUCTOR_MAP, company.deserializationMethod);
	}
	
	@Test
	void test_getWithMapValueOf()
	{
		TypeCompany_fromValueOfMap typeCompany_fromValueOfMap = getSingleConfigInstance(TypeCompany_fromValueOfMap.class);
		Company company = typeCompany_fromValueOfMap.getCompany();
		Assertions.assertEquals(companyInConfigFile, company);
		Assertions.assertEquals(BoundProperty.DeserializationMethod.VALUEOF_MAP, company.deserializationMethod);
	}
	
	@Test
	void test_getWithMapDeserializer()
	{
		TypeCompany_fromDeserializerMap typeCompany_fromDeserializerMap = getSingleConfigInstance(TypeCompany_fromDeserializerMap.class);
		Company company = typeCompany_fromDeserializerMap.getCompany();
		Assertions.assertEquals(companyInConfigFile, company);
		Assertions.assertEquals(BoundProperty.DeserializationMethod.DESERIALIZER_MAP, company.deserializationMethod);
	}
	
	@Test
	void test_getWithMapMixedWaysSimultaneously()
	{
		TypeCompany_MixedMap typeCompany_mixedMap = getSingleConfigInstance(TypeCompany_MixedMap.class);
		
		Company companyFromConstructor = typeCompany_mixedMap.getCompany_fromConstructorMap();
		Assertions.assertEquals(companyInConfigFile, companyFromConstructor);
		Assertions.assertEquals(BoundProperty.DeserializationMethod.CONSTRUCTOR_MAP, companyFromConstructor.deserializationMethod);
		
		Company companyFromValueOf = typeCompany_mixedMap.getCompany_fromValueOfMap();
		Assertions.assertEquals(companyInConfigFile, companyFromValueOf);
		Assertions.assertEquals(BoundProperty.DeserializationMethod.VALUEOF_MAP, companyFromValueOf.deserializationMethod);
		
		Company companyFromDeserizlizer = typeCompany_mixedMap.getCompany_fromDeserializerMap();
		Assertions.assertEquals(companyInConfigFile, companyFromDeserizlizer);
		Assertions.assertEquals(BoundProperty.DeserializationMethod.DESERIALIZER_MAP, companyFromDeserizlizer.deserializationMethod);
	}
	
	
	// TODO: collection of complex types
	
	@Test
	void test_getWithStringMixedWaysForArray()
	{
		TypeCompany_array typeCompany_mixedMap = getSingleConfigInstance(TypeCompany_array.class);
		
		Company companyFromConstructor = typeCompany_mixedMap.getCompany_fromConstructorMap();
		Assertions.assertEquals(companyInConfigFile, companyFromConstructor);
		Assertions.assertEquals(BoundProperty.DeserializationMethod.CONSTRUCTOR_MAP, companyFromConstructor.deserializationMethod);
		
		Company companyFromValueOf = typeCompany_mixedMap.getCompany_fromValueOfMap();
		Assertions.assertEquals(companyInConfigFile, companyFromValueOf);
		Assertions.assertEquals(BoundProperty.DeserializationMethod.VALUEOF_MAP, companyFromValueOf.deserializationMethod);
		
		Company companyFromDeserizlizer = typeCompany_mixedMap.getCompany_fromDeserializerMap();
		Assertions.assertEquals(companyInConfigFile, companyFromDeserizlizer);
		Assertions.assertEquals(BoundProperty.DeserializationMethod.DESERIALIZER_MAP, companyFromDeserizlizer.deserializationMethod);
	}
	
	@Test
	void test_getWithStringMixedWaysForList()
	{
		TypeCompany_list typeCompany_mixedMap = getSingleConfigInstance(TypeCompany_list.class);
		
		Company companyFromConstructor = typeCompany_mixedMap.getCompany_fromConstructorMap();
		Assertions.assertEquals(companyInConfigFile, companyFromConstructor);
		Assertions.assertEquals(BoundProperty.DeserializationMethod.CONSTRUCTOR_MAP, companyFromConstructor.deserializationMethod);
		
		Company companyFromValueOf = typeCompany_mixedMap.getCompany_fromValueOfMap();
		Assertions.assertEquals(companyInConfigFile, companyFromValueOf);
		Assertions.assertEquals(BoundProperty.DeserializationMethod.VALUEOF_MAP, companyFromValueOf.deserializationMethod);
		
		Company companyFromDeserizlizer = typeCompany_mixedMap.getCompany_fromDeserializerMap();
		Assertions.assertEquals(companyInConfigFile, companyFromDeserizlizer);
		Assertions.assertEquals(BoundProperty.DeserializationMethod.DESERIALIZER_MAP, companyFromDeserizlizer.deserializationMethod);
	}
	
	@Test
	void test_getWithStringMixedWaysForSet()
	{
		TypeCompany_set typeCompany_mixedMap = getSingleConfigInstance(TypeCompany_set.class);
		
		Company companyFromConstructor = typeCompany_mixedMap.getCompany_fromConstructorMap();
		Assertions.assertEquals(companyInConfigFile, companyFromConstructor);
		Assertions.assertEquals(BoundProperty.DeserializationMethod.CONSTRUCTOR_MAP, companyFromConstructor.deserializationMethod);
		
		Company companyFromValueOf = typeCompany_mixedMap.getCompany_fromValueOfMap();
		Assertions.assertEquals(companyInConfigFile, companyFromValueOf);
		Assertions.assertEquals(BoundProperty.DeserializationMethod.VALUEOF_MAP, companyFromValueOf.deserializationMethod);
		
		Company companyFromDeserizlizer = typeCompany_mixedMap.getCompany_fromDeserializerMap();
		Assertions.assertEquals(companyInConfigFile, companyFromDeserizlizer);
		Assertions.assertEquals(BoundProperty.DeserializationMethod.DESERIALIZER_MAP, companyFromDeserizlizer.deserializationMethod);
	}
	
	@Test
	void test_getWithStringMixedWaysForMap()
	{
		TypeCompany_map typeCompany_mixedMap = getSingleConfigInstance(TypeCompany_map.class);
		
		Company companyFromConstructor = typeCompany_mixedMap.getCompany_fromConstructorMap();
		Assertions.assertEquals(companyInConfigFile, companyFromConstructor);
		Assertions.assertEquals(BoundProperty.DeserializationMethod.CONSTRUCTOR_MAP, companyFromConstructor.deserializationMethod);
		
		Company companyFromValueOf = typeCompany_mixedMap.getCompany_fromValueOfMap();
		Assertions.assertEquals(companyInConfigFile, companyFromValueOf);
		Assertions.assertEquals(BoundProperty.DeserializationMethod.VALUEOF_MAP, companyFromValueOf.deserializationMethod);
		
		Company companyFromDeserizlizer = typeCompany_mixedMap.getCompany_fromDeserializerMap();
		Assertions.assertEquals(companyInConfigFile, companyFromDeserizlizer);
		Assertions.assertEquals(BoundProperty.DeserializationMethod.DESERIALIZER_MAP, companyFromDeserizlizer.deserializationMethod);
	}
	
	// autorecognize generics
	// default values ? (like a fallback config)
	// variables in configs
}


@BoundObject(sourcePath = "configs/complex_types.properties")
interface TypeCompany_fromConstructorString
{
	@BoundProperty(name = "affiliate.horns-and-hooves.info.string", deserializationMethod = BoundProperty.DeserializationMethod.CONSTRUCTOR_STRING)
	Company getCompany();
}

@BoundObject(sourcePath = "configs/complex_types.properties")
interface TypeCompany_fromValueOfString
{
	@BoundProperty(name = "affiliate.horns-and-hooves.info.string", deserializationMethod = BoundProperty.DeserializationMethod.VALUEOF_STRING)
	Company getCompany();
}

@BoundObject(sourcePath = "configs/complex_types.properties")
interface TypeCompany_fromDeserializerString
{
	@BoundProperty(name = "affiliate.horns-and-hooves.info.string", deserializationMethod = BoundProperty.DeserializationMethod.DESERIALIZER_STRING)
	Company getCompany();
}

@BoundObject(sourcePath = "configs/complex_types.properties")
interface TypeCompany_MixedString
{
	@BoundProperty(name = "affiliate.horns-and-hooves.info.string", deserializationMethod = BoundProperty.DeserializationMethod.CONSTRUCTOR_STRING)
	Company getCompany_fromConstructorString();
	
	@BoundProperty(name = "affiliate.horns-and-hooves.info.string", deserializationMethod = BoundProperty.DeserializationMethod.VALUEOF_STRING)
	Company getCompany_fromValueOfString();
	
	@BoundProperty(name = "affiliate.horns-and-hooves.info.string", deserializationMethod = BoundProperty.DeserializationMethod.DESERIALIZER_STRING)
	Company getCompany_fromDeserializerString();
}


@BoundObject(sourcePath = "configs/complex_types.properties")
interface TypeCompany_fromConstructorMap
{
	@BoundProperty(name = "affiliate.horns-and-hooves.info.map", deserializationMethod = BoundProperty.DeserializationMethod.CONSTRUCTOR_MAP)
	Company getCompany();
}

@BoundObject(sourcePath = "configs/complex_types.properties")
interface TypeCompany_fromValueOfMap
{
	@BoundProperty(name = "affiliate.horns-and-hooves.info.map", deserializationMethod = BoundProperty.DeserializationMethod.VALUEOF_MAP)
	Company getCompany();
}

@BoundObject(sourcePath = "configs/complex_types.properties")
interface TypeCompany_fromDeserializerMap
{
	@BoundProperty(name = "affiliate.horns-and-hooves.info.map", deserializationMethod = BoundProperty.DeserializationMethod.DESERIALIZER_MAP)
	Company getCompany();
}

@BoundObject(sourcePath = "configs/complex_types.properties")
interface TypeCompany_MixedMap
{
	@BoundProperty(name = "affiliate.horns-and-hooves.info.map", deserializationMethod = BoundProperty.DeserializationMethod.CONSTRUCTOR_MAP)
	Company getCompany_fromConstructorMap();
	
	@BoundProperty(name = "affiliate.horns-and-hooves.info.map", deserializationMethod = BoundProperty.DeserializationMethod.VALUEOF_MAP)
	Company getCompany_fromValueOfMap();
	
	@BoundProperty(name = "affiliate.horns-and-hooves.info.map", deserializationMethod = BoundProperty.DeserializationMethod.DESERIALIZER_MAP)
	Company getCompany_fromDeserializerMap();
}


@BoundObject(sourcePath = "configs/complex_types.properties")
interface TypeCompany_array
{
	@BoundProperty(name = "affiliates.info.list", deserializationMethod = BoundProperty.DeserializationMethod.CONSTRUCTOR_STRING)
	Company[] getArrayCompanies_fromConstructorString();
	
	@BoundProperty(name = "affiliates.info.list", deserializationMethod = BoundProperty.DeserializationMethod.VALUEOF_STRING)
	Company[] getArrayCompanies_fromValueOfString();
	
	@BoundProperty(name = "affiliates.info.list", deserializationMethod = BoundProperty.DeserializationMethod.DESERIALIZER_STRING)
	Company[] getArrayCompanies_fromDeserializerString();
}

@BoundObject(sourcePath = "configs/complex_types.properties")
interface TypeCompany_list
{
	@BoundProperty(name = "affiliates.info.list", customTypeOrDeserializer = Company.class, deserializationMethod = BoundProperty.DeserializationMethod.CONSTRUCTOR_STRING)
	List<Company> getListCompanies_fromConstructorString();
	
	@BoundProperty(name = "affiliates.info.list", customTypeOrDeserializer = Company.class, deserializationMethod = BoundProperty.DeserializationMethod.VALUEOF_STRING)
	List<Company> getListCompanies_fromValueOfString();
	
	@BoundProperty(name = "affiliates.info.list", customTypeOrDeserializer = Company.class, deserializationMethod = BoundProperty.DeserializationMethod.DESERIALIZER_STRING)
	List<Company> getListCompanies_fromDeserializerString();
}

@BoundObject(sourcePath = "configs/complex_types.properties")
interface TypeCompany_set
{
	@BoundProperty(name = "affiliates.info.list", customTypeOrDeserializer = Company.class, deserializationMethod = BoundProperty.DeserializationMethod.CONSTRUCTOR_STRING)
	Set<Company> getSetCompanies_fromConstructorString();
	
	@BoundProperty(name = "affiliates.info.list", customTypeOrDeserializer = Company.class, deserializationMethod = BoundProperty.DeserializationMethod.VALUEOF_STRING)
	Set<Company> getSetCompanies_fromValueOfString();
	
	@BoundProperty(name = "affiliates.info.list", customTypeOrDeserializer = Company.class, deserializationMethod = BoundProperty.DeserializationMethod.DESERIALIZER_STRING)
	Set<Company> getSetCompanies_fromDeserializerString();
}

@BoundObject(sourcePath = "configs/complex_types.properties")
interface TypeCompany_map
{
	@BoundProperty(name = "affiliates.info.map", customTypeOrDeserializer = Company.class, deserializationMethod = BoundProperty.DeserializationMethod.CONSTRUCTOR_STRING)
	Map<String, Company> getMapCompanies_fromConstructorString();
	
	@BoundProperty(name = "affiliates.info.map", customTypeOrDeserializer = Company.class, deserializationMethod = BoundProperty.DeserializationMethod.VALUEOF_STRING)
	Map<String, Company> getMapCompanies_fromValueOfString();
	
	@BoundProperty(name = "affiliates.info.map", customTypeOrDeserializer = Company.class, deserializationMethod = BoundProperty.DeserializationMethod.DESERIALIZER_STRING)
	Map<String, Company> getMapCompanies_fromDeserializerString();
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
		company.emails = Arrays.stream(rawKeyValues.get("emails").split(";")).map(String::trim).toArray(String[]::new);
		company.phoneNumbers = Arrays.stream(rawKeyValues.get("phoneNumbers").split(";")).map(String::trim).toArray(String[]::new);
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
	public Company deserialize(Map<String, String> stringValues)
	{
		Company newCompany = new Company();
		parseFromMap(newCompany, stringValues);
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
