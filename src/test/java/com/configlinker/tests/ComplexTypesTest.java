package com.configlinker.tests;


import com.configlinker.IDeserializer;
import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;


@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class ComplexTypesTest extends AbstractBaseTest
{
	private static Company companyInConfigFile;
	private static List<Company> companiesInConfigFile;
	
	@BeforeAll
	static void initHardcodedValues()
	{
		ComplexTypesTest.companyInConfigFile = new Company();
		ComplexTypesTest.companyInConfigFile.name = "Horns and hooves";
		ComplexTypesTest.companyInConfigFile.emails = new String[]{"horns.hooves@great.org", "director@great.org"};
		ComplexTypesTest.companyInConfigFile.phoneNumbers = new String[]{"456-876-876", "764-143-078"};
		ComplexTypesTest.companyInConfigFile.ceo = "Peter Jackson";
		ComplexTypesTest.companyInConfigFile.dateFoundation = 1993;
		ComplexTypesTest.companyInConfigFile.authorizedCapital = 140500.82;
		
		ArrayList<Company> companies = new ArrayList<>();
		companies.add(ComplexTypesTest.companyInConfigFile);
		
		Company company2 = new Company();
		company2.name = "Moon Light";
		company2.emails = new String[]{"admin@moonlight.org", "review@moonlight.org"};
		company2.phoneNumbers = new String[]{"(122) 544-56-78", "7648"};
		company2.ceo = "John Smith";
		company2.dateFoundation = 1985;
		company2.authorizedCapital = 1000.00;
		companies.add(company2);
		
		Company company3 = new Company();
		company3.name = "Simple and Affordable";
		company3.emails = new String[]{"director@simpaff.org", "feedback@simpaff.org"};
		company3.phoneNumbers = new String[]{"(556) 44-55-987", "98-55"};
		company3.ceo = "Ethan Hunt";
		company3.dateFoundation = 2008;
		company3.authorizedCapital = 12500.;
		companies.add(company3);
		
		Company company4 = new Company();
		company4.name = "Moon Light";
		company4.emails = new String[]{"admin@moonlight.org", "review@moonlight.org"};
		company4.phoneNumbers = new String[]{"(122) 544-56-78", "7648"};
		company4.ceo = "John Smith";
		company4.dateFoundation = 1985;
		company4.authorizedCapital = 1000.00;
		companies.add(company4);
		
		ComplexTypesTest.companiesInConfigFile = Collections.unmodifiableList(companies);
	}
	
	@Test
	void test_getWithStringConstructor()
	{
		TypeCompany_fromConstructorString typeCompany_fromConstructorString = getSingleConfigInstance(TypeCompany_fromConstructorString.class);
		Company company = typeCompany_fromConstructorString.getCompany();
		Assertions.assertEquals(ComplexTypesTest.companyInConfigFile, company);
		Assertions.assertEquals(BoundProperty.DeserializationMethod.CONSTRUCTOR_STRING, company.deserializationMethod);
	}
	
	@Test
	void test_getWithStringValueOf()
	{
		TypeCompany_fromValueOfString typeCompany_fromValueOfString = getSingleConfigInstance(TypeCompany_fromValueOfString.class);
		Company company = typeCompany_fromValueOfString.getCompany();
		Assertions.assertEquals(ComplexTypesTest.companyInConfigFile, company);
		Assertions.assertEquals(BoundProperty.DeserializationMethod.VALUEOF_STRING, company.deserializationMethod);
	}
	
	@Test
	void test_getWithStringDeserializer()
	{
		TypeCompany_fromDeserializerString typeCompany_fromDeserializerString = getSingleConfigInstance(TypeCompany_fromDeserializerString.class);
		Company company = typeCompany_fromDeserializerString.getCompany();
		Assertions.assertEquals(ComplexTypesTest.companyInConfigFile, company);
		Assertions.assertEquals(BoundProperty.DeserializationMethod.DESERIALIZER_STRING, company.deserializationMethod);
	}
	
	@Test
	void test_getWithStringMixedWaysSimultaneously()
	{
		TypeCompany_MixedString typeCompany_mixedString = getSingleConfigInstance(TypeCompany_MixedString.class);
		
		Company companyFromConstructor = typeCompany_mixedString.getCompany_fromConstructorString();
		Assertions.assertEquals(ComplexTypesTest.companyInConfigFile, companyFromConstructor);
		Assertions.assertEquals(BoundProperty.DeserializationMethod.CONSTRUCTOR_STRING, companyFromConstructor.deserializationMethod);
		
		Company companyFromValueOf = typeCompany_mixedString.getCompany_fromValueOfString();
		Assertions.assertEquals(ComplexTypesTest.companyInConfigFile, companyFromValueOf);
		Assertions.assertEquals(BoundProperty.DeserializationMethod.VALUEOF_STRING, companyFromValueOf.deserializationMethod);
		
		Company companyFromDeserializer = typeCompany_mixedString.getCompany_fromDeserializerString();
		Assertions.assertEquals(ComplexTypesTest.companyInConfigFile, companyFromDeserializer);
		Assertions.assertEquals(BoundProperty.DeserializationMethod.DESERIALIZER_STRING, companyFromDeserializer.deserializationMethod);
	}
	
	@Test
	void test_getWithMapConstructor()
	{
		TypeCompany_fromConstructorMap typeCompany_fromConstructorMap = getSingleConfigInstance(TypeCompany_fromConstructorMap.class);
		Company company = typeCompany_fromConstructorMap.getCompany();
		Assertions.assertEquals(ComplexTypesTest.companyInConfigFile, company);
		Assertions.assertEquals(BoundProperty.DeserializationMethod.CONSTRUCTOR_MAP, company.deserializationMethod);
	}
	
	@Test
	void test_getWithMapValueOf()
	{
		TypeCompany_fromValueOfMap typeCompany_fromValueOfMap = getSingleConfigInstance(TypeCompany_fromValueOfMap.class);
		Company company = typeCompany_fromValueOfMap.getCompany();
		Assertions.assertEquals(ComplexTypesTest.companyInConfigFile, company);
		Assertions.assertEquals(BoundProperty.DeserializationMethod.VALUEOF_MAP, company.deserializationMethod);
	}
	
	@Test
	void test_getWithMapDeserializer()
	{
		TypeCompany_fromDeserializerMap typeCompany_fromDeserializerMap = getSingleConfigInstance(TypeCompany_fromDeserializerMap.class);
		Company company = typeCompany_fromDeserializerMap.getCompany();
		Assertions.assertEquals(ComplexTypesTest.companyInConfigFile, company);
		Assertions.assertEquals(BoundProperty.DeserializationMethod.DESERIALIZER_MAP, company.deserializationMethod);
	}
	
	@Test
	void test_getWithMapMixedWaysSimultaneously()
	{
		TypeCompany_MixedMap typeCompany_mixedMap = getSingleConfigInstance(TypeCompany_MixedMap.class);
		
		Company companyFromConstructor = typeCompany_mixedMap.getCompany_fromConstructorMap();
		Assertions.assertEquals(ComplexTypesTest.companyInConfigFile, companyFromConstructor);
		Assertions.assertEquals(BoundProperty.DeserializationMethod.CONSTRUCTOR_MAP, companyFromConstructor.deserializationMethod);
		
		Company companyFromValueOf = typeCompany_mixedMap.getCompany_fromValueOfMap();
		Assertions.assertEquals(ComplexTypesTest.companyInConfigFile, companyFromValueOf);
		Assertions.assertEquals(BoundProperty.DeserializationMethod.VALUEOF_MAP, companyFromValueOf.deserializationMethod);
		
		Company companyFromDeserializer = typeCompany_mixedMap.getCompany_fromDeserializerMap();
		Assertions.assertEquals(ComplexTypesTest.companyInConfigFile, companyFromDeserializer);
		Assertions.assertEquals(BoundProperty.DeserializationMethod.DESERIALIZER_MAP, companyFromDeserializer.deserializationMethod);
	}
	
	
	@Test
	void test_getWithStringMixedWaysForArray()
	{
		TypeCompany_array typeCompany_array = getSingleConfigInstance(TypeCompany_array.class);
		
		Company[] arrayCompaniesFromConfig = ComplexTypesTest.companiesInConfigFile.toArray(new Company[ComplexTypesTest.companiesInConfigFile.size()]);
		
		Company[] arrayCompaniesFromConstructor = typeCompany_array.getArrayCompanies_fromConstructorString();
		Assertions.assertArrayEquals(arrayCompaniesFromConfig, arrayCompaniesFromConstructor);
		Stream.of(arrayCompaniesFromConstructor)
		  .forEach(company -> Assertions.assertEquals(BoundProperty.DeserializationMethod.CONSTRUCTOR_STRING, company.deserializationMethod));
		
		Company[] arrayCompaniesFromValueOf = typeCompany_array.getArrayCompanies_fromValueOfString();
		Assertions.assertArrayEquals(arrayCompaniesFromConfig, arrayCompaniesFromValueOf);
		Stream.of(arrayCompaniesFromValueOf)
		  .forEach(company -> Assertions.assertEquals(BoundProperty.DeserializationMethod.VALUEOF_STRING, company.deserializationMethod));
		
		Company[] arrayCompaniesFromDeserializer = typeCompany_array.getArrayCompanies_fromDeserializerString();
		Assertions.assertArrayEquals(arrayCompaniesFromConfig, arrayCompaniesFromDeserializer);
		Stream.of(arrayCompaniesFromDeserializer)
		  .forEach(company -> Assertions.assertEquals(BoundProperty.DeserializationMethod.DESERIALIZER_STRING, company.deserializationMethod));
	}
	
	@Test
	void test_getWithStringMixedWaysForList()
	{
		TypeCompany_list typeCompany_list = getSingleConfigInstance(TypeCompany_list.class);
		
		ArrayList<Company> listCompaniesFromConfig = new ArrayList<>(ComplexTypesTest.companiesInConfigFile);
		
		List<Company> listCompaniesFromConstructor = typeCompany_list.getListCompanies_fromConstructorString();
		Assertions.assertEquals(listCompaniesFromConfig, listCompaniesFromConstructor);
		listCompaniesFromConstructor
		  .forEach(company -> Assertions.assertEquals(BoundProperty.DeserializationMethod.CONSTRUCTOR_STRING, company.deserializationMethod));
		
		List<Company> listCompaniesFromValueOf = typeCompany_list.getListCompanies_fromValueOfString();
		Assertions.assertEquals(listCompaniesFromConfig, listCompaniesFromValueOf);
		listCompaniesFromValueOf
		  .forEach(company -> Assertions.assertEquals(BoundProperty.DeserializationMethod.VALUEOF_STRING, company.deserializationMethod));
		
		List<Company> listCompaniesFromDeserializer = typeCompany_list.getListCompanies_fromDeserializerString();
		Assertions.assertEquals(listCompaniesFromConfig, listCompaniesFromDeserializer);
		listCompaniesFromDeserializer
		  .forEach(company -> Assertions.assertEquals(BoundProperty.DeserializationMethod.DESERIALIZER_STRING, company.deserializationMethod));
	}
	
	@Test
	void test_getWithStringMixedWaysForSet()
	{
		TypeCompany_set typeCompany_set = getSingleConfigInstance(TypeCompany_set.class);
		
		HashSet<Company> setCompaniesFromConfig = new HashSet<>(ComplexTypesTest.companiesInConfigFile);
		
		Set<Company> setCompaniesFromConstructor = typeCompany_set.getSetCompanies_fromConstructorString();
		Assertions.assertEquals(setCompaniesFromConfig, setCompaniesFromConstructor);
		setCompaniesFromConstructor
		  .forEach(company -> Assertions.assertEquals(BoundProperty.DeserializationMethod.CONSTRUCTOR_STRING, company.deserializationMethod));
		
		Set<Company> setCompaniesFromValueOf = typeCompany_set.getSetCompanies_fromValueOfString();
		Assertions.assertEquals(setCompaniesFromConfig, setCompaniesFromValueOf);
		setCompaniesFromValueOf
		  .forEach(company -> Assertions.assertEquals(BoundProperty.DeserializationMethod.VALUEOF_STRING, company.deserializationMethod));
		
		Set<Company> setCompaniesFromDeserializer = typeCompany_set.getSetCompanies_fromDeserializerString();
		Assertions.assertEquals(setCompaniesFromConfig, setCompaniesFromDeserializer);
		setCompaniesFromDeserializer
		  .forEach(company -> Assertions.assertEquals(BoundProperty.DeserializationMethod.DESERIALIZER_STRING, company.deserializationMethod));
	}
	
	@Test
	void test_getWithStringMixedWaysForMap()
	{
		TypeCompany_map typeCompany_map = getSingleConfigInstance(TypeCompany_map.class);
		
		LinkedHashMap<String, Company> mapCompaniesFromConfig = new LinkedHashMap<>();
		ComplexTypesTest.companiesInConfigFile.forEach(company ->
		  mapCompaniesFromConfig.put(company.name, company)
		);
		
		Map<String, Company> mapCompaniesFromConstructor = typeCompany_map.getMapCompanies_fromConstructorString();
		Assertions.assertEquals(mapCompaniesFromConfig, mapCompaniesFromConstructor);
		mapCompaniesFromConstructor
		  .forEach((name, company) -> Assertions.assertEquals(BoundProperty.DeserializationMethod.CONSTRUCTOR_STRING, company.deserializationMethod));
		
		Map<String, Company> mapCompaniesFromValueOf = typeCompany_map.getMapCompanies_fromValueOfString();
		Assertions.assertEquals(mapCompaniesFromConfig, mapCompaniesFromValueOf);
		mapCompaniesFromValueOf
		  .forEach((name, company) -> Assertions.assertEquals(BoundProperty.DeserializationMethod.VALUEOF_STRING, company.deserializationMethod));
		
		Map<String, Company> mapCompaniesFromDeserializer = typeCompany_map.getMapCompanies_fromDeserializerString();
		Assertions.assertEquals(mapCompaniesFromConfig, mapCompaniesFromDeserializer);
		mapCompaniesFromDeserializer
		  .forEach((name, company) -> Assertions.assertEquals(BoundProperty.DeserializationMethod.DESERIALIZER_STRING, company.deserializationMethod));
	}
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


class Company implements IDeserializer<Company>
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
		company.emails = Arrays.stream(parts[1].split(";")).map(String::trim).toArray(String[]::new);
		company.phoneNumbers = Arrays.stream(parts[2].split(";")).map(String::trim).toArray(String[]::new);
		company.ceo = parts[3].trim();
		company.dateFoundation = Short.parseShort(parts[4].trim());
		company.authorizedCapital = Double.parseDouble(parts[5].trim());
	}
	
	private static void parseFromMap(Company company, Map<String, String> rawKeyValues)
	{
		company.name = rawKeyValues.get("name");
		company.emails = Arrays.stream(rawKeyValues.get("emailsList").split(";")).map(String::trim).toArray(String[]::new);
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
		  ", emailsList=" + Arrays.toString(emails) +
		  ", phoneNumbers=" + Arrays.toString(phoneNumbers) +
		  ", ceo='" + ceo + '\'' +
		  ", dateFoundation=" + dateFoundation +
		  ", authorizedСapital=" + authorizedCapital +
		  '}';
	}
}
