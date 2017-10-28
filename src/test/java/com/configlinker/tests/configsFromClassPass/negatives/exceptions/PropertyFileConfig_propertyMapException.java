package com.configlinker.tests.configsFromClassPass.negatives.exceptions;

import com.configlinker.annotations.BoundObject;
import com.configlinker.annotations.BoundProperty;

@BoundObject(sourcePath = "config_propertyMapException.properties")
public interface PropertyFileConfig_propertyMapException {

// task #26
@BoundProperty(name = "regex.pattern", regexpPattern = "?")
String getRegexPattern();

    //"{" - java.util.regex.PatternSyntaxException: Illegal repetition
    //"*" - java.util.regex.PatternSyntaxException: Dangling meta character '*' near index 0
    //"+" - java.util.regex.PatternSyntaxException: Dangling meta character '+' near index 0
    //"(" - java.util.regex.PatternSyntaxException: Unclosed group near index 1
    //"())" - java.util.regex.PatternSyntaxException: Unmatched closing ')' near index 1
    //"?" - java.util.regex.PatternSyntaxException: Dangling meta character '?' near index 0
    //"[" - java.util.regex.PatternSyntaxException: Unclosed character class near index 0
    //"\\" - java.util.regex.PatternSyntaxException: Unexpected internal error near index 1
}
