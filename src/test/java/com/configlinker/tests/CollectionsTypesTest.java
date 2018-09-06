package com.configlinker.tests;


import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
		Assertions.assertArrayEquals(new char[]{'w', '8', 'h', '&', '\\', '*', 'h', '`'}, charArray.getValues());
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
		charsExpected.add('`');
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
		HashSet<Boolean> booleansExpected = new HashSet<>();
		booleansExpected.add(false);
		booleansExpected.add(true);
		booleansExpected.add(false);
		Assertions.assertEquals(booleansExpected, booleanSet.getValues());
	}
	
	@Test
	void test_setOfBytes()
	{
		ByteSet byteSet = getSingleConfigInstance(ByteSet.class);
		HashSet<Byte> bytesExpected = new HashSet<>();
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
		HashSet<Character> charsExpected = new HashSet<>();
		charsExpected.add('w');
		charsExpected.add('8');
		charsExpected.add('h');
		charsExpected.add('&');
		charsExpected.add('\\');
		charsExpected.add('*');
		charsExpected.add('h');
		charsExpected.add('`');
		Assertions.assertEquals(charsExpected, charSet.getValues());
	}
	
	@Test
	void test_setOfShorts()
	{
		ShortSet shortSet = getSingleConfigInstance(ShortSet.class);
		HashSet<Short> shortsExpected = new HashSet<>();
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
		HashSet<Integer> intsExpected = new HashSet<>();
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
		HashSet<Long> longsExpeted = new HashSet<>();
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
		HashSet<Float> floatsExpected = new HashSet<>();
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
		HashSet<Double> doublesExpected = new HashSet<>();
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
		HashSet<String> stringsExpected = new HashSet<>();
		stringsExpected.add("horns.hooves@great.org");
		stringsExpected.add("director@great.org");
		stringsExpected.add("horns.hooves@great.org");
		Assertions.assertEquals(stringsExpected, stringSet.getValues());
	}
	
	@Test
	void test_setOfEnums()
	{
		NumberNameEnumSet enumsSet = getSingleConfigInstance(NumberNameEnumSet.class);
		HashSet<NumberName> enumsExpected = new HashSet<>();
		enumsExpected.add(NumberName.three);
		enumsExpected.add(NumberName.two);
		enumsExpected.add(NumberName.one);
		enumsExpected.add(NumberName.five);
		enumsExpected.add(NumberName.two);
		Assertions.assertEquals(enumsExpected, enumsSet.getValues());
	}
	
	
	// ---------- maps ----------
	
	
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
	@BoundProperty(name = "collection.of.boolean", customTypeOrDeserializer = Boolean.class)
	List<Boolean> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface ByteList
{
	@BoundProperty(name = "collection.of.byte", customTypeOrDeserializer = Byte.class)
	List<Byte> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface CharList
{
	@BoundProperty(name = "collection.of.char", customTypeOrDeserializer = Character.class)
	List<Character> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface ShortList
{
	@BoundProperty(name = "collection.of.short", customTypeOrDeserializer = Short.class)
	List<Short> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface IntList
{
	@BoundProperty(name = "collection.of.int", customTypeOrDeserializer = Integer.class)
	List<Integer> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface LongList
{
	@BoundProperty(name = "collection.of.long", customTypeOrDeserializer = Long.class)
	List<Long> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface FloatList
{
	@BoundProperty(name = "collection.of.float", customTypeOrDeserializer = Float.class)
	List<Float> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface DoubleList
{
	@BoundProperty(name = "collection.of.double", customTypeOrDeserializer = Double.class)
	List<Double> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface StringList
{
	@BoundProperty(name = "collection.of.string", customTypeOrDeserializer = String.class)
	List<String> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface NumberNameEnumList
{
	@BoundProperty(name = "collection.of.enum.numberNames", customTypeOrDeserializer = NumberName.class)
	List<NumberName> getValues();
}


// ---------- sets ----------

@BoundObject(sourcePath = "configs/collections.properties")
interface BooleanSet
{
	@BoundProperty(name = "collection.of.boolean", customTypeOrDeserializer = Boolean.class)
	Set<Boolean> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface ByteSet
{
	@BoundProperty(name = "collection.of.byte", customTypeOrDeserializer = Byte.class)
	Set<Byte> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface CharSet
{
	@BoundProperty(name = "collection.of.char", customTypeOrDeserializer = Character.class)
	Set<Character> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface ShortSet
{
	@BoundProperty(name = "collection.of.short", customTypeOrDeserializer = Short.class)
	Set<Short> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface IntSet
{
	@BoundProperty(name = "collection.of.int", customTypeOrDeserializer = Integer.class)
	Set<Integer> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface LongSet
{
	@BoundProperty(name = "collection.of.long", customTypeOrDeserializer = Long.class)
	Set<Long> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface FloatSet
{
	@BoundProperty(name = "collection.of.float", customTypeOrDeserializer = Float.class)
	Set<Float> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface DoubleSet
{
	@BoundProperty(name = "collection.of.double", customTypeOrDeserializer = Double.class)
	Set<Double> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface StringSet
{
	@BoundProperty(name = "collection.of.string", customTypeOrDeserializer = String.class)
	Set<String> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface NumberNameEnumSet
{
	@BoundProperty(name = "collection.of.enum.numberNames", customTypeOrDeserializer = NumberName.class)
	Set<NumberName> getValues();
}


// ---------- maps ----------

