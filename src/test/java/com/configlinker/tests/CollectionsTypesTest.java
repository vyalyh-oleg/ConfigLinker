package com.configlinker.tests;


import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;


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
		NumberNameEnumArray numberNames = getSingleConfigInstance(NumberNameEnumArray.class);
		Assertions
		  .assertArrayEquals(new NumberName[]{NumberName.three, NumberName.two, NumberName.one, NumberName.five, NumberName.two}, numberNames.getValues());
	}
	
	
	// ---------- lists ----------
	
	
	
	
	// ---------- sets ----------
	
	
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
	@BoundProperty(name = "collection.of.boolean")
	List<Boolean> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface ByteList
{
	@BoundProperty(name = "collection.of.byte")
	List<Byte> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface CharList
{
	@BoundProperty(name = "collection.of.char")
	List<Character> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface ShortList
{
	@BoundProperty(name = "collection.of.short")
	List<Short> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface IntList
{
	@BoundProperty(name = "collection.of.int")
	List<Integer> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface LongList
{
	@BoundProperty(name = "collection.of.long")
	List<Long> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface FloatList
{
	@BoundProperty(name = "collection.of.float")
	List<Float> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface DoubleList
{
	@BoundProperty(name = "collection.of.double")
	List<Double> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface StringList
{
	@BoundProperty(name = "collection.of.string")
	List<String> getValues();
}

@BoundObject(sourcePath = "configs/collections.properties")
interface NumberNameEnumList
{
	@BoundProperty(name = "collection.of.enum.numberNames")
	List<NumberName> getValues();
}


// ---------- sets ----------

