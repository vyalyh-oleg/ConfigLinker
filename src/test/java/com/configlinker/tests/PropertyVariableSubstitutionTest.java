package com.configlinker.tests;


import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;


@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class PropertyVariableSubstitutionTest extends AbstractBaseTest
{
	// propertyNamePrefix in BoundObject
	
	@Test
	void test1()
	{
	
	}
}


@BoundObject(sourcePath = "${substitution1}/path_part1/${substitution2}/path_part2/endPart")
interface PropertyWithPrefix1
{
	@BoundProperty(name = "ordinary.value")
	String value();
}

@BoundObject(sourcePath = "${substitution1}/path_part1/${substitution2}/path_part2/endPart")
interface PropertyWithPrefix2
{
	@BoundProperty(name = ".value")
	String value();
}

@BoundObject(sourcePath = "${substitution1}/path_part1/${substitution2}/path_part2/endPart")
interface PropertyWithPrefix3
{
	@BoundProperty(name = ".configuration.${type}.memory.limit")
	String value();
}


@BoundObject(sourcePath = "${substitution1}/path_part1/${substitution2}/path_part2/endPart")
interface PropertyWithPrefix4
{
	@BoundProperty(name = "configuration.${type}.@{group}.limit.@{border}")
	String value(String group, String border);
}
