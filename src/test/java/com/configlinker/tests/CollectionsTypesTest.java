package com.configlinker.tests;


import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class CollectionsTypesTest extends AbstractBaseTest
{
	// ---------- arrays ----------
	
	@Test
	void test_arrayOfBooleans()
	{
		BooleanArray booleanArray = getSingleConfigInstance(BooleanArray.class);
		Assertions.assertArrayEquals(new boolean[]{false, true, false}, booleanArray.getValues());
	}
	
	@Test
	void test_arrayOfBytes()
	{
		ByteArray byteArray = getSingleConfigInstance(ByteArray.class);
		Assertions.assertArrayEquals(new byte[]{100, 23, -122, -80, 100}, byteArray.getValues());
	}
	
	@Test
	void test_arrayOfChars()
	{
		CharArray charArray = getSingleConfigInstance(CharArray.class);
		Assertions.assertArrayEquals(new char[]{'w', '8', 'h', '&', '\\', '*', 'h', '\u1234', 'ሴ'}, charArray.getValues());
	}
	
	@Test
	void test_arrayOfShorts()
	{
		ShortArray shortArray = getSingleConfigInstance(ShortArray.class);
		Assertions.assertArrayEquals(new short[]{22000, 3456, -18000, 32000, +32050, +3456}, shortArray.getValues());
	}
	
	@Test
	void test_arrayOfInts()
	{
		IntArray intArray = getSingleConfigInstance(IntArray.class);
		Assertions.assertArrayEquals(new int[]{123456098, 8479893, 981753, +1792364978, -2132364978, 981753}, intArray.getValues());
	}
	
	@Test
	void test_arrayOfLongs()
	{
		LongArray longArray = getSingleConfigInstance(LongArray.class);
		Assertions.assertArrayEquals(new long[]{22345778909876L, 1542375271000L, 1407110697000L, -1531375271200L, 22345778909876L}, longArray.getValues());
	}
	
	@Test
	void test_arrayOfFloats()
	{
		FloatArray floatArray = getSingleConfigInstance(FloatArray.class);
		Assertions.assertArrayEquals(new float[]{3.1415926545f, 2.0333f, 0.123456789f, 0.000003e-14f, -2.03311f, 3.1415926545f}, floatArray.getValues());
	}
	
	@Test
	void test_arrayOfDoubles()
	{
		DoubleArray doubleArray = getSingleConfigInstance(DoubleArray.class);
		Assertions.assertArrayEquals(new double[]{2.040336982365, 2.040336982365, 2.140382e-101, 98765.34567896567, 2.140382e+122}, doubleArray.getValues());
	}
	
	@Test
	void test_arrayOfStrings()
	{
		StringArray stringArray = getSingleConfigInstance(StringArray.class);
		Assertions.assertArrayEquals(new String[]{"horns.hooves@great.org", "director@great.org", "horns.hooves@great.org"}, stringArray.getValues());
	}
	
	@Test
	void test_arrayOfEnums()
	{
		NumberNameEnumArray enumArray = getSingleConfigInstance(NumberNameEnumArray.class);
		Assertions.assertArrayEquals(
		  new NumberName[]{NumberName.three, NumberName.two, NumberName.one, NumberName.five, NumberName.two},
		  enumArray.getValues());
	}
	
	
	// ---------- lists ----------
	
	@Test
	void test_listOfBooleans()
	{
		BooleanList booleanList = getSingleConfigInstance(BooleanList.class);
		ArrayList<Boolean> booleansExpected = new ArrayList<>();
		booleansExpected.add(false);
		booleansExpected.add(true);
		booleansExpected.add(false);
		Assertions.assertEquals(booleansExpected, booleanList.getValues());
	}
	
	@Test
	void test_listOfBytes()
	{
		ByteList byteList = getSingleConfigInstance(ByteList.class);
		ArrayList<Byte> bytesExpected = new ArrayList<>();
		bytesExpected.add((byte) 100);
		bytesExpected.add((byte) 23);
		bytesExpected.add((byte) -122);
		bytesExpected.add((byte) -80);
		bytesExpected.add((byte) 100);
		Assertions.assertEquals(bytesExpected, byteList.getValues());
	}
	
	@Test
	void test_listOfChars()
	{
		CharList charList = getSingleConfigInstance(CharList.class);
		ArrayList<Character> charsExpected = new ArrayList<>();
		charsExpected.add('w');
		charsExpected.add('8');
		charsExpected.add('h');
		charsExpected.add('&');
		charsExpected.add('\\');
		charsExpected.add('*');
		charsExpected.add('h');
		charsExpected.add('\u1234');
		charsExpected.add('ሴ');
		Assertions.assertEquals(charsExpected, charList.getValues());
	}
	
	@Test
	void test_listOfShorts()
	{
		ShortList shortList = getSingleConfigInstance(ShortList.class);
		ArrayList<Short> shortsExpected = new ArrayList<>();
		shortsExpected.add((short) 22000);
		shortsExpected.add((short) 3456);
		shortsExpected.add((short) -18000);
		shortsExpected.add((short) 32000);
		shortsExpected.add((short) +32050);
		shortsExpected.add((short) +3456);
		Assertions.assertEquals(shortsExpected, shortList.getValues());
	}
	
	@Test
	void test_listOfInts()
	{
		IntList intList = getSingleConfigInstance(IntList.class);
		ArrayList<Integer> intsExpected = new ArrayList<>();
		intsExpected.add(123456098);
		intsExpected.add(8479893);
		intsExpected.add(981753);
		intsExpected.add(+1792364978);
		intsExpected.add(-2132364978);
		intsExpected.add(981753);
		Assertions.assertEquals(intsExpected, intList.getValues());
	}
	
	@Test
	void test_listOfLongs()
	{
		LongList longList = getSingleConfigInstance(LongList.class);
		ArrayList<Long> longsExpeted = new ArrayList<>();
		longsExpeted.add(22345778909876L);
		longsExpeted.add(1542375271000L);
		longsExpeted.add(1407110697000L);
		longsExpeted.add(-1531375271200L);
		longsExpeted.add(22345778909876L);
		Assertions.assertEquals(longsExpeted, longList.getValues());
	}
	
	@Test
	void test_listOfFloats()
	{
		FloatList floatList = getSingleConfigInstance(FloatList.class);
		ArrayList<Float> floatsExpected = new ArrayList<>();
		floatsExpected.add(3.1415926545f);
		floatsExpected.add(2.0333f);
		floatsExpected.add(0.123456789f);
		floatsExpected.add(0.000003e-14f);
		floatsExpected.add(-2.03311f);
		floatsExpected.add(3.1415926545f);
		Assertions.assertEquals(floatsExpected, floatList.getValues());
	}
	
	@Test
	void test_listOfDoubles()
	{
		DoubleList doubleList = getSingleConfigInstance(DoubleList.class);
		ArrayList<Double> doublesExpected = new ArrayList<>();
		doublesExpected.add(2.040336982365);
		doublesExpected.add(2.040336982365);
		doublesExpected.add(2.140382e-101);
		doublesExpected.add(98765.34567896567);
		doublesExpected.add(2.140382e+122);
		Assertions.assertEquals(doublesExpected, doubleList.getValues());
	}
	
	@Test
	void test_listOfStrings()
	{
		StringList stringList = getSingleConfigInstance(StringList.class);
		ArrayList<String> stringsExpected = new ArrayList<>();
		stringsExpected.add("horns.hooves@great.org");
		stringsExpected.add("director@great.org");
		stringsExpected.add("horns.hooves@great.org");
		Assertions.assertEquals(stringsExpected, stringList.getValues());
	}
	
	@Test
	void test_listOfEnums()
	{
		NumberNameEnumList enumsList = getSingleConfigInstance(NumberNameEnumList.class);
		ArrayList<NumberName> enumsExpected = new ArrayList<>();
		enumsExpected.add(NumberName.three);
		enumsExpected.add(NumberName.two);
		enumsExpected.add(NumberName.one);
		enumsExpected.add(NumberName.five);
		enumsExpected.add(NumberName.two);
		Assertions.assertEquals(enumsExpected, enumsList.getValues());
	}
	
	
	// ---------- sets ----------
	
	@Test
	void test_setOfBooleans()
	{
		BooleanSet booleanSet = getSingleConfigInstance(BooleanSet.class);
		LinkedHashSet<Boolean> booleansExpected = new LinkedHashSet<>();
		booleansExpected.add(false);
		booleansExpected.add(true);
		booleansExpected.add(false);
		Assertions.assertEquals(booleansExpected, booleanSet.getValues());
	}
	
	@Test
	void test_setOfBytes()
	{
		ByteSet byteSet = getSingleConfigInstance(ByteSet.class);
		LinkedHashSet<Byte> bytesExpected = new LinkedHashSet<>();
		bytesExpected.add((byte) 100);
		bytesExpected.add((byte) 23);
		bytesExpected.add((byte) -122);
		bytesExpected.add((byte) -80);
		bytesExpected.add((byte) 100);
		Assertions.assertEquals(bytesExpected, byteSet.getValues());
	}
	
	@Test
	void test_setOfChars()
	{
		CharSet charSet = getSingleConfigInstance(CharSet.class);
		LinkedHashSet<Character> charsExpected = new LinkedHashSet<>();
		charsExpected.add('w');
		charsExpected.add('8');
		charsExpected.add('h');
		charsExpected.add('&');
		charsExpected.add('\\');
		charsExpected.add('*');
		charsExpected.add('h');
		charsExpected.add('\u1234');
		charsExpected.add('ሴ');
		Assertions.assertEquals(charsExpected, charSet.getValues());
	}
	
	@Test
	void test_setOfShorts()
	{
		ShortSet shortSet = getSingleConfigInstance(ShortSet.class);
		LinkedHashSet<Short> shortsExpected = new LinkedHashSet<>();
		shortsExpected.add((short) 22000);
		shortsExpected.add((short) 3456);
		shortsExpected.add((short) -18000);
		shortsExpected.add((short) 32000);
		shortsExpected.add((short) +32050);
		shortsExpected.add((short) +3456);
		Assertions.assertEquals(shortsExpected, shortSet.getValues());
	}
	
	@Test
	void test_setOfInts()
	{
		IntSet intSet = getSingleConfigInstance(IntSet.class);
		LinkedHashSet<Integer> intsExpected = new LinkedHashSet<>();
		intsExpected.add(123456098);
		intsExpected.add(8479893);
		intsExpected.add(981753);
		intsExpected.add(+1792364978);
		intsExpected.add(-2132364978);
		intsExpected.add(981753);
		Assertions.assertEquals(intsExpected, intSet.getValues());
	}
	
	@Test
	void test_setOfLongs()
	{
		LongSet longSet = getSingleConfigInstance(LongSet.class);
		LinkedHashSet<Long> longsExpeted = new LinkedHashSet<>();
		longsExpeted.add(22345778909876L);
		longsExpeted.add(1542375271000L);
		longsExpeted.add(1407110697000L);
		longsExpeted.add(-1531375271200L);
		longsExpeted.add(22345778909876L);
		Assertions.assertEquals(longsExpeted, longSet.getValues());
	}
	
	@Test
	void test_setOfFloats()
	{
		FloatSet floatSet = getSingleConfigInstance(FloatSet.class);
		LinkedHashSet<Float> floatsExpected = new LinkedHashSet<>();
		floatsExpected.add(3.1415926545f);
		floatsExpected.add(2.0333f);
		floatsExpected.add(0.123456789f);
		floatsExpected.add(0.000003e-14f);
		floatsExpected.add(-2.03311f);
		floatsExpected.add(3.1415926545f);
		Assertions.assertEquals(floatsExpected, floatSet.getValues());
	}
	
	@Test
	void test_setOfDoubles()
	{
		DoubleSet doubleSet = getSingleConfigInstance(DoubleSet.class);
		LinkedHashSet<Double> doublesExpected = new LinkedHashSet<>();
		doublesExpected.add(2.040336982365);
		doublesExpected.add(2.040336982365);
		doublesExpected.add(2.140382e-101);
		doublesExpected.add(98765.34567896567);
		doublesExpected.add(2.140382e+122);
		Assertions.assertEquals(doublesExpected, doubleSet.getValues());
	}
	
	@Test
	void test_setOfStrings()
	{
		StringSet stringSet = getSingleConfigInstance(StringSet.class);
		LinkedHashSet<String> stringsExpected = new LinkedHashSet<>();
		stringsExpected.add("horns.hooves@great.org");
		stringsExpected.add("director@great.org");
		stringsExpected.add("horns.hooves@great.org");
		Assertions.assertEquals(stringsExpected, stringSet.getValues());
	}
	
	@Test
	void test_setOfEnums()
	{
		NumberNameEnumSet enumsSet = getSingleConfigInstance(NumberNameEnumSet.class);
		LinkedHashSet<NumberName> enumsExpected = new LinkedHashSet<>();
		enumsExpected.add(NumberName.three);
		enumsExpected.add(NumberName.two);
		enumsExpected.add(NumberName.one);
		enumsExpected.add(NumberName.five);
		enumsExpected.add(NumberName.two);
		Assertions.assertEquals(enumsExpected, enumsSet.getValues());
	}
	
	@Test
	void test_sequenceOfElementsInSet()
	{
		IntSet intSet = getSingleConfigInstance(IntSet.class);
		
		LinkedHashSet<Integer> intsExpected = new LinkedHashSet<>();
		intsExpected.add(123456098);
		intsExpected.add(8479893);
		intsExpected.add(981753);
		intsExpected.add(+1792364978);
		intsExpected.add(-2132364978);
		intsExpected.add(981753);
		
		Assertions.assertArrayEquals(
		  intsExpected.toArray(new Integer[intsExpected.size()]),
		  intSet.getValues().toArray(new Integer[intSet.getValues().size()])
		);
	}
	
	
	// ---------- maps ----------
	
	@Test
	void test_mapOfBooleans()
	{
		BooleanMap booleanMap = getSingleConfigInstance(BooleanMap.class);
		LinkedHashMap<String, Boolean> booleansExpected = new LinkedHashMap<>();
		booleansExpected.put("boolean-1", false);
		booleansExpected.put("boolean-2", true);
		booleansExpected.put("boolean-3", false);
		Assertions.assertEquals(booleansExpected, booleanMap.getValues());
	}
	
	@Test
	void test_mapOfBytes()
	{
		ByteMap byteMap = getSingleConfigInstance(ByteMap.class);
		LinkedHashMap<String, Byte> bytesExpected = new LinkedHashMap<>();
		bytesExpected.put("byte-1", (byte) 100);
		bytesExpected.put("byte-2", (byte) 23);
		bytesExpected.put("byte-3", (byte) -122);
		bytesExpected.put("byte-4", (byte) -80);
		bytesExpected.put("byte-5", (byte) 100);
		Assertions.assertEquals(bytesExpected, byteMap.getValues());
	}
	
	@Test
	void test_mapOfChars()
	{
		CharMap charMap = getSingleConfigInstance(CharMap.class);
		LinkedHashMap<String, Character> charsExpected = new LinkedHashMap<>();
		charsExpected.put("char-1", 'w');
		charsExpected.put("char-2", '8');
		charsExpected.put("char-3", 'h');
		charsExpected.put("char-4", '&');
		charsExpected.put("char-5", '\\');
		charsExpected.put("char-6", '*');
		charsExpected.put("char-7", 'h');
		charsExpected.put("char-8", '\u1234');
		charsExpected.put("char-9", 'ሴ');
		Assertions.assertEquals(charsExpected, charMap.getValues());
	}
	
	@Test
	void test_mapOfShorts()
	{
		ShortMap shortMap = getSingleConfigInstance(ShortMap.class);
		LinkedHashMap<String, Short> shortsExpected = new LinkedHashMap<>();
		shortsExpected.put("short-1", (short) 22000);
		shortsExpected.put("short-2", (short) 3456);
		shortsExpected.put("short-3", (short) -18000);
		shortsExpected.put("short-4", (short) 32000);
		shortsExpected.put("short-5", (short) +32050);
		shortsExpected.put("short-6", (short) +3456);
		Assertions.assertEquals(shortsExpected, shortMap.getValues());
	}
	
	@Test
	void test_mapOfInts()
	{
		IntMap intMap = getSingleConfigInstance(IntMap.class);
		LinkedHashMap<String, Integer> intsExpected = new LinkedHashMap<>();
		intsExpected.put("int-1", 123456098);
		intsExpected.put("int-2", 8479893);
		intsExpected.put("int-3", 981753);
		intsExpected.put("int-4", +1792364978);
		intsExpected.put("int-5", -2132364978);
		intsExpected.put("int-6", 981753);
		Assertions.assertEquals(intsExpected, intMap.getValues());
	}
	
	@Test
	void test_mapOfLongs()
	{
		LongMap longMap = getSingleConfigInstance(LongMap.class);
		LinkedHashMap<String, Long> longsExpeted = new LinkedHashMap<>();
		longsExpeted.put("long-1", 22345778909876L);
		longsExpeted.put("long-2", 1542375271000L);
		longsExpeted.put("long-3", 1407110697000L);
		longsExpeted.put("long-4", -1531375271200L);
		longsExpeted.put("long-5", 22345778909876L);
		Assertions.assertEquals(longsExpeted, longMap.getValues());
	}
	
	@Test
	void test_mapOfFloats()
	{
		FloatMap floatMap = getSingleConfigInstance(FloatMap.class);
		LinkedHashMap<String, Float> floatsExpected = new LinkedHashMap<>();
		floatsExpected.put("float-1", 3.1415926545f);
		floatsExpected.put("float-2", 2.0333f);
		floatsExpected.put("float-3", 0.123456789f);
		floatsExpected.put("float-4", 0.000003e-14f);
		floatsExpected.put("float-5", -2.03311f);
		floatsExpected.put("float-6", 3.1415926545f);
		Assertions.assertEquals(floatsExpected, floatMap.getValues());
	}
	
	@Test
	void test_mapOfDoubles()
	{
		DoubleMap doubleMap = getSingleConfigInstance(DoubleMap.class);
		LinkedHashMap<String, Double> doublesExpected = new LinkedHashMap<>();
		doublesExpected.put("double-1", 2.040336982365);
		doublesExpected.put("double-2", 2.040336982365);
		doublesExpected.put("double-3", 2.140382e-101);
		doublesExpected.put("double-4", 98765.34567896567);
		doublesExpected.put("double-5", 2.140382e+122);
		Assertions.assertEquals(doublesExpected, doubleMap.getValues());
	}
	
	@Test
	void test_mapOfStrings()
	{
		StringMap stringMap = getSingleConfigInstance(StringMap.class);
		LinkedHashMap<String, String> stringsExpected = new LinkedHashMap<>();
		stringsExpected.put("string-1", "horns.hooves@great.org");
		stringsExpected.put("string-2", "director@great.org");
		stringsExpected.put("string-3", "horns.hooves@great.org");
		Assertions.assertEquals(stringsExpected, stringMap.getValues());
	}
	
	@Test
	void test_mapOfEnums()
	{
		NumberNameEnumMap enumsMap = getSingleConfigInstance(NumberNameEnumMap.class);
		LinkedHashMap<String, NumberName> enumsExpected = new LinkedHashMap<>();
		enumsExpected.put("enum-1", NumberName.three);
		enumsExpected.put("enum-2", NumberName.two);
		enumsExpected.put("enum-3", NumberName.one);
		enumsExpected.put("enum-4", NumberName.five);
		enumsExpected.put("enum-5", NumberName.two);
		Assertions.assertEquals(enumsExpected, enumsMap.getValues());
	}
	
	@Test
	void test_sequenceOfElementsInMap()
	{
		CharMap charMap = getSingleConfigInstance(CharMap.class);
		String[] charKeys = charMap.getValues().keySet().toArray(new String[charMap.getValues().size()]);
		Character[] charValues = charMap.getValues().values().toArray(new Character[charMap.getValues().size()]);
		
		LinkedHashMap<String, Character> charsExpected = new LinkedHashMap<>();
		charsExpected.put("char-1", 'w');
		charsExpected.put("char-2", '8');
		charsExpected.put("char-3", 'h');
		charsExpected.put("char-4", '&');
		charsExpected.put("char-5", '\\');
		charsExpected.put("char-6", '*');
		charsExpected.put("char-7", 'h');
		charsExpected.put("char-8", '\u1234');
		charsExpected.put("char-9", 'ሴ');
		String[] charKeysExpected = charsExpected.keySet().toArray(new String[charsExpected.size()]);
		Character[] charValuesExpected = charsExpected.values().toArray(new Character[charsExpected.size()]);
		
		Assertions.assertArrayEquals(charKeysExpected, charKeys);
		Assertions.assertArrayEquals(charValuesExpected, charValues);
	}
	
	
	@Test @Disabled("TODO: implement")
	void test_returnCopyOfArray()
	{
		// TODO: implement
	}
	
	@Test @Disabled("TODO: implement")
	void test_returnUnmodifiableList()
	{
		// TODO: implement
	}
	
	@Test @Disabled("TODO: implement")
	void test_returnUnmodifiableSet()
	{
		// TODO: implement
	}
	
	@Test @Disabled("TODO: implement")
	void test_returnUnmodifiableMap()
	{
		// TODO: implement
	}
}


// ---------- arrays ----------

@BoundObject(sourcePath = "configs/collections.properties")
interface BooleanArray
{
	@BoundProperty(name = "collection.of.boolean")
	boolean[] getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface ByteArray
{
	@BoundProperty(name = "collection.of.byte")
	byte[] getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface CharArray
{
	@BoundProperty(name = "collection.of.char")
	char[] getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface ShortArray
{
	@BoundProperty(name = "collection.of.short")
	short[] getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface IntArray
{
	@BoundProperty(name = "collection.of.int")
	int[] getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface LongArray
{
	@BoundProperty(name = "collection.of.long")
	long[] getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface FloatArray
{
	@BoundProperty(name = "collection.of.float")
	float[] getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface DoubleArray
{
	@BoundProperty(name = "collection.of.double")
	double[] getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface StringArray
{
	@BoundProperty(name = "collection.of.string")
	String[] getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface NumberNameEnumArray
{
	@BoundProperty(name = "collection.of.enum.numberNames")
	NumberName[] getValues();
}


// ---------- lists ----------

@BoundObject(sourcePath = "configs/collections.properties")
interface BooleanList
{
	@BoundProperty(name = "collection.of.boolean", customType = Boolean.class)
	List<Boolean> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface ByteList
{
	@BoundProperty(name = "collection.of.byte", customType = Byte.class)
	List<Byte> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface CharList
{
	@BoundProperty(name = "collection.of.char", customType = Character.class)
	List<Character> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface ShortList
{
	@BoundProperty(name = "collection.of.short", customType = Short.class)
	List<Short> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface IntList
{
	@BoundProperty(name = "collection.of.int", customType = Integer.class)
	List<Integer> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface LongList
{
	@BoundProperty(name = "collection.of.long", customType = Long.class)
	List<Long> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface FloatList
{
	@BoundProperty(name = "collection.of.float", customType = Float.class)
	List<Float> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface DoubleList
{
	@BoundProperty(name = "collection.of.double", customType = Double.class)
	List<Double> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface StringList
{
	@BoundProperty(name = "collection.of.string", customType = String.class)
	List<String> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface NumberNameEnumList
{
	@BoundProperty(name = "collection.of.enum.numberNames", customType = NumberName.class)
	List<NumberName> getValues();
}


// ---------- sets ----------

@BoundObject(sourcePath = "configs/collections.properties")
interface BooleanSet
{
	@BoundProperty(name = "collection.of.boolean", customType = Boolean.class)
	Set<Boolean> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface ByteSet
{
	@BoundProperty(name = "collection.of.byte", customType = Byte.class)
	Set<Byte> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface CharSet
{
	@BoundProperty(name = "collection.of.char", customType = Character.class)
	Set<Character> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface ShortSet
{
	@BoundProperty(name = "collection.of.short", customType = Short.class)
	Set<Short> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface IntSet
{
	@BoundProperty(name = "collection.of.int", customType = Integer.class)
	Set<Integer> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface LongSet
{
	@BoundProperty(name = "collection.of.long", customType = Long.class)
	Set<Long> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface FloatSet
{
	@BoundProperty(name = "collection.of.float", customType = Float.class)
	Set<Float> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface DoubleSet
{
	@BoundProperty(name = "collection.of.double", customType = Double.class)
	Set<Double> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface StringSet
{
	@BoundProperty(name = "collection.of.string", customType = String.class)
	Set<String> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface NumberNameEnumSet
{
	@BoundProperty(name = "collection.of.enum.numberNames", customType = NumberName.class)
	Set<NumberName> getValues();
}


// ---------- maps ----------

@BoundObject(sourcePath = "configs/collections.properties")
interface BooleanMap
{
	@BoundProperty(name = "map.of.boolean", customType = Boolean.class)
	Map<String, Boolean> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface ByteMap
{
	@BoundProperty(name = "map.of.byte", customType = Byte.class)
	Map<String, Byte> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface CharMap
{
	@BoundProperty(name = "map.of.char", customType = Character.class)
	Map<String, Character> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface ShortMap
{
	@BoundProperty(name = "map.of.short", customType = Short.class)
	Map<String,Short> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface IntMap
{
	@BoundProperty(name = "map.of.int", customType = Integer.class)
	Map<String,Integer> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface LongMap
{
	@BoundProperty(name = "map.of.long", customType = Long.class)
	Map<String,Long> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface FloatMap
{
	@BoundProperty(name = "map.of.float", customType = Float.class)
	Map<String,Float> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface DoubleMap
{
	@BoundProperty(name = "map.of.double", customType = Double.class)
	Map<String,Double> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface StringMap
{
	@BoundProperty(name = "map.of.string", customType = String.class)
	Map<String,String> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface NumberNameEnumMap
{
	@BoundProperty(name = "map.of.enum.numberNames", customType = NumberName.class)
	Map<String,NumberName> getValues();
}
